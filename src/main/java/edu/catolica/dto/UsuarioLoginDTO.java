package edu.catolica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioLoginDTO(
    @Email
    @NotBlank
    String email,
    @Size(min = 8, max = 12)
    @NotBlank
    String senha
) {

}
