import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { of } from 'rxjs';
import { catchError, finalize, retry } from 'rxjs/operators';
import { BeneficiaryService } from '../../services/beneficiary.service';

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
    pending: 0
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
          this.stats = { total: 0, active: 0, pending: 0 };
          return of({ beneficiarios: [] });
        }),
        finalize(() => {
          this.loading = false;
        })
      )
      .subscribe(({ beneficiarios }) => {
        const active = beneficiarios.filter((beneficiary) => this.isActive(beneficiary.status)).length;
        const pending = beneficiarios.filter((beneficiary) => this.hasPendingStatus(beneficiary.status)).length;

        this.stats = {
          total: beneficiarios.length,
          active,
          pending
        };
      });
  }

  private isActive(status?: string): boolean {
    return (status ?? '').toLowerCase() === 'ativo';
  }

  private hasPendingStatus(status?: string): boolean {
    const normalized = (status ?? '').toLowerCase();
    return normalized.includes('pend') || normalized.includes('análise') || normalized.includes('analise');
  }
}
