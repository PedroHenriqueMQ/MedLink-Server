package edu.catolica.exception;

public class UsuariosClinicasDistintasException extends IllegalArgumentException {
    public UsuariosClinicasDistintasException() {
        super("Os usuários informados não são compatíveis com a mesma clínica.");
    }
}
