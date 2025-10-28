package edu.catolica.controller;

import edu.catolica.dto.UsuarioLoginDTO;
import edu.catolica.service.usuario.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    @CrossOrigin(origins = "*")
    public String login(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO) {
        return usuarioService.login(usuarioLoginDTO.email(), usuarioLoginDTO.senha());
    }
}
