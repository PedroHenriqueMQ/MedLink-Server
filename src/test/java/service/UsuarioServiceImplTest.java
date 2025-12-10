package service;

import edu.catolica.exception.clinica.UsuariosClinicasDistintasException;
import edu.catolica.exception.usuario.AcessoNegadoException;
import edu.catolica.exception.usuario.CredenciaisInvalidasException;
import edu.catolica.exception.usuario.EmailDuplicadoException;
import edu.catolica.exception.usuario.UsuarioInexistenteException;
import edu.catolica.infra.GerenciadorSessao;
import edu.catolica.model.Clinica;
import edu.catolica.model.Usuario;
import edu.catolica.model.enums.TipoUsuario;
import edu.catolica.repository.UsuarioRepository;
import edu.catolica.service.usuario.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private GerenciadorSessao gerenciadorSessao;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void deveRealizarLoginComSucesso() {
        String email = "teste@email.com";
        String senha = "123";
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setInativo(false);
        usuario.setTipoUsuario(TipoUsuario.ADMINISTRADOR);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        String resultado = usuarioService.login(email, senha);

        assertEquals(TipoUsuario.ADMINISTRADOR.toString(), resultado);

        verify(usuarioRepository).findByEmail(email);
        verify(gerenciadorSessao).login(email);
    }

    @Test
    void deveLancarExcecaoLoginEmailInexistente() {
        String email = "inexistente@email.com";
        String senha = "123";

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(CredenciaisInvalidasException.class, () -> usuarioService.login(email, senha));

        verify(usuarioRepository).findByEmail(email);
        verify(gerenciadorSessao, never()).login(anyString());
    }

    @Test
    void deveLancarExcecaoLoginSenhaIncorreta() {
        String email = "teste@email.com";
        String senhaCorreta = "123";
        String senhaIncorreta = "456";
        Usuario usuario = new Usuario();
        usuario.setSenha(senhaCorreta);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        assertThrows(CredenciaisInvalidasException.class, () -> usuarioService.login(email, senhaIncorreta));

        verify(usuarioRepository).findByEmail(email);
        verify(gerenciadorSessao, never()).login(anyString());
    }

    @Test
    void deveLancarExcecaoLoginUsuarioInativo() {
        String email = "teste@email.com";
        String senha = "123";
        Usuario usuario = new Usuario();
        usuario.setSenha(senha);
        usuario.setInativo(true);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        assertThrows(AcessoNegadoException.class, () -> usuarioService.login(email, senha));

        verify(usuarioRepository).findByEmail(email);
        verify(gerenciadorSessao, never()).login(anyString());
    }

    @Test
    void deveVerificarEmailDuplicadoComSucesso() {
        String email = "novo@email.com";
        Clinica clinica = new Clinica();
        clinica.setId(1L);

        when(usuarioRepository.findByClinicaIdAndEmail(1L, email)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> usuarioService.verificarEmailDuplicado(email, clinica));

        verify(usuarioRepository).findByClinicaIdAndEmail(1L, email);
    }

    @Test
    void deveLancarExcecaoEmailDuplicado() {
        String email = "existente@email.com";
        Clinica clinica = new Clinica();
        clinica.setId(1L);
        clinica.setRazaoSocial("Clinica Teste");
        Usuario usuario = new Usuario();

        when(usuarioRepository.findByClinicaIdAndEmail(1L, email)).thenReturn(Optional.of(usuario));

        assertThrows(EmailDuplicadoException.class, () -> usuarioService.verificarEmailDuplicado(email, clinica));

        verify(usuarioRepository).findByClinicaIdAndEmail(1L, email);
    }

    @Test
    void deveVerificarClinicaCoincidenteComSucesso() {
        String email1 = "user1@email.com";
        String email2 = "user2@email.com";
        Clinica clinica = new Clinica();
        clinica.setId(1L);

        Usuario user1 = new Usuario();
        user1.setClinica(clinica);
        Usuario user2 = new Usuario();
        user2.setClinica(clinica);

        when(usuarioRepository.findByEmail(email1)).thenReturn(Optional.of(user1));
        when(usuarioRepository.findByEmail(email2)).thenReturn(Optional.of(user2));

        Clinica resultado = usuarioService.verificarClinicaCoincidente(email1, email2);

        assertEquals(clinica, resultado);

        verify(usuarioRepository).findByEmail(email1);
        verify(usuarioRepository).findByEmail(email2);
    }

    @Test
    void deveLancarExcecaoClinicasDistintas() {
        String email1 = "user1@email.com";
        String email2 = "user2@email.com";

        Clinica clinica1 = new Clinica();
        clinica1.setId(1L);
        Clinica clinica2 = new Clinica();
        clinica2.setId(2L);

        Usuario user1 = new Usuario();
        user1.setClinica(clinica1);
        Usuario user2 = new Usuario();
        user2.setClinica(clinica2);

        when(usuarioRepository.findByEmail(email1)).thenReturn(Optional.of(user1));
        when(usuarioRepository.findByEmail(email2)).thenReturn(Optional.of(user2));

        assertThrows(UsuariosClinicasDistintasException.class, () ->
                usuarioService.verificarClinicaCoincidente(email1, email2)
        );

        verify(usuarioRepository).findByEmail(email1);
        verify(usuarioRepository).findByEmail(email2);
    }

    @Test
    void deveObterUsuarioPeloEmailComSucesso() {
        String email = "teste@email.com";
        Usuario usuario = new Usuario();

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.obterUsuarioPeloEmail(email);

        assertNotNull(resultado);

        verify(usuarioRepository).findByEmail(email);
    }

    @Test
    void deveLancarExcecaoUsuarioInexistenteAoObterPeloEmail() {
        String email = "inexistente@email.com";

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsuarioInexistenteException.class, () -> usuarioService.obterUsuarioPeloEmail(email));

        verify(usuarioRepository).findByEmail(email);
    }

    @Test
    void deveObterRazaoSocialPeloEmailComSucesso() {
        String email = "teste@email.com";
        String razaoSocial = "Clinica Teste";
        Clinica clinica = new Clinica();
        clinica.setRazaoSocial(razaoSocial);
        Usuario usuario = new Usuario();
        usuario.setClinica(clinica);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        List<String> resultado = usuarioService.obterRazaoSocialPeloEmail(email);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(razaoSocial, resultado.get(0));

        verify(usuarioRepository).findByEmail(email);
    }

    @Test
    void deveLancarExcecaoUsuarioInexistenteAoObterRazaoSocial() {
        String email = "inexistente@email.com";

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UsuarioInexistenteException.class, () -> usuarioService.obterRazaoSocialPeloEmail(email));

        verify(usuarioRepository).findByEmail(email);
    }
}