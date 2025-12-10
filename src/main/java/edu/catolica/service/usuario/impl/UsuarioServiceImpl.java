package edu.catolica.service.usuario.impl;

import edu.catolica.exception.clinica.UsuariosClinicasDistintasException;
import edu.catolica.exception.usuario.*;
import edu.catolica.infra.GerenciadorSessao;
import edu.catolica.model.Clinica;
import edu.catolica.model.Usuario;
import edu.catolica.repository.UsuarioRepository;
import edu.catolica.service.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final GerenciadorSessao gerenciadorSessao;
    private final UsuarioRepository usuarioRepository;

    @Override
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

    @Override
    public void verificarEmailDuplicado(String email, Clinica clinica) {
        var usuario = usuarioRepository.findByClinicaIdAndEmail(clinica.getId(), email);

        if (usuario.isPresent()) throw new EmailDuplicadoException(email, clinica.getRazaoSocial());
    }

    @Override
    public Clinica verificarClinicaCoincidente(String emailRemetente, String emailDestinatario) {
        Usuario remetente = obterUsuarioPeloEmail(emailRemetente);
        Usuario destinatario = obterUsuarioPeloEmail(emailDestinatario);

        if (!remetente.getClinica().getId().equals(destinatario.getClinica().getId()))
            throw new UsuariosClinicasDistintasException();

        return remetente.getClinica();
    }

    @Override
    public Usuario obterUsuarioPeloEmail(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new UsuarioInexistenteException(email));
    }

    @Override
    public List<String> obterRazaoSocialPeloEmail(String email) {
        var usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new UsuarioInexistenteException(email));

        return List.of(usuario.getClinica().getRazaoSocial());
    }
}
