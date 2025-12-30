import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AutorizacaoCompraRequest {
  titulo: string;
  tipo: string;
  area?: string;
  responsavel: string;
  dataPrevista?: string;
  valor: number;
  justificativa?: string;
  centroCusto?: string;
  status: string;
  aprovador?: string;
  decisao?: string;
  observacoesAprovacao?: string;
  dataAprovacao?: string;
  dispensarCotacao?: boolean;
  motivoDispensa?: string;
  vencedor?: string;
  registroPatrimonio?: boolean;
  registroAlmoxarifado?: boolean;
  numeroReserva?: string;
  autorizacaoPagamentoNumero?: string;
  autorizacaoPagamentoAutor?: string;
  autorizacaoPagamentoData?: string;
  autorizacaoPagamentoObservacoes?: string;
  prioridade?: 'urgente' | 'normal' | 'baixa';
  quantidadeItens?: number;
}

export interface AutorizacaoCompraResponse extends AutorizacaoCompraRequest {
  id: number;
  criadoEm: string;
  atualizadoEm: string;
}

@Injectable({ providedIn: 'root' })
export class AutorizacaoComprasService {
  private readonly baseUrl = `${environment.apiUrl}/api/financeiro/autorizacao-compras`;

  constructor(private readonly http: HttpClient) {}

  list(): Observable<AutorizacaoCompraResponse[]> {
    return this.http.get<AutorizacaoCompraResponse[]>(this.baseUrl);
  }

  create(payload: AutorizacaoCompraRequest): Observable<AutorizacaoCompraResponse> {
    return this.http.post<AutorizacaoCompraResponse>(this.baseUrl, payload);
  }

  update(id: string, payload: AutorizacaoCompraRequest): Observable<AutorizacaoCompraResponse> {
    return this.http.put<AutorizacaoCompraResponse>(`${this.baseUrl}/${id}`, payload);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
