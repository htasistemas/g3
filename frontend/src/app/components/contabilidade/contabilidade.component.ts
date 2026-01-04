import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { Router } from '@angular/router';
import { PopupErrorBuilder } from '../../utils/popup-error.builder';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';
import { DialogComponent } from '../compartilhado/dialog/dialog.component';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { ConfigAcoesCrud, EstadoAcoesCrud, TelaBaseComponent } from '../compartilhado/tela-base.component';
import {
  ContabilidadeService,
  ContaBancariaRequest,
  ContaBancariaResponse,
  EmendaImpositivaRequest,
  EmendaImpositivaResponse,
  LancamentoFinanceiroRequest,
  LancamentoFinanceiroResponse,
  MovimentacaoFinanceiraRequest,
  MovimentacaoFinanceiraResponse,
  ReciboPagamentoResponse
} from '../../services/contabilidade.service';
import {
  faArrowTrendDown,
  faArrowTrendUp,
  faBell,
  faChartLine,
  faCircleCheck,
  faCircleDot,
  faCircleExclamation,
  faFileInvoice,
  faMoneyBillTransfer,
  faPiggyBank,
  faPrint,
  faWallet
} from '@fortawesome/free-solid-svg-icons';

interface MetricCard {
  label: string;
  value: string;
  icon: any;
  helper?: string;
  trend?: 'up' | 'down';
}

interface CashFlowEntry {
  period: string;
  receivable: number;
  payable: number;
  projection: number;
}

interface FinanceTab {
  id: 'visao-geral' | 'contas' | 'lancamentos' | 'movimentacoes' | 'emendas' | 'relatorios';
  label: string;
  badge?: string;
}

@Component({
  selector: 'app-contabilidade',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    FontAwesomeModule,
    TelaPadraoComponent,
    PopupMessagesComponent,
    DialogComponent
  ],
  templateUrl: './contabilidade.component.html',
  styleUrl: './contabilidade.component.scss'
})
export class ContabilidadeComponent extends TelaBaseComponent implements OnInit {
  readonly faPrint = faPrint;
  readonly faBell = faBell;
  readonly faFileInvoice = faFileInvoice;
  readonly faCircleCheck = faCircleCheck;
  readonly faCircleExclamation = faCircleExclamation;
  readonly faCircleDot = faCircleDot;
  readonly faMoneyBillTransfer = faMoneyBillTransfer;
  readonly faChartLine = faChartLine;
  readonly faWallet = faWallet;
  readonly faPiggyBank = faPiggyBank;
  readonly faArrowTrendUp = faArrowTrendUp;
  readonly faArrowTrendDown = faArrowTrendDown;

  readonly acoesToolbar: Required<ConfigAcoesCrud> = this.criarConfigAcoes({
    salvar: true,
    excluir: true,
    novo: true,
    cancelar: true,
    imprimir: true
  });

  popupErros: string[] = [];
  private popupTimeout?: ReturnType<typeof setTimeout>;
  dialogConfirmacaoAberta = false;
  dialogTitulo = 'Confirmar acao';
  dialogMensagem = 'Deseja continuar?';
  dialogConfirmarLabel = 'Confirmar';
  dialogCancelarLabel = 'Cancelar';
  private dialogAcao?: () => void;

  tabs: FinanceTab[] = [
    {
      id: 'visao-geral',
      label: 'Visao geral financeira',
      badge: ''
    },
    {
      id: 'contas',
      label: 'Contas bancarias',
      badge: ''
    },
    {
      id: 'lancamentos',
      label: 'Lancamentos',
      badge: ''
    },
    {
      id: 'movimentacoes',
      label: 'Movimentacoes',
      badge: ''
    },
    {
      id: 'emendas',
      label: 'Emendas impositivas',
      badge: ''
    },
    {
      id: 'relatorios',
      label: 'Relatorios e documentos',
      badge: ''
    }
  ];

  bancosDisponiveis = [
    'Banco do Brasil',
    'Caixa Economica Federal',
    'Itau',
    'Bradesco',
    'Santander',
    'Banrisul',
    'Banco do Nordeste',
    'Banco da Amazonia',
    'Sicredi',
    'Sicoob',
    'Banco Inter',
    'Nubank',
    'C6 Bank',
    'Banco Original',
    'Banco Pan',
    'Neon',
    'Next',
    'PagBank',
    'Mercado Pago',
    'PicPay',
    'BTG Pactual',
    'Safra',
    'XP',
    'Daycoval',
    'Banco BMG'
  ];

  tiposConta = [
    { value: 'corrente', label: 'Conta corrente' },
    { value: 'poupanca', label: 'Conta poupanca' },
    { value: 'salario', label: 'Conta salario' },
    { value: 'pagamento', label: 'Conta pagamento' },
    { value: 'investimento', label: 'Conta investimento' },
    { value: 'digital', label: 'Conta digital' },
    { value: 'conjunta', label: 'Conta conjunta' },
    { value: 'universitaria', label: 'Conta universitaria' },
    { value: 'empresarial', label: 'Conta empresarial' },
    { value: 'projeto', label: 'Conta vinculada a projeto' }
  ];

