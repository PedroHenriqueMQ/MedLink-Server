package edu.catolica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.catolica.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByClinicaIdAndEmail(Long clinicaId, String email);
    Optional<Usuario> findByEmail(String email);
}
