import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface SalaRecord {
  id: string;
  nome: string;
}

export interface SalaPayload {
  nome: string;
}

@Injectable({ providedIn: 'root' })
export class SalasService {
  private readonly baseUrl = `${environment.apiUrl}/api/salas`;

  constructor(private readonly http: HttpClient) {}

  list(): Observable<SalaRecord[]> {
    return this.http.get<{ rooms: SalaRecord[] }>(this.baseUrl).pipe(map((response) => response.rooms ?? []));
  }

  create(payload: SalaPayload): Observable<SalaRecord> {
    return this.http.post<{ room: SalaRecord }>(this.baseUrl, payload).pipe(map((response) => response.room));
  }
}
