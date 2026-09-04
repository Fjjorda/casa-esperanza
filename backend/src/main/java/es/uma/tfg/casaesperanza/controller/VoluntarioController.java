package es.uma.tfg.casaesperanza.controller;

import es.uma.tfg.casaesperanza.dto.request.CreateVoluntarioRequest;
import es.uma.tfg.casaesperanza.dto.response.VoluntarioListResponse;
import es.uma.tfg.casaesperanza.service.VoluntarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping
    public ResponseEntity<?> crearVoluntario(@Valid @RequestBody CreateVoluntarioRequest request) {
        try {
            voluntarioService.crearCuentaVoluntario(request);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(Map.of("Error!", exception.getMessage()));
        }
    }
}