  tiposChavePix = [
    { value: 'cnpj', label: 'CNPJ' },
    { value: 'email', label: 'Email' },
    { value: 'telefone', label: 'Telefone' },
    { value: 'aleatoria', label: 'Aleatoria' }
  ];

  activeTab: FinanceTab['id'] = 'visao-geral';

  summaryCards: MetricCard[] = [];
  cashFlow: CashFlowEntry[] = [];
  agenda: LancamentoFinanceiroResponse[] = [];
  bankAccounts: ContaBancariaResponse[] = [];
  financialMovements: MovimentacaoFinanceiraResponse[] = [];
  amendmentControls: EmendaImpositivaResponse[] = [];
  upcomingReceivables: LancamentoFinanceiroResponse[] = [];
  upcomingPayables: LancamentoFinanceiroResponse[] = [];
  alerts: string[] = [];
  reciboPagamento: ReciboPagamentoResponse | null = null;

  filtrosContas = {
    banco: '',
    agencia: '',
    numero: '',
    tipo: ''
  };

  filtrosLancamentos = {
    descricao: '',
    contraparte: '',
    tipo: '',
    situacao: ''
  };

  filtroResumoTipo: '' | 'receber' | 'pagar' = '';

  filtrosMovimentacoes = {
    descricao: '',
    categoria: '',
    tipo: ''
  };

  filtrosEmendas = {
    identificacao: '',
    status: ''
  };

  newEntry: LancamentoFinanceiroRequest = {
    tipo: 'receber',
    descricao: '',
    contraparte: '',
    vencimento: this.todayISO(),
    valor: 0,
    situacao: 'aberto'
  };
  valorLancamentoMascara = 'R$ 0,00';
  lancamentoEmEdicaoId: number | null = null;
  salvandoLancamento = false;

  contaEmEdicaoId: number | null = null;
  saldoContaMascara = '';

  newAmendment: EmendaImpositivaRequest = {
    identificacao: '',
    referenciaLegal: '',
    dataPrevista: this.todayISO(),
    valorPrevisto: 0,
    diasAlerta: 15,
    status: 'previsto',
    observacoes: ''
  };

  newMovement: MovimentacaoFinanceiraRequest = {
    descricao: '',
    contraparte: '',
    dataMovimentacao: this.todayISO(),
    contaBancariaId: undefined,
    valor: 0,
    tipo: 'entrada',
    categoria: 'Operacional'
  };

  reportTotals = {
    recebimentos: 0,
    pagamentos: 0,
    saldoProjetado: 0,
    saldoAReceber: 0
  };

  constructor(
    private readonly contabilidadeService: ContabilidadeService,
    private readonly changeDetector: ChangeDetectorRef,
    private readonly router: Router
  ) {
    super();
  }

  ngOnInit(): void {
    this.carregarDados();
    this.saldoContaMascara = this.formatarValorMonetario(this.newAccount.saldo);
  }

  get acoesDesabilitadas(): EstadoAcoesCrud {
    const bloqueado = this.salvandoLancamento;
    return {
      salvar: bloqueado,
      excluir: bloqueado,
      novo: bloqueado,
      cancelar: bloqueado,
      imprimir: false
    };
  }

  private carregarDados(): void {
    this.contabilidadeService.listarContasBancarias().subscribe((lista) => {
      this.bankAccounts = lista ?? [];
      this.refreshPanels();
      this.changeDetector.detectChanges();
    });
    this.carregarLancamentos();
    this.contabilidadeService.listarMovimentacoes().subscribe((lista) => {
      this.financialMovements = lista ?? [];
      this.refreshPanels();
      this.changeDetector.detectChanges();
    });
    this.contabilidadeService.listarEmendas().subscribe((lista) => {
      this.amendmentControls = lista ?? [];
      this.refreshPanels();
      this.changeDetector.detectChanges();
    });
  }

  private carregarLancamentos(): void {
    this.contabilidadeService.listarLancamentos().subscribe((lista) => {
      this.agenda = lista ?? [];
      this.refreshPanels();
      this.changeDetector.detectChanges();
    });
  }

  get activeTabIndex(): number {
    return this.tabs.findIndex((tab) => tab.id === this.activeTab);
  }

  get hasPreviousTab(): boolean {
    return this.activeTabIndex > 0;
  }

  get hasNextTab(): boolean {
    return this.activeTabIndex < this.tabs.length - 1;
  }

  get nextTabLabel(): string {
    return this.hasNextTab ? this.tabs[this.activeTabIndex + 1].label : '';
  }

  get previousTabLabel(): string {
    return this.hasPreviousTab ? this.tabs[this.activeTabIndex - 1].label : '';
  }

  goToPreviousTab(): void {
    if (this.hasPreviousTab) {
      this.changeTab(this.tabs[this.activeTabIndex - 1].id);
    }
  }

  goToNextTab(): void {
    if (this.hasNextTab) {
      this.changeTab(this.tabs[this.activeTabIndex + 1].id);
    }
  }

