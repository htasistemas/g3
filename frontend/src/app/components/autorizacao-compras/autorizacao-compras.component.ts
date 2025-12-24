import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faArrowLeft,
  faArrowRight,
  faBuildingColumns,
  faCartShopping,
  faCheckCircle,
  faClipboardList,
  faFileSignature,
  faGavel,
  faMoneyBills,
  faPrint,
  faStamp,
  faCircleCheck,
  faWarehouse
} from '@fortawesome/free-solid-svg-icons';

type PurchaseType = 'produto' | 'bem' | 'servico' | 'contrato';
type StepId = 'solicitacao' | 'autorizacao' | 'cotacoes' | 'empenho' | 'conclusao';
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
  justification: string;
  status: StepId;
  approval?: {
    director: string;
    decision: ApprovalDecision;
    notes?: string;
    date: string;
  };
  budgetCenter?: string;
  empenhoNumber?: string;
  winnerSupplier?: string;
  quotationDispensed?: boolean;
  quotationExemptionReason?: string;
  patrimonyRegistration?: boolean;
  warehouseRegistration?: boolean;
}

interface Quotation {
  supplier: string;
  legalName: string;
  cnpj: string;
  cnpjStatus: CnpjStatus;
  cnpjCheckedAt: string;
  cnpjCardUrl: string;
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

interface SupplierRegistryEntry {
  cnpj: string;
  legalName: string;
  tradeName?: string;
  status: CnpjStatus;
  lastUpdate: string;
  cnpjCardUrl: string;
}

@Component({
  selector: 'app-autorizacao-compras',
  standalone: true,
  imports: [CommonModule, FormsModule, FontAwesomeModule],
  templateUrl: './autorizacao-compras.component.html',
  styleUrl: './autorizacao-compras.component.scss'
})
export class AutorizacaoComprasComponent {
  readonly faCartShopping = faCartShopping;
  readonly faClipboardList = faClipboardList;
  readonly faPrint = faPrint;
  readonly faArrowLeft = faArrowLeft;
  readonly faArrowRight = faArrowRight;
  readonly faCheckCircle = faCheckCircle;
  readonly faCircleCheck = faCircleCheck;
  readonly faBuildingColumns = faBuildingColumns;
  readonly faMoneyBills = faMoneyBills;
  readonly faGavel = faGavel;
  readonly faStamp = faStamp;
  readonly faFileSignature = faFileSignature;
  readonly faWarehouse = faWarehouse;
  readonly quotationThreshold = 300;

  readonly cnpjRegistry: Record<string, SupplierRegistryEntry> = {
    '33000167000101': {
      cnpj: '33.000.167/0001-01',
      legalName: 'PETRÓLEO BRASILEIRO S.A. - PETROBRAS',
      tradeName: 'Petrobras',
      status: 'ATIVA',
      lastUpdate: this.todayISO(30),
      cnpjCardUrl: this.receitaCardUrl('33000167000101')
    },
    '60746948000112': {
      cnpj: '60.746.948/0001-12',
      legalName: 'BANCO BRADESCO S.A.',
      tradeName: 'Bradesco',
      status: 'ATIVA',
      lastUpdate: this.todayISO(20),
      cnpjCardUrl: this.receitaCardUrl('60746948000112')
    },
    '00623904000173': {
      cnpj: '00.623.904/0001-73',
      legalName: 'BANCO SANTANDER (BRASIL) S.A.',
      tradeName: 'Santander',
      status: 'ATIVA',
      lastUpdate: this.todayISO(18),
      cnpjCardUrl: this.receitaCardUrl('00623904000173')
    },
    '53113791000122': {
      cnpj: '53.113.791/0001-22',
      legalName: 'TOTVS S.A.',
      tradeName: 'TOTVS',
      status: 'ATIVA',
      lastUpdate: this.todayISO(25),
      cnpjCardUrl: this.receitaCardUrl('53113791000122')
    },
    '19131243000197': {
      cnpj: '19.131.243/0001-97',
      legalName: 'MERCADO LIVRE COMÉRCIO ATACADISTA S.A.',
      tradeName: 'Mercado Livre',
      status: 'ATIVA',
      lastUpdate: this.todayISO(15),
      cnpjCardUrl: this.receitaCardUrl('19131243000197')
    },
    '47960950000121': {
      cnpj: '47.960.950/0001-21',
      legalName: 'MAGAZINE LUIZA S.A.',
      tradeName: 'Magazine Luiza',
      status: 'ATIVA',
      lastUpdate: this.todayISO(22),
      cnpjCardUrl: this.receitaCardUrl('47960950000121')
    }
  };

