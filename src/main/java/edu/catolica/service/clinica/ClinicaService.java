package edu.catolica.service.clinica;

import edu.catolica.exception.clinica.ClinicaInexistenteException;
import edu.catolica.model.Clinica;

import java.util.List;
import java.util.Optional;

public interface ClinicaService {
    Clinica consultarClinicaExistente(String razaoSocial);

    List<String> obterTodasRazoesSociais();

    Optional<Long> obterIdPelaRazaoSocial(String razaoSocial);
}
