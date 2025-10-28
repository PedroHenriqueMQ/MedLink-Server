package edu.catolica.controller;

import edu.catolica.dto.UsuarioProfissionalDTO;
import edu.catolica.service.usuario.AdministradorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administradores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdministradorController {
    private final AdministradorService administradorService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/profissional")
    public void cadastrarProfissional(@RequestBody @Valid UsuarioProfissionalDTO usuarioProfissionalDTO,
                                      @RequestHeader("token") String token) {
        administradorService.cadastrarProfissional(usuarioProfissionalDTO, token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/profissional")
    public void atualizarProfissional(@RequestBody @Valid UsuarioProfissionalDTO usuarioProfissionalDTO,
                                      @RequestHeader("token") String token) {
        administradorService.atualizarProfissional(usuarioProfissionalDTO, token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/profissional/{email}")
    public void atualizarEstadoAtividadeProfissional(@PathVariable("email") String email,
                                                     @RequestParam boolean estadoInativo,
                                                     @RequestHeader("token") String token) {
        administradorService.atualizarEstadoProfissional(email, token, estadoInativo);
    }
}
