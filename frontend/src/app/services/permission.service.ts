import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface PermissionPayload {
  id: number;
  nome: string;
}

@Injectable({ providedIn: 'root' })
export class PermissionService {
  private readonly baseUrl = `${environment.apiUrl}/api/usuarios/permissoes`;

  constructor(private readonly http: HttpClient) {}

  list(): Observable<PermissionPayload[]> {
    return this.http.get<PermissionPayload[]>(this.baseUrl);
  }
}
