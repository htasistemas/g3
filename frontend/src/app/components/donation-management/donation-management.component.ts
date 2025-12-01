import { CommonModule } from '@angular/common';
import { Component, computed, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faBoxOpen,
  faClipboardList,
  faFilePdf,
  faMagnifyingGlass,
  faPrint,
  faReceipt,
  faTimeline,
  faUserPlus
} from '@fortawesome/free-solid-svg-icons';

interface Beneficiary {
  name: string;
  document: string;
  birthDate?: string;
  phone?: string;
  address?: string;
  nextEligibilityDate?: string;
  lastDonationDate?: string;
}

interface StockItem {
  code: string;
  description: string;
  unit: string;
  currentStock: number;
  category: string;
}

interface DonationItem {
  itemCode: string;
  description: string;
  quantity: number;
  unit: string;
  notes?: string;
}

interface DonationRecord {
  id: string;
  date: string;
  beneficiary: Beneficiary;
  items: DonationItem[];
  notes?: string;
  deliveryDate: string;
}

@Component({
  selector: 'app-donation-management',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, FontAwesomeModule],
  templateUrl: './donation-management.component.html',
  styleUrl: './donation-management.component.scss'
})
export class DonationManagementComponent {
  readonly faUserPlus = faUserPlus;
  readonly faClipboardList = faClipboardList;
  readonly faMagnifyingGlass = faMagnifyingGlass;
  readonly faBoxOpen = faBoxOpen;
  readonly faTimeline = faTimeline;
  readonly faReceipt = faReceipt;
  readonly faFilePdf = faFilePdf;
  readonly faPrint = faPrint;

  private readonly fb = new FormBuilder();
  private readonly todayIso = new Date().toISOString().substring(0, 10);

  beneficiaries = signal<Beneficiary[]>([
    {
      name: 'Maria Fernanda Alves',
      document: '123.456.789-00',
      birthDate: '1991-06-12',
      phone: '(34) 99999-1122',
      address: 'Rua das Acácias, 210 - Bairro Primavera',
      nextEligibilityDate: this.addDaysToDate(this.todayIso, 5),
      lastDonationDate: this.addDaysToDate(this.todayIso, -25)
    },
    {
      name: 'João Pedro Duarte',
      document: '987.654.321-00',
      birthDate: '1986-02-03',
      phone: '(34) 98888-3344',
      address: 'Av. Goiás, 455 - Centro',
      nextEligibilityDate: this.addDaysToDate(this.todayIso, -2),
      lastDonationDate: this.addDaysToDate(this.todayIso, -40)
    },
    {
      name: 'Associação Vila Nova',
      document: '11.111.111/0001-11',
      phone: '(34) 3232-9090',
      address: 'Rua 7 de Setembro, 102',
      nextEligibilityDate: this.addDaysToDate(this.todayIso, 12),
      lastDonationDate: this.addDaysToDate(this.todayIso, -18)
    }
  ]);

  stockItems = signal<StockItem[]>([
    { code: 'ALM-001', description: 'Álcool em gel 70% 500ml', unit: 'Frasco', currentStock: 85, category: 'Higiene' },
    { code: 'ALM-002', description: 'Luvas descartáveis tamanho M', unit: 'Caixa', currentStock: 18, category: 'EPI' },
    { code: 'ALM-003', description: 'Papel sulfite A4 75g', unit: 'Resma', currentStock: 120, category: 'Escritório' },
    { code: 'ALM-004', description: 'Cartucho de impressora HP 664', unit: 'Unidade', currentStock: 6, category: 'Escritório' },
    { code: 'ALM-005', description: 'Cesta básica padrão', unit: 'Kit', currentStock: 42, category: 'Alimentos' }
  ]);

  donationHistory = signal<DonationRecord[]>([
    {
      id: 'DOA-2025-0004',
      date: this.todayIso,
      deliveryDate: this.todayIso,
      beneficiary: this.beneficiaries()[0],
      items: [
        {
          itemCode: 'ALM-005',
          description: 'Cesta básica padrão',
          quantity: 1,
          unit: 'Kit'
        }
      ],
      notes: 'Distribuição mensal programada.'
    }
  ]);

  donationForm: FormGroup = this.fb.group({
    beneficiary: this.fb.group({
      name: ['', Validators.required],
      document: ['', Validators.required],
      birthDate: [''],
      phone: [''],
      address: ['']
    }),
    deliveryDate: [this.todayIso, Validators.required],
    notes: ['']
  });

  itemForm: FormGroup = this.fb.group({
    itemCode: ['', Validators.required],
    quantity: [1, [Validators.required, Validators.min(1)]],
    notes: ['']
  });

