package edu.catolica.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import edu.catolica.dto.UsuarioPacienteDTO;
import edu.catolica.service.usuario.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public final class PacienteController {
    private final PacienteService pacienteService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public String cadastrarPaciente(@RequestBody @Valid UsuarioPacienteDTO usuarioPacienteDTO) {
        return pacienteService.cadastrarPaciente(usuarioPacienteDTO);
    }
}
