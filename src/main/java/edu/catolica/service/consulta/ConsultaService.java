package edu.catolica.service.consulta;

import edu.catolica.dto.ConsultaDTO;
import edu.catolica.exception.consulta.ConsultaDuplicadaException;
import edu.catolica.exception.consulta.ConsultaInexistenteException;
import edu.catolica.exception.consulta.HorarioInvalidoConsultaException;
import edu.catolica.exception.usuario.UsuarioInvalidoException;
import edu.catolica.model.*;
import edu.catolica.model.enums.*;
import edu.catolica.repository.ConsultaRepository;
import edu.catolica.service.clinica.ClinicaService;
import edu.catolica.service.consulta.modo.*;
import edu.catolica.service.usuario.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ConsultaService {

    private final UsuarioService usuarioService;
    private final ClinicaService clinicaService;
    private final ProfissionalService profissionalService;
    private final ConsultaRepository consultaRepository;

    private final ModoDomiciliar modoDomiciliar;
    private final ModoPresencial modoPresencial;
    private final ModoTelemedicina modoTelemedicina;

    public String solicitarConsulta(String token, ConsultaDTO dto) {
        var paciente = usuarioService.verificarRequisicao(token, TipoUsuario.PACIENTE);
        var profissional = validarProfissional(dto.emailDestinatario());

        var clinica = usuarioService.verificarClinicaCoincidente(paciente.getEmail(), profissional.getEmail());
        validarDuplicidadeConsulta(profissional, paciente, clinica, dto);

        var consulta = definirModoConsulta(dtoToEntity(dto, paciente, profissional));
        var consultaEntity = consulta.processarConsulta(StatusConsulta.SOLICITADA);
        validarDisponibilidade(profissional.getId(), consultaEntity);

        consultaRepository.save(consultaEntity);
        return consulta.obterDetalhesConsulta(consultaEntity);
    }

    public String confirmarConsulta(String token, ConsultaDTO dto) {
        var profissional = usuarioService.verificarRequisicao(token, TipoUsuario.PROFISSIONAL);
        var paciente = validarPaciente(dto.emailDestinatario());

        var consulta = buscarConsultaExistente(profissional, paciente, dto);
        validarDisponibilidade(profissional.getId(), consulta);

        var domain = definirModoConsulta(consulta);
        domain.processarConsulta(StatusConsulta.SOLICITADA);

        consulta.setStatusConsulta(StatusConsulta.CONFIRMADA);
        consultaRepository.save(consulta);

        return domain.obterDetalhesConsulta(consulta);
    }

    public void cancelarConsultaMarcada(String token, ConsultaDTO dto) {
        var profissional = usuarioService.verificarRequisicao(token, TipoUsuario.PROFISSIONAL);
        var paciente = validarPaciente(dto.emailDestinatario());

        var consulta = buscarConsultaExistente(profissional, paciente, dto);
        processarCancelamento(consulta, dto, StatusConsulta.CONFIRMADA);
    }

    public void negarConsulta(String token, ConsultaDTO dto) {
        var profissional = usuarioService.verificarRequisicao(token, TipoUsuario.PROFISSIONAL);
        var paciente = validarPaciente(dto.emailDestinatario());

        var consulta = buscarConsultaExistente(profissional, paciente, dto);
        processarCancelamento(consulta, dto, StatusConsulta.SOLICITADA);
    }

    public void cancelarSolicitacao(String token, ConsultaDTO dto) {
        var paciente = usuarioService.verificarRequisicao(token, TipoUsuario.PACIENTE);
        var profissional = validarProfissional(dto.emailDestinatario());

        var consulta = buscarConsultaExistente(profissional, paciente, dto);
        processarCancelamento(consulta, dto, StatusConsulta.SOLICITADA);
    }

    @Transactional(readOnly = true)
    public List<Consulta> buscarTodasPorDataEProfissional(LocalDate data, Long profissionalId) {
        return consultaRepository.findAllByProfissionalIdAndDataConsulta(profissionalId, data);
    }

    private Usuario validarProfissional(String email) {
        var profissional = usuarioService.verificarUsuarioPeloEmail(email);
        if (profissional.getTipoUsuario() != TipoUsuario.PROFISSIONAL)
            throw new UsuarioInvalidoException(email);
        return profissional;
    }

    private Usuario validarPaciente(String email) {
        var paciente = usuarioService.verificarUsuarioPeloEmail(email);
        if (paciente.getTipoUsuario() != TipoUsuario.PACIENTE)
            throw new UsuarioInvalidoException(email);
        return paciente;
    }

    private void validarDuplicidadeConsulta(Usuario profissional, Usuario paciente, Clinica clinica, ConsultaDTO dto) {
        var existe = consultaRepository.findByProfissionalIdAndPacienteIdAndClinicaIdAndDataConsultaAndHoraInicio(
                profissional.getId(), paciente.getId(), clinica.getId(),
                dto.dataConsulta(), dto.horaInicio()
        ).isPresent();

        if (existe) throw new ConsultaDuplicadaException();
    }

    private Consulta buscarConsultaExistente(Usuario profissional, Usuario paciente, ConsultaDTO dto) {
        var clinica = usuarioService.verificarClinicaCoincidente(profissional.getEmail(), paciente.getEmail());
        return consultaRepository.findByProfissionalIdAndPacienteIdAndClinicaIdAndDataConsultaAndHoraInicio(
                profissional.getId(), paciente.getId(), clinica.getId(),
                dto.dataConsulta(), dto.horaInicio()
        ).orElseThrow(ConsultaInexistenteException::new);
    }

    private void validarDisponibilidade(Long profissionalId, Consulta consulta) {
        var agenda = buscarTodasPorDataEProfissional(consulta.getDataConsulta(), profissionalId);
        if (!profissionalService.analisarDisponibilidade(agenda, consulta.getHoraInicio(), consulta.getHoraFim())) {
            throw new HorarioInvalidoConsultaException();
        }
    }

    private void processarCancelamento(Consulta consulta, ConsultaDTO dto, StatusConsulta preStatus) {
        var domain = definirModoConsulta(consulta);
        domain.processarConsulta(preStatus);
        domain.validarJustificativa(dto.justificativa());

        consulta.setStatusConsulta(StatusConsulta.CANCELADA);
        consulta.setJustificativa(dto.justificativa());
        consultaRepository.save(consulta);
    }

    private Consulta dtoToEntity(ConsultaDTO dto, Usuario paciente, Usuario profissional) {
        var endereco = new Endereco(
                dto.endereco().numero(),
                dto.endereco().rua(),
                dto.endereco().bairro(),
                dto.endereco().cidade(),
                dto.endereco().estado()
        );

        return new Consulta(
                paciente,
                profissional,
                clinicaService.consultarClinicaExistente(dto.clinica()),
                endereco,
                dto.dataConsulta(),
                dto.horaInicio(),
                dto.horaFim(),
                dto.tipoConsulta(),
                dto.justificativa(),
                dto.statusConsulta()
        );
    }

    private ConsultaDomain definirModoConsulta(Consulta consulta) {
        return switch (consulta.getTipoConsulta()) {
            case DOMICILIAR -> new ConsultaDomain(modoDomiciliar, consulta);
            case PRESENCIAL -> new ConsultaDomain(modoPresencial, consulta);
            case TELEMEDICINA -> new ConsultaDomain(modoTelemedicina, consulta);
        };
    }
}
