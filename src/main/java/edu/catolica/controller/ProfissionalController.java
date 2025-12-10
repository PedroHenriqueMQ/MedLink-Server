package edu.catolica.controller;

import edu.catolica.dto.response.ConsultaResponseDTO;
import edu.catolica.dto.response.ProfissionalResponseDTO;
import edu.catolica.service.consulta.ConsultaService;
import edu.catolica.service.usuario.ProfissionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profissionais")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfissionalController {
    private final ProfissionalService profissionalService;
    private final ConsultaService consultaService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/clinica/{razao-social}")
    public List<ProfissionalResponseDTO> obterProfissionaisPorClinica(
            @PathVariable("razao-social") String razaoSocial
    ) {
        return profissionalService.obterProfissionaisPorClinica(razaoSocial);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ProfissionalResponseDTO> obterProfissionaisPelaClinicaDoUsuario(@RequestHeader("token") String token) {
        return profissionalService.obterProfissionaisPelaClinicaDoUsuario(token);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/consultas")
    public List<ConsultaResponseDTO> obterConsultasPeloEmail(@RequestHeader("token") String token) {
        return profissionalService.obterConsultasPeloEmail(token);
    }

    @PatchMapping("/consultas/{id}/confirmar")
    public ResponseEntity<ConsultaResponseDTO> aceitarConsulta(@PathVariable("id") Long id) {
        return ResponseEntity.ok(consultaService.confirmarConsulta(id));
    }

    @PatchMapping("/consultas/{id}/negar")
    public ResponseEntity<ConsultaResponseDTO> negarConsulta(
            @PathVariable("id") Long id, @RequestBody String justificativa) {
        return ResponseEntity.ok(consultaService.negarConsulta(id, justificativa));
    }
}
