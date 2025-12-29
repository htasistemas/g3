import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AssistanceUnitPayload {
  id?: number;
  nomeFantasia: string;
  razaoSocial?: string;
  enderecoId?: number;
  cnpj?: string;
  telefone?: string;
  email?: string;
  cep?: string;
  endereco?: string;
  numeroEndereco?: string;
  complemento?: string;
  bairro?: string;
  pontoReferencia?: string;
  cidade?: string;
  zona?: string;
  subzona?: string;
  estado?: string;
  latitude?: string;
  longitude?: string;
  observacoes?: string;
  logomarca?: string;
  logomarcaRelatorio?: string;
  diretoria?: DiretoriaUnidadePayload[];
  horarioFuncionamento?: string;
  unidadePrincipal?: boolean;
}

export interface DiretoriaUnidadePayload {
  id?: number;
  nomeCompleto: string;
  documento: string;
  funcao: string;
  mandatoInicio?: string;
  mandatoFim?: string;
}

@Injectable({ providedIn: 'root' })
export class AssistanceUnitService {
  private readonly baseUrl = `${environment.apiUrl}/api/unidades-assistenciais`;
  private readonly activeUnitKey = 'g3-active-assistance-unit';
  private readonly activeUnitLogoKey = 'g3-active-assistance-logo';

  private readonly currentUnitNameSubject = new BehaviorSubject<string>(
    localStorage.getItem(this.activeUnitKey) || 'Navegacao'
  );

  private readonly currentUnitLogoSubject = new BehaviorSubject<string | null>(
    localStorage.getItem(this.activeUnitLogoKey)
  );

  readonly currentUnitName$ = this.currentUnitNameSubject.asObservable();
  readonly currentUnitLogo$ = this.currentUnitLogoSubject.asObservable();

  constructor(private readonly http: HttpClient) {}

  get(): Observable<{ unidade: AssistanceUnitPayload | null }> {
    return this.http.get<{ unidade: AssistanceUnitPayload | null }>(`${this.baseUrl}/atual`);
  }

  list(): Observable<AssistanceUnitPayload[]> {
    return this.http.get<AssistanceUnitPayload[]>(this.baseUrl);
  }

  save(payload: AssistanceUnitPayload): Observable<AssistanceUnitPayload> {
    if (payload.id) {
      return this.http.put<AssistanceUnitPayload>(`${this.baseUrl}/${payload.id}`, payload);
    }

    return this.http.post<AssistanceUnitPayload>(this.baseUrl, payload);
  }

  remove(id: number): Observable<void> {
    return this.http
      .delete<void>(`${this.baseUrl}/${id}`)
      .pipe(tap(() => this.setActiveUnit('Navegacao', null)));
  }

  setActiveUnit(name: string, logo?: string | null): void {
    this.currentUnitNameSubject.next(name);
    localStorage.setItem(this.activeUnitKey, name);
    this.setActiveUnitLogo(logo ?? null);
  }

  private setActiveUnitLogo(logo: string | null): void {
    this.currentUnitLogoSubject.next(logo);

    if (logo) {
      localStorage.setItem(this.activeUnitLogoKey, logo);
      return;
    }

    localStorage.removeItem(this.activeUnitLogoKey);
  }
}
