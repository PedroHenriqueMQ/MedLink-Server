package edu.catolica.repository;

import edu.catolica.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findAllByProfissionalIdAndDataConsulta(Long profissionalId, LocalDate dataConsulta);

    Optional<Consulta> findByProfissionalIdAndPacienteIdAndClinicaIdAndDataConsultaAndHoraInicio(
            Long profissionalId, Long pacienteId, Long clinicaId, LocalDate dataConsulta, LocalTime horaInicio);
}
