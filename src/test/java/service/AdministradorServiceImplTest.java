package service;

import edu.catolica.dto.request.AreaAtuacaoRequestDTO;
import edu.catolica.dto.request.ProfissionalRequestDTO;
import edu.catolica.exception.clinica.UsuariosClinicasDistintasException;
import edu.catolica.mapper.UsuarioMapper;
import edu.catolica.model.Clinica;
import edu.catolica.model.Usuario;
import edu.catolica.repository.UsuarioRepository;
import edu.catolica.service.usuario.impl.AdministradorServiceImpl;
import edu.catolica.service.usuario.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdministradorServiceImplTest {

    @Mock
    private UsuarioServiceImpl usuarioServiceImpl;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private AdministradorServiceImpl administradorService;

    @Test
    void deveCadastrarProfissionalComSucesso() {
        String token = "admin@clinica.com";
        String razaoSocial = "Clinica Saude";

        Clinica clinica = new Clinica();
        clinica.setRazaoSocial(razaoSocial);

        Usuario administrador = new Usuario();
        administrador.setEmail(token);
        administrador.setClinica(clinica);

        ProfissionalRequestDTO dto = new ProfissionalRequestDTO(
                razaoSocial,
                "Dr. Teste",
                "profissional@clinica.com",
                "senha123",
                "12345678900",
                LocalDate.of(1990, 1, 1),
                Collections.emptyList(),
                Collections.emptyList(),
                false
        );

        Usuario novoUsuario = new Usuario();

        when(usuarioServiceImpl.obterUsuarioPeloEmail(token)).thenReturn(administrador);
        when(usuarioMapper.toEntityProfissionalCadastro(dto, clinica)).thenReturn(novoUsuario);

        administradorService.cadastrarProfissional(dto, token);

        verify(usuarioServiceImpl).obterUsuarioPeloEmail(token);
        verify(usuarioServiceImpl).verificarEmailDuplicado(dto.email(), clinica);
        verify(usuarioMapper).toEntityProfissionalCadastro(dto, clinica);
        verify(usuarioRepository).save(novoUsuario);
    }

    @Test
    void deveLancarExcecaoAoCadastrarProfissionalComClinicaDistinta() {
        String token = "admin@clinica.com";
        String razaoSocialAdmin = "Clinica A";
        String razaoSocialDto = "Clinica B";

        Clinica clinica = new Clinica();
        clinica.setRazaoSocial(razaoSocialAdmin);

        Usuario administrador = new Usuario();
        administrador.setEmail(token);
        administrador.setClinica(clinica);

        ProfissionalRequestDTO dto = new ProfissionalRequestDTO(
                razaoSocialDto,
                "Dr. Teste",
                "profissional@clinica.com",
                "senha123",
                "12345678900",
                LocalDate.now(),
                Collections.emptyList(),
                Collections.emptyList(),
                false
        );

        when(usuarioServiceImpl.obterUsuarioPeloEmail(token)).thenReturn(administrador);

        assertThrows(UsuariosClinicasDistintasException.class, () ->
                administradorService.cadastrarProfissional(dto, token)
        );

        verify(usuarioServiceImpl).obterUsuarioPeloEmail(token);
        verify(usuarioServiceImpl).verificarEmailDuplicado(dto.email(), clinica);
        verify(usuarioMapper, never()).toEntityProfissionalCadastro(any(), any());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveAtualizarProfissionalComSucesso() {
        String token = "admin@clinica.com";
        String emailProfissional = "profissional@clinica.com";

        Usuario administrador = new Usuario();
        administrador.setEmail(token);

        Usuario profissional = new Usuario();
        profissional.setEmail(emailProfissional);
        profissional.setAreasAtuacao(new ArrayList<>());

        AreaAtuacaoRequestDTO areaDto = new AreaAtuacaoRequestDTO("Cardiologia", "Coracao");
        List<AreaAtuacaoRequestDTO> areasDto = List.of(areaDto);

        ProfissionalRequestDTO dto = new ProfissionalRequestDTO(
                "Clinica Saude",
                "Dr. Atualizado",
                emailProfissional,
                "novaSenha",
                "98765432100",
                LocalDate.of(1985, 5, 20),
                areasDto,
                Collections.emptyList(),
                true
        );

        when(usuarioServiceImpl.obterUsuarioPeloEmail(token)).thenReturn(administrador);
        when(usuarioServiceImpl.obterUsuarioPeloEmail(emailProfissional)).thenReturn(profissional);

        administradorService.atualizarProfissional(dto, token);

        assertEquals("Dr. Atualizado", profissional.getNome());
        assertEquals("98765432100", profissional.getCpf());
        assertEquals("novaSenha", profissional.getSenha());
        assertTrue(profissional.getInativo());
        assertEquals(1, profissional.getAreasAtuacao().size());
        assertEquals("Cardiologia", profissional.getAreasAtuacao().get(0).getTitulo());

        verify(usuarioServiceImpl).obterUsuarioPeloEmail(token);
        verify(usuarioServiceImpl).obterUsuarioPeloEmail(emailProfissional);
        verify(usuarioServiceImpl).verificarClinicaCoincidente(token, emailProfissional);
        verify(usuarioRepository).save(profissional);
    }
}
