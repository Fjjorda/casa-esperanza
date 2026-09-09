/**
 * Estos tipos son el "espejo" en TypeScript de tus DTOs y enums de Java.
 * No hay magia: Angular no sabe nada de tu backend, solo confía en que el
 * JSON que llega por HTTP tiene esta forma. Si cambias un campo en el DTO
 * de Java, tienes que reflejarlo aquí también a mano.
 */

// Equivalente TypeScript de tus enums de Java. Un "union type" de strings
// literales es más simple que un `enum` de TypeScript para este caso,
// y encaja mejor con cómo Jackson serializa tus enums de Java (como texto).
export type Rol = 'ADMIN' | 'OPERATIVO' | 'PROFESIONAL' | 'PUENTE' | 'AGENTE_TEMP';
export type NivelSeguridad = 'BASICO' | 'PRIVILEGIADO';
export type EstadoCuenta = 'PENDIENTE_ACTIVACION' | 'ACTIVA' | 'DESACTIVADA';

// Espejo de tu VoluntarioListResponse (record de Java)
export interface VoluntarioListResponse {
  voluntarioId: number;
  nombre: string;
  apellido: string;
  email: string;
  rol: Rol;
  nivelSeguridad: NivelSeguridad;
  estadoCuenta: EstadoCuenta;
}

// Espejo de tu CreateVoluntarioRequest
export interface CreateVoluntarioRequest {
  nombre: string;
  apellido: string;
  telefono: string;
  dni: string;
  email: string;
  passwordTemporal: string;
  rol: Rol | null;
  nivelSeguridad: NivelSeguridad | null;
  areaProfesionalId: number | null;
  nuevaAreaProfesional: string | null;
  capacidadesAdicionales: string[];
}

// Placeholder simple mientras no tengas el endpoint /voluntarios/opciones.
// Lo sustituirás por datos reales de la API en el siguiente paso.
export interface AreaProfesional {
  areaProfesionalId: number;
  nombre: string;
}
