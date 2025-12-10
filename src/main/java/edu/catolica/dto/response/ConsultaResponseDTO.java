package edu.catolica.dto.response;

import edu.catolica.dto.request.EnderecoRequestDTO;
import edu.catolica.model.enums.StatusConsulta;
import edu.catolica.model.enums.TipoConsulta;

import java.time.LocalDate;
import java.time.LocalTime;

public record ConsultaResponseDTO(
        Long id,
        String nomeDestinatario,
        String emailDestinatario,
        String clinica,
        EnderecoRequestDTO endereco,
        LocalDate dataConsulta,
        LocalTime horaInicio,
        LocalTime horaFim,
        TipoConsulta tipoConsulta,
        String justificativa,
        StatusConsulta statusConsulta
) {
}