  items = signal<DonationItem[]>([]);

  selectedRecord = signal<DonationRecord | null>(null);

  searchTerm = signal('');
  beneficiarySearch = signal('');

  filteredBeneficiaries = computed(() => {
    const term = this.beneficiarySearch().toLowerCase();
    if (!term) return this.beneficiaries();
    return this.beneficiaries().filter((beneficiary) => beneficiary.name.toLowerCase().includes(term));
  });

  filteredStock = computed(() => {
    const term = this.searchTerm().toLowerCase();
    if (!term) {
      return this.stockItems();
    }
    return this.stockItems().filter(
      (item) =>
        item.description.toLowerCase().includes(term) ||
        item.code.toLowerCase().includes(term) ||
        item.category.toLowerCase().includes(term)
    );
  });

  constructor() {
    const firstBeneficiary = this.beneficiaries()[0];
    this.donationForm.patchValue({ beneficiary: firstBeneficiary });
    this.beneficiarySearch.set(firstBeneficiary.name);
  }

  get selectedItem() {
    const code = this.itemForm.value['itemCode'];
    return this.stockItems().find((item) => item.code === code);
  }

  onBeneficiaryNameInput(value: string): void {
    this.beneficiarySearch.set(value);
    this.donationForm.get(['beneficiary', 'name'])?.setValue(value);
    const match = this.filteredBeneficiaries()[0];
    if (match) {
      this.populateBeneficiary(match);
    }
  }

  populateBeneficiary(beneficiary: Beneficiary): void {
    this.donationForm.patchValue({ beneficiary });
    this.beneficiarySearch.set(beneficiary.name);
  }

  beneficiaryEligibility(beneficiary: Beneficiary | null): { status: 'aguardo' | 'liberado'; message: string } {
    if (!beneficiary || !beneficiary.nextEligibilityDate) {
      return { status: 'aguardo', message: 'Selecione um beneficiário cadastrado para verificar carência.' };
    }

    const nextDate = new Date(beneficiary.nextEligibilityDate);
    const today = new Date(this.todayIso);

    if (nextDate > today) {
      return {
        status: 'aguardo',
        message: `Em carência até ${nextDate.toLocaleDateString('pt-BR')} (aguarde ${this.daysBetween(today, nextDate)} dia(s)).`
      };
    }

    return { status: 'liberado', message: 'Disponível para nova retirada de itens.' };
  }

  addItem(): void {
    if (this.itemForm.invalid || !this.selectedItem) {
      this.itemForm.markAllAsTouched();
      return;
    }

    const item: DonationItem = {
      itemCode: this.selectedItem.code,
      description: this.selectedItem.description,
      quantity: this.itemForm.value['quantity'],
      unit: this.selectedItem.unit,
      notes: this.itemForm.value['notes']
    };

    this.items.update((current) => [...current, item]);
    this.itemForm.reset({ itemCode: '', quantity: 1, notes: '' });
  }

  removeItem(index: number): void {
    this.items.update((current) => current.filter((_, i) => i !== index));
  }

  registerDonation(): void {
    if (this.donationForm.invalid || this.items().length === 0) {
      this.donationForm.markAllAsTouched();
      return;
    }

    const formValue = this.donationForm.value;
    const record: DonationRecord = {
      id: `DOA-${new Date().getFullYear()}-${String(this.donationHistory().length + 1).padStart(4, '0')}`,
      date: new Date().toISOString(),
      deliveryDate: formValue['deliveryDate'],
      beneficiary: formValue['beneficiary'] as Beneficiary,
      items: this.items(),
      notes: formValue['notes']
    };

    this.donationHistory.update((history) => [record, ...history]);
    this.selectedRecord.set(record);
    this.items.set([]);
    this.donationForm.patchValue({ notes: '' });
  }

  selectRecord(record: DonationRecord): void {
    this.selectedRecord.set(record);
  }

  printTerm(record: DonationRecord): void {
    const termHtml = this.buildTermHtml(record);
    const printWindow = window.open('', '_blank', 'width=900,height=1200');
    if (!printWindow) return;

    printWindow.document.open();
    printWindow.document.write(termHtml);
    printWindow.document.close();
    printWindow.focus();
    printWindow.print();
  }

