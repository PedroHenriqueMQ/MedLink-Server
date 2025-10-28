package edu.catolica.exception.usuario;

public class AcessoNegadoException extends RuntimeException {
    public AcessoNegadoException() {
        super("Acesso negado. Você não possui as permissões necessárias para esta ação.");
    }
}
