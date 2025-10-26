package edu.catolica.service;

import edu.catolica.exception.AcessoNegadoException;
import edu.catolica.exception.CredenciaisInvalidasException;
import edu.catolica.exception.EmailDuplicadoException;
import edu.catolica.exception.SessaoExpiradaException;
import edu.catolica.infra.GerenciadorSessao;
import edu.catolica.model.Clinica;
import edu.catolica.model.TipoUsuario;
import edu.catolica.model.Usuario;
import edu.catolica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UsuarioService {
    private final GerenciadorSessao gerenciadorSessao = GerenciadorSessao.getInstancia();
    private final UsuarioRepository usuarioRepository;

    public void login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!usuario.getSenha().equals(senha))
            throw new CredenciaisInvalidasException();

        gerenciadorSessao.login(email);
    }

    protected Usuario verificarEmailDuplicado(String email, Clinica clinica) {
        var usuario = usuarioRepository.findByClinicaIdAndEmail(clinica.getId(), email);

        if (usuario.isEmpty()) throw new EmailDuplicadoException(email, clinica.getRazaoSocial());

        return usuario.get();
    }

    protected Usuario validarRequisicaoUsuario(String email, TipoUsuario tipoUsuario) {
        if (!gerenciadorSessao.validarSessao(email))
            throw new SessaoExpiradaException();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(CredenciaisInvalidasException::new);

        if (!usuario.getTipoUsuario().equals(tipoUsuario))
            throw new AcessoNegadoException();

        return usuario;
    }
}
