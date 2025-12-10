package service;

import edu.catolica.dto.request.ConsultaRequestDTO;
import edu.catolica.dto.response.ConsultaResponseDTO;
import edu.catolica.exception.consulta.ConsultaDuplicadaException;
import edu.catolica.exception.consulta.ConsultaInexistenteException;
import edu.catolica.exception.consulta.JustificativaAusenteException;
import edu.catolica.exception.consulta.StatusInvalidoConsultaException;
import edu.catolica.exception.usuario.UsuarioInvalidoException;
import edu.catolica.mapper.ConsultaMapper;
import edu.catolica.model.Clinica;
import edu.catolica.model.Consulta;
import edu.catolica.model.Usuario;
import edu.catolica.model.enums.StatusConsulta;
import edu.catolica.model.enums.TipoUsuario;
import edu.catolica.repository.ConsultaRepository;
import edu.catolica.service.consulta.impl.ConsultaServiceImpl;
import edu.catolica.service.usuario.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultaServiceImplTest {
    @Mock
    private UsuarioServiceImpl usuarioServiceImpl;

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private ConsultaMapper consultaMapper;

    @InjectMocks
    private ConsultaServiceImpl consultaService;

    @Test
    void deveSolicitarConsultaComSucesso() {
        String token = "paciente@email.com";
        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                "profissional@email.com",
                "Clinica Teste",
                null,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                null,
                null,
                StatusConsulta.SOLICITADA
        );

        Usuario paciente = criarUsuario(1L, token, TipoUsuario.PACIENTE);
        Usuario profissional = criarUsuario(2L, "profissional@email.com", TipoUsuario.PROFISSIONAL);
        Clinica clinica = new Clinica();
        Consulta consulta = new Consulta();

        when(usuarioServiceImpl.obterUsuarioPeloEmail(token)).thenReturn(paciente);
        when(usuarioServiceImpl.obterUsuarioPeloEmail("profissional@email.com")).thenReturn(profissional);
        when(usuarioServiceImpl.verificarClinicaCoincidente(token, "profissional@email.com")).thenReturn(clinica);
        when(consultaRepository.findByProfissionalIdAndDataConsultaAndHoraInicio(anyLong(), any(), any()))
                .thenReturn(Optional.empty());
        when(consultaRepository.findByPacienteIdAndDataConsultaAndHoraInicio(anyLong(), any(), any()))
                .thenReturn(Optional.empty());
        when(consultaMapper.toEntity(dto, paciente, profissional, clinica)).thenReturn(consulta);

        consultaService.solicitarConsulta(token, dto);

        verify(usuarioServiceImpl).obterUsuarioPeloEmail(token);
        verify(usuarioServiceImpl).obterUsuarioPeloEmail("profissional@email.com");
        verify(usuarioServiceImpl).verificarClinicaCoincidente(anyString(), anyString());
        verify(consultaRepository).findByProfissionalIdAndDataConsultaAndHoraInicio(anyLong(), any(), any());
        verify(consultaRepository).findByPacienteIdAndDataConsultaAndHoraInicio(anyLong(), any(), any());
        verify(consultaRepository).save(any(Consulta.class));
    }

    @Test
    void deveLancarExcecaoQuandoStatusConsultaInvalido() {
        String token = "paciente@email.com";
        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                "profissional@email.com",
                "Clinica Teste",
                null,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                null,
                null,
                StatusConsulta.CONFIRMADA
        );

        StatusInvalidoConsultaException exception = assertThrows(
                StatusInvalidoConsultaException.class,
                () -> consultaService.solicitarConsulta(token, dto)
        );

        assertNotNull(exception);
        verify(usuarioServiceImpl, never()).obterUsuarioPeloEmail(anyString());
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoDestinatarioNaoEProfissional() {
        String token = "paciente@email.com";
        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                "outro@email.com",
                "Clinica Teste",
                null,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                null,
                null,
                StatusConsulta.SOLICITADA
        );

        Usuario paciente = criarUsuario(1L, token, TipoUsuario.PACIENTE);
        Usuario outroPaciente = criarUsuario(2L, "outro@email.com", TipoUsuario.PACIENTE);

        when(usuarioServiceImpl.obterUsuarioPeloEmail(token)).thenReturn(paciente);
        when(usuarioServiceImpl.obterUsuarioPeloEmail("outro@email.com")).thenReturn(outroPaciente);

        UsuarioInvalidoException exception = assertThrows(
                UsuarioInvalidoException.class,
                () -> consultaService.solicitarConsulta(token, dto)
        );

        assertNotNull(exception);
        verify(usuarioServiceImpl, times(2)).obterUsuarioPeloEmail(anyString());
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoProfissionalJaPossuiConsultaNoHorario() {
        String token = "paciente@email.com";
        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                "profissional@email.com",
                "Clinica Teste",
                null,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                null,
                null,
                StatusConsulta.SOLICITADA
        );

        Usuario paciente = criarUsuario(1L, token, TipoUsuario.PACIENTE);
        Usuario profissional = criarUsuario(2L, "profissional@email.com", TipoUsuario.PROFISSIONAL);
        Clinica clinica = new Clinica();
        Consulta consultaExistente = new Consulta();
        consultaExistente.setStatusConsulta(StatusConsulta.CONFIRMADA);

        when(usuarioServiceImpl.obterUsuarioPeloEmail(token)).thenReturn(paciente);
        when(usuarioServiceImpl.obterUsuarioPeloEmail("profissional@email.com")).thenReturn(profissional);
        when(usuarioServiceImpl.verificarClinicaCoincidente(anyString(), anyString())).thenReturn(clinica);
        when(consultaRepository.findByProfissionalIdAndDataConsultaAndHoraInicio(anyLong(), any(), any()))
                .thenReturn(Optional.of(consultaExistente));

        ConsultaDuplicadaException exception = assertThrows(
                ConsultaDuplicadaException.class,
                () -> consultaService.solicitarConsulta(token, dto)
        );

        assertNotNull(exception);
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void devePermitirConsultaQuandoProfissionalPossuiConsultaCanceladaNoHorario() {
        String token = "paciente@email.com";
        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                "profissional@email.com",
                "Clinica Teste",
                null,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                null,
                null,
                StatusConsulta.SOLICITADA
        );

        Usuario paciente = criarUsuario(1L, token, TipoUsuario.PACIENTE);
        Usuario profissional = criarUsuario(2L, "profissional@email.com", TipoUsuario.PROFISSIONAL);
        Clinica clinica = new Clinica();
        Consulta consultaCancelada = new Consulta();
        consultaCancelada.setStatusConsulta(StatusConsulta.CANCELADA);
        Consulta novaConsulta = new Consulta();

        when(usuarioServiceImpl.obterUsuarioPeloEmail(token)).thenReturn(paciente);
        when(usuarioServiceImpl.obterUsuarioPeloEmail("profissional@email.com")).thenReturn(profissional);
        when(usuarioServiceImpl.verificarClinicaCoincidente(anyString(), anyString())).thenReturn(clinica);
        when(consultaRepository.findByProfissionalIdAndDataConsultaAndHoraInicio(anyLong(), any(), any()))
                .thenReturn(Optional.of(consultaCancelada));
        when(consultaRepository.findByPacienteIdAndDataConsultaAndHoraInicio(anyLong(), any(), any()))
                .thenReturn(Optional.empty());
        when(consultaMapper.toEntity(dto, paciente, profissional, clinica)).thenReturn(novaConsulta);

        consultaService.solicitarConsulta(token, dto);

        verify(consultaRepository).save(any(Consulta.class));
    }

    @Test
    void deveLancarExcecaoQuandoPacienteJaPossuiConsultaNoHorario() {
        String token = "paciente@email.com";
        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                "profissional@email.com",
                "Clinica Teste",
                null,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                null,
                null,
                StatusConsulta.SOLICITADA
        );

        Usuario paciente = criarUsuario(1L, token, TipoUsuario.PACIENTE);
        Usuario profissional = criarUsuario(2L, "profissional@email.com", TipoUsuario.PROFISSIONAL);
        Clinica clinica = new Clinica();
        Consulta consultaExistente = new Consulta();
        consultaExistente.setStatusConsulta(StatusConsulta.SOLICITADA);

        when(usuarioServiceImpl.obterUsuarioPeloEmail(token)).thenReturn(paciente);
        when(usuarioServiceImpl.obterUsuarioPeloEmail("profissional@email.com")).thenReturn(profissional);
        when(usuarioServiceImpl.verificarClinicaCoincidente(anyString(), anyString())).thenReturn(clinica);
        when(consultaRepository.findByProfissionalIdAndDataConsultaAndHoraInicio(anyLong(), any(), any()))
                .thenReturn(Optional.empty());
        when(consultaRepository.findByPacienteIdAndDataConsultaAndHoraInicio(anyLong(), any(), any()))
                .thenReturn(Optional.of(consultaExistente));

        ConsultaDuplicadaException exception = assertThrows(
                ConsultaDuplicadaException.class,
                () -> consultaService.solicitarConsulta(token, dto)
        );

        assertNotNull(exception);
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void devePermitirConsultaQuandoPacientePossuiConsultaCanceladaNoHorario() {
        String token = "paciente@email.com";
        ConsultaRequestDTO dto = new ConsultaRequestDTO(
                "profissional@email.com",
                "Clinica Teste",
                null,
                LocalDate.now().plusDays(1),
                LocalTime.of(10, 0),
                null,
                null,
                StatusConsulta.SOLICITADA
        );

        Usuario paciente = criarUsuario(1L, token, TipoUsuario.PACIENTE);
        Usuario profissional = criarUsuario(2L, "profissional@email.com", TipoUsuario.PROFISSIONAL);
        Clinica clinica = new Clinica();
        Consulta consultaCancelada = new Consulta();
        consultaCancelada.setStatusConsulta(StatusConsulta.CANCELADA);
        Consulta novaConsulta = new Consulta();

        when(usuarioServiceImpl.obterUsuarioPeloEmail(token)).thenReturn(paciente);
        when(usuarioServiceImpl.obterUsuarioPeloEmail("profissional@email.com")).thenReturn(profissional);
        when(usuarioServiceImpl.verificarClinicaCoincidente(anyString(), anyString())).thenReturn(clinica);
        when(consultaRepository.findByProfissionalIdAndDataConsultaAndHoraInicio(anyLong(), any(), any()))
                .thenReturn(Optional.empty());
        when(consultaRepository.findByPacienteIdAndDataConsultaAndHoraInicio(anyLong(), any(), any()))
                .thenReturn(Optional.of(consultaCancelada));
        when(consultaMapper.toEntity(dto, paciente, profissional, clinica)).thenReturn(novaConsulta);

        consultaService.solicitarConsulta(token, dto);

        verify(consultaRepository).save(any(Consulta.class));
    }

    @Test
    void deveConfirmarConsultaComSucesso() {
        Long consultaId = 1L;
        Consulta consulta = new Consulta();
        consulta.setId(consultaId);
        consulta.setStatusConsulta(StatusConsulta.SOLICITADA);
        ConsultaResponseDTO responseDTO = new ConsultaResponseDTO(
                consultaId,
                "Nome Paciente",
                "paciente@email.com",
                "Clinica Teste",
                null,
                LocalDate.now(),
                LocalTime.of(10, 0),
                null,
                null,
                null,
                StatusConsulta.CONFIRMADA
        );

        when(consultaRepository.findById(consultaId)).thenReturn(Optional.of(consulta));
        when(consultaMapper.toResponseDTOParaProfissional(consulta)).thenReturn(responseDTO);

        ConsultaResponseDTO resultado = consultaService.confirmarConsulta(consultaId);

        assertNotNull(resultado);
        assertEquals(StatusConsulta.CONFIRMADA, resultado.statusConsulta());
        assertEquals(StatusConsulta.CONFIRMADA, consulta.getStatusConsulta());

        verify(consultaRepository).findById(anyLong());
        verify(consultaRepository).save(any(Consulta.class));
        verify(consultaMapper).toResponseDTOParaProfissional(any(Consulta.class));
    }

    @Test
    void deveLancarExcecaoQuandoConsultaNaoExistirAoConfirmar() {
        Long consultaId = 999L;

        when(consultaRepository.findById(consultaId)).thenReturn(Optional.empty());

        ConsultaInexistenteException exception = assertThrows(
                ConsultaInexistenteException.class,
                () -> consultaService.confirmarConsulta(consultaId)
        );

        assertNotNull(exception);
        verify(consultaRepository).findById(anyLong());
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void deveNegarConsultaComSucesso() {
        Long consultaId = 1L;
        String justificativa = "Paciente não compareceu";
        Consulta consulta = new Consulta();
        consulta.setId(consultaId);
        consulta.setStatusConsulta(StatusConsulta.SOLICITADA);
        ConsultaResponseDTO responseDTO = new ConsultaResponseDTO(
                consultaId,
                "Nome Paciente",
                "paciente@email.com",
                "Clinica Teste",
                null,
                LocalDate.now(),
                LocalTime.of(10, 0),
                null,
                null,
                justificativa,
                StatusConsulta.CANCELADA
        );

        when(consultaRepository.findById(consultaId)).thenReturn(Optional.of(consulta));
        when(consultaMapper.toResponseDTOParaProfissional(consulta)).thenReturn(responseDTO);

        ConsultaResponseDTO resultado = consultaService.negarConsulta(consultaId, justificativa);

        assertNotNull(resultado);
        assertEquals(StatusConsulta.CANCELADA, resultado.statusConsulta());
        assertEquals(justificativa, resultado.justificativa());
        assertEquals(StatusConsulta.CANCELADA, consulta.getStatusConsulta());
        assertEquals(justificativa, consulta.getJustificativa());

        verify(consultaRepository).findById(anyLong());
        verify(consultaRepository).save(any(Consulta.class));
        verify(consultaMapper).toResponseDTOParaProfissional(any(Consulta.class));
    }

    @Test
    void deveLancarExcecaoQuandoJustificativaForNula() {
        Long consultaId = 1L;
        String justificativa = null;

        JustificativaAusenteException exception = assertThrows(
                JustificativaAusenteException.class,
                () -> consultaService.negarConsulta(consultaId, justificativa)
        );

        assertNotNull(exception);
        verify(consultaRepository, never()).findById(anyLong());
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoJustificativaForVazia() {
        Long consultaId = 1L;
        String justificativa = "";

        JustificativaAusenteException exception = assertThrows(
                JustificativaAusenteException.class,
                () -> consultaService.negarConsulta(consultaId, justificativa)
        );

        assertNotNull(exception);
        verify(consultaRepository, never()).findById(anyLong());
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoJustificativaForApenasEspacos() {
        Long consultaId = 1L;
        String justificativa = "   ";

        JustificativaAusenteException exception = assertThrows(
                JustificativaAusenteException.class,
                () -> consultaService.negarConsulta(consultaId, justificativa)
        );

        assertNotNull(exception);
        verify(consultaRepository, never()).findById(anyLong());
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoConsultaNaoExistirAoNegar() {
        Long consultaId = 999L;
        String justificativa = "Motivo válido";

        when(consultaRepository.findById(consultaId)).thenReturn(Optional.empty());

        ConsultaInexistenteException exception = assertThrows(
                ConsultaInexistenteException.class,
                () -> consultaService.negarConsulta(consultaId, justificativa)
        );

        assertNotNull(exception);
        verify(consultaRepository).findById(anyLong());
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void deveBuscarConsultasPorEmailProfissionalComSucesso() {
        String email = "profissional@email.com";
        Usuario profissional = criarUsuario(1L, email, TipoUsuario.PROFISSIONAL);
        Consulta consulta1 = new Consulta();
        Consulta consulta2 = new Consulta();
        List<Consulta> consultas = Arrays.asList(consulta1, consulta2);

        when(usuarioServiceImpl.obterUsuarioPeloEmail(email)).thenReturn(profissional);
        when(consultaRepository.findAllByProfissionalId(1L)).thenReturn(consultas);

        List<Consulta> resultado = consultaService.buscarConsultasPorEmailProfissional(email);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(usuarioServiceImpl).obterUsuarioPeloEmail(anyString());
        verify(consultaRepository).findAllByProfissionalId(anyLong());
    }

    @Test
    void deveBuscarConsultasPorEmailPacienteComSucesso() {
        String token = "paciente@email.com";
        Usuario paciente = criarUsuario(1L, token, TipoUsuario.PACIENTE);
        Consulta consulta1 = new Consulta();
        Consulta consulta2 = new Consulta();
        Consulta consulta3 = new Consulta();
        List<Consulta> consultas = Arrays.asList(consulta1, consulta2, consulta3);

        when(usuarioServiceImpl.obterUsuarioPeloEmail(token)).thenReturn(paciente);
        when(consultaRepository.findAllByPacienteId(1L)).thenReturn(consultas);

        List<Consulta> resultado = consultaService.buscarConsultasPorEmailPaciente(token);

        assertNotNull(resultado);
        assertEquals(3, resultado.size());

        verify(usuarioServiceImpl).obterUsuarioPeloEmail(anyString());
        verify(consultaRepository).findAllByPacienteId(anyLong());
    }

    @Test
    void deveRetornarListaVaziaQuandoProfissionalNaoTemConsultas() {
        String email = "profissional@email.com";
        Usuario profissional = criarUsuario(1L, email, TipoUsuario.PROFISSIONAL);

        when(usuarioServiceImpl.obterUsuarioPeloEmail(email)).thenReturn(profissional);
        when(consultaRepository.findAllByProfissionalId(1L)).thenReturn(List.of());

        List<Consulta> resultado = consultaService.buscarConsultasPorEmailProfissional(email);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(usuarioServiceImpl).obterUsuarioPeloEmail(anyString());
        verify(consultaRepository).findAllByProfissionalId(anyLong());
    }

    @Test
    void deveRetornarListaVaziaQuandoPacienteNaoTemConsultas() {
        String token = "paciente@email.com";
        Usuario paciente = criarUsuario(1L, token, TipoUsuario.PACIENTE);

        when(usuarioServiceImpl.obterUsuarioPeloEmail(token)).thenReturn(paciente);
        when(consultaRepository.findAllByPacienteId(1L)).thenReturn(List.of());

        List<Consulta> resultado = consultaService.buscarConsultasPorEmailPaciente(token);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(usuarioServiceImpl).obterUsuarioPeloEmail(anyString());
        verify(consultaRepository).findAllByPacienteId(anyLong());
    }

    private Usuario criarUsuario(Long id, String email, TipoUsuario tipo) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setEmail(email);
        usuario.setTipoUsuario(tipo);
        return usuario;
    }
}
