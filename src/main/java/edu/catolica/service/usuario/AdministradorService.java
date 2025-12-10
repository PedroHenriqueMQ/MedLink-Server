package edu.catolica.service.usuario;

import edu.catolica.dto.request.ProfissionalRequestDTO;
import edu.catolica.exception.clinica.UsuariosClinicasDistintasException;
import edu.catolica.model.AreaAtuacao;
import edu.catolica.model.Usuario;

import java.util.stream.Collectors;

public interface AdministradorService {
    void cadastrarProfissional(ProfissionalRequestDTO profissionalRequestDTO, String token);

    void atualizarProfissional(ProfissionalRequestDTO profissionalRequestDTO, String token);
}
