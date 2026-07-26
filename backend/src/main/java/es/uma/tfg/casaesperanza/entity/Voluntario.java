package es.uma.tfg.casaesperanza.entity;

import es.uma.tfg.casaesperanza.entity.enums.Rol;
import es.uma.tfg.casaesperanza.entity.enums.NivelSeguridad;
import es.uma.tfg.casaesperanza.entity.enums.Capacidad;
import jakarta.persistence.Entity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Voluntario {
    private String nombre;
    private String apellido;
    private int telefono;
    private String dni;
    private String email;
    private Rol rol;
    private NivelSeguridad nivel_seguridad;
    private Set<Capacidad> funciones_especificas = new HashSet<>();
    private boolean cuentaActiva = true;
    private LocalDateTime fecha_creacion;

}
