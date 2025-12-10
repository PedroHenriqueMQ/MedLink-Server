package edu.catolica.service.consulta.impl;

import edu.catolica.dto.request.ConsultaRequestDTO;
import edu.catolica.dto.response.ConsultaResponseDTO;
import edu.catolica.exception.consulta.ConsultaDuplicadaException;
import edu.catolica.exception.consulta.ConsultaInexistenteException;
import edu.catolica.exception.consulta.StatusInvalidoConsultaException;
import edu.catolica.exception.consulta.JustificativaAusenteException;
import edu.catolica.exception.usuario.UsuarioInvalidoException;
import edu.catolica.mapper.ConsultaMapper;
import edu.catolica.model.*;
import edu.catolica.model.enums.*;
import edu.catolica.repository.ConsultaRepository;
import edu.catolica.service.consulta.ConsultaService;
import edu.catolica.service.usuario.impl.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ConsultaServiceImpl implements ConsultaService {
    private final UsuarioServiceImpl usuarioServiceImpl;
    private final ConsultaRepository consultaRepository;
    private final ConsultaMapper consultaMapper;

    @Override
    public void solicitarConsulta(String token, ConsultaRequestDTO consultaRequestDTO) {
        if (consultaRequestDTO.statusConsulta() != StatusConsulta.SOLICITADA)
            throw new StatusInvalidoConsultaException();

        var paciente = usuarioServiceImpl.obterUsuarioPeloEmail(token);
        var profissional = validarProfissional(consultaRequestDTO.emailDestinatario());
        var clinica = usuarioServiceImpl.verificarClinicaCoincidente(paciente.getEmail(), profissional.getEmail());

        validarDisponibilidadeProfissional(profissional, consultaRequestDTO);
        validarDisponibilidadePaciente(paciente, consultaRequestDTO);

        var consulta = consultaMapper.toEntity(consultaRequestDTO, paciente, profissional, clinica);

        consultaRepository.save(consulta);
    }

    @Override
    public ConsultaResponseDTO confirmarConsulta(Long id) {
        var consulta = consultaRepository.findById(id).orElseThrow(ConsultaInexistenteException::new);

        consulta.setStatusConsulta(StatusConsulta.CONFIRMADA);

        consultaRepository.save(consulta);

        return consultaMapper.toResponseDTOParaProfissional(consulta);
    }

    @Override
    public ConsultaResponseDTO negarConsulta(Long id, String justificativa) {
        if (justificativa == null || justificativa.isBlank()) throw new JustificativaAusenteException();

        var consulta = consultaRepository.findById(id).orElseThrow(ConsultaInexistenteException::new);

        consulta.setStatusConsulta(StatusConsulta.CANCELADA);
        consulta.setJustificativa(justificativa);

        consultaRepository.save(consulta);

        return consultaMapper.toResponseDTOParaProfissional(consulta);
    }

    @Override
    public List<Consulta> buscarConsultasPorEmailProfissional(String email) {
        var profissional = usuarioServiceImpl.obterUsuarioPeloEmail(email);
        return consultaRepository.findAllByProfissionalId(profissional.getId());
    }

    @Override
    public List<Consulta> buscarConsultasPorEmailPaciente(String token) {
        var profissional = usuarioServiceImpl.obterUsuarioPeloEmail(token);
        return consultaRepository.findAllByPacienteId(profissional.getId());
    }

    private Usuario validarProfissional(String email) {
        var profissional = usuarioServiceImpl.obterUsuarioPeloEmail(email);
        if (profissional.getTipoUsuario() != TipoUsuario.PROFISSIONAL)
            throw new UsuarioInvalidoException(email);
        return profissional;
    }

    private void validarDisponibilidadeProfissional(Usuario profissional, ConsultaRequestDTO dto) {
        var consultaExistente = consultaRepository.findByProfissionalIdAndDataConsultaAndHoraInicio(
                profissional.getId(),
                dto.dataConsulta(),
                dto.horaInicio()
        );

        if (consultaExistente.isPresent() && consultaExistente.get().getStatusConsulta() != StatusConsulta.CANCELADA)
            throw new ConsultaDuplicadaException();
    }

    private void validarDisponibilidadePaciente(Usuario paciente, ConsultaRequestDTO dto) {
        var consultaExistente = consultaRepository.findByPacienteIdAndDataConsultaAndHoraInicio(
                paciente.getId(),
                dto.dataConsulta(),
                dto.horaInicio()
        );

        if (consultaExistente.isPresent() && consultaExistente.get().getStatusConsulta() != StatusConsulta.CANCELADA)
            throw new ConsultaDuplicadaException();
    }
}