  steps: Step[] = [
    {
      id: 'solicitacao',
      label: 'Solicitação de compras',
      helper: 'Cadastro inicial, escopo e anexos',
      documentLabel: 'Formulário de solicitação'
    },
    {
      id: 'autorizacao',
      label: 'Autorização de compras',
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
      id: 'empenho',
      label: 'Realização de empenho',
      helper: 'Reserva orçamentária e lastro contábil',
      documentLabel: 'Reserva orçamentária'
    },
    {
      id: 'conclusao',
      label: 'Conclusão da compra',
      helper: 'Integrações com patrimônio e almoxarifado',
      documentLabel: 'Termo de conclusão'
    }
  ];

  activeStep: StepId = 'solicitacao';

  requests: PurchaseRequest[] = [
    {
      id: 'SC-2024/091',
      title: 'Aquisição de notebooks corporativos Lenovo',
      type: 'bem',
      requester: 'Tecnologia da Informação',
      area: 'TI Corporativa',
      expectedDate: this.todayISO(10),
      value: 148000,
      justification: 'Padronizar estações com Windows 11 Pro, criptografia e suporte remoto para equipes de campo.',
      status: 'cotacoes',
      approval: {
        director: 'Marina Prado (COO)',
        decision: 'aprovado',
        date: this.todayISO(14),
        notes: 'Aprovado com recurso do centro 3.3.90.30 e entrega faseada em dois lotes.'
      },
      budgetCenter: '3.3.90.30',
      winnerSupplier: 'Magazine Luiza'
    },
    {
      id: 'SC-2024/134',
      title: 'Contrato de manutenção predial preventiva',
      type: 'servico',
      requester: 'Infraestrutura',
      area: 'Operações',
      expectedDate: this.todayISO(20),
      value: 12500,
      justification: 'Plano trimestral de manutenção para evitar paradas das salas.',
      status: 'empenho',
      approval: {
        director: 'Carlos Menezes',
        decision: 'aprovado',
        date: this.todayISO(30),
        notes: 'Priorizar fornecedores homologados.'
      },
      budgetCenter: '3.3.90.39',
      empenhoNumber: 'EMP-058/2024',
      winnerSupplier: 'Bradesco Serviços'
    },
    {
      id: 'SC-2024/205',
      title: 'Compra emergencial de materiais de limpeza',
      type: 'produto',
      requester: 'Serviços Gerais',
      area: 'Administrativo',
      expectedDate: this.todayISO(40),
      value: 248,
      justification: 'Reposição de álcool 70% e detergentes para salas de atendimento. Valor abaixo do limite de dispensa.',
      status: 'cotacoes',
      quotationExemptionReason: 'Valor estimado inferior ao limite de R$ 300,00 para cotação prévia.'
    }
  ];

  quotes: Record<string, Quotation[]> = {
    'SC-2024/091': [
      {
        supplier: 'Magazine Luiza',
        legalName: 'MAGAZINE LUIZA S.A.',
        cnpj: this.formatCnpj('47960950000121'),
        cnpjStatus: 'ATIVA',
        cnpjCheckedAt: this.todayISO(2),
        cnpjCardUrl: this.receitaCardUrl('47960950000121'),
        value: 142000,
        delivery: this.todayISO(7),
        validity: this.todayISO(20),
        compliance: 'ok',
        notes: 'Inclui suporte onsite para configuração inicial dos notebooks.',
        isWinner: true
      },
      {
        supplier: 'Mercado Livre',
        legalName: 'MERCADO LIVRE COMÉRCIO ATACADISTA S.A.',
        cnpj: this.formatCnpj('19131243000197'),
        cnpjStatus: 'ATIVA',
        cnpjCheckedAt: this.todayISO(1),
        cnpjCardUrl: this.receitaCardUrl('19131243000197'),
        value: 138500,
        delivery: this.todayISO(8),
        validity: this.todayISO(22),
        compliance: 'ok',
        notes: 'Entrega consolidada no hub Campinas com NF-e emitida.'
      },
      {
        supplier: 'TOTVS',
        legalName: 'TOTVS S.A.',
        cnpj: this.formatCnpj('53113791000122'),
        cnpjStatus: 'ATIVA',
        cnpjCheckedAt: this.todayISO(3),
        cnpjCardUrl: this.receitaCardUrl('53113791000122'),
        value: 168000,
        delivery: this.todayISO(9),
        validity: this.todayISO(18),
        compliance: 'pendente',
        notes: 'Necessita comprovar cadeia de distribuição autorizada pela Lenovo.'
      }
    ],
    'SC-2024/134': [
      {
        supplier: 'Bradesco Serviços',
        legalName: 'BANCO BRADESCO S.A.',
        cnpj: this.formatCnpj('60746948000112'),
        cnpjStatus: 'ATIVA',
        cnpjCheckedAt: this.todayISO(4),
        cnpjCardUrl: this.receitaCardUrl('60746948000112'),
        value: 11800,
        delivery: this.todayISO(15),
        validity: this.todayISO(40),
        compliance: 'ok',
        notes: 'Equipe técnica credenciada com check-list mensal e plantão 24h.'
      }
    ]
  };

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

