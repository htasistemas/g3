import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
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
  amount: string;
  type: 'receber' | 'pagar';
  status: 'aberto' | 'pago' | 'atrasado';
}

interface FinanceTab {
  id: 'visao-geral' | 'fluxo' | 'relatorios';
  label: string;
  helper: string;
  badge?: string;
}

@Component({
  selector: 'app-contabilidade',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule],
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

  summaryCards: MetricCard[] = [
    {
      label: 'A receber (30 dias)',
      value: 'R$ 182.450,00',
      helper: '+ 12% vs. mês anterior',
      icon: faWallet,
      trend: 'up'
    },
    {
      label: 'A pagar (30 dias)',
      value: 'R$ 126.800,00',
      helper: '- 6% vs. mês anterior',
      icon: faMoneyBillTransfer,
      trend: 'down'
    },
    {
      label: 'Em caixa',
      value: 'R$ 58.230,00',
      helper: 'Disponível para hoje',
      icon: faPiggyBank
    },
    {
      label: 'Aplicado em bancos',
      value: 'R$ 214.900,00',
      helper: 'Aplicações e contas correntes',
      icon: faChartLine
    }
  ];

  cashFlow: CashFlowEntry[] = [
    { period: 'Semana 1', receivable: 48000, payable: 36500, projection: 11500 },
    { period: 'Semana 2', receivable: 38500, payable: 31800, projection: 6700 },
    { period: 'Semana 3', receivable: 52500, payable: 24800, projection: 27700 },
    { period: 'Semana 4', receivable: 43200, payable: 33700, projection: 9500 }
  ];

  agenda: AgendaEntry[] = [
    {
      title: 'Mensalidade corporativa',
      counterparty: 'Grupo Horizonte',
      dueDate: '05/07',
      amount: 'R$ 18.200,00',
      type: 'receber',
      status: 'aberto'
    },
    {
      title: 'Aluguel da sede',
      counterparty: 'Imobiliária Vida Justa',
      dueDate: '07/07',
      amount: 'R$ 12.500,00',
      type: 'pagar',
      status: 'pago'
    },
    {
      title: 'Fornecimento de cestas',
      counterparty: 'Rede Solidária',
      dueDate: '11/07',
      amount: 'R$ 26.900,00',
      type: 'pagar',
      status: 'aberto'
    },
    {
      title: 'Repasse convênio municipal',
      counterparty: 'Prefeitura',
      dueDate: '14/07',
      amount: 'R$ 42.000,00',
      type: 'receber',
      status: 'atrasado'
    },
    {
      title: 'Prestadores de serviços',
      counterparty: 'Assistência médica',
      dueDate: '21/07',
      amount: 'R$ 8.700,00',
      type: 'pagar',
      status: 'aberto'
    }
  ];

  receivablePipeline = [
    { title: 'Convênios públicos', amount: 'R$ 96.000', due: 'Liberar notas até 08/07' },
    { title: 'Doações recorrentes', amount: 'R$ 41.500', due: 'Débito automático em 12/07' },
    { title: 'Campanhas ativas', amount: 'R$ 28.900', due: 'Meta 72% atingida' }
  ];

  payablePipeline = [
    { title: 'Equipe técnica', amount: 'R$ 34.200', due: 'Folha prevista para 28/06' },
    { title: 'Logística e combustível', amount: 'R$ 11.700', due: 'Roteiros urbanos e rurais' },
    { title: 'Projetos educacionais', amount: 'R$ 18.600', due: 'Execução do cronograma Q3' }
  ];

  get activeTabIndex(): number {
    return this.tabs.findIndex((tab) => tab.id === this.activeTab);
  }

  changeTab(tabId: FinanceTab['id']): void {
    this.activeTab = tabId;
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
}
