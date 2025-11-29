import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { IconDefinition } from '@fortawesome/fontawesome-svg-core';
import {
  faChevronDown,
  faChevronUp,
  faClipboardList,
  faGauge,
  faHandshakeAngle,
  faHouseChimneyUser,
  faRightFromBracket,
  faUserCircle,
  faUserPlus,
  faUsers,
  faWallet
} from '@fortawesome/free-solid-svg-icons';
import { environment } from '../../environments/environment';
import { AssistanceUnitService } from '../services/assistance-unit.service';

interface MenuChild {
  label: string;
  icon: IconDefinition;
  route?: string;
}

interface MenuItem {
  label: string;
  icon: IconDefinition;
  children?: MenuChild[];
}

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, FontAwesomeModule],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent {
  readonly faChevronDown = faChevronDown;
  readonly faChevronUp = faChevronUp;
  readonly faRightFromBracket = faRightFromBracket;
  readonly faUserCircle = faUserCircle;
  readonly version = environment.version;

  openSection: string | null = null;

  menuSections: MenuItem[] = [
    {
      label: 'Dashboard',
      icon: faGauge,
      children: [{ label: 'Visão Geral', icon: faGauge, route: '/' }]
    },
    {
      label: 'Cadastros',
      icon: faUsers,
      children: [
        { label: 'Beneficiário', icon: faUserPlus, route: '/beneficiarios/cadastro' },
        { label: 'Unidade Assistencial', icon: faHouseChimneyUser, route: '/unidades/cadastro' }
      ]
    },
    {
      label: 'Famílias e Domicílios',
      icon: faHouseChimneyUser,
      children: [
        { label: 'Cadastro de Famílias', icon: faUsers },
        { label: 'Condições Habitacionais', icon: faClipboardList },
        { label: 'Acompanhamento', icon: faHandshakeAngle }
      ]
    },
    {
      label: 'Atendimentos Sociais',
      icon: faHandshakeAngle,
      children: [
        { label: 'Registrar Atendimento', icon: faClipboardList },
        { label: 'Relatório de Visita', icon: faWallet },
        { label: 'Acompanhamentos Contínuos', icon: faUsers }
      ]
    },
    {
      label: 'Programas e Projetos',
      icon: faWallet,
      children: [
        { label: 'Cadastro de Programas', icon: faClipboardList },
        { label: 'Inclusão de Beneficiários', icon: faUsers },
        { label: 'Avaliação Social', icon: faHandshakeAngle }
      ]
    }
  ];

  constructor(
    private readonly auth: AuthService,
    private readonly assistanceUnitService: AssistanceUnitService
  ) {}

  get username(): string {
    return this.auth.user()?.username ?? 'Admin';
  }

  get activeUnitName$() {
    return this.assistanceUnitService.currentUnitName$;
  }

  toggleSection(label: string): void {
    this.openSection = this.openSection === label ? null : label;
  }

  logout(): void {
    this.auth.logout();
  }
}
