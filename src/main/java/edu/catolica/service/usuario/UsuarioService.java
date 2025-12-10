package edu.catolica.service.usuario;

import edu.catolica.exception.clinica.UsuariosClinicasDistintasException;
import edu.catolica.exception.usuario.*;
import edu.catolica.infra.GerenciadorSessao;
import edu.catolica.model.Clinica;
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
    private final GerenciadorSessao gerenciadorSessao;
    private final UsuarioRepository usuarioRepository;

    public String login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!usuario.getSenha().equals(senha))
            throw new CredenciaisInvalidasException();

        if (usuario.getInativo())
            throw new AcessoNegadoException();

        gerenciadorSessao.login(email);
        return usuario.getTipoUsuario().toString();
    }

    public void verificarEmailDuplicado(String email, Clinica clinica) {
        var usuario = usuarioRepository.findByClinicaIdAndEmail(clinica.getId(), email);

        if (usuario.isPresent()) throw new EmailDuplicadoException(email, clinica.getRazaoSocial());
    }

    public Clinica verificarClinicaCoincidente(String emailRemetente, String emailDestinatario) {
        Usuario remetente = obterUsuarioPeloEmail(emailRemetente);
        Usuario destinatario = obterUsuarioPeloEmail(emailDestinatario);

        if (!remetente.getClinica().getId().equals(destinatario.getClinica().getId()))
            throw new UsuariosClinicasDistintasException();

        return remetente.getClinica();
    }

    public Usuario obterUsuarioPeloEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new UsuarioInexistenteException(email));
    }

    public List<String> obterRazaoSocialPeloEmail(String email) {
        var usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new UsuarioInexistenteException(email));

        return List.of(usuario.getClinica().getRazaoSocial());
    }
}
