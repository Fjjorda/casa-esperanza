import { Component } from '@angular/core';
import { GestionVoluntariosComponent } from './components/gestion-voluntarios/gestion-voluntarios.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [GestionVoluntariosComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected title = 'frontend';
}
