package edu.catolica.exception;

public class SessaoExpiradaException extends RuntimeException {
    public SessaoExpiradaException() {
        super("Sessão de usuário expirada. Por favor, faça login novamente.");
    }
}
