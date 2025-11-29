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
  faBullhorn,
  faGauge,
  faHandshakeAngle,
  faHouseChimneyUser,
  faMapLocationDot,
  faRightFromBracket,
  faScaleBalanced,
  faUserCircle,
  faUserPlus,
  faUsers,
  faWallet,
  faWrench
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
  readonly versionLabel = this.formatVersion(environment.version);

  openSection: string | null = null;

  menuSections: MenuItem[] = [
    {
      label: 'Dashboard',
      icon: faGauge,
      children: [
        { label: 'Visão Geral', icon: faGauge, route: '/' },
        { label: 'Indicadores', icon: faGauge }
      ]
    },
    {
      label: 'Cadastros',
      icon: faUsers,
      children: [
        { label: 'Beneficiários', icon: faUserPlus, route: '/beneficiarios/cadastro' },
        { label: 'Unidade Assistencial', icon: faHouseChimneyUser, route: '/unidades/cadastro' },
        { label: 'Cadastros de Família', icon: faUsers },
        { label: 'Voluntariados', icon: faClipboardList },
        { label: 'Colaboradores', icon: faClipboardList }
      ]
    },
    {
      label: 'Gestão Jurídica',
      icon: faScaleBalanced,
      children: [
        { label: 'Termo de Fomento', icon: faScaleBalanced },
        { label: 'Plano de Trabalho', icon: faClipboardList }
      ]
    },
    {
      label: 'Atendimentos',
      icon: faHandshakeAngle,
      children: [{ label: 'Registrar Doações', icon: faClipboardList }]
    },
    {
      label: 'Gestão de Marketing',
      icon: faBullhorn,
      children: [
        { label: 'Material publicitário', icon: faBullhorn },
        { label: 'Fotos em geral', icon: faClipboardList },
        { label: 'Arte final', icon: faClipboardList }
      ]
    },
    {
      label: 'Gestão Administrativa',
      icon: faClipboardList,
      children: [
        { label: 'Ofícios', icon: faClipboardList },
        { label: 'Documentos Internos', icon: faClipboardList },
        { label: 'Almoxarifado', icon: faClipboardList },
        { label: 'Patrimônio', icon: faClipboardList }
      ]
    },
    {
      label: 'Gestão Financeira',
      icon: faWallet,
      children: [
        { label: 'Prestação de Contas', icon: faWallet },
        { label: 'Contabilidade', icon: faWallet }
      ]
    },
    {
      label: 'Georeferenciamento',
      icon: faMapLocationDot,
      children: [{ label: 'Localização', icon: faMapLocationDot }]
    },
    {
      label: 'Configurações Gerais',
      icon: faWrench,
      children: [
        { label: 'Documentos obrigatórios', icon: faClipboardList, route: '/configuracoes/sistema' },
        { label: 'Usuários e permissões', icon: faUserPlus },
        { label: 'Backup e restauração', icon: faClipboardList }
      ]
    }
  ];

  constructor(
    private readonly auth: AuthService,
    private readonly assistanceUnitService: AssistanceUnitService
  ) {}

  get username(): string {
    return this.auth.user()?.nomeUsuario ?? 'Admin';
  }

  get activeUnitName$() {
    return this.assistanceUnitService.currentUnitName$;
  }

  toggleSection(label: string): void {
    this.openSection = this.openSection === label ? null : label;
  }

  closeSection(label: string): void {
    if (this.openSection === label) {
      this.openSection = null;
    }
  }

  logout(): void {
    this.auth.logout();
  }

  private formatVersion(version: string): string {
    const cleaned = version.trim().replace(/^v/i, '');
    const segments = cleaned.split('.').filter(Boolean);

    if (!segments.length) {
      return '0.0.00';
    }

    const [major = '0', minor = '0', patch = '0'] = segments;
    const paddedPatch = patch.padStart(2, '0');

    return `${major}.${minor}.${paddedPatch}`;
  }
}
