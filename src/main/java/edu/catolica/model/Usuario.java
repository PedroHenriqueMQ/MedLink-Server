package edu.catolica.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.catolica.model.enums.TipoUsuario;
import edu.catolica.model.enums.TurnoAtendimento;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
    private List<AreaAtuacao> areasAtuacao = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;
    @Enumerated(EnumType.STRING)
    private List<TurnoAtendimento> turnosAtendimento = new ArrayList<>();
    private boolean inativo = false;

    public Usuario(Clinica clinica, String nome, String email, String senha, String cpf, LocalDate dataNascimento,
                   List<AreaAtuacao> areasAtuacao, TipoUsuario tipoUsuario, List<TurnoAtendimento> turnosAtendimento) {
        this.clinica = clinica;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.areasAtuacao = areasAtuacao;
        this.tipoUsuario = tipoUsuario;
        this.turnosAtendimento = turnosAtendimento;
    }

    public boolean getInativo() {
        return this.inativo;
    }
}
