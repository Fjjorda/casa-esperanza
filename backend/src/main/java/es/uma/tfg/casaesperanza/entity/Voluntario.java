package es.uma.tfg.casaesperanza.entity;

import jakarta.persistence.*;
import lombok.Data;

/* Versión Dummy de prueba para el esqueleto */
@Entity
@Data
@Table(name = "voluntario")
public class Voluntario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 254)
    private String email;
}
