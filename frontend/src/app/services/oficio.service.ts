import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../environments/environment';

export interface TramiteRegistro {
  data?: string;
  origem?: string;
  destino?: string;
  responsavel?: string;
  acao: string;
  observacoes?: string;
}

export interface OficioPayload {
  id?: string;
  identificacao: {
    tipo: 'emissao' | 'recebimento';
    numero: string;
    data: string;
    setorOrigem: string;
    responsavel: string;
    destinatario: string;
    meioEnvio: string;
    prazoResposta?: string;
    classificacao?: string;
  };
  conteudo: {
    razaoSocial: string;
    logoUrl?: string;
    titulo?: string;
    saudacao?: string;
    assunto: string;
    corpo: string;
    finalizacao?: string;
    assinaturaNome?: string;
    assinaturaCargo?: string;
    rodape?: string;
  };
  protocolo: {
    status: string;
    protocoloEnvio?: string;
    dataEnvio?: string;
    protocoloRecebimento?: string;
    dataRecebimento?: string;
    proximoDestino?: string;
    observacoes?: string;
  };
  tramites: TramiteRegistro[];
}

interface OficioListaResponse {
  oficios: OficioPayload[];
}

@Injectable({ providedIn: 'root' })
export class OficioService {
  private readonly baseUrl = `${environment.apiUrl}/api/oficios`;

  constructor(private readonly http: HttpClient) {}

  list(): Observable<OficioPayload[]> {
    return this.http
      .get<OficioListaResponse>(this.baseUrl)
      .pipe(map((response) => (response?.oficios ?? []).map((item) => this.normalizar(item))))
      .pipe(catchError(this.logAndRethrow('ao carregar oficios')));
  }

  create(payload: OficioPayload): Observable<OficioPayload> {
    return this.http
      .post<OficioPayload>(this.baseUrl, payload)
      .pipe(map((item) => this.normalizar(item)))
      .pipe(catchError(this.logAndRethrow('ao criar oficio')));
  }

  update(id: string, payload: OficioPayload): Observable<OficioPayload> {
    return this.http
      .put<OficioPayload>(`${this.baseUrl}/${id}`, payload)
      .pipe(map((item) => this.normalizar(item)))
      .pipe(catchError(this.logAndRethrow('ao atualizar oficio')));
  }

  delete(id: string): Observable<void> {
    return this.http
      .delete<void>(`${this.baseUrl}/${id}`)
      .pipe(catchError(this.logAndRethrow('ao excluir oficio')));
  }

  private normalizar(oficio: OficioPayload): OficioPayload {
    return {
      ...oficio,
      id: oficio.id ? String(oficio.id) : undefined,
      tramites: oficio.tramites ?? []
    };
  }

  private logAndRethrow(operacao: string) {
    return (error: unknown) => {
      console.error(`Erro ${operacao}`, error);
      return throwError(() => error);
    };
  }
}
