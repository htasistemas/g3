import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';
import { PopupErrorBuilder } from '../../utils/popup-error.builder';
import { ConfigAcoesCrud, EstadoAcoesCrud, TelaBaseComponent } from '../compartilhado/tela-base.component';
import {
  faArrowLeft,
  faArrowRight,
  faBuildingColumns,
  faCheckCircle,
  faFileSignature,
  faGavel,
  faPrint,
  faStamp,
  faCircleCheck,
  faWarehouse,
  faSpinner
} from '@fortawesome/free-solid-svg-icons';
import { finalize } from 'rxjs/operators';
import {
  AutorizacaoCompraCotacaoResponse,
  AutorizacaoCompraRequest,
  AutorizacaoCompraResponse,
  AutorizacaoComprasService
} from '../../services/autorizacao-compras.service';
import { ProfessionalRecord, ProfessionalService } from '../../services/professional.service';
import { ContaBancariaResponse, ContabilidadeService } from '../../services/contabilidade.service';

type PurchaseType = 'produto' | 'bem' | 'servico' | 'contrato';
type StepId = 'solicitacao' | 'autorizacao' | 'cotacoes' | 'reserva' | 'conclusao';
type ApprovalDecision = 'aprovado' | 'ajustes' | 'rejeitado';
type ReservationStatus = 'reservado' | 'aguardando' | 'insuficiente';
type CnpjStatus = 'ATIVA' | 'SUSPENSA' | 'BAIXADA';

interface Step {
  id: StepId;
  label: string;
  helper: string;
  documentLabel: string;
}

interface PurchaseRequest {
  id: string;
  title: string;
  type: PurchaseType;
  requester: string;
  area: string;
  expectedDate: string;
  value: number;
  quantity: number;
  justification: string;
  status: StepId;
  approval?: {
    director: string;
    decision: ApprovalDecision;
    notes?: string;
    date: string;
  };
  budgetCenter?: string;
  reservationNumber?: string;
  winnerSupplier?: string;
  quotationDispensed?: boolean;
  quotationExemptionReason?: string;
  patrimonyRegistration?: boolean;
  warehouseRegistration?: boolean;
  paymentAuthorization?: PaymentAuthorization;
  priority?: 'urgente' | 'normal' | 'baixa';
}

interface Quotation {
  id?: string;
  supplier: string;
  legalName: string;
  cnpj: string;
  cnpjStatus: CnpjStatus;
  cnpjCheckedAt: string;
  cnpjCardUrl: string;
  cnpjCardPdf?: string;
  orcamentoFisicoNome?: string;
  orcamentoFisicoTipo?: string;
  orcamentoFisicoConteudo?: string;
  value: number;
  delivery: string;
  validity: string;
  compliance: 'ok' | 'pendente';
  notes?: string;
  isWinner?: boolean;
}

interface BudgetEnvelope {
  center: string;
  title: string;
  available: number;
  linkedTo: string;
}

interface BudgetReservation {
  requestId: string;
  center: string;
  value: number;
  status: ReservationStatus;
  observation: string;
  createdAt: string;
}

interface IntegrationChecklist {
  patrimony: boolean;
  warehouse: boolean;
  contracts: boolean;
}

interface PaymentAuthorization {
  number?: string;
  authorizedBy?: string;
  date?: string;
  notes?: string;
}


@Component({
  selector: 'app-autorizacao-compras',
  standalone: true,
  imports: [CommonModule, FormsModule, FontAwesomeModule, TelaPadraoComponent, PopupMessagesComponent],
  templateUrl: './autorizacao-compras.component.html',
  styleUrl: './autorizacao-compras.component.scss'
})
export class AutorizacaoComprasComponent extends TelaBaseComponent implements OnInit {
  readonly faPrint = faPrint;
  readonly faArrowLeft = faArrowLeft;
  readonly faArrowRight = faArrowRight;
  readonly faCheckCircle = faCheckCircle;
  readonly faCircleCheck = faCircleCheck;
  readonly faBuildingColumns = faBuildingColumns;
  readonly faGavel = faGavel;
  readonly faStamp = faStamp;
  readonly faFileSignature = faFileSignature;
  readonly faWarehouse = faWarehouse;
  readonly faSpinner = faSpinner;
  readonly quotationThreshold = 300;

  readonly acoesToolbar: Required<ConfigAcoesCrud> = this.criarConfigAcoes({
    salvar: true,
    excluir: true,
    novo: true,
    cancelar: true,
    imprimir: true
  });

  steps: Step[] = [
    {
      id: 'solicitacao',
      label: 'Solicitação',
      helper: 'Cadastro inicial, escopo e anexos',
      documentLabel: 'Formulário de solicitação'
    },
    {
      id: 'autorizacao',
      label: 'Autorização',
      helper: 'Parecer da diretoria e reservas',
      documentLabel: 'Relatório para autorização'
    },
    {
      id: 'cotacoes',
      label: 'Cotação prévia',
      helper: '3 cotações e definição do vencedor',
      documentLabel: 'Mapa comparativo de preços'
    },
    {
      id: 'reserva',
      label: 'Reserva orçamentária',
      helper: 'Bloqueio de orçamento e lastro contábil',
      documentLabel: 'Reserva orçamentária'
    },
    {
      id: 'conclusao',
      label: 'Conclusão da compra',
      helper: 'Integrações com patrimônio e almoxarifado',
      documentLabel: 'Termo de conclusão'
    }
  ];

