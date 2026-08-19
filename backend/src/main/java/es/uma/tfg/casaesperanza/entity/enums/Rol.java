package es.uma.tfg.casaesperanza.entity.enums;

import lombok.Getter;

import java.util.EnumSet;
import java.util.Set;

/**
 * RF2.1 Roles del sistema.
 * <p>
 * Cada rol tiene sus capacidades inherentes definidas en
 * sección 4.3.6 del DGR.
 */
public enum Rol {
    ADMIN(
            // Capacidades inherentes
            Set.of(
                    Capacidad.AGENDAR_SERVICIOS,
                    Capacidad.CONCEDER_HABILITACION_TEMPORAL,
                    Capacidad.CONSULTAR_LOG_AUDITORIA,
                    Capacidad.CONSULTAR_NOTAS_AMIGO,
                    Capacidad.CREAR_ASIENTOS,
                    Capacidad.CREAR_NOTA_PRIVADA,
                    Capacidad.ENTREGAR_ROPA,
                    Capacidad.ESCALAR_NIVEL_3,
                    Capacidad.EXPORTAR_INFORMES,
                    Capacidad.GESTIONAR_ARMARIO,
                    Capacidad.GESTIONAR_CUENTAS_USUARIO,
                    Capacidad.IMPORTAR_DATOS,
                    Capacidad.IMPORTAR_EXTRACTOS,
                    Capacidad.OPERAR_LAVANDERIA,
                    Capacidad.REGISTRAR_ENTREGA_ALIMENTARIA,
                    Capacidad.REGISTRAR_GASTO_AMIGO,
                    Capacidad.REGISTRAR_SALIDA_ALIMENTARIA,
                    Capacidad.REGISTRAR_AMIGO),

            // Capacidades opcionales
            Set.of()
    ),
    OPERATIVO(
            // Capacidades inherentes
            Set.of(
                    Capacidad.AGENDAR_SERVICIOS,
                    Capacidad.CREAR_NOTA_PRIVADA,
                    Capacidad.ENTREGAR_ROPA,
                    Capacidad.GESTIONAR_ARMARIO,
                    Capacidad.IMPORTAR_DATOS,
                    Capacidad.OPERAR_LAVANDERIA,
                    Capacidad.REGISTRAR_ENTREGA_ALIMENTARIA,
                    Capacidad.REGISTRAR_SALIDA_ALIMENTARIA,
                    Capacidad.REGISTRAR_AMIGO
            ),

            // Capacidades opcionales
            Set.of(
                    Capacidad.CONSULTAR_NOTAS_AMIGO,
                    Capacidad.CREAR_ASIENTOS,
                    Capacidad.ESCALAR_NIVEL_3,
                    Capacidad.EXPORTAR_INFORMES,
                    Capacidad.REGISTRAR_GASTO_AMIGO
            )

    ),
    PROFESIONAL(
            // Capacidades inherentes
            Set.of(
                    Capacidad.AGENDAR_SERVICIOS,
                    Capacidad.CREAR_NOTA_PRIVADA,
                    Capacidad.GESTIONAR_ARMARIO
            ),

            // Capacidades opcionales
            Set.of(Capacidad.CONSULTAR_NOTAS_AMIGO)
    ),
    PUENTE(
            Set.of(
                    Capacidad.CREAR_NOTA_PRIVADA,
                    Capacidad.GESTIONAR_ARMARIO
            ),

            // Capacidades opcionales
            Set.of(
                    Capacidad.AGENDAR_SERVICIOS,
                    Capacidad.OPERAR_LAVANDERIA
            )
    ),
    AGENTE_TEMP(
            // Capacidades inherentes
            Set.of(Capacidad.CREAR_NOTA_PRIVADA),

            // Capacidades opcionales
            Set.of(
                    Capacidad.CONSULTAR_NOTAS_AMIGO,
                    Capacidad.EXPORTAR_INFORMES,
                    Capacidad.GESTIONAR_ARMARIO
            )

    );

    // Creamos los Sets que almacenan las capacidades inherentes y opcionales de cada Rol como final para que esa referencia
    // no pueda apuntar a otro Set después de construirse la instancia
    @Getter
    private final Set<Capacidad> capacidadesInherentes;
    @Getter
    private final Set<Capacidad> capacidadesOpcionales;

    // Constructor que instancia cada Rol con sus capacidades. Cada conjunto es inmutable
    Rol(Set<Capacidad> capacidadesInherentes, Set<Capacidad> capacidadesOpcionales) {
        this.capacidadesInherentes = capacidadesInherentes;
        this.capacidadesOpcionales = capacidadesOpcionales;
    }

    /**
     * Calcula las capacidades que NO están disponibles para este rol.
     * <p>
     * (Todas las Capacidades - Inherentes - Opcionales)
     */
    public Set<Capacidad> getCapacidadesIncompatibles() {
        EnumSet<Capacidad> capacidadesIncompatibles = EnumSet.allOf(Capacidad.class);
        capacidadesIncompatibles.removeAll(this.capacidadesInherentes);
        capacidadesIncompatibles.removeAll(this.capacidadesOpcionales);

        return capacidadesIncompatibles;
    }

    /**
     * Consulta si una capacidad se encuentra en el conjunto de capacidades opcionales.
     */
    public boolean puedeTener(Capacidad capacidad) {
        return capacidadesOpcionales.contains(capacidad);
    }
}