  get counterpartySuggestions(): string[] {
    const fromEntries = this.agenda.map((item) => item.contraparte);
    const fromMovements = this.financialMovements.map((item) => item.contraparte || '');
    return Array.from(new Set([...fromEntries, ...fromMovements])).filter(Boolean);
  }

  get contasFiltradas(): ContaBancariaResponse[] {
    const filtradas = this.bankAccounts.filter((conta) => {
      const banco = this.normalizeString(conta.banco);
      const numero = this.normalizeString(conta.numero);
      const agencia = this.normalizeString(conta.agencia || '');
      const tipo = this.normalizeString(conta.tipo);
      return (
        (!this.filtrosContas.banco || banco.includes(this.normalizeString(this.filtrosContas.banco))) &&
        (!this.filtrosContas.agencia || agencia.includes(this.normalizeString(this.filtrosContas.agencia))) &&
        (!this.filtrosContas.numero || numero.includes(this.normalizeString(this.filtrosContas.numero))) &&
        (!this.filtrosContas.tipo || tipo === this.normalizeString(this.filtrosContas.tipo))
      );
    });

    const ordemTipo: Record<string, number> = {
      corrente: 0,
      projeto: 1
    };

    return filtradas.slice().sort((a, b) => {
      const ordemA = ordemTipo[a.tipo] ?? 2;
      const ordemB = ordemTipo[b.tipo] ?? 2;
      if (ordemA !== ordemB) {
        return ordemA - ordemB;
      }
      return this.normalizeString(a.banco).localeCompare(this.normalizeString(b.banco));
    });
  }

  get lancamentosFiltrados(): LancamentoFinanceiroResponse[] {
    return this.agenda.filter((item) => {
      const descricao = this.normalizeString(item.descricao);
      const contraparte = this.normalizeString(item.contraparte);
      const tipo = this.normalizeString(item.tipo);
      const situacao = this.normalizeString(item.situacao);
      return (
        (!this.filtrosLancamentos.descricao ||
          descricao.includes(this.normalizeString(this.filtrosLancamentos.descricao))) &&
        (!this.filtrosLancamentos.contraparte ||
          contraparte.includes(this.normalizeString(this.filtrosLancamentos.contraparte))) &&
        (!this.filtrosLancamentos.tipo || tipo === this.normalizeString(this.filtrosLancamentos.tipo)) &&
        (!this.filtrosLancamentos.situacao || situacao === this.normalizeString(this.filtrosLancamentos.situacao))
      );
    });
  }

  get movimentacoesFiltradas(): MovimentacaoFinanceiraResponse[] {
    return this.financialMovements.filter((item) => {
      const descricao = this.normalizeString(item.descricao);
      const categoria = this.normalizeString(item.categoria || '');
      const tipo = this.normalizeString(item.tipo);
      return (
        (!this.filtrosMovimentacoes.descricao ||
          descricao.includes(this.normalizeString(this.filtrosMovimentacoes.descricao))) &&
        (!this.filtrosMovimentacoes.categoria ||
          categoria.includes(this.normalizeString(this.filtrosMovimentacoes.categoria))) &&
        (!this.filtrosMovimentacoes.tipo || tipo === this.normalizeString(this.filtrosMovimentacoes.tipo))
      );
    });
  }

  get emendasFiltradas(): EmendaImpositivaResponse[] {
    return this.amendmentControls.filter((item) => {
      const identificacao = this.normalizeString(item.identificacao);
      const status = this.normalizeString(item.status);
      return (
        (!this.filtrosEmendas.identificacao ||
          identificacao.includes(this.normalizeString(this.filtrosEmendas.identificacao))) &&
        (!this.filtrosEmendas.status || status === this.normalizeString(this.filtrosEmendas.status))
      );
    });
  }

  changeTab(tabId: FinanceTab['id']): void {
    this.activeTab = tabId;
    if (tabId === 'visao-geral') {
      this.carregarLancamentos();
    }
  }

  registerEntry(): void {
    this.onValorLancamentoInput(this.valorLancamentoMascara);
    if (
      !this.newEntry.descricao ||
      !this.newEntry.contraparte ||
      !this.newEntry.vencimento ||
      !this.newEntry.valor
    ) {
      this.popupErros = new PopupErrorBuilder()
        .adicionar('Preencha os campos obrigatorios do lancamento.')
        .build();
      this.abrirPopupTemporario();
      return;
    }
    if (this.salvandoLancamento) {
      return;
    }
    this.salvandoLancamento = true;
    const request$ = this.lancamentoEmEdicaoId
      ? this.contabilidadeService.atualizarLancamento(this.lancamentoEmEdicaoId, this.newEntry)
      : this.contabilidadeService.criarLancamento(this.newEntry);
    const subscription = request$.subscribe((response) => {
      if (this.lancamentoEmEdicaoId) {
        this.agenda = this.agenda.map((item) => (item.id === response.id ? response : item));
      } else {
        this.agenda = [...this.agenda, response];
      }
      this.newEntry = {
        tipo: 'receber',
        descricao: '',
        contraparte: '',
        vencimento: this.todayISO(),
        valor: 0,
        situacao: 'aberto'
      };
      this.valorLancamentoMascara = this.formatarValorMonetario(0);
      this.lancamentoEmEdicaoId = null;
      this.salvandoLancamento = false;
      this.refreshPanels();
    });
    subscription.add(() => {
      this.salvandoLancamento = false;
    });
  }