  constructor(
    private readonly autorizacaoComprasService: AutorizacaoComprasService,      
    private readonly professionalService: ProfessionalService,
    private readonly contabilidadeService: ContabilidadeService
  ) {
    super();
  }

  ngOnInit(): void {
    this.loadProfessionals();
    this.loadRequests();
    this.loadContasBancarias();
  }

  activeStep: StepId = 'solicitacao';

  requests: PurchaseRequest[] = [];

  profissionais: ProfessionalRecord[] = [];

  get pendingSolicitacoes(): PurchaseRequest[] {
    return this.requests.filter((request) => request.status === 'solicitacao');
  }

  get autorizacoesPendentes(): PurchaseRequest[] {
    return this.requests.filter((request) => request.status === 'autorizacao');
  }

  get contasBancariasComSaldo(): ContaBancariaResponse[] {
    return this.contasBancarias.filter((conta) => Number(conta.saldo) > 0);
  }

  loadingRequests = false;

  feedback: string | null = null;

  popupErros: string[] = [];
  popupErrosCotacao: string[] = [];
  carregandoFornecedor = false;
  avisoFornecedor = '';
  ultimoCnpjConsultado = '';
  cacheFornecedor: { razaoSocial?: string; nomeFantasia?: string } | null = null;
  razaoSocialAutoPreenchida = false;
  nomeFantasiaAutoPreenchido = false;
  contasBancarias: ContaBancariaResponse[] = [];
  carregandoContas = false;
  reservaBancariaSelecionada: { contaId: number; valor: number } | null = null;

  private feedbackTimeout: ReturnType<typeof setTimeout> | undefined;

  quotes: Record<string, Quotation[]> = {};

  budgetEnvelopes: BudgetEnvelope[] = [
    {
      center: '3.3.90.30',
      title: 'Materiais de consumo pedagógico',
      available: 42000,
      linkedTo: 'Contabilidade'
    },
    {
      center: '4.4.90.52',
      title: 'Investimentos e bens permanentes',
      available: 75000,
      linkedTo: 'Patrimônio'
    },
    {
      center: '3.3.90.39',
      title: 'Serviços de terceiros e contratos',
      available: 52000,
      linkedTo: 'Contratos'
    }
  ];

  reservations: BudgetReservation[] = [];

  integrationChecklist: Record<string, IntegrationChecklist> = {};

  selectedRequestId = '';

  newRequestForm = {
    title: '',
    type: 'produto' as PurchaseType,
    requester: '',
    area: 'Operações',
    expectedDate: this.todayISO(15),
    value: 0,
    justification: '',
    budgetCenter: '3.3.90.30',
    priority: 'normal' as PurchaseRequest['priority'],
    quantity: 1
  };

  valorEstimadoDisplay = '';
  valorCotacaoDisplay = '';

  approvalForm = {
    director: '',
    decision: 'aprovado' as ApprovalDecision,
    notes: '',
    date: this.todayISO()
  };

  newQuoteForm = {
    supplier: '',
    companyName: '',
    cnpj: '',
    cnpjCardUrl: '',
    value: 0,
    delivery: this.todayISO(7),
    validity: this.todayISO(15),
    compliance: 'pendente' as Quotation['compliance'],
    notes: '',
    orcamentoFisicoNome: '',
    orcamentoFisicoTipo: '',
    orcamentoFisicoConteudo: ''
  };

  reservationForm = {
    center: '3.3.90.30',
    value: 0,
    reservationNumber: '',
    observation: ''
  };

  get acoesDesabilitadas(): EstadoAcoesCrud {
    const hasSelected = Boolean(this.selectedRequest);
    return {
      salvar: !this.podeSalvarSolicitacao,
      excluir: !hasSelected,
      imprimir: false,
      novo: false,
      cancelar: false
    };
  }

  salvarSolicitacao(): void {
    this.createRequest();
  }

  iniciarNovaSolicitacao(): void {
    this.limparFormularioSolicitacao();
    this.activeStep = 'solicitacao';
  }

  limparFormularioSolicitacao(): void {
    this.resetNewRequestForm();
  }

  private resetNewRequestForm(): void {
    this.newRequestForm = {
      title: '',
      type: 'produto',
      requester: '',
      area: 'Operações',
      expectedDate: this.todayISO(15),
      value: 0,
      justification: '',
      budgetCenter: '3.3.90.30',
      priority: 'normal',
      quantity: 1
    };
    this.valorEstimadoDisplay = '';
  }

  private loadProfessionals(): void {
    this.professionalService.list().subscribe({
      next: (records) => (this.profissionais = records),
      error: () => this.setFeedback('Não foi possível carregar os profissionais no momento.')
    });
  }

  private loadContasBancarias(): void {
    this.carregandoContas = true;
    this.contabilidadeService.listarContasBancarias().subscribe({
      next: (records) => {
        this.contasBancarias = records;
        this.carregandoContas = false;
      },
      error: () => {
        this.setFeedback('Não foi possível carregar as contas bancárias no momento.');
        this.carregandoContas = false;
      }
    });
  }

