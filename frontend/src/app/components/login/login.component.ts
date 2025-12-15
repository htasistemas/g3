import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { EMPTY } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  nomeUsuario = 'admin';
  senha = '123';
  loading = false;
  error: string | null = null;

  constructor(
    private readonly auth: AuthService,
    private readonly router: Router
  ) {}

  submit(): void {
    this.error = null;
    this.loading = true;

    try {
      this.auth
        .login(this.nomeUsuario, this.senha)
        .pipe(
          catchError((err) => {
            this.error = this.mapError(err);
            return EMPTY;
          }),
          finalize(() => {
            this.loading = false;
          })
        )
        .subscribe(() => {
          this.router.navigate(['/']);
        });
    } catch (err) {
      this.error = this.mapError(err);
      this.loading = false;
    }
  }

  private mapError(err: unknown): string {
    if (err && typeof err === 'object' && 'name' in err && err['name'] === 'TimeoutError') {
      return 'O servidor não respondeu. Verifique sua conexão ou tente novamente em instantes.';
    }

    const fallback =
      err && typeof err === 'object' && 'error' in err && (err as any).error?.message
        ? (err as any).error.message
        : null;

    if (fallback) {
      return fallback;
    }

    if (err instanceof Error) {
      return err.message;
    }

    return 'Falha ao fazer login. Tente novamente.';
  }
}
