import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VoluntarioService } from '../../services/voluntario.service';
import { VoluntarioListResponse } from '../../models/voluntario.models';
import { NuevaCuentaVoluntarioComponent } from '../nueva-cuenta-voluntario/nueva-cuenta-voluntario.component';

/**
 * @Component es la anotación que convierte esta clase en un componente Angular
 * — el equivalente más cercano a un @Controller de Spring, pero para la vista:
 * conecta una plantilla HTML (templateUrl) con la lógica que la alimenta (esta clase).
 *
 * `standalone: true` + `imports: [...]` es el modelo moderno de Angular (17+):
 * en vez de registrar el componente en un NgModule central, cada componente
 * declara explícitamente qué otras piezas necesita para funcionar (aquí,
 * CommonModule para directivas como @for/@if, y el componente del formulario).
 */
@Component({
  selector: 'app-gestion-voluntarios',
  standalone: true,
  imports: [CommonModule, NuevaCuentaVoluntarioComponent],
  templateUrl: './gestion-voluntarios.component.html',
  styleUrl: './gestion-voluntarios.component.css'
})
export class GestionVoluntariosComponent implements OnInit {
  // Estas son "propiedades del componente" — variables normales de la clase
  // que la plantilla HTML puede leer y mostrar (data binding).
  voluntarios: VoluntarioListResponse[] = [];
  cargando = true;
  error: string | null = null;
  mostrarFormulario = false;

  // Inyección por constructor, igual que en Spring.
  constructor(private voluntarioService: VoluntarioService) {}

  /**
   * ngOnInit es un "lifecycle hook": Angular lo llama automáticamente justo
   * después de crear el componente. Es el sitio естándar para disparar la
   * carga inicial de datos — el equivalente en espíritu a un @PostConstruct
   * de Spring.
   */
  ngOnInit(): void {
    this.cargarVoluntarios();
  }

  cargarVoluntarios(): void {
    this.cargando = true;
    this.error = null;

    // .subscribe() es el momento en que la petición HTTP realmente se dispara.
    // Recibe dos funciones: qué hacer si todo va bien, y qué hacer si falla.
    this.voluntarioService.listarVoluntarios().subscribe({
      next: (data) => {
        this.voluntarios = data;
        this.cargando = false;
      },
      error: (err) => {
        this.error = 'No se pudo cargar el listado de voluntarios.';
        this.cargando = false;
        console.error(err);
      }
    });
  }

  abrirFormulario(): void {
    this.mostrarFormulario = true;
  }

  cerrarFormulario(): void {
    this.mostrarFormulario = false;
  }

  /**
   * Este método se lo pasamos al componente hijo (el formulario) para que
   * nos avise cuando termine de crear un voluntario con éxito. Es el mismo
   * flujo que ya decidiste en el handout: el POST no "redirige" a nada,
   * simplemente Angular vuelve a pedir el GET para refrescar la tabla.
   */
  onVoluntarioCreado(): void {
    this.mostrarFormulario = false;
    this.cargarVoluntarios();
  }

  claseBadgeRol(rol: string): string {
    return 'badge badge-' + rol.toLowerCase();
  }

  claseEstado(estado: string): string {
    return estado === 'ACTIVA' ? 'estado estado-activo' : 'estado estado-pendiente';
  }
}
