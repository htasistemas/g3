import { Injectable } from '@angular/core';

export type ProfessionalStatus = 'Disponível' | 'Em atendimento' | 'Em intervalo' | 'Indisponível';

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

@Injectable({ providedIn: 'root' })
export class ProfessionalService {
  private readonly storageKey = 'g3.professionals';

  list(): ProfessionalRecord[] {
    return this.load();
  }

  create(payload: ProfessionalPayload): ProfessionalRecord {
    const record: ProfessionalRecord = {
      ...payload,
      id: payload.id ?? crypto.randomUUID(),
      criadoEm: new Date().toISOString(),
      status: payload.status ?? 'Disponível'
    };

    const all = [record, ...this.list()];
    this.persist(all);
    return record;
  }

  update(id: string, payload: ProfessionalPayload): ProfessionalRecord {
    const existing = this.list();
    const updated: ProfessionalRecord = {
      ...payload,
      id,
      criadoEm: existing.find((item) => item.id === id)?.criadoEm ?? new Date().toISOString(),
      atualizadoEm: new Date().toISOString(),
      status: payload.status ?? 'Disponível'
    };

    const merged = existing.map((item) => (item.id === id ? updated : item));
    this.persist(merged);
    return updated;
  }

  delete(id: string): void {
    const filtered = this.list().filter((item) => item.id !== id);
    this.persist(filtered);
  }

  search(term: string): ProfessionalRecord[] {
    const normalized = term.trim().toLowerCase();
    if (!normalized) return [];

    return this.list()
      .filter((item) =>
        [item.nome, item.especialidade, item.categoria]
          .filter(Boolean)
          .some((field) => field!.toLowerCase().includes(normalized))
      )
      .slice(0, 8);
  }

  private load(): ProfessionalRecord[] {
    try {
      const saved = typeof localStorage !== 'undefined' ? localStorage.getItem(this.storageKey) : null;
      return saved ? (JSON.parse(saved) as ProfessionalRecord[]) : [];
    } catch (error) {
      console.error('Erro ao ler profissionais do armazenamento local', error);
      return [];
    }
  }

  private persist(data: ProfessionalRecord[]): void {
    try {
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem(this.storageKey, JSON.stringify(data));
      }
    } catch (error) {
      console.error('Erro ao salvar profissionais no armazenamento local', error);
    }
  }
}
