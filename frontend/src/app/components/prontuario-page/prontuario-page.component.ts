import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Subject, debounceTime, distinctUntilChanged, takeUntil } from 'rxjs';
import { BeneficiarioApiPayload, BeneficiarioApiService } from '../../services/beneficiario-api.service';
import {
  BeneficiarioResumo,
  ProntuarioAnexoRequest,
  ProntuarioFiltro,
  ProntuarioRegistroRequest,
  ProntuarioRegistroResponse,
  ProntuarioResumoResponse,
  ProntuarioService
} from '../../services/prontuario.service';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';
import { ProntuarioFiltrosComponent } from '../prontuario-filtros/prontuario-filtros.component';
import { ProntuarioTimelineComponent } from '../prontuario-timeline/prontuario-timeline.component';
import { ProntuarioFormModalComponent } from '../prontuario-form-modal/prontuario-form-modal.component';
import { ProntuarioIndicadoresComponent } from '../prontuario-indicadores/prontuario-indicadores.component';
import { AutocompleteOpcao } from '../compartilhado/autocomplete/autocomplete.component';
import { DialogComponent } from '../compartilhado/dialog/dialog.component';

@Component({
  selector: 'app-prontuario-page',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    TelaPadraoComponent,
    PopupMessagesComponent,
    ProntuarioFiltrosComponent,
    ProntuarioTimelineComponent,
    ProntuarioFormModalComponent,
    ProntuarioIndicadoresComponent,
    DialogComponent
  ],
  templateUrl: './prontuario-page.component.html',
  styleUrl: './prontuario-page.component.scss'
})
export class ProntuarioPageComponent implements OnInit, OnDestroy {
  beneficiarioId: number | null = null;
  resumo: ProntuarioResumoResponse | null = null;
  registros: ProntuarioRegistroResponse[] = [];
  totalRegistros = 0;
  carregandoResumo = false;
  carregandoRegistros = false;
  popupErros: string[] = [];

  filtros: ProntuarioFiltro = { page: 0, pageSize: 10 };
  termoBusca = '';
  opcoesBeneficiarios: AutocompleteOpcao[] = [];
  carregandoBeneficiarios = false;
  erroBeneficiarios: string | null = null;

  modalAberto = false;
  registroEmEdicao: ProntuarioRegistroResponse | null = null;
  tipoNovoRegistro: ProntuarioRegistroRequest['tipo'] | null = null;
  dialogoExcluirAberto = false;
  registroParaExcluir: ProntuarioRegistroResponse | null = null;

  modoImpressao: 'resumo' | 'completo' | null = null;

  abas = [
    { id: 'linha', label: 'Linha do tempo', tipo: '' },
    { id: 'atendimentos', label: 'Atendimentos', tipo: 'atendimento' },
    { id: 'procedimentos', label: 'Procedimentos', tipo: 'procedimento' },
    { id: 'evolucoes', label: 'Evoluções', tipo: 'evolucao' },
    { id: 'encaminhamentos', label: 'Encaminhamentos', tipo: 'encaminhamento' },
    { id: 'documentos', label: 'Documentos/Anexos', tipo: 'documento' },
    { id: 'indicadores', label: 'Indicadores', tipo: '' }
  ];
  abaAtiva = 'linha';

  get abaAtivaIndex(): number {
    return this.abas.findIndex((aba) => aba.id === this.abaAtiva);
  }

  private buscarBeneficiario$ = new Subject<string>();
  private destroy$ = new Subject<void>();

  acoesTopo = {
    salvar: true,
    excluir: true,
    imprimir: true,
    novo: true,
    cancelar: true
  };

  acoesDesabilitadas = {
    salvar: true,
    excluir: true
  };

