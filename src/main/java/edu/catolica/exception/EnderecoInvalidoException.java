package edu.catolica.exception;

public class EnderecoInvalidoException extends IllegalArgumentException {
    public EnderecoInvalidoException() {
        super("Endereço inválido para o modo de consulta selecionado.");
    }
}
