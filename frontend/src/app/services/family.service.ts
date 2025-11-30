import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { BeneficiarioApiPayload } from './beneficiario-api.service';

export interface FamiliaMembroPayload {
  id_familia_membro?: string;
  id_beneficiario: string;
  parentesco: string;
  responsavel_familiar?: boolean;
  contribui_renda?: boolean;
  renda_individual?: number | string;
  participa_servicos?: boolean;
  observacoes?: string;
  beneficiario?: BeneficiarioApiPayload;
}

export interface FamiliaPayload {
  id_familia?: string;
  nome_familia: string;
  id_referencia_familiar?: string;
  cep?: string;
  logradouro?: string;
  numero?: string;
  complemento?: string;
  bairro?: string;
  ponto_referencia?: string;
  municipio?: string;
  uf?: string;
  zona?: string;
  situacao_imovel?: string;
  tipo_moradia?: string;
  agua_encanada?: boolean;
  esgoto_tipo?: string;
  coleta_lixo?: string;
  energia_eletrica?: boolean;
  internet?: boolean;
  arranjo_familiar?: string;
  qtd_membros?: number;
  qtd_criancas?: number;
  qtd_adolescentes?: number;
  qtd_idosos?: number;
  qtd_pessoas_deficiencia?: number;
  renda_familiar_total?: number | string;
  renda_per_capita?: number | string;
  faixa_renda_per_capita?: string;
  principais_fontes_renda?: string;
  situacao_inseguranca_alimentar?: string;
  possui_dividas_relevantes?: boolean;
  descricao_dividas?: string;
  vulnerabilidades_familia?: string;
  servicos_acompanhamento?: string;
  tecnico_responsavel?: string;
  periodicidade_atendimento?: string;
  proxima_visita_prevista?: string;
  observacoes?: string;
  membros?: FamiliaMembroPayload[];
}

@Injectable({ providedIn: 'root' })
export class FamilyService {
  private readonly baseUrl = `${environment.apiUrl}/api/familias`;

  constructor(private readonly http: HttpClient) {}

  list(params?: { nome_familia?: string; municipio?: string; referencia?: string }): Observable<{ familias: FamiliaPayload[] }> {
    let httpParams = new HttpParams();
    if (params?.nome_familia) httpParams = httpParams.set('nome_familia', params.nome_familia);
    if (params?.municipio) httpParams = httpParams.set('municipio', params.municipio);
    if (params?.referencia) httpParams = httpParams.set('referencia', params.referencia);
    return this.http.get<{ familias: FamiliaPayload[] }>(this.baseUrl, { params: httpParams });
  }

  getById(id: string): Observable<{ familia: FamiliaPayload }> {
    return this.http.get<{ familia: FamiliaPayload }>(`${this.baseUrl}/${id}`);
  }

  create(payload: FamiliaPayload): Observable<{ familia: FamiliaPayload }> {
    return this.http.post<{ familia: FamiliaPayload }>(this.baseUrl, payload);
  }

  update(id: string, payload: FamiliaPayload): Observable<{ familia: FamiliaPayload }> {
    return this.http.put<{ familia: FamiliaPayload }>(`${this.baseUrl}/${id}`, payload);
  }

  addMember(familiaId: string, membro: FamiliaMembroPayload) {
    return this.http.post(`${this.baseUrl}/${familiaId}/membros`, membro);
  }

  updateMember(familiaId: string, membroId: string, membro: FamiliaMembroPayload) {
    return this.http.put(`${this.baseUrl}/${familiaId}/membros/${membroId}`, membro);
  }

  removeMember(familiaId: string, membroId: string) {
    return this.http.delete(`${this.baseUrl}/${familiaId}/membros/${membroId}`);
  }
}
