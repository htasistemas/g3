import { Component, EventEmitter, HostListener, Input, Output } from '@angular/core';
import { AgendaDiaDetalheResponse } from '../../services/emprestimos-eventos.service';

@Component({
  standalone: false,
  selector: 'app-agenda-dia-modal',
  templateUrl: './agenda-dia-modal.component.html',
  styleUrl: './agenda-dia-modal.component.scss'
})
export class AgendaDiaModalComponent {
  @Input() aberto = false;
  @Input() data: Date | null = null;
  @Input() detalhes: AgendaDiaDetalheResponse[] = [];

  @Output() fechar = new EventEmitter<void>();
  @Output() abrirEmprestimo = new EventEmitter<number>();

  @HostListener('document:keydown.escape')
  onEsc(): void {
    if (this.aberto) {
      this.fechar.emit();
    }
  }

  get rotuloData(): string {
    return this.data ? this.data.toLocaleDateString('pt-BR') : '';
  }
}


