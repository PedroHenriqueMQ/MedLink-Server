package edu.catolica.service.clinica;

import edu.catolica.exception.ClinicaInexistenteException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.catolica.model.Clinica;
import edu.catolica.repository.ClinicaRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ClinicaService {
    private final ClinicaRepository clinicaRepository;

    public Clinica consultarClinicaExistente(String razaoSocial) {
        return clinicaRepository.findByRazaoSocial(razaoSocial)
            .orElseThrow(() -> new ClinicaInexistenteException(razaoSocial));
    }

    public List<String> obterTodasRazoesSociais() {
        return clinicaRepository.findAll().stream().map(Clinica::getRazaoSocial).toList();
    }

    public Optional<Long> obterIdPelaRazaoSocial(String razaoSocial) {
        return clinicaRepository.findByRazaoSocial(razaoSocial).map(Clinica::getId);
    }
}
