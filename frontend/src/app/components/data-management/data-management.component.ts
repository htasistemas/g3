import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { DialogComponent } from '../compartilhado/dialog/dialog.component';
import {
  GerenciamentoDadosBackupResponse,
  GerenciamentoDadosConfiguracaoResponse,
  GerenciamentoDadosService,
  GerenciamentoDadosRestauracaoResponse,
} from '../../services/gerenciamento-dados.service';

type BackupStatus = 'executando' | 'sucesso' | 'falha';

interface BackupRecord {
  id: number;
  codigo: string;
  rotulo: string;
  tipo: 'Completo' | 'Incremental';
  status: BackupStatus;
  iniciadoEm: string;
  armazenadoEm?: string;
  tamanho?: string;
  criptografado: boolean;
  retencaoDias: number;
}

@Component({
  selector: 'app-data-management',
  standalone: true,
  imports: [CommonModule, TelaPadraoComponent, DialogComponent],
  templateUrl: './data-management.component.html',
  styleUrl: './data-management.component.scss'
})
export class DataManagementComponent implements OnInit {
  configuracao: GerenciamentoDadosConfiguracaoResponse | null = null;
  runningBackupId: number | null = null;
  recentBackups: BackupRecord[] = [];
  restauracaoFeedback: { tipo: 'success' | 'error' | 'info'; mensagem: string } | null = null;
  dialogoRestaurarAberto = false;
  backupSelecionado: BackupRecord | null = null;

  constructor(
    private readonly gerenciamentoDadosService: GerenciamentoDadosService
  ) {}

  ngOnInit(): void {
    this.carregarConfiguracao();
    this.carregarBackups();
  }

  startBackup(): void {
    if (this.runningBackupId) {
      return;
    }

    const retencaoDias = this.configuracao?.retencaoDias ?? 15;
    const armazenadoEm = this.configuracao?.caminhoDestino ?? 'C:\\Backup G3';
    const criptografado = !!this.configuracao?.criptografia;

    this.gerenciamentoDadosService
      .criarBackup({
        rotulo: 'Backup completo manual',
        tipo: 'Completo',
        status: 'executando',
        armazenadoEm,
        criptografado,
        retencaoDias,
      })
      .subscribe((backup) => {
        const item = this.mapearBackupResposta(backup);
        this.recentBackups = [item, ...this.recentBackups].slice(0, 6);
        this.runningBackupId = item.status === 'executando' ? item.id : null;
      });
  }

  abrirDialogoRestaurar(backup: BackupRecord): void {
    this.backupSelecionado = backup;
    this.dialogoRestaurarAberto = true;
  }

  cancelarDialogoRestaurar(): void {
    this.dialogoRestaurarAberto = false;
    this.backupSelecionado = null;
  }

  confirmarDialogoRestaurar(): void {
    if (!this.backupSelecionado) {
      this.cancelarDialogoRestaurar();
      return;
    }

    const backupId = this.backupSelecionado.id;
    this.dialogoRestaurarAberto = false;
    this.restauracaoFeedback = {
      tipo: 'info',
      mensagem: 'Restauracao iniciada. Aguarde a conclusao.',
    };

    this.gerenciamentoDadosService.restaurarBackup(backupId).subscribe({
      next: (response) => this.aplicarFeedbackRestauracao(response),
      error: () => {
        this.restauracaoFeedback = {
          tipo: 'error',
          mensagem: 'Falha ao solicitar a restauracao do backup.',
        };
      },
    });
  }

  lastSuccessfulBackup(): BackupRecord | undefined {
    return this.recentBackups.find((backup) => backup.status === 'sucesso');
  }

  runningBackup(): BackupRecord | undefined {
    return this.recentBackups.find((backup) => backup.status === 'executando');
  }

  private carregarConfiguracao(): void {
    this.gerenciamentoDadosService
      .obterConfiguracao()
      .subscribe((configuracao) => {
        this.configuracao = configuracao;
      });
  }

  private carregarBackups(): void {
    this.gerenciamentoDadosService.listarBackups().subscribe((backups) => {
      this.recentBackups = backups.map((backup) => this.mapearBackupResposta(backup));
      this.runningBackupId = this.runningBackup()?.id ?? null;
    });
  }

  private aplicarFeedbackRestauracao(response: GerenciamentoDadosRestauracaoResponse): void {
    this.restauracaoFeedback = {
      tipo: response.status === 'sucesso' ? 'success' : 'error',
      mensagem: response.mensagem,
    };
    this.carregarBackups();
  }

  private mapearBackupResposta(backup: GerenciamentoDadosBackupResponse): BackupRecord {
    return {
      id: backup.id,
      codigo: backup.codigo,
      rotulo: backup.rotulo,
      tipo: backup.tipo as BackupRecord['tipo'],
      status: backup.status as BackupStatus,
      iniciadoEm: backup.iniciadoEm,
      armazenadoEm: backup.armazenadoEm,
      tamanho: backup.tamanho,
      criptografado: backup.criptografado,
      retencaoDias: backup.retencaoDias,
    };
  }
}
