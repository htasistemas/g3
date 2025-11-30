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

export interface FamilySaneamento {
  agua: boolean;
  esgoto: boolean;
  lixo: boolean;
  energia: boolean;
}

export interface FamilyIncomeDetails {
  rendaTotal: number;
  rendaPerCapita: number;
  faixaRenda?: string;
  fontes: {
    trabalhoFormal: boolean;
    trabalhoInformal: boolean;
    previdenciario: boolean;
    assistencial: boolean;
    doacoes: boolean;
    outros: boolean;
  };
  despesas: {
    aluguel?: string;
    alimentacao?: string;
    aguaLuz?: string;
    transporte?: string;
    medicamentos?: string;
    outros?: string;
  };
  insegurancaAlimentar?: string;
  possuiDividas: boolean;
  detalhesDividas?: string;
}

export interface FamilyVulnerability {
  violenciaDomestica: boolean;
  trabalhoInfantil: boolean;
  situacaoRua: boolean;
  desempregoLongo: boolean;
  moradiaPrecaria: boolean;
  dependenciaQuimica: boolean;
  outras?: string;
  programas?: string;
  historico?: string;
  tecnicoResponsavel?: string;
  periodicidade?: string;
  proximaVisita?: string;
}

export interface FamilyMemberPayload {
  beneficiarioId: string;
  parentesco: string;
  ehResponsavelFamiliar?: boolean;
  observacoes?: string;
  situacaoTrabalho?: string;
  rendaIndividual?: number;
  escolaridade?: string;
  possuiDeficiencia?: boolean;
  contribuiComRenda?: boolean;
  participaServicos?: boolean;
}

export interface FamilyPayload {
  id?: string;
  responsavelFamiliarId: string | null;
  beneficiarioReferenciaId: string | null;
  nomeReferencia?: string;
  tipoArranjo?: string;
  arranjoOutro?: string;
  logradouro?: string;
  numero?: string;
  bairro?: string;
  cidade?: string;
  uf?: string;
  cep?: string;
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

  save(payload: FamilyPayload) {
    const familiaPayload = this.toFamiliaPayload(payload);
    return payload.id
      ? this.update(payload.id, familiaPayload)
      : this.create(familiaPayload);
  }

  private toFamiliaPayload(payload: FamilyPayload): FamiliaPayload {
    const renda = payload.rendaFamiliar;
    const vulnerabilidade = payload.vulnerabilidadeFamiliar;
    const saneamento = payload.saneamento;

    const membros: FamiliaMembroPayload[] = (payload.membros || []).map((membro) => ({
      id_beneficiario: membro.beneficiarioId,
      parentesco: membro.parentesco,
      responsavel_familiar: membro.ehResponsavelFamiliar,
      observacoes: membro.observacoes,
      renda_individual: membro.rendaIndividual,
      contribui_renda: membro.contribuiComRenda,
      participa_servicos: membro.participaServicos
    }));

    const familia: FamiliaPayload = {
      id_familia: payload.id,
      nome_familia: payload.nomeReferencia || '',
      id_referencia_familiar: payload.responsavelFamiliarId || payload.beneficiarioReferenciaId || undefined,
      arranjo_familiar: payload.tipoArranjo === 'outra' ? payload.arranjoOutro : payload.tipoArranjo,
      logradouro: payload.logradouro || '',
      numero: payload.numero || '',
      bairro: payload.bairro || '',
      municipio: payload.cidade || '',
      uf: payload.uf || '',
      cep: payload.cep || '',
      complemento: payload.complemento,
      ponto_referencia: payload.pontoReferencia,
      zona: payload.zona,
      situacao_imovel: payload.situacaoImovel,
      tipo_moradia: payload.tipoMoradia,
      agua_encanada: saneamento?.agua,
      esgoto_tipo: saneamento ? (saneamento.esgoto ? 'sim' : 'nao') : undefined,
      coleta_lixo: saneamento ? (saneamento.lixo ? 'sim' : 'nao') : undefined,
      energia_eletrica: saneamento?.energia,
      renda_familiar_total: renda?.rendaTotal,
      renda_per_capita: renda?.rendaPerCapita,
      faixa_renda_per_capita: renda?.faixaRenda,
      principais_fontes_renda: this.mapIncomeSources(renda),
      situacao_inseguranca_alimentar: renda?.insegurancaAlimentar,
      possui_dividas_relevantes: renda?.possuiDividas,
      descricao_dividas: renda?.detalhesDividas,
      vulnerabilidades_familia: this.mapVulnerability(vulnerabilidade),
      servicos_acompanhamento: vulnerabilidade?.programas,
      tecnico_responsavel: vulnerabilidade?.tecnicoResponsavel,
      periodicidade_atendimento: vulnerabilidade?.periodicidade,
      proxima_visita_prevista: vulnerabilidade?.proximaVisita,
      observacoes: vulnerabilidade?.historico,
      membros
    };

    return familia;
  }

  private mapIncomeSources(renda?: FamilyIncomeDetails): string | undefined {
    if (!renda) return undefined;
    const fontes: string[] = [];
    const labels: Record<keyof FamilyIncomeDetails['fontes'], string> = {
      trabalhoFormal: 'Trabalho formal',
      trabalhoInformal: 'Trabalho informal',
      previdenciario: 'Benefício previdenciário',
      assistencial: 'Benefício assistencial',
      doacoes: 'Doações',
      outros: 'Outros'
    };

    for (const key of Object.keys(renda.fontes) as (keyof FamilyIncomeDetails['fontes'])[]) {
      if (renda.fontes[key]) fontes.push(labels[key]);
    }

    return fontes.length ? fontes.join(', ') : undefined;
  }

  private mapVulnerability(vulnerability?: FamilyVulnerability): string | undefined {
    if (!vulnerability) return undefined;
    const flags: { field: keyof FamilyVulnerability; label: string }[] = [
      { field: 'violenciaDomestica', label: 'Violência doméstica' },
      { field: 'trabalhoInfantil', label: 'Trabalho infantil' },
      { field: 'situacaoRua', label: 'Situação de rua' },
      { field: 'desempregoLongo', label: 'Desemprego prolongado' },
      { field: 'moradiaPrecaria', label: 'Moradia precária' },
      { field: 'dependenciaQuimica', label: 'Dependência química' }
    ];

    const selected = flags.filter(({ field }) => vulnerability[field]).map(({ label }) => label);
    if (vulnerability.outras) selected.push(`Outras: ${vulnerability.outras}`);

    return selected.length ? selected.join(', ') : undefined;
  }
}
