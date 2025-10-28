package edu.catolica.controller;

import edu.catolica.dto.UsuarioLoginDTO;
import edu.catolica.model.Usuario;
import edu.catolica.service.usuario.UsuarioService;
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
    private final UsuarioService usuarioService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public String login(@RequestBody @Valid UsuarioLoginDTO usuarioLoginDTO) {
        return usuarioService.login(usuarioLoginDTO.email(), usuarioLoginDTO.senha());
    }

    @GetMapping("/{email}")
    public ResponseEntity<Usuario> verificarUsuarioPeloEmail(@PathVariable("email") String email) {
        Usuario usuario = usuarioService.verificarUsuarioPeloEmail(email);
        return ResponseEntity.ok(usuario);
    }
}
