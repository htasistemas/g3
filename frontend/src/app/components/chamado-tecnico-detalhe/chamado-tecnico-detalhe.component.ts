import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Subject, finalize, takeUntil } from 'rxjs';
import { PopupErrorBuilder } from '../../utils/popup-error.builder';
import { AuthService } from '../../services/auth.service';
import { AssistanceUnitService } from '../../services/assistance-unit.service';
import { ConfigService } from '../../services/config.service';
import { AutocompleteComponent, AutocompleteOpcao } from '../compartilhado/autocomplete/autocomplete.component';
import {
  AuditoriaEvento,
  ChamadoImpacto,
  ChamadoPrioridade,
  ChamadoStatus,
  ChamadoTecnicoAcao,
  ChamadoTecnicoAnexo,
  ChamadoTecnicoAuditoriaVinculo,
  ChamadoTecnicoComentario,
  ChamadoTecnicoPayload,
  ChamadoTecnicoService,
  ChamadoTipo,
} from '../../services/chamado-tecnico.service';
import { TelaBaseComponent } from '../compartilhado/tela-base.component';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';
import { menuSections } from '../../layout/menu-config';
import { ChamadoTecnicoKanbanComponent } from '../chamado-tecnico-kanban/chamado-tecnico-kanban.component';

type AbaChamadoTecnico =
  | 'listagem'
  | 'resumo'
  | 'kanban'
  | 'historico'
  | 'comentarios'
  | 'anexos'
  | 'auditoria'
  | 'resposta';

@Component({
  selector: 'app-chamado-tecnico-detalhe',
  standalone: true,
  templateUrl: './chamado-tecnico-detalhe.component.html',
  styleUrl: './chamado-tecnico-detalhe.component.scss',
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    TelaPadraoComponent,
    PopupMessagesComponent,
    AutocompleteComponent,
    ChamadoTecnicoKanbanComponent,
  ],
})
export class ChamadoTecnicoDetalheComponent extends TelaBaseComponent implements OnInit, OnDestroy {
  private static readonly PERMISSAO_DESENVOLVEDOR = 'ADMINISTRADOR';
  readonly acoesToolbar = this.criarConfigAcoes({
    salvar: true,
    excluir: false,
    novo: true,
    cancelar: true,
    imprimir: false,
    buscar: true,
  });

  popupErros: string[] = [];
  feedback: string | null = null;
  carregando = false;
  chamadoId: string | null = null;
  activeTab: AbaChamadoTecnico = 'resumo';
  tabs: { id: AbaChamadoTecnico; label: string }[] = [];
  modoTela: 'usuario' | 'desenvolvedor' = 'usuario';
  destinoChamado = 'htasistemas@gmail.com';

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
  impactoOptions: ChamadoImpacto[] = ['BAIXO', 'MEDIO', 'ALTO'];

  acoes: ChamadoTecnicoAcao[] = [];
  comentarios: ChamadoTecnicoComentario[] = [];
  anexos: ChamadoTecnicoAnexo[] = [];
  auditorias: ChamadoTecnicoAuditoriaVinculo[] = [];
  auditoriaDisponivel: AuditoriaEvento[] = [];
  chamadosAbertos: ChamadoTecnicoPayload[] = [];
  carregandoAbertos = false;
  carregouAbertos = false;

  comentarioTexto = '';
  auditoriaFiltro!: FormGroup;

  form!: FormGroup;
  moduloTermo = '';
  menuTermo = '';
  moduloOpcoes: AutocompleteOpcao[] = [];
  menuOpcoes: AutocompleteOpcao[] = [];
  private moduloOpcoesBase: AutocompleteOpcao[] = [];
  private menuOpcoesBase: AutocompleteOpcao[] = [];

  private readonly destroy$ = new Subject<void>();

