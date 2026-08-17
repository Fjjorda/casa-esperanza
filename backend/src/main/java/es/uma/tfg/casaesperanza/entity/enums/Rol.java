package es.uma.tfg.casaesperanza.entity.enums;

import java.util.Set;

/**
 * RF2.1 Roles del sistema.
 * <p>
 * Cada rol tiene sus capacidades inherentes definidas en
 * sección 4.3.6 del DGR.
 */
public enum Rol {
    ADMIN(
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
            Capacidad.REGISTRAR_AMIGO
    ),
    OPERATIVO(
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
    PROFESIONAL(
            Capacidad.AGENDAR_SERVICIOS,
            Capacidad.CREAR_NOTA_PRIVADA,
            Capacidad.GESTIONAR_ARMARIO
    ),
    PUENTE(
            Capacidad.CREAR_NOTA_PRIVADA,
            Capacidad.GESTIONAR_ARMARIO
    ),
    AGENTE_TEMP(
            Capacidad.CREAR_NOTA_PRIVADA
    );

    // Creamos el Set que almacena las capacidades inherentes de cada Rol como final para que esa referencia
    // no pueda apuntar a otro Set después de construirse la instancia
    private final Set<Capacidad> capacidadesInherentes;

    // Constructor con argumentos variables que crea cada instancia de Rol en tiempo de ejecución
    // Cada instancia tiene su propio Set inmutable y final de capacidades inherentes
    Rol(Capacidad... capacidades) {
        this.capacidadesInherentes = Set.of(capacidades);
    }

    public Set<Capacidad> getCapacidadesInherentes() {
        return capacidadesInherentes;
    }

    public boolean tiene(Capacidad capacidad) {
        return capacidadesInherentes.contains(capacidad);
    }
}
