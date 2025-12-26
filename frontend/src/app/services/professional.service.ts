import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, catchError, map, throwError } from 'rxjs';
import { environment } from '../../environments/environment';

export type ProfessionalStatus = 'Disponivel' | 'Em atendimento' | 'Em intervalo' | 'Indisponivel';

export interface ProfessionalPayload {
  id?: string;
  nome: string;
  categoria: string;
  registroConselho?: string;
  especialidade?: string;
  email?: string;
  telefone?: string;
  unidade?: string;
  cargaHoraria?: number;
  disponibilidade?: string[];
  canaisAtendimento?: string[];
  status?: ProfessionalStatus;
  tags?: string[];
  resumo?: string;
  observacoes?: string;
}

export interface ProfessionalRecord extends ProfessionalPayload {
  id: string;
  criadoEm: string;
  atualizadoEm?: string;
  status: ProfessionalStatus;
}

type ProfessionalApiPayload = {
  id_profissional?: string | number;
  nome_completo: string;
  categoria: string;
  registro_conselho?: string;
  especialidade?: string;
  email?: string;
  telefone?: string;
  unidade?: string;
  carga_horaria?: number;
  disponibilidade?: string[];
  canais_atendimento?: string[];
  status?: string;
  tags?: string[];
  resumo?: string;
  observacoes?: string;
  data_cadastro?: string;
  data_atualizacao?: string;
};

@Injectable({ providedIn: 'root' })
export class ProfessionalService {
  private readonly baseUrl = `${environment.apiUrl}/api/profissionais`;
  private cachedProfessionals: ProfessionalRecord[] = [];

  constructor(private readonly http: HttpClient) {}

  list(nome?: string): Observable<ProfessionalRecord[]> {
    let params = new HttpParams();
    if (nome) params = params.set('nome', nome);

    return this.http
      .get<{ profissionais: ProfessionalApiPayload[] }>(this.baseUrl, { params })
      .pipe(
        map((response) => {
          const records = (response.profissionais ?? []).map((item) => this.mapApiToRecord(item));
          if (!nome) {
            this.cachedProfessionals = records;
          }
          return records;
        }),
        catchError((error) => {
          console.error('Erro ao carregar profissionais', error);
          return throwError(() => error);
        })
      );
  }

  create(payload: ProfessionalPayload): Observable<ProfessionalRecord> {
    return this.http
      .post<{ profissional: ProfessionalApiPayload }>(this.baseUrl, this.mapPayloadToApi(payload))
      .pipe(map(({ profissional }) => this.mapApiToRecord(profissional)));
  }

  update(id: string, payload: ProfessionalPayload): Observable<ProfessionalRecord> {
    return this.http
      .put<{ profissional: ProfessionalApiPayload }>(`${this.baseUrl}/${id}`, this.mapPayloadToApi(payload))
      .pipe(map(({ profissional }) => this.mapApiToRecord(profissional)));
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  search(term: string): ProfessionalRecord[] {
    const normalized = term.trim().toLowerCase();
    if (!normalized) return [];

    return (this.cachedProfessionals ?? [])
      .filter((item) =>
        [item.nome, item.especialidade, item.categoria]
          .filter(Boolean)
          .some((field) => field!.toLowerCase().includes(normalized))
      )
      .slice(0, 8);
  }

  private mapPayloadToApi(payload: ProfessionalPayload): ProfessionalApiPayload {
    return {
      nome_completo: payload.nome,
      categoria: payload.categoria,
      registro_conselho: payload.registroConselho,
      especialidade: payload.especialidade,
      email: payload.email,
      telefone: payload.telefone,
      unidade: payload.unidade,
      carga_horaria: payload.cargaHoraria,
      disponibilidade: payload.disponibilidade ?? [],
      canais_atendimento: payload.canaisAtendimento ?? [],
      status: payload.status,
      tags: payload.tags ?? [],
      resumo: payload.resumo,
      observacoes: payload.observacoes
    };
  }

  private mapApiToRecord(item: ProfessionalApiPayload): ProfessionalRecord {
    return {
      id: String(item.id_profissional ?? ''),
      nome: item.nome_completo,
      categoria: item.categoria,
      registroConselho: item.registro_conselho,
      especialidade: item.especialidade,
      email: item.email,
      telefone: item.telefone,
      unidade: item.unidade,
      cargaHoraria: item.carga_horaria,
      disponibilidade: item.disponibilidade ?? [],
      canaisAtendimento: item.canais_atendimento ?? [],
      status: (item.status as ProfessionalStatus) ?? 'Disponivel',
      tags: item.tags ?? [],
      resumo: item.resumo,
      observacoes: item.observacoes,
      criadoEm: item.data_cadastro ?? new Date().toISOString(),
      atualizadoEm: item.data_atualizacao
    };
  }
}
