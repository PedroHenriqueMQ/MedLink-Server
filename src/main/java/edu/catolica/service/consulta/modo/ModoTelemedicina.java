package edu.catolica.service.consulta.modo;

import edu.catolica.exception.usuario.EnderecoInvalidoException;
import edu.catolica.exception.consulta.StatusInvalidoConsultaException;
import edu.catolica.exception.consulta.TipoInvalidoConsultaException;
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
    }

    @Override
    public String obterInformacoesEspecificas(Consulta consulta) {
        return """
               Consulta no modo Telemedicina, com link de acesso:
               %s
               """.formatted(linkAcesso);
    }
}