  editarLancamento(item: LancamentoFinanceiroResponse): void {
    this.lancamentoEmEdicaoId = item.id;
    this.newEntry = {
      tipo: item.tipo,
      descricao: item.descricao,
      contraparte: item.contraparte,
      vencimento: item.vencimento,
      valor: Number(item.valor || 0),
      situacao: item.situacao
    };
    this.valorLancamentoMascara = this.formatarValorMonetario(Number(item.valor || 0));
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  cancelarEdicaoLancamento(): void {
    this.newEntry = {
      tipo: 'receber',
      descricao: '',
      contraparte: '',
      vencimento: this.todayISO(),
      valor: 0,
      situacao: 'aberto'
    };
    this.valorLancamentoMascara = this.formatarValorMonetario(0);
    this.lancamentoEmEdicaoId = null;
  }

  novoLancamento(): void {
    this.cancelarEdicaoLancamento();
  }

  onValorLancamentoInput(valor: string): void {
    const apenasNumeros = (valor ?? '').replace(/\D/g, '');
    const numero = Number(apenasNumeros || 0) / 100;
    this.newEntry = { ...this.newEntry, valor: numero };
    this.valorLancamentoMascara = this.formatarValorMonetario(numero);
  }

  newAccount: ContaBancariaRequest = {
    banco: '',
    agencia: '',
    numero: '',
    tipo: 'corrente',
    projetoVinculado: '',
    pixVinculado: false,
    tipoChavePix: '',
    chavePix: '',
    saldo: 0,
    dataAtualizacao: this.todayISO()
  };
  erroChavePix: string | null = null;

  registerMovement(): void {
    if (!this.newMovement.descricao || !this.newMovement.contaBancariaId || !this.newMovement.dataMovimentacao) {
      this.popupErros = new PopupErrorBuilder()
        .adicionar('Preencha os campos obrigatorios da movimentacao.')
        .build();
      this.abrirPopupTemporario();
      return;
    }
    this.contabilidadeService.criarMovimentacao(this.newMovement).subscribe((response) => {
      this.financialMovements = [...this.financialMovements, response];
      this.contabilidadeService.listarContasBancarias().subscribe((contas) => {
        this.bankAccounts = contas ?? [];
        this.refreshPanels();
      });
      this.newMovement = {
        descricao: '',
        contraparte: '',
        dataMovimentacao: this.todayISO(),
        contaBancariaId: undefined,
        valor: 0,
        tipo: 'entrada',
        categoria: 'Operacional'
      };
    });
  }

  addBankAccount(): void {
    if (!this.newAccount.banco || !this.newAccount.numero) {
      this.popupErros = new PopupErrorBuilder()
        .adicionar('Informe banco e numero da conta.')
        .build();
      this.abrirPopupTemporario();
      return;
    }
    if (this.newAccount.tipo === 'projeto' && !this.newAccount.projetoVinculado) {
      this.popupErros = new PopupErrorBuilder()
        .adicionar('Informe o projeto vinculado para conta de projeto.')
        .build();
      this.abrirPopupTemporario();
      return;
    }
    this.erroChavePix = this.validarChavePix();
    if (this.erroChavePix) {
      this.popupErros = new PopupErrorBuilder().adicionar(this.erroChavePix).build();
      this.abrirPopupTemporario();
      return;
    }
    const payload = { ...this.newAccount };
    const request$ = this.contaEmEdicaoId
      ? this.contabilidadeService.atualizarContaBancaria(this.contaEmEdicaoId, payload)
      : this.contabilidadeService.criarContaBancaria(payload);
    request$.subscribe((response) => {
      if (this.contaEmEdicaoId) {
        this.bankAccounts = this.bankAccounts.map((conta) => (conta.id === response.id ? response : conta));
      } else {
        this.bankAccounts = [...this.bankAccounts, response];
      }
      this.resetContaBancariaForm();
      this.refreshPanels();
    });
  }

  editarContaBancaria(conta: ContaBancariaResponse): void {
    this.contaEmEdicaoId = conta.id;
    this.newAccount = {
      banco: conta.banco,
      agencia: conta.agencia || '',
      numero: conta.numero,
      tipo: conta.tipo,
      projetoVinculado: conta.projetoVinculado || '',
      pixVinculado: Boolean(conta.pixVinculado),
      tipoChavePix: conta.tipoChavePix || '',
      chavePix: conta.chavePix || '',
      saldo: Number(conta.saldo || 0),
      dataAtualizacao: conta.dataAtualizacao
    };
    this.saldoContaMascara = this.formatarValorMonetario(Number(conta.saldo || 0));
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  cancelarEdicaoContaBancaria(): void {
    this.resetContaBancariaForm();
  }

  removerContaBancaria(conta: ContaBancariaResponse): void {
    if (!conta.id) {
      return;
    }
    this.abrirDialogoConfirmacao(
      'Excluir conta bancaria',
      'Deseja excluir esta conta bancaria? Esta acao nao pode ser desfeita.',
      'Excluir',
      () => {
        this.contabilidadeService.removerContaBancaria(conta.id).subscribe(() => {
          this.bankAccounts = this.bankAccounts.filter((item) => item.id !== conta.id);
          if (this.contaEmEdicaoId === conta.id) {
            this.resetContaBancariaForm();
          }
          this.refreshPanels();
        });
      }
    );
  }

  onSaldoContaInput(valor: string): void {
    const apenasNumeros = (valor ?? '').replace(/\D/g, '');
    const numero = Number(apenasNumeros || 0) / 100;
    this.newAccount = { ...this.newAccount, saldo: numero };
    this.saldoContaMascara = this.formatarValorMonetario(numero);
  }

  addAmendment(): void {
    if (!this.newAmendment.identificacao || !this.newAmendment.dataPrevista || !this.newAmendment.valorPrevisto) {
      this.popupErros = new PopupErrorBuilder()
        .adicionar('Preencha os campos obrigatorios da emenda.')
        .build();
      this.abrirPopupTemporario();
      return;
    }
    this.contabilidadeService.criarEmenda(this.newAmendment).subscribe((response) => {
      this.amendmentControls = [...this.amendmentControls, response];
      this.newAmendment = {
        identificacao: '',
        referenciaLegal: '',
        dataPrevista: this.todayISO(),
        valorPrevisto: 0,
        diasAlerta: 15,
        status: 'previsto',
        observacoes: ''
      };
      this.refreshPanels();
    });
  }

  setEntryStatus(entry: LancamentoFinanceiroResponse, status: string): void {
    if (!entry.id) return;
    const mensagem =
      status === 'pago'
        ? 'Confirmar registro de pagamento deste lancamento?'
        : 'Confirmar marcacao de atraso deste lancamento?';
    this.abrirDialogoConfirmacao('Confirmar alteracao', mensagem, 'Confirmar', () => {
      this.contabilidadeService.atualizarSituacaoLancamento(entry.id, status).subscribe((response) => {
        this.agenda = this.agenda.map((item) => (item.id === entry.id ? response : item));
        this.refreshPanels();
      });
    });
  }

  pagarLancamento(entry: LancamentoFinanceiroResponse): void {
    if (!entry.id) return;
    this.abrirDialogoConfirmacao('Confirmar pagamento', 'Confirmar pagamento deste lancamento?', 'Confirmar', () => {
      this.contabilidadeService.pagarLancamento(entry.id).subscribe((response) => {
        this.reciboPagamento = response;
        this.agenda = this.agenda.map((item) =>
          item.id === entry.id ? { ...item, situacao: 'pago' } : item
        );
        this.refreshPanels();
        this.contabilidadeService.listarMovimentacoes().subscribe((lista) => {
          this.financialMovements = lista ?? [];
          this.refreshPanels();
        });
        this.contabilidadeService.listarContasBancarias().subscribe((contas) => {
          this.bankAccounts = contas ?? [];
          this.refreshPanels();
        });
        window.print();
      });
    });
  }

  setAmendmentStatus(amendment: EmendaImpositivaResponse, status: string): void {
    if (!amendment.id) return;
    this.contabilidadeService.atualizarStatusEmenda(amendment.id, status).subscribe((response) => {
      this.amendmentControls = this.amendmentControls.map((item) => (item.id === amendment.id ? response : item));
      this.refreshPanels();
    });
  }

  printResumo(): void {
    window.print();
  }

  onSalvar(): void {
    switch (this.activeTab) {
      case 'visao-geral':
      case 'lancamentos':
        this.registerEntry();
        return;
      case 'contas':
        this.addBankAccount();
        return;
      case 'movimentacoes':
        this.registerMovement();
        return;
      case 'emendas':
        this.addAmendment();
        return;
      default:
        this.popupErros = new PopupErrorBuilder()
          .adicionar('Nada para salvar nesta aba.')
          .build();
        this.abrirPopupTemporario();
    }
  }

  onExcluir(): void {
    if (this.activeTab === 'contas') {
      const conta = this.bankAccounts.find((item) => item.id === this.contaEmEdicaoId);
      if (!conta) {
        this.popupErros = new PopupErrorBuilder()
          .adicionar('Selecione uma conta para excluir.')
          .build();
        this.abrirPopupTemporario();
        return;
      }
      this.removerContaBancaria(conta);
      return;
    }

    this.popupErros = new PopupErrorBuilder()
      .adicionar('Exclusao disponivel apenas para contas bancarias.')
      .build();
    this.abrirPopupTemporario();
  }

  onNovo(): void {
    switch (this.activeTab) {
      case 'visao-geral':
      case 'lancamentos':
        this.novoLancamento();
        return;
      case 'contas':
        this.resetContaBancariaForm();
        return;
      case 'movimentacoes':
        this.newMovement = {
          descricao: '',
          contraparte: '',
          dataMovimentacao: this.todayISO(),
          contaBancariaId: undefined,
          valor: 0,
          tipo: 'entrada',
          categoria: 'Operacional'
        };
        return;
      case 'emendas':
        this.newAmendment = {
          identificacao: '',
          referenciaLegal: '',
          dataPrevista: this.todayISO(),
          valorPrevisto: 0,
          diasAlerta: 15,
          status: 'previsto',
          observacoes: ''
        };
        return;
      default:
        this.popupErros = new PopupErrorBuilder()
          .adicionar('Nada para limpar nesta aba.')
          .build();
        this.abrirPopupTemporario();
    }
  }

  onCancelar(): void {
    if (this.activeTab === 'visao-geral' || this.activeTab === 'lancamentos') {
      this.cancelarEdicaoLancamento();
      return;
    }
    if (this.activeTab === 'contas') {
      this.cancelarEdicaoContaBancaria();
      return;
    }
    this.onNovo();
  }

  onImprimir(): void {
    this.printResumo();
  }

  onFechar(): void {
    this.router.navigate(['/financeiro/contabilidade']);
  }

  fecharPopupErros(): void {
    this.popupErros = [];
    if (this.popupTimeout) {
      clearTimeout(this.popupTimeout);
      this.popupTimeout = undefined;
    }
  }

  private abrirPopupTemporario(): void {
    if (this.popupTimeout) {
      clearTimeout(this.popupTimeout);
    }
    this.popupTimeout = setTimeout(() => {
      this.fecharPopupErros();
    }, 10000);
  }

  abrirDialogoConfirmacao(titulo: string, mensagem: string, confirmarLabel: string, acao: () => void): void {
    this.dialogTitulo = titulo;
    this.dialogMensagem = mensagem;
    this.dialogConfirmarLabel = confirmarLabel;
    this.dialogAcao = acao;
    this.dialogConfirmacaoAberta = true;
  }

  confirmarDialogo(): void {
    const acao = this.dialogAcao;
    this.dialogConfirmacaoAberta = false;
    this.dialogAcao = undefined;
    if (acao) {
      acao();
    }
  }

  cancelarDialogo(): void {
    this.dialogConfirmacaoAberta = false;
    this.dialogAcao = undefined;
  }

  getStatusLabel(entry: LancamentoFinanceiroResponse): string {
    switch (entry.situacao) {
      case 'pago':
        return 'Pago';
      case 'atrasado':
        return 'Em atraso';
      default:
        return entry.tipo === 'receber' ? 'A receber' : 'A pagar';
    }
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
      minimumFractionDigits: 2
    }).format(value || 0);
  }

