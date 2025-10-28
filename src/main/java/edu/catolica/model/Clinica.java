package edu.catolica.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "clinicas")
public final class Clinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome_fantasia")
    private String nomeFantasia;
    @Column(name = "razao_social", unique = true)
    private String razaoSocial;
    @Column(unique = true)
    private String cnpj;

    public Clinica(String nomeFantasia, String razaoSocial, String cnpj) {
        this.nomeFantasia = nomeFantasia;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
    }
}
