package edu.catolica.exception.consulta;

public class TipoInvalidoConsultaException extends IllegalArgumentException {
    public TipoInvalidoConsultaException() {
        super("Tipo de consulta inválido para o modo selecionado.");
    }
}
