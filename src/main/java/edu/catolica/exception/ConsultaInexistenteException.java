package edu.catolica.exception;

public class ConsultaInexistenteException extends IllegalArgumentException {
    public ConsultaInexistenteException() {
        super("Os dados da consulta não estão presentes no banco de consultas.");
    }
}
