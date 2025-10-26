package edu.catolica.service;

import edu.catolica.model.TipoUsuario;
import edu.catolica.model.TurnoAtendimento;
import edu.catolica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ProfissionalService {
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public void atualizarTurnosAtendimento(String email, String token, List<TurnoAtendimento> turnosAtendimento) {
        var profissional = usuarioService.validarRequisicao(token, TipoUsuario.PROFISSIONAL);
        var clinica = profissional.getClinica();
        var usuario = usuarioService.verificarEmailDuplicado(email, clinica);
        usuarioService.validarAutoRequisicao(email, token);

        usuario.setTurnosAtendimento(turnosAtendimento);
        usuarioRepository.save(usuario);
    }
}
