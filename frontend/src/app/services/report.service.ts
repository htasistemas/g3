import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AuthorizationTermPayload {
  beneficiarioNome: string;
  rg?: string | null;
  cpf?: string | null;
  enderecoCompleto?: string | null;
  cidade?: string | null;
  uf?: string | null;
  finalidadeDados?: string | null;
  finalidadeImagem?: string | null;
  vigencia?: string | null;
  localAssinatura?: string | null;
  dataAssinatura?: string | null;
  responsavelNome?: string | null;
  responsavelCpf?: string | null;
  responsavelRelacao?: string | null;
  representanteNome?: string | null;
  representanteCargo?: string | null;
  issuedBy?: string | null;
}

export interface BeneficiaryReportFilters {
  nome?: string;
  cpf?: string;
  codigo?: string;
  status?: string;
  dataNascimento?: string;
  ordenarPor?: string;
  ordem?: string;
  usuarioEmissor?: string;
}

export interface CursosAtendimentosRelacaoFilters {
  nome?: string;
  tipo?: string;
  status?: string;
  profissional?: string;
  salaId?: string;
  usuarioEmissor?: string;
}

export interface SolicitacaoComprasPayload {
  solicitacaoId: string;
  usuarioEmissor?: string;
}

export interface EmprestimoEventoRelatorioPayload {
  emprestimoId: string;
  usuarioEmissor?: string;
}

@Injectable({ providedIn: 'root' })
export class ReportService {
  private readonly baseUrl = `${environment.apiUrl}/api/reports`;

  constructor(private readonly http: HttpClient) {}

  generateAuthorizationTerm(payload: AuthorizationTermPayload): Observable<HttpResponse<Blob>> {
    return this.http.post(`${this.baseUrl}/authorization-term`, payload, {
      responseType: 'blob',
      observe: 'response'
    });
  }

  generateBeneficiaryList(filters: BeneficiaryReportFilters): Observable<Blob> {
    return this.http.post(`${this.baseUrl}/beneficiarios/relacao`, filters, { responseType: 'blob' });
  }

  generateBeneficiaryProfile(payload: { beneficiarioId: string; usuarioEmissor?: string }): Observable<Blob> {
    return this.http.post(
      `${this.baseUrl}/beneficiarios/ficha`,
      payload,
      { responseType: 'blob' }
    );
  }

  generateCursosAtendimentosList(filters: CursosAtendimentosRelacaoFilters): Observable<Blob> {
    return this.http.post(`${this.baseUrl}/cursos-atendimentos/relacao`, filters, {
      responseType: 'blob'
    });
  }

  generateCursoAtendimentoFicha(payload: {
    cursoId: string;
    usuarioEmissor?: string;
  }): Observable<Blob> {
    return this.http.post(`${this.baseUrl}/cursos-atendimentos/ficha`, payload, {
      responseType: 'blob'
    });
  }

  generateSolicitacaoCompras(payload: SolicitacaoComprasPayload): Observable<Blob> {
    return this.http.post(`${this.baseUrl}/autorizacao-compras/solicitacao`, payload, {
      responseType: 'blob'
    });
  }

  generateEmprestimoEventoRelatorio(
    dados: EmprestimoEventoRelatorioPayload
  ): Observable<Blob> {
    return this.http.post(`${this.baseUrl}/emprestimos-eventos/relatorio`, dados, {
      responseType: 'blob'
    });
  }

  generateEmprestimoEventoTermo(
    dados: EmprestimoEventoRelatorioPayload
  ): Observable<Blob> {
    return this.http.post(`${this.baseUrl}/emprestimos-eventos/termo`, dados, {
      responseType: 'blob'
    });
  }
}

