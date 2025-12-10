package edu.catolica.controller;

import edu.catolica.dto.request.ConsultaRequestDTO;
import edu.catolica.service.consulta.impl.ConsultaServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import edu.catolica.dto.request.UsuarioPacienteRequestDTO;
import edu.catolica.service.usuario.impl.PacienteServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public final class PacienteController {
    private final PacienteServiceImpl pacienteServiceImpl;
    private final ConsultaServiceImpl consultaServiceImpl;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public String cadastrarPaciente(@RequestBody @Valid UsuarioPacienteRequestDTO usuarioPacienteRequestDTO) {
        return pacienteServiceImpl.cadastrarPaciente(usuarioPacienteRequestDTO);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("consultas/solicitar")
    public void solicitarConsulta(@RequestHeader("token") String token,
                                    @RequestBody @Valid ConsultaRequestDTO consultaRequestDTO) {
        consultaServiceImpl.solicitarConsulta(token, consultaRequestDTO);
    }
}
