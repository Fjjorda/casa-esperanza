package es.uma.tfg.casaesperanza.service;

import es.uma.tfg.casaesperanza.dto.request.CreateVoluntarioRequest;
import es.uma.tfg.casaesperanza.dto.response.VoluntarioListResponse;
import es.uma.tfg.casaesperanza.entity.AreaProfesionalEntity;
import es.uma.tfg.casaesperanza.entity.VoluntarioEntity;
import es.uma.tfg.casaesperanza.entity.enums.Capacidad;
import es.uma.tfg.casaesperanza.entity.enums.Rol;
import es.uma.tfg.casaesperanza.factory.VoluntarioFactory;
import es.uma.tfg.casaesperanza.mapper.VoluntarioMapper;
import es.uma.tfg.casaesperanza.repository.AreaProfesionalRepository;
import es.uma.tfg.casaesperanza.repository.VoluntarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

@Service
public class VoluntarioService {

    private final VoluntarioRepository voluntarioRepository;
    private final VoluntarioFactory voluntarioFactory;
    private final AreaProfesionalRepository areaProfesionalRepository;
    private final PasswordEncoder passwordEncoder;

    public VoluntarioService(
            VoluntarioRepository voluntarioRepository, VoluntarioFactory voluntarioFactory,
            AreaProfesionalRepository areaProfesionalRepository, PasswordEncoder passwordEncoder) {
        this.voluntarioRepository = voluntarioRepository;
        this.areaProfesionalRepository = areaProfesionalRepository;
        this.voluntarioFactory = voluntarioFactory;
        this.passwordEncoder = passwordEncoder;
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

        // Identifica las capacidades del voluntario
        Set<Capacidad> capacidadesEfectivas = resolverCapacidadesEfectivas(
                requestDTO.getCapacidadesAdicionales(),
                requestDTO.getRol()
        );

        // Cifra contraseña temporal antes de persistir
        String passwordHash = passwordEncoder.encode(requestDTO.getPasswordTemporal());

        // Transforma los datos en una entidad y se persiste en la base de datos
        VoluntarioEntity nuevoVoluntario = voluntarioFactory.buildVoluntarioEntity(
                requestDTO, areaProfesional, capacidadesEfectivas, passwordHash);
        voluntarioRepository.save(nuevoVoluntario);
    }


    private AreaProfesionalEntity resolverAreaProfesional(Integer id, String nuevoNombre) {
        // Verificamos si el ID corresponde a "Otro"
        if (id == 1) { // Asumimos por ahora que el ID 1 corresponde a "Otro"
            if (nuevoNombre == null || nuevoNombre.isBlank()) {
                throw new IllegalArgumentException("No se especificó un nombre para la nueva área profesional");
            }
            // En tal caso, creamos una nueva entidad de área profesional y la persistimos en la base de datos
            AreaProfesionalEntity nuevaArea = new AreaProfesionalEntity();
            nuevaArea.setNombre(nuevoNombre);
            return areaProfesionalRepository.save(nuevaArea);
        }
        // Si no es "Otro", la buscamos en la BD y la devolvemos
        return areaProfesionalRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Área profesional no válida")
        );
    }


    private Set<Capacidad> resolverCapacidadesEfectivas(Set<Capacidad> capacidadesAdicionales, Rol rol) {
        // Identificamos las capacidades inherentes del Rol
        Set<Capacidad> capacidadesEfectivas = EnumSet.copyOf(rol.getCapacidadesInherentes());
        // Luego, añadimos las capacidades adicionales que el usuario haya podido indicar
        if (capacidadesAdicionales != null) {
            for(Capacidad capacidadAdicional : capacidadesAdicionales) {
                // Verificamos que la capacidad que se intenta añadir está permitida para el rol
                if(!rol.puedeTener(capacidadAdicional)){
                    throw new IllegalArgumentException(
                            "El rol " + rol + " no puede tener la capacidad " + capacidadAdicional
                    );
                }
                // Si está permitida, la añadimos a las capacidades efectivas
                capacidadesEfectivas.add(capacidadAdicional);
            }
        }

        return capacidadesEfectivas;
    }

    public List<VoluntarioListResponse> listarVoluntarios() {
        return voluntarioRepository.findAll().stream().map(VoluntarioMapper::toListResponseDTO).toList();
    }
}
