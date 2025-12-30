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

export interface AutorizacaoCompraCotacaoRequest {
  fornecedor: string;
  razaoSocial?: string;
  cnpj?: string;
  valor: number;
  prazoEntrega?: string;
  validade?: string;
  conformidade?: string;
  observacoes?: string;
  orcamentoFisicoNome?: string;
  orcamentoFisicoTipo?: string;
  orcamentoFisicoConteudo?: string;
}

export interface AutorizacaoCompraCotacaoResponse extends AutorizacaoCompraCotacaoRequest {
  id: number;
  autorizacaoCompraId: number;
  criadoEm: string;
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

  listQuotes(id: string): Observable<AutorizacaoCompraCotacaoResponse[]> {
    return this.http.get<AutorizacaoCompraCotacaoResponse[]>(`${this.baseUrl}/${id}/cotacoes`);
  }

  createQuote(
    id: string,
    payload: AutorizacaoCompraCotacaoRequest
  ): Observable<AutorizacaoCompraCotacaoResponse> {
    return this.http.post<AutorizacaoCompraCotacaoResponse>(`${this.baseUrl}/${id}/cotacoes`, payload);
  }
}
