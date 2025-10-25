package edu.catolica.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.catolica.dto.UsuarioPacienteDTO;
import edu.catolica.exception.EmailDuplicadoException;
import edu.catolica.model.Clinica;
import edu.catolica.model.Usuario;
import edu.catolica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Transactional
@RequiredArgsConstructor
@Service
public class PacienteService {
    private final ClinicaService clinicaService;
    private final UsuarioRepository usuarioRepository;

    public void cadastrarPaciente(UsuarioPacienteDTO usuarioPacienteDTO) {
        var clinica = clinicaService.consultarClinicaExistente(usuarioPacienteDTO.clinica());
        verificarEmailDuplicado(usuarioPacienteDTO.email(), clinica);

        Usuario usuarioModel = new Usuario(
            clinica,
            usuarioPacienteDTO.nome(),
            usuarioPacienteDTO.email(),
            usuarioPacienteDTO.senha(),
            usuarioPacienteDTO.cpf(),
            usuarioPacienteDTO.dataNascimento(),
            null,
            edu.catolica.model.TipoUsuario.PACIENTE
        );

        usuarioRepository.save(usuarioModel);
    }

    private void verificarEmailDuplicado(String email, Clinica clinica) {
        if (usuarioRepository.existsByClinicaId(clinica.getId()))
            throw new EmailDuplicadoException(email, clinica.getRazaoSocial());
    }
}
