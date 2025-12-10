package edu.catolica.controller;

import edu.catolica.dto.response.ConsultaResponseDTO;
import edu.catolica.dto.response.ProfissionalResponseDTO;
import edu.catolica.service.consulta.impl.ConsultaServiceImpl;
import edu.catolica.service.usuario.impl.ProfissionalServiceImpl;
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
    private final ProfissionalServiceImpl profissionalServiceImpl;
    private final ConsultaServiceImpl consultaServiceImpl;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/clinica/{razao-social}")
    public List<ProfissionalResponseDTO> obterProfissionaisPorClinica(
            @PathVariable("razao-social") String razaoSocial
    ) {
        return profissionalServiceImpl.obterProfissionaisPorClinica(razaoSocial);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ProfissionalResponseDTO> obterProfissionaisPelaClinicaDoUsuario(@RequestHeader("token") String token) {
        return profissionalServiceImpl.obterProfissionaisPelaClinicaDoUsuario(token);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/consultas")
    public List<ConsultaResponseDTO> obterConsultasPeloEmail(@RequestHeader("token") String token) {
        return profissionalServiceImpl.obterConsultasPeloEmail(token);
    }

    @PatchMapping("/consultas/{id}/confirmar")
    public ResponseEntity<ConsultaResponseDTO> aceitarConsulta(@PathVariable("id") Long id) {
        return ResponseEntity.ok(consultaServiceImpl.confirmarConsulta(id));
    }

    @PatchMapping("/consultas/{id}/negar")
    public ResponseEntity<ConsultaResponseDTO> negarConsulta(
            @PathVariable("id") Long id, @RequestBody String justificativa) {
        return ResponseEntity.ok(consultaServiceImpl.negarConsulta(id, justificativa));
    }
}
