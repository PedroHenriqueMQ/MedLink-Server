package edu.catolica.exception;

public class StatusInvalidoConsultaException extends IllegalArgumentException {
    public StatusInvalidoConsultaException () {
        super("Status de consulta não coincide.");
    }
}
