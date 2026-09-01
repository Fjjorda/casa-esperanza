package es.uma.tfg.casaesperanza.service;

import es.uma.tfg.casaesperanza.dto.CreateVoluntarioRequest;
import es.uma.tfg.casaesperanza.entity.AreaProfesionalEntity;
import es.uma.tfg.casaesperanza.entity.VoluntarioEntity;
import es.uma.tfg.casaesperanza.entity.enums.NivelSeguridad;
import es.uma.tfg.casaesperanza.entity.enums.Rol;
import es.uma.tfg.casaesperanza.factory.VoluntarioFactory;
import es.uma.tfg.casaesperanza.repository.AreaProfesionalRepository;
import es.uma.tfg.casaesperanza.repository.VoluntarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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
    private VoluntarioFactory mockVoluntarioFactory;

    @BeforeEach
    void init() {
        // Inyección de dependencias por constructor
        voluntarioService = new VoluntarioService(
                mockVoluntarioRepository,
                mockVoluntarioFactory,
                mockAreaProfesionalRepository
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
                requestDTO.setAreaProfesionalId(10); // Alguna área profesional existente

                // Mock de AreaProfesional
                AreaProfesionalEntity areaProfesionalDummy = new AreaProfesionalEntity();
                areaProfesionalDummy.setAreaProfesional_id(10);
                areaProfesionalDummy.setNombre("Área Profesional Existente");
                when(mockAreaProfesionalRepository.findById(10)).thenReturn(Optional.of(areaProfesionalDummy));

                // Mock de VoluntarioFactory
                VoluntarioEntity nuevoVoluntario = new VoluntarioEntity();
                when(mockVoluntarioFactory.toEntity(requestDTO, areaProfesionalDummy)).thenReturn(nuevoVoluntario);

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

                // Mock de VoluntarioRepository - Simula que ya existe un voluntario con el mismo dni
                when(mockVoluntarioRepository.existsByDni("00000000A")).thenReturn(true);
                String exceptionMessage = "DNI ya está registrado en la base de datos.";

                /// Act & Assert
                // Al inentar crear otro usuario con el mismo dni fallará
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> voluntarioService.crearCuentaVoluntario(requestDTO))
                        .withMessage(exceptionMessage);
                verify(mockVoluntarioRepository).existsByDni("00000000A");
            }

            @DisplayName("4.c Se selecciona 'Otro' en el área profesional.")
            @Test
            void crearCuentaVoluntario_AreaProfesionalOtro() {
                /// Arrange
                CreateVoluntarioRequest requestDTO = new CreateVoluntarioRequest();
                requestDTO.setNombre("Dummy3");
                requestDTO.setDni("00000000B");
                requestDTO.setAreaProfesionalId(1); // ID del área profesional "Otro"
                requestDTO.setNuevaAreaProfesional("Área Profesional Nueva");

                AreaProfesionalEntity nuevaProfesion = new AreaProfesionalEntity();
                // Simulamos la entidad tal y como la devolvería la base de datos al persistirla:
                nuevaProfesion.setAreaProfesional_id(99);                       // con su ID,
                nuevaProfesion.setNombre(requestDTO.getNuevaAreaProfesional()); // y el nombre que se le dio en el formulario

                // VoluntarioService debería persistir esta nueva área profesional antes de persistir el voluntario
                // Usamos any() para que Mockito reconozca esa instancia que crea el Service
                when(mockAreaProfesionalRepository.save(any(AreaProfesionalEntity.class)))
                        .thenReturn(nuevaProfesion);

                VoluntarioEntity nuevoVoluntario = new VoluntarioEntity();
                when(mockVoluntarioFactory.toEntity(requestDTO, nuevaProfesion)).
                        thenReturn(nuevoVoluntario);

                /// Act
                voluntarioService.crearCuentaVoluntario(requestDTO);

                /// Assert
                // Verificamos que el Service utiliza el save() del repositorio para persistir la nueva entidad
                // y se guardó la nueva área profesional antes de guardar el voluntario
                verify(mockAreaProfesionalRepository).save(any(AreaProfesionalEntity.class));
                verify(mockVoluntarioRepository).save(nuevoVoluntario);
            }
        } // end CrearCuentaVoluntario
    } // end class RF1_1
}
