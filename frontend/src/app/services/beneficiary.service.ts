import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

export interface DocumentoObrigatorio {
  nome: string;
  nomeArquivo?: string;
  file?: File;
  obrigatorio?: boolean;
  required?: boolean;
  baseRequired?: boolean;
}

export interface BeneficiaryPayload {
  id?: number;
  cep: string;
  nomeCompleto: string;
  nomeMae?: string;
  documentos: string;
  dataNascimento: string;
  idade?: number;
  telefone: string;
  email: string;
  endereco: string;
  numeroEndereco?: string;
  pontoReferencia?: string;
  bairro?: string;
  cidade?: string;
  estado?: string;
  observacoes?: string;
  status: string;
  possuiFilhosMenores?: boolean;
  possuiCnh?: boolean;
  quantidadeFilhosMenores?: number;
  escolaridade?: string;
  rendaIndividual?: number | string;
  rendaFamiliar?: number | string;
  informacoesMoradia?: string;
  condicoesSaneamento?: string;
  situacaoEmprego?: string;
  ocupacao?: string;
  documentosAnexos: DocumentoObrigatorio[];
  foto?: string | null;
}

@Injectable({ providedIn: 'root' })
export class BeneficiaryService {
  private readonly baseUrl = `${environment.apiUrl}/api/beneficiaries`;

  constructor(private readonly http: HttpClient) {}

  getRequiredDocuments(): Observable<{ documents: DocumentoObrigatorio[] }> {
    return this.http.get<{ documents: DocumentoObrigatorio[] }>(`${this.baseUrl}/documents`);
  }

  list(): Observable<{ beneficiarios: BeneficiaryPayload[] }> {
    return this.http.get<{ beneficiarios: BeneficiaryPayload[] }>(this.baseUrl);
  }

  save(payload: BeneficiaryPayload): Observable<BeneficiaryPayload> {
    if (payload.id) {
      return this.http.put<BeneficiaryPayload>(`${this.baseUrl}/${payload.id}`, payload);
    }

    return this.http.post<BeneficiaryPayload>(this.baseUrl, payload);
  }
}
