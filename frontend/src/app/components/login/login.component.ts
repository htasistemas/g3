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
  success: string | null = null;

  criarContaAberto = false;
  recuperarSenhaAberto = false;
  cadastroSubmetido = false;
  recuperacaoSubmetida = false;
  redefinicaoSubmetida = false;
  recuperacaoEtapa: 'solicitar' | 'redefinir' = 'solicitar';

  cadastroNome = '';
  cadastroEmail = '';
  cadastroSenha = '';
  cadastroConfirmarSenha = '';
  cadastroErro: string | null = null;

  recuperacaoEmail = '';
  recuperacaoToken = '';
  recuperacaoSenha = '';
  recuperacaoConfirmarSenha = '';
  recuperacaoErro: string | null = null;
  recuperacaoMensagem: string | null = null;

  constructor(
    private readonly auth: AuthService,
    private readonly router: Router
  ) {}

  submit(): void {
    this.error = null;
    this.success = null;
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

  abrirCriarConta(): void {
    this.resetCadastro();
    this.criarContaAberto = true;
  }

  fecharCriarConta(): void {
    this.criarContaAberto = false;
  }

  abrirRecuperarSenha(): void {
    this.resetRecuperacao();
    this.recuperarSenhaAberto = true;
  }

  fecharRecuperarSenha(): void {
    this.recuperarSenhaAberto = false;
  }

  confirmarCadastro(): void {
    this.cadastroSubmetido = true;
    this.cadastroErro = null;
    this.success = null;

    if (!this.cadastroValido()) {
      return;
    }

    this.auth
      .registrarConta({
        nome: this.cadastroNome.trim(),
        email: this.cadastroEmail.trim(),
        senha: this.cadastroSenha
      })
      .pipe(
        catchError((err) => {
          this.cadastroErro = this.mapError(err);
          return EMPTY;
        })
      )
      .subscribe(() => {
        this.success = 'Conta criada com sucesso. Faca o login para continuar.';
        this.fecharCriarConta();
      });
  }

  solicitarRecuperacao(): void {
    this.recuperacaoSubmetida = true;
    this.recuperacaoErro = null;
    this.recuperacaoMensagem = null;

    if (!this.recuperacaoEmailValido()) {
      return;
    }

    this.auth
      .solicitarRecuperacaoSenha(this.recuperacaoEmail.trim())
      .pipe(
        catchError((err) => {
          this.recuperacaoErro = this.mapError(err);
          return EMPTY;
        })
      )
      .subscribe(() => {
        this.recuperacaoMensagem =
          'Se o e-mail estiver cadastrado, voce recebera as instrucoes para recuperar a senha.';
        this.recuperacaoEtapa = 'redefinir';
      });
  }

  redefinirSenha(): void {
    this.redefinicaoSubmetida = true;
    this.recuperacaoErro = null;
    this.recuperacaoMensagem = null;

    if (!this.redefinicaoValida()) {
      return;
    }

    this.auth
      .redefinirSenha({
        token: this.recuperacaoToken.trim(),
        senha: this.recuperacaoSenha,
        confirmarSenha: this.recuperacaoConfirmarSenha
      })
      .pipe(
        catchError((err) => {
          this.recuperacaoErro = this.mapError(err);
          return EMPTY;
        })
      )
      .subscribe(() => {
        this.success = 'Senha atualizada com sucesso. Faca o login para continuar.';
        this.fecharRecuperarSenha();
      });
  }

  private cadastroValido(): boolean {
    if (!this.cadastroNome.trim() || !this.cadastroEmail.trim() || !this.cadastroSenha) {
      return false;
    }

    if (!this.emailValido(this.cadastroEmail)) {
      return false;
    }

    if (this.cadastroSenha.length < 6) {
      return false;
    }

    if (this.cadastroSenha !== this.cadastroConfirmarSenha) {
      return false;
    }

    return true;
  }

  private recuperacaoEmailValido(): boolean {
    return !!this.recuperacaoEmail.trim() && this.emailValido(this.recuperacaoEmail);
  }

  private redefinicaoValida(): boolean {
    if (!this.recuperacaoToken.trim() || !this.recuperacaoSenha) {
      return false;
    }

    if (this.recuperacaoSenha.length < 6) {
      return false;
    }

    if (this.recuperacaoSenha !== this.recuperacaoConfirmarSenha) {
      return false;
    }

    return true;
  }

  emailValido(email: string): boolean {
    return /^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$/.test(email.trim());
  }

  private resetCadastro(): void {
    this.cadastroSubmetido = false;
    this.cadastroNome = '';
    this.cadastroEmail = '';
    this.cadastroSenha = '';
    this.cadastroConfirmarSenha = '';
    this.cadastroErro = null;
  }

  private resetRecuperacao(): void {
    this.recuperacaoSubmetida = false;
    this.redefinicaoSubmetida = false;
    this.recuperacaoEtapa = 'solicitar';
    this.recuperacaoEmail = '';
    this.recuperacaoToken = '';
    this.recuperacaoSenha = '';
    this.recuperacaoConfirmarSenha = '';
    this.recuperacaoErro = null;
    this.recuperacaoMensagem = null;
  }

  private mapError(err: unknown): string {
    if (err && typeof err === 'object' && 'name' in err && err['name'] === 'TimeoutError') {
      return 'O servidor nao respondeu. Verifique sua conexao ou tente novamente em instantes.';
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

    return 'Falha ao processar a solicitacao. Tente novamente.';
  }
}

