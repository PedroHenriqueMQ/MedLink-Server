package edu.catolica.exception.usuario;

public class EmailDuplicadoException extends RuntimeException{
    public EmailDuplicadoException(String email, String razaoSocial) {
        super("O email %s já está cadastrado na clínica %s.".formatted(email, razaoSocial));
    }
}
