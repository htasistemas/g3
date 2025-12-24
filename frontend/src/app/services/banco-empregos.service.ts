import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';

export type JobStatus = 'Aberta' | 'Em andamento' | 'Encerrada';

export interface JobDadosVaga {
  titulo: string;
  status: JobStatus;
  dataAbertura: string;
  dataEncerramento?: string;
  tipoContrato?: string;
  cargaHoraria?: string;
  salario?: string;
}

export interface JobEmpresaLocal {
  nomeEmpresa: string;
  cnpj?: string;
  cidade: string;
  endereco?: string;
  bairro?: string;
}

export interface JobRequisitos {
  requisitos?: string;
  descricao: string;
  observacoes?: string;
}

export interface JobEncaminhamento {
  id: string;
  beneficiarioId: string;
  beneficiarioNome: string;
  data: string;
  status: string;
  observacoes?: string;
}

export interface JobRecord {
  id: string;
  dadosVaga: JobDadosVaga;
  empresaLocal: JobEmpresaLocal;
  requisitos: JobRequisitos;
  encaminhamentos: JobEncaminhamento[];
  createdAt: string;
  updatedAt?: string;
}

export type JobPayload = Omit<JobRecord, 'id' | 'createdAt' | 'updatedAt'> & { id?: string };

@Injectable({ providedIn: 'root' })
export class BancoEmpregosService {
  private readonly storageKey = 'g3:banco-empregos';
  private readonly records$ = new BehaviorSubject<JobRecord[]>(this.readFromStorage());

  list(): Observable<JobRecord[]> {
    return this.records$.asObservable();
  }

  save(payload: JobPayload): Observable<JobRecord> {
    const now = new Date().toISOString();
    const current = this.readFromStorage();
    const record: JobRecord = {
      id: payload.id ?? this.generateId(),
      dadosVaga: payload.dadosVaga,
      empresaLocal: payload.empresaLocal,
      requisitos: payload.requisitos,
      encaminhamentos: payload.encaminhamentos ?? [],
      createdAt: payload.id ? this.findExistingCreatedAt(current, payload.id) ?? now : now,
      updatedAt: now
    };

    const updated = payload.id
      ? current.map((item) => (item.id === record.id ? { ...record } : item))
      : [record, ...current];

    this.persist(updated);
    this.records$.next(this.sortRecords(updated));
    return of(record);
  }

  delete(id: string): Observable<void> {
    const filtered = this.readFromStorage().filter((item) => item.id !== id);
    this.persist(filtered);
    this.records$.next(this.sortRecords(filtered));
    return of(void 0);
  }

  private findExistingCreatedAt(records: JobRecord[], id: string): string | undefined {
    return records.find((item) => item.id === id)?.createdAt;
  }

  private readFromStorage(): JobRecord[] {
    if (typeof localStorage === 'undefined') return this.records$?.value ?? [];

    try {
      const raw = localStorage.getItem(this.storageKey);
      if (!raw) return [];
      const parsed = JSON.parse(raw) as JobRecord[];
      return this.sortRecords(parsed);
    } catch (error) {
      console.error('Falha ao ler vagas salvas', error);
      return [];
    }
  }

  private persist(records: JobRecord[]): void {
    if (typeof localStorage === 'undefined') return;

    try {
      localStorage.setItem(this.storageKey, JSON.stringify(records));
    } catch (error) {
      console.error('Não foi possível salvar as vagas localmente', error);
    }
  }

  private generateId(): string {
    if (typeof crypto !== 'undefined' && 'randomUUID' in crypto) {
      return crypto.randomUUID();
    }

    return `vaga-${Date.now()}-${Math.random().toString(16).slice(2, 8)}`;
  }

  private sortRecords(records: JobRecord[]): JobRecord[] {
    return [...records].sort((a, b) => b.createdAt.localeCompare(a.createdAt));
  }
}
