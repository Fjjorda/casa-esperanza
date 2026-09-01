package es.uma.tfg.casaesperanza.dto;

import es.uma.tfg.casaesperanza.entity.AreaProfesionalEntity;
import es.uma.tfg.casaesperanza.entity.enums.Capacidad;
import es.uma.tfg.casaesperanza.entity.enums.NivelSeguridad;
import es.uma.tfg.casaesperanza.entity.enums.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * RequestDTO que almacenará los datos necesarios para crear un nuevo voluntario.
 * <p>
 * Bean Validation se encargará de validar los datos recibidos en el request. Ya fue
 * activado en las dependencias de Spring Boot (starter-validation en pom.xml).
 */
@Data
public class CreateVoluntarioRequest {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @NotBlank(message = "Los apellidos no pueden estar vacíos")
    private String apellidos;
    @NotBlank(message = "El teléfono no puede estar vacío")
    private String telefono;
    @NotBlank(message = "El DNI no puede estar vacío")
    private String dni;
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe tener un formato válido")
    private String email;
    @NotBlank(message = "Debe establecer una contraseña temporal")
    private String passwordTemporal;
    // No debería darse el caso de que alguno de los siguientes sea null
    // El formulario de creación de voluntario en la vista web obligará a seleccionar alguna opción. Sin embargo, se añade la validación por si acaso
    @NotNull(message = "Debe seleccionar un rol")
    private Rol rol;
    @NotNull(message = "Debe seleccionar un nivel de seguridad")
    private NivelSeguridad nivelSeguridad;
    @NotNull(message = "Debe seleccionar un área profesional")
    private int areaProfesionalId;
    private String nuevaAreaProfesional; // Null cuando se selecciona un área profesional existente. No null cuando se crea una nueva área profesional

    private Set<Capacidad> capacidadesAdicionales = new HashSet<>(); // Recupera cualquier capacidad no inherente al rol
    // Al momento de persistir, se concatenan estas capacidades con las inherentes al rol
}
