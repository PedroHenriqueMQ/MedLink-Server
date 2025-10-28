package edu.catolica.exception;

public class UsuarioInvalidoException extends IllegalArgumentException {
    public UsuarioInvalidoException(String email) {
        super("Usuário com email %s inválido.".formatted(email));
    }
}
