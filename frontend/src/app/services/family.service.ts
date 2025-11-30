import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { BeneficiaryPayload } from './beneficiary.service';

export interface FamilyMemberPayload {
  id?: number;
  beneficiarioId: number;
  parentesco: string;
  ehResponsavelFamiliar: boolean;
  situacaoTrabalho?: string;
  rendaIndividual?: number;
  escolaridade?: string;
  possuiDeficiencia?: boolean;
  contribuiComRenda?: boolean;
  participaServicos?: boolean;
  dataEntradaFamilia?: string;
  observacoes?: string;
  beneficiario?: BeneficiaryPayload;
}

export interface FamilySaneamento {
  agua?: boolean;
  esgoto?: boolean;
  lixo?: boolean;
  energia?: boolean;
}

export interface FamilyIncomeDetails {
  rendaTotal?: number | string;
  rendaPerCapita?: number | string;
  faixaRenda?: string;
  fontes?: {
    trabalhoFormal?: boolean;
    trabalhoInformal?: boolean;
    previdenciario?: boolean;
    assistencial?: boolean;
    doacoes?: boolean;
    outros?: boolean;
  };
  despesas?: {
    aluguel?: string;
    alimentacao?: string;
    aguaLuz?: string;
    transporte?: string;
    medicamentos?: string;
    outros?: string;
  };
  insegurancaAlimentar?: string;
  possuiDividas?: boolean;
  detalhesDividas?: string;
}

export interface FamilyVulnerability {
  violenciaDomestica?: boolean;
  trabalhoInfantil?: boolean;
  situacaoRua?: boolean;
  desempregoLongo?: boolean;
  moradiaPrecaria?: boolean;
  dependenciaQuimica?: boolean;
  outras?: string;
  programas?: string;
  historico?: string;
  tecnicoResponsavel?: string;
  periodicidade?: string;
  proximaVisita?: string;
}

export interface FamilyPayload {
  id?: number;
  responsavelFamiliarId?: number | null;
  beneficiarioReferenciaId?: number | null;
  nomeReferencia?: string;
  tipoArranjo?: string;
  arranjoOutro?: string;
  logradouro: string;
  numero: string;
  bairro: string;
  cidade: string;
  uf: string;
  cep: string;
  complemento?: string;
  pontoReferencia?: string;
  zona?: string;
  situacaoImovel?: string;
  tipoMoradia?: string;
  saneamento?: FamilySaneamento;
  membros: FamilyMemberPayload[];
  rendaFamiliar?: FamilyIncomeDetails;
  vulnerabilidadeFamiliar?: FamilyVulnerability;
}

@Injectable({ providedIn: 'root' })
export class FamilyService {
  private readonly baseUrl = `${environment.apiUrl}/api/familias`;

  constructor(private readonly http: HttpClient) {}

  getById(id: number): Observable<{ familia: FamilyPayload }> {
    return this.http.get<{ familia: FamilyPayload }>(`${this.baseUrl}/${id}`);
  }

  save(payload: FamilyPayload): Observable<FamilyPayload> {
    if (payload.id) {
      return this.http.put<FamilyPayload>(`${this.baseUrl}/${payload.id}`, payload);
    }

    return this.http.post<FamilyPayload>(this.baseUrl, payload);
  }
}
