package edu.catolica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AreaAtuacaoDTO(
        @NotBlank
        @Size(max = 20)
        String titulo,
        @NotBlank
        @Size(max = 80)
        String descricao
) {
}
