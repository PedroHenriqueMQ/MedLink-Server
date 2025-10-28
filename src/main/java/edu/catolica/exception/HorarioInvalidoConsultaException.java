package edu.catolica.exception;

public class HorarioInvalidoConsultaException extends IllegalArgumentException {
    public HorarioInvalidoConsultaException() {
        super("Horário de consulta inválido.");
    }
}
