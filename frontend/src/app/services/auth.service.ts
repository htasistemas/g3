import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap, timeout } from 'rxjs/operators';
import { Observable } from 'rxjs';
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
          const authenticatedUser = response.user ?? { id: '0', nomeUsuario };
          const session: LoginResponse = { ...response, user: authenticatedUser };

          this.user.set(authenticatedUser);
          localStorage.setItem(this.storageKey, JSON.stringify(session));
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
}
