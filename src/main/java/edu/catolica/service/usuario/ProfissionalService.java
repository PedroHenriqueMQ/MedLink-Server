package edu.catolica.service.usuario;

import edu.catolica.dto.request.AreaAtuacaoRequestDTO;
import edu.catolica.dto.request.EnderecoRequestDTO;
import edu.catolica.dto.response.ConsultaResponseDTO;
import edu.catolica.dto.response.ProfissionalResponseDTO;
import edu.catolica.exception.clinica.ClinicaInexistenteException;
import edu.catolica.mapper.ConsultaMapper;
import edu.catolica.mapper.UsuarioMapper;
import edu.catolica.model.enums.TipoUsuario;
import edu.catolica.repository.UsuarioRepository;
import edu.catolica.service.clinica.ClinicaService;
import edu.catolica.service.consulta.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ProfissionalService {
    private final UsuarioService usuarioService;
    private final ClinicaService clinicaService;
    private final UsuarioRepository usuarioRepository;
    private final ConsultaService consultaService;
    private final UsuarioMapper usuarioMapper;
    private final ConsultaMapper consultaMapper;

    public List<ProfissionalResponseDTO> obterProfissionaisPorClinica(String razaoSocial) {
        var clinicaId = clinicaService.obterIdPelaRazaoSocial(razaoSocial)
                .orElseThrow(() -> new ClinicaInexistenteException(razaoSocial));

        var usuarios = usuarioRepository.findAllByClinicaId(clinicaId);

        return usuarios.stream()
                .filter(usuario -> usuario.getTipoUsuario() == TipoUsuario.PROFISSIONAL)
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }

    public List<ProfissionalResponseDTO> obterProfissionaisPelaClinicaDoUsuario(String token) {
        var clinicaId = usuarioService.obterUsuarioPeloEmail(token).getClinica().getId();
        var usuarios = usuarioRepository.findAllByClinicaId(clinicaId);

        return usuarios.stream().filter(
                (usuario) -> usuario.getTipoUsuario() == TipoUsuario.PROFISSIONAL)
                .map(usuarioMapper::toResponseDTO).toList();
    }

    public List<ConsultaResponseDTO> obterConsultasPeloEmail(String token) {
        var consultas = consultaService.buscarConsultasPorEmailProfissional(token);

        return consultas.stream().map(consultaMapper::toResponseDTOParaProfissional).toList();
    }
}
