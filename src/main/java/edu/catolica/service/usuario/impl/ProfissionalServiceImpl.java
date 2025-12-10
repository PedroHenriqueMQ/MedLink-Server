package edu.catolica.service.usuario.impl;

import edu.catolica.dto.response.ConsultaResponseDTO;
import edu.catolica.dto.response.ProfissionalResponseDTO;
import edu.catolica.exception.clinica.ClinicaInexistenteException;
import edu.catolica.mapper.ConsultaMapper;
import edu.catolica.mapper.UsuarioMapper;
import edu.catolica.model.enums.TipoUsuario;
import edu.catolica.repository.UsuarioRepository;
import edu.catolica.service.clinica.impl.ClinicaServiceImpl;
import edu.catolica.service.consulta.impl.ConsultaServiceImpl;
import edu.catolica.service.usuario.ProfissionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ProfissionalServiceImpl implements ProfissionalService {
    private final UsuarioServiceImpl usuarioServiceImpl;
    private final ClinicaServiceImpl clinicaServiceImpl;
    private final UsuarioRepository usuarioRepository;
    private final ConsultaServiceImpl consultaServiceImpl;
    private final UsuarioMapper usuarioMapper;
    private final ConsultaMapper consultaMapper;

    @Override
    public List<ProfissionalResponseDTO> obterProfissionaisPorClinica(String razaoSocial) {
        var clinicaId = clinicaServiceImpl.obterIdPelaRazaoSocial(razaoSocial)
                .orElseThrow(() -> new ClinicaInexistenteException(razaoSocial));

        var usuarios = usuarioRepository.findAllByClinicaId(clinicaId);

        return usuarios.stream()
                .filter(usuario -> usuario.getTipoUsuario() == TipoUsuario.PROFISSIONAL)
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }

    @Override
    public List<ProfissionalResponseDTO> obterProfissionaisPelaClinicaDoUsuario(String token) {
        var clinicaId = usuarioServiceImpl.obterUsuarioPeloEmail(token).getClinica().getId();
        var usuarios = usuarioRepository.findAllByClinicaId(clinicaId);

        return usuarios.stream().filter(
                (usuario) -> usuario.getTipoUsuario() == TipoUsuario.PROFISSIONAL)
                .map(usuarioMapper::toResponseDTO).toList();
    }

    @Override
    public List<ConsultaResponseDTO> obterConsultasPeloEmail(String token) {
        var consultas = consultaServiceImpl.buscarConsultasPorEmailProfissional(token);

        return consultas.stream().map(consultaMapper::toResponseDTOParaProfissional).toList();
    }
}
