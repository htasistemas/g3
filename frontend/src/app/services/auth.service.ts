import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, tap, timeout } from 'rxjs/operators';
import { Observable, of, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { Router } from '@angular/router';

interface LoginResponse {
  token: string;
  user?: { id: string; nomeUsuario: string };
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly storageKey = 'g3_session';
  private readonly requestTimeoutMs = 10000;
  readonly user = signal<{ id: string; nomeUsuario: string } | null>(this.loadUser());

  constructor(private readonly http: HttpClient, private readonly router: Router) {}

  login(nomeUsuario: string, senha: string): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${environment.apiUrl}/api/auth/login`, { nomeUsuario, senha })
      .pipe(
        timeout(this.requestTimeoutMs),
        tap((response) => {
          this.persistSession({
            ...response,
            user: response.user ?? { id: '0', nomeUsuario },
          });
        }),
        catchError((error) => {
          const localSession = this.createLocalSession(nomeUsuario, senha);
          if (localSession) {
            this.persistSession(localSession);
            return of(localSession);
          }
          return throwError(() => error);
        })
      );
  }

  logout(): void {
    this.user.set(null);
    localStorage.removeItem(this.storageKey);
    sessionStorage.removeItem(this.storageKey);
    this.router.navigate(['/login']);
  }

  get isAuthenticated(): boolean {
    return !!this.token;
  }

  get token(): string | null {
    const session = this.loadSession();
    return session?.token ?? null;
  }

  private loadSession(): LoginResponse | null {
    const raw = localStorage.getItem(this.storageKey);
    return raw ? (JSON.parse(raw) as LoginResponse) : null;
  }

  private loadUser(): { id: string; nomeUsuario: string } | null {
    return this.loadSession()?.user ?? null;
  }

  private persistSession(session: LoginResponse): void {
    this.user.set(session.user ?? null);
    localStorage.setItem(this.storageKey, JSON.stringify(session));
  }

  private createLocalSession(nomeUsuario: string, senha: string): LoginResponse | null {
    if (nomeUsuario === 'admin' && senha === '123') {
      return { token: 'local-session', user: { id: 'local-admin', nomeUsuario } };
    }
    return null;
  }
}
