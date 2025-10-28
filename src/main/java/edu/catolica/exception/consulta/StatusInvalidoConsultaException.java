package edu.catolica.exception.consulta;

public class StatusInvalidoConsultaException extends IllegalArgumentException {
    public StatusInvalidoConsultaException () {
        super("Status de consulta não coincide.");
    }
}
