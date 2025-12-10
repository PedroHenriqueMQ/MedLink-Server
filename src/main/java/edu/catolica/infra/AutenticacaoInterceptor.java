package edu.catolica.infra;

import edu.catolica.exception.usuario.AcessoNegadoException;
import edu.catolica.model.Usuario;
import edu.catolica.model.enums.TipoUsuario;
import edu.catolica.service.usuario.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AutenticacaoInterceptor implements HandlerInterceptor {
    private final UsuarioService usuarioService;
    private final GerenciadorSessao gerenciadorSessao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        if (path.startsWith("/profissionais") && "GET".equals(method)) return true;
        if (path.startsWith("/usuarios/")) return true;
        if ("OPTIONS".equals(method)) return true;

        String token = request.getHeader("token");

        if (token == null || token.trim().isEmpty()) throw new AcessoNegadoException();

        Usuario usuario = usuarioService.obterUsuarioPeloEmail(token);

        if (usuario.getInativo()) throw new AcessoNegadoException();

        gerenciadorSessao.validarSessao(usuario.getEmail());

        if (!validarAcesso(path, usuario.getTipoUsuario())) throw new AcessoNegadoException();

        request.setAttribute("usuarioLogado", usuario);

        return true;
    }

    private boolean validarAcesso(String path, TipoUsuario tipoUsuario) {
        if (path.contains("/administradores")) {
            return tipoUsuario == TipoUsuario.ADMINISTRADOR;
        }

        if (path.contains("/pacientes")) {
            return tipoUsuario == TipoUsuario.PACIENTE;
        }

        if (path.contains("/profissionais")) {
            return tipoUsuario == TipoUsuario.PROFISSIONAL;
        }

        return true;
    }
}
