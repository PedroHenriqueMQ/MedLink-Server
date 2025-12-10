package edu.catolica.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AreaAtuacaoRequestDTO(
        @NotBlank(message = "Título de Área de Atuação não deve estar vazio")
        @Size(max = 20, message = "Título de Área de Atuação só aceita até 20 caracteres")
        String titulo,
        @NotBlank(message = "Descrição de Área de Atuação não deve estar vazia")
        @Size(max = 80, message = "Descrição de Área de Atuação só aceita até 80 caracteres")
        String descricao
) {
}
