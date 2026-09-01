package es.uma.tfg.casaesperanza.service;

import es.uma.tfg.casaesperanza.dto.CreateVoluntarioRequest;
import es.uma.tfg.casaesperanza.entity.AreaProfesionalEntity;
import es.uma.tfg.casaesperanza.entity.VoluntarioEntity;
import es.uma.tfg.casaesperanza.factory.VoluntarioFactory;
import es.uma.tfg.casaesperanza.repository.AreaProfesionalRepository;
import es.uma.tfg.casaesperanza.repository.VoluntarioRepository;
import org.springframework.stereotype.Service;

@Service
public class VoluntarioService {

    private final VoluntarioRepository voluntarioRepository;
    private final VoluntarioFactory voluntarioFactory;
    private final AreaProfesionalRepository areaProfesionalRepository;

    public VoluntarioService(VoluntarioRepository voluntarioRepository, VoluntarioFactory voluntarioFactory, AreaProfesionalRepository areaProfesionalRepository) {
        this.voluntarioRepository = voluntarioRepository;
        this.areaProfesionalRepository = areaProfesionalRepository;
        this.voluntarioFactory = voluntarioFactory;
    }


    public void crearCuentaVoluntario(CreateVoluntarioRequest requestDTO) {
        if (voluntarioRepository.existsByDni(requestDTO.getDni())) {
            throw new IllegalArgumentException("DNI ya está registrado en la base de datos.");
        }
        // Identifica si se trata de un área profesional existente o una nueva
        AreaProfesionalEntity areaProfesional = resolverAreaProfesional(
                requestDTO.getAreaProfesionalId(),
                requestDTO.getNuevaAreaProfesional()
        );
        // Transforma los datos en una entidad
        VoluntarioEntity nuevoVoluntario = voluntarioFactory.toEntity(requestDTO, areaProfesional);
        voluntarioRepository.save(nuevoVoluntario);
    }


    private AreaProfesionalEntity resolverAreaProfesional(Integer id, String nuevoNombre) {
        if (id == 1) { // Asumimos por ahora que el ID 1 corresponde a "Otro"
            if (nuevoNombre == null || nuevoNombre.isBlank()) {
                throw new IllegalArgumentException("No se especificó un nombre para la nueva área profesional");
            }
            AreaProfesionalEntity nuevaArea = new AreaProfesionalEntity();
            nuevaArea.setNombre(nuevoNombre);
            return areaProfesionalRepository.save(nuevaArea);
        }
        // Si no es "Otro", la buscamos en la BD
        return areaProfesionalRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Área profesional no válida")
        );
    }
}
