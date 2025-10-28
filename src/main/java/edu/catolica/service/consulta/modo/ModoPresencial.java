package edu.catolica.service.consulta.modo;

import edu.catolica.exception.usuario.EnderecoInvalidoException;
import edu.catolica.exception.consulta.StatusInvalidoConsultaException;
import edu.catolica.exception.consulta.TipoInvalidoConsultaException;
import edu.catolica.model.Consulta;
import edu.catolica.model.enums.StatusConsulta;
import edu.catolica.model.enums.TipoConsulta;
import org.springframework.stereotype.Component;

@Component
public class ModoPresencial implements ConsultaModo {
    @Override
    public void validarRequisitos(Consulta consulta, StatusConsulta preStatusEsperado) {
        if (consulta.getStatusConsulta() != preStatusEsperado)
            throw new StatusInvalidoConsultaException();

        if (consulta.getTipoConsulta() != TipoConsulta.PRESENCIAL)
            throw new TipoInvalidoConsultaException();
    }

    @Override
    public String obterInformacoesEspecificas(Consulta consulta) {
        return """
               Consulta no modo Domiciliar, no seguinte endereço:
               Número: Número da Clínica,
               Rua: Rua da Clínica,
               Bairro: Bairro da Clínica,
               Cidade: Cidade da Clínica,
               Estado: Estado da Clínica
               """;
    }
}
