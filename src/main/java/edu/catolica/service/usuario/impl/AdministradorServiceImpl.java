package edu.catolica.service.usuario.impl;

import edu.catolica.dto.request.ProfissionalRequestDTO;
import edu.catolica.exception.clinica.UsuariosClinicasDistintasException;
import edu.catolica.mapper.UsuarioMapper;
import edu.catolica.model.AreaAtuacao;
import edu.catolica.model.Usuario;
import edu.catolica.repository.UsuarioRepository;
import edu.catolica.service.usuario.AdministradorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class AdministradorServiceImpl implements AdministradorService {
    private final UsuarioServiceImpl usuarioServiceImpl;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public void cadastrarProfissional(ProfissionalRequestDTO profissionalRequestDTO, String token) {
        var administrador = usuarioServiceImpl.obterUsuarioPeloEmail(token);
        var clinica = administrador.getClinica();
        usuarioServiceImpl.verificarEmailDuplicado(profissionalRequestDTO.email(), clinica);

        if (!profissionalRequestDTO.clinica().equals(clinica.getRazaoSocial()))
            throw new UsuariosClinicasDistintasException();

        Usuario usuarioModel = usuarioMapper.toEntityProfissionalCadastro(profissionalRequestDTO, clinica);

        usuarioRepository.save(usuarioModel);
    }

    @Override
    public void atualizarProfissional(ProfissionalRequestDTO profissionalRequestDTO, String token) {
        var administrador = usuarioServiceImpl.obterUsuarioPeloEmail(token);
        var usuario = usuarioServiceImpl.obterUsuarioPeloEmail(profissionalRequestDTO.email());
        usuarioServiceImpl.verificarClinicaCoincidente(administrador.getEmail(), profissionalRequestDTO.email());

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
