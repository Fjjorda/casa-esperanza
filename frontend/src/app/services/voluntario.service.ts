import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateVoluntarioRequest, VoluntarioListResponse } from '../models/voluntario.models';

/**
 * Un Service en Angular cumple el mismo papel conceptual que tu VoluntarioService
 * en Spring: concentra "cómo se habla con el backend" en un único sitio, para que
 * los componentes (la parte visual) no tengan que saber URLs ni verbos HTTP.
 *
 * @Injectable({ providedIn: 'root' }) es el equivalente aproximado a @Service en
 * Spring: le dice a Angular "esto es una pieza inyectable, crea una única
 * instancia compartida por toda la app" (un singleton, igual que tus @Service
 * y @Repository de Spring son singletons por defecto).
 */
@Injectable({ providedIn: 'root' })
export class VoluntarioService {
  // Ajusta el puerto/host si tu backend corre en otro sitio.
  // En un proyecto real esto se mueve a un archivo de configuración
  // (environment.ts), pero de momento lo dejamos explícito para que lo veas.
  private readonly baseUrl = 'http://localhost:8080/casa-esperanza/voluntarios';

  // HttpClient se inyecta por constructor — exactamente igual que inyectas
  // VoluntarioRepository en tu Service de Spring. Angular lo resuelve solo.
  constructor(private http: HttpClient) {}

  /**
   * Equivalente a tu GET /casa-esperanza/voluntarios.
   * No hace la petición todavía: HttpClient devuelve un Observable, que es
   * una especie de "promesa de dato futuro". La petición HTTP real solo se
   * dispara cuando alguien se "suscribe" a este Observable (lo verás en el
   * componente, con .subscribe(...)).
   */
  listarVoluntarios(): Observable<VoluntarioListResponse[]> {
    return this.http.get<VoluntarioListResponse[]>(this.baseUrl);
  }

  /**
   * Equivalente a tu POST /casa-esperanza/voluntarios.
   * Angular serializa el objeto `request` a JSON automáticamente
   * (el equivalente en sentido inverso a como Jackson deserializa
   * el JSON entrante a tu CreateVoluntarioRequest en el backend).
   */
  crearVoluntario(request: CreateVoluntarioRequest): Observable<void> {
    return this.http.post<void>(this.baseUrl, request);
  }
}
