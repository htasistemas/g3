import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { map, Observable } from 'rxjs';

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
  parentesco?: string;
  observacoes?: string;
  status: string;
  motivoBloqueio?: string;
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

  getById(id: number): Observable<BeneficiaryPayload> {
    return this.http.get<{ beneficiario: BeneficiaryPayload }>(`${this.baseUrl}/${id}`).pipe(map(({ beneficiario }) => beneficiario));
  }

  getRequiredDocuments(): Observable<{ documents: DocumentoObrigatorio[] }> {
    return this.http.get<{ documents: DocumentoObrigatorio[] }>(`${this.baseUrl}/documents`);
  }

  list(): Observable<{ beneficiarios: BeneficiaryPayload[] }> {
    return this.http.get<
      { beneficiarios?: BeneficiaryPayload[] } | { beneficiaries?: BeneficiaryPayload[] } | BeneficiaryPayload[]
    >(this.baseUrl).pipe(
      map((response) => {
        if (Array.isArray(response)) {
          return { beneficiarios: response };
        }

        if ('beneficiarios' in response) {
          return { beneficiarios: response.beneficiarios ?? [] };
        }

        if ('beneficiaries' in response) {
          return { beneficiarios: response.beneficiaries ?? [] };
        }

        return { beneficiarios: [] };
      })
    );
  }

  save(payload: BeneficiaryPayload, photoFile?: File | null): Observable<BeneficiaryPayload> {
    const formData = this.buildFormData(payload, photoFile);

    if (payload.id) {
      return this.http.put<BeneficiaryPayload>(`${this.baseUrl}/${payload.id}`, formData);
    }

    return this.http.post<BeneficiaryPayload>(this.baseUrl, formData);
  }

  private buildFormData(payload: BeneficiaryPayload, photoFile?: File | null): FormData {
    const formData = new FormData();
    const { documentosAnexos, foto, ...rest } = payload;

    Object.entries(rest).forEach(([key, value]) => {
      if (value === undefined || value === null) {
        return;
      }

      formData.append(key, typeof value === 'object' ? JSON.stringify(value) : String(value));
    });

    formData.append('documentosAnexos', JSON.stringify(documentosAnexos ?? []));

    if (photoFile) {
      formData.append('fotoArquivo', photoFile, photoFile.name || 'foto.jpg');
    } else if (foto) {
      formData.append('foto', foto);
    }

    return formData;
  }
}
