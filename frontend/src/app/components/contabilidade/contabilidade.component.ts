import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
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
  helper: string;
  icon: any;
  trend?: 'up' | 'down';
}

interface CashFlowEntry {
  period: string;
  receivable: number;
  payable: number;
  projection: number;
}

interface AgendaEntry {
  title: string;
  counterparty: string;
  dueDate: string;
  amount: number;
  type: 'receber' | 'pagar';
  status: 'aberto' | 'pago' | 'atrasado';
}

interface FinanceTab {
  id: 'visao-geral' | 'fluxo' | 'relatorios';
  label: string;
  helper: string;
  badge?: string;
}

interface BankAccount {
  bankName: string;
  accountNumber: string;
  type: 'corrente' | 'aplicacao' | 'projeto';
  balance: number;
  lastUpdate: string;
}

interface FinancialMovement {
  description: string;
  counterparty: string;
  date: string;
  accountNumber: string;
  amount: number;
  type: 'entrada' | 'saida';
  category: string;
}

interface AmendmentControl {
  name: string;
  lawReference: string;
  expectedDate: string;
  amount: number;
  alertDays: number;
  status: 'previsto' | 'empenhado' | 'recebido';
  notes: string;
}

@Component({
  selector: 'app-contabilidade',
  standalone: true,
  imports: [CommonModule, FormsModule, FontAwesomeModule],
  templateUrl: './contabilidade.component.html',
  styleUrl: './contabilidade.component.scss'
})
export class ContabilidadeComponent {
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

  tabs: FinanceTab[] = [
    {
      id: 'visao-geral',
      label: 'Visão geral financeira',
      helper: 'Indicadores e obrigações imediatas',
      badge: 'Resumo'
    },
    {
      id: 'fluxo',
      label: 'Fluxo de caixa e projeções',
      helper: 'Entradas, saídas e saldo esperado',
      badge: 'Planejamento'
    },
    {
      id: 'relatorios',
      label: 'Relatórios e documentos',
      helper: 'Demonstrativos e materiais de apoio',
      badge: 'Exportar'
    }
  ];

  activeTab: FinanceTab['id'] = 'visao-geral';

  summaryCards: MetricCard[] = [];
  cashFlow: CashFlowEntry[] = [];
  agenda: AgendaEntry[] = [];
  bankAccounts: BankAccount[] = [];
  financialMovements: FinancialMovement[] = [];
  amendmentControls: AmendmentControl[] = [];
  upcomingReceivables: AgendaEntry[] = [];
  upcomingPayables: AgendaEntry[] = [];
  alerts: string[] = [];

  newEntry: AgendaEntry = {
    title: '',
    counterparty: '',
    dueDate: this.todayISO(),
    amount: 0,
    type: 'receber',
    status: 'aberto'
  };

  newAccount: BankAccount = {
    bankName: '',
    accountNumber: '',
    type: 'corrente',
    balance: 0,
    lastUpdate: this.todayISO()
  };

  newAmendment: AmendmentControl = {
    name: '',
    lawReference: '',
    expectedDate: this.todayISO(),
    amount: 0,
    alertDays: 15,
    status: 'previsto',
    notes: ''
  };

  newMovement: FinancialMovement = {
    description: '',
    counterparty: '',
    date: this.todayISO(),
    accountNumber: '',
    amount: 0,
    type: 'entrada',
    category: 'Operacional'
  };

  reportTotals = {
    recebimentos: 0,
    pagamentos: 0,
    saldoProjetado: 0,
    saldoAReceber: 0
  };

  constructor() {
    this.refreshPanels();
  }

  get activeTabIndex(): number {
    return this.tabs.findIndex((tab) => tab.id === this.activeTab);
  }

  get counterpartySuggestions(): string[] {
    const fromEntries = this.agenda.map((item) => item.counterparty);
    const fromMovements = this.financialMovements.map((item) => item.counterparty);
    return Array.from(new Set([...fromEntries, ...fromMovements])).filter(Boolean);
  }

  changeTab(tabId: FinanceTab['id']): void {
    this.activeTab = tabId;
  }

