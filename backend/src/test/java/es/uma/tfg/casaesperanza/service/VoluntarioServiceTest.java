package es.uma.tfg.casaesperanza.service;

import es.uma.tfg.casaesperanza.dto.CreateVoluntarioRequest;
import es.uma.tfg.casaesperanza.entity.AreaProfesionalEntity;
import es.uma.tfg.casaesperanza.entity.VoluntarioEntity;
import es.uma.tfg.casaesperanza.entity.enums.Capacidad;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
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
    @Mock
    private PasswordEncoder mockPasswordEncoder;

    @BeforeEach
    void init() {
        // Inyección de dependencias por constructor
        voluntarioService = new VoluntarioService(
                mockVoluntarioRepository,
                mockVoluntarioFactory,
                mockAreaProfesionalRepository,
                mockPasswordEncoder
        );
    }

    @Nested
    @DisplayName("RF1-1: Creación de Cuentas y Autenticación")
    class RF1_1 {

        @Nested
        @DisplayName("Crear cuenta de voluntario")
        class CrearCuentaVoluntario {

            @DisplayName("Escenario principal: Crear cuenta de voluntario con datos válidos.")
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
                String dummyPassword = "nuevaPasswordTemporal";
                requestDTO.setPasswordTemporal(dummyPassword);
                requestDTO.setAreaProfesionalId(10); // Alguna área profesional existente

                // Mock de AreaProfesional
                AreaProfesionalEntity areaProfesionalDummy = new AreaProfesionalEntity();
                areaProfesionalDummy.setAreaProfesional_id(10);
                areaProfesionalDummy.setNombre("Área Profesional Existente");
                when(mockAreaProfesionalRepository.findById(10)).thenReturn(Optional.of(areaProfesionalDummy));

                // Mock de PasswordEncoder
                when(mockPasswordEncoder.encode(dummyPassword)).thenReturn("hashedPassword");

                // Mock de VoluntarioFactory
                VoluntarioEntity nuevoVoluntario = new VoluntarioEntity();
                when(mockVoluntarioFactory.buildVoluntarioEntity(
                        requestDTO, areaProfesionalDummy, Rol.ADMIN.getCapacidadesInherentes(), "hashedPassword"))
                        .thenReturn(nuevoVoluntario);

                /// Act
                voluntarioService.crearCuentaVoluntario(requestDTO);

                /// Assert
                // Verificamos que el Service utiliza el save() del repositorio para persistir la nueva entidad
                verify(mockVoluntarioRepository).save(nuevoVoluntario);
            }

            @DisplayName("Crear cuenta de voluntario con capacidades adicionales a su rol.")
            @Test
            void crearCuentaVoluntario_CapacidadesAdicionales() {
                /// Arrange
                CreateVoluntarioRequest requestDTO = new CreateVoluntarioRequest();
                Set<Capacidad> dummyCapacidades = Set.of(Capacidad.CONSULTAR_NOTAS_AMIGO, Capacidad.CREAR_ASIENTOS);
                String dummyPassword = "nuevaPasswordTemporal";

                requestDTO.setNombre("Dummy1");
                requestDTO.setRol(Rol.OPERATIVO);
                requestDTO.setCapacidadesAdicionales(dummyCapacidades);
                requestDTO.setPasswordTemporal(dummyPassword);
                requestDTO.setAreaProfesionalId(10); // Alguna área profesional existente

                AreaProfesionalEntity areaProfesionalDummy = new AreaProfesionalEntity();
                areaProfesionalDummy.setAreaProfesional_id(10);
                areaProfesionalDummy.setNombre("Área Profesional Existente");

                when(mockAreaProfesionalRepository.findById(10)).thenReturn(Optional.of(areaProfesionalDummy));
                when(mockPasswordEncoder.encode(dummyPassword)).thenReturn("hashedPassword");

                // Preparamos un Set de capacidades como lo esperaría el Service
                Set<Capacidad> capacidadesEfectivas = EnumSet.copyOf(Rol.OPERATIVO.getCapacidadesInherentes());
                capacidadesEfectivas.addAll(dummyCapacidades);

                VoluntarioEntity nuevoVoluntario = new VoluntarioEntity();
                when(mockVoluntarioFactory.buildVoluntarioEntity(
                        requestDTO, areaProfesionalDummy, capacidadesEfectivas, "hashedPassword"))
                        .thenReturn(nuevoVoluntario);

                /// Act
                voluntarioService.crearCuentaVoluntario(requestDTO);

                /// Assert
                verify(mockVoluntarioRepository).save(nuevoVoluntario);
            }

            @DisplayName("Crear cuenta de voluntario con capacidades no permitidas para su rol.")
            @Test
            void crearCuentaVoluntario_CapacidadesNoPermitidas_Exception() {
                /// Arrange
                CreateVoluntarioRequest requestDTO = new CreateVoluntarioRequest();
                Set<Capacidad> dummyCapacidadesNoPermitidas = Rol.OPERATIVO.getCapacidadesIncompatibles();

                requestDTO.setNombre("Dummy4");
                requestDTO.setRol(Rol.OPERATIVO);
                requestDTO.setCapacidadesAdicionales(dummyCapacidadesNoPermitidas);

                // Preparamos un Set de capacidades como lo esperaría el Service
                Set<Capacidad> capacidadesEfectivas = EnumSet.copyOf(Rol.OPERATIVO.getCapacidadesInherentes());
                capacidadesEfectivas.addAll(dummyCapacidadesNoPermitidas);


                /// Act & Assert
                // Al intentar crear la cuenta con capacidades no permitidas, se lanzará una excepción
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> voluntarioService.crearCuentaVoluntario(requestDTO));
            }

            @DisplayName("4.b Se intenta crear una cuenta con un campo repetido.")
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
                requestDTO.setRol(Rol.ADMIN);
                requestDTO.setAreaProfesionalId(1); // ID del área profesional "Otro"
                String dummyPassword = "nuevaPasswordTemporal";
                requestDTO.setNuevaAreaProfesional("Área Profesional Nueva");
                requestDTO.setPasswordTemporal(dummyPassword);

                AreaProfesionalEntity nuevaProfesion = new AreaProfesionalEntity();
                // Simulamos la entidad tal y como la devolvería la base de datos al persistirla:
                nuevaProfesion.setAreaProfesional_id(99);                       // con su ID,
                nuevaProfesion.setNombre(requestDTO.getNuevaAreaProfesional()); // y el nombre que se le dio en el formulario

                // VoluntarioService debería persistir esta nueva área profesional antes de persistir el voluntario
                // Usamos any() para que Mockito reconozca esa instancia que crea el Service
                when(mockAreaProfesionalRepository.save(any(AreaProfesionalEntity.class)))
                        .thenReturn(nuevaProfesion);

                when(mockPasswordEncoder.encode(dummyPassword)).thenReturn("hashedPassword");

                VoluntarioEntity nuevoVoluntario = new VoluntarioEntity();
                when(mockVoluntarioFactory.buildVoluntarioEntity(
                        requestDTO, nuevaProfesion, Rol.ADMIN.getCapacidadesInherentes(), "hashedPassword"))
                        .thenReturn(nuevoVoluntario);

                /// Act
                voluntarioService.crearCuentaVoluntario(requestDTO);

                /// Assert
                // Verificamos que el Service utiliza el save() del repositorio para persistir la nueva entidad
                // y se guardó la nueva área profesional antes de guardar el voluntario
                verify(mockAreaProfesionalRepository).save(any(AreaProfesionalEntity.class));
                verify(mockVoluntarioRepository).save(nuevoVoluntario);
            }

            @DisplayName("Se selecciona 'Otro' en el área profesional pero el nombre viene vacío.")
            @Test
            void crearCuentaVoluntario_AreaProfesionalOtro_NombreVacio_Exception() {
                /// Arrange
                CreateVoluntarioRequest requestDTO = new CreateVoluntarioRequest();
                requestDTO.setNombre("Dummy4");
                requestDTO.setDni("00000000C");
                requestDTO.setAreaProfesionalId(1);     // ID del área profesional "Otro"
                requestDTO.setNuevaAreaProfesional(""); // Nombre vacío

                String exceptionMessage = "No se especificó un nombre para la nueva área profesional";

                /// Act & Assert
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> voluntarioService.crearCuentaVoluntario(requestDTO))
                        .withMessage(exceptionMessage);
            }

            @DisplayName("Se selecciona 'Otro' en el área profesional pero el nombre viene nulo.")
            @Test
            void crearCuentaVoluntario_AreaProfesionalOtro_NombreNull_Exception() {
                /// Arrange
                CreateVoluntarioRequest requestDTO = new CreateVoluntarioRequest();
                requestDTO.setNombre("Dummy4");
                requestDTO.setDni("00000000C");
                requestDTO.setAreaProfesionalId(1);
                requestDTO.setNuevaAreaProfesional(null);

                String exceptionMessage = "No se especificó un nombre para la nueva área profesional";

                /// Act & Assert
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> voluntarioService.crearCuentaVoluntario(requestDTO))
                        .withMessage(exceptionMessage);
            }

            @DisplayName("Se inenta asignar un área profesional inexistente.")
            @Test
            void crearCuentaVoluntario_AreaProfesionalInexistente_Exception() {
                /// Arrange
                CreateVoluntarioRequest requestDTO = new CreateVoluntarioRequest();
                requestDTO.setNombre("Dummy5");
                requestDTO.setDni("00000000D");
                requestDTO.setAreaProfesionalId(999); // ID inexistente

                String exceptionMessage = "Área profesional no válida";

                /// Act & Assert
                assertThatExceptionOfType(IllegalArgumentException.class)
                        .isThrownBy(() -> voluntarioService.crearCuentaVoluntario(requestDTO))
                        .withMessage(exceptionMessage);
            }


        } // end CrearCuentaVoluntario
    } // end class RF1_1
}