  formatDate(date: string): string {
    const parsed = this.parseDate(date);
    if (!parsed) return '';
    return parsed.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric' });
  }

  getTipoContaLabel(tipo: string): string {
    const encontrado = this.tiposConta.find((item) => item.value === tipo);
    return encontrado ? encontrado.label : tipo;
  }

  getTipoChavePixLabel(tipo: string | undefined): string {
    if (!tipo) {
      return 'chave';
    }
    const encontrado = this.tiposChavePix.find((item) => item.value === tipo);
    return encontrado ? encontrado.label : tipo;
  }

  private resetContaBancariaForm(): void {
    this.contaEmEdicaoId = null;
    this.newAccount = {
      banco: '',
      agencia: '',
      numero: '',
      tipo: 'corrente',
      projetoVinculado: '',
      pixVinculado: false,
      tipoChavePix: '',
      chavePix: '',
      saldo: 0,
      dataAtualizacao: this.todayISO()
    };
    this.saldoContaMascara = this.formatarValorMonetario(0);
  }

  onPixVinculadoChange(valor: boolean): void {
    this.newAccount = {
      ...this.newAccount,
      pixVinculado: valor,
      tipoChavePix: valor ? this.newAccount.tipoChavePix : '',
      chavePix: valor ? this.newAccount.chavePix : ''
    };
    this.erroChavePix = null;
  }

