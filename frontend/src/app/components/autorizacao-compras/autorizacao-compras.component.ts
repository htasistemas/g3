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
  patrimonyRegistration?: boolean;
  warehouseRegistration?: boolean;
}

interface Quotation {
  supplier: string;
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
      id: 'REQ-2401',
      title: 'Reposição de materiais pedagógicos',
      type: 'produto',
      requester: 'Coordenação Pedagógica',
      area: 'Educação',
      expectedDate: this.todayISO(10),
      value: 14800,
      justification: 'Garantir continuidade das oficinas de reforço com kits completos.',
      status: 'cotacoes',
      approval: {
        director: 'Marina Prado',
        decision: 'aprovado',
        date: this.todayISO(14),
        notes: 'Aprovado com recurso do centro 3.3.90.30'
      },
      budgetCenter: '3.3.90.30',
      winnerSupplier: 'Papelaria Central'
    },
    {
      id: 'REQ-3202',
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
      empenhoNumber: 'EMP-058/2024'
    },
    {
      id: 'REQ-4001',
      title: 'Aquisição de veículo utilitário',
      type: 'bem',
      requester: 'Assistência Social',
      area: 'Logística',
      expectedDate: this.todayISO(40),
      value: 185000,
      justification: 'Transporte de equipes e insumos para as unidades externas.',
      status: 'autorizacao',
      approval: {
        director: 'Diretoria Executiva',
        decision: 'ajustes',
        date: this.todayISO(5),
        notes: 'Rever opção de leasing e manutenção inclusa.'
      }
    }
  ];

  quotes: Record<string, Quotation[]> = {
    'REQ-2401': [
      {
        supplier: 'Papelaria Central',
        value: 14200,
        delivery: this.todayISO(7),
        validity: this.todayISO(20),
        compliance: 'ok',
        notes: 'Inclui frete e montagem dos kits',
        isWinner: true
      },
      {
        supplier: 'EducaMais',
        value: 15600,
        delivery: this.todayISO(8),
        validity: this.todayISO(22),
        compliance: 'ok'
      },
      {
        supplier: 'Papel e Arte',
        value: 16800,
        delivery: this.todayISO(9),
        validity: this.todayISO(18),
        compliance: 'pendente',
        notes: 'Necessita comprovar certificação ambiental'
      }
    ],
    'REQ-3202': [
      {
        supplier: 'Serv Predial Ltda',
        value: 11800,
        delivery: this.todayISO(15),
        validity: this.todayISO(40),
        compliance: 'ok',
        notes: 'Inclui check-list mensal e plantão 24h'
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
      requestId: 'REQ-3202',
      center: '3.3.90.39',
      value: 12000,
      status: 'reservado',
      observation: 'Reserva vinculada ao contrato trimestral',
      createdAt: this.todayISO(2)
    }
  ];

  integrationChecklist: Record<string, IntegrationChecklist> = {
    'REQ-2401': {
      patrimony: false,
      warehouse: true,
      contracts: false
    },
    'REQ-3202': {
      patrimony: false,
      warehouse: false,
      contracts: true
    },
    'REQ-4001': {
      patrimony: true,
      warehouse: false,
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
    value: 0,
    delivery: this.todayISO(5),
    validity: this.todayISO(20),
    compliance: 'ok' as Quotation['compliance'],
    notes: ''
  };

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
      budgetCenter: this.newRequestForm.budgetCenter
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
    if (!this.selectedRequest || !this.newQuoteForm.supplier || !this.newQuoteForm.value) return;

    const quotes = this.quotes[this.selectedRequestId] ?? [];
    this.quotes = {
      ...this.quotes,
      [this.selectedRequestId]: [
        ...quotes,
        {
          supplier: this.newQuoteForm.supplier,
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

  private updateRequest(updated: PurchaseRequest): void {
    this.requests = this.requests.map((req) => (req.id === updated.id ? updated : req));
  }
}
