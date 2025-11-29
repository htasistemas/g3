import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

export interface AssistanceUnitPayload {
  id?: number;
  name: string;
}

@Injectable({ providedIn: 'root' })
export class AssistanceUnitService {
  private readonly baseUrl = `${environment.apiUrl}/api/assistance-units`;
  private readonly activeUnitKey = 'g3-active-assistance-unit';

  private readonly currentUnitNameSubject = new BehaviorSubject<string>(
    localStorage.getItem(this.activeUnitKey) || 'Navegação'
  );

  readonly currentUnitName$ = this.currentUnitNameSubject.asObservable();

  constructor(private readonly http: HttpClient) {}

  list(): Observable<{ units: AssistanceUnitPayload[] }> {
    return this.http.get<{ units: AssistanceUnitPayload[] }>(this.baseUrl);
  }

  save(payload: AssistanceUnitPayload): Observable<AssistanceUnitPayload> {
    return this.http.post<AssistanceUnitPayload>(this.baseUrl, payload);
  }

  setActiveUnit(name: string): void {
    this.currentUnitNameSubject.next(name);
    localStorage.setItem(this.activeUnitKey, name);
  }
}
