package es.uma.tfg.casaesperanza.dto.response;


import es.uma.tfg.casaesperanza.entity.enums.EstadoCuenta;
import es.uma.tfg.casaesperanza.entity.enums.NivelSeguridad;
import es.uma.tfg.casaesperanza.entity.enums.Rol;

/**
 * DTO inmutable para transportar los datos que requiere la vista de la pantalla principal en el
 * submenú Gestión de Voluntarios.
 */
public record VoluntarioListResponse(
        Integer voluntarioId,
        String nombre,
        String apellido,
        String email,
        Rol rol,
        NivelSeguridad nivelSeguridad,
        EstadoCuenta estadoCuenta
) {}
