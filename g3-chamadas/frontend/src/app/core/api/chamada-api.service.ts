import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { FilaModel } from '../shared/modelos/fila.model';
import { ChamadaModel } from '../shared/modelos/chamada.model';

@Injectable({ providedIn: 'root' })
export class ChamadaApiService {
  private readonly baseUrl = `${environment.apiUrl}/api`;

  constructor(private readonly http: HttpClient) {}

  listarFilaAguardando(): Observable<FilaModel[]> {
    return this.http.get<FilaModel[]>(`${this.baseUrl}/fila/aguardando`);
  }

  entrarFila(idBeneficiario: number, prioridade?: number): Observable<FilaModel> {
    return this.http.post<FilaModel>(`${this.baseUrl}/fila/entrar`, {
      idBeneficiario,
      prioridade
    });
  }

  chamar(idFilaAtendimento: number, localAtendimento: string): Observable<ChamadaModel> {
    return this.http.post<ChamadaModel>(`${this.baseUrl}/chamadas/chamar`, {
      idFilaAtendimento,
      localAtendimento
    });
  }

  ultimaChamada(): Observable<ChamadaModel> {
    return this.http.get<ChamadaModel>(`${this.baseUrl}/chamadas/ultima`);
  }

  ultimasChamadas(limite = 10): Observable<ChamadaModel[]> {
    return this.http.get<ChamadaModel[]>(`${this.baseUrl}/chamadas/ultimas`, {
      params: { limite }
    });
  }
}
