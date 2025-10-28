package edu.catolica.controller;

import edu.catolica.dto.ConsultaDTO;
import edu.catolica.service.consulta.ConsultaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ConsultaController {
    private final ConsultaService consultaService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/solicitar")
    public String solicitarConsulta(@RequestHeader("token") String token,
                                    @RequestBody @Valid ConsultaDTO consultaDTO) {
        return consultaService.solicitarConsulta(token, consultaDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/confirmar")
    public String confirmarConsulta(@RequestHeader("token") String token,
                                    @RequestBody @Valid ConsultaDTO consultaDTO) {
        return consultaService.confirmarConsulta(token, consultaDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/cancelar-marcada")
    public void cancelarConsultaMarcada(@RequestHeader("token") String token,
                                        @RequestBody @Valid ConsultaDTO consultaDTO) {
        consultaService.cancelarConsultaMarcada(token, consultaDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/negar-consulta")
    public void negarConsulta(@RequestHeader("token") String token,
                              @RequestBody @Valid ConsultaDTO consultaDTO) {
        consultaService.negarConsulta(token, consultaDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/cancelar-solicitacao")
    public void cancelarSolicitacao(@RequestHeader("token") String token,
                              @RequestBody @Valid ConsultaDTO consultaDTO) {
        consultaService.cancelarSolicitacao(token, consultaDTO);
    }
}
