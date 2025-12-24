import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AssistanceUnitPayload } from './assistance-unit.service';
import { environment } from '../../environments/environment';

export interface AuthorizationTermPayload {
  beneficiaryName: string;
  birthDate?: string | null;
  motherName?: string | null;
  cpf?: string | null;
  rg?: string | null;
  nis?: string | null;
  address?: string | null;
  contact?: string | null;
  issueDate: string;
  unit?: AssistanceUnitPayload | null;
}

export interface BeneficiaryReportFilters {
  nome?: string;
  cpf?: string;
  codigo?: string;
  status?: string;
  dataNascimento?: string;
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

  generateBeneficiaryProfile(beneficiarioId: string): Observable<Blob> {
    return this.http.post(
      `${this.baseUrl}/beneficiarios/ficha`,
      { beneficiarioId },
      { responseType: 'blob' }
    );
  }
}
