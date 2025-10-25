package edu.catolica.service;

import edu.catolica.exception.CredenciaisInvalidasException;
import edu.catolica.infra.GerenciadorSessao;
import edu.catolica.model.TipoUsuario;
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
    private final GerenciadorSessao gerenciadorSessao = GerenciadorSessao.getInstancia();
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
            TipoUsuario.PACIENTE
        );

        usuarioRepository.save(usuarioModel);
    }

    public void login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!usuario.getSenha().equals(senha))
            throw new CredenciaisInvalidasException();

        gerenciadorSessao.login(email);
    }

    private void verificarEmailDuplicado(String email, Clinica clinica) {
        if (usuarioRepository.existsByClinicaId(clinica.getId()))
            throw new EmailDuplicadoException(email, clinica.getRazaoSocial());
    }
}
