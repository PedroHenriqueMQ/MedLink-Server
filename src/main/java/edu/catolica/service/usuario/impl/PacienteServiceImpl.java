package edu.catolica.service.usuario.impl;

import edu.catolica.dto.response.ConsultaResponseDTO;
import edu.catolica.mapper.ConsultaMapper;
import edu.catolica.mapper.UsuarioMapper;
import edu.catolica.service.clinica.impl.ClinicaServiceImpl;
import edu.catolica.service.consulta.impl.ConsultaServiceImpl;
import edu.catolica.service.usuario.PacienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.catolica.dto.request.UsuarioPacienteRequestDTO;
import edu.catolica.model.Usuario;
import edu.catolica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class PacienteServiceImpl implements PacienteService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioServiceImpl usuarioServiceImpl;
    private final ClinicaServiceImpl clinicaServiceImpl;
    private final ConsultaServiceImpl consultaServiceImpl;
    private final UsuarioMapper usuarioMapper;
    private final ConsultaMapper consultaMapper;

    @Override
    public String cadastrarPaciente(UsuarioPacienteRequestDTO usuarioPacienteRequestDTO) {
        var clinica = clinicaServiceImpl.consultarClinicaExistente(usuarioPacienteRequestDTO.clinica());
        usuarioServiceImpl.verificarEmailDuplicado(usuarioPacienteRequestDTO.email(), clinica);

        Usuario usuarioModel = usuarioMapper.toEntityPacienteCadastro(usuarioPacienteRequestDTO, clinica);

        var paciente = usuarioRepository.save(usuarioModel);
        return paciente.getTipoUsuario().toString();
    }

    @Override
    public List<ConsultaResponseDTO> obterConsultasPeloEmail(String token) {
        var consultas = consultaServiceImpl.buscarConsultasPorEmailPaciente(token);

        return consultas.stream().map(consultaMapper::toResponseDTOParaPaciente).toList();
    }
}
