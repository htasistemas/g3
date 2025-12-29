import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export type JobStatus = 'Aberta' | 'Pausada' | 'Encerrada';

export interface JobEncaminhamento {
  id: string;
  beneficiarioId: string;
  beneficiarioNome: string;
  data: string;
  status: string;
  observacoes?: string;
}

export interface JobPayload {
  dadosVaga: {
    titulo: string;
    area: string;
    tipo: string;
    nivel: string;
    modelo: string;
    status: JobStatus;
    salario?: string;
    beneficios?: string;
  };
  empresaLocal?: {
    nomeEmpresa: string;
    responsavel?: string;
    telefone?: string;
    email?: string;
    endereco?: string;
    bairro?: string;
    cidade?: string;
    uf?: string;
  };
  requisitos?: {
    escolaridade?: string;
    experiencia?: string;
    habilidades?: string;
    descricao?: string;
  };
  encaminhamentos?: JobEncaminhamento[];
}

export interface JobRecord extends JobPayload {
  id: string;
  criadoEm: string;
  atualizadoEm?: string;
}

@Injectable({ providedIn: 'root' })
export class BancoEmpregosService {
  private readonly baseUrl = `${environment.apiUrl}/api/banco-empregos`;

  constructor(private readonly http: HttpClient) {}

  list(): Observable<JobRecord[]> {
    return this.http.get<JobRecord[]>(this.baseUrl);
  }

  getById(id: string): Observable<JobRecord> {
    return this.http.get<JobRecord>(`${this.baseUrl}/${id}`);
  }

  create(payload: JobPayload): Observable<JobRecord> {
    return this.http.post<JobRecord>(this.baseUrl, payload);
  }

  update(id: string, payload: JobPayload): Observable<JobRecord> {
    return this.http.put<JobRecord>(`${this.baseUrl}/${id}`, payload);
  }

  remove(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
