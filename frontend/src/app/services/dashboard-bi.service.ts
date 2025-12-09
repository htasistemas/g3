import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

export interface ChartResponse {
  labels: string[];
  values: number[];
}

export interface CursosMetricasResponse {
  inscritos: number;
  concluintes: number;
  evasao: number;
  ativos: number;
}

@Injectable({ providedIn: 'root' })
export class DashboardBiService {
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  constructor(private readonly http: HttpClient) {}

  getBeneficiariosStatus() {
    return this.http.get<ChartResponse>(`${environment.apiUrl}/api/dashboard/bi/beneficiarios-status`);
  }

  getAtendimentosMes() {
    return this.http.get<ChartResponse>(`${environment.apiUrl}/api/dashboard/bi/atendimentos-mes`);
  }

  getAtendimentosTipo() {
    return this.http.get<ChartResponse>(`${environment.apiUrl}/api/dashboard/bi/atendimentos-tipo`);
  }

  getCursosMetricas() {
    return this.http.get<CursosMetricasResponse>(`${environment.apiUrl}/api/dashboard/bi/cursos-metricas`);
  }

  getIvfFaixas() {
    return this.http.get<ChartResponse>(`${environment.apiUrl}/api/dashboard/bi/ivf-faixas`);
  }
}
