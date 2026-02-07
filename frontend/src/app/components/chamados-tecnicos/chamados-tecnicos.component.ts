import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faHeadset } from '@fortawesome/free-solid-svg-icons';
import { Subject, finalize, takeUntil } from 'rxjs';
import { TelaBaseComponent } from '../compartilhado/tela-base.component';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';
import {
  ChamadoTecnicoListaResponse,
  ChamadoTecnicoPayload,
  ChamadoTecnicoService,
  ChamadoStatus,
  ChamadoPrioridade,
  ChamadoTipo,
} from '../../services/chamado-tecnico.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-chamados-tecnicos',
  standalone: true,
  templateUrl: './chamados-tecnicos.component.html',
  styleUrl: './chamados-tecnicos.component.scss',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    FontAwesomeModule,
    TelaPadraoComponent,
    PopupMessagesComponent
  ],
})
export class ChamadosTecnicosComponent extends TelaBaseComponent implements OnInit, OnDestroy {
  readonly acoesToolbar = this.criarConfigAcoes({
    buscar: true,
    novo: true,
    imprimir: false,
    salvar: false,
    excluir: false,
    cancelar: false,
  });

  chamados: ChamadoTecnicoPayload[] = [];
  chamadoSelecionado: ChamadoTecnicoPayload | null = null;
  total = 0;
  pagina = 1;
  tamanhoPagina = 12;
  carregando = false;
  erroLista: string | null = null;
  popupErros: string[] = [];
  modoTela: 'usuario' | 'desenvolvedor' = 'usuario';
  readonly faHeadset = faHeadset;
  abaAtiva: 'listagem' | 'resumo' | 'historico' | 'comentarios' | 'anexos' = 'listagem';
  abaStatusAtiva: ChamadoStatus = 'ABERTO';
  abasStatus: { id: ChamadoStatus; label: string }[] = [
    { id: 'ABERTO', label: 'Abertos' },
    { id: 'EM_ANALISE', label: 'Em análise' },
    { id: 'EM_DESENVOLVIMENTO', label: 'Em desenvolvimento' },
    { id: 'AGUARDANDO_CLIENTE', label: 'Aguardando cliente' },
    { id: 'RESOLVIDO', label: 'Resolvidos' },
    { id: 'CANCELADO', label: 'Fechados' },
  ];

  statusOptions: ChamadoStatus[] = [
    'ABERTO',
    'EM_ANALISE',
    'EM_DESENVOLVIMENTO',
    'EM_TESTE',
    'AGUARDANDO_CLIENTE',
    'RESOLVIDO',
    'CANCELADO',
  ];
  prioridadeOptions: ChamadoPrioridade[] = ['BAIXA', 'MEDIA', 'ALTA', 'CRITICA'];
  tipoOptions: ChamadoTipo[] = ['ERRO', 'MELHORIA'];

  filtros!: FormGroup;

  private readonly destroy$ = new Subject<void>();

  constructor(
    private readonly fb: FormBuilder,
    private readonly service: ChamadoTecnicoService,
    private readonly authService: AuthService,
    private readonly router: Router,
    private readonly route: ActivatedRoute
  ) {
    super();
  }

