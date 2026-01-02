import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';

export interface FotoEventoUpload {
  nomeArquivo: string;
  contentType: string;
  conteudo: string;
}

export interface FotoEventoRequestPayload {
  titulo: string;
  descricao?: string;
  dataEvento: string;
  local?: string;
  tags?: string;
  unidadeId?: number | null;
  fotoPrincipalUpload?: FotoEventoUpload | null;
  fotoPrincipalId?: number | null;
}

export interface FotoEventoResponse {
  id: number;
  unidadeId?: number | null;
  titulo: string;
  descricao?: string | null;
  dataEvento: string;
  local?: string | null;
  tags?: string | null;
  fotoPrincipal?: string | null;
  fotoPrincipalUrl?: string | null;
  criadoEm?: string | null;
  atualizadoEm?: string | null;
}

export interface FotoEventoFotoResponse {
  id: number;
  eventoId: number;
  arquivo?: string | null;
  arquivoUrl?: string | null;
  legenda?: string | null;
  ordem?: number | null;
  criadoEm?: string | null;
}

export interface FotoEventoDetalheResponse {
  evento: FotoEventoResponse;
  fotos: FotoEventoFotoResponse[];
}

export interface FotoEventoListaResponse {
  eventos: FotoEventoResponse[];
  pagina: number;
  tamanho: number;
  total: number;
  totalPaginas: number;
}

@Injectable({ providedIn: 'root' })
export class FotosEventosService {
  private readonly baseUrl = `${environment.apiUrl}/api/fotos-eventos`;

  constructor(private readonly http: HttpClient) {}

  listar(params: {
    busca?: string;
    dataInicio?: string;
    dataFim?: string;
    unidadeId?: number | null;
    pagina?: number;
    tamanho?: number;
  }): Observable<FotoEventoListaResponse> {
    return this.http
      .get<FotoEventoListaResponse>(this.baseUrl, { params: this.toParams(params) })
      .pipe(catchError(this.logAndRethrow('ao carregar eventos')));
  }

  obter(id: number): Observable<FotoEventoDetalheResponse> {
    return this.http
      .get<FotoEventoDetalheResponse>(`${this.baseUrl}/${id}`)
      .pipe(catchError(this.logAndRethrow('ao carregar o evento')));
  }

  criar(payload: FotoEventoRequestPayload): Observable<FotoEventoResponse> {
    return this.http
      .post<FotoEventoResponse>(this.baseUrl, payload)
      .pipe(catchError(this.logAndRethrow('ao criar o evento')));
  }

  atualizar(id: number, payload: FotoEventoRequestPayload): Observable<FotoEventoResponse> {
    return this.http
      .put<FotoEventoResponse>(`${this.baseUrl}/${id}`, payload)
      .pipe(catchError(this.logAndRethrow('ao atualizar o evento')));
  }

  excluir(id: number): Observable<void> {
    return this.http
      .delete<void>(`${this.baseUrl}/${id}`)
      .pipe(catchError(this.logAndRethrow('ao excluir o evento')));
  }

  adicionarFoto(
    id: number,
    payload: { arquivo: FotoEventoUpload; legenda?: string; ordem?: number | null }
  ): Observable<FotoEventoFotoResponse> {
    return this.http
      .post<FotoEventoFotoResponse>(`${this.baseUrl}/${id}/fotos`, payload)
      .pipe(catchError(this.logAndRethrow('ao adicionar a foto')));
  }

  atualizarFoto(
    id: number,
    fotoId: number,
    payload: { legenda?: string | null; ordem?: number | null }
  ): Observable<FotoEventoFotoResponse> {
    return this.http
      .put<FotoEventoFotoResponse>(`${this.baseUrl}/${id}/fotos/${fotoId}`, payload)
      .pipe(catchError(this.logAndRethrow('ao atualizar a foto')));
  }

  removerFoto(id: number, fotoId: number): Observable<void> {
    return this.http
      .delete<void>(`${this.baseUrl}/${id}/fotos/${fotoId}`)
      .pipe(catchError(this.logAndRethrow('ao remover a foto')));
  }

  private toParams(params: {
    busca?: string;
    dataInicio?: string;
    dataFim?: string;
    unidadeId?: number | null;
    pagina?: number;
    tamanho?: number;
  }): Record<string, string> {
    const result: Record<string, string> = {};
    if (params.busca) {
      result['busca'] = params.busca;
    }
    if (params.dataInicio) {
      result['dataInicio'] = params.dataInicio;
    }
    if (params.dataFim) {
      result['dataFim'] = params.dataFim;
    }
    if (params.unidadeId) {
      result['unidadeId'] = String(params.unidadeId);
    }
    if (params.pagina !== undefined) {
      result['pagina'] = String(params.pagina);
    }
    if (params.tamanho !== undefined) {
      result['tamanho'] = String(params.tamanho);
    }
    return result;
  }

  private logAndRethrow(operation: string) {
    return (error: unknown) => {
      console.error(`Erro ${operation}`, error);
      return throwError(() => error);
    };
  }
}