  constructor(
    private readonly fb: FormBuilder,
    private readonly service: ChamadoTecnicoService,
    private readonly authService: AuthService,
    private readonly assistanceUnitService: AssistanceUnitService,
    private readonly configService: ConfigService,
    private readonly route: ActivatedRoute,
    private readonly router: Router
  ) {
    super();
  }

  ngOnInit(): void {
    this.auditoriaFiltro = this.fb.group({
      data_inicio: [''],
      data_fim: [''],
      usuario_id: [''],
      entidade: [''],
      texto: [''],
    });
    this.form = this.fb.group({
      titulo: ['', [Validators.required, Validators.maxLength(200)]],
      descricao: ['', [Validators.required]],
      tipo: ['ERRO' as ChamadoTipo, Validators.required],
      status: ['ABERTO' as ChamadoStatus, Validators.required],
      prioridade: ['MEDIA' as ChamadoPrioridade, Validators.required],
      impacto: ['MEDIO' as ChamadoImpacto, Validators.required],
      modulo: ['', [Validators.required, Validators.maxLength(120)]],
      menu: ['', [Validators.required, Validators.maxLength(160)]],
      cliente: ['', [Validators.required, Validators.maxLength(160)]],
      ambiente: ['PRODUCAO'],
      versao_sistema: [''],
      passos_reproducao: [''],
      resultado_atual: [''],
      resultado_esperado: [''],
      usuarios_teste: [''],
      prazo_sla_em_horas: [''],
      responsavel_usuario_id: [''],
      tags: [''],
      resposta_desenvolvedor: [''],
      respondido_por_usuario_id: [''],
    });
    this.configurarModuloMenu();
    this.carregarClientePrincipal();
    this.carregarDestinoChamados();
    this.route.data.pipe(takeUntil(this.destroy$)).subscribe((data) => {
      this.modoTela = data['perfil'] === 'desenvolvedor' ? 'desenvolvedor' : 'usuario';
      this.configurarTabs();
      this.validarPermissaoTela();
    });
    this.route.paramMap.pipe(takeUntil(this.destroy$)).subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.chamadoId = id;
        this.carregarChamado();
      } else {
        this.chamadoId = null;
      }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onBuscar(): void {
    this.router.navigate([this.rotaBase()]);
  }

  onNovo(): void {
    if (this.modoTela === 'desenvolvedor') {
      this.router.navigate(['/configuracoes/chamados-tecnicos-dev']);
      return;
    }
    this.router.navigate(['/configuracoes/chamados-tecnicos/novo']);
  }

  onCancel(): void {
    this.router.navigate([this.rotaBase()]);
  }

  onSave(): void {
    if (this.form.invalid) {
      this.popupErros = new PopupErrorBuilder()
        .adicionar('Preencha os campos obrigatórios antes de salvar.')
        .build();
      return;
    }
    const payload = this.buildPayload();
    if (this.chamadoId) {
      this.service.atualizar(this.chamadoId, payload).subscribe({
        next: () => {
          this.feedback = 'Chamado atualizado com sucesso.';
          this.carregarChamado();
        },
        error: () => {
          this.popupErros = new PopupErrorBuilder()
            .adicionar('Não foi possível atualizar o chamado.')
            .build();
        },
      });
    } else {
      this.service.criar(payload).subscribe({
        next: (novo) => {
          this.feedback = 'Chamado criado com sucesso.';
          if (novo.id) {
            this.router.navigate(['/configuracoes/chamados-tecnicos', novo.id]);
          }
        },
        error: () => {
          this.popupErros = new PopupErrorBuilder()
            .adicionar('Não foi possível criar o chamado.')
            .build();
        },
      });
    }
  }

  carregarChamado(): void {
    if (!this.chamadoId) return;
    this.carregando = true;
    this.service
      .detalhar(this.chamadoId)
      .pipe(finalize(() => (this.carregando = false)))
      .subscribe({
        next: (chamado) => {
          this.form.patchValue(chamado as any);
          this.moduloTermo = chamado.modulo || '';
          this.menuTermo = chamado.menu || '';
          this.atualizarMenuOpcoes(this.moduloTermo);
          this.carregarAbaDados();
        },
        error: () => {
          this.popupErros = new PopupErrorBuilder()
            .adicionar('Não foi possível carregar o chamado.')
            .build();
        },
      });
  }