  constructor(
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly prontuarioService: ProntuarioService,
    private readonly beneficiarioService: BeneficiarioApiService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.pipe(takeUntil(this.destroy$)).subscribe((params) => {
      const id = params.get('beneficiarioId');
      this.beneficiarioId = id ? Number(id) : null;
      if (this.beneficiarioId) {
        this.carregarResumo();
        this.carregarRegistros(true);
      } else {
        this.resumo = null;
        this.registros = [];
        this.totalRegistros = 0;
      }
    });

    this.buscarBeneficiario$
      .pipe(debounceTime(400), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe((termo) => this.buscarBeneficiarios(termo));
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onTermoBuscaChange(valor: string): void {
    this.termoBusca = valor;
    if (!valor || valor.length < 3) {
      this.opcoesBeneficiarios = [];
      return;
    }
    this.buscarBeneficiario$.next(valor);
  }

  selecionarBeneficiario(opcao: AutocompleteOpcao): void {
    this.router.navigate(['/atendimentos/prontuario', opcao.id]);
  }

  atualizarFiltros(filtros: ProntuarioFiltro): void {
    this.filtros = { ...filtros, page: 0, pageSize: this.filtros.pageSize ?? 10 };
    if (this.beneficiarioId) {
      this.carregarRegistros(true);
    }
  }

  limparFiltros(): void {
    this.filtros = { page: 0, pageSize: this.filtros.pageSize ?? 10 };
    if (this.beneficiarioId) {
      this.carregarRegistros(true);
    }
  }

  selecionarAba(id: string, tipo: string): void {
    this.abaAtiva = id;
    if (id === 'linha') {
      this.atualizarFiltros({ ...this.filtros, tipo: '' });
      return;
    }
    if (id === 'indicadores') {
      return;
    }
    this.atualizarFiltros({ ...this.filtros, tipo });
  }

  abrirNovoRegistro(tipo?: ProntuarioRegistroRequest['tipo']): void {
    this.registroEmEdicao = null;
    this.tipoNovoRegistro = tipo ?? this.obterTipoPorAba();
    this.modalAberto = true;
  }

  editarRegistro(registro: ProntuarioRegistroResponse): void {
    this.registroEmEdicao = registro;
    this.tipoNovoRegistro = null;
    this.modalAberto = true;
  }

  abrirDialogoExcluir(registro: ProntuarioRegistroResponse): void {
    this.registroParaExcluir = registro;
    this.dialogoExcluirAberto = true;
  }

  fecharDialogoExcluir(): void {
    this.dialogoExcluirAberto = false;
    this.registroParaExcluir = null;
  }

  fecharModal(): void {
    this.modalAberto = false;
  }

  salvarRegistro(payload: { registro: ProntuarioRegistroRequest; anexo?: ProntuarioAnexoRequest }): void {
    if (!this.beneficiarioId) {
      return;
    }
    this.popupErros = [];
    const request$ = this.registroEmEdicao
      ? this.prontuarioService.atualizarRegistro(this.registroEmEdicao.id, payload.registro)
      : this.prontuarioService.criarRegistro(this.beneficiarioId, payload.registro);
    request$.subscribe({
      next: (registro) => {
        if (payload.anexo) {
          this.prontuarioService.adicionarAnexo(registro.id, payload.anexo).subscribe();
        }
        this.modalAberto = false;
        this.carregarResumo();
        this.carregarRegistros(true);
      },
      error: () => {
        this.popupErros = ['Não foi possível salvar o registro do prontuário.'];
      }
    });
  }

  removerRegistroConfirmado(): void {
    const registro = this.registroParaExcluir;
    if (!registro?.id) {
      return;
    }
    this.prontuarioService.removerRegistro(registro.id).subscribe({
      next: () => {
        this.carregarResumo();
        this.carregarRegistros(true);
        this.fecharDialogoExcluir();
      },
      error: () => {
        this.popupErros = ['Não foi possível remover o registro do prontuário.'];
        this.fecharDialogoExcluir();
      }
    });
  }

  carregarMais(): void {
    if (!this.beneficiarioId) {
      return;
    }
    this.filtros = { ...this.filtros, page: (this.filtros.page ?? 0) + 1 };
    this.carregarRegistros(false);
  }

  imprimirResumo(): void {
    this.modoImpressao = 'resumo';
    setTimeout(() => {
      window.print();
      this.modoImpressao = null;
    }, 200);
  }

  imprimirCompleto(): void {
    this.modoImpressao = 'completo';
    setTimeout(() => {
      window.print();
      this.modoImpressao = null;
    }, 200);
  }

  cancelarSelecao(): void {
    this.router.navigate(['/atendimentos/prontuario']);
  }

  private obterTipoPorAba(): ProntuarioRegistroRequest['tipo'] | null {
    const aba = this.abas.find((item) => item.id === this.abaAtiva);
    return (aba?.tipo as ProntuarioRegistroRequest['tipo']) || 'atendimento';
  }

  private carregarResumo(): void {
    if (!this.beneficiarioId) {
      return;
    }
    this.carregandoResumo = true;
    this.prontuarioService.obterResumo(this.beneficiarioId).subscribe({
      next: (resumo) => {
        this.resumo = resumo;
        this.carregandoResumo = false;
      },
      error: () => {
        this.popupErros = ['Não foi possível carregar o resumo do prontuário.'];
        this.carregandoResumo = false;
      }
    });
  }

  private carregarRegistros(reset: boolean): void {
    if (!this.beneficiarioId) {
      return;
    }
    this.carregandoRegistros = true;
    const filtros = { ...this.filtros };
    this.prontuarioService.listarRegistros(this.beneficiarioId, filtros).subscribe({
      next: (lista) => {
        this.totalRegistros = lista.total;
        this.registros = reset ? lista.registros : [...this.registros, ...lista.registros];
        this.carregandoRegistros = false;
      },
      error: () => {
        this.popupErros = ['Não foi possível carregar os registros do prontuário.'];
        this.carregandoRegistros = false;
      }
    });
  }

  private buscarBeneficiarios(termo: string): void {
    this.carregandoBeneficiarios = true;
    this.erroBeneficiarios = null;
    this.beneficiarioService.list({ nome: termo }).subscribe({
      next: ({ beneficiarios }) => {
        this.opcoesBeneficiarios = (beneficiarios ?? []).map((item: BeneficiarioApiPayload) => ({
          id: item.id_beneficiario ?? '',
          label: item.nome_completo,
          sublabel: item.cpf || item.codigo || ''
        }));
        this.carregandoBeneficiarios = false;
      },
      error: () => {
        this.erroBeneficiarios = 'Não foi possível buscar beneficiarios.';
        this.carregandoBeneficiarios = false;
      }
    });
  }

  get beneficiarioSelecionado(): BeneficiarioResumo | null {
    return this.resumo?.beneficiario ?? null;
  }
}
