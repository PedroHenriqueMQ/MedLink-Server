package edu.catolica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EnderecoDTO(
        @Size(max = 10, message = "Número do endereço não ser maior que 10 caracteres")
        @NotBlank(message = "Número do endereço não pode estar vazio")
        String numero,
        @NotBlank(message = "Rua do endereço não pode estar vazia")
        @Size(max = 80, message = "Rua do endereço não aceita mais que 80 caracteres")
        String rua,
        @NotBlank(message = "Bairro do endereço não pode estar vazio")
        @Size(max = 80, message = "Bairro do endereço não aceita mais que 80 caracteres")
        String bairro,
        @NotBlank(message = "Cidade do endereço não pode estar vazia")
        @Size(max = 20, message = "Cidade do endereço não aceita mais que 20 caracteres")
        String cidade,
        @NotBlank(message = "Estado do endereço não pode estar vazio")
        @Size(max = 20, message = "Estado do endereço não aceita mais que 20 caracteres")
        String estado
) {
}
