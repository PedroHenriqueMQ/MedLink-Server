package edu.catolica.exception.usuario;

public class UsuarioInexistenteException extends RuntimeException {
    public UsuarioInexistenteException(String email) {
        super("Usuário com email \"%s\" não encontrado.".formatted(email));
    }
}
