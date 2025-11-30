import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { BeneficiaryPayload } from './beneficiary.service';

export interface FamilyMemberPayload {
  id?: number;
  beneficiarioId: number;
  parentesco: string;
  ehResponsavelFamiliar: boolean;
  dataEntradaFamilia?: string;
  observacoes?: string;
  beneficiario?: BeneficiaryPayload;
}

export interface FamilyPayload {
  id?: number;
  responsavelFamiliarId?: number | null;
  logradouro: string;
  numero: string;
  bairro: string;
  cidade: string;
  uf: string;
  cep: string;
  complemento?: string;
  membros: FamilyMemberPayload[];
}

@Injectable({ providedIn: 'root' })
export class FamilyService {
  private readonly baseUrl = `${environment.apiUrl}/api/familias`;

  constructor(private readonly http: HttpClient) {}

  getById(id: number): Observable<{ familia: FamilyPayload }> {
    return this.http.get<{ familia: FamilyPayload }>(`${this.baseUrl}/${id}`);
  }

  save(payload: FamilyPayload): Observable<FamilyPayload> {
    if (payload.id) {
      return this.http.put<FamilyPayload>(`${this.baseUrl}/${payload.id}`, payload);
    }

    return this.http.post<FamilyPayload>(this.baseUrl, payload);
  }
}
