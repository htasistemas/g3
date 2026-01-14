import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface GerenciamentoDadosConfiguracaoResponse {
  id: number;
  frequencia: string;
  horarioExecucao: string;
  horarioIncremental: string;
  retencaoDias: number;
  automacaoAtiva: boolean;
  limiteBanda: number;
  pausarHorarioComercial: boolean;
  provedor: string;
  caminhoDestino?: string;
  bucketNome?: string;
  criptografia: boolean;
  compressao: string;
  verificarIntegridade: boolean;
  emailNotificacao?: string;
  copiaExterna: boolean;
  alertas: boolean;
  deteccaoAnomalia: boolean;
  autoVerificacao: boolean;
  integracoes?: string;
  simulacaoRetencao: boolean;
  atualizadoEm?: string;
}

export interface GerenciamentoDadosConfiguracaoRequest {
  frequencia: string;
  horarioExecucao: string;
  horarioIncremental: string;
  retencaoDias: number;
  automacaoAtiva: boolean;
  limiteBanda: number;
  pausarHorarioComercial: boolean;
  provedor: string;
  caminhoDestino?: string;
  bucketNome?: string;
  criptografia: boolean;
  compressao: string;
  verificarIntegridade: boolean;
  emailNotificacao?: string;
  copiaExterna: boolean;
  alertas: boolean;
  deteccaoAnomalia: boolean;
  autoVerificacao: boolean;
  integracoes?: string;
  simulacaoRetencao: boolean;
}

export interface GerenciamentoDadosBackupResponse {
  id: number;
  codigo: string;
  rotulo: string;
  tipo: string;
  status: string;
  iniciadoEm: string;
  armazenadoEm?: string;
  tamanho?: string;
  criptografado: boolean;
  retencaoDias: number;
}

export interface GerenciamentoDadosBackupRequest {
  rotulo: string;
  tipo: string;
  status?: string;
  armazenadoEm?: string;
  tamanho?: string;
  criptografado: boolean;
  retencaoDias: number;
}

export interface GerenciamentoDadosRestauracaoResponse {
  backupId: number;
  status: string;
  mensagem: string;
  arquivo?: string;
}

@Injectable({ providedIn: 'root' })
export class GerenciamentoDadosService {
  private readonly baseUrl = `${environment.apiUrl}/api/config/gerenciamento-dados`;

  constructor(private readonly http: HttpClient) {}

  obterConfiguracao(): Observable<GerenciamentoDadosConfiguracaoResponse> {
    return this.http.get<GerenciamentoDadosConfiguracaoResponse>(`${this.baseUrl}/configuracao`);
  }

  atualizarConfiguracao(
    payload: GerenciamentoDadosConfiguracaoRequest
  ): Observable<GerenciamentoDadosConfiguracaoResponse> {
    return this.http.put<GerenciamentoDadosConfiguracaoResponse>(
      `${this.baseUrl}/configuracao`,
      payload
    );
  }

  listarBackups(): Observable<GerenciamentoDadosBackupResponse[]> {
    return this.http.get<GerenciamentoDadosBackupResponse[]>(`${this.baseUrl}/backups`);
  }

  criarBackup(payload: GerenciamentoDadosBackupRequest): Observable<GerenciamentoDadosBackupResponse> {
    return this.http.post<GerenciamentoDadosBackupResponse>(`${this.baseUrl}/backups`, payload);
  }

  restaurarBackup(backupId: number): Observable<GerenciamentoDadosRestauracaoResponse> {
    return this.http.post<GerenciamentoDadosRestauracaoResponse>(
      `${this.baseUrl}/backups/${backupId}/restaurar`,
      {}
    );
  }
}
