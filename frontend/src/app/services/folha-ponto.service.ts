import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface RhLocalPontoRequest {
  nome: string;
  endereco?: string;
  latitude: number;
  longitude: number;
  raioMetros: number;
  accuracyMaxMetros: number;
  ativo: boolean;
}

export interface RhLocalPontoResponse extends RhLocalPontoRequest {
  id: number;
  criadoEm?: string;
  atualizadoEm?: string;
}

export interface RhConfiguracaoPontoRequest {
  cargaSemanalMinutos?: number;
  cargaSegQuiMinutos?: number;
  cargaSextaMinutos?: number;
  cargaSabadoMinutos?: number;
  cargaDomingoMinutos?: number;
  toleranciaMinutos?: number;
}

export interface RhConfiguracaoPontoResponse extends RhConfiguracaoPontoRequest {
  id: number;
  atualizadoEm?: string;
}

export interface RhPontoBaterRequest {
  funcionarioId?: number;
  tipo: string;
  senha: string;
  latitude: number;
  longitude: number;
  accuracy: number;
}

export interface RhPontoMarcacaoResponse {
  id: number;
  tipo: string;
  dataHoraServidor: string;
  latitude: number;
  longitude: number;
  accuracy: number;
  distanciaMetros: number;
  dentroPerimetro: boolean;
}

export interface RhPontoDiaResponse {
  id: number;
  funcionarioId: number;
  data: string;
  ocorrencia: string;
  observacoes?: string;
  cargaPrevistaMinutos: number;
  toleranciaMinutos: number;
  totalTrabalhadoMinutos: number;
  extrasMinutos: number;
  faltasAtrasosMinutos: number;
  marcacoes: RhPontoMarcacaoResponse[];
}

export interface RhPontoDiaResumoResponse {
  data: string;
  ocorrencia: string;
  entradaManha: string;
  saidaManha: string;
  entradaTarde: string;
  saidaTarde: string;
  totalTrabalhadoMinutos: number;
  extrasMinutos: number;
  faltasAtrasosMinutos: number;
  observacoes?: string;
  pontoDiaId?: number;
}

export interface RhPontoEspelhoResponse {
  funcionarioId: number;
  mes: number;
  ano: number;
  totalTrabalhadoMinutos: number;
  totalDevidoMinutos: number;
  totalExtrasMinutos: number;
  totalFaltasAtrasosMinutos: number;
  diasTrabalhados: number;
  dias: RhPontoDiaResumoResponse[];
}

export interface UnidadeAssistencialResponse {
  id: number;
  nomeFantasia: string;
  razaoSocial?: string;
  cnpj?: string;
  email?: string;
  site?: string;
  telefone?: string;
  unidadePrincipal?: boolean;
  endereco?: string;
  numeroEndereco?: string;
  complemento?: string;
  bairro?: string;
  cidade?: string;
  estado?: string;
  cep?: string;
  latitude?: string;
  longitude?: string;
}

export interface UnidadeAssistencialConsultaResponse {
  unidade: UnidadeAssistencialResponse;
}

@Injectable({ providedIn: 'root' })
export class FolhaPontoService {
  private readonly baseUrl = `${environment.apiUrl}/api/rh/ponto`;
  private readonly unidadesUrl = `${environment.apiUrl}/api/unidades-assistenciais`;

  constructor(private readonly http: HttpClient) {}

  listarLocais(): Observable<RhLocalPontoResponse[]> {
    return this.http.get<RhLocalPontoResponse[]>(`${this.baseUrl}/locais`);
  }

  criarLocal(payload: RhLocalPontoRequest): Observable<RhLocalPontoResponse> {
    return this.http.post<RhLocalPontoResponse>(`${this.baseUrl}/locais`, payload);
  }

  atualizarLocal(id: number, payload: RhLocalPontoRequest): Observable<RhLocalPontoResponse> {
    return this.http.put<RhLocalPontoResponse>(`${this.baseUrl}/locais/${id}`, payload);
  }

  removerLocal(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/locais/${id}`);
  }

  buscarLocalAtivo(): Observable<RhLocalPontoResponse> {
    return this.http.get<RhLocalPontoResponse>(`${this.baseUrl}/locais/ativo`);
  }

  buscarConfiguracao(): Observable<RhConfiguracaoPontoResponse> {
    return this.http.get<RhConfiguracaoPontoResponse>(`${this.baseUrl}/configuracao`);
  }

  atualizarConfiguracao(usuarioId: number, payload: RhConfiguracaoPontoRequest): Observable<RhConfiguracaoPontoResponse> {
    return this.http.put<RhConfiguracaoPontoResponse>(`${this.baseUrl}/configuracao`, payload, {
      params: { usuarioId }
    });
  }

  baterPonto(usuarioId: number, payload: RhPontoBaterRequest): Observable<RhPontoDiaResponse> {
    return this.http.post<RhPontoDiaResponse>(`${this.baseUrl}/bater`, payload, {
      params: { usuarioId }
    });
  }

  consultarEspelho(mes: number, ano: number, funcionarioId: number): Observable<RhPontoEspelhoResponse> {
    return this.http.get<RhPontoEspelhoResponse>(`${this.baseUrl}/espelho`, {
      params: { mes, ano, funcionarioId }
    });
  }

  atualizarDia(id: number, usuarioId: number, payload: { ocorrencia?: string; observacoes?: string }): Observable<RhPontoDiaResponse> {
    return this.http.put<RhPontoDiaResponse>(`${this.baseUrl}/dia/${id}`, payload, {
      params: { usuarioId }
    });
  }

  exportarExcel(mes: number, ano: number, funcionarioId: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/relatorio/excel`, {
      params: { mes, ano, funcionarioId },
      responseType: 'blob'
    });
  }

  buscarUnidadeAtual(): Observable<UnidadeAssistencialConsultaResponse> {
    return this.http.get<UnidadeAssistencialConsultaResponse>(`${this.unidadesUrl}/atual`);
  }

  listarUnidades(): Observable<UnidadeAssistencialResponse[]> {
    return this.http.get<UnidadeAssistencialResponse[]>(this.unidadesUrl);
  }

  geocodificarEnderecoUnidade(
    id: number,
    forcar = false
  ): Observable<UnidadeAssistencialResponse> {
    const params = forcar ? new HttpParams().set('forcar', 'true') : undefined;
    return this.http.post<UnidadeAssistencialResponse>(
      `${this.unidadesUrl}/${id}/geocodificar-endereco`,
      {},
      { params, responseType: 'json' }
    );
  }
}
