package edu.catolica.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.catolica.model.Clinica;

public interface ClinicaRepository extends JpaRepository<Clinica, Long> {
    Optional<Clinica> findByRazaoSocial(String razaoSocial);
}
