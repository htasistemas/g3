import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
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

    this.beneficiaryService.list().subscribe({
      next: ({ beneficiaries }) => {
        const active = beneficiaries.filter((beneficiary) => this.isActive(beneficiary.status)).length;
        const pending = beneficiaries.filter((beneficiary) => this.hasPendingStatus(beneficiary.status)).length;

        this.stats = {
          total: beneficiaries.length,
          active,
          pending
        };
        this.loading = false;
      },
      error: () => {
        this.error = 'Não foi possível carregar os dados de beneficiários.';
        this.loading = false;
      }
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
