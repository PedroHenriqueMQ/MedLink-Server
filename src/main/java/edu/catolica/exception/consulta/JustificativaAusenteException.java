package edu.catolica.exception.consulta;

public class JustificativaAusenteException extends IllegalArgumentException {
    public JustificativaAusenteException() {
        super("Justificativa inválida.");
    }
}