  private buildTermHtml(record: DonationRecord): string {
    const itemsRows = record.items
      .map(
        (item, index) => `
          <tr>
            <td>${index + 1}</td>
            <td>${item.description}</td>
            <td>${item.quantity} ${item.unit}</td>
            <td>${item.notes ?? '-'}</td>
          </tr>
        `
      )
      .join('');

    const formattedDate = new Date(record.deliveryDate).toLocaleDateString('pt-BR');

    return `
      <html>
        <head>
          <title>Termo de doação</title>
          <style>
            * { font-family: 'Inter', Arial, sans-serif; }
            body { margin: 0; padding: 32px; background: #f8fafc; color: #0f172a; }
            .container { max-width: 900px; margin: 0 auto; background: white; padding: 32px; border-radius: 16px; box-shadow: 0 20px 50px rgba(15, 23, 42, 0.12); }
            .header { display: flex; align-items: center; justify-content: space-between; border-bottom: 2px solid #0ea5e9; padding-bottom: 16px; margin-bottom: 24px; }
            .brand { font-size: 14px; color: #334155; letter-spacing: 0.08em; text-transform: uppercase; }
            h1 { margin: 0; font-size: 24px; color: #0f172a; }
            .muted { color: #475569; margin: 4px 0 0; }
            .section { margin-bottom: 24px; }
            .section-title { font-size: 14px; color: #0ea5e9; letter-spacing: 0.08em; font-weight: 700; text-transform: uppercase; margin-bottom: 8px; }
            .card { border: 1px solid #e2e8f0; border-radius: 12px; padding: 16px; background: #f8fafc; }
            table { width: 100%; border-collapse: collapse; margin-top: 12px; }
            th { text-align: left; background: #0f172a; color: white; padding: 10px; font-size: 13px; text-transform: uppercase; letter-spacing: 0.04em; }
            td { padding: 12px 10px; border-bottom: 1px solid #e2e8f0; font-size: 14px; color: #0f172a; }
            tfoot td { border-bottom: none; }
            .footer { margin-top: 32px; display: grid; gap: 12px; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); }
            .signature { border-top: 1px solid #cbd5e1; padding-top: 12px; text-align: center; color: #475569; font-size: 13px; }
            .badge { display: inline-flex; align-items: center; gap: 8px; background: #e0f2fe; color: #0ea5e9; padding: 6px 12px; border-radius: 999px; font-weight: 600; }
            .badge span { font-size: 12px; color: #0f172a; }
          </style>
        </head>
        <body>
          <div class="container">
            <div class="header">
              <div>
                <p class="brand">Agência Adventista de Desenvolvimento e Recursos Assistenciais</p>
                <h1>Termo de Recebimento de Doação</h1>
                <p class="muted">Registro formal de entrega para beneficiário</p>
              </div>
              <div class="badge">Número <span>${record.id}</span></div>
            </div>

            <div class="section">
              <div class="section-title">Beneficiário</div>
              <div class="card">
                <p><strong>Nome/Razão Social:</strong> ${record.beneficiary.name}</p>
                <p><strong>Documento:</strong> ${record.beneficiary.document}</p>
                <p><strong>Contato:</strong> ${record.beneficiary.phone ?? 'Não informado'}</p>
                <p><strong>Endereço:</strong> ${record.beneficiary.address ?? 'Não informado'}</p>
              </div>
            </div>

            <div class="section">
              <div class="section-title">Itens doados</div>
              <div class="card">
                <table>
                  <thead>
                    <tr>
                      <th>#</th>
                      <th>Descrição</th>
                      <th>Quantidade</th>
                      <th>Observações</th>
                    </tr>
                  </thead>
                  <tbody>${itemsRows}</tbody>
                  <tfoot>
                    <tr>
                      <td colspan="4" style="padding-top:14px; color:#475569; font-size:13px;">
                        Conferi os itens descritos acima e declaro que recebi os bens/doações de forma gratuita, sem custos ou obrigações futuras.
                      </td>
                    </tr>
                  </tfoot>
                </table>
              </div>
            </div>

            <div class="section">
              <div class="section-title">Entrega</div>
              <div class="card">
                <p><strong>Data da entrega:</strong> ${formattedDate}</p>
                <p><strong>Observações:</strong> ${record.notes ?? 'Sem observações adicionais.'}</p>
              </div>
            </div>

            <div class="footer">
              <div class="signature">Assinatura do Responsável pelo Recebimento</div>
              <div class="signature">Assinatura do Representante da Instituição</div>
            </div>
          </div>
        </body>
      </html>
    `;
  }

  private addDaysToDate(dateIso: string, days: number): string {
    const base = new Date(dateIso);
    base.setDate(base.getDate() + days);
    return base.toISOString().substring(0, 10);
  }

  private daysBetween(start: Date, end: Date): number {
    const diffMs = end.getTime() - start.getTime();
    return Math.max(0, Math.round(diffMs / (1000 * 60 * 60 * 24)));
  }
}
