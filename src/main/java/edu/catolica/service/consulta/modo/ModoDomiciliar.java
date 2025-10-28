package edu.catolica.service.consulta.modo;

import edu.catolica.exception.usuario.EnderecoInvalidoException;
import edu.catolica.exception.consulta.StatusInvalidoConsultaException;
import edu.catolica.exception.consulta.TipoInvalidoConsultaException;
import edu.catolica.model.Consulta;
import edu.catolica.model.enums.StatusConsulta;
import edu.catolica.model.enums.TipoConsulta;
import org.springframework.stereotype.Component;

@Component
public class ModoDomiciliar implements ConsultaModo {
    @Override
    public void validarRequisitos(Consulta consulta, StatusConsulta preStatusEsperado) {
        if (consulta.getStatusConsulta() != preStatusEsperado)
            throw new StatusInvalidoConsultaException();

        if (consulta.getTipoConsulta() != TipoConsulta.DOMICILIAR)
            throw new TipoInvalidoConsultaException();

        if (consulta.getEndereco() == null)
            throw new EnderecoInvalidoException();
    }

    @Override
    public String obterInformacoesEspecificas(Consulta consulta) {
        return """
               Consulta no modo Presencial, no seguinte endereço:
               Número: %s,
               Rua: %s,
               Bairro: %s,
               Cidade: %s,
               Estado: %s,
               """.formatted(
                consulta.getEndereco().getNumero(),
                consulta.getEndereco().getRua(),
                consulta.getEndereco().getBairro(),
                consulta.getEndereco().getCidade(),
                consulta.getEndereco().getEstado()
        );
    }
}