  registerEntry(): void {
    if (!this.newEntry.title || !this.newEntry.counterparty || !this.newEntry.dueDate || !this.newEntry.amount) {
      return;
    }

    this.agenda = [
      ...this.agenda,
      {
        ...this.newEntry,
        amount: Number(this.newEntry.amount)
      }
    ];

    this.newEntry = {
      title: '',
      counterparty: '',
      dueDate: this.todayISO(),
      amount: 0,
      type: 'receber',
      status: 'aberto'
    };

    this.refreshPanels();
  }

  addBankAccount(): void {
    if (!this.newAccount.bankName || !this.newAccount.accountNumber) {
      return;
    }

    const normalizedBalance = Number(this.newAccount.balance);
    const accountExists = this.bankAccounts.some((acc) => acc.accountNumber === this.newAccount.accountNumber);

    if (accountExists) {
      this.bankAccounts = this.bankAccounts.map((acc) =>
        acc.accountNumber === this.newAccount.accountNumber ? { ...this.newAccount, balance: normalizedBalance } : acc
      );
    } else {
      this.bankAccounts = [...this.bankAccounts, { ...this.newAccount, balance: normalizedBalance }];
    }

    this.newAccount = {
      bankName: '',
      accountNumber: '',
      type: 'corrente',
      balance: 0,
      lastUpdate: this.todayISO()
    };

    this.refreshPanels();
  }

  registerMovement(): void {
    if (!this.newMovement.description || !this.newMovement.accountNumber || !this.newMovement.date) {
      return;
    }

    const normalizedAmount = Number(this.newMovement.amount);
    this.financialMovements = [
      ...this.financialMovements,
      {
        ...this.newMovement,
        amount: normalizedAmount
      }
    ];

    this.bankAccounts = this.bankAccounts.map((account) => {
      if (account.accountNumber !== this.newMovement.accountNumber) {
        return account;
      }

      const newBalance =
        this.newMovement.type === 'entrada' ? account.balance + normalizedAmount : account.balance - normalizedAmount;

      return { ...account, balance: newBalance, lastUpdate: this.newMovement.date };
    });

    this.newMovement = {
      description: '',
      counterparty: '',
      date: this.todayISO(),
      accountNumber: '',
      amount: 0,
      type: 'entrada',
      category: 'Operacional'
    };

    this.refreshPanels();
  }

  addAmendment(): void {
    if (!this.newAmendment.name || !this.newAmendment.expectedDate || !this.newAmendment.amount) {
      return;
    }

    this.amendmentControls = [
      ...this.amendmentControls,
      { ...this.newAmendment, amount: Number(this.newAmendment.amount) }
    ];

    this.newAmendment = {
      name: '',
      lawReference: '',
      expectedDate: this.todayISO(),
      amount: 0,
      alertDays: 15,
      status: 'previsto',
      notes: ''
    };

    this.refreshPanels();
  }

  setEntryStatus(entry: AgendaEntry, status: AgendaEntry['status']): void {
    this.agenda = this.agenda.map((item) => (item === entry ? { ...item, status } : item));
    this.refreshPanels();
  }

  setAmendmentStatus(amendment: AmendmentControl, status: AmendmentControl['status']): void {
    this.amendmentControls = this.amendmentControls.map((item) => (item === amendment ? { ...item, status } : item));
    this.refreshPanels();
  }

  printResumo(): void {
    window.print();
  }

