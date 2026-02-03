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
  versaoDisponivel = '';
  historicoVersoes: HistoricoVersaoResponse[] = [];
  carregandoVersao = false;
  carregandoVersaoDisponivel = false;
  carregandoHistorico = false;
  erroVersao: string | null = null;
  erroVersaoDisponivel: string | null = null;
  erroHistorico: string | null = null;
  atualizandoVersao = false;
  feedbackAtualizacao: string | null = null;
  paginaAtual = 1;
  readonly itensPorPagina = 5;
  private versaoAlvo = '';
  private readonly resumoAtualizacao =
    'Controle de veiculos com persistencia no banco e ajustes no layout da tela.';

  private readonly destroy$ = new Subject<void>();

  constructor(
    private readonly configService: ConfigService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.recarregarDados();
    this.loadVersaoDisponivel();
    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        takeUntil(this.destroy$)
      )
      .subscribe(() => {
        if (this.router.url.includes('/configuracoes/versao')) {
          this.recarregarDados();
          this.loadVersaoDisponivel();
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
        this.atualizarVersaoSistemaSeNecessario();
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
        this.paginaAtual = 1;
      },
      error: () => {
        this.erroHistorico = 'Nao foi possivel carregar o historico de versoes.';
      },
      complete: () => {
        this.carregandoHistorico = false;
      }
    });
  }

  loadVersaoDisponivel(): void {
    this.carregandoVersaoDisponivel = true;
    this.erroVersaoDisponivel = null;
    this.configService.getVersaoArquivo().subscribe({
      next: (versao) => {
        const versaoLimpa = (versao || '').trim();
        this.versaoDisponivel = versaoLimpa;
        this.versaoAlvo = versaoLimpa;
        this.atualizarVersaoSistemaSeNecessario();
      },
      error: () => {
        this.erroVersaoDisponivel = 'Nao foi possivel carregar a versao disponivel.';
      },
      complete: () => {
        this.carregandoVersaoDisponivel = false;
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

  get totalPaginas(): number {
    return Math.max(1, Math.ceil(this.historicoVersoes.length / this.itensPorPagina));
  }

  get historicoPaginado(): HistoricoVersaoResponse[] {
    const inicio = (this.paginaAtual - 1) * this.itensPorPagina;
    return this.historicoVersoes.slice(inicio, inicio + this.itensPorPagina);
  }

  avancarPagina(): void {
    if (this.paginaAtual < this.totalPaginas) {
      this.paginaAtual += 1;
    }
  }

  voltarPagina(): void {
    if (this.paginaAtual > 1) {
      this.paginaAtual -= 1;
    }
  }

  private atualizarVersaoSistemaSeNecessario(): void {
    if (!this.versaoAlvo) return;
    if (this.atualizandoVersao) return;
    if (this.currentVersion === this.versaoAlvo) return;

    this.atualizandoVersao = true;
    this.feedbackAtualizacao = null;
    this.configService
      .atualizarVersaoSistema({
        versao: this.versaoAlvo,
        descricao: this.resumoAtualizacao
      })
      .subscribe({
        next: (response) => {
          this.currentVersion = response.versao || this.versaoAlvo;
          this.feedbackAtualizacao = 'Versao atualizada com sucesso.';
          this.loadHistorico();
        },
        error: () => {
          this.feedbackAtualizacao = 'Nao foi possivel atualizar a versao.';
        },
        complete: () => {
          this.atualizandoVersao = false;
        }
      });
  }

  atualizarSistema(): void {
    if (!this.versaoDisponivel || this.versaoDisponivel === this.currentVersion) {
      return;
    }
    this.feedbackAtualizacao = 'Live update pendente de implementacao.';
  }
}
