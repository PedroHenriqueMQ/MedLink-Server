package edu.catolica.service.usuario;

import edu.catolica.dto.response.ConsultaResponseDTO;
import edu.catolica.dto.response.ProfissionalResponseDTO;
import edu.catolica.exception.clinica.ClinicaInexistenteException;
import edu.catolica.model.enums.TipoUsuario;

import java.util.List;

public interface ProfissionalService {
    List<ProfissionalResponseDTO> obterProfissionaisPorClinica(String razaoSocial);

    List<ProfissionalResponseDTO> obterProfissionaisPelaClinicaDoUsuario(String token);

    List<ConsultaResponseDTO> obterConsultasPeloEmail(String token);
}
