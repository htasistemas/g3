import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { BeneficiarioPayload, BeneficiarioService } from '../../services/beneficiario.service';

@Component({
  selector: 'app-beneficiary-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './beneficiary-list.component.html',
  styleUrl: './beneficiary-list.component.scss'
})
export class BeneficiaryListComponent implements OnInit {
  beneficiaries: BeneficiarioPayload[] = [];
  filtered: BeneficiarioPayload[] = [];
  searchTerm = '';
  loading = false;
  error: string | null = null;

  constructor(private readonly beneficiaryService: BeneficiarioService, private readonly router: Router) {}

  ngOnInit(): void {
    this.loadBeneficiaries();
  }

  loadBeneficiaries(): void {
    this.loading = true;
    this.error = null;

    this.beneficiaryService.list().subscribe({
      next: ({ beneficiarios }) => {
        this.beneficiaries = beneficiarios ?? [];
        this.applyFilter();
        this.loading = false;
      },
      error: () => {
        this.error = 'Não foi possível carregar os beneficiários. Tente novamente.';
        this.loading = false;
      }
    });
  }

  applyFilter(): void {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) {
      this.filtered = [...this.beneficiaries];
      return;
    }

    this.filtered = this.beneficiaries.filter((beneficiary) => {
      const name = beneficiary.nomeCompleto?.toLowerCase() ?? '';
      const cpf = beneficiary.cpf?.toLowerCase() ?? beneficiary.documentos?.toLowerCase() ?? '';
      return name.includes(term) || cpf.includes(term);
    });
  }

  newBeneficiary(): void {
    this.router.navigate(['/beneficiarios/novo']);
  }

  editBeneficiary(id: number | undefined): void {
    if (!id) {
      return;
    }

    this.router.navigate(['/beneficiarios/editar', id]);
  }
}