  private loadRequests(): void {
    this.loadingRequests = true;
    this.autorizacaoComprasService
      .list()
      .pipe(finalize(() => (this.loadingRequests = false)))
      .subscribe({
        next: (records) => {
          this.requests = records.map((record) => this.toPurchaseRequest(record));
          this.selectedRequestId = this.requests[0]?.id ?? '';
        },
        error: () => {
          this.setFeedback('Não foi possível carregar as solicitações no momento.');
        }
      });
  }

  removerSolicitacaoSelecionada(): void {
    const selected = this.selectedRequest;
    if (!selected) return;

    this.autorizacaoComprasService.delete(selected.id).subscribe({
      next: () => {
        this.requests = this.requests.filter((req) => req.id !== selected.id);
        this.selectedRequestId = this.requests[0]?.id ?? '';
        this.setFeedback('Solicitação removida.');
      },
      error: () => {
        this.setFeedback('Não foi possível remover a solicitação. Tente novamente.');
      }
    });
  }

  fecharTela(): void {
    window.history.back();
  }

  conclusionForm = {
    documentNumber: '',
    observation: '',
    sendToPatrimony: false,
    sendToWarehouse: false
  };

  paymentAuthorizationForm: PaymentAuthorization = {
    number: '',
    authorizedBy: '',
    date: this.todayISO(),
    notes: ''
  };

  get selectedRequest(): PurchaseRequest | undefined {
    return this.requests.find((req) => req.id === this.selectedRequestId);
  }

  get activeStepIndex(): number {
    return this.steps.findIndex((step) => step.id === this.activeStep);
  }

  get currentQuotes(): Quotation[] {
    return this.quotes[this.selectedRequestId] ?? [];
  }

  get quoteRequirementMet(): boolean {
    if (!this.selectedRequest) return false;
    if (!this.requiresQuotation(this.selectedRequest)) return true;
    if (this.selectedRequest.quotationDispensed) return true;

    return this.currentQuotes.length >= 3 && this.currentQuotes.some((quote) => quote.isWinner);
  }

  get lowestQuote(): Quotation | null {
    if (!this.currentQuotes.length) return null;
    return this.currentQuotes.reduce((lowest, quote) =>
      quote.value < lowest.value ? quote : lowest
    );
  }

  get pendingApprovals(): number {
    return this.requests.filter((req) => req.status === 'autorizacao').length;
  }

  get pendingQuotes(): number {
    return this.requests.filter((req) => req.status === 'cotacoes').length;
  }

  get readyToConclude(): number {
    return this.requests.filter((req) => req.status === 'reserva').length;
  }

  todayISO(offsetDays = 0): string {
    const date = new Date();
    date.setDate(date.getDate() + offsetDays);
    return date.toISOString().substring(0, 10);
  }

  requiresQuotation(request: PurchaseRequest): boolean {
    return request.value >= this.quotationThreshold;
  }

  canDispenseQuotation(request: PurchaseRequest | undefined): boolean {
    if (!request) return false;
    return !this.requiresQuotation(request) && !request.quotationDispensed;
  }

  changeStep(step: StepId): void {
    this.activeStep = step;
    if (step === 'cotacoes') {
      this.loadQuotes();
    }
  }

  navigateStep(direction: 'next' | 'prev'): void {
    const nextIndex = this.activeStepIndex + (direction === 'next' ? 1 : -1);
    const targetStep = this.steps[nextIndex]?.id;

    if (targetStep) {
      this.activeStep = targetStep;
    }
  }

  selectRequest(id: string): void {
    this.selectedRequestId = id;
    const target = this.selectedRequest;
    this.conclusionForm.sendToPatrimony = target?.type === 'bem';
    this.conclusionForm.sendToWarehouse = target?.type === 'produto';
    this.reservationForm = {
      center: target?.budgetCenter || this.reservationForm.center,
      value: target?.value ?? 0,
      reservationNumber: target?.reservationNumber || '',
      observation: ''
    };
    this.paymentAuthorizationForm = {
      number: target?.paymentAuthorization?.number || '',
      authorizedBy: target?.paymentAuthorization?.authorizedBy || '',
      date: target?.paymentAuthorization?.date || this.todayISO(),
      notes: target?.paymentAuthorization?.notes || ''
    };
    this.reservaBancariaSelecionada = null;
    if (this.activeStep === 'cotacoes') {
      this.loadQuotes();
    }
  }

  get podeSalvarSolicitacao(): boolean {
    return (
      Boolean(this.newRequestForm.type?.trim()) &&
      Boolean(this.newRequestForm.title?.trim()) &&
      Boolean(this.newRequestForm.requester?.trim()) &&
      Boolean(this.newRequestForm.justification?.trim()) &&
      Number(this.newRequestForm.value) > 0
    );
  }

