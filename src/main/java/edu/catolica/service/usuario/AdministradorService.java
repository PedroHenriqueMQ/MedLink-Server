package edu.catolica.service.usuario;

import edu.catolica.dto.UsuarioProfissionalDTO;
import edu.catolica.exception.UsuarioInexistenteException;
import edu.catolica.model.AreaAtuacao;
import edu.catolica.model.enums.TipoUsuario;
import edu.catolica.model.enums.TurnoAtendimento;
import edu.catolica.model.Usuario;
import edu.catolica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class AdministradorService {
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public void cadastrarProfissional(UsuarioProfissionalDTO usuarioProfissionalDTO, String token) {
        var administrador = usuarioService.verificarRequisicao(token, TipoUsuario.ADMINISTRADOR);
        var clinica = administrador.getClinica();
        usuarioService.verificarEmailDuplicado(usuarioProfissionalDTO.email(), clinica);

        Usuario usuarioModel = new Usuario(
                clinica,
                usuarioProfissionalDTO.nome(),
                usuarioProfissionalDTO.email(),
                usuarioProfissionalDTO.senha(),
                usuarioProfissionalDTO.cpf(),
                usuarioProfissionalDTO.dataNascimento(),
                usuarioProfissionalDTO.areasAtuacao()
                        .stream().map(
                                (areaAtuacao) -> new AreaAtuacao(areaAtuacao.titulo(), areaAtuacao.descricao())
                        ).toList(),
                TipoUsuario.PROFISSIONAL,
                List.of(TurnoAtendimento.MATUTINO, TurnoAtendimento.VESPERTINO)
        );

        usuarioRepository.save(usuarioModel);
    }

    public void atualizarProfissional(UsuarioProfissionalDTO usuarioProfissionalDTO, String token) {
        var administrador = usuarioService.verificarRequisicao(token, TipoUsuario.ADMINISTRADOR);
        var clinica = administrador.getClinica();
        var usuario = usuarioRepository.findByClinicaIdAndEmail(clinica.getId(), usuarioProfissionalDTO.email())
                .orElseThrow(() -> new UsuarioInexistenteException(usuarioProfissionalDTO.email()));

        usuario.setCpf(usuarioProfissionalDTO.cpf());
        usuario.setDataNascimento(usuarioProfissionalDTO.dataNascimento());
        usuario.setNome(usuarioProfissionalDTO.nome());
        usuario.setEmail(usuarioProfissionalDTO.email());
        usuario.setSenha(usuarioProfissionalDTO.senha());
        usuario.setAreasAtuacao(usuarioProfissionalDTO.areasAtuacao()
                .stream().map(
                        (areaAtuacao) -> new AreaAtuacao(areaAtuacao.titulo(), areaAtuacao.descricao())
                ).collect(Collectors.toList()));

        usuarioRepository.save(usuario);
    }

    public void atualizarEstadoProfissional(String email, String token, boolean estadoInativo) {
        var administrador = usuarioService.verificarRequisicao(token, TipoUsuario.ADMINISTRADOR);
        var clinica = administrador.getClinica();
        var usuario = usuarioRepository.findByClinicaIdAndEmail(clinica.getId(), email)
                .orElseThrow(() -> new UsuarioInexistenteException(email));

        usuario.setInativo(estadoInativo);

        usuarioRepository.save(usuario);
    }
}
