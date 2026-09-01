package es.uma.tfg.casaesperanza.service;

import es.uma.tfg.casaesperanza.dto.CreateVoluntarioRequest;
import es.uma.tfg.casaesperanza.entity.AreaProfesionalEntity;
import es.uma.tfg.casaesperanza.entity.VoluntarioEntity;
import es.uma.tfg.casaesperanza.mapper.VoluntarioMapper;
import es.uma.tfg.casaesperanza.repository.AreaProfesionalRepository;
import es.uma.tfg.casaesperanza.repository.VoluntarioRepository;
import org.springframework.stereotype.Service;

@Service
public class VoluntarioService {

    private final VoluntarioRepository voluntarioRepository;
    private final VoluntarioMapper voluntarioMapper;
    private final AreaProfesionalRepository areaProfesionalRepository;

    public VoluntarioService(VoluntarioRepository voluntarioRepository, VoluntarioMapper voluntarioMapper, AreaProfesionalRepository areaProfesionalRepository) {
        this.voluntarioRepository = voluntarioRepository;
        this.areaProfesionalRepository = areaProfesionalRepository;
        this.voluntarioMapper = voluntarioMapper;
    }

    // Implementación de los casos de uso
    public void crearCuentaVoluntario(CreateVoluntarioRequest requestDTO) {
        // Antes de crear la cuenta, verificamos si el dni es único
        if (voluntarioRepository.existsByDni(requestDTO.getDni())) {
            throw new IllegalArgumentException("DNI ya está registrado en la base de datos.");
        }

        // Verificamos si ÁreaProfesional es 'Otro'
        if(requestDTO.getAreaProfesionalId() == 1) { // Suponemos ahora que 'Otro' es 1
            // En caso afirmativo, necesitamos añadir esa nueva área profesional a la base de datos
            if(requestDTO.getNuevaAreaProfesional() == null || requestDTO.getNuevaAreaProfesional().isBlank()) {
                throw new IllegalArgumentException("Debe proporcionar un nombre para la nueva área profesional.");
            }
            // Creamos la nueva área profesional y la guardamos en la base de datos
            AreaProfesionalEntity nuevaArea = new AreaProfesionalEntity();
            nuevaArea.setNombre(requestDTO.getNuevaAreaProfesional());
            AreaProfesionalEntity savedArea = areaProfesionalRepository.save(nuevaArea);
            // Finalmente, asignamos el ID de la nueva área profesional al requestDTO para que el mapper lo use
            requestDTO.setAreaProfesionalId(savedArea.getAreaProfesional_id());

        }
        // En caso contrario, el mapper se encarga de asignar el área profesional existente


        VoluntarioEntity nuevoVoluntario = voluntarioMapper.toEntity(requestDTO);
        voluntarioRepository.save(nuevoVoluntario);
    }
}
