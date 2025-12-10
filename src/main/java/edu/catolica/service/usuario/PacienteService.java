package edu.catolica.service.usuario;

import edu.catolica.dto.request.UsuarioPacienteRequestDTO;
import edu.catolica.dto.response.ConsultaResponseDTO;
import edu.catolica.model.Usuario;

import java.util.List;

public interface PacienteService {
    String cadastrarPaciente(UsuarioPacienteRequestDTO usuarioPacienteRequestDTO);

    List<ConsultaResponseDTO> obterConsultasPeloEmail(String token);
}
