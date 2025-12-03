import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  DashboardAssistenciaResponse,
  DashboardAssistenciaService,
  DashboardAtendimento,
  DashboardFamilias,
  DashboardTop12
} from '../../services/dashboard-assistencia.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit, OnDestroy {
  filters = {
    startDate: '',
    endDate: ''
  };

  private liveRefreshId?: ReturnType<typeof setInterval>;

  constructor(public readonly dashboardService: DashboardAssistenciaService) {}

  ngOnInit(): void {
    this.dashboardService.fetch();
    this.startLiveUpdates();
  }

  ngOnDestroy(): void {
    if (this.liveRefreshId) {
      clearInterval(this.liveRefreshId);
    }
  }

  get loading(): boolean {
    return this.dashboardService.loading();
  }

  get top12(): DashboardTop12 | null {
    return this.dashboardService.data()?.top12 ?? null;
  }

  get atendimento(): DashboardAtendimento | null {
    return this.dashboardService.data()?.atendimento ?? null;
  }

  get familias(): DashboardFamilias | null {
    return this.dashboardService.data()?.familias ?? null;
  }

  get realtimeHighlights() {
    return [
      {
        label: 'Beneficiários agora',
        value: this.top12?.beneficiariosAtendidosPeriodo ?? 0,
        icon: 'monitor_heart',
        accent: 'emerald'
      },
      {
        label: 'Cursos ativos',
        value: this.top12?.cursosAtivos ?? 0,
        icon: 'cast_for_education',
        accent: 'indigo'
      },
      {
        label: 'Visitas domiciliares',
        value: this.top12?.visitasDomiciliares ?? 0,
        icon: 'home_pin',
        accent: 'amber'
      },
      {
        label: 'Execução financeira',
        value: this.top12?.execucaoFinanceira ?? 0,
        icon: 'stacked_line_chart',
        accent: 'sky',
        suffix: '%'
      }
    ];
  }

  get termos() {
    return this.dashboardService.data()?.termos ?? null;
  }

  refresh() {
    this.dashboardService.fetch(this.currentFilters());
  }

  private startLiveUpdates() {
    this.liveRefreshId = setInterval(() => {
      this.dashboardService.fetch(this.currentFilters());
    }, 30000);
  }

  private currentFilters() {
    return {
      startDate: this.filters.startDate || null,
      endDate: this.filters.endDate || null
    };
  }
}
