package edu.catolica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.catolica.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByClinicaId(Long clinicaId);
}
