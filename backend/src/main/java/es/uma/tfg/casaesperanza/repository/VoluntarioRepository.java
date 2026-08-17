package es.uma.tfg.casaesperanza.repository;

import es.uma.tfg.casaesperanza.entity.VoluntarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoluntarioRepository extends JpaRepository<VoluntarioEntity, Integer> {
    // Definimos las consultas que piden los casos de uso

    // Escenario alternativo 4.b - Verificar si hay un campo repetido
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);

}
