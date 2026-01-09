import { Component, ElementRef, HostListener, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, NavigationEnd, Router, RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faChevronDown,
  faChevronUp,
  faPalette,
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
  readonly faPalette = faPalette;
  pageTitle = 'Visao geral';
  get activeUnitLogo$() {
    return this.assistanceUnitService.currentUnitLogo$;
  }
  private destroy$ = new Subject<void>();
  isSidebarCollapsed = true;

  openSection: string | null = null;

  menuSections: MenuItem[] = [];

  constructor(
    private readonly auth: AuthService,
    private readonly assistanceUnitService: AssistanceUnitService,
    public readonly themeService: ThemeService,
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute
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