  reservations: BudgetReservation[] = [
    {
      requestId: 'SC-2024/134',
      center: '3.3.90.39',
      value: 12000,
      status: 'reservado',
      observation: 'Reserva vinculada ao contrato trimestral',
      createdAt: this.todayISO(2)
    }
  ];

  integrationChecklist: Record<string, IntegrationChecklist> = {
    'SC-2024/091': {
      patrimony: true,
      warehouse: false,
      contracts: false
    },
    'SC-2024/134': {
      patrimony: false,
      warehouse: false,
      contracts: true
    },
    'SC-2024/205': {
      patrimony: false,
      warehouse: true,
      contracts: false
    }
  };

  selectedRequestId = this.requests[0]?.id ?? '';

  newRequestForm = {
    title: '',
    type: 'produto' as PurchaseType,
    requester: '',
    area: 'Operações',
    expectedDate: this.todayISO(15),
    value: 0,
    justification: '',
    budgetCenter: '3.3.90.30'
  };

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
    delivery: this.todayISO(5),
    validity: this.todayISO(20),
    compliance: 'ok' as Quotation['compliance'],
    notes: ''
  };

  cnpjValidationError: string | null = null;

  empenhoForm = {
    center: '3.3.90.30',
    value: 0,
    empenhoNumber: '',
    observation: ''
  };

  conclusionForm = {
    documentNumber: '',
    observation: '',
    sendToPatrimony: false,
    sendToWarehouse: false
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

  get pendingApprovals(): number {
    return this.requests.filter((req) => req.status === 'autorizacao').length;
  }

  get pendingQuotes(): number {
    return this.requests.filter((req) => req.status === 'cotacoes').length;
  }

  get readyToConclude(): number {
    return this.requests.filter((req) => req.status === 'empenho').length;
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
  }

  createRequest(): void {
    if (!this.newRequestForm.title || !this.newRequestForm.requester || !this.newRequestForm.justification) {
      return;
    }

    const newRequest: PurchaseRequest = {
      id: `REQ-${Math.floor(Math.random() * 9000) + 1000}`,
      title: this.newRequestForm.title,
      type: this.newRequestForm.type,
      requester: this.newRequestForm.requester,
      area: this.newRequestForm.area,
      expectedDate: this.newRequestForm.expectedDate,
      value: Number(this.newRequestForm.value),
      justification: this.newRequestForm.justification,
      status: 'solicitacao',
      budgetCenter: this.newRequestForm.budgetCenter,
      quotationExemptionReason:
        Number(this.newRequestForm.value) < this.quotationThreshold
          ? `Valor estimado inferior ao limite de R$ ${this.quotationThreshold.toFixed(2)} para cotação prévia.`
          : undefined
    };

    this.requests = [newRequest, ...this.requests];
    this.selectedRequestId = newRequest.id;
    this.activeStep = 'autorizacao';
    this.newRequestForm = {
      title: '',
      type: 'produto',
      requester: '',
      area: 'Operações',
      expectedDate: this.todayISO(15),
      value: 0,
      justification: '',
      budgetCenter: '3.3.90.30'
    };
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
      status: this.approvalForm.decision === 'aprovado' ? 'cotacoes' : 'autorizacao'
    };

    this.updateRequest(updated);
  }

  addQuotation(): void {
    if (!this.selectedRequest || !this.newQuoteForm.value) return;

    const cnpjValidation = this.validateSupplierCnpj(this.newQuoteForm.cnpj);
    if (!cnpjValidation.valid) {
      this.cnpjValidationError = cnpjValidation.message ?? 'CNPJ inválido';
      return;
    }

    this.cnpjValidationError = null;

    const registry = cnpjValidation.record;
    const supplierLabel = registry?.tradeName || this.newQuoteForm.supplier || registry?.legalName || '';
    const legalName = registry?.legalName || this.newQuoteForm.companyName || supplierLabel;
    const cardUrl = registry?.cnpjCardUrl || this.newQuoteForm.cnpjCardUrl;

    const quotes = this.quotes[this.selectedRequestId] ?? [];
    this.quotes = {
      ...this.quotes,
      [this.selectedRequestId]: [
        ...quotes,
        {
          supplier: supplierLabel,
          legalName,
          cnpj: this.formatCnpj(this.newQuoteForm.cnpj),
          cnpjStatus: registry?.status ?? 'ATIVA',
          cnpjCheckedAt: this.todayISO(),
          cnpjCardUrl: cardUrl || this.receitaCardUrl(this.newQuoteForm.cnpj),
          value: Number(this.newQuoteForm.value),
          delivery: this.newQuoteForm.delivery,
          validity: this.newQuoteForm.validity,
          compliance: this.newQuoteForm.compliance,
          notes: this.newQuoteForm.notes
        }
      ]
    };

    this.newQuoteForm = {
      supplier: '',
      companyName: '',
      cnpj: '',
      cnpjCardUrl: '',
      value: 0,
      delivery: this.todayISO(5),
      validity: this.todayISO(20),
      compliance: 'ok',
      notes: ''
    };
  }