  createRequest(): void {
    this.popupErros = [];
    if (!this.podeSalvarSolicitacao) {
      const builder = new PopupErrorBuilder();
      if (!this.newRequestForm.type?.trim()) {
        builder.adicionar('Tipo é obrigatório.');
      }
      if (!this.newRequestForm.title?.trim()) {
        builder.adicionar('Título da compra é obrigatório.');
      }
      if (!this.newRequestForm.requester?.trim()) {
        builder.adicionar('Responsável é obrigatório.');
      }
      if (!Number(this.newRequestForm.value)) {
        builder.adicionar('Valor estimado é obrigatório.');
      }
      if (!this.newRequestForm.justification?.trim()) {
        builder.adicionar('Justificativa e escopo é obrigatório.');
      }
      this.popupErros = builder.build();
      this.setFeedback('Preencha os campos obrigatórios antes de salvar a solicitação.');
      return;
    }

    this.autorizacaoComprasService.create(this.buildCreationPayload()).subscribe({
      next: (response) => {
        const saved = this.toPurchaseRequest(response);
        this.requests = [saved, ...this.requests];
        this.selectedRequestId = saved.id;
        this.activeStep = 'autorizacao';
        this.popupErros = [];
        this.resetNewRequestForm();
      },
      error: () => {
        this.setFeedback('Não foi possível salvar a solicitação. Tente novamente.');
      }
    });
  }

  fecharPopupErros(): void {
    this.popupErros = [];
  }

  avancarParaAutorizacao(request: PurchaseRequest): void {
    if (request.status !== 'solicitacao') return;
    const updated: PurchaseRequest = { ...request, status: 'autorizacao' };
    this.selectedRequestId = request.id;
    this.activeStep = 'autorizacao';
    this.updateRequest(updated);
  }

  registerAuthorization(): void {
    if (!this.selectedRequest) return;

    const updated: PurchaseRequest = {
      ...this.selectedRequest,
      approval: {
        director: this.approvalForm.director || 'Diretoria',
        decision: this.approvalForm.decision,
        notes: this.approvalForm.notes,
        date: this.approvalForm.date
      },
      status: this.approvalForm.decision === 'aprovado' ? 'cotacoes' : 'solicitacao'
    };

    this.updateRequest(updated);
    this.selectedRequestId = updated.id;
    this.activeStep = updated.status === 'cotacoes' ? 'cotacoes' : 'solicitacao';
  }

  addQuotation(): void {
    if (!this.selectedRequest) return;
    this.popupErrosCotacao = [];
    const builder = new PopupErrorBuilder();
    if (!this.newQuoteForm.supplier?.trim()) {
      builder.adicionar('Fornecedor é obrigatório.');
    }
    if (!this.newQuoteForm.cnpj?.trim()) {
      builder.adicionar('CNPJ do fornecedor é obrigatório.');
    }
    if (!Number(this.newQuoteForm.value)) {
      builder.adicionar('Valor ofertado é obrigatório.');
    }
    this.popupErrosCotacao = builder.build();
    if (this.popupErrosCotacao.length) {
      this.setFeedback('Preencha os campos obrigatórios antes de adicionar a cotação.');
      return;
    }
    const supplierLabel = this.newQuoteForm.supplier || this.newQuoteForm.companyName || '';
    const payload = {
      fornecedor: supplierLabel,
      razaoSocial: this.newQuoteForm.companyName || undefined,
      cnpj: this.newQuoteForm.cnpj ? this.formatCnpj(this.newQuoteForm.cnpj) : undefined,
      valor: Number(this.newQuoteForm.value),
      prazoEntrega: this.newQuoteForm.delivery || undefined,
      validade: this.newQuoteForm.validity || undefined,
      conformidade: this.newQuoteForm.compliance,
      observacoes: this.newQuoteForm.notes || undefined,
      cartaoCnpjUrl: this.newQuoteForm.cnpjCardUrl || undefined,
      orcamentoFisicoNome: this.newQuoteForm.orcamentoFisicoNome || undefined,
      orcamentoFisicoTipo: this.newQuoteForm.orcamentoFisicoTipo || undefined,
      orcamentoFisicoConteudo: this.newQuoteForm.orcamentoFisicoConteudo || undefined
    };

    this.autorizacaoComprasService.createQuote(this.selectedRequest.id, payload).subscribe({
      next: (response) => {
        const quotes = this.quotes[this.selectedRequestId] ?? [];
        this.quotes = {
          ...this.quotes,
          [this.selectedRequestId]: [...quotes, this.toQuotation(response)]
        };
        this.newQuoteForm = {
          supplier: '',
          companyName: '',
          cnpj: '',
          cnpjCardUrl: '',
          value: 0,
          delivery: this.todayISO(7),
          validity: this.todayISO(15),
          compliance: 'pendente',
          notes: '',
          orcamentoFisicoNome: '',
          orcamentoFisicoTipo: '',
          orcamentoFisicoConteudo: ''
        };
        this.valorCotacaoDisplay = '';
        this.popupErrosCotacao = [];
      },
      error: () => {
        this.setFeedback('Não foi possível salvar a cotação. Tente novamente.');
      }
    });
  }

