package edu.catolica.service.clinica.impl;

import edu.catolica.exception.clinica.ClinicaInexistenteException;
import edu.catolica.service.clinica.ClinicaService;
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
public class ClinicaServiceImpl implements ClinicaService {
    private final ClinicaRepository clinicaRepository;

    @Override
    public Clinica consultarClinicaExistente(String razaoSocial) {
        return clinicaRepository.findByRazaoSocial(razaoSocial)
            .orElseThrow(() -> new ClinicaInexistenteException(razaoSocial));
    }

    @Override
    public List<String> obterTodasRazoesSociais() {
        return clinicaRepository.findAll().stream().map(Clinica::getRazaoSocial).toList();
    }

    @Override
    public Optional<Long> obterIdPelaRazaoSocial(String razaoSocial) {
        return clinicaRepository.findByRazaoSocial(razaoSocial).map(Clinica::getId);
    }
}
