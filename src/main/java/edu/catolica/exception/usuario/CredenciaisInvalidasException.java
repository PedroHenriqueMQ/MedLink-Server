package edu.catolica.exception.usuario;

public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException() {
        super("Email ou senha inválidos.");
    }
}
