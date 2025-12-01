import { Injectable } from '@angular/core';

export interface VolunteerPayload {
  id?: string;
  nome: string;
  cpf: string;
  email: string;
  telefone?: string;
  cidade?: string;
  estado?: string;
  areaInteresse?: string;
  habilidades?: string;
  idiomas?: string;
  disponibilidadeDias?: string[];
  disponibilidadePeriodos?: string[];
  cargaHoraria?: string;
  presencial?: boolean;
  remoto?: boolean;
  inicioPrevisto?: string;
  motivacao?: string;
  observacoes?: string;
}

@Injectable({ providedIn: 'root' })
export class VolunteerService {
  private readonly storageKey = 'g3.volunteers';

  list(): VolunteerPayload[] {
    try {
      const saved = typeof localStorage !== 'undefined' ? localStorage.getItem(this.storageKey) : null;
      return saved ? (JSON.parse(saved) as VolunteerPayload[]) : [];
    } catch (error) {
      console.error('Erro ao ler voluntários do armazenamento local', error);
      return [];
    }
  }

  create(payload: VolunteerPayload): VolunteerPayload {
    const record: VolunteerPayload = {
      ...payload,
      id: payload.id ?? crypto.randomUUID()
    };
    const all = [record, ...this.list()];
    this.persist(all);
    return record;
  }

  update(id: string, payload: VolunteerPayload): VolunteerPayload {
    const updated: VolunteerPayload = { ...payload, id };
    const all = this.list().map((item) => (item.id === id ? updated : item));
    this.persist(all);
    return updated;
  }

  delete(id: string): void {
    const filtered = this.list().filter((item) => item.id !== id);
    this.persist(filtered);
  }

  private persist(data: VolunteerPayload[]): void {
    try {
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem(this.storageKey, JSON.stringify(data));
      }
    } catch (error) {
      console.error('Erro ao salvar voluntários no armazenamento local', error);
    }
  }
}
