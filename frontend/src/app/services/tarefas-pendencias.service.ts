import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../environments/environment';

export interface ChecklistItem {
  id: string;
  titulo: string;
  concluido: boolean;
  concluidoEm?: string;
  ordem?: number;
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
  prazo?: string;
  status: 'Aberta' | 'Em andamento' | 'Concluída' | 'Em atraso';
  checklist?: ChecklistItem[];
}

export interface TaskRecord extends TaskPayload {
  id: string;
  criadoEm: string;
  atualizadoEm?: string;
  checklist: ChecklistItem[];
  historico: TaskHistory[];
}

interface TarefaPendenciaApiChecklist {
  id?: number;
  titulo: string;
  concluido: boolean;
  concluidoEm?: string;
  ordem: number;
}

interface TarefaPendenciaApiHistorico {
  id: number;
  mensagem: string;
  criadoEm: string;
}

interface TarefaPendenciaApiResponse {
  id: number;
  titulo: string;
  descricao: string;
  responsavel: string;
  prioridade: string;
  prazo?: string;
  status: string;
  criadoEm: string;
  atualizadoEm?: string;
  checklist: TarefaPendenciaApiChecklist[];
  historico: TarefaPendenciaApiHistorico[];
}

interface TarefaPendenciaApiRequest {
  titulo: string;
  descricao: string;
  responsavel: string;
  prioridade: string;
  prazo?: string;
  status: string;
  checklist: TarefaPendenciaApiChecklist[];
}

@Injectable({ providedIn: 'root' })
export class TarefasPendenciasService {
  private readonly baseUrl = `${environment.apiUrl}/api/administrativo/tarefas`;

  constructor(private readonly http: HttpClient) {}

  list(): Observable<TaskRecord[]> {
    return this.http
      .get<TarefaPendenciaApiResponse[]>(this.baseUrl)
      .pipe(
        map((response) => response.map((item) => this.mapResponse(item))),
        catchError(this.logAndRethrow('ao carregar as tarefas'))
      );
  }

  create(payload: TaskPayload): Observable<TaskRecord> {
    return this.http
      .post<TarefaPendenciaApiResponse>(this.baseUrl, this.toApiPayload(payload))
      .pipe(
        map((response) => this.mapResponse(response)),
        catchError(this.logAndRethrow('ao criar a tarefa'))
      );
  }

  update(id: string, payload: TaskPayload): Observable<TaskRecord> {
    return this.http
      .put<TarefaPendenciaApiResponse>(`${this.baseUrl}/${id}`, this.toApiPayload(payload))
      .pipe(
        map((response) => this.mapResponse(response)),
        catchError(this.logAndRethrow('ao atualizar a tarefa'))
      );
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`).pipe(catchError(this.logAndRethrow('ao remover a tarefa')));
  }

  toggleChecklist(task: TaskRecord, item: ChecklistItem): Observable<TaskRecord> {
    const checklist = task.checklist.map((entry) =>
      entry.id === item.id
        ? {
            ...entry,
            concluido: !entry.concluido,
            concluidoEm: entry.concluido ? undefined : new Date().toISOString()
          }
        : entry
    );
    return this.update(task.id, {
      titulo: task.titulo,
      descricao: task.descricao,
      responsavel: task.responsavel,
      prioridade: task.prioridade,
      prazo: task.prazo,
      status: task.status,
      checklist
    });
  }

  updateStatus(task: TaskRecord, status: TaskRecord['status']): Observable<TaskRecord> {
    return this.update(task.id, {
      titulo: task.titulo,
      descricao: task.descricao,
      responsavel: task.responsavel,
      prioridade: task.prioridade,
      prazo: task.prazo,
      status,
      checklist: task.checklist
    });
  }

  private toApiPayload(payload: TaskPayload): TarefaPendenciaApiRequest {
    const checklist = (payload.checklist ?? []).map((item, index) => ({
      id: item.id ? Number(item.id) : undefined,
      titulo: item.titulo,
      concluido: item.concluido,
      concluidoEm: item.concluidoEm,
      ordem: item.ordem ?? index
    }));

    return {
      titulo: payload.titulo,
      descricao: payload.descricao,
      responsavel: payload.responsavel,
      prioridade: payload.prioridade,
      prazo: payload.prazo,
      status: payload.status,
      checklist
    };
  }

  private mapResponse(response: TarefaPendenciaApiResponse): TaskRecord {
    const prioridadeOptions: TaskPayload['prioridade'][] = ['Alta', 'Média', 'Baixa'];
    const prioridade =
      prioridadeOptions.includes(response.prioridade as TaskPayload['prioridade'])
        ? (response.prioridade as TaskPayload['prioridade'])
        : 'Média';

    return {
      id: String(response.id ?? ''),
      titulo: response.titulo,
      descricao: response.descricao,
      responsavel: response.responsavel,
      prioridade,
      prazo: response.prazo ?? '',
      status: response.status as TaskPayload['status'],
      checklist: (response.checklist ?? []).map((item) => ({
        id: String(item.id ?? ''),
        titulo: item.titulo,
        concluido: item.concluido,
        concluidoEm: item.concluidoEm ?? undefined,
        ordem: item.ordem
      })),
      historico: (response.historico ?? []).map((entry) => ({
        id: String(entry.id ?? ''),
        mensagem: entry.mensagem,
        data: entry.criadoEm
      })),
      criadoEm: response.criadoEm,
      atualizadoEm: response.atualizadoEm
    };
  }

  private logAndRethrow(operation: string) {
    return (error: unknown) => {
      console.error(`Erro ${operation}`, error);
      return throwError(() => error);
    };
  }
}
