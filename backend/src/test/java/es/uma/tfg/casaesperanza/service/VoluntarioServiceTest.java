package es.uma.tfg.casaesperanza.service;

import es.uma.tfg.casaesperanza.dto.CreateVoluntarioRequest;
import es.uma.tfg.casaesperanza.entity.VoluntarioEntity;
import es.uma.tfg.casaesperanza.entity.enums.NivelSeguridad;
import es.uma.tfg.casaesperanza.entity.enums.Rol;
import es.uma.tfg.casaesperanza.mapper.VoluntarioMapper;
import es.uma.tfg.casaesperanza.repository.VoluntarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Test para VoluntarioService")
@ExtendWith(MockitoExtension.class)
public class VoluntarioServiceTest {

    private VoluntarioService voluntarioService;

    @Mock
    private VoluntarioRepository mockVoluntarioRepository;
    @Mock
    private VoluntarioMapper mockVoluntarioMapper;

    @BeforeEach
    void init() {
        // Injección de dependencias por constructor
        voluntarioService = new VoluntarioService(
                mockVoluntarioRepository, mockVoluntarioMapper
        );
    }

    @Nested
    @DisplayName("RF1-1: Creación de Cuentas y Autenticación")
    class RF1_1 {

        @Nested
        @DisplayName("Crear cuenta de voluntario")
        class CrearCuentaVoluntario {

            @DisplayName("Escenario principal: Crear cuenta de voluntario con datos válidos")
            @Test
            void crearCuentaVoluntario_DatosValidos() {
                /// Arrange
                // Objetos y mocks necesarios para los Tests
                CreateVoluntarioRequest requestDTO = new CreateVoluntarioRequest();
                requestDTO.setNombre("Francisco");
                requestDTO.setApellidos("Jordá Garay");
                requestDTO.setTelefono("123456789");
                requestDTO.setDni("12345678A");
                requestDTO.setEmail("micorreo@gmail.com");
                requestDTO.setRol(Rol.ADMIN);
                requestDTO.setNivelSeguridad(NivelSeguridad.PRIVILEGIADO);
                requestDTO.setCapacidadesAdicionales(null); // Nos quedamos con las capacidades inherentes al rol
                requestDTO.setPasswordTemporal("nuevaPassword");

                // Transformar el DTO en una entidad es trabajo del Service en crearCuentaVoluntario,
                // pero preparamos el comportamiento de la dependencia simulada para que, cuando el
                // Service la utilice durante el Act, sepamos qué devolverá
                VoluntarioEntity nuevoVoluntario = new VoluntarioEntity();
                when(mockVoluntarioMapper.toEntity(requestDTO)).thenReturn(nuevoVoluntario);

                /// Act
                voluntarioService.crearCuentaVoluntario(requestDTO);

                /// Assert
                // Verificamos que el Service utiliza el save() del repositorio para persistir la nueva entidad
                verify(mockVoluntarioRepository).save(nuevoVoluntario);
            }
        } // end CrearCuentaVoluntario
    } // end class RF1_1
}
