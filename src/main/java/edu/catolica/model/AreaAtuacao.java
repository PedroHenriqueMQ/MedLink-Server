package edu.catolica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "areas_atuacao")
public class AreaAtuacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descricao;

    public AreaAtuacao(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }
}
