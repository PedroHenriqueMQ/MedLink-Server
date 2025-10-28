package edu.catolica.exception.usuario;

public class SessaoInvalidaException extends RuntimeException {
    public SessaoInvalidaException() {
        super("Sessão inválida. Faça login para utilizar os recursos do sistema.");
    }
}
