package edu.catolica.service.usuario;

import edu.catolica.exception.*;
import edu.catolica.infra.GerenciadorSessao;
import edu.catolica.model.Clinica;
import edu.catolica.model.enums.TipoUsuario;
import edu.catolica.model.Usuario;
import edu.catolica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UsuarioService {
    private final GerenciadorSessao gerenciadorSessao = GerenciadorSessao.getInstancia();
    private final UsuarioRepository usuarioRepository;

    public String login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!usuario.getSenha().equals(senha))
            throw new CredenciaisInvalidasException();

        gerenciadorSessao.login(email);
        return usuario.getTipoUsuario().toString();
    }

    public void verificarEmailDuplicado(String email, Clinica clinica) {
        var usuario = usuarioRepository.findByClinicaIdAndEmail(clinica.getId(), email);

        if (usuario.isPresent()) throw new EmailDuplicadoException(email, clinica.getRazaoSocial());
    }

    public Usuario verificarRequisicao(String email, TipoUsuario tipoUsuario) {
        if (!gerenciadorSessao.validarSessao(email))
            throw new AcessoNegadoException();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!usuario.getTipoUsuario().equals(tipoUsuario))
            throw new AcessoNegadoException();

        return usuario;
    }

    public void verificarAutoRequisicao(String email, String token) {
        if (!email.equals(token)) throw new AcessoNegadoException();
    }

    public Clinica verificarClinicaCoincidente(String emailRemetente, String emailDestinatario) {
        Usuario paciente = usuarioRepository.findByEmail(emailRemetente)
                .orElseThrow(() -> new UsuarioInexistenteException(emailRemetente));

        Usuario profissional = usuarioRepository.findByEmail(emailDestinatario)
                .orElseThrow(() -> new UsuarioInexistenteException(emailDestinatario));

        if (!paciente.getClinica().getId().equals(profissional.getClinica().getId()))
            throw new UsuariosClinicasDistintasException();

        return paciente.getClinica();
    }

    public Usuario verificarUsuarioPeloEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new UsuarioInexistenteException(email));
    }

    public List<String> obterRazaoSocialPeloEmail(String email) {
        var usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new UsuarioInexistenteException(email));

        return List.of(usuario.getClinica().getRazaoSocial());
    }
}
