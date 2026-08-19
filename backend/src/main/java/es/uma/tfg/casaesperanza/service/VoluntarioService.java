package es.uma.tfg.casaesperanza.service;

import es.uma.tfg.casaesperanza.dto.CreateVoluntarioRequest;
import es.uma.tfg.casaesperanza.entity.VoluntarioEntity;
import es.uma.tfg.casaesperanza.mapper.VoluntarioMapper;
import es.uma.tfg.casaesperanza.repository.VoluntarioRepository;
import org.springframework.stereotype.Service;

@Service
public class VoluntarioService {

    private final VoluntarioRepository voluntarioRepository;
    private final VoluntarioMapper voluntarioMapper;

    public VoluntarioService(VoluntarioRepository voluntarioRepository, VoluntarioMapper voluntarioMapper) {
        this.voluntarioRepository = voluntarioRepository;
        this.voluntarioMapper = voluntarioMapper;
    }

    public void crearCuentaVoluntario(CreateVoluntarioRequest requestDTO) {
        VoluntarioEntity nuevoVoluntario = voluntarioMapper.toEntity(requestDTO);
        voluntarioRepository.save(nuevoVoluntario);
    }
}
