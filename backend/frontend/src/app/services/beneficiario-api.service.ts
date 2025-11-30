import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface BeneficiarioApiPayload {
  id_beneficiario?: string;
  nome_completo: string;
  nome_social?: string;
  apelido?: string;
  data_nascimento: string;
  sexo_biologico?: string;
  identidade_genero?: string;
  cor_raca?: string;
  estado_civil?: string;
  nacionalidade?: string;
  naturalidade_cidade?: string;
  naturalidade_uf?: string;
  nome_mae: string;
  nome_pai?: string;
  cpf?: string | null;
  rg_numero?: string;
  rg_orgao_emissor?: string;
  rg_uf?: string;
  rg_data_emissao?: string;
  nis?: string;
  certidao_tipo?: string;
  certidao_livro?: string;
  certidao_folha?: string;
  certidao_termo?: string;
  certidao_cartorio?: string;
  certidao_municipio?: string;
  certidao_uf?: string;
  titulo_eleitor?: string;
  cnh?: string;
  cartao_sus?: string;
  telefone_principal?: string;
  telefone_principal_whatsapp?: boolean;
  telefone_secundario?: string;
  telefone_recado_nome?: string;
  telefone_recado_numero?: string;
  email?: string;
  permite_contato_tel?: boolean;
  permite_contato_whatsapp?: boolean;
  permite_contato_sms?: boolean;
  permite_contato_email?: boolean;
  horario_preferencial_contato?: string;
  usa_endereco_familia?: boolean;
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
  mora_com_familia?: boolean;
  responsavel_legal?: boolean;
  vinculo_familiar?: string;
  situacao_vulnerabilidade?: string;
  sabe_ler_escrever?: boolean;
  nivel_escolaridade?: string;
  estuda_atualmente?: boolean;
  ocupacao?: string;
  situacao_trabalho?: string;
  local_trabalho?: string;
  renda_mensal?: number | string;
  fonte_renda?: string;
  possui_deficiencia?: boolean;
  tipo_deficiencia?: string;
  cid_principal?: string;
  usa_medicacao_continua?: boolean;
  descricao_medicacao?: string;
  servico_saude_referencia?: string;
  recebe_beneficio?: boolean;
  beneficios_descricao?: string;
  valor_total_beneficios?: number | string;
  aceite_lgpd?: boolean;
  data_aceite_lgpd?: string;
  observacoes?: string;
}

@Injectable({ providedIn: 'root' })
export class BeneficiarioApiService {
  private readonly baseUrl = `${environment.apiUrl}/api/beneficiarios`;

  constructor(private readonly http: HttpClient) {}

  list(params?: { nome?: string; cpf?: string; nis?: string }): Observable<{ beneficiarios: BeneficiarioApiPayload[] }> {
    let httpParams = new HttpParams();
    if (params?.nome) httpParams = httpParams.set('nome', params.nome);
    if (params?.cpf) httpParams = httpParams.set('cpf', params.cpf);
    if (params?.nis) httpParams = httpParams.set('nis', params.nis);
    return this.http.get<{ beneficiarios: BeneficiarioApiPayload[] }>(this.baseUrl, { params: httpParams });
  }

  getById(id: string): Observable<{ beneficiario: BeneficiarioApiPayload }> {
    return this.http.get<{ beneficiario: BeneficiarioApiPayload }>(`${this.baseUrl}/${id}`);
  }

  create(payload: BeneficiarioApiPayload): Observable<{ beneficiario: BeneficiarioApiPayload }> {
    return this.http.post<{ beneficiario: BeneficiarioApiPayload }>(this.baseUrl, payload);
  }

  update(id: string, payload: BeneficiarioApiPayload): Observable<{ beneficiario: BeneficiarioApiPayload }> {
    return this.http.put<{ beneficiario: BeneficiarioApiPayload }>(`${this.baseUrl}/${id}`, payload);
  }
}
