package edu.catolica.exception.clinica;


import jakarta.persistence.EntityNotFoundException;

public class ClinicaInexistenteException extends EntityNotFoundException {
    public ClinicaInexistenteException(String razaoSocial) {
        super("Clínica com razão social %s não encontrada.".formatted(razaoSocial));
    }
}
