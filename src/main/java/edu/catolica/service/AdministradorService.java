package edu.catolica.service;

import edu.catolica.dto.UsuarioProfissionalDTO;
import edu.catolica.model.AreaAtuacao;
import edu.catolica.model.TipoUsuario;
import edu.catolica.model.Usuario;
import edu.catolica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class AdministradorService {
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public void cadastrarProfissional(UsuarioProfissionalDTO usuarioProfissionalDTO, String token) {
        var administrador = usuarioService.validarRequisicaoUsuario(token, TipoUsuario.ADMINISTRADOR);
        var clinica = administrador.getClinica();
        usuarioService.verificarEmailDuplicado(usuarioProfissionalDTO.email(), clinica);

        Usuario usuarioModel = new Usuario(
                clinica,
                usuarioProfissionalDTO.nome(),
                usuarioProfissionalDTO.email(),
                usuarioProfissionalDTO.senha(),
                usuarioProfissionalDTO.cpf(),
                usuarioProfissionalDTO.dataNascimento(),
                new AreaAtuacao(
                        usuarioProfissionalDTO.tituloAreaAtuacao(),
                        usuarioProfissionalDTO.descricaoAreaAtuacao()
                ),
                TipoUsuario.PROFISSIONAL
        );

        usuarioRepository.save(usuarioModel);
    }

    public void atualizarProfissional(UsuarioProfissionalDTO usuarioProfissionalDTO, String token) {
        var administrador = usuarioService.validarRequisicaoUsuario(token, TipoUsuario.ADMINISTRADOR);
        var clinica = administrador.getClinica();
        var usuario = usuarioService.verificarEmailDuplicado(usuarioProfissionalDTO.email(), clinica);

        usuario.setCpf(usuarioProfissionalDTO.cpf());
        usuario.setDataNascimento(usuarioProfissionalDTO.dataNascimento());
        usuario.setNome(usuarioProfissionalDTO.nome());
        usuario.setEmail(usuarioProfissionalDTO.email());
        usuario.setSenha(usuarioProfissionalDTO.senha());
        usuario.getAreaAtuacao().get(0).setDescricao(usuarioProfissionalDTO.descricaoAreaAtuacao());
        usuario.getAreaAtuacao().get(0).setTitulo(usuarioProfissionalDTO.tituloAreaAtuacao());

        usuarioRepository.save(usuario);
    }

    public void atualizarEstadoProfissional(String email, String token, boolean estadoInativo) {
        var administrador = usuarioService.validarRequisicaoUsuario(token, TipoUsuario.ADMINISTRADOR);
        var clinica = administrador.getClinica();
        var usuario = usuarioService.verificarEmailDuplicado(email, clinica);

        usuario.setInativo(estadoInativo);

        usuarioRepository.save(usuario);
    }
}
