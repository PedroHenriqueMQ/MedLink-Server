package edu.catolica.exception;

public class UsuarioInexistenteException extends RuntimeException {
    public UsuarioInexistenteException(String email) {
        super("Usuário com email \"%s\" não encontrado.".formatted(email));
    }
}
