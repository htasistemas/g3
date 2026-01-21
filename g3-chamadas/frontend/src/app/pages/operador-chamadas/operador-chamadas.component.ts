import { Component, OnInit } from '@angular/core';
import { ChamadaApiService } from '../../core/api/chamada-api.service';
import { BeneficiarioApiService } from '../../core/api/beneficiario-api.service';
import { BeneficiarioModel } from '../../core/shared/modelos/beneficiario.model';
import { FilaModel } from '../../core/shared/modelos/fila.model';

@Component({
  selector: 'app-operador-chamadas',
  templateUrl: './operador-chamadas.component.html',
  styleUrls: ['./operador-chamadas.component.scss']
})
export class OperadorChamadasComponent implements OnInit {
  filaAguardando: FilaModel[] = [];
  beneficiarios: BeneficiarioModel[] = [];
  carregando = false;
  carregandoBeneficiarios = false;
  mensagem: string | null = null;
  localAtendimento = 'Guiche 02';
  beneficiarioSelecionadoId: number | null = null;
  prioridadeEntrada = 0;

  constructor(
    private readonly chamadaApi: ChamadaApiService,
    private readonly beneficiarioApi: BeneficiarioApiService
  ) {}

  ngOnInit(): void {
    this.carregarBeneficiarios();
    this.carregarFila();
  }

  carregarBeneficiarios(): void {
    this.carregandoBeneficiarios = true;
    this.beneficiarioApi.listar().subscribe({
      next: (lista) => {
        this.beneficiarios = lista;
        this.carregandoBeneficiarios = false;
      },
      error: () => {
        this.mensagem = 'Nao foi possivel carregar os beneficiarios.';
        this.carregandoBeneficiarios = false;
      }
    });
  }

  carregarFila(): void {
    this.carregando = true;
    this.mensagem = null;
    this.chamadaApi.listarFilaAguardando().subscribe({
      next: (fila) => {
        this.filaAguardando = fila;
        this.carregando = false;
      },
      error: () => {
        this.carregando = false;
        this.mensagem = 'Nao foi possivel carregar a fila.';
      }
    });
  }

  entrarFila(): void {
    if (!this.beneficiarioSelecionadoId) {
      this.mensagem = 'Selecione um beneficiario para inserir na fila.';
      return;
    }
    const prioridade = this.prioridadeEntrada ?? 0;
    this.chamadaApi.entrarFila(this.beneficiarioSelecionadoId, prioridade).subscribe({
      next: () => {
        this.mensagem = 'Beneficiario inserido na fila.';
        this.beneficiarioSelecionadoId = null;
        this.prioridadeEntrada = 0;
        this.carregarFila();
      },
      error: () => {
        this.mensagem = 'Nao foi possivel inserir o beneficiario na fila.';
      }
    });
  }

  chamar(item: FilaModel): void {
    if (!this.localAtendimento.trim()) {
      this.mensagem = 'Informe o local de atendimento.';
      return;
    }
    this.chamadaApi.chamar(item.idFilaAtendimento, this.localAtendimento).subscribe({
      next: () => {
        this.mensagem = `Beneficiario chamado para ${this.localAtendimento}.`;
        this.carregarFila();
      },
      error: () => {
        this.mensagem = 'Nao foi possivel chamar o beneficiario.';
      }
    });
  }
}
