package edu.catolica.exception.consulta;

public class ConsultaDuplicadaException extends IllegalArgumentException {
    public ConsultaDuplicadaException() {
        super("Consulta já registrada no sistema.");
    }
}
