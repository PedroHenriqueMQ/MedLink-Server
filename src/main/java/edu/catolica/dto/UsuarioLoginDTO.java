package edu.catolica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioLoginDTO(
    @Email(message = "Email inválido")
    @NotBlank(message = "Email não pode estar vazio")
    String email,
    @Size(min = 8, max = 12, message = "Senha deve ter entre 8 e 12 caracteres")
    @NotBlank(message = "Senha não pode estar vazia")
    String senha
) {

}
