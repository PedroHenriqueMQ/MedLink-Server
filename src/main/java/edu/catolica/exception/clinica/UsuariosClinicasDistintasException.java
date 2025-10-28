package edu.catolica.exception.clinica;

public class UsuariosClinicasDistintasException extends IllegalArgumentException {
    public UsuariosClinicasDistintasException() {
        super("Os usuários informados não são compatíveis com a mesma clínica.");
    }
}
