package edu.catolica.service.consulta.modo;

import edu.catolica.exception.EnderecoInvalidoException;
import edu.catolica.exception.StatusInvalidoConsultaException;
import edu.catolica.exception.TipoInvalidoConsultaException;
import edu.catolica.model.Consulta;
import edu.catolica.model.enums.StatusConsulta;
import edu.catolica.model.enums.TipoConsulta;
import org.springframework.stereotype.Component;

@Component
public class ModoTelemedicina implements ConsultaModo {
    String linkAcesso = "https://link.ficticio.com.br";

    @Override
    public void validarRequisitos(Consulta consulta, StatusConsulta preStatusEsperado) {
        if (consulta.getStatusConsulta() != preStatusEsperado)
            throw new StatusInvalidoConsultaException();

        if (consulta.getTipoConsulta() != TipoConsulta.TELEMEDICINA)
            throw new TipoInvalidoConsultaException();

        if (consulta.getEndereco() != null)
            throw new EnderecoInvalidoException();
    }

    @Override
    public String obterInformacoesEspecificas(Consulta consulta) {
        return """
               Consulta no modo Telemedicina, com link de acesso:
               %s
               """.formatted(linkAcesso);
    }
}
