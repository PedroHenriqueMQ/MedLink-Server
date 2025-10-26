package edu.catolica.service;

import edu.catolica.model.TipoUsuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.catolica.dto.UsuarioPacienteDTO;
import edu.catolica.model.Usuario;
import edu.catolica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class PacienteService {
    private final UsuarioService usuarioService;
    private final ClinicaService clinicaService;
    private final UsuarioRepository usuarioRepository;

    public void cadastrarPaciente(UsuarioPacienteDTO usuarioPacienteDTO) {
        var clinica = clinicaService.consultarClinicaExistente(usuarioPacienteDTO.clinica());
        usuarioService.verificarEmailDuplicado(usuarioPacienteDTO.email(), clinica);

        Usuario usuarioModel = new Usuario(
            clinica,
            usuarioPacienteDTO.nome(),
            usuarioPacienteDTO.email(),
            usuarioPacienteDTO.senha(),
            usuarioPacienteDTO.cpf(),
            usuarioPacienteDTO.dataNascimento(),
            null,
            TipoUsuario.PACIENTE
        );

        usuarioRepository.save(usuarioModel);
    }
}
