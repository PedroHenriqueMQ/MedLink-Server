package edu.catolica.service.usuario;

import edu.catolica.exception.UsuarioInexistenteException;
import edu.catolica.model.Consulta;
import edu.catolica.model.enums.StatusConsulta;
import edu.catolica.model.enums.TipoUsuario;
import edu.catolica.model.enums.TurnoAtendimento;
import edu.catolica.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ProfissionalService {
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    public void atualizarTurnosAtendimento(String email, String token, List<TurnoAtendimento> turnosAtendimento) {
        var profissional = usuarioService.verificarRequisicao(token, TipoUsuario.PROFISSIONAL);
        var clinica = profissional.getClinica();
        var usuario = usuarioRepository.findByClinicaIdAndEmail(clinica.getId(), email)
                .orElseThrow(() -> new UsuarioInexistenteException(email));
        usuarioService.verificarAutoRequisicao(email, token);

        usuario.setTurnosAtendimento(turnosAtendimento);
        usuarioRepository.save(usuario);
    }

    public boolean analisarDisponibilidade(List<Consulta> consultas, LocalTime horaInicio, LocalTime horaFim) {
        var consultasMarcadas = consultas.stream().filter(
                (consulta) -> consulta.getStatusConsulta() == StatusConsulta.CONFIRMADA)
                .toList();

        for (Consulta consultaMarcada : consultasMarcadas) {
            if (consultaMarcada.getHoraInicio().equals(horaInicio) || consultaMarcada.getHoraFim().equals(horaFim))
                return false;

            if (Duration.between(consultaMarcada.getHoraInicio(), horaInicio).toHours() != 1)
                return false;

            if (Duration.between(consultaMarcada.getHoraFim(), horaFim).toHours() != 1)
                return false;

            if (consultaMarcada.getHoraInicio().isBefore(LocalTime.of(12, 0)) &&
                !consultaMarcada.getProfissional().getTurnosAtendimento().contains(TurnoAtendimento.MATUTINO))
                return false;

            if (consultaMarcada.getHoraFim().isAfter(LocalTime.of(13, 0)) &&
                !consultaMarcada.getProfissional().getTurnosAtendimento().contains(TurnoAtendimento.VESPERTINO))
                return false;
        }

        return true;
    }

}
