package edu.catolica.controller;

import edu.catolica.service.clinica.impl.ClinicaServiceImpl;
import edu.catolica.service.usuario.impl.UsuarioServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clinicas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ClinicaController {
    private final ClinicaServiceImpl clinicaServiceImpl;
    private final UsuarioServiceImpl usuarioServiceImpl;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<String> obterTodasRazoesSociais() {
        return clinicaServiceImpl.obterTodasRazoesSociais();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{email}")
    public List<String> obterRazaoSocialPeloEmail(@PathVariable("email") String email) {
        return usuarioServiceImpl.obterRazaoSocialPeloEmail(email);
    }
}
