import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, NavigationEnd, Router, RouterModule } from '@angular/router';
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
  faPalette,
  faRightFromBracket,
  faScaleBalanced,
  faSun,
  faMoon,
  faUserCircle,
  faUserPlus,
  faUsers,
  faWallet,
  faWrench
} from '@fortawesome/free-solid-svg-icons';
import { AssistanceUnitService } from '../services/assistance-unit.service';
import { ThemeService } from '../services/theme.service';
import { filter, takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';

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
export class LayoutComponent implements OnInit, OnDestroy {
  readonly faChevronDown = faChevronDown;
  readonly faChevronUp = faChevronUp;
  readonly faRightFromBracket = faRightFromBracket;
  readonly faUserCircle = faUserCircle;
  readonly faSun = faSun;
  readonly faMoon = faMoon;
  readonly faPalette = faPalette;
  pageTitle = 'Visão geral';
  get activeUnitLogo$() {
    return this.assistanceUnitService.currentUnitLogo$;
  }
  private destroy$ = new Subject<void>();
  isSidebarCollapsed = true;

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
        { label: 'Unidade Assistencial', icon: faHouseChimneyUser, route: '/unidades/cadastro' },
        { label: 'Beneficiários', icon: faUserPlus, route: '/cadastros/beneficiarios' },
        { label: 'Composição familiar', icon: faUsers, route: '/cadastros/familias' },
        { label: 'Voluntariados', icon: faClipboardList, route: '/cadastros/voluntariados' }
      ]
    },
    {
      label: 'Atendimentos',
      icon: faHandshakeAngle,
      children: [{ label: 'Registrar Doações', icon: faClipboardList, route: '/atendimentos/doacoes' }]
    },
    {
      label: 'Administrativo',
      icon: faClipboardList,
      children: [
        { label: 'Ofícios', icon: faClipboardList },
        { label: 'Documentos Internos', icon: faClipboardList },
        { label: 'Almoxarifado', icon: faClipboardList, route: '/administrativo/almoxarifado' },
        { label: 'Patrimônio', icon: faClipboardList, route: '/administrativo/patrimonio' }
      ]
    },
    {
      label: 'Financeiro',
      icon: faWallet,
      children: [
        { label: 'Prestação de Contas', icon: faWallet, route: '/financeiro/prestacao-contas' },
        { label: 'Contabilidade', icon: faWallet, route: '/financeiro/contabilidade' }
      ]
    },
    {
      label: 'Jurídico',
      icon: faScaleBalanced,
      children: [
        { label: 'Termo de Fomento', icon: faScaleBalanced },
        { label: 'Plano de Trabalho', icon: faClipboardList }
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
        { label: 'Parâmetros do sistema', icon: faWrench, route: '/configuracoes/parametros' },
        { label: 'Modelo de textos', icon: faClipboardList, route: '/configuracoes/modelos-texto' },
        { label: 'Versão do sistema', icon: faClipboardList, route: '/configuracoes/versao' },
        { label: 'Usuários e permissões', icon: faUserPlus, route: '/configuracoes/usuarios' },
        { label: 'Personalização', icon: faPalette, route: '/configuracoes/personalizacao' },
        { label: 'Backup e restauração', icon: faClipboardList }
      ]
    }
  ];

  constructor(
    private readonly auth: AuthService,
    private readonly assistanceUnitService: AssistanceUnitService,
    public readonly themeService: ThemeService,
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const initialRoute = this.findDeepestChild(this.activatedRoute);
    this.pageTitle = initialRoute.snapshot.data['title'] || 'Visão geral';

    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        takeUntil(this.destroy$)
      )
      .subscribe(() => {
        const deepest = this.findDeepestChild(this.activatedRoute);
        this.pageTitle = deepest.snapshot.data['title'] || 'Visão geral';
      });
  }

  get username(): string {
    return this.auth.user()?.nomeUsuario ?? 'Admin';
  }

  get activeUnitName$() {
    return this.assistanceUnitService.currentUnitName$;
  }

  toggleSection(label: string): void {
    this.openSection = this.openSection === label ? null : label;
  }

  handleSidebarEnter(): void {
    this.isSidebarCollapsed = false;
  }

  handleSidebarLeave(): void {
    this.isSidebarCollapsed = true;
    this.openSection = null;
  }

  closeSection(label: string): void {
    if (this.openSection === label) {
      this.openSection = null;
    }
  }

  logout(): void {
    this.auth.logout();
  }

  toggleTheme(): void {
    this.themeService.toggleTheme();
  }

  get themeLabel(): 'Claro' | 'Escuro' {
    return this.themeService.currentTheme() === 'dark' ? 'Escuro' : 'Claro';
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private findDeepestChild(route: ActivatedRoute): ActivatedRoute {
    let child: ActivatedRoute = route;

    while (child.firstChild) {
      child = child.firstChild;
    }

    return child;
  }
}
