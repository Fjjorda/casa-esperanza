package es.uma.tfg.casaesperanza.entity.enums;

/**
 * RF2.2 Asignación de funciones específicas
 * <p>
 * Funciones basadas en Tabla resumen de roles y permisos, sección 4.3.6
 * del DGR_v02.
 */
public enum Capacidad {
    GestionarCuentasRoles,
    HabilitarAccesoTemp,
    CrearFichaAmigo1,
    EscalarFichaAmigoNivel2,
    EscalarFichaAmigoNivel3,
    AccederDatosSensiblesAmigo,
    CrearNotaPrivada,
    AgendarServicio,
    RegistrarEntradaRopa,
    RegistrarEntradaSalidaAlimentaria,
    OperarLavandaria,
    ConsultarStockRopa,
    CrearAsientosContables,
    ExportarAsientosContables,
    GenerarInformes,
    AccederLogAuditoria
}
