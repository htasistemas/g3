import { HttpClient } from '@angular/common/http';
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

@Injectable({ providedIn: 'root' })
export class ReportService {
  private readonly baseUrl = `${environment.apiUrl}/api/reports`;

  constructor(private readonly http: HttpClient) {}

  generateAuthorizationTerm(payload: AuthorizationTermPayload): Observable<Blob> {
    return this.http.post(`${this.baseUrl}/authorization-term`, payload, { responseType: 'blob' });
  }
}
