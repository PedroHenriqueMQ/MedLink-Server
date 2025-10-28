package edu.catolica.dto;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioProfissionalDTO(
        @Size(max = 80, message = "Profissional não pode ter nome da Clínica com mais de 80 caracteres")
        @NotBlank(message = "Profissional não pode ter nome da Clínica vazio")
        String clinica,
        @Size(max = 80, message = "Profissional não pode ter nome com mais de 80 caracteres")
        @NotBlank(message = "Profissional não pode ter nome vazio")
        String nome,
        @Email(message = "Profissional não pode ter email inválido")
        @NotBlank(message = "Profissional não pode ter email vazio")
        String email,
        @Size(min = 8, max = 12, message = "Profissional não pode ter senha com menos de 8 caracteres ou mais de 12 caracteres")
        @NotBlank(message = "Profissional não pode ter senha vazia")
        String senha,
        @CPF(message = "Profissional não pode ter CPF inválido")
        @NotBlank(message = "Profissional não pode ter CPF vazio")
        String cpf,
        @NotNull(message = "Profissional não pode ter data de nascimento vazia")
        LocalDate dataNascimento,
        @NotNull(message = "Profissional não pode ter especialidade vazia")
        List<AreaAtuacaoDTO> areasAtuacao
) {
}