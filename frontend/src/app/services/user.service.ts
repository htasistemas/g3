import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { map, Observable } from 'rxjs';

export interface UserPayload {
  id: number;
  nomeUsuario: string;
  nome?: string;
  email?: string;
  horarioEntrada1?: string;
  horarioSaida1?: string;
  horarioEntrada2?: string;
  horarioSaida2?: string;
  horarioSegundaEntrada1?: string;
  horarioSegundaSaida1?: string;
  horarioSegundaEntrada2?: string;
  horarioSegundaSaida2?: string;
  horarioTercaEntrada1?: string;
  horarioTercaSaida1?: string;
  horarioTercaEntrada2?: string;
  horarioTercaSaida2?: string;
  horarioQuartaEntrada1?: string;
  horarioQuartaSaida1?: string;
  horarioQuartaEntrada2?: string;
  horarioQuartaSaida2?: string;
  horarioQuintaEntrada1?: string;
  horarioQuintaSaida1?: string;
  horarioQuintaEntrada2?: string;
  horarioQuintaSaida2?: string;
  horarioSextaEntrada1?: string;
  horarioSextaSaida1?: string;
  horarioSextaEntrada2?: string;
  horarioSextaSaida2?: string;
  horarioSabadoEntrada1?: string;
  horarioSabadoSaida1?: string;
  horarioSabadoEntrada2?: string;
  horarioSabadoSaida2?: string;
  horarioDomingoEntrada1?: string;
  horarioDomingoSaida1?: string;
  horarioDomingoEntrada2?: string;
  horarioDomingoSaida2?: string;
  criadoEm?: string;
  atualizadoEm?: string;
  permissoes?: string[];
}

export interface UserInput {
  nome: string;
  email: string;
  senha: string;
  permissoes: string[];
  horarioEntrada1?: string;
  horarioSaida1?: string;
  horarioEntrada2?: string;
  horarioSaida2?: string;
  horarioSegundaEntrada1?: string;
  horarioSegundaSaida1?: string;
  horarioSegundaEntrada2?: string;
  horarioSegundaSaida2?: string;
  horarioTercaEntrada1?: string;
  horarioTercaSaida1?: string;
  horarioTercaEntrada2?: string;
  horarioTercaSaida2?: string;
  horarioQuartaEntrada1?: string;
  horarioQuartaSaida1?: string;
  horarioQuartaEntrada2?: string;
  horarioQuartaSaida2?: string;
  horarioQuintaEntrada1?: string;
  horarioQuintaSaida1?: string;
  horarioQuintaEntrada2?: string;
  horarioQuintaSaida2?: string;
  horarioSextaEntrada1?: string;
  horarioSextaSaida1?: string;
  horarioSextaEntrada2?: string;
  horarioSextaSaida2?: string;
  horarioSabadoEntrada1?: string;
  horarioSabadoSaida1?: string;
  horarioSabadoEntrada2?: string;
  horarioSabadoSaida2?: string;
  horarioDomingoEntrada1?: string;
  horarioDomingoSaida1?: string;
  horarioDomingoEntrada2?: string;
  horarioDomingoSaida2?: string;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly baseUrl = `${environment.apiUrl}/api/usuarios`;

  constructor(private readonly http: HttpClient) {}

  list(): Observable<UserPayload[]> {
    return this.http
      .get<{ usuarios?: UserPayload[] } | UserPayload[]>(this.baseUrl)
      .pipe(map((response) => (Array.isArray(response) ? response : response.usuarios ?? [])));
  }

  create(payload: UserInput): Observable<UserPayload> {
    return this.http.post<UserPayload>(this.baseUrl, payload);
  }

  update(id: number, payload: Partial<UserInput>): Observable<UserPayload> {
    return this.http.put<UserPayload>(`${this.baseUrl}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}

