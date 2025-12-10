package edu.catolica.controller;

import edu.catolica.dto.request.ConsultaRequestDTO;
import edu.catolica.service.consulta.ConsultaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import edu.catolica.dto.request.UsuarioPacienteRequestDTO;
import edu.catolica.service.usuario.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public final class PacienteController {
    private final PacienteService pacienteService;
    private final ConsultaService consultaService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public String cadastrarPaciente(@RequestBody @Valid UsuarioPacienteRequestDTO usuarioPacienteRequestDTO) {
        return pacienteService.cadastrarPaciente(usuarioPacienteRequestDTO);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("consultas/solicitar")
    public void solicitarConsulta(@RequestHeader("token") String token,
                                    @RequestBody @Valid ConsultaRequestDTO consultaRequestDTO) {
        consultaService.solicitarConsulta(token, consultaRequestDTO);
    }
}
