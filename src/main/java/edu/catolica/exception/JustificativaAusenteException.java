package edu.catolica.exception;

public class JustificativaAusenteException extends IllegalArgumentException {
    public JustificativaAusenteException() {
        super("Justificativa inválida.");
    }
}
