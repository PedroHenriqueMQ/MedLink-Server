package edu.catolica.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "enderecos")
@Entity
@Data
@NoArgsConstructor
public class Endereco {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String numero;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;

    public Endereco(String numero, String rua, String bairro, String cidade, String estado) {
        this.numero = numero;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
    }
}
