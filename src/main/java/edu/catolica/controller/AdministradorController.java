package edu.catolica.controller;

import edu.catolica.dto.request.ProfissionalRequestDTO;
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
    public void cadastrarProfissional(@RequestBody @Valid ProfissionalRequestDTO profissionalRequestDTO,
                                      @RequestHeader("token") String token) {
        administradorService.cadastrarProfissional(profissionalRequestDTO, token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/profissional")
    public void atualizarProfissional(@RequestBody @Valid ProfissionalRequestDTO profissionalRequestDTO,
                                      @RequestHeader("token") String token) {
        administradorService.atualizarProfissional(profissionalRequestDTO, token);
    }
}
