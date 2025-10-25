package edu.catolica.controller;

import edu.catolica.dto.UsuarioLoginDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import edu.catolica.dto.UsuarioPacienteDTO;
import edu.catolica.service.PacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public final class PacienteController {
    private final PacienteService pacienteService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void cadastrarPaciente(@RequestBody @Valid UsuarioPacienteDTO usuarioPacienteDTO) {
        pacienteService.cadastrarPaciente(usuarioPacienteDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public void login(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO) {
        pacienteService.login(usuarioLoginDTO.email(), usuarioLoginDTO.senha());
    }
}
