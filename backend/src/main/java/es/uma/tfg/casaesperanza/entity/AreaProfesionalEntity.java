package es.uma.tfg.casaesperanza.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "area_profesional")
public class AreaProfesionalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "area_profesional_id")
    private Integer areaProfesionalId;

    @Column(nullable = false, unique = true)
    private String nombre;

    // Si necesitáramos navegar desde area_profesional a voluntario añadimos bidireccionalidad
    // @OneToMany(mappedBy = "areaProfesional")
    // private List<VoluntarioEntity> voluntarioList;
}
