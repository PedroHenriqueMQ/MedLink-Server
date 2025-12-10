package edu.catolica.mapper;

import edu.catolica.dto.request.ConsultaRequestDTO;
import edu.catolica.dto.request.EnderecoRequestDTO;
import edu.catolica.dto.response.ConsultaResponseDTO;
import edu.catolica.model.Clinica;
import edu.catolica.model.Consulta;
import edu.catolica.model.Endereco;
import edu.catolica.model.Usuario;
import org.mapstruct.*;

import java.time.LocalTime;

@Mapper(componentModel = "spring")
public abstract class ConsultaMapper {

    // Método principal de conversão - recebe a clínica já validada
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "dto.tipoConsulta", target = "tipoConsulta")
    @Mapping(source = "dto.dataConsulta", target = "dataConsulta")
    @Mapping(source = "dto.justificativa", target = "justificativa")
    @Mapping(source = "dto.statusConsulta", target = "statusConsulta")
    @Mapping(source = "paciente", target = "paciente")
    @Mapping(source = "profissional", target = "profissional")
    @Mapping(source = "clinica", target = "clinica")
    @Mapping(source = "dto.endereco", target = "endereco")
    @Mapping(target = "horaInicio", ignore = true)
    @Mapping(target = "horaFim", ignore = true)
    public abstract Consulta toEntity(ConsultaRequestDTO dto, Usuario paciente, Usuario profissional, Clinica clinica);

    @AfterMapping
    protected void ajustarHorarios(@MappingTarget Consulta consulta, ConsultaRequestDTO dto) {
        LocalTime horaInicio = dto.horaInicio()
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        LocalTime horaFim = horaInicio.plusHours(1);

        consulta.setHoraInicio(horaInicio);
        consulta.setHoraFim(horaFim);
    }

    @Mapping(source = "paciente.nome", target = "nomeDestinatario")
    @Mapping(source = "paciente.email", target = "emailDestinatario")
    @Mapping(source = "clinica.razaoSocial", target = "clinica")
    @Mapping(source = "endereco", target = "endereco")
    public abstract ConsultaResponseDTO toResponseDTOParaProfissional(Consulta consulta);

    @Mapping(source = "profissional.nome", target = "nomeDestinatario")
    @Mapping(source = "profissional.email", target = "emailDestinatario")
    @Mapping(source = "clinica.razaoSocial", target = "clinica")
    @Mapping(source = "endereco", target = "endereco")
    public abstract ConsultaResponseDTO toResponseDTOParaPaciente(Consulta consulta);

    public abstract EnderecoRequestDTO toEnderecoDTO(Endereco endereco);

    public abstract Endereco toEndereco(EnderecoRequestDTO dto);
}
