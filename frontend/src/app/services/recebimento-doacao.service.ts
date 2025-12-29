import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { environment } from '../../environments/environment';

export interface DoadorRequest {
  nome: string;
  tipoPessoa?: string;
  documento?: string;
  email?: string;
  telefone?: string;
  observacoes?: string;
}

export interface DoadorResponse extends DoadorRequest {
  id: number;
}

export interface RecebimentoDoacaoRequest {
  doadorId?: number;
  tipoDoacao: string;
  descricao?: string;
  valor?: number;
  dataRecebimento: string;
  formaRecebimento?: string;
  recorrente: boolean;
  periodicidade?: string;
  proximaCobranca?: string;
  status: string;
  observacoes?: string;
}

export interface RecebimentoDoacaoResponse extends RecebimentoDoacaoRequest {
  id: number;
  doadorNome?: string;
  contabilidadePendente: boolean;
}

@Injectable({ providedIn: 'root' })
export class RecebimentoDoacaoService {
  private readonly baseUrl = `${environment.apiUrl}/api/recebimentos-doacao`;

  constructor(private readonly http: HttpClient) {}

  listarDoadores(): Observable<DoadorResponse[]> {
    return this.http.get<DoadorResponse[]>(`${this.baseUrl}/doadores`);
  }

  criarDoador(payload: DoadorRequest): Observable<DoadorResponse> {
    return this.http.post<DoadorResponse>(`${this.baseUrl}/doadores`, payload);
  }

  listarRecebimentos(): Observable<RecebimentoDoacaoResponse[]> {
    return this.http
      .get<{ recebimentos: RecebimentoDoacaoResponse[] }>(this.baseUrl)
      .pipe(map((response) => response.recebimentos || []));
  }

  criarRecebimento(payload: RecebimentoDoacaoRequest): Observable<RecebimentoDoacaoResponse> {
    return this.http.post<RecebimentoDoacaoResponse>(this.baseUrl, payload);
  }
}
