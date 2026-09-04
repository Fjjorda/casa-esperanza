package es.uma.tfg.casaesperanza.mapper;

import es.uma.tfg.casaesperanza.dto.response.VoluntarioListResponse;
import es.uma.tfg.casaesperanza.entity.VoluntarioEntity;

public class VoluntarioMapper {

    public static VoluntarioListResponse toListResponseDTO(VoluntarioEntity entity) {
        return new VoluntarioListResponse(
                entity.getVoluntarioId(),
                entity.getNombre(),
                entity.getApellido(),
                entity.getEmail(),
                entity.getRol(),
                entity.getNivelSeguridad(),
                entity.getEstadoCuenta()
        );
    }
}
