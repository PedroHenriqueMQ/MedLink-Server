package service;

import edu.catolica.dto.response.ConsultaResponseDTO;
import edu.catolica.dto.response.ProfissionalResponseDTO;
import edu.catolica.exception.clinica.ClinicaInexistenteException;
import edu.catolica.mapper.ConsultaMapper;
import edu.catolica.mapper.UsuarioMapper;
import edu.catolica.model.Clinica;
import edu.catolica.model.Consulta;
import edu.catolica.model.Usuario;
import edu.catolica.model.enums.TipoUsuario;
import edu.catolica.repository.UsuarioRepository;
import edu.catolica.service.clinica.impl.ClinicaServiceImpl;
import edu.catolica.service.consulta.impl.ConsultaServiceImpl;
import edu.catolica.service.usuario.impl.ProfissionalServiceImpl;
import edu.catolica.service.usuario.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfissionalServiceImplTest {

    @Mock
    private UsuarioServiceImpl usuarioServiceImpl;

    @Mock
    private ClinicaServiceImpl clinicaServiceImpl;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ConsultaServiceImpl consultaServiceImpl;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private ConsultaMapper consultaMapper;

    @InjectMocks
    private ProfissionalServiceImpl profissionalService;

    @Test
    void deveObterProfissionaisPorClinicaComSucesso() {
        String razaoSocial = "Clinica Teste";
        Long clinicaId = 1L;

        Usuario profissional = new Usuario();
        profissional.setTipoUsuario(TipoUsuario.PROFISSIONAL);

        Usuario paciente = new Usuario();
        paciente.setTipoUsuario(TipoUsuario.PACIENTE);

        ProfissionalResponseDTO responseDTO = mock(ProfissionalResponseDTO.class);

        when(clinicaServiceImpl.obterIdPelaRazaoSocial(razaoSocial)).thenReturn(Optional.of(clinicaId));
        when(usuarioRepository.findAllByClinicaId(clinicaId)).thenReturn(List.of(profissional, paciente));
        when(usuarioMapper.toResponseDTO(profissional)).thenReturn(responseDTO);

        List<ProfissionalResponseDTO> resultado = profissionalService.obterProfissionaisPorClinica(razaoSocial);

        assertEquals(1, resultado.size());

        verify(clinicaServiceImpl).obterIdPelaRazaoSocial(razaoSocial);
        verify(usuarioRepository).findAllByClinicaId(clinicaId);
        verify(usuarioMapper).toResponseDTO(profissional);
        verify(usuarioMapper, never()).toResponseDTO(paciente);
    }

    @Test
    void deveLancarExcecaoQuandoClinicaNaoExisteAoBuscarProfissionais() {
        String razaoSocial = "Clinica Inexistente";

        when(clinicaServiceImpl.obterIdPelaRazaoSocial(razaoSocial)).thenReturn(Optional.empty());

        assertThrows(ClinicaInexistenteException.class, () ->
                profissionalService.obterProfissionaisPorClinica(razaoSocial)
        );

        verify(clinicaServiceImpl).obterIdPelaRazaoSocial(razaoSocial);
        verify(usuarioRepository, never()).findAllByClinicaId(anyLong());
    }

    @Test
    void deveObterProfissionaisPelaClinicaDoUsuarioComSucesso() {
        String token = "admin@email.com";
        Long clinicaId = 1L;

        Clinica clinica = new Clinica();
        clinica.setId(clinicaId);

        Usuario admin = new Usuario();
        admin.setClinica(clinica);

        Usuario profissional = new Usuario();
        profissional.setTipoUsuario(TipoUsuario.PROFISSIONAL);

        ProfissionalResponseDTO responseDTO = mock(ProfissionalResponseDTO.class);

        when(usuarioServiceImpl.obterUsuarioPeloEmail(token)).thenReturn(admin);
        when(usuarioRepository.findAllByClinicaId(clinicaId)).thenReturn(List.of(profissional));
        when(usuarioMapper.toResponseDTO(profissional)).thenReturn(responseDTO);

        List<ProfissionalResponseDTO> resultado = profissionalService.obterProfissionaisPelaClinicaDoUsuario(token);

        assertEquals(1, resultado.size());

        verify(usuarioServiceImpl).obterUsuarioPeloEmail(token);
        verify(usuarioRepository).findAllByClinicaId(clinicaId);
        verify(usuarioMapper).toResponseDTO(profissional);
    }

    @Test
    void deveRetornarListaVaziaSeNaoHouverProfissionaisNaClinicaDoUsuario() {
        String token = "admin@email.com";
        Long clinicaId = 1L;

        Clinica clinica = new Clinica();
        clinica.setId(clinicaId);

        Usuario admin = new Usuario();
        admin.setClinica(clinica);

        when(usuarioServiceImpl.obterUsuarioPeloEmail(token)).thenReturn(admin);
        when(usuarioRepository.findAllByClinicaId(clinicaId)).thenReturn(Collections.emptyList());

        List<ProfissionalResponseDTO> resultado = profissionalService.obterProfissionaisPelaClinicaDoUsuario(token);

        assertTrue(resultado.isEmpty());

        verify(usuarioServiceImpl).obterUsuarioPeloEmail(token);
        verify(usuarioRepository).findAllByClinicaId(clinicaId);
        verify(usuarioMapper, never()).toResponseDTO(any());
    }

    @Test
    void deveObterConsultasPeloEmailComSucesso() {
        String token = "profissional@email.com";
        Consulta consulta = new Consulta();
        ConsultaResponseDTO responseDTO = mock(ConsultaResponseDTO.class);

        when(consultaServiceImpl.buscarConsultasPorEmailProfissional(token)).thenReturn(List.of(consulta));
        when(consultaMapper.toResponseDTOParaProfissional(consulta)).thenReturn(responseDTO);

        List<ConsultaResponseDTO> resultado = profissionalService.obterConsultasPeloEmail(token);

        assertEquals(1, resultado.size());

        verify(consultaServiceImpl).buscarConsultasPorEmailProfissional(token);
        verify(consultaMapper).toResponseDTOParaProfissional(consulta);
    }
}
