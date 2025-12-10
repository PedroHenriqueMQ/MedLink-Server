package service;

import edu.catolica.dto.request.UsuarioPacienteRequestDTO;
import edu.catolica.dto.response.ConsultaResponseDTO;
import edu.catolica.mapper.ConsultaMapper;
import edu.catolica.mapper.UsuarioMapper;
import edu.catolica.model.Clinica;
import edu.catolica.model.Consulta;
import edu.catolica.model.Usuario;
import edu.catolica.model.enums.StatusConsulta;
import edu.catolica.model.enums.TipoUsuario;
import edu.catolica.repository.UsuarioRepository;
import edu.catolica.service.clinica.impl.ClinicaServiceImpl;
import edu.catolica.service.consulta.impl.ConsultaServiceImpl;
import edu.catolica.service.usuario.impl.PacienteServiceImpl;
import edu.catolica.service.usuario.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PacienteServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioServiceImpl usuarioServiceImpl;

    @Mock
    private ClinicaServiceImpl clinicaServiceImpl;

    @Mock
    private ConsultaServiceImpl consultaServiceImpl;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private ConsultaMapper consultaMapper;

    @InjectMocks
    private PacienteServiceImpl pacienteService;

    @Test
    void deveCadastrarPacienteComSucesso() {
        String clinicaNome = "Clinica Saude";
        String email = "paciente@email.com";

        UsuarioPacienteRequestDTO dto = new UsuarioPacienteRequestDTO(
                clinicaNome,
                "Nome Paciente",
                email,
                "senha123",
                "12345678900",
                LocalDate.of(1990, 1, 1)
        );

        Clinica clinica = new Clinica();
        clinica.setRazaoSocial(clinicaNome);

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setTipoUsuario(TipoUsuario.PACIENTE);

        when(clinicaServiceImpl.consultarClinicaExistente(clinicaNome)).thenReturn(clinica);
        when(usuarioMapper.toEntityPacienteCadastro(dto, clinica)).thenReturn(usuario);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        String resultado = pacienteService.cadastrarPaciente(dto);

        assertEquals(TipoUsuario.PACIENTE.toString(), resultado);

        verify(clinicaServiceImpl).consultarClinicaExistente(clinicaNome);
        verify(usuarioServiceImpl).verificarEmailDuplicado(email, clinica);
        verify(usuarioMapper).toEntityPacienteCadastro(dto, clinica);
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveObterConsultasPeloEmailComSucesso() {
        String token = "paciente@email.com";

        Consulta consulta = new Consulta();
        consulta.setId(1L);

        ConsultaResponseDTO responseDTO = new ConsultaResponseDTO(
                1L,
                "Nome Medico",
                "medico@email.com",
                "Clinica Saude",
                null,
                LocalDate.now(),
                LocalTime.now(),
                LocalTime.now().plusHours(1),
                null,
                null,
                StatusConsulta.CONFIRMADA
        );

        when(consultaServiceImpl.buscarConsultasPorEmailPaciente(token)).thenReturn(List.of(consulta));
        when(consultaMapper.toResponseDTOParaPaciente(consulta)).thenReturn(responseDTO);

        List<ConsultaResponseDTO> resultado = pacienteService.obterConsultasPeloEmail(token);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).id());

        verify(consultaServiceImpl).buscarConsultasPorEmailPaciente(token);
        verify(consultaMapper).toResponseDTOParaPaciente(any(Consulta.class));
    }
}