  ngOnInit(): void {
    this.filtros = this.fb.group({
      status: [''],
      tipo: [''],
      prioridade: [''],
      modulo: [''],
      cliente: [''],
      responsavel: [''],
      data_inicio: [''],
      data_fim: [''],
      texto: [''],
    });
    this.route.data.pipe(takeUntil(this.destroy$)).subscribe((data) => {
      this.modoTela = data['perfil'] === 'desenvolvedor' ? 'desenvolvedor' : 'usuario';
    });
    this.selecionarAbaStatus(this.abaStatusAtiva, false);
    this.listar();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get totalPaginas(): number {
    return Math.max(1, Math.ceil(this.total / this.tamanhoPagina));
  }

  onBuscar(): void {
    this.pagina = 1;
    const statusSelecionado = this.filtros.value.status as ChamadoStatus | '';
    if (statusSelecionado) {
      this.abaStatusAtiva = statusSelecionado;
    }
    this.listar();
  }

  selecionarAba(aba: 'listagem' | 'resumo' | 'historico' | 'comentarios' | 'anexos'): void {
    this.abaAtiva = aba;
  }

  onNovo(): void {
    if (this.modoTela === 'desenvolvedor') {
      this.router.navigate(['/configuracoes/chamados-tecnicos']);
      return;
    }
    this.router.navigate(['/configuracoes/chamados-tecnicos/novo']);
  }

  onNew(): void {
    this.onNovo();
  }

  paginaAnterior(): void {
    if (this.pagina > 1) {
      this.pagina -= 1;
      this.listar();
    }
  }

  proximaPagina(): void {
    if (this.pagina < this.totalPaginas) {
      this.pagina += 1;
      this.listar();
    }
  }

  listar(): void {
    this.carregando = true;
    this.erroLista = null;
    const filtros = this.normalizeFiltros();
    this.service
      .listar({
        ...filtros,
        pagina: this.pagina,
        tamanho_pagina: this.tamanhoPagina,
      })
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => (this.carregando = false))
      )
      .subscribe({
        next: (response: ChamadoTecnicoListaResponse) => {
          this.chamados = response.chamados ?? [];
          this.total = response.total ?? 0;
        },
        error: () => {
          this.erroLista = 'Não foi possível carregar os chamados.';
        },
      });
  }

  private normalizeFiltros(): {
    status?: string;
    tipo?: string;
    prioridade?: string;
    responsavel?: string;
    modulo?: string;
    cliente?: string;
    data_inicio?: string;
    data_fim?: string;
    texto?: string;
  } {
    const raw = this.filtros.value as Record<string, string | null | undefined>;
    return {
      status: raw['status'] || undefined,
      tipo: raw['tipo'] || undefined,
      prioridade: raw['prioridade'] || undefined,
      responsavel: raw['responsavel'] || undefined,
      modulo: raw['modulo'] || undefined,
      cliente: raw['cliente'] || undefined,
      data_inicio: raw['data_inicio'] || undefined,
      data_fim: raw['data_fim'] || undefined,
      texto: raw['texto'] || undefined,
    };
  }

  limparFiltros(): void {
    this.filtros.reset({
      status: '',
      tipo: '',
      prioridade: '',
      modulo: '',
      cliente: '',
      responsavel: '',
      data_inicio: '',
      data_fim: '',
      texto: '',
    });
    this.listar();
  }

  selecionarAbaStatus(status: ChamadoStatus, listar = true): void {
    this.abaStatusAtiva = status;
    this.filtros.patchValue({ status });
    if (listar) {
      this.pagina = 1;
      this.listar();
    }
  }

  abrirChamado(chamado: ChamadoTecnicoPayload): void {
    if (!chamado.id) return;
    this.router.navigate([this.rotaBase(), chamado.id]);
  }

  selecionarChamado(chamado: ChamadoTecnicoPayload): void {
    this.chamadoSelecionado = chamado;
  }

  alterarStatus(chamado: ChamadoTecnicoPayload, status: ChamadoStatus): void {
    if (!chamado.id) return;
    const usuarioId = Number(this.authService.user()?.id || 0) || null;
    this.service.alterarStatus(chamado.id, status, usuarioId).subscribe({
      next: () => this.listar(),
      error: () => (this.erroLista = 'Não foi possível alterar o status.'),
    });
  }

  atribuirResponsavel(chamado: ChamadoTecnicoPayload, responsavelId: string): void {
    if (!chamado.id) return;
    const usuarioId = Number(this.authService.user()?.id || 0) || null;
    const parsed = responsavelId ? Number(responsavelId) : null;
    this.service.atribuirResponsavel(chamado.id, parsed, usuarioId).subscribe({
      next: () => this.listar(),
      error: () => (this.erroLista = 'Não foi possível atribuir o responsável.'),
    });
  }

  formatStatus(status?: string): string {
    if (!status) return '---';
    return status.replace(/_/g, ' ');
  }

  formatPrioridade(prioridade?: string): string {
    if (!prioridade) return '---';
    return prioridade.replace(/_/g, ' ');
  }

  statusClass(status?: string): string {
    switch (status) {
      case 'ABERTO':
        return 'pill pill--status pill--status-aberto';
      case 'EM_ANALISE':
        return 'pill pill--status pill--status-analise';
      case 'EM_DESENVOLVIMENTO':
        return 'pill pill--status pill--status-desenvolvimento';
      case 'EM_TESTE':
        return 'pill pill--status pill--status-teste';
      case 'AGUARDANDO_CLIENTE':
        return 'pill pill--status pill--status-aguardando';
      case 'RESOLVIDO':
        return 'pill pill--status pill--status-resolvido';
      case 'CANCELADO':
        return 'pill pill--status pill--status-cancelado';
      default:
        return 'pill pill--status';
    }
  }

  prioridadeClass(prioridade?: string): string {
    switch (prioridade) {
      case 'BAIXA':
        return 'pill pill--prioridade pill--prioridade-baixa';
      case 'MEDIA':
        return 'pill pill--prioridade pill--prioridade-media';
      case 'ALTA':
        return 'pill pill--prioridade pill--prioridade-alta';
      case 'CRITICA':
        return 'pill pill--prioridade pill--prioridade-critica';
      default:
        return 'pill pill--prioridade';
    }
  }

  slaLabel(chamado: ChamadoTecnicoPayload): string {
    if (!chamado.data_limite_sla) return 'Sem SLA';
    return chamado.sla_atrasado ? 'SLA atrasado' : 'SLA ok';
  }

  get usuarioAtualId(): number | null {
    return Number(this.authService.user()?.id || 0) || null;
  }

  private rotaBase(): string {
    return this.modoTela === 'desenvolvedor'
      ? '/configuracoes/chamados-tecnicos-dev'
      : '/configuracoes/chamados-tecnicos';
  }
}

