package edu.catolica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.catolica.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByClinicaId(Long clinicaId);
    Optional<Usuario> findByEmail(String email);
}