  removerCotacao(quote: Quotation): void {
    if (!this.selectedRequest || !quote.id) {
      return;
    }
    this.autorizacaoComprasService.deleteQuote(this.selectedRequest.id, quote.id).subscribe({
      next: () => {
        const quotes = this.quotes[this.selectedRequestId] ?? [];
        this.quotes = {
          ...this.quotes,
          [this.selectedRequestId]: quotes.filter((item) => item.id !== quote.id)
        };
      },
      error: () => {
        this.setFeedback('Não foi possível remover a cotação. Tente novamente.');
      }
    });
  }

  imprimirOrcamento(quote: Quotation): void {
    if (!quote.orcamentoFisicoConteudo) {
      this.setFeedback('Nenhum orçamento físico anexado para esta cotação.');
      return;
    }
    const link = document.createElement('a');
    link.href = quote.orcamentoFisicoConteudo;
    link.download = quote.orcamentoFisicoNome || 'orcamento-fornecedor';
    link.target = '_blank';
    link.click();
  }

  markWinner(quote: Quotation): void {
    if (!this.selectedRequest) return;
    if (!this.currentQuotes.length) {
      this.setFeedback('Cadastre ao menos uma cotação antes de definir o vencedor.');
      return;
    }

    const updatedQuotes = (this.quotes[this.selectedRequestId] ?? []).map((item) => ({
      ...item,
      isWinner: item === quote
    }));

    this.quotes = { ...this.quotes, [this.selectedRequestId]: updatedQuotes };

    const updatedRequest: PurchaseRequest = {
      ...this.selectedRequest,
      winnerSupplier: quote.supplier,
      quotationDispensed: false,
      status: 'reserva'
    };

    this.updateRequest(updatedRequest, () => {
      this.activeStep = 'reserva';
    });
  }

  dispenseQuotation(): void {
    if (!this.selectedRequest) return;

    const updatedRequest: PurchaseRequest = {
      ...this.selectedRequest,
      quotationDispensed: true,
      quotationExemptionReason:
        this.selectedRequest.quotationExemptionReason ??
        `Valor estimado abaixo de R$ ${this.quotationThreshold.toFixed(2)}: dispensa de cotação liberada.`,
      status: 'reserva'
    };

    this.updateRequest(updatedRequest, () => {
      this.activeStep = 'reserva';
    });
  }

  registerReservation(): void {
    if (!this.selectedRequest || !this.reservationForm.center || !this.reservationForm.value) return;

    const envelope = this.budgetEnvelopes.find((item) => item.center === this.reservationForm.center);
    const requested = Number(this.reservationForm.value);
    const hasBalance = this.reservaBancariaSelecionada ? true : envelope ? envelope.available >= requested : false;
    const status: ReservationStatus = hasBalance ? 'reservado' : 'insuficiente';

    if (envelope && hasBalance && !this.reservaBancariaSelecionada) {
      this.budgetEnvelopes = this.budgetEnvelopes.map((item) =>
        item.center === envelope.center ? { ...item, available: item.available - requested } : item
      );
    }

    this.reservations = [
      {
        requestId: this.selectedRequest.id,
        center: this.reservationForm.center,
        value: requested,
        status,
        observation: this.reservationForm.observation || 'Reserva registrada',
        createdAt: this.todayISO()
      },
      ...this.reservations
    ];

    const updatedRequest: PurchaseRequest = {
      ...this.selectedRequest,
      budgetCenter: this.reservationForm.center,
      reservationNumber: this.reservationForm.reservationNumber || this.selectedRequest.reservationNumber,
      status: status === 'reservado' ? 'conclusao' : 'reserva'
    };

    this.updateRequest(updatedRequest, (synced) => {
      this.selectedRequestId = synced.id;
      this.activeStep = synced.status === 'conclusao' ? 'conclusao' : 'reserva';
    });
  }

  reservarSaldoBanco(conta: ContaBancariaResponse): void {
    if (!this.selectedRequest) return;
    if (this.reservaBancariaSelecionada) {
      this.setFeedback('Reserva bancária já definida. Use a reserva contábil para avançar.');
      return;
    }
    const valorReserva = Number(this.selectedRequest.value || 0);
    if (!valorReserva) {
      this.setFeedback('Informe o valor da autorização para reservar.');
      return;
    }
    if (Number(conta.saldo) < valorReserva) {
      this.setFeedback('Saldo insuficiente para reservar este valor.');
      return;
    }
    const payload = {
      tipo: 'RESERVA',
      descricao: `Reserva para autorização ${this.selectedRequest.title}`,
      contraparte: this.selectedRequest.requester,
      contaBancariaId: conta.id,
      dataMovimentacao: this.todayISO(),
      valor: valorReserva
    };
    this.contabilidadeService.criarMovimentacao(payload).subscribe({
      next: () => {
        this.contasBancarias = this.contasBancarias.map((item) =>
          item.id === conta.id ? { ...item, saldo: Number(item.saldo) - valorReserva } : item
        );
        this.reservaBancariaSelecionada = { contaId: conta.id, valor: valorReserva };
        this.reservationForm.value = valorReserva;
        this.setFeedback('Reserva bancária registrada. Selecione o centro de custo para concluir.');
      },
      error: () => {
        this.setFeedback('Não foi possível reservar o saldo bancário.');
      }
    });
  }

