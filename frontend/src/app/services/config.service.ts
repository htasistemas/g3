import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

export interface BeneficiaryDocumentConfig {
  id?: number;
  name: string;
  required: boolean;
}

@Injectable({ providedIn: 'root' })
export class ConfigService {
  private readonly baseUrl = `${environment.apiUrl}/api/config`;

  constructor(private readonly http: HttpClient) {}

  getBeneficiaryDocuments(): Observable<{ documents: BeneficiaryDocumentConfig[] }> {
    return this.http.get<{ documents: BeneficiaryDocumentConfig[] }>(`${this.baseUrl}/beneficiary-documents`);
  }

  updateBeneficiaryDocuments(documents: BeneficiaryDocumentConfig[]): Observable<{ documents: BeneficiaryDocumentConfig[] }> {
    return this.http.put<{ documents: BeneficiaryDocumentConfig[] }>(`${this.baseUrl}/beneficiary-documents`, { documents });
  }
}
