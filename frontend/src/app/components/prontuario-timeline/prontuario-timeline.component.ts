import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ProntuarioRegistroResponse } from '../../services/prontuario.service';

@Component({
  selector: 'app-prontuario-timeline',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './prontuario-timeline.component.html',
  styleUrl: './prontuario-timeline.component.scss'
})
export class ProntuarioTimelineComponent {
  @Input() registros: ProntuarioRegistroResponse[] = [];
  @Input() total = 0;
  @Input() carregando = false;
  @Input() pagina = 0;
  @Input() tamanhoPagina = 10;

  @Output() editar = new EventEmitter<ProntuarioRegistroResponse>();
  @Output() remover = new EventEmitter<ProntuarioRegistroResponse>();
  @Output() carregarMais = new EventEmitter<void>();

  expandedId: number | null = null;

  private readonly tipoLabels: Record<string, string> = {
    atendimento: 'Atendimento',
    procedimento: 'Procedimento',
    evolucao: 'Evolução',
    encaminhamento: 'Encaminhamento',
    documento: 'Documento/Anexo',
    visita_ref: 'Visita domiciliar (referência)',
    outro: 'Outro'
  };

  toggle(registro: ProntuarioRegistroResponse): void {
    this.expandedId = this.expandedId === registro.id ? null : registro.id;
  }

  get hasMore(): boolean {
    return (this.pagina + 1) * this.tamanhoPagina < this.total;
  }

  getTipoLabel(tipo: string): string {
    return this.tipoLabels[tipo] ?? tipo;
  }

  getExtraDetalhes(registro: ProntuarioRegistroResponse): { label: string; value: string }[] {
    const extra = registro.dadosExtra ?? {};
    const mapping: Record<string, string> = {
      dataInicio: 'Data de início',
      dataFim: 'Data de fim',
      tipoAtendimento: 'Tipo de atendimento',
      localAtendimento: 'Local do atendimento',
      servicoPrograma: 'Serviço/Programa',
      motivoDemanda: 'Motivo/Demanda',
      classificacaoRisco: 'Classificação de risco',
      resultadoAtendimento: 'Resultado',
      proximosPassos: 'Próximos passos',
      tipoProcedimento: 'Tipo de procedimento',
      vinculoRegistro: 'Vínculo com registro',
      situacaoAtual: 'Situação atual',
      pendencias: 'Pendências',
      destino: 'Destino',
      motivo: 'Motivo',
      prioridade: 'Prioridade',
      statusEncaminhamento: 'Status do encaminhamento',
      prazoRetorno: 'Prazo previsto',
      formaEncaminhamento: 'Forma de encaminhamento',
      tipoDocumento: 'Tipo do documento',
      dataDocumento: 'Data do documento',
      origemDocumento: 'Origem',
      descricaoDocumento: 'Descrição do documento'
    };

    return Object.entries(extra)
      .filter(([key, value]) => mapping[key] && value !== null && value !== undefined && String(value).trim() !== '')
      .map(([key, value]) => ({ label: mapping[key], value: String(value) }));
  }
}
