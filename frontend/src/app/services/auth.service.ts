import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap, timeout } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Router } from '@angular/router';

interface LoginResponse {
  token: string;
  usuario?: {
    id: string;
    nomeUsuario: string;
    nome?: string;
    email?: string;
    permissoes?: string[];
  };
  user?: {
    id: string;
    nomeUsuario: string;
    nome?: string;
    email?: string;
    permissoes?: string[];
  };
}

interface CadastroContaRequest {
  nome: string;
  email: string;
  senha: string;
}

interface RecuperarSenhaRequest {
  email: string;
}

interface RedefinirSenhaRequest {
  token: string;
  senha: string;
  confirmarSenha: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly storageKey = 'g3_session';
  private readonly requestTimeoutMs = 10000;
  readonly user = signal<{
    id: string;
    nomeUsuario: string;
    nome?: string;
    email?: string;
    permissoes?: string[];
  } | null>(this.loadUser());

  constructor(private readonly http: HttpClient, private readonly router: Router) {}

  login(nomeUsuario: string, senha: string): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${environment.apiUrl}/api/auth/login`, { nomeUsuario, senha })
      .pipe(
        timeout(this.requestTimeoutMs),
        tap((response) => {
          const usuario =
            response.usuario ?? { id: '0', nomeUsuario };
          this.persistSession({
            ...response,
            user: usuario,
          });
        })
      );
  }

  loginGoogle(idToken: string): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${environment.apiUrl}/api/auth/google`, { idToken })
      .pipe(
        timeout(this.requestTimeoutMs),
        tap((response) => {
          const usuario =
            response.usuario ?? response.user ?? { id: '0', nomeUsuario: '' };
          this.persistSession({
            ...response,
            user: usuario,
          });
        })
      );
  }

  registrarConta(payload: CadastroContaRequest): Observable<void> {
    return this.http
      .post<void>(`${environment.apiUrl}/api/auth/registrar`, payload)
      .pipe(timeout(this.requestTimeoutMs));
  }

  solicitarRecuperacaoSenha(email: string): Observable<void> {
    const payload: RecuperarSenhaRequest = { email };
    return this.http
      .post<void>(`${environment.apiUrl}/api/auth/recuperar-senha`, payload)
      .pipe(timeout(this.requestTimeoutMs));
  }

  redefinirSenha(payload: RedefinirSenhaRequest): Observable<void> {
    return this.http
      .post<void>(`${environment.apiUrl}/api/auth/redefinir-senha`, payload)
      .pipe(timeout(this.requestTimeoutMs));
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

  private loadUser(): {
    id: string;
    nomeUsuario: string;
    nome?: string;
    email?: string;
    permissoes?: string[];
  } | null {
    return this.loadSession()?.user ?? null;
  }

  private persistSession(session: LoginResponse): void {
    this.user.set(session.user ?? null);
    localStorage.setItem(this.storageKey, JSON.stringify(session));
  }

}

