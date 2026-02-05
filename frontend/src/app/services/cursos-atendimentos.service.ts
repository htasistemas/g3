import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { SalaRecord } from './salas.service';

export type CourseType = 'Curso' | 'Atendimento' | 'Oficina';
export type EnrollmentStatus = 'Ativo' | 'ConcluÃ­do' | 'Cancelado';

export interface Enrollment {
  id: string;
  beneficiaryName: string;
  cpf: string;
  status: EnrollmentStatus;
  enrolledAt: string;
}

export type PresencaStatus = 'PRESENTE' | 'AUSENTE';

export interface PresencaItem {
  matriculaId: string;
  status: PresencaStatus;
}

export interface PresencaResponse {
  dataAula: string;
  presencas: PresencaItem[];
}

export interface WaitlistEntry {
  id: string;
  beneficiaryName: string;
  cpf: string;
  joinedAt: string;
}

export interface CourseRecord {
  id: string;
  tipo: CourseType;
  nome: string;
  descricao: string;
  imagem?: string | null;
  vagasTotais: number;
  vagasDisponiveis: number;
  cargaHoraria?: number | null;
  horarioInicial: string;
  duracaoHoras: number;
  diasSemana: string[];
  faixasEtarias?: string[];
  restricoes?: string | null;
  vagaPreferencialIdosos?: boolean;
  sexoPermitido?: string;
  profissional: string;
  salaId?: string | null;
  sala?: SalaRecord | null;
  createdAt: string;
  updatedAt?: string;
  status: 'TRIAGEM' | 'EM_ANDAMENTO' | 'ENCAMINHADO' | 'EM_VISITA' | 'CONCLUIDO';
  statusHistory?: { status: CourseRecord['status']; changedAt: string; justification?: string }[];
  dataTriagem?: string | null;
  dataEncaminhamento?: string | null;
  dataConclusao?: string | null;
  enrollments: Enrollment[];
  waitlist: WaitlistEntry[];
}

export interface CoursePayload
  extends Omit<CourseRecord, 'id' | 'createdAt' | 'updatedAt' | 'vagasDisponiveis' | 'sala'> {
  vagasDisponiveis?: number;
}

@Injectable({ providedIn: 'root' })
export class CursosAtendimentosService {
  private readonly baseUrl = `${environment.apiUrl}/api/cursos-atendimentos`;

  constructor(private readonly http: HttpClient) {}

  list(): Observable<CourseRecord[]> {
    return this.http
      .get<{ records: CourseRecord[] }>(this.baseUrl)
      .pipe(map((response) => response.records ?? []));
  }

  create(payload: CoursePayload): Observable<CourseRecord> {
    return this.http
      .post<{ record: CourseRecord }>(this.baseUrl, payload)
      .pipe(map((response) => response.record));
  }

  update(id: string, payload: Partial<CoursePayload>): Observable<CourseRecord> {
    return this.http
      .put<{ record: CourseRecord }>(`${this.baseUrl}/${id}`, payload)
      .pipe(map((response) => response.record));
  }

  updateStatus(
    id: string,
    payload: { status: CourseRecord['status']; justification?: string },
  ): Observable<CourseRecord> {
    return this.http
      .patch<{ record: CourseRecord }>(`${this.baseUrl}/${id}/status`, payload)
      .pipe(map((response) => response.record));
  }

  listarPresencas(cursoId: string, dataAula: string): Observable<PresencaResponse> {
    return this.http.get<PresencaResponse>(`${this.baseUrl}/${cursoId}/presencas`, {
      params: { data: dataAula },
    });
  }

  salvarPresencas(cursoId: string, payload: PresencaResponse): Observable<PresencaResponse> {
    return this.http.post<PresencaResponse>(`${this.baseUrl}/${cursoId}/presencas`, payload);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}

