package edu.catolica.controller;

import edu.catolica.dto.request.ProfissionalRequestDTO;
import edu.catolica.service.usuario.impl.AdministradorServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/administradores")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdministradorController {
    private final AdministradorServiceImpl administradorServiceImpl;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/profissional")
    public void cadastrarProfissional(@RequestBody @Valid ProfissionalRequestDTO profissionalRequestDTO,
                                      @RequestHeader("token") String token) {
        administradorServiceImpl.cadastrarProfissional(profissionalRequestDTO, token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/profissional")
    public void atualizarProfissional(@RequestBody @Valid ProfissionalRequestDTO profissionalRequestDTO,
                                      @RequestHeader("token") String token) {
        administradorServiceImpl.atualizarProfissional(profissionalRequestDTO, token);
    }
}