  markWinner(quote: Quotation): void {
    if (!this.selectedRequest) return;

    const updatedQuotes = (this.quotes[this.selectedRequestId] ?? []).map((item) => ({
      ...item,
      isWinner: item === quote
    }));

    this.quotes = { ...this.quotes, [this.selectedRequestId]: updatedQuotes };

    const updatedRequest: PurchaseRequest = {
      ...this.selectedRequest,
      winnerSupplier: quote.supplier,
      quotationDispensed: false,
      status: 'empenho'
    };

    this.updateRequest(updatedRequest);
    this.activeStep = 'empenho';
  }

  dispenseQuotation(): void {
    if (!this.selectedRequest) return;

    const updatedRequest: PurchaseRequest = {
      ...this.selectedRequest,
      quotationDispensed: true,
      quotationExemptionReason:
        this.selectedRequest.quotationExemptionReason ??
        `Valor estimado abaixo de R$ ${this.quotationThreshold.toFixed(2)}: dispensa de cotação liberada.`,
      status: 'empenho'
    };

    this.updateRequest(updatedRequest);
    this.activeStep = 'empenho';
  }

  registerReservation(): void {
    if (!this.selectedRequest || !this.empenhoForm.center || !this.empenhoForm.value) return;

    const envelope = this.budgetEnvelopes.find((item) => item.center === this.empenhoForm.center);
    const requested = Number(this.empenhoForm.value);
    const hasBalance = envelope ? envelope.available >= requested : false;
    const status: ReservationStatus = hasBalance ? 'reservado' : 'insuficiente';

    if (envelope && hasBalance) {
      this.budgetEnvelopes = this.budgetEnvelopes.map((item) =>
        item.center === envelope.center ? { ...item, available: item.available - requested } : item
      );
    }

    this.reservations = [
      {
        requestId: this.selectedRequest.id,
        center: this.empenhoForm.center,
        value: requested,
        status,
        observation: this.empenhoForm.observation || 'Reserva registrada',
        createdAt: this.todayISO()
      },
      ...this.reservations
    ];

    const updatedRequest: PurchaseRequest = {
      ...this.selectedRequest,
      budgetCenter: this.empenhoForm.center,
      empenhoNumber: this.empenhoForm.empenhoNumber || this.selectedRequest.empenhoNumber,
      status: status === 'reservado' ? 'conclusao' : 'empenho'
    };

    this.updateRequest(updatedRequest);
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

  prefillSupplierFromCnpj(): void {
    const registry = this.lookupSupplier(this.newQuoteForm.cnpj);
    if (!registry) return;

    this.newQuoteForm.companyName = registry.legalName;
    this.newQuoteForm.supplier = registry.tradeName || registry.legalName;
    this.newQuoteForm.cnpjCardUrl = registry.cnpjCardUrl;
    this.cnpjValidationError = null;
  }

  validateSupplierCnpj(cnpj: string): { valid: boolean; record?: SupplierRegistryEntry; message?: string } {
    const normalized = this.normalizeCnpj(cnpj);
    if (!normalized) {
      return { valid: false, message: 'Informe o CNPJ do fornecedor.' };
    }

    if (!this.isValidCnpj(normalized)) {
      return { valid: false, message: 'CNPJ inválido pelo cálculo de dígitos verificadores.' };
    }

    const registry = this.lookupSupplier(normalized);
    if (!registry) {
      return {
        valid: false,
        message: 'CNPJ não localizado na consulta da Receita Federal. Inclua apenas fornecedores ativos.'
      };
    }

    if (registry.status !== 'ATIVA') {
      return { valid: false, message: `Situação cadastral: ${registry.status}.` };
    }

    return { valid: true, record: registry };
  }

  private lookupSupplier(cnpj: string): SupplierRegistryEntry | undefined {
    return this.cnpjRegistry[this.normalizeCnpj(cnpj)];
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

  private receitaCardUrl(cnpj: string): string {
    return `https://servicos.receita.economia.gov.br/servicos/cnpjreva/cnpjreva_solicitacao.asp?cnpj=${this.normalizeCnpj(cnpj)}`;
  }

  private updateRequest(updated: PurchaseRequest): void {
    this.requests = this.requests.map((req) => (req.id === updated.id ? updated : req));
  }
}
