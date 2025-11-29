import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

export interface DocumentRequirement {
  name: string;
  fileName?: string;
  file?: File;
  required?: boolean;
}

export interface BeneficiaryPayload {
  id?: number;
  zipCode: string;
  fullName: string;
  motherName?: string;
  document: string;
  birthDate: string;
  age?: number;
  phone: string;
  email: string;
  address: string;
  addressNumber?: string;
  referencePoint?: string;
  neighborhood?: string;
  city?: string;
  state?: string;
  notes?: string;
  status: string;
  documents: DocumentRequirement[];
}

@Injectable({ providedIn: 'root' })
export class BeneficiaryService {
  private readonly baseUrl = `${environment.apiUrl}/api/beneficiaries`;

  constructor(private readonly http: HttpClient) {}

  getRequiredDocuments(): Observable<{ documents: DocumentRequirement[] }> {
    return this.http.get<{ documents: DocumentRequirement[] }>(`${this.baseUrl}/documents`);
  }

  list(): Observable<{ beneficiaries: BeneficiaryPayload[] }> {
    return this.http.get<{ beneficiaries: BeneficiaryPayload[] }>(this.baseUrl);
  }

  save(payload: BeneficiaryPayload): Observable<BeneficiaryPayload> {
    if (payload.id) {
      return this.http.put<BeneficiaryPayload>(`${this.baseUrl}/${payload.id}`, payload);
    }

    return this.http.post<BeneficiaryPayload>(this.baseUrl, payload);
  }
}