  finalizePurchase(): void {
    if (!this.selectedRequest) return;

    const shouldSendToPatrimony =
      this.selectedRequest.type === 'bem' || this.conclusionForm.sendToPatrimony;
    const shouldSendToWarehouse =
      this.selectedRequest.type === 'produto' || this.conclusionForm.sendToWarehouse;

    const updatedRequest: PurchaseRequest = {
      ...this.selectedRequest,
      patrimonyRegistration: shouldSendToPatrimony,
      warehouseRegistration: shouldSendToWarehouse,
      paymentAuthorization: { ...this.paymentAuthorizationForm },
      status: 'conclusao'
    };

    this.integrationChecklist = {
      ...this.integrationChecklist,
      [this.selectedRequest.id]: {
        patrimony: shouldSendToPatrimony,
        warehouse: shouldSendToWarehouse,
        contracts: this.selectedRequest.type === 'contrato' || this.selectedRequest.type === 'servico'
      }
    };

    this.updateRequest(updatedRequest);
  }

  printSection(): void {
    window.print();
  }

  getStepLabel(stepId: StepId): string {
    return this.steps.find((step) => step.id === stepId)?.label ?? '';
  }

  isLowestQuote(quote: Quotation): boolean {
    return this.lowestQuote?.value === quote.value && this.lowestQuote?.supplier === quote.supplier;
  }

  formatValorEstimadoInput(raw: string): void {
    const digits = (raw || '').replace(/\D/g, '');
    const amount = digits ? Number(digits) / 100 : 0;
    this.newRequestForm.value = amount;
    this.valorEstimadoDisplay = digits ? this.formatCurrency(amount) : '';
  }

  formatValorCotacaoInput(raw: string): void {
    const digits = (raw || '').replace(/\D/g, '');
    const amount = digits ? Number(digits) / 100 : 0;
    this.newQuoteForm.value = amount;
    this.valorCotacaoDisplay = digits ? this.formatCurrency(amount) : '';
  }

