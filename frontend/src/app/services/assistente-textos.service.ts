import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AssistenteTextoPayload {
  beneficiario?: {
    nomeCompleto?: string;
    dataNascimento?: string;
    idadeAproximada?: number;
    situacaoBasica?: string;
  };
  tipo?: 'parecer' | 'relatorio' | 'plano_atendimento' | string;
}

@Injectable({ providedIn: 'root' })
export class AssistenteTextosService {
  private readonly baseUrl = `${environment.apiUrl}/api/assistente-textos`;

  constructor(private readonly http: HttpClient) {}

  gerarTexto(payload: AssistenteTextoPayload): Observable<{ texto: string }> {
    return this.http.post<{ texto: string }>(`${this.baseUrl}/gerar`, payload);
  }
}
