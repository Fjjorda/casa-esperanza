package es.uma.tfg.casaesperanza.factory;

import es.uma.tfg.casaesperanza.dto.request.CreateVoluntarioRequest;
import es.uma.tfg.casaesperanza.entity.AreaProfesionalEntity;
import es.uma.tfg.casaesperanza.entity.VoluntarioEntity;
import es.uma.tfg.casaesperanza.entity.enums.Capacidad;
import es.uma.tfg.casaesperanza.entity.enums.EstadoCuenta;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class VoluntarioFactory {

    /**
     * Abstrae toda la lógica de creación de un VoluntarioEntity a partir de los parámetros
     * de entrada.
     * @param requestDTO DTO con los datos de entrada recuperados del formulario de creación.
     * @param areaProfesional Área profesional asociada al voluntario.
     * @param capacidadesEfectivas Capacidades efectivas del voluntario.
     * @param passwordHash Contraseña temporal encriptada.
     * @return Entidad de tipo VoluntarioEntity lista para ser persistida.
     */
    public VoluntarioEntity buildVoluntarioEntity(
            CreateVoluntarioRequest requestDTO, AreaProfesionalEntity areaProfesional,
            Set<Capacidad> capacidadesEfectivas, String passwordHash) {
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
        newEntity.setAreaProfesional(areaProfesional);
        newEntity.setCapacidadesEfectivasSet(capacidadesEfectivas);

        // Campos que se registran siempre igual en la creación de una cuenta:
        newEntity.setEstadoCuenta(EstadoCuenta.PENDIENTE_ACTIVACION);
        // Contraseña (temporal) encriptada
        newEntity.setPasswordHash(passwordHash);
        newEntity.setFechaCreacion(LocalDateTime.now());
        newEntity.setFechaUltimoLogin(null); // Se actualizará después del primer login
        newEntity.setIntentosFallidos(0);
        newEntity.setBloqueadaHasta(null);

        return newEntity;
    }
}
