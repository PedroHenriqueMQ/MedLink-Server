package edu.catolica.service.usuario;

import edu.catolica.exception.clinica.UsuariosClinicasDistintasException;
import edu.catolica.exception.usuario.AcessoNegadoException;
import edu.catolica.exception.usuario.CredenciaisInvalidasException;
import edu.catolica.exception.usuario.EmailDuplicadoException;
import edu.catolica.exception.usuario.UsuarioInexistenteException;
import edu.catolica.model.Clinica;
import edu.catolica.model.Usuario;

import java.util.List;

public interface UsuarioService {
    String login(String email, String senha);

    void verificarEmailDuplicado(String email, Clinica clinica);

    Clinica verificarClinicaCoincidente(String emailRemetente, String emailDestinatario);

    Usuario obterUsuarioPeloEmail(String email);

    List<String> obterRazaoSocialPeloEmail(String email);
}
