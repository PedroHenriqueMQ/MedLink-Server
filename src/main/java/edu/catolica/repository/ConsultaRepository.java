package edu.catolica.repository;

import edu.catolica.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    Optional<Consulta> findByProfissionalIdAndDataConsultaAndHoraInicio(
            Long profissionalId, LocalDate dataConsulta, LocalTime horaInicio
    );

    Optional<Consulta> findByPacienteIdAndDataConsultaAndHoraInicio(
            Long pacienteId, LocalDate dataConsulta, LocalTime horaInicio
    );

    List<Consulta> findAllByProfissionalId(Long profissionalId);
}
