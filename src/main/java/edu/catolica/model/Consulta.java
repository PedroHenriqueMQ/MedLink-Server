package edu.catolica.model;

import edu.catolica.model.enums.StatusConsulta;
import edu.catolica.model.enums.TipoConsulta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "consultas")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Usuario paciente;
    @ManyToOne
    @JoinColumn(name = "profissional_id")
    private Usuario profissional;
    @ManyToOne
    @JoinColumn(name = "clinica_id")
    private Clinica clinica;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;
    @Column(name = "data_consulta", nullable = false)
    private LocalDate dataConsulta;
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;
    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_consulta", nullable = false)
    private TipoConsulta tipoConsulta;
    private String justificativa;
    @Enumerated(EnumType.STRING)
    @Column(name = "status_consulta", nullable = false)
    private StatusConsulta statusConsulta;

    public Consulta(Usuario paciente, Usuario profissional, Clinica clinica, Endereco endereco, LocalDate dataConsulta,
                    LocalTime horaInicio, LocalTime horaFim, TipoConsulta tipoConsulta, String justificativa,
                    StatusConsulta statusConsulta) {
        this.paciente = paciente;
        this.profissional = profissional;
        this.clinica = clinica;
        this.endereco = endereco;
        this.dataConsulta = dataConsulta;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.tipoConsulta = tipoConsulta;
        this.justificativa = justificativa;
        this.statusConsulta = statusConsulta;
    }
}
