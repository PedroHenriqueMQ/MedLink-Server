package edu.catolica.mapper;

import edu.catolica.dto.request.AreaAtuacaoRequestDTO;
import edu.catolica.dto.request.ProfissionalRequestDTO;
import edu.catolica.dto.request.UsuarioPacienteRequestDTO;
import edu.catolica.dto.response.ProfissionalResponseDTO;
import edu.catolica.model.AreaAtuacao;
import edu.catolica.model.Clinica;
import edu.catolica.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UsuarioMapper {
    @Mapping(source = "clinica", target = "clinica")
    @Mapping(source = "dto.areasAtuacao", target = "areasAtuacao")
    @Mapping(source = "dto.turnosAtendimento", target = "turnosAtendimento")
    @Mapping(source = "dto.inativo", target = "inativo")
    @Mapping(target = "id", ignore = true)
    @Mapping(constant = "PROFISSIONAL", target = "tipoUsuario")
    public abstract Usuario toEntityProfissionalCadastro(ProfissionalRequestDTO dto, Clinica clinica);

    @Mapping(source = "clinica", target = "clinica")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "areasAtuacao", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "turnosAtendimento", expression = "java(new java.util.ArrayList<>())")
    @Mapping(constant = "PACIENTE", target = "tipoUsuario")
    @Mapping(constant = "false", target = "inativo")
    public abstract Usuario toEntityPacienteCadastro(UsuarioPacienteRequestDTO dto, Clinica clinica);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "clinica.razaoSocial", target = "clinica")
    @Mapping(source = "nome", target = "nome")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "senha", target = "senha")
    @Mapping(source = "cpf", target = "cpf")
    @Mapping(source = "dataNascimento", target = "dataNascimento")
    @Mapping(source = "areasAtuacao", target = "areasAtuacao")
    @Mapping(source = "inativo", target = "inativo")
    public abstract ProfissionalResponseDTO toResponseDTO(Usuario usuario);

    public abstract List<AreaAtuacao> toAreasAtuacao(List<AreaAtuacaoRequestDTO> dtos);

    @Mapping(target = "id", ignore = true)
    public abstract AreaAtuacao toAreaAtuacao(AreaAtuacaoRequestDTO dto);

    public abstract List<AreaAtuacaoRequestDTO> toAreasAtuacaoDTO(List<AreaAtuacao> entities);

    @Mapping(source = "titulo", target = "titulo")
    @Mapping(source = "descricao", target = "descricao")
    public abstract AreaAtuacaoRequestDTO toAreaAtuacaoDTO(AreaAtuacao entity);
}
