import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VoluntarioService } from '../../services/voluntario.service';
import { CreateVoluntarioRequest, Rol, NivelSeguridad } from '../../models/voluntario.models';
import { Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-nueva-cuenta-voluntario',
  standalone: true,
  // FormsModule habilita ngModel — es lo que permite el "two-way binding"
  // entre un <input> del HTML y una propiedad de esta clase.
  imports: [CommonModule, FormsModule],
  templateUrl: './nueva-cuenta-voluntario.component.html',
  styleUrl: './nueva-cuenta-voluntario.component.css'
})
export class NuevaCuentaVoluntarioComponent {
  /**
   * @Output + EventEmitter es como un componente hijo "habla" con su padre.
   * Es la dirección opuesta a @Input (que sería el padre pasando datos hacia
   * abajo). Aquí, el padre (GestionVoluntariosComponent) escucha estos dos
   * eventos con (cancelado) y (voluntarioCreado) en su plantilla HTML.
   */
  @Output() cancelado = new EventEmitter<void>();
  @Output() voluntarioCreado = new EventEmitter<void>();

  // Roles y niveles como listas fijas. Cuando implementes
  // GET /voluntarios/opciones, esto se sustituye por datos reales
  // recibidos del backend en lugar de estar escritos a mano aquí.
  roles: Rol[] = ['ADMIN', 'OPERATIVO', 'PROFESIONAL', 'PUENTE', 'AGENTE_TEMP'];
  niveles: NivelSeguridad[] = ['BASICO', 'PRIVILEGIADO'];

  // Este objeto es lo que ngModel va rellenando campo a campo según el
  // usuario escribe. Al enviar el formulario, es literalmente el body
  // que se manda tal cual al POST.
  form: CreateVoluntarioRequest = {
    nombre: '',
    apellido: '',
    telefono: '',
    dni: '',
    email: '',
    passwordTemporal: '',
    rol: null,
    nivelSeguridad: null,
    areaProfesionalId: 2, // provisional: hasta tener el selector real de áreas
    nuevaAreaProfesional: null,
    capacidadesAdicionales: []
  };

  enviando = false;
  errorServidor: string | null = null;

  constructor(private voluntarioService: VoluntarioService) {}

  guardar(): void {
    this.enviando = true;
    this.errorServidor = null;

    this.voluntarioService.crearVoluntario(this.form).subscribe({
      next: () => {
        this.enviando = false;
        this.voluntarioCreado.emit();
      },
      error: (err) => {
        this.enviando = false;
        // Aquí es donde recoges el {"Error!": "..."} que ya viste en el .http.
        // err.error es el body de la respuesta HTTP ya deserializado por Angular.
        this.errorServidor = err.error?.['Error!'] ?? 'No se pudo crear el voluntario.';
      }
    });
  }

  cerrar(): void {
    this.cancelado.emit();
  }
}
