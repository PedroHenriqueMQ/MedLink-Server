package edu.catolica.controller;

import edu.catolica.dto.UsuarioProfissionalDTO;
import edu.catolica.service.AdministradorService;
import edu.catolica.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administradores")
@RequiredArgsConstructor
public class AdministradorController {
    private final UsuarioService usuarioService;
    private final AdministradorService administradorService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/profissionais")
    public void cadastrarProfissional(@RequestBody @Valid UsuarioProfissionalDTO usuarioProfissionalDTO,
                                      @RequestHeader("token") String token) {
        administradorService.cadastrarProfissional(usuarioProfissionalDTO, token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/profissionais")
    public void atualizarProfissional(@RequestBody @Valid UsuarioProfissionalDTO usuarioProfissionalDTO,
                                      @RequestHeader("token") String token) {
        administradorService.atualizarProfissional(usuarioProfissionalDTO, token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/profissionais/{email}")
    public void atualizarEstadoAtividadeProfissional(@PathVariable("email") String email,
                                                     @RequestParam boolean estadoInativo,
                                                     @RequestHeader("token") String token) {
        administradorService.atualizarEstadoProfissional(email, token, estadoInativo);
    }
}
