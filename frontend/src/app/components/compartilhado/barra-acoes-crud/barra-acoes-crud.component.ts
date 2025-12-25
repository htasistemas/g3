import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ConfigAcoesCrud, EstadoAcoesCrud } from '../tela-base.component';

@Component({
  selector: 'app-barra-acoes-crud',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './barra-acoes-crud.component.html',
  styleUrl: './barra-acoes-crud.component.scss'
})
export class BarraAcoesCrudComponent {
  @Input({ required: true }) acoes: ConfigAcoesCrud = {};
  @Input() desabilitado: EstadoAcoesCrud = {};
  @Input() mostrarFechar = false;

  @Output() salvar = new EventEmitter<void>();
  @Output() excluir = new EventEmitter<void>();
  @Output() novo = new EventEmitter<void>();
  @Output() cancelar = new EventEmitter<void>();
  @Output() imprimir = new EventEmitter<void>();
  @Output() buscar = new EventEmitter<void>();
  @Output() fechar = new EventEmitter<void>();
}
