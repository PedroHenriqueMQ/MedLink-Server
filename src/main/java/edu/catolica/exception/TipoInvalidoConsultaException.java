package edu.catolica.exception;

public class TipoInvalidoConsultaException extends IllegalArgumentException {
    public TipoInvalidoConsultaException() {
        super("Tipo de consulta inválido para o modo selecionado.");
    }
}
