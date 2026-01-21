import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { BeneficiarioModel } from '../shared/modelos/beneficiario.model';

interface BeneficiarioResumoListaResponse {
  beneficiarios: BeneficiarioModel[];
}

@Injectable({ providedIn: 'root' })
export class BeneficiarioApiService {
  private readonly baseUrl = `${environment.g3ApiUrl}/api/beneficiarios/resumo`;

  constructor(private readonly http: HttpClient) {}

  listar(): Observable<BeneficiarioModel[]> {
    return this.http
      .get<BeneficiarioResumoListaResponse>(this.baseUrl)
      .pipe(map((response) => response.beneficiarios ?? []));
  }
}
