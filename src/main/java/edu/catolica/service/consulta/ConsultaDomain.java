package edu.catolica.service.consulta;

import edu.catolica.exception.consulta.HorarioInvalidoConsultaException;
import edu.catolica.exception.consulta.JustificativaAusenteException;
import edu.catolica.model.Consulta;
import edu.catolica.model.enums.StatusConsulta;
import edu.catolica.service.consulta.modo.ConsultaModo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultaDomain {
    private ConsultaModo modo;
    private Consulta consulta;

    public ConsultaDomain(ConsultaModo modo, Consulta consulta) {
        this.modo = modo;
        this.consulta = consulta;
    }

    public Consulta processarConsulta(StatusConsulta preStatusEsperado) {
        modo.validarRequisitos(consulta, preStatusEsperado);
        validarHorarioConsulta(consulta.getHoraInicio(), consulta.getHoraFim());
        validarDataConsulta(consulta.getDataConsulta());

        return consulta;
    }

    public void validarJustificativa(String justificativa) {
        if (justificativa == null || justificativa.isBlank())
            throw new JustificativaAusenteException();
    }

    public String obterDetalhesConsulta(Consulta consulta) {
        return modo.obterInformacoesEspecificas(consulta);
    }

    private void validarHorarioConsulta(LocalTime horaInicio, LocalTime horaFim) {
        if (horaInicio.isAfter(horaFim) || Duration.between(horaInicio, horaFim).toHours() != 1)
            throw new HorarioInvalidoConsultaException();

        if (horaInicio.isBefore(LocalTime.of(7, 0)) || horaFim.isAfter(LocalTime.of(17, 0)))
            throw new HorarioInvalidoConsultaException();

        if (horaInicio.isBefore(LocalTime.of(13, 0)) && horaFim.isAfter(LocalTime.of(12, 0)))
            throw new HorarioInvalidoConsultaException();
    }

    private void validarDataConsulta(LocalDate dataConsulta) {
        if (dataConsulta.isBefore(LocalDate.now()))
            throw new HorarioInvalidoConsultaException();
    }
}
