import { Injectable } from '@angular/core';

export interface ChecklistItem {
  id: string;
  titulo: string;
  concluido: boolean;
  concluidoEm?: string;
}

export interface TaskHistory {
  id: string;
  mensagem: string;
  data: string;
}

export interface TaskPayload {
  titulo: string;
  descricao: string;
  responsavel: string;
  prioridade: 'Alta' | 'Média' | 'Baixa';
  prazo: string;
  status: 'Aberta' | 'Em andamento' | 'Concluída' | 'Em atraso';
  checklist?: ChecklistItem[];
  historico?: TaskHistory[];
}

export interface TaskRecord extends TaskPayload {
  id: string;
  criadoEm: string;
  atualizadoEm?: string;
  checklist: ChecklistItem[];
  historico: TaskHistory[];
}

@Injectable({ providedIn: 'root' })
export class TarefasPendenciasService {
  private readonly storageKey = 'g3.tarefas-pendencias';

  list(): TaskRecord[] {
    return this.load();
  }

  create(payload: TaskPayload): TaskRecord {
    const now = new Date().toISOString();
    const historico: TaskHistory[] = [
      ...(payload.historico ?? []),
      { id: crypto.randomUUID(), mensagem: `Tarefa criada como ${payload.status || 'Aberta'}`, data: now }
    ];

    const record: TaskRecord = {
      ...payload,
      id: crypto.randomUUID(),
      criadoEm: now,
      checklist: payload.checklist ?? [],
      historico,
      status: payload.status ?? 'Aberta'
    };

    const all = [record, ...this.list()];
    this.persist(all);
    return record;
  }

  update(id: string, payload: Partial<TaskPayload>, historyNote?: string): TaskRecord {
    const existing = this.list();
    const current = existing.find((task) => task.id === id);
    const now = new Date().toISOString();
    const historico: TaskHistory[] = [
      ...(payload.historico ?? current?.historico ?? []),
      ...(historyNote ? [{ id: crypto.randomUUID(), mensagem: historyNote, data: now }] : [])
    ];

    const record: TaskRecord = {
      ...current!,
      ...payload,
      id,
      checklist: payload.checklist ?? current?.checklist ?? [],
      historico,
      criadoEm: current?.criadoEm ?? now,
      atualizadoEm: now,
      status: payload.status ?? current?.status ?? 'Aberta'
    } as TaskRecord;

    const merged = existing.map((task) => (task.id === id ? record : task));
    this.persist(merged);
    return record;
  }

  delete(id: string): void {
    const filtered = this.list().filter((task) => task.id !== id);
    this.persist(filtered);
  }

  toggleChecklist(taskId: string, itemId: string): TaskRecord | undefined {
    const tasks = this.list();
    const target = tasks.find((task) => task.id === taskId);
    if (!target) return undefined;

    const checklist = (target.checklist ?? []).map((item) =>
      item.id === itemId
        ? {
            ...item,
            concluido: !item.concluido,
            concluidoEm: !item.concluido ? new Date().toISOString() : undefined
          }
        : item
    );

    const toggled = checklist.find((item) => item.id === itemId);
    const historyMessage = toggled
      ? `Checklist: ${toggled.titulo} ${toggled.concluido ? 'concluída' : 'aberta'}`
      : 'Checklist atualizada';

    return this.update(taskId, { checklist }, historyMessage);
  }

  updateStatus(taskId: string, status: TaskPayload['status']): TaskRecord | undefined {
    const tasks = this.list();
    const target = tasks.find((task) => task.id === taskId);
    if (!target) return undefined;

    return this.update(taskId, { status }, `Status alterado para ${status}`);
  }

  private load(): TaskRecord[] {
    try {
      const saved = typeof localStorage !== 'undefined' ? localStorage.getItem(this.storageKey) : null;
      return saved ? (JSON.parse(saved) as TaskRecord[]) : [];
    } catch (error) {
      console.error('Erro ao ler tarefas do armazenamento local', error);
      return [];
    }
  }

  private persist(data: TaskRecord[]): void {
    try {
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem(this.storageKey, JSON.stringify(data));
      }
    } catch (error) {
      console.error('Erro ao salvar tarefas no armazenamento local', error);
    }
  }
}
