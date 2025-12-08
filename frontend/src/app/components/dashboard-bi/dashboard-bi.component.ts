import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { ChartResponse, CursosMetricasResponse, DashboardBiService } from '../../services/dashboard-bi.service';

@Component({
  selector: 'app-dashboard-bi',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard-bi.component.html',
  styleUrl: './dashboard-bi.component.scss'
})
export class DashboardBiComponent implements OnInit {
  beneficiariosStatus: ChartResponse | null = null;
  atendimentosMes: ChartResponse | null = null;
  atendimentosTipo: ChartResponse | null = null;
  cursosMetricas: CursosMetricasResponse | null = null;
  ivfFaixas: ChartResponse | null = null;
  loading = false;
  error: string | null = null;

  constructor(private readonly biService: DashboardBiService) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.error = null;
    forkJoin({
      beneficiariosStatus: this.biService.getBeneficiariosStatus(),
      atendimentosMes: this.biService.getAtendimentosMes(),
      atendimentosTipo: this.biService.getAtendimentosTipo(),
      cursosMetricas: this.biService.getCursosMetricas(),
      ivfFaixas: this.biService.getIvfFaixas()
    }).subscribe({
      next: (data) => {
        this.beneficiariosStatus = data.beneficiariosStatus;
        this.atendimentosMes = data.atendimentosMes;
        this.atendimentosTipo = data.atendimentosTipo;
        this.cursosMetricas = data.cursosMetricas;
        this.ivfFaixas = data.ivfFaixas;
        this.loading = false;
      },
      error: () => {
        this.error = 'Não foi possível carregar os indicadores do dashboard.';
        this.loading = false;
      }
    });
  }

  trackByIndex(index: number): number {
    return index;
  }
}
