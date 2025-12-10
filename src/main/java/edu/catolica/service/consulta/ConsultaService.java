package edu.catolica.service.consulta;

import edu.catolica.dto.request.ConsultaRequestDTO;
import edu.catolica.dto.response.ConsultaResponseDTO;
import edu.catolica.model.Consulta;

import java.util.List;

public interface ConsultaService {
    void solicitarConsulta(String token, ConsultaRequestDTO consultaRequestDTO);

    ConsultaResponseDTO confirmarConsulta(Long id);

    ConsultaResponseDTO negarConsulta(Long id, String justificativa);

    List<Consulta> buscarConsultasPorEmailProfissional(String email);
}
