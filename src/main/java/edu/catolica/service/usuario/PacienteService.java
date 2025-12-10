package edu.catolica.service.usuario;

import edu.catolica.mapper.UsuarioMapper;
import edu.catolica.model.enums.TipoUsuario;
import edu.catolica.service.clinica.ClinicaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.catolica.dto.request.UsuarioPacienteRequestDTO;
import edu.catolica.model.Usuario;
import edu.catolica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class PacienteService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final ClinicaService clinicaService;
    private final UsuarioMapper usuarioMapper;

    public String cadastrarPaciente(UsuarioPacienteRequestDTO usuarioPacienteRequestDTO) {
        var clinica = clinicaService.consultarClinicaExistente(usuarioPacienteRequestDTO.clinica());
        usuarioService.verificarEmailDuplicado(usuarioPacienteRequestDTO.email(), clinica);

        Usuario usuarioModel = usuarioMapper.toEntityPacienteCadastro(usuarioPacienteRequestDTO, clinica);

        var paciente = usuarioRepository.save(usuarioModel);
        return paciente.getTipoUsuario().toString();
    }
}
