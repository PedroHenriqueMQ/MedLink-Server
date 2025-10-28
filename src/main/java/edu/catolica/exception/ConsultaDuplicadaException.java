package edu.catolica.exception;

public class ConsultaDuplicadaException extends IllegalArgumentException {
    public ConsultaDuplicadaException() {
        super("Consulta já registrada no sistema.");
    }
}
