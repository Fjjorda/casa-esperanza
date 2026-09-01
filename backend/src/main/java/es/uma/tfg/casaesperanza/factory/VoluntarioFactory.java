package es.uma.tfg.casaesperanza.factory;

import es.uma.tfg.casaesperanza.dto.CreateVoluntarioRequest;
import es.uma.tfg.casaesperanza.entity.AreaProfesionalEntity;
import es.uma.tfg.casaesperanza.entity.VoluntarioEntity;
import es.uma.tfg.casaesperanza.entity.enums.Capacidad;
import es.uma.tfg.casaesperanza.entity.enums.EstadoCuenta;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Set;

@Component
public class VoluntarioFactory {

    private final PasswordEncoder passwordEncoder;

    public VoluntarioFactory(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Abstrae toda la lógica de creación de un VoluntarioEntity a partir de los parámetros
     * de entrada.
     * @param requestDTO DTO con los datos de entrada recuperados del formulario de creación.
     * @param areaProfesional Área profesional asociada al voluntario.
     * @return Entidad de tipo VoluntarioEntity lista para ser persistida.
     */
    public VoluntarioEntity toEntity(CreateVoluntarioRequest requestDTO, AreaProfesionalEntity areaProfesional) {
        // Creamos uns nueva instancia de VoluntarioEntity para rellenar
        VoluntarioEntity newEntity = new VoluntarioEntity();

        // Extraemos los datos del DTO y los escribimos en newEntity
        newEntity.setNombre(requestDTO.getNombre());
        newEntity.setApellido(requestDTO.getApellidos());
        newEntity.setTelefono(requestDTO.getTelefono());
        newEntity.setDni(requestDTO.getDni());
        newEntity.setEmail(requestDTO.getEmail());
        newEntity.setRol(requestDTO.getRol());
        newEntity.setNivelSeguridad(requestDTO.getNivelSeguridad());

        // Asignación de entidades complejas (resueltas previamente por el Service)
        newEntity.setAreaProfesional(areaProfesional);

        // Definimos las capacidades efectivas del voluntario
        // Primero, extraemos las capacidades inherentes del Rol
        Set<Capacidad> capacidadesEfectivas = EnumSet.copyOf(requestDTO.getRol().getCapacidadesInherentes());
        // Luego, añadimos las capacidades adicionales que el usuario haya podido indicar
        if (requestDTO.getCapacidadesAdicionales() != null) {
            for(Capacidad capacidadAdicional : requestDTO.getCapacidadesAdicionales()) {
                // Verificamos que la capacidad que se intenta añadir está permitida para el rol
                if(!requestDTO.getRol().puedeTener(capacidadAdicional)){
                    throw new IllegalArgumentException(
                            "El rol " + requestDTO.getRol() + " no puede tener la capacidad " + capacidadAdicional
                    );
                }
                // Si está permitida, la añadimos a las capacidades efectivas
                capacidadesEfectivas.add(capacidadAdicional);
            }
        }
        // Al finalizar, capacidadesEfectivas contiene todas las capacidades que el voluntario tendrá
        // Se las asignamos al entity
        newEntity.setCapacidadesEfectivasSet(capacidadesEfectivas);

        // Campos que se registran siempre igual en la creación de una cuenta:
        newEntity.setEstadoCuenta(EstadoCuenta.PENDIENTE_ACTIVACION);
        // Contraseña (temporal) encriptada
        newEntity.setPasswordHash(passwordEncoder.encode(requestDTO.getPasswordTemporal()));
        newEntity.setFechaCreacion(LocalDateTime.now());
        newEntity.setFechaUltimoLogin(null); // Se actualizará después del primer login
        newEntity.setIntentosFallidos(0);
        newEntity.setBloqueadaHasta(null);

        return newEntity;
    }
}
