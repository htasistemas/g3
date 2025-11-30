import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface UnidadeAssistencialPayload {
  id?: number;
  nome: string;
  telefone?: string;
  email?: string;
  cep?: string;
  endereco?: string;
  numeroEndereco?: string;
  bairro?: string;
  cidade?: string;
  estado?: string;
  observacoes?: string;
}

@Injectable({ providedIn: 'root' })
export class UnidadeAssistencialService {
  private readonly baseUrl = `${environment.apiUrl}/api/unidades-assistenciais`;
  private readonly activeUnitKey = 'g3-unidade-assistencial-ativa';

  private readonly currentUnitNameSubject = new BehaviorSubject<string>(
    localStorage.getItem(this.activeUnitKey) || 'Navegação'
  );

  readonly currentUnitName$ = this.currentUnitNameSubject.asObservable();

  constructor(private readonly http: HttpClient) {}

  get(): Observable<{ unidade: UnidadeAssistencialPayload | null }> {
    return this.http.get<{ unidade: UnidadeAssistencialPayload | null }>(this.baseUrl);
  }

  save(payload: UnidadeAssistencialPayload): Observable<UnidadeAssistencialPayload> {
    if (payload.id) {
      return this.http.put<UnidadeAssistencialPayload>(`${this.baseUrl}/${payload.id}`, payload);
    }

    return this.http.post<UnidadeAssistencialPayload>(this.baseUrl, payload);
  }

  remove(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(tap(() => this.setActiveUnit('Navegação')));
  }

  setActiveUnit(name: string): void {
    this.currentUnitNameSubject.next(name);
    localStorage.setItem(this.activeUnitKey, name);
  }
}