  onTipoChavePixChange(valor: string): void {
    this.newAccount = { ...this.newAccount, tipoChavePix: valor, chavePix: '' };
    this.erroChavePix = null;
  }

  onChavePixInput(valor: string): void {
    const tipo = this.newAccount.tipoChavePix;
    let atualizado = valor || '';
    if (tipo === 'cnpj') {
      atualizado = this.aplicarMascaraCnpj(atualizado);
    } else if (tipo === 'telefone') {
      atualizado = this.aplicarMascaraTelefone(atualizado);
    }
    this.newAccount = { ...this.newAccount, chavePix: atualizado };
    this.erroChavePix = null;
  }

  private aplicarMascaraCnpj(valor: string): string {
    const numeros = (valor ?? '').replace(/\D/g, '').slice(0, 14);
    const parte1 = numeros.slice(0, 2);
    const parte2 = numeros.slice(2, 5);
    const parte3 = numeros.slice(5, 8);
    const parte4 = numeros.slice(8, 12);
    const parte5 = numeros.slice(12, 14);
    let resultado = '';
    if (parte1) {
      resultado = parte1;
    }
    if (parte2) {
      resultado += `.${parte2}`;
    }
    if (parte3) {
      resultado += `.${parte3}`;
    }
    if (parte4) {
      resultado += `/${parte4}`;
    }
    if (parte5) {
      resultado += `-${parte5}`;
    }
    return resultado;
  }

