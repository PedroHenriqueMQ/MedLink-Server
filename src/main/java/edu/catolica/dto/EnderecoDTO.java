package edu.catolica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnderecoDTO(
        @Size(max = 10)
        @NotBlank
        String numero,
        @NotBlank
        @Size(max = 80)
        String rua,
        @NotBlank
        @Size(max = 80)
        String bairro,
        @NotBlank
        @Size(max = 20)
        String cidade,
        @NotBlank
        @Size(max = 20)
        String estado
) {
}