  getStatusLabel(entry: AgendaEntry): string {
    switch (entry.status) {
      case 'pago':
        return 'Pago';
      case 'atrasado':
        return 'Em atraso';
      default:
        return entry.type === 'receber' ? 'A receber' : 'A pagar';
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
    return parsed.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit' });
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
      .filter((account) => account.type !== 'corrente')
      .reduce((total, account) => total + account.balance, 0);

    this.summaryCards = [
      {
        label: 'A receber (30 dias)',
        value: this.formatCurrency(receivable),
        helper: 'Valores lançados e aguardando entrada',
        icon: faWallet,
        trend: receivable >= payable ? 'up' : undefined
      },
      {
        label: 'A pagar (30 dias)',
        value: this.formatCurrency(payable),
        helper: 'Contas registradas com data limite',
        icon: faMoneyBillTransfer,
        trend: payable > receivable ? 'down' : undefined
      },
      {
        label: 'Em caixa',
        value: this.formatCurrency(cashBalance),
        helper: 'Saldo consolidado das contas bancárias',
        icon: faPiggyBank
      },
      {
        label: 'Aplicado em bancos',
        value: this.formatCurrency(investments),
        helper: 'Aplicações e recursos vinculados',
        icon: faChartLine
      }
    ];
  }

  private refreshPipelines(): void {
    const ordered = [...this.agenda].sort((a, b) => {
      const dateA = this.parseDate(a.dueDate)?.getTime() ?? 0;
      const dateB = this.parseDate(b.dueDate)?.getTime() ?? 0;
      return dateA - dateB;
    });

    this.upcomingReceivables = ordered.filter((item) => item.type === 'receber' && item.status !== 'pago');
    this.upcomingPayables = ordered.filter((item) => item.type === 'pagar' && item.status !== 'pago');
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
      .filter((item) => item.status !== 'pago')
      .filter((item) => {
        const parsed = this.parseDate(item.dueDate);
        if (!parsed) return false;
        const diff = this.daysBetween(today, parsed);
        return diff <= 7;
      })
      .map((item) => `${item.title} vence em ${this.formatDate(item.dueDate)}`);

    const amendmentAlerts = this.amendmentControls
      .filter((amendment) => amendment.status !== 'recebido')
      .filter((amendment) => {
        const parsed = this.parseDate(amendment.expectedDate);
        if (!parsed) return false;
        const diff = this.daysBetween(today, parsed);
        return diff <= amendment.alertDays;
      })
      .map((amendment) => `Emenda ${amendment.name} prevista para ${this.formatDate(amendment.expectedDate)}`);

    this.alerts = [...dueSoon, ...amendmentAlerts];
  }

  private refreshReport(): void {
    const recebimentos = this.financialMovements
      .filter((movement) => movement.type === 'entrada')
      .reduce((total, movement) => total + movement.amount, 0);
    const pagamentos = this.financialMovements
      .filter((movement) => movement.type === 'saida')
      .reduce((total, movement) => total + movement.amount, 0);
    const saldoAReceber = this.agenda
      .filter((item) => item.type === 'receber' && item.status !== 'pago')
      .reduce((total, item) => total + item.amount, 0);

    this.reportTotals = {
      recebimentos,
      pagamentos,
      saldoProjetado: this.totalCashBalance() + saldoAReceber - pagamentos,
      saldoAReceber
    };
  }

  private sumEntries(type: AgendaEntry['type'], daysAhead: number): number {
    const today = new Date();
    const limit = new Date();
    limit.setDate(limit.getDate() + daysAhead);

    return this.agenda
      .filter((item) => item.type === type && item.status !== 'pago')
      .filter((item) => {
        const parsed = this.parseDate(item.dueDate);
        if (!parsed) return false;
        return parsed <= limit && parsed >= today;
      })
      .reduce((total, item) => total + item.amount, 0);
  }

  private sumEntriesInRange(type: AgendaEntry['type'], start: Date, end: Date): number {
    return this.agenda
      .filter((item) => item.type === type && item.status !== 'pago')
      .filter((item) => {
        const parsed = this.parseDate(item.dueDate);
        if (!parsed) return false;
        return parsed >= start && parsed <= end;
      })
      .reduce((total, item) => total + item.amount, 0);
  }

  private totalCashBalance(): number {
    return this.bankAccounts.reduce((total, account) => total + account.balance, 0);
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

  private parseDate(date: string): Date | null {
    if (!date) return null;
    const parsed = new Date(date);
    if (isNaN(parsed.getTime())) return null;
    return parsed;
  }

  private daysBetween(start: Date, end: Date): number {
    const diff = end.getTime() - start.getTime();
    return Math.ceil(diff / (1000 * 60 * 60 * 24));
  }
}
