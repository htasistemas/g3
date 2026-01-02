import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfigService, HistoricoVersaoResponse } from '../../services/config.service';
import { NavigationEnd, Router } from '@angular/router';
import { Subject, filter, takeUntil } from 'rxjs';

@Component({
  selector: 'app-system-version',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './system-version.component.html',
  styleUrl: './system-version.component.scss'
})
export class SystemVersionComponent implements OnInit, OnDestroy {
  currentVersion = '';
  historicoVersoes: HistoricoVersaoResponse[] = [];
  carregandoVersao = false;
  carregandoHistorico = false;
  erroVersao: string | null = null;
  erroHistorico: string | null = null;

  private readonly destroy$ = new Subject<void>();

  constructor(
    private readonly configService: ConfigService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.recarregarDados();
    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        takeUntil(this.destroy$)
      )
      .subscribe(() => {
        if (this.router.url.includes('/configuracoes/versao')) {
          this.recarregarDados();
        }
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private recarregarDados(): void {
    this.loadVersaoAtual();
    this.loadHistorico();
  }

  loadVersaoAtual(): void {
    this.carregandoVersao = true;
    this.erroVersao = null;
    this.configService.getVersaoSistema().subscribe({
      next: (response) => {
        this.currentVersion = response.versao || '';
      },
      error: () => {
        this.erroVersao = 'Nao foi possivel carregar a versao do sistema.';
      },
      complete: () => {
        this.carregandoVersao = false;
      }
    });
  }

  loadHistorico(): void {
    this.carregandoHistorico = true;
    this.erroHistorico = null;
    this.configService.listarHistoricoVersoes().subscribe({
      next: (response) => {
        this.historicoVersoes = response || [];
      },
      error: () => {
        this.erroHistorico = 'Nao foi possivel carregar o historico de versoes.';
      },
      complete: () => {
        this.carregandoHistorico = false;
      }
    });
  }

  formatarDataHora(valor: string): string {
    if (!valor) {
      return '';
    }
    const data = new Date(valor);
    return data.toLocaleString('pt-BR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      second: '2-digit'
    });
  }
}
