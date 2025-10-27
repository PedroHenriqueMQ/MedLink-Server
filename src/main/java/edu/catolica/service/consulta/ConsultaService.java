package edu.catolica.service.consulta;

import edu.catolica.dto.ConsultaDTO;
import edu.catolica.exception.ConsultaDuplicadaException;
import edu.catolica.exception.ConsultaInexistenteException;
import edu.catolica.exception.HorarioInvalidoConsultaException;
import edu.catolica.exception.UsuarioInvalidoException;
import edu.catolica.model.Consulta;
import edu.catolica.model.Endereco;
import edu.catolica.model.Usuario;
import edu.catolica.model.enums.StatusConsulta;
import edu.catolica.model.enums.TipoUsuario;
import edu.catolica.repository.ConsultaRepository;
import edu.catolica.service.clinica.ClinicaService;
import edu.catolica.service.consulta.modo.ModoDomiciliar;
import edu.catolica.service.consulta.modo.ModoPresencial;
import edu.catolica.service.consulta.modo.ModoTelemedicina;
import edu.catolica.service.usuario.ProfissionalService;
import edu.catolica.service.usuario.UsuarioService;
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


    public String solicitarConsulta(String token, ConsultaDTO consultaDTO) {
        var paciente = usuarioService.verificarRequisicao(token, TipoUsuario.PACIENTE);
        var profissional = usuarioService.verificarUsuarioPeloEmail(consultaDTO.emailDestinatario());

        if (profissional.getTipoUsuario() != TipoUsuario.PROFISSIONAL)
            throw new UsuarioInvalidoException(profissional.getEmail());
        var clinica = usuarioService.verificarClinicaCoincidente(paciente.getEmail(), profissional.getEmail());

        var consultaJaExiste = consultaRepository.findByProfissionalIdAndPacienteIdAndClinicaIdAndDataConsultaAndHoraInicio(
                profissional.getId(), paciente.getId(), clinica.getId(),
                consultaDTO.dataConsulta(), consultaDTO.horaInicio()).isPresent();
        if (consultaJaExiste) throw new ConsultaDuplicadaException();

        var consultaDomain = definirModoConsulta(dtoToEntity(consultaDTO, paciente, profissional));
        var consultaEntity = consultaDomain.processarConsulta(StatusConsulta.SOLICITADA);
        var agendamentoProfissional = buscarTodasPorDataEProfissional(
                consultaEntity.getDataConsulta(), profissional.getId());

        if (!profissionalService.analisarDisponibilidade(
                agendamentoProfissional, consultaEntity.getHoraInicio(), consultaEntity.getHoraFim()))
            throw new HorarioInvalidoConsultaException();

        consultaRepository.save(consultaEntity);
        return consultaDomain.obterDetalhesConsulta(consultaEntity);
    }

    public String confirmarConsulta(String token, ConsultaDTO consultaDTO) {
        var profissional = usuarioService.verificarRequisicao(token, TipoUsuario.PROFISSIONAL);
        var paciente = usuarioService.verificarUsuarioPeloEmail(consultaDTO.emailDestinatario());

        if (paciente.getTipoUsuario() != TipoUsuario.PACIENTE)
            throw new UsuarioInvalidoException(consultaDTO.emailDestinatario());
        var clinica = usuarioService.verificarClinicaCoincidente(profissional.getEmail(), paciente.getEmail());
        var consulta = consultaRepository.findByProfissionalIdAndPacienteIdAndClinicaIdAndDataConsultaAndHoraInicio(
                profissional.getId(), paciente.getId(), clinica.getId(),
                consultaDTO.dataConsulta(), consultaDTO.horaInicio()).orElseThrow(ConsultaInexistenteException::new);

        var consultaDomain = definirModoConsulta(consulta);
        consultaDomain.processarConsulta(StatusConsulta.SOLICITADA);
        consulta.setStatusConsulta(StatusConsulta.CONFIRMADA);

        consultaRepository.save(consulta);
        return consultaDomain.obterDetalhesConsulta(consulta);
    }

    public void cancelarConsultaMarcada(String token, ConsultaDTO consultaDTO) {
        var profissional = usuarioService.verificarRequisicao(token, TipoUsuario.PROFISSIONAL);
        var paciente = usuarioService.verificarUsuarioPeloEmail(consultaDTO.emailDestinatario());

        if (paciente.getTipoUsuario() != TipoUsuario.PACIENTE)
            throw new UsuarioInvalidoException(consultaDTO.emailDestinatario());
        var clinica = usuarioService.verificarClinicaCoincidente(profissional.getEmail(), paciente.getEmail());
        var consulta = consultaRepository.findByProfissionalIdAndPacienteIdAndClinicaIdAndDataConsultaAndHoraInicio(
                profissional.getId(), paciente.getId(), clinica.getId(),
                consultaDTO.dataConsulta(), consultaDTO.horaInicio()).orElseThrow(ConsultaInexistenteException::new);

        var consultaDomain = definirModoConsulta(consulta);
        consultaDomain.processarConsulta(StatusConsulta.CONFIRMADA);
        consultaDomain.validarJustificativa(consultaDTO.justificativa());
        consulta.setStatusConsulta(StatusConsulta.CANCELADA);
        consulta.setJustificativa(consultaDTO.justificativa());

        consultaRepository.save(consulta);
    }

    public void negarConsulta(String token, ConsultaDTO consultaDTO) {
        var profissional = usuarioService.verificarRequisicao(token, TipoUsuario.PROFISSIONAL);
        var paciente = usuarioService.verificarUsuarioPeloEmail(consultaDTO.emailDestinatario());

        if (paciente.getTipoUsuario() != TipoUsuario.PACIENTE)
            throw new UsuarioInvalidoException(consultaDTO.emailDestinatario());
        var clinica = usuarioService.verificarClinicaCoincidente(profissional.getEmail(), paciente.getEmail());
        var consulta = consultaRepository.findByProfissionalIdAndPacienteIdAndClinicaIdAndDataConsultaAndHoraInicio(
                profissional.getId(), paciente.getId(), clinica.getId(),
                consultaDTO.dataConsulta(), consultaDTO.horaInicio()).orElseThrow(ConsultaInexistenteException::new);

        var consultaDomain = definirModoConsulta(consulta);
        consultaDomain.processarConsulta(StatusConsulta.SOLICITADA);
        consultaDomain.validarJustificativa(consultaDTO.justificativa());
        consulta.setStatusConsulta(StatusConsulta.CANCELADA);
        consulta.setJustificativa(consultaDTO.justificativa());

        consultaRepository.save(consulta);
    }

    public void cancelarSolicitacao(String token, ConsultaDTO consultaDTO) {
        var paciente = usuarioService.verificarRequisicao(token, TipoUsuario.PACIENTE);
        var profissional = usuarioService.verificarUsuarioPeloEmail(consultaDTO.emailDestinatario());

        if (profissional.getTipoUsuario() != TipoUsuario.PROFISSIONAL)
            throw new UsuarioInvalidoException(consultaDTO.emailDestinatario());
        var clinica = usuarioService.verificarClinicaCoincidente(paciente.getEmail(), profissional.getEmail());
        var consulta = consultaRepository.findByProfissionalIdAndPacienteIdAndClinicaIdAndDataConsultaAndHoraInicio(
                profissional.getId(), paciente.getId(), clinica.getId(),
                consultaDTO.dataConsulta(), consultaDTO.horaInicio()).orElseThrow(ConsultaInexistenteException::new);

        var consultaDomain = definirModoConsulta(consulta);
        consultaDomain.processarConsulta(StatusConsulta.SOLICITADA);
        consultaDomain.validarJustificativa(consultaDTO.justificativa());
        consulta.setStatusConsulta(StatusConsulta.CANCELADA);
        consulta.setJustificativa(consultaDTO.justificativa());

        consultaRepository.save(consulta);
    }

    @Transactional(readOnly = true)
    public List<Consulta> buscarTodasPorDataEProfissional (LocalDate data, Long profissionalId) {
        return consultaRepository.findAllByProfissionalIdAndDataConsulta(profissionalId, data);
    }

    private Consulta dtoToEntity(ConsultaDTO consultaDTO, Usuario paciente, Usuario profissional) {
        var endereco = new Endereco(
                consultaDTO.endereco().numero(),
                consultaDTO.endereco().rua(),
                consultaDTO.endereco().bairro(),
                consultaDTO.endereco().cidade(),
                consultaDTO.endereco().estado()
        );

        var consulta = new Consulta(
                paciente,
                profissional,
                clinicaService.consultarClinicaExistente(consultaDTO.clinica()),
                endereco,
                consultaDTO.dataConsulta(),
                consultaDTO.horaInicio(),
                consultaDTO.horaFim(),
                consultaDTO.tipoConsulta(),
                consultaDTO.justificativa(),
                consultaDTO.statusConsulta()
        );

        return consulta;
    }

    private ConsultaDomain definirModoConsulta(Consulta consulta) {
        return switch (consulta.getTipoConsulta()) {
            case DOMICILIAR -> new ConsultaDomain(modoDomiciliar, consulta);
            case PRESENCIAL -> new ConsultaDomain(modoPresencial, consulta);
            case TELEMEDICINA -> new ConsultaDomain(modoTelemedicina, consulta);
        };
    }
}
