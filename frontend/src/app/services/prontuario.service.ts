import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface ProntuarioIdentificacao {
  unidadeReferencia?: string;
  responsavelTecnico?: string;
  dataAbertura?: string;
  observacoes?: string;
}

export interface ProntuarioComposicaoFamiliarItem {
  id?: string;
  nome?: string;
  parentesco?: string;
  renda?: string | null;
  responsavelFamiliar?: boolean;
}

export interface ProntuarioComposicaoFamiliar {
  familiaId?: string;
  resumo?: string;
  membros?: ProntuarioComposicaoFamiliarItem[];
}

export interface ProntuarioParticipacao {
  servicoOuProjeto?: string;
  papel?: string;
  situacao?: string;
  dataInicio?: string;
  dataFim?: string;
  observacoes?: string;
}

export interface ProntuarioAtendimento {
  idProntuarioAtendimento?: string;
  dataAtendimento?: string;
  tipoAtendimento?: string;
  descricao?: string;
  responsavel?: string;
  resultado?: string;
}

export interface ProntuarioEncaminhamento {
  idProntuarioEncaminhamento?: string;
  dataEncaminhamento?: string;
  destino?: string;
  motivo?: string;
  responsavel?: string;
  status?: string;
  retornoPrevisto?: string;
  observacoes?: string;
}

export interface ProntuarioPayload {
  identificacao?: ProntuarioIdentificacao;
  composicaoFamiliar?: ProntuarioComposicaoFamiliar;
  situacoesVulnerabilidade?: string[];
  participacoesServicos?: ProntuarioParticipacao[];
  parecerTecnico?: string;
  historicoAtendimentos?: ProntuarioAtendimento[];
  encaminhamentos?: ProntuarioEncaminhamento[];
}

export interface ProntuarioResponse {
  beneficiario: any;
  prontuario?: ProntuarioPayload & { idProntuario: string };
  composicaoFamiliar: any[];
  historicoAtendimentos: ProntuarioAtendimento[];
  encaminhamentos: ProntuarioEncaminhamento[];
}

@Injectable({ providedIn: 'root' })
export class ProntuarioService {
  private readonly baseUrl = `${environment.apiUrl}/api/prontuario`;

  constructor(private readonly http: HttpClient) {}

  carregar(beneficiarioId: string): Observable<ProntuarioResponse> {
    return this.http.get<ProntuarioResponse>(`${this.baseUrl}/${beneficiarioId}`);
  }

  salvar(beneficiarioId: string, payload: ProntuarioPayload): Observable<ProntuarioResponse> {
    return this.http.post<ProntuarioResponse>(`${this.baseUrl}/${beneficiarioId}`, payload);
  }
}
