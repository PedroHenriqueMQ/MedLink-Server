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
        @NotBlank(message = "Consulta não aceita email de destinatário vazio")
        @Email(message = "Consulta não aceita email de destinatário inválido")
        String emailDestinatario,
        @NotBlank(message = "Consulta não aceita nome da Clínica vazio")
        @Size(max = 80, message = "Consulta com nome da Clínica só aceita até 80 caracteres")
        String clinica,
        EnderecoDTO endereco,
        @NotNull(message = "Consulta não aceita data vazia")
        LocalDate dataConsulta,
        @NotNull(message = "Consulta não aceita horário de início vazio")
        LocalTime horaInicio,
        @NotNull(message = "Consulta não aceita horário de fim vazio")
        LocalTime horaFim,
        @NotNull(message = "Consulta não aceita tipo de consulta vazio")
        TipoConsulta tipoConsulta,
        @Size(max = 300, message = "Consulta com justificativa só aceita até 300 caracteres")
        String justificativa,
        @NotNull(message = "Consulta não aceita status vazio")
        StatusConsulta statusConsulta
) {
}
