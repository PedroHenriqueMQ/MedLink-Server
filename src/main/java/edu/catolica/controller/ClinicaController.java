package edu.catolica.controller;

import edu.catolica.service.clinica.ClinicaService;
import edu.catolica.service.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clinicas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClinicaController {
    private final ClinicaService clinicaService;
    private final UsuarioService usuarioService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<String> obterTodasRazoesSociais() {
        return clinicaService.obterTodasRazoesSociais();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{email}")
    public List<String> obterRazaoSocialPeloEmail(@PathVariable("email") String email) {
        return usuarioService.obterRazaoSocialPeloEmail(email);
    }
}
