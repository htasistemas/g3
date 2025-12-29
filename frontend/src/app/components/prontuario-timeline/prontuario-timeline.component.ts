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

  toggle(registro: ProntuarioRegistroResponse): void {
    this.expandedId = this.expandedId === registro.id ? null : registro.id;
  }

  get hasMore(): boolean {
    return (this.pagina + 1) * this.tamanhoPagina < this.total;
  }
}
