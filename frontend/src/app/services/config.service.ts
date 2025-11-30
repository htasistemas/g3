import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

export interface ConfiguracaoDocumentoBeneficiario {
  id?: number;
  nome: string;
  obrigatorio: boolean;
}

@Injectable({ providedIn: 'root' })
export class ConfigService {
  private readonly baseUrl = `${environment.apiUrl}/api/config`;

  constructor(private readonly http: HttpClient) {}

  obterDocumentosBeneficiario(): Observable<{ documentos: ConfiguracaoDocumentoBeneficiario[] }> {
    return this.http.get<{ documentos: ConfiguracaoDocumentoBeneficiario[] }>(`${this.baseUrl}/documentos-beneficiario`);
  }

  atualizarDocumentosBeneficiario(
    documentos: ConfiguracaoDocumentoBeneficiario[]
  ): Observable<{ documentos: ConfiguracaoDocumentoBeneficiario[] }> {
    return this.http.put<{ documentos: ConfiguracaoDocumentoBeneficiario[] }>(`${this.baseUrl}/documentos-beneficiario`, {
      documentos
    });
  }
}
