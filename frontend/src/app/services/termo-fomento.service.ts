import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface TermoFomento {
  id: string;
  numero: string;
  objeto?: string;
  orgaoConcedente?: string;
  modalidade?: string;
  vigenciaInicio?: string;
  vigenciaFim?: string;
  valorGlobal?: string;
  status?: string;
}

export interface TermoFomentoPayload {
  numero: string;
  objeto?: string;
  orgaoConcedente?: string;
  modalidade?: string;
  vigenciaInicio?: string;
  vigenciaFim?: string;
  valorGlobal?: string;
  status?: string;
}

@Injectable({ providedIn: 'root' })
export class TermoFomentoService {
  private readonly baseUrl = `${environment.apiUrl}/api/termos-fomento`;

  constructor(private readonly http: HttpClient) {}

  list(): Observable<TermoFomento[]> {
    return this.http.get<{ termos: TermoFomento[] }>(this.baseUrl).pipe(map((response) => response.termos));
  }

  create(payload: TermoFomentoPayload): Observable<TermoFomento> {
    return this.http.post<{ termo: TermoFomento }>(this.baseUrl, payload).pipe(map((response) => response.termo));
  }
}
