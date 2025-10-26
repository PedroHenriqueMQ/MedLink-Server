package edu.catolica.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
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
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "area_atuacao_id")
    private List<AreaAtuacao> areaAtuacao = new ArrayList<>();
    private TipoUsuario tipoUsuario;
    private List<TurnoAtendimento> turnosAtendimento = new ArrayList<>();
    private boolean inativo = false;

    public Usuario(Clinica clinica, String nome, String email, String senha, String cpf, LocalDate dataNascimento,
                   AreaAtuacao areaAtuacao, TipoUsuario tipoUsuario, List<TurnoAtendimento> turnosAtendimento) {
        this.clinica = clinica;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.areaAtuacao.add(areaAtuacao);
        this.tipoUsuario = tipoUsuario;
        this.turnosAtendimento = turnosAtendimento;
    }
}
