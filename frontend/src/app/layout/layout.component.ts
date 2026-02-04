import { Component, ElementRef, HostListener, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, NavigationEnd, Router, RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faChevronDown,
  faChevronUp,
  faRightFromBracket,
  faSun,
  faMoon,
  faUserCircle
} from '@fortawesome/free-solid-svg-icons';
import { menuSections, MenuChild, MenuItem } from './menu-config';
import { AssistanceUnitService } from '../services/assistance-unit.service';
import { ThemeService } from '../services/theme.service';
import { filter, takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';
import { TarefasPendenciasService } from '../services/tarefas-pendencias.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterModule, FontAwesomeModule],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss'
})
export class LayoutComponent implements OnInit, OnDestroy {
  @ViewChild('sidebar') sidebarRef?: ElementRef<HTMLElement>;

  readonly faChevronDown = faChevronDown;
  readonly faChevronUp = faChevronUp;
  readonly faRightFromBracket = faRightFromBracket;
  readonly faUserCircle = faUserCircle;
  readonly faSun = faSun;
  readonly faMoon = faMoon;
  pageTitle = 'Visao geral';
  get activeUnitLogo$() {
    return this.assistanceUnitService.currentUnitLogo$;
  }
  private destroy$ = new Subject<void>();
  isSidebarCollapsed = true;

  openSection: string | null = null;

  menuSections: MenuItem[] = [];
  tarefasAbertas = 0;
  tarefasVencidas = 0;
  private tarefasRefreshId?: ReturnType<typeof setInterval>;

  constructor(
    private readonly auth: AuthService,
    private readonly assistanceUnitService: AssistanceUnitService,
    public readonly themeService: ThemeService,
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute,
    private readonly tarefasService: TarefasPendenciasService
  ) {}

  ngOnInit(): void {
    this.menuSections = this.filtrarMenuPorPermissoes(menuSections);
    const initialRoute = this.findDeepestChild(this.activatedRoute);
    this.pageTitle = initialRoute.snapshot.data['title'] || 'Visao geral';

    this.router.events
      .pipe(
        filter((event) => event instanceof NavigationEnd),
        takeUntil(this.destroy$)
      )
      .subscribe(() => {
        const deepest = this.findDeepestChild(this.activatedRoute);
        this.pageTitle = deepest.snapshot.data['title'] || 'Visao geral';
      });

    this.carregarResumoTarefas();
    this.carregarUnidadeAtiva();
    this.tarefasRefreshId = setInterval(() => {
      this.carregarResumoTarefas();
    }, 30000);
  }

  get username(): string {
    const usuario = this.auth.user();
    return usuario?.nome ?? usuario?.nomeUsuario ?? 'Admin';
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
    if (this.tarefasRefreshId) {
      clearInterval(this.tarefasRefreshId);
    }
    this.destroy$.next();
    this.destroy$.complete();
  }

  abrirTarefas(): void {
    this.router.navigate(['administrativo/tarefas']);
  }

  abrirTarefasAbertas(): void {
    this.router.navigate(['administrativo/tarefas'], { queryParams: { filtro: 'abertas' } });
  }

  abrirTarefasVencidas(): void {
    this.router.navigate(['administrativo/tarefas'], { queryParams: { filtro: 'vencidas' } });
  }

  private findDeepestChild(route: ActivatedRoute): ActivatedRoute {
    let child: ActivatedRoute = route;

    while (child.firstChild) {
      child = child.firstChild;
    }

    return child;
  }

  private filtrarMenuPorPermissoes(sections: MenuItem[]): MenuItem[] {
    const permissoes = this.auth.user()?.permissoes ?? [];
    return sections
      .map((section) => {
        const children =
          section.children?.filter((child) => {
            if (!child.permissao) return true;
            return permissoes.includes(child.permissao);
          }) ?? [];
        return { ...section, children };
      })
      .filter((section) => (section.children ?? []).length > 0);
  }

  private carregarResumoTarefas(): void {
    this.tarefasService.list().subscribe({
      next: (tarefas) => {
        const abertas = tarefas.filter((item) => item.status !== 'Concluída');
        this.tarefasAbertas = abertas.length;
        this.tarefasVencidas = abertas.filter((item) => {
          const prazo = new Date(item.prazo ?? '');
          if (Number.isNaN(prazo.getTime())) return false;
          return prazo < new Date();
        }).length;
      },
      error: () => {
        this.tarefasAbertas = 0;
        this.tarefasVencidas = 0;
      }
    });
  }

  private carregarUnidadeAtiva(): void {
    this.assistanceUnitService
      .get()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (resposta) => {
          const unidade = resposta?.unidade;
          if (unidade) {
            const nome = unidade.nomeFantasia?.trim() || unidade.razaoSocial?.trim() || 'Navegação';
            const logomarca = unidade.logomarca || null;
            this.assistanceUnitService.setActiveUnit(nome, logomarca);
          } else {
            this.assistanceUnitService.setActiveUnit('Navegação', null);
          }
        },
        error: () => {
          this.assistanceUnitService.setActiveUnit('Navegação', null);
        }
      });
  }

  @HostListener('document:click', ['$event'])
  handleDocumentClick(event: MouseEvent): void {
    const sidebarElement = this.sidebarRef?.nativeElement;

    if (!sidebarElement) {
      return;
    }

    if (!sidebarElement.contains(event.target as Node)) {
      this.isSidebarCollapsed = true;
      this.openSection = null;
    }
  }
}
