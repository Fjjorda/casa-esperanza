package es.uma.tfg.casaesperanza.controller;

import es.uma.tfg.casaesperanza.dto.response.VoluntarioListResponse;
import es.uma.tfg.casaesperanza.service.VoluntarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/casa-esperanza/voluntarios")
public class VoluntarioController {

    private final VoluntarioService voluntarioService;

    public VoluntarioController(VoluntarioService voluntarioService) {
        this.voluntarioService = voluntarioService;
    }

    @GetMapping
    public ResponseEntity<List<VoluntarioListResponse>> listarVoluntarios() {
        return ResponseEntity.ok(voluntarioService.listarVoluntarios());
    }
}
