package edu.catolica.exception.consulta;

public class ConsultaDuplicadaException extends IllegalArgumentException {
    public ConsultaDuplicadaException() {
        super("Já existe uma consulta com os mesmos valores de data e horário.");
    }
}
