package service;

import edu.catolica.exception.clinica.ClinicaInexistenteException;
import edu.catolica.model.Clinica;
import edu.catolica.repository.ClinicaRepository;
import edu.catolica.service.clinica.impl.ClinicaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClinicaServiceImplTest {

    @Mock
    private ClinicaRepository clinicaRepository;

    @InjectMocks
    private ClinicaServiceImpl clinicaService;

    @Test
    void deveConsultarClinicaExistenteComSucesso() {
        String razaoSocial = "Clínica Saúde Total";
        Clinica clinica = new Clinica();
        clinica.setId(1L);
        clinica.setRazaoSocial(razaoSocial);

        when(clinicaRepository.findByRazaoSocial(razaoSocial)).thenReturn(Optional.of(clinica));

        Clinica resultado = clinicaService.consultarClinicaExistente(razaoSocial);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(razaoSocial, resultado.getRazaoSocial());

        verify(clinicaRepository).findByRazaoSocial(anyString());
    }

    @Test
    void deveLancarExcecaoQuandoClinicaNaoExistir() {
        String razaoSocial = "Clínica Inexistente";

        when(clinicaRepository.findByRazaoSocial(razaoSocial)).thenReturn(Optional.empty());

        ClinicaInexistenteException exception = assertThrows(
                ClinicaInexistenteException.class,
                () -> clinicaService.consultarClinicaExistente(razaoSocial)
        );

        assertNotNull(exception);

        verify(clinicaRepository).findByRazaoSocial(anyString());
    }

    @Test
    void deveObterTodasRazoesSociaisComSucesso() {
        Clinica clinica1 = new Clinica();
        clinica1.setId(1L);
        clinica1.setRazaoSocial("Clínica A");

        Clinica clinica2 = new Clinica();
        clinica2.setId(2L);
        clinica2.setRazaoSocial("Clínica B");

        Clinica clinica3 = new Clinica();
        clinica3.setId(3L);
        clinica3.setRazaoSocial("Clínica C");

        List<Clinica> clinicas = Arrays.asList(clinica1, clinica2, clinica3);

        when(clinicaRepository.findAll()).thenReturn(clinicas);

        List<String> resultado = clinicaService.obterTodasRazoesSociais();

        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertTrue(resultado.contains("Clínica A"));
        assertTrue(resultado.contains("Clínica B"));
        assertTrue(resultado.contains("Clínica C"));

        verify(clinicaRepository).findAll();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverClinicas() {
        when(clinicaRepository.findAll()).thenReturn(List.of());

        List<String> resultado = clinicaService.obterTodasRazoesSociais();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.size());

        verify(clinicaRepository).findAll();
    }

    @Test
    void deveObterIdPelaRazaoSocialComSucesso() {
        String razaoSocial = "Clínica Saúde Total";
        Clinica clinica = new Clinica();
        clinica.setId(10L);
        clinica.setRazaoSocial(razaoSocial);

        when(clinicaRepository.findByRazaoSocial(razaoSocial)).thenReturn(Optional.of(clinica));

        Optional<Long> resultado = clinicaService.obterIdPelaRazaoSocial(razaoSocial);

        assertTrue(resultado.isPresent());
        assertEquals(10L, resultado.get());

        verify(clinicaRepository).findByRazaoSocial(anyString());
    }

    @Test
    void deveRetornarOptionalVazioQuandoClinicaNaoExistirNaConsultaPorId() {
        String razaoSocial = "Clínica Inexistente";

        when(clinicaRepository.findByRazaoSocial(razaoSocial)).thenReturn(Optional.empty());

        Optional<Long> resultado = clinicaService.obterIdPelaRazaoSocial(razaoSocial);

        assertFalse(resultado.isPresent());
        assertTrue(resultado.isEmpty());

        verify(clinicaRepository).findByRazaoSocial(anyString());
    }
}
