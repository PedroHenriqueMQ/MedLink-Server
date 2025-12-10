package edu.catolica.service.usuario;

import edu.catolica.dto.request.ProfissionalRequestDTO;
import edu.catolica.exception.clinica.UsuariosClinicasDistintasException;
import edu.catolica.mapper.UsuarioMapper;
import edu.catolica.model.AreaAtuacao;
import edu.catolica.model.Usuario;
import edu.catolica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class AdministradorService {
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public void cadastrarProfissional(ProfissionalRequestDTO profissionalRequestDTO, String token) {
        var administrador = usuarioService.obterUsuarioPeloEmail(token);
        var clinica = administrador.getClinica();
        usuarioService.verificarEmailDuplicado(profissionalRequestDTO.email(), clinica);

        if (!profissionalRequestDTO.clinica().equals(clinica.getRazaoSocial()))
            throw new UsuariosClinicasDistintasException();

        Usuario usuarioModel = usuarioMapper.toEntityProfissionalCadastro(profissionalRequestDTO, clinica);

        usuarioRepository.save(usuarioModel);
    }

    public void atualizarProfissional(ProfissionalRequestDTO profissionalRequestDTO, String token) {
        var administrador = usuarioService.obterUsuarioPeloEmail(token);
        var usuario = usuarioService.obterUsuarioPeloEmail(profissionalRequestDTO.email());
        usuarioService.verificarClinicaCoincidente(administrador.getEmail(), profissionalRequestDTO.email());

        usuario.setCpf(profissionalRequestDTO.cpf());
        usuario.setDataNascimento(profissionalRequestDTO.dataNascimento());
        usuario.setNome(profissionalRequestDTO.nome());
        usuario.setEmail(profissionalRequestDTO.email());
        usuario.setSenha(profissionalRequestDTO.senha());
        usuario.setAreasAtuacao(profissionalRequestDTO.areasAtuacao()
                .stream().map(
                        (areaAtuacao) -> new AreaAtuacao(areaAtuacao.titulo(), areaAtuacao.descricao())
                ).collect(Collectors.toList()));
        usuario.setTurnosAtendimento(profissionalRequestDTO.turnosAtendimento());
        usuario.setInativo(profissionalRequestDTO.inativo());

        usuarioRepository.save(usuario);
    }
}
