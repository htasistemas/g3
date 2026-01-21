import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ChamadaApiService } from '../../core/api/chamada-api.service';
import { ChamadaRealtimeService } from '../../core/realtime/chamada-realtime.service';
import { ChamadaEventoModel, ChamadaModel } from '../../core/shared/modelos/chamada.model';

@Component({
  selector: 'app-painel-senha',
  templateUrl: './painel-senha.component.html',
  styleUrls: ['./painel-senha.component.scss']
})
export class PainelSenhaComponent implements OnInit, OnDestroy {
  chamadaAtual: ChamadaModel | null = null;
  private assinatura: Subscription | null = null;

  constructor(
    private readonly chamadaApi: ChamadaApiService,
    private readonly realtime: ChamadaRealtimeService
  ) {}

  ngOnInit(): void {
    this.chamadaApi.ultimaChamada().subscribe({
      next: (chamada) => {
        this.chamadaAtual = chamada;
      },
      error: () => {
        this.chamadaAtual = null;
      }
    });

    this.assinatura = this.realtime.conectar().subscribe((evento: ChamadaEventoModel) => {
      if (evento?.evento === 'CHAMADA_BENEFICIARIO') {
        this.chamadaAtual = {
          idChamada: evento.dados.idChamada,
          idFilaAtendimento: 0,
          nomeBeneficiario: evento.dados.nomeBeneficiario,
          localAtendimento: evento.dados.localAtendimento,
          statusChamada: 'CHAMADO',
          dataHoraChamada: evento.dados.dataHoraChamada
        };
      }
    });
  }

  ngOnDestroy(): void {
    this.assinatura?.unsubscribe();
    this.realtime.desconectar();
  }
}
