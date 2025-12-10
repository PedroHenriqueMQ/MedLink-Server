package edu.catolica.service.usuario.impl;

import edu.catolica.mapper.UsuarioMapper;
import edu.catolica.service.clinica.impl.ClinicaServiceImpl;
import edu.catolica.service.usuario.PacienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.catolica.dto.request.UsuarioPacienteRequestDTO;
import edu.catolica.model.Usuario;
import edu.catolica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class PacienteServiceImpl implements PacienteService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioServiceImpl usuarioServiceImpl;
    private final ClinicaServiceImpl clinicaServiceImpl;
    private final UsuarioMapper usuarioMapper;

    @Override
    public String cadastrarPaciente(UsuarioPacienteRequestDTO usuarioPacienteRequestDTO) {
        var clinica = clinicaServiceImpl.consultarClinicaExistente(usuarioPacienteRequestDTO.clinica());
        usuarioServiceImpl.verificarEmailDuplicado(usuarioPacienteRequestDTO.email(), clinica);

        Usuario usuarioModel = usuarioMapper.toEntityPacienteCadastro(usuarioPacienteRequestDTO, clinica);

        var paciente = usuarioRepository.save(usuarioModel);
        return paciente.getTipoUsuario().toString();
    }
}
