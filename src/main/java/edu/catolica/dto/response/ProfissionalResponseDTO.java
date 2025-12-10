package edu.catolica.dto.response;

import edu.catolica.dto.request.AreaAtuacaoRequestDTO;

import java.time.LocalDate;
import java.util.List;

public record ProfissionalResponseDTO (
    Long id,
    String clinica,
    String nome,
    String email,
    String senha,
    String cpf,
    LocalDate dataNascimento,
    List<AreaAtuacaoRequestDTO> areasAtuacao,
    boolean inativo
) {

}
