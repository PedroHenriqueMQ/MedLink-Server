package edu.catolica.service.consulta.modo;

import edu.catolica.model.Consulta;
import edu.catolica.model.enums.StatusConsulta;

public interface ConsultaModo {
    void validarRequisitos(Consulta consulta, StatusConsulta preStatusEsperado);
    String obterInformacoesEspecificas(Consulta consulta);
}
