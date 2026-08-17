package es.uma.tfg.casaesperanza.entity;

import es.uma.tfg.casaesperanza.entity.enums.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "voluntarios")
public class VoluntarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer voluntario_id;

    @Column(nullable  = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 9)
    private String dni;

    @Column(length = 20)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "nivel_seguridad")
    private NivelSeguridad nivelSeguridad;

    @ManyToOne
    @JoinColumn(name = "areaProfesional_id")
    private AreaProfesionalEntity areaProfesional;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "estado_cuenta")
    private EstadoCuenta estadoCuenta;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "voluntario_capacidades",
            joinColumns = @JoinColumn(name = "voluntario_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "capacidad")
    private Set<Capacidad> capacidadesEfectivasSet = new HashSet<>();
    // capacidadesEfectivas = capacidadesInherentes del Rol + capacidadesAdicionales

    @Column(nullable = false, name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_ultimo_login")
    private LocalDateTime fechaUltimoLogin;

    @Column(nullable = false, name = "intentos_fallidos")
    private int intentosFallidos;

    @Column(name = "bloqueada_hasta")
    private LocalDateTime bloqueadaHasta;

}
