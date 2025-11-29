import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { map, Observable } from 'rxjs';

export interface UserPayload {
  id: number;
  nomeUsuario: string;
  criadoEm?: string;
  atualizadoEm?: string;
}

export interface UserInput {
  nomeUsuario: string;
  senha: string;
}

@Injectable({ providedIn: 'root' })
export class UserService {
  private readonly baseUrl = `${environment.apiUrl}/api/users`;

  constructor(private readonly http: HttpClient) {}

  list(): Observable<UserPayload[]> {
    return this.http
      .get<{ usuarios?: UserPayload[] } | UserPayload[]>(this.baseUrl)
      .pipe(map((response) => (Array.isArray(response) ? response : response.usuarios ?? [])));
  }

  create(payload: UserInput): Observable<UserPayload> {
    return this.http.post<UserPayload>(this.baseUrl, payload);
  }

  update(id: number, payload: Partial<UserInput> & { nomeUsuario?: string }): Observable<UserPayload> {
    return this.http.put<UserPayload>(`${this.baseUrl}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
