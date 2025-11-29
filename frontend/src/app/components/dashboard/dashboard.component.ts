import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {
  placeholderItems = [
    'Beneficiários',
    'Famílias e Domicílios',
    'Atendimentos',
    'Programas e Projetos',
    'Documentos e Anexos'
  ];
}