  carregarAbaDados(): void {
    if (!this.chamadoId) return;
    this.service.listarAcoes(this.chamadoId).subscribe((data) => (this.acoes = data));
    this.service.listarComentarios(this.chamadoId).subscribe((data: ChamadoTecnicoComentario[]) => {
      this.comentarios = data;
    });
    this.service.listarAnexos(this.chamadoId).subscribe((data) => (this.anexos = data));
    this.service
      .listarAuditoriaVinculada(this.chamadoId)
      .subscribe((data) => (this.auditorias = data));
  }

  adicionarComentario(): void {
    if (!this.chamadoId || !this.comentarioTexto.trim()) return;
    const usuarioId = this.usuarioAtualId;
    this.service.adicionarComentario(this.chamadoId, this.comentarioTexto.trim(), usuarioId).subscribe({
      next: (comentario) => {
        this.comentarios = [comentario, ...this.comentarios];
        this.comentarioTexto = '';
      },
      error: () => {
        this.popupErros = new PopupErrorBuilder()
          .adicionar('Não foi possível adicionar o comentário.')
          .build();
      },
    });
  }

  selecionarArquivo(event: Event): void {
    if (!this.chamadoId) return;
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = () => {
      const conteudo = (reader.result as string) || '';
      const usuarioId = this.usuarioAtualId;
      this.service
        .adicionarAnexo(
          this.chamadoId!,
          {
            nome_arquivo: file.name,
            mime_type: file.type,
            tamanho_bytes: file.size,
            conteudo_base64: conteudo,
          },
          usuarioId
        )
        .subscribe({
          next: (anexo) => {
            this.anexos = [anexo, ...this.anexos];
          },
          error: () => {
            this.popupErros = new PopupErrorBuilder()
              .adicionar('Não foi possível adicionar o anexo.')
              .build();
          },
        });
    };
    reader.readAsDataURL(file);
  }

  buscarAuditoria(): void {
    const filtros = this.normalizeAuditoriaFiltros();
    this.service.listarAuditoria(filtros).subscribe((data) => (this.auditoriaDisponivel = data));
  }

  vincularAuditoria(evento: AuditoriaEvento): void {
    if (!this.chamadoId) return;
    const usuarioId = this.usuarioAtualId;
    this.service.vincularAuditoria(this.chamadoId, evento.id, usuarioId).subscribe({
      next: (vinculo) => {
        this.auditorias = [vinculo, ...this.auditorias];
      },
      error: () => {
        this.popupErros = new PopupErrorBuilder()
          .adicionar('Não foi possível vincular a auditoria.')
          .build();
      },
    });
  }

  changeTab(tab: AbaChamadoTecnico): void {
    this.activeTab = tab;
    if (tab === 'listagem') {
      this.carregarChamadosAbertos();
    }
  }

  get activeTabIndex(): number {
    return this.tabs.findIndex((tab) => tab.id === this.activeTab);
  }

  formatStatus(status?: string): string {
    if (!status) return '---';
    return status.replace(/_/g, ' ');
  }

  formatarTipoAcao(tipo: string): string {
    return tipo ? tipo.replace(/_/g, ' ') : '---';
  }

  detalhesAcao(acao: ChamadoTecnicoAcao): string[] {
    const detalhes: string[] = [];
    if (acao.de_status || acao.para_status) {
      detalhes.push(
        `Status: ${this.formatStatus(acao.de_status || '')} -> ${this.formatStatus(acao.para_status || '')}`
      );
    }
    if (acao.de_responsavel_id || acao.para_responsavel_id) {
      detalhes.push(
        `Responsável: ${acao.de_responsavel_id ?? '---'} -> ${acao.para_responsavel_id ?? '---'}`
      );
    }
    if (acao.criado_por_usuario_id) {
      detalhes.push(`Registrado por: ${acao.criado_por_usuario_id}`);
    }
    if (acao.tipo) {
      detalhes.unshift(`Tipo: ${this.formatarTipoAcao(acao.tipo)}`);
    }
    return detalhes;
  }

