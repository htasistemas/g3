import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../environments/environment';

export interface FeriadoPayload {
  id?: string;
  data: string;
  descricao: string;
}

interface FeriadoPublicoResponse {
  holidays?: {
    date: string;
    name: string;
    local_name?: string;
  }[];
}

@Injectable({ providedIn: 'root' })
export class FeriadoService {
  private readonly baseUrl = `${environment.apiUrl}/api/feriados`;
  private readonly basePublica = 'https://tallyfy.com/national-holidays/api';

  constructor(private readonly http: HttpClient) {}

  listar(): Observable<FeriadoPayload[]> {
    return this.http
      .get<FeriadoPayload[]>(this.baseUrl)
      .pipe(
        map((feriados) => (feriados ?? []).map((item) => this.normalizar(item))),
        catchError(this.logAndRethrow('ao carregar feriados'))
      );
  }

  criar(payload: FeriadoPayload): Observable<FeriadoPayload> {
    return this.http
      .post<FeriadoPayload>(this.baseUrl, payload)
      .pipe(
        map((item) => this.normalizar(item)),
        catchError(this.logAndRethrow('ao criar feriado'))
      );
  }

  atualizar(id: string, payload: FeriadoPayload): Observable<FeriadoPayload> {
    return this.http
      .put<FeriadoPayload>(`${this.baseUrl}/${id}`, payload)
      .pipe(
        map((item) => this.normalizar(item)),
        catchError(this.logAndRethrow('ao atualizar feriado'))
      );
  }

  excluir(id: string): Observable<void> {
    return this.http
      .delete<void>(`${this.baseUrl}/${id}`)
      .pipe(catchError(this.logAndRethrow('ao excluir feriado')));
  }

  listarPublicos(ano: number, pais = 'BR'): Observable<FeriadoPayload[]> {
    return this.http
      .get<FeriadoPublicoResponse>(`${this.basePublica}/${pais}/${ano}.json`)
      .pipe(
        map((response) =>
          (response?.holidays ?? [])
            .map((item) => ({
              data: item.date,
              descricao: (item.local_name || item.name || '').trim()
            }))
            .filter((item) => item.data && item.descricao)
        ),
        catchError(() => of([]))
      );
  }

  private normalizar(feriado: FeriadoPayload): FeriadoPayload {
    return {
      ...feriado,
      id: feriado.id ? String(feriado.id) : undefined,
    };
  }

  private logAndRethrow(operacao: string) {
    return (error: unknown) => {
      console.error(`Erro ${operacao}`, error);
      return throwError(() => error);
    };
  }
}

