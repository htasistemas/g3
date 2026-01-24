import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface VeiculoCadastro {
  id: number;
  placa: string;
  modelo: string;
  marca: string;
  ano: number;
  tipoCombustivel: string;
  mediaConsumoPadrao: number | null;
  capacidadeTanqueLitros: number | null;
  observacoes?: string;
  ativo: boolean;
}

export interface RegistroDiarioBordo {
  id: number;
  veiculoId: number;
  data: string;
  condutor: string;
  horarioSaida: string;
  kmInicial: number;
  horarioChegada: string;
  kmFinal: number;
  destino: string;
  combustivelConsumidoLitros: number;
  kmRodados: number;
  mediaConsumo: number;
  observacoes?: string;
}

export interface VeiculoCadastroEntrada {
  placa: string;
  modelo: string;
  marca: string;
  ano: number;
  tipoCombustivel: string;
  mediaConsumoPadrao: number | null;
  capacidadeTanqueLitros: number | null;
  observacoes?: string;
  ativo: boolean;
}

export interface RegistroDiarioBordoEntrada {
  veiculoId: number;
  data: string;
  condutor: string;
  horarioSaida: string;
  kmInicial: number;
  horarioChegada: string;
  kmFinal: number;
  destino: string;
  observacoes?: string;
}

@Injectable({ providedIn: 'root' })
export class ControleVeiculosService {
  private readonly urlApiBase = environment.apiUrl.replace(/\/api\/?$/, '');
  private readonly urlBase = `${this.urlApiBase}/api/controle-veiculos`;

  constructor(private readonly http: HttpClient) {}

  listarVeiculos(): Observable<VeiculoCadastro[]> {
    return this.http.get<VeiculoCadastro[]>(`${this.urlBase}/veiculos`);
  }

  listarRegistros(): Observable<RegistroDiarioBordo[]> {
    return this.http.get<RegistroDiarioBordo[]>(`${this.urlBase}/diario-bordo`);
  }

  criarVeiculo(dados: VeiculoCadastroEntrada): Observable<VeiculoCadastro> {
    return this.http.post<VeiculoCadastro>(`${this.urlBase}/veiculos`, dados);
  }

  atualizarVeiculo(id: number, dados: VeiculoCadastroEntrada): Observable<VeiculoCadastro> {
    return this.http.put<VeiculoCadastro>(`${this.urlBase}/veiculos/${id}`, dados);
  }

  removerVeiculo(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlBase}/veiculos/${id}`);
  }

  criarRegistro(dados: RegistroDiarioBordoEntrada): Observable<RegistroDiarioBordo> {
    return this.http.post<RegistroDiarioBordo>(`${this.urlBase}/diario-bordo`, dados);
  }

  atualizarRegistro(id: number, dados: RegistroDiarioBordoEntrada): Observable<RegistroDiarioBordo> {
    return this.http.put<RegistroDiarioBordo>(`${this.urlBase}/diario-bordo/${id}`, dados);
  }

  removerRegistro(id: number): Observable<void> {
    return this.http.delete<void>(`${this.urlBase}/diario-bordo/${id}`);
  }
}