  private buildPayload(): ChamadoTecnicoPayload {
    const usuarioId = this.usuarioAtualId;
    const raw = this.form.getRawValue();
    const payload: ChamadoTecnicoPayload = {
      ...raw,
      criado_por_usuario_id: usuarioId ?? undefined,
      respondido_por_usuario_id:
        this.modoTela === 'desenvolvedor' && raw.resposta_desenvolvedor
          ? usuarioId ?? undefined
          : undefined,
      responsavel_usuario_id: raw.responsavel_usuario_id
        ? Number(raw.responsavel_usuario_id)
        : undefined,
      prazo_sla_em_horas: raw.prazo_sla_em_horas ? Number(raw.prazo_sla_em_horas) : undefined,
    };
    if (this.modoTela !== 'desenvolvedor') {
      delete payload.resposta_desenvolvedor;
      delete payload.respondido_por_usuario_id;
    }
    return payload;
  }

  private normalizeAuditoriaFiltros(): {
    data_inicio?: string;
    data_fim?: string;
    usuario_id?: string;
    entidade?: string;
    texto?: string;
  } {
    const raw = this.auditoriaFiltro.value as Record<string, string | null | undefined>;
    return {
      data_inicio: raw['data_inicio'] || undefined,
      data_fim: raw['data_fim'] || undefined,
      usuario_id: raw['usuario_id'] || undefined,
      entidade: raw['entidade'] || undefined,
      texto: raw['texto'] || undefined,
    };
  }

  get usuarioAtualId(): number | null {
    return Number(this.authService.user()?.id || 0) || null;
  }

  private configurarTabs(): void {
    const baseTabs: { id: AbaChamadoTecnico; label: string }[] = [
      { id: 'listagem', label: 'Listagem de chamados' },
      { id: 'resumo', label: 'Resumo' },
      { id: 'kanban', label: 'Kanban' },
      { id: 'historico', label: 'Histórico' },
      { id: 'comentarios', label: 'Comentários' },
      { id: 'anexos', label: 'Anexos' },
    ];
    if (this.modoTela === 'desenvolvedor') {
      baseTabs.push({ id: 'resposta', label: 'Resposta' });
      baseTabs.push({ id: 'auditoria', label: 'Auditoria' });
    }
    this.tabs = baseTabs;
    if (!this.tabs.find((tab) => tab.id === this.activeTab)) {
      this.activeTab = 'resumo';
    }
  }

  private validarPermissaoTela(): void {
    if (this.modoTela !== 'desenvolvedor') return;
    const permissoes = this.authService.user()?.permissoes ?? [];
    if (!permissoes.includes(ChamadoTecnicoDetalheComponent.PERMISSAO_DESENVOLVEDOR)) {
      this.router.navigate(['/configuracoes/chamados-tecnicos']);
    }
  }

  private rotaBase(): string {
    return this.modoTela === 'desenvolvedor'
      ? '/configuracoes/chamados-tecnicos-dev'
      : '/configuracoes/chamados-tecnicos';
  }

  private configurarModuloMenu(): void {
    this.moduloOpcoesBase = menuSections.map((section) => ({
      id: section.label,
      label: section.label,
    }));
    this.menuOpcoesBase = this.listarMenus();
    this.moduloOpcoes = [...this.moduloOpcoesBase];
    this.menuOpcoes = [...this.menuOpcoesBase];
  }

