package edu.catolica.model;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@Entity
@Table(name = "usuarios")
public final class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "clinica_id")
    private Clinica clinica;
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private LocalDate dataNascimento;
    @OneToMany
    @JoinColumn(name = "area_atuacao_id")
    private List<AreaAtuacao> areaAtuacao;
    private TipoUsuario tipoUsuario;

    public Usuario(Clinica clinica, String nome, String email, String senha, String cpf, LocalDate dataNascimento, List<AreaAtuacao> areaAtuacao, TipoUsuario tipoUsuario) {
        this.clinica = clinica;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.areaAtuacao = areaAtuacao;
        this.tipoUsuario = tipoUsuario;
    }
}
