import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DashboardComponent } from '../components/dashboard/dashboard.component';
import { AuthService } from '../services/auth.service';

interface MenuItem {
  label: string;
  children?: string[];
}

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, DashboardComponent],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {
  menuSections: MenuItem[] = [
    { label: 'Beneficiários', children: ['Cadastro de Beneficiário', 'Lista e Consulta', 'Documentos'] },
    { label: 'Famílias e Domicílios', children: ['Cadastro de Famílias', 'Condições Habitacionais', 'Acompanhamento'] },
    { label: 'Atendimentos Sociais', children: ['Registrar Atendimento', 'Relatório de Visita', 'Acompanhamentos Contínuos'] },
    { label: 'Programas e Projetos', children: ['Cadastro de Programas', 'Inclusão de Beneficiários', 'Avaliação Social'] }
  ];

  constructor(private readonly auth: AuthService) {}

  logout(): void {
    this.auth.logout();
  }
}
