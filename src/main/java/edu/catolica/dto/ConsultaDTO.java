package edu.catolica.dto;

import edu.catolica.model.enums.StatusConsulta;
import edu.catolica.model.enums.TipoConsulta;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record ConsultaDTO(
        @NotBlank
        @Email
        String emailDestinatario,
        @NotBlank
        @Size(max = 80)
        String clinica,
        EnderecoDTO endereco,
        @NotNull
        LocalDate dataConsulta,
        @NotNull
        LocalTime horaInicio,
        @NotNull
        LocalTime horaFim,
        @NotNull
        TipoConsulta tipoConsulta,
        @Size(max = 300)
        String justificativa,
        @NotNull
        StatusConsulta statusConsulta
) {
}
