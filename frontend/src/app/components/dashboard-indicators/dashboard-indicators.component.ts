import { Component, OnInit } from '@angular/core';
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
  selector: 'app-dashboard-indicators',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard-indicators.component.html',
  styleUrls: ['../dashboard/dashboard.component.scss']
})
export class DashboardIndicatorsComponent implements OnInit {
  filters = {
    startDate: '',
    endDate: ''
  };

  constructor(public readonly dashboardService: DashboardAssistenciaService) {}

  ngOnInit(): void {
    this.dashboardService.fetch();
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

  get termos(): DashboardAssistenciaResponse['termos'] | null {
    return this.dashboardService.data()?.termos ?? null;
  }

  refresh(): void {
    this.dashboardService.fetch({
      startDate: this.filters.startDate || null,
      endDate: this.filters.endDate || null
    });
  }
}
