package edu.catolica.dto.request;

import jakarta.validation.constraints.Size;

public record EnderecoRequestDTO(
        @Size(max = 10, message = "Número do endereço não ser maior que 10 caracteres")
        String numero,
        @Size(max = 80, message = "Rua do endereço não aceita mais que 80 caracteres")
        String rua,
        @Size(max = 80, message = "Bairro do endereço não aceita mais que 80 caracteres")
        String bairro,
        @Size(max = 20, message = "Cidade do endereço não aceita mais que 20 caracteres")
        String cidade,
        @Size(max = 20, message = "Estado do endereço não aceita mais que 20 caracteres")
        String estado
) {
}
