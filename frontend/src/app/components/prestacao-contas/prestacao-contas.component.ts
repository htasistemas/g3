import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import {
  faArrowTrendUp,
  faBookOpen,
  faChartPie,
  faCircleCheck,
  faCircleExclamation,
  faCloudArrowUp,
  faFileLines,
  faHandHoldingHeart,
  faListCheck,
  faShareNodes
} from '@fortawesome/free-solid-svg-icons';

interface PrestacaoResumo {
  label: string;
  value: string;
  helper: string;
  emphasis?: boolean;
}

interface DestinacaoItem {
  title: string;
  description: string;
  percentage: number;
}

interface PrestacaoTab {
  id: 'resumo' | 'fluxo' | 'transparencia';
  label: string;
  helper: string;
}

@Component({
  selector: 'app-prestacao-contas',
  standalone: true,
  imports: [CommonModule, FontAwesomeModule, TelaPadraoComponent],
  templateUrl: './prestacao-contas.component.html',
  styleUrl: './prestacao-contas.component.scss'
})
export class PrestacaoContasComponent {
  readonly faBookOpen = faBookOpen;
  readonly faListCheck = faListCheck;
  readonly faFileLines = faFileLines;
  readonly faHandHoldingHeart = faHandHoldingHeart;
  readonly faShareNodes = faShareNodes;
  readonly faCloudArrowUp = faCloudArrowUp;
  readonly faCircleCheck = faCircleCheck;
  readonly faCircleExclamation = faCircleExclamation;
  readonly faArrowTrendUp = faArrowTrendUp;
  readonly faChartPie = faChartPie;

  tabs: PrestacaoTab[] = [
    {
      id: 'resumo',
      label: 'Resumo financeiro',
      helper: 'Visão geral dos recursos recebidos, aplicados e saldo disponível.'
    },
    {
      id: 'fluxo',
      label: 'Entradas e destinação',
      helper: 'Detalhamento das fontes de recursos e o percentual aplicado em cada frente.'
    },
    {
      id: 'transparencia',
      label: 'Transparência e evidências',
      helper: 'Linha do tempo das entregas, checklist e comprovantes anexados.'
    }
  ];

  activeTab: PrestacaoTab['id'] = this.tabs[0].id;

  resumoCards: PrestacaoResumo[] = [
    { label: 'Total recebido', value: 'R$ 326.500,00', helper: 'Somatório dos doadores ativos', emphasis: true },
    { label: 'Total aplicado', value: 'R$ 291.800,00', helper: 'Projetos e despesas comprovadas' },
    { label: 'Saldo para executar', value: 'R$ 34.700,00', helper: 'Disponível para novos repasses' },
    { label: 'Prestado no mês', value: 'R$ 118.400,00', helper: '+ 9% vs. mês anterior' }
  ];

  destinacoes: DestinacaoItem[] = [
    {
      title: 'Segurança alimentar',
      description: 'Cestas básicas, hortifrúti e cartões alimentação',
      percentage: 42
    },
    {
      title: 'Educação e desenvolvimento',
      description: 'Cursos, materiais e bolsas de apoio',
      percentage: 27
    },
    {
      title: 'Saúde e bem-estar',
      description: 'Atendimento clínico, exames e transporte',
      percentage: 18
    },
    {
      title: 'Gestão e transparência',
      description: 'Auditoria, comunicação e compliance',
      percentage: 13
    }
  ];

  recebimentos = [
    {
      fonte: 'Doações recorrentes',
      valor: 'R$ 74.200,00',
      periodicidade: 'Débito mensal (420 apoiadores)',
      status: 'OK'
    },
    { fonte: 'Convênio estadual', valor: 'R$ 120.000,00', periodicidade: 'Trimestral', status: 'Pendente NFe' },
    { fonte: 'Fundação Luz', valor: 'R$ 64.500,00', periodicidade: 'Projeto Juventudes', status: 'OK' },
    { fonte: 'Campanha emergencial', valor: 'R$ 51.800,00', periodicidade: 'Arrecadação digital', status: 'OK' }
  ];

  comprovantes = [
    {
      title: 'Planilha detalhada do mês',
      description: 'Download em .xlsx com recibos e notas anexadas',
      icon: faFileLines
    },
    {
      title: 'Relatório narrativo',
      description: 'Resultados entregues por projeto e indicadores sociais',
      icon: faBookOpen
    },
    {
      title: 'Evidências e fotos',
      description: 'Galeria para enviar imagens das ações financiadas',
      icon: faCloudArrowUp
    }
  ];

  timelines = [
    {
      title: 'Execução do Plano de Trabalho',
      detail: 'Entrega do módulo de capacitação feminina e kits de estudo',
      status: 'concluido'
    },
    {
      title: 'Transparência aos doadores',
      detail: 'Envio da newsletter mensal com prestação de contas',
      status: 'andamento'
    },
    {
      title: 'Projeção do próximo trimestre',
      detail: 'Planejamento de metas e cronograma de desembolso',
      status: 'pendente'
    }
  ];

  get activeTabIndex(): number {
    return this.tabs.findIndex((tab) => tab.id === this.activeTab);
  }

  get activeTabHelper(): string {
    return this.tabs[this.activeTabIndex]?.helper ?? '';
  }

  changeTab(tabId: PrestacaoTab['id']): void {
    this.activeTab = tabId;
  }
}