  private aplicarMascaraTelefone(valor: string): string {
    const numeros = (valor ?? '').replace(/\D/g, '').slice(0, 11);
    if (!numeros) {
      return '';
    }
    const ddd = numeros.slice(0, 2);
    const corpo = numeros.slice(2);
    if (corpo.length <= 4) {
      return `(${ddd}) ${corpo}`;
    }
    if (corpo.length <= 9) {
      return `(${ddd}) ${corpo.slice(0, 5)}-${corpo.slice(5)}`;
    }
    return `(${ddd}) ${corpo.slice(0, 5)}-${corpo.slice(5, 9)}`;
  }

  private validarChavePix(): string | null {
    if (!this.newAccount.pixVinculado) {
      return null;
    }
    const tipo = this.newAccount.tipoChavePix || '';
    const chave = (this.newAccount.chavePix || '').trim();
    if (!tipo) {
      return 'Informe o tipo da chave Pix.';
    }
    if (!chave) {
      return 'Informe a chave Pix.';
    }
    if (tipo === 'cnpj' && !this.validarCnpj(chave)) {
      return 'CNPJ invalido na chave Pix.';
    }
    if (tipo === 'email' && !this.validarEmail(chave)) {
      return 'Email invalido na chave Pix.';
    }
    if (tipo === 'telefone' && !this.validarTelefone(chave)) {
      return 'Telefone invalido na chave Pix.';
    }
    if (tipo === 'aleatoria' && !this.validarChaveAleatoria(chave)) {
      return 'Chave aleatoria invalida.';
    }
    return null;
  }

  private validarCnpj(valor: string): boolean {
    const cnpj = valor.replace(/\D/g, '');
    if (cnpj.length !== 14 || /^(\d)\1+$/.test(cnpj)) {
      return false;
    }
    const pesos1 = [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];
    const pesos2 = [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];
    const calcular = (pesos: number[]) => {
      let soma = 0;
      for (let i = 0; i < pesos.length; i += 1) {
        soma += Number(cnpj.charAt(i)) * pesos[i];
      }
      const resto = soma % 11;
      return resto < 2 ? 0 : 11 - resto;
    };
    const digito1 = calcular(pesos1);
    const digito2 = calcular(pesos2);
    return (
      digito1 === Number(cnpj.charAt(12)) &&
      digito2 === Number(cnpj.charAt(13))
    );
  }

  private validarEmail(valor: string): boolean {
    const email = valor.trim();
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  }

  private validarTelefone(valor: string): boolean {
    const telefone = valor.replace(/\D/g, '');
    return telefone.length === 10 || telefone.length === 11;
  }

  private validarChaveAleatoria(valor: string): boolean {
    const chave = valor.trim();
    return /^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$/.test(
      chave
    );
  }

