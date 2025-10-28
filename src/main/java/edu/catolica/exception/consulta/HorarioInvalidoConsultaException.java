package edu.catolica.exception.consulta;

public class HorarioInvalidoConsultaException extends IllegalArgumentException {
    public HorarioInvalidoConsultaException() {
        super("Horário de consulta inválido.");
    }
}
