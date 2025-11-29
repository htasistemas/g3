import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Router } from '@angular/router';

interface LoginResponse {
  token: string;
  user: { id: string; username: string };
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly storageKey = 'g3_session';
  readonly user = signal<{ id: string; username: string } | null>(this.loadUser());

  constructor(private readonly http: HttpClient, private readonly router: Router) {}

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${environment.apiUrl}/api/auth/login`, { username, password })
      .pipe(
        tap((response) => {
          this.user.set(response.user);
          localStorage.setItem(this.storageKey, JSON.stringify(response));
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
    return !!this.user();
  }

  get token(): string | null {
    const session = this.loadSession();
    return session?.token ?? null;
  }

  private loadSession(): LoginResponse | null {
    const raw = localStorage.getItem(this.storageKey);
    return raw ? (JSON.parse(raw) as LoginResponse) : null;
  }

  private loadUser(): { id: string; username: string } | null {
    return this.loadSession()?.user ?? null;
  }
}
