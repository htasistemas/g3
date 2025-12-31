import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConfigService, HistoricoVersaoResponse } from '../../services/config.service';

@Component({
  selector: 'app-system-version',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './system-version.component.html',
  styleUrl: './system-version.component.scss'
})
export class SystemVersionComponent implements OnInit {
  currentVersion = '';
  historicoVersoes: HistoricoVersaoResponse[] = [];
  carregandoVersao = false;
  carregandoHistorico = false;
  erroVersao: string | null = null;
  erroHistorico: string | null = null;

  constructor(private readonly configService: ConfigService) {}

  ngOnInit(): void {
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
