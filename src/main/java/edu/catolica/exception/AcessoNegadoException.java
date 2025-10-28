package edu.catolica.exception;

public class AcessoNegadoException extends RuntimeException {
    public AcessoNegadoException() {
        super("Acesso negado. Você não possui as permissões necessárias para esta ação.");
    }
}
