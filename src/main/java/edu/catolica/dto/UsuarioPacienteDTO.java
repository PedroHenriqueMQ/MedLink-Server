package edu.catolica.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioPacienteDTO(
        @Size(max = 80)
        @NotBlank
        String clinica,
        @Size(max = 80)
        @NotBlank
        String nome,
        @Email
        @NotBlank
        String email,
        @Size(min = 8, max = 12)
        @NotBlank
        String senha,
        @CPF
        @NotBlank
        String cpf,
        @NotNull
        LocalDate dataNascimento
) {
}
