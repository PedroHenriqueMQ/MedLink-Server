package edu.catolica.service.usuario;

import edu.catolica.dto.request.UsuarioPacienteRequestDTO;
import edu.catolica.model.Usuario;

public interface PacienteService {
    String cadastrarPaciente(UsuarioPacienteRequestDTO usuarioPacienteRequestDTO);
}
