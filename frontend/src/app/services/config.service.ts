import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

export interface BeneficiaryDocumentConfig {
  id?: number;
  nome: string;
  obrigatorio: boolean;
}

export interface VersaoSistemaResponse {
  versao: string;
  descricao?: string;
  atualizadoEm?: string;
}

export interface AtualizarVersaoRequest {
  versao: string;
  descricao?: string;
}

export interface HistoricoVersaoResponse {
  id: number;
  versao: string;
  descricao?: string;
  criadoEm: string;
}

export interface DestinoChamadoResponse {
  destino: string;
}

@Injectable({ providedIn: 'root' })
export class ConfigService {
  private readonly baseUrl = `${environment.apiUrl}/api/config`;

  constructor(private readonly http: HttpClient) {}

  getBeneficiaryDocuments(): Observable<{ documents: BeneficiaryDocumentConfig[] }> {
    return this.http.get<{ documents: BeneficiaryDocumentConfig[] }>(`${this.baseUrl}/beneficiary-documents`);
  }

  updateBeneficiaryDocuments(documents: BeneficiaryDocumentConfig[]): Observable<{ documents: BeneficiaryDocumentConfig[] }> {
    return this.http.put<{ documents: BeneficiaryDocumentConfig[] }>(`${this.baseUrl}/beneficiary-documents`, { documents });
  }

  getVersaoSistema(): Observable<VersaoSistemaResponse> {
    return this.http.get<VersaoSistemaResponse>(`${this.baseUrl}/versao`);
  }

  atualizarVersaoSistema(payload: AtualizarVersaoRequest): Observable<VersaoSistemaResponse> {
    return this.http.put<VersaoSistemaResponse>(`${this.baseUrl}/versao`, payload);
  }

  listarHistoricoVersoes(): Observable<HistoricoVersaoResponse[]> {
    return this.http.get<HistoricoVersaoResponse[]>(`${this.baseUrl}/versao/historico`);
  }

  getVersaoArquivo(): Observable<string> {
    return this.http.get(`${environment.apiUrl}/version.txt`, { responseType: 'text' });
  }

  getDestinoChamados(): Observable<DestinoChamadoResponse> {
    return this.http.get<DestinoChamadoResponse>(`${this.baseUrl}/chamados/destino`);
  }
}
