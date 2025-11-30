import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { AssistanceUnitService } from '../../services/assistance-unit.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent implements OnInit {
  nomeUsuario = 'admin';
  senha = '123';
  loading = false;
  error: string | null = null;
  unidadeLogomarca: string | null = null;
  unidadeNome: string | undefined;

  constructor(
    private readonly auth: AuthService,
    private readonly router: Router,
    private readonly assistanceUnitService: AssistanceUnitService
  ) {}

  ngOnInit(): void {
    this.assistanceUnitService.get().subscribe(({ unidade }) => {
      this.unidadeLogomarca = unidade?.logomarca || null;
      this.unidadeNome = unidade?.nomeFantasia;
    });
  }

  submit(): void {
    this.error = null;
    this.loading = true;
    this.auth.login(this.nomeUsuario, this.senha).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.message || 'Falha ao fazer login. Tente novamente.';
      }
    });
  }
}