  private formatarValorMonetario(valor: number): string {
    const numero = Number.isFinite(valor) ? valor : 0;
    return `R$ ${numero.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
  }

  private refreshPanels(): void {
    this.refreshMetrics();
    this.refreshPipelines();
    this.refreshCashFlow();
    this.refreshAlerts();
    this.refreshReport();
  }

  private refreshMetrics(): void {
    const receivable = this.sumEntries('receber', 30);
    const payable = this.sumEntries('pagar', 30);
    const cashBalance = this.totalCashBalance();
    const investments = this.bankAccounts
      .filter((account) => account.tipo !== 'corrente')
      .reduce((total, account) => total + Number(account.saldo || 0), 0);

    const baseReceivable = this.filtroResumoTipo === 'pagar' ? 0 : receivable;
    const basePayable = this.filtroResumoTipo === 'receber' ? 0 : payable;

    this.summaryCards = [
      {
        label: 'A receber (30 dias)',
        value: this.formatCurrency(baseReceivable),
        icon: faWallet,
        trend: baseReceivable >= basePayable ? 'up' : undefined
      },
      {
        label: 'A pagar (30 dias)',
        value: this.formatCurrency(basePayable),
        icon: faMoneyBillTransfer,
        trend: basePayable > baseReceivable ? 'down' : undefined
      },
      {
        label: 'Em caixa',
        value: this.formatCurrency(cashBalance),
        icon: faPiggyBank
      },
      {
        label: 'Aplicado em bancos',
        value: this.formatCurrency(investments),
        icon: faChartLine
      }
    ];
  }

  private refreshPipelines(): void {
    const ordered = [...this.agenda].sort((a, b) => {
      const dateA = this.parseDate(a.vencimento)?.getTime() ?? 0;
      const dateB = this.parseDate(b.vencimento)?.getTime() ?? 0;
      return dateA - dateB;
    });

    this.upcomingReceivables = ordered.filter((item) => item.tipo === 'receber' && item.situacao !== 'pago');
    this.upcomingPayables = ordered.filter((item) => item.tipo === 'pagar' && item.situacao !== 'pago');
  }

  private refreshCashFlow(): void {
    const start = this.startOfWeek(new Date());
    let runningBalance = this.totalCashBalance();

    this.cashFlow = Array.from({ length: 4 }).map((_, index) => {
      const startWeek = new Date(start);
      startWeek.setDate(startWeek.getDate() + index * 7);
      const endWeek = new Date(startWeek);
      endWeek.setDate(endWeek.getDate() + 6);

      const receivable = this.sumEntriesInRange('receber', startWeek, endWeek);
      const payable = this.sumEntriesInRange('pagar', startWeek, endWeek);
      runningBalance += receivable - payable;

      return {
        period: `${this.formatDate(startWeek.toISOString())} - ${this.formatDate(endWeek.toISOString())}`,
        receivable,
        payable,
        projection: runningBalance
      };
    });
  }

  private refreshAlerts(): void {
    const today = new Date();
    const dueSoon = this.agenda
      .filter((item) => item.situacao !== 'pago')
      .filter((item) => {
        const parsed = this.parseDate(item.vencimento);
        if (!parsed) return false;
        const diff = this.daysBetween(today, parsed);
        return diff <= 7;
      })
      .map((item) => `${item.descricao} vence em ${this.formatDate(item.vencimento)}`);

    const amendmentAlerts = this.amendmentControls
      .filter((amendment) => amendment.status !== 'recebido')
      .filter((amendment) => {
        const parsed = this.parseDate(amendment.dataPrevista);
        if (!parsed) return false;
        const diff = this.daysBetween(today, parsed);
        return diff <= (amendment.diasAlerta ?? 15);
      })
      .map((amendment) => `Emenda ${amendment.identificacao} prevista para ${this.formatDate(amendment.dataPrevista)}`);

    this.alerts = [...dueSoon, ...amendmentAlerts];
  }

  private refreshReport(): void {
    const recebimentos = this.financialMovements
      .filter((movement) => movement.tipo === 'entrada')
      .reduce((total, movement) => total + Number(movement.valor || 0), 0);
    const pagamentos = this.financialMovements
      .filter((movement) => movement.tipo === 'saida')
      .reduce((total, movement) => total + Number(movement.valor || 0), 0);
    const saldoAReceber = this.agenda
      .filter((item) => item.tipo === 'receber' && item.situacao !== 'pago')
      .reduce((total, item) => total + Number(item.valor || 0), 0);

    this.reportTotals = {
      recebimentos,
      pagamentos,
      saldoProjetado: this.totalCashBalance() + saldoAReceber - pagamentos,
      saldoAReceber
    };
  }

  private sumEntries(type: string, daysAhead: number): number {
    const today = this.toDateOnly(new Date());
    const start = this.toDateOnly(new Date());
    start.setDate(start.getDate() - daysAhead);
    const limit = this.toDateOnly(new Date());
    limit.setDate(limit.getDate() + daysAhead);

    return this.agenda
      .filter((item) => item.tipo === type && item.situacao !== 'pago')
      .filter((item) => {
        const parsed = this.parseDate(item.vencimento);
        if (!parsed) return false;
        const data = this.toDateOnly(parsed);
        return data <= limit && data >= start;
      })
      .reduce((total, item) => total + Number(item.valor || 0), 0);
  }

  private sumEntriesInRange(type: string, start: Date, end: Date): number {
    return this.agenda
      .filter((item) => item.tipo === type && item.situacao !== 'pago')
      .filter((item) => {
        const parsed = this.parseDate(item.vencimento);
        if (!parsed) return false;
        const data = this.toDateOnly(parsed);
        const inicio = this.toDateOnly(start);
        const fim = this.toDateOnly(end);
        return data >= inicio && data <= fim;
      })
      .reduce((total, item) => total + Number(item.valor || 0), 0);
  }

  private totalCashBalance(): number {
    return this.bankAccounts.reduce((total, account) => total + Number(account.saldo || 0), 0);
  }

  private todayISO(): string {
    return new Date().toISOString().substring(0, 10);
  }

  private startOfWeek(date: Date): Date {
    const result = new Date(date);
    const day = result.getDay();
    const diff = result.getDate() - day + (day === 0 ? -6 : 1);
    result.setDate(diff);
    return result;
  }

  private parseDate(date: string | Date | null | undefined): Date | null {
    if (!date) return null;
    const parsed = new Date(date as any);
    if (isNaN(parsed.getTime())) return null;
    return parsed;
  }

  private toDateOnly(date: Date): Date {
    return new Date(date.getFullYear(), date.getMonth(), date.getDate());
  }

  private daysBetween(start: Date, end: Date): number {
    const diff = end.getTime() - start.getTime();
    return Math.ceil(diff / (1000 * 60 * 60 * 24));
  }

  private normalizeString(value: string): string {
    return (value || '').toString().trim().toLowerCase();
  }
}