  private formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
      minimumFractionDigits: 2
    }).format(value);
  }


  private normalizeCnpj(cnpj: string): string {
    return (cnpj || '').replace(/\D/g, '');
  }

  private formatCnpj(cnpj: string): string {
    const digits = this.normalizeCnpj(cnpj).padStart(14, '0');
    return digits.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})$/, '$1.$2.$3/$4-$5');
  }

  private isValidCnpj(cnpj: string): boolean {
    const digits = this.normalizeCnpj(cnpj);
    if (digits.length !== 14 || /^(\d)\1+$/.test(digits)) return false;

    const calc = (base: string, factors: number[]): number => {
      const total = base
        .split('')
        .map((num, idx) => Number(num) * factors[idx])
        .reduce((sum, value) => sum + value, 0);
      const remainder = total % 11;
      return remainder < 2 ? 0 : 11 - remainder;
    };

    const base = digits.slice(0, 12);
    const digit1 = calc(base, [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]);
    const digit2 = calc(base + digit1, [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]);

    return digits.endsWith(`${digit1}${digit2}`);
  }

  onOrcamentoFisicoSelecionado(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) {
      this.newQuoteForm.orcamentoFisicoNome = '';
      this.newQuoteForm.orcamentoFisicoTipo = '';
      this.newQuoteForm.orcamentoFisicoConteudo = '';
      this.atualizarConformidadeCotacao();
      return;
    }
    this.newQuoteForm.orcamentoFisicoNome = file.name;
    this.newQuoteForm.orcamentoFisicoTipo = file.type || 'application/octet-stream';
    const reader = new FileReader();
    reader.onload = () => {
      const result = reader.result?.toString() || '';
      this.newQuoteForm.orcamentoFisicoConteudo = result;
      this.atualizarConformidadeCotacao();
    };
    reader.readAsDataURL(file);
  }

  onCnpjCardUrlChange(valor: string): void {
    this.newQuoteForm.cnpjCardUrl = valor;
    this.atualizarConformidadeCotacao();
  }

  onLinkCnpjKeydown(event: KeyboardEvent): void {
    if (event.key !== 'Tab') return;
    const cnpjExtraido = this.parseCnpjFromLink(this.newQuoteForm.cnpjCardUrl);
    if (cnpjExtraido) {
      this.newQuoteForm.cnpj = this.formatCnpj(cnpjExtraido);
    }
  }

  onCnpjKeydown(event: KeyboardEvent): void {
    if (event.key === 'Tab') {
      this.consultarFornecedorPorCnpj();
    }
  }

  onCnpjBlur(): void {
    this.consultarFornecedorPorCnpj();
  }

  onRazaoSocialKeydown(event: KeyboardEvent): void {
    if (event.key === 'Tab') {
      this.preencherNomeFantasiaSeDisponivel();
    }
  }

  onRazaoSocialBlur(): void {
    this.preencherNomeFantasiaSeDisponivel();
  }

  onRazaoSocialInput(): void {
    this.razaoSocialAutoPreenchida = false;
  }

  onNomeFantasiaInput(): void {
    this.nomeFantasiaAutoPreenchido = false;
  }

  private toPurchaseRequest(response: AutorizacaoCompraResponse): PurchaseRequest {
    const purchase: PurchaseRequest = {
      id: String(response.id ?? ''),
      title: response.titulo,
      type: (response.tipo as PurchaseType) ?? 'produto',
      requester: response.responsavel ?? '',
      area: response.area ?? '',
      expectedDate: response.dataPrevista ?? '',
      value: Number(response.valor ?? 0),
      justification: response.justificativa ?? '',
      status: (response.status as StepId) ?? 'solicitacao',
      budgetCenter: response.centroCusto,
      reservationNumber: response.numeroReserva,
      winnerSupplier: response.vencedor,
      quotationDispensed: Boolean(response.dispensarCotacao),
      quotationExemptionReason: response.motivoDispensa,
      patrimonyRegistration: Boolean(response.registroPatrimonio),
      warehouseRegistration: Boolean(response.registroAlmoxarifado),
      approval: response.aprovador
        ? {
            director: response.aprovador,
            decision: (response.decisao as ApprovalDecision) ?? 'aprovado',
            notes: response.observacoesAprovacao,
            date: response.dataAprovacao ?? ''
          }
        : undefined,
      paymentAuthorization:
        response.autorizacaoPagamentoNumero ||
        response.autorizacaoPagamentoAutor ||
        response.autorizacaoPagamentoData ||
        response.autorizacaoPagamentoObservacoes
          ? {
              number: response.autorizacaoPagamentoNumero,
              authorizedBy: response.autorizacaoPagamentoAutor,
              date: response.autorizacaoPagamentoData ?? '',
              notes: response.autorizacaoPagamentoObservacoes
            }
          : undefined,
      priority: response.prioridade ?? 'normal',
      quantity: Number(response.quantidadeItens ?? 1)
    };
    return purchase;
  }

  private loadQuotes(): void {
    if (!this.selectedRequestId) return;
    this.autorizacaoComprasService.listQuotes(this.selectedRequestId).subscribe({
      next: (records) => {
        this.quotes = {
          ...this.quotes,
          [this.selectedRequestId]: records.map((record) => this.toQuotation(record))
        };
      },
      error: () => {
        this.setFeedback('Não foi possível carregar as cotações no momento.');
      }
    });
  }

  private toQuotation(response: AutorizacaoCompraCotacaoResponse): Quotation {
    return {
      id: response.id ? String(response.id) : undefined,
      supplier: response.fornecedor,
      legalName: response.razaoSocial || response.fornecedor,
      cnpj: response.cnpj || '',
      cnpjStatus: 'ATIVA',
      cnpjCheckedAt: response.criadoEm || this.todayISO(),
      cnpjCardUrl: response.cartaoCnpjUrl || '',
      cnpjCardPdf: response.cartaoCnpjConteudo || undefined,
      orcamentoFisicoNome: response.orcamentoFisicoNome || undefined,
      orcamentoFisicoTipo: response.orcamentoFisicoTipo || undefined,
      orcamentoFisicoConteudo: response.orcamentoFisicoConteudo || undefined,
      value: Number(response.valor ?? 0),
      delivery: response.prazoEntrega ?? '',
      validity: response.validade ?? '',
      compliance: (response.conformidade as Quotation['compliance']) ?? 'ok',
      notes: response.observacoes ?? ''
    };
  }

  private atualizarConformidadeCotacao(): void {
    const possuiCartao = Boolean(this.newQuoteForm.cnpjCardUrl?.trim());
    const possuiOrcamento = Boolean(this.newQuoteForm.orcamentoFisicoConteudo?.trim());
    this.newQuoteForm.compliance = possuiCartao && possuiOrcamento ? 'ok' : 'pendente';
  }

  private consultarFornecedorPorCnpj(): void {
    const cnpjLimpo = this.normalizeCnpj(this.newQuoteForm.cnpj);
    this.avisoFornecedor = '';
    if (cnpjLimpo.length !== 14) {
      return;
    }
    if (cnpjLimpo === this.ultimoCnpjConsultado) {
      return;
    }
    this.carregandoFornecedor = true;
    this.autorizacaoComprasService.buscarFornecedorPorCnpj(cnpjLimpo).subscribe({
      next: (response) => {
        this.cacheFornecedor = {
          razaoSocial: response.razaoSocial,
          nomeFantasia: response.nomeFantasia
        };
        if (response.razaoSocial && (!this.newQuoteForm.companyName || this.razaoSocialAutoPreenchida)) {
          this.newQuoteForm.companyName = response.razaoSocial;
          this.razaoSocialAutoPreenchida = true;
        }
        if (response.nomeFantasia && (!this.newQuoteForm.supplier || this.nomeFantasiaAutoPreenchido)) {
          this.newQuoteForm.supplier = response.nomeFantasia;
          this.nomeFantasiaAutoPreenchido = true;
        }
        this.ultimoCnpjConsultado = cnpjLimpo;
        this.carregandoFornecedor = false;
      },
      error: () => {
        this.avisoFornecedor = 'Fornecedor não encontrado, preencha manualmente.';
        this.cacheFornecedor = null;
        this.ultimoCnpjConsultado = cnpjLimpo;
        this.carregandoFornecedor = false;
      }
    });
  }

  private preencherNomeFantasiaSeDisponivel(): void {
    if (!this.newQuoteForm.supplier && this.cacheFornecedor?.nomeFantasia) {
      this.newQuoteForm.supplier = this.cacheFornecedor.nomeFantasia;
      this.nomeFantasiaAutoPreenchido = true;
    }
  }

  private parseCnpjFromLink(link: string): string | null {
    if (!link) return null;
    const valor = link.trim();
    const decodificado = this.tryDecode(valor);
    const parametros = ['cnpj', 'documento', 'doc', 'cpfcnpj'];
    try {
      const url = new URL(decodificado);
      for (const parametro of parametros) {
        const encontrado = url.searchParams.get(parametro) || url.searchParams.get(parametro.toUpperCase());
        if (encontrado) {
          const digits = this.normalizeCnpj(encontrado);
          if (digits.length === 14) {
            return digits;
          }
        }
      }
    } catch {
      // ignora links sem formato de URL válido
    }

    const matchParam = decodificado.match(/(?:cnpj|documento|doc|cpfcnpj)=([^&]+)/i);
    if (matchParam?.[1]) {
      const digits = this.normalizeCnpj(matchParam[1]);
      if (digits.length === 14) {
        return digits;
      }
    }

    const matchDigits = decodificado.match(/\d{14}/);
    if (matchDigits) {
      return matchDigits[0];
    }
    const matchMask = decodificado.match(/\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}/);
    if (matchMask) {
      const digits = this.normalizeCnpj(matchMask[0]);
      return digits.length === 14 ? digits : null;
    }
    return null;
  }

  private tryDecode(valor: string): string {
    try {
      return decodeURIComponent(valor);
    } catch {
      return valor;
    }
  }

  private buildPayload(request: PurchaseRequest): AutorizacaoCompraRequest {
    return {
      titulo: request.title,
      tipo: request.type,
      area: request.area,
      responsavel: request.requester,
      dataPrevista: request.expectedDate || undefined,
      valor: request.value,
      justificativa: request.justification,
      centroCusto: request.budgetCenter,
      status: request.status,
      aprovador: request.approval?.director,
      decisao: request.approval?.decision,
      observacoesAprovacao: request.approval?.notes,
      dataAprovacao: request.approval?.date,
      dispensarCotacao: request.quotationDispensed,
      motivoDispensa: request.quotationExemptionReason,
      vencedor: request.winnerSupplier,
      registroPatrimonio: request.patrimonyRegistration,
      registroAlmoxarifado: request.warehouseRegistration,
      numeroReserva: request.reservationNumber,
      autorizacaoPagamentoNumero: request.paymentAuthorization?.number,
      autorizacaoPagamentoAutor: request.paymentAuthorization?.authorizedBy,
      autorizacaoPagamentoData: request.paymentAuthorization?.date,
      autorizacaoPagamentoObservacoes: request.paymentAuthorization?.notes
      ,
      prioridade: request.priority,
      quantidadeItens: request.quantity
    };
  }

  private buildCreationPayload(): AutorizacaoCompraRequest {
    const valor = Number(this.newRequestForm.value);
    const payload: AutorizacaoCompraRequest = {
      titulo: this.newRequestForm.title,
      tipo: this.newRequestForm.type,
      area: this.newRequestForm.area,
      responsavel: this.newRequestForm.requester,
      dataPrevista: this.newRequestForm.expectedDate || undefined,
      valor,
      justificativa: this.newRequestForm.justification,
      centroCusto: this.newRequestForm.budgetCenter,
      status: 'solicitacao',
      dispensarCotacao: valor < this.quotationThreshold
      ,
      prioridade: this.newRequestForm.priority,
      quantidadeItens: this.newRequestForm.quantity
    };
    payload.quantidadeItens = this.newRequestForm.quantity;
    if (payload.dispensarCotacao) {
      payload.motivoDispensa = `Valor estimado inferior ao limite de R$ ${this.quotationThreshold.toFixed(
        2
      )} para cotação prévia.`;
    }
    return payload;
  }

  private updateRequest(updated: PurchaseRequest, onSuccess?: (synced: PurchaseRequest) => void): void {
    this.requests = this.requests.map((req) => (req.id === updated.id ? updated : req));
    this.autorizacaoComprasService.update(updated.id, this.buildPayload(updated)).subscribe({
      next: (response) => {
        const synced = this.toPurchaseRequest(response);
        this.requests = this.requests.map((req) => (req.id === synced.id ? synced : req));
        if (onSuccess) {
          onSuccess(synced);
        }
      },
      error: () => {
        this.setFeedback('Não foi possível salvar as alterações na solicitação.');
      }
    });
  }

  private setFeedback(message: string, timeoutMs = 10000): void {
    this.feedback = message;
    this.clearFeedbackTimeout();
    this.feedbackTimeout = window.setTimeout(() => {
      this.feedback = null;
      this.feedbackTimeout = undefined;
    }, timeoutMs);
  }

  private clearFeedbackTimeout(): void {
    if (this.feedbackTimeout) {
      clearTimeout(this.feedbackTimeout);
      this.feedbackTimeout = undefined;
    }
  }
}
