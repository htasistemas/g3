import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { of } from 'rxjs';
import { catchError, finalize, retry } from 'rxjs/operators';
import { BeneficiaryPayload, BeneficiaryService } from '../../services/beneficiary.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  loading = false;
  error: string | null = null;
  stats = {
    total: 0,
    active: 0,
    pending: 0,
    blocked: 0,
    inAnalysis: 0,
    updated: 0,
    withBenefits: 0
  };

  constructor(private readonly beneficiaryService: BeneficiaryService) {}

  ngOnInit(): void {
    this.loadBeneficiaryStats();
  }

  private loadBeneficiaryStats(): void {
    this.loading = true;
    this.error = null;

    this.beneficiaryService
      .list()
      .pipe(
        retry(1),
        catchError((error) => {
          console.error('Falha ao carregar estatísticas de beneficiários', error);
          this.error = 'Não foi possível carregar os dados de beneficiários.';
          this.stats = {
            total: 0,
            active: 0,
            pending: 0,
            blocked: 0,
            inAnalysis: 0,
            updated: 0,
            withBenefits: 0
          };
          return of({ beneficiarios: [] });
        }),
        finalize(() => {
          this.loading = false;
        })
      )
      .subscribe(({ beneficiarios }) => this.calculateStats(beneficiarios));
  }

  private calculateStats(beneficiarios: BeneficiaryPayload[] = []): void {
    const normalized = beneficiarios.map((beneficiary) => (beneficiary.status ?? '').toUpperCase());

    const active = normalized.filter((status) => status === 'ATIVO').length;
    const pending = normalized.filter((status) => this.hasPendingStatus(status)).length;
    const blocked = normalized.filter((status) => status === 'BLOQUEADO').length;
    const inAnalysis = normalized.filter((status) => status === 'EM_ANALISE').length;
    const updated = normalized.filter((status) => status === 'DESATUALIZADO' || status === 'INCOMPLETO').length;
    const withBenefits = beneficiarios.filter((beneficiary) => this.hasBenefits(beneficiary.recebeBeneficio)).length;

    this.stats = {
      total: beneficiarios.length,
      active,
      pending,
      blocked,
      inAnalysis,
      updated,
      withBenefits
    };
  }

  private hasBenefits(recebeBeneficio?: string | boolean | null): boolean {
    if (typeof recebeBeneficio === 'string') {
      return recebeBeneficio.toLowerCase() === 'true' || recebeBeneficio === '1';
    }

    return Boolean(recebeBeneficio);
  }

  private hasPendingStatus(status?: string): boolean {
    const normalized = (status ?? '').toUpperCase();
    return normalized.includes('PEND') || normalized.includes('ANÁLISE') || normalized.includes('ANALISE');
  }
}
