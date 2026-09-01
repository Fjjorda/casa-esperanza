package es.uma.tfg.casaesperanza.service;

import es.uma.tfg.casaesperanza.dto.CreateVoluntarioRequest;
import es.uma.tfg.casaesperanza.entity.AreaProfesionalEntity;
import es.uma.tfg.casaesperanza.entity.VoluntarioEntity;
import es.uma.tfg.casaesperanza.entity.enums.NivelSeguridad;
import es.uma.tfg.casaesperanza.entity.enums.Rol;
import es.uma.tfg.casaesperanza.mapper.VoluntarioMapper;
import es.uma.tfg.casaesperanza.repository.AreaProfesionalRepository;
import es.uma.tfg.casaesperanza.repository.VoluntarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Test para VoluntarioService")
@ExtendWith(MockitoExtension.class)
public class VoluntarioServiceTest {

    private VoluntarioService voluntarioService;

    @Mock
    private VoluntarioRepository mockVoluntarioRepository;
    @Mock
    private AreaProfesionalRepository mockAreaProfesionalRepository;
    @Mock
    private VoluntarioMapper mockVoluntarioMapper;

    @BeforeEach
    void init() {
        // Inyección de dependencias por constructor
        voluntarioService = new VoluntarioService(
                mockVoluntarioRepository, mockVoluntarioMapper, mockAreaProfesionalRepository
        );

        // Insertamos un usuario en la base de datos
        CreateVoluntarioRequest requestDTO = new CreateVoluntarioRequest();
        requestDTO.setNombre("Dummy");
        requestDTO.setApellidos("User");
        requestDTO.setTelefono("55555555");
        requestDTO.setDni("00000000A");
        requestDTO.setEmail("dummy@gmail.com");
        requestDTO.setRol(Rol.OPERATIVO);
        requestDTO.setNivelSeguridad(NivelSeguridad.BASICO);
        requestDTO.setCapacidadesAdicionales(null); // Nos quedamos con las capacidades inherentes al rol
        requestDTO.setPasswordTemporal("dummyPassword");
        voluntarioService.crearCuentaVoluntario(requestDTO);

        // Definimos el Área Profesional Otro
        AreaProfesionalEntity areaProfesional = new AreaProfesionalEntity();
        areaProfesional.setNombre("Otro");
        // Se asume que el área profesional "Otro" ya existe en la base de datos y tiene ID 1
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
                requestDTO.setAreaProfesionalId(10); // Suponemos que el área profesional con ID 10 ya existe

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

            @DisplayName("4.b Se intenta crear una cuenta con un campo repetido")
            @Test
            void crearCuentaVoluntario_CampoDniExistente_Exception() {
                /// Arrange
                CreateVoluntarioRequest requestDTO = new CreateVoluntarioRequest();
                requestDTO.setNombre("Dummy2");
                requestDTO.setDni("00000000A");

                when(mockVoluntarioRepository.existsByDni("00000000A")).thenReturn(true);
                String exceptionMessage = "DNI ya está registrado en la base de datos.";

                /// Act & Assert
                // El @BeforeEach ya ha insertado un usuario con el dni "00000000A"
                // Intentaremos crear otro usuario con el mismo dni y verificamos que se llamó existsByDni()
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> voluntarioService.crearCuentaVoluntario(requestDTO))
                        .withMessage(exceptionMessage);
                verify(mockVoluntarioRepository).existsByDni("00000000A");
            }

            @DisplayName("4.c Se selecciona 'Otro' en el área profesional.")
            @Test
            void crearCuentaVoluntario_AreaProfesionalOtro() {
                /// Arrange
                // @BeforeEach ya instanció AreaProfesionalRepository y lo inyectó por constructor
                // Preparamos el requestDTO
                CreateVoluntarioRequest requestDTO = new CreateVoluntarioRequest();
                requestDTO.setNombre("Dummy3");
                requestDTO.setDni("00000000B");
                requestDTO.setAreaProfesionalId(1); // ID del área profesional "Otro"
                requestDTO.setNuevaAreaProfesional("Área Profesional Nueva");

                // Extraemos el área profesional del requestDTO y preparamos la nueva entidad de AreaProfesional
                AreaProfesionalEntity nuevaProfesion = new AreaProfesionalEntity();
                // Simulamos la entidad tal y como la devolvería la base de datos al persistirla:
                nuevaProfesion.setAreaProfesional_id(99);                       // con su ID,
                nuevaProfesion.setNombre(requestDTO.getNuevaAreaProfesional()); // y el nombre que se le dio en el formulario

                // CrearCuentaVoluntario() debería persistir esta nueva área profesional antes de persistir el voluntario
                // entonces usamos any() para que Mockito reconozca esa instancia que crea el Service
                when(mockAreaProfesionalRepository.save(any(AreaProfesionalEntity.class)))
                        .thenReturn(nuevaProfesion);

                // Transformar el DTO en una entidad es trabajo del Service en crearCuentaVoluntario,
                // pero preparamos el comportamiento de la dependencia simulada para que, cuando el
                // Service la utilice durante el Act, sepamos qué devolverá
                VoluntarioEntity nuevoVoluntario = new VoluntarioEntity();
                when(mockVoluntarioMapper.toEntity(requestDTO)).thenReturn(nuevoVoluntario);

                /// Act
                voluntarioService.crearCuentaVoluntario(requestDTO);

                /// Assert
                // Verificamos que el Service utiliza el save() del repositorio para persistir la nueva entidad
                // y se guardó la nueva área profesional antes de guardar el voluntario
                verify(mockAreaProfesionalRepository).save(any(AreaProfesionalEntity.class));
                verify(mockVoluntarioRepository).save(nuevoVoluntario);
                // Verificamos que el DTO se actualizó con el ID de la nueva área profesional antes de persistir el voluntario
                assertEquals(99, requestDTO.getAreaProfesionalId());
            }
        } // end CrearCuentaVoluntario
    } // end class RF1_1
}
