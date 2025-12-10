package edu.catolica.controller;

import edu.catolica.dto.request.UsuarioLoginRequestDTO;
import edu.catolica.model.Usuario;
import edu.catolica.service.usuario.impl.UsuarioServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UsuarioController {
    private final UsuarioServiceImpl usuarioServiceImpl;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public String login(@RequestBody @Valid UsuarioLoginRequestDTO usuarioLoginRequestDTO) {
        return usuarioServiceImpl.login(usuarioLoginRequestDTO.email(), usuarioLoginRequestDTO.senha());
    }

    @GetMapping("/{email}")
    public ResponseEntity<Usuario> verificarUsuarioPeloEmail(@PathVariable("email") String email) {
        Usuario usuario = usuarioServiceImpl.obterUsuarioPeloEmail(email);
        return ResponseEntity.ok(usuario);
    }
}
