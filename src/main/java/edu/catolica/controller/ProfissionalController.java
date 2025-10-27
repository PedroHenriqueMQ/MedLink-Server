package edu.catolica.controller;

import edu.catolica.model.enums.TurnoAtendimento;
import edu.catolica.service.usuario.ProfissionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
@RequiredArgsConstructor
public class ProfissionalController {
    private final ProfissionalService profissionalService;

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/turnos-atendimento/{email}")
    public void atualizarTurnosAtendimento(@RequestBody @Valid List<TurnoAtendimento> turnosAtendimento,
                                         @PathVariable("email") String email,
                                         @RequestHeader("token") String token) {
        profissionalService.atualizarTurnosAtendimento(email, token, turnosAtendimento);
    }
}