  atualizarTermoModulo(termo: string): void {
    this.moduloTermo = termo;
    this.form.patchValue({ modulo: termo });
    this.moduloOpcoes = this.filtrarOpcoes(this.moduloOpcoesBase, termo);
    this.atualizarMenuOpcoes(termo);
  }

  selecionarModulo(opcao: AutocompleteOpcao): void {
    this.moduloTermo = opcao.label;
    this.form.patchValue({ modulo: opcao.label });
    this.atualizarMenuOpcoes(opcao.label);
  }

  atualizarTermoMenu(termo: string): void {
    this.menuTermo = termo;
    this.form.patchValue({ menu: termo });
    this.menuOpcoes = this.filtrarOpcoes(this.menuOpcoesBase, termo);
  }

  selecionarMenu(opcao: AutocompleteOpcao): void {
    this.menuTermo = opcao.label;
    this.form.patchValue({ menu: opcao.label });
  }

  private atualizarMenuOpcoes(moduloSelecionado: string): void {
    const moduloNormalizado = this.normalizarTermo(moduloSelecionado);
    if (!moduloNormalizado) {
      this.menuOpcoesBase = this.listarMenus();
      this.menuOpcoes = this.filtrarOpcoes(this.menuOpcoesBase, this.menuTermo);
      return;
    }
    const filtrados = menuSections
      .filter((section) => this.normalizarTermo(section.label) === moduloNormalizado)
      .flatMap((section) =>
        (section.children ?? []).map((child) => ({
          id: child.label,
          label: child.label,
          sublabel: section.label,
        }))
      );
    this.menuOpcoesBase = filtrados.length ? filtrados : this.listarMenus();
    this.menuOpcoes = this.filtrarOpcoes(this.menuOpcoesBase, this.menuTermo);
  }

  private listarMenus(): AutocompleteOpcao[] {
    return menuSections.flatMap((section) =>
      (section.children ?? []).map((child) => ({
        id: child.label,
        label: child.label,
        sublabel: section.label,
      }))
    );
  }

  private filtrarOpcoes(opcoes: AutocompleteOpcao[], termo: string): AutocompleteOpcao[] {
    const filtrado = this.normalizarTermo(termo);
    if (!filtrado) {
      return opcoes;
    }
    return opcoes.filter((opcao) =>
      this.normalizarTermo(opcao.label).includes(filtrado)
    );
  }

  private normalizarTermo(valor: string): string {
    return (valor || '')
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .trim()
      .toLowerCase();
  }

  private carregarClientePrincipal(): void {
    this.assistanceUnitService.list().subscribe({
      next: (unidades) => {
        const principal =
          unidades.find((unidade) => unidade.unidadePrincipal) ?? unidades[0];
        const nome = principal?.nomeFantasia || principal?.razaoSocial;
        if (nome && !this.form.get('cliente')?.value) {
          this.form.patchValue({ cliente: nome });
        }
      },
      error: () => {
        this.assistanceUnitService.get().subscribe({
          next: (response) => {
            const nome = response.unidade?.nomeFantasia || response.unidade?.razaoSocial;
            if (nome && !this.form.get('cliente')?.value) {
              this.form.patchValue({ cliente: nome });
            }
          },
        });
      },
    });
  }

  private carregarDestinoChamados(): void {
    this.configService.getDestinoChamados().subscribe({
      next: (response) => {
        this.destinoChamado = response?.destino || this.destinoChamado;
      },
    });
  }

  private carregarChamadosAbertos(): void {
    if (this.carregouAbertos || this.carregandoAbertos) return;
    this.carregandoAbertos = true;
    this.service
      .listar({ status: 'ABERTO', tamanho_pagina: 50 })
      .pipe(finalize(() => (this.carregandoAbertos = false)))
      .subscribe({
        next: (response) => {
          this.chamadosAbertos = response?.chamados ?? [];
          this.carregouAbertos = true;
        },
        error: () => {
          this.feedback = 'Não foi possível carregar os chamados abertos.';
        },
      });
  }
}

