import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faBoxOpen,
  faChartPie,
  faCheck,
  faCircleCheck,
  faClipboardList,
  faClock,
  faFilter,
  faMagnifyingGlass,
  faNotesMedical,
  faPeopleGroup,
  faPlus,
  faTriangleExclamation,
  faUserCheck,
  faUserPlus
} from '@fortawesome/free-solid-svg-icons';
import { firstValueFrom } from 'rxjs';
import { BeneficiarioApiPayload, BeneficiarioApiService } from '../../services/beneficiario-api.service';
import { FamilyService, FamiliaPayload } from '../../services/family.service';

interface Beneficiary {
  id: string;
  name: string;
  document: string;
  phone?: string;
  address?: string;
  family?: string;
  type: 'beneficiario' | 'familia';
}

interface StockItem {
  code: string;
  description: string;
  unit: string;
  currentStock: number;
  category: string;
  status: 'Ativo' | 'Inativo';
}

interface DonationItem {
  stockCode: string;
  description: string;
  unit: string;
  quantity: number;
  notes?: string;
}

interface DonationRecord {
  id: string;
  beneficiaryId: string;
  beneficiaryName: string;
  beneficiaryDocument: string;
  date: string;
  deliveryDate: string;
  status: string;
  donationType: string;
  responsible: string;
  notes?: string;
  items: DonationItem[];
}

interface PlannedDonation {
  id: string;
  beneficiaryId: string;
  beneficiaryName: string;
  beneficiaryDocument: string;
  itemCode: string;
  itemDescription: string;
  unit: string;
  quantity: number;
  dueDate: string;
  priority: 'baixa' | 'media' | 'alta';
  status: 'pendente' | 'em_separacao' | 'pronto' | 'entregue' | 'cancelado';
  notes?: string;
}

type TabId = 'identificacao' | 'historico' | 'planejamento' | 'dashboard';

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
  readonly faUserCheck = faUserCheck;
  readonly faTriangleExclamation = faTriangleExclamation;
  readonly faPlus = faPlus;
  readonly faFilter = faFilter;
  readonly faNotesMedical = faNotesMedical;
  readonly faClock = faClock;
  readonly faCircleCheck = faCircleCheck;
  readonly faCheck = faCheck;
  readonly faChartPie = faChartPie;
  readonly faPeopleGroup = faPeopleGroup;
  readonly Math = Math;

  private readonly fb = new FormBuilder();
  private readonly beneficiaryService = inject(BeneficiarioApiService);
  private readonly familyService = inject(FamilyService);
  private readonly todayIso = new Date().toISOString().substring(0, 10);

  tabs: { id: TabId; label: string }[] = [
    { id: 'identificacao', label: 'Identificação' },
    { id: 'historico', label: 'Histórico de Doações' },
    { id: 'planejamento', label: 'Doações a realizar' },
    { id: 'dashboard', label: 'Dashboard' }
  ];

  activeTab = signal<TabId>('identificacao');
  activeTabIndex = computed(() => this.tabs.findIndex((tab) => tab.id === this.activeTab()));
  stockModalOpen = signal(false);
  stockModalContext = signal<'deliver' | 'plan'>('deliver');
  stockSearch = signal('');
  stockCategory = signal('todos');
  stockStatus = signal<'todos' | StockItem['status']>('todos');
  itemError = signal<string | null>(null);
  plannedError = signal<string | null>(null);

  beneficiaries = signal<Beneficiary[]>([]);

  stockItems = signal<StockItem[]>([
    { code: 'ALM-001', description: 'Álcool em gel 70% 500ml', unit: 'Frasco', currentStock: 85, category: 'Higiene', status: 'Ativo' },
    { code: 'ALM-002', description: 'Luvas descartáveis tamanho M', unit: 'Caixa', currentStock: 18, category: 'EPI', status: 'Ativo' },
    { code: 'ALM-003', description: 'Papel sulfite A4 75g', unit: 'Resma', currentStock: 120, category: 'Escritório', status: 'Ativo' },
    { code: 'ALM-004', description: 'Cartucho de impressora HP 664', unit: 'Unidade', currentStock: 6, category: 'Escritório', status: 'Inativo' },
    { code: 'ALM-005', description: 'Cesta básica padrão', unit: 'Kit', currentStock: 42, category: 'Alimentos', status: 'Ativo' }
  ]);

  donationHistory = signal<DonationRecord[]>([
    {
      id: 'DOA-2025-0004',
      beneficiaryId: 'B-001',
      beneficiaryName: 'Maria Fernanda Alves',
      beneficiaryDocument: '123.456.789-00',
      date: this.todayIso,
      deliveryDate: this.todayIso,
      donationType: 'Cesta básica',
      status: 'entregue',
      responsible: 'Equipe Assistência',
      notes: 'Distribuição mensal programada.',
      items: [
        { stockCode: 'ALM-005', description: 'Cesta básica padrão', unit: 'Kit', quantity: 1 }
      ]
    },
    {
      id: 'DOA-2025-0003',
      beneficiaryId: 'B-002',
      beneficiaryName: 'João Pedro Duarte',
      beneficiaryDocument: '987.654.321-00',
      date: this.offsetDate(-7),
      deliveryDate: this.offsetDate(-6),
      donationType: 'Roupa',
      status: 'entregue',
      responsible: 'Ana Costa',
      notes: 'Tamanho M',
      items: [
        { stockCode: 'ALM-002', description: 'Luvas descartáveis tamanho M', unit: 'Caixa', quantity: 2 }
      ]
    }
  ]);

  plannedDonations = signal<PlannedDonation[]>([
    {
      id: 'PLAN-001',
      beneficiaryId: 'B-001',
      beneficiaryName: 'Maria Fernanda Alves',
      beneficiaryDocument: '123.456.789-00',
      itemCode: 'ALM-001',
      itemDescription: 'Álcool em gel 70% 500ml',
      unit: 'Frasco',
      quantity: 2,
      dueDate: this.offsetDate(3),
      priority: 'media',
      status: 'pendente',
      notes: 'Reposição semanal'
    },
    {
      id: 'PLAN-002',
      beneficiaryId: 'B-003',
      beneficiaryName: 'Associação Vila Nova',
      beneficiaryDocument: '11.111.111/0001-11',
      itemCode: 'ALM-005',
      itemDescription: 'Cesta básica padrão',
      unit: 'Kit',
      quantity: 5,
      dueDate: this.offsetDate(10),
      priority: 'alta',
      status: 'em_separacao'
    }
  ]);

  identificationForm: FormGroup = this.fb.group({
    beneficiaryName: ['', Validators.required],
    donationType: ['', Validators.required],
    donationStatus: ['em_analise', Validators.required],
    responsible: ['Usuário logado', Validators.required],
    notes: ['']
  });

  deliveredItemForm: FormGroup = this.fb.group({
    itemCode: ['', Validators.required],
    quantity: [1, [Validators.required, Validators.min(1)]],
    notes: ['']
  });

  plannedForm: FormGroup = this.fb.group({
    itemCode: ['', Validators.required],
    quantity: [1, [Validators.required, Validators.min(1)]],
    dueDate: [this.offsetDate(3), Validators.required],
    priority: ['media', Validators.required],
    status: ['pendente', Validators.required],
    notes: ['']
  });

  historyFilters: FormGroup = this.fb.group({
    startDate: [''],
    endDate: [''],
    donationType: ['']
  });

  deliveredItems = signal<DonationItem[]>([]);
  selectedBeneficiary = signal<Beneficiary | null>(null);
  beneficiarySearch = signal('');
  beneficiarySearchError = signal<string | null>(null);
  searchingBeneficiaries = signal(false);
  editingPlanId: string | null = null;

  filteredBeneficiaries = computed(() => {
    const term = this.beneficiarySearch().toLowerCase();
    if (!term) return this.beneficiaries();
    return this.beneficiaries().filter((beneficiary) =>
      beneficiary.name.toLowerCase().includes(term) || beneficiary.family?.toLowerCase().includes(term)
    );
  });

  filteredStock = computed(() => {
    const term = this.stockSearch().toLowerCase();
    const category = this.stockCategory();
    const status = this.stockStatus();

    return this.stockItems().filter((item) => {
      const matchesTerm =
        !term ||
        item.description.toLowerCase().includes(term) ||
        item.code.toLowerCase().includes(term) ||
        item.category.toLowerCase().includes(term);
      const matchesCategory = category === 'todos' || item.category === category;
      const matchesStatus = status === 'todos' || item.status === status;
      return matchesTerm && matchesCategory && matchesStatus;
    });
  });

  filteredHistory = computed(() => {
    let history = this.donationHistory();
    if (this.selectedBeneficiary()) {
      history = history.filter((record) => record.beneficiaryId === this.selectedBeneficiary()!.id);
    }

    const filters = this.historyFilters.value;
    if (filters['startDate']) {
      history = history.filter((record) => record.deliveryDate >= filters['startDate']);
    }
    if (filters['endDate']) {
      history = history.filter((record) => record.deliveryDate <= filters['endDate']);
    }
    if (filters['donationType']) {
      history = history.filter((record) =>
        record.donationType.toLowerCase().includes(String(filters['donationType']).toLowerCase())
      );
    }

    return history.sort((a, b) => b.deliveryDate.localeCompare(a.deliveryDate));
  });

  filteredPlans = computed(() => {
    let plans = this.plannedDonations();
    if (this.selectedBeneficiary()) {
      plans = plans.filter((plan) => plan.beneficiaryId === this.selectedBeneficiary()!.id);
    }
    return plans;
  });

  dashboardStats = computed(() => {
    const history = this.filteredHistory();
    const plans = this.filteredPlans();

    const deliveredCount = history.length;
    const deliveredQuantity = history.reduce(
      (total, record) => total + record.items.reduce((sum, item) => sum + item.quantity, 0),
      0
    );
    const pendingPlans = plans.filter((plan) => plan.status !== 'entregue' && plan.status !== 'cancelado');
    const pendingQuantity = pendingPlans.reduce((total, plan) => total + plan.quantity, 0);

    const distribution = history.reduce<Record<string, number>>((acc, record) => {
      acc[record.donationType] = (acc[record.donationType] || 0) + record.items.reduce((s, i) => s + i.quantity, 0);
      return acc;
    }, {});

    return { deliveredCount, deliveredQuantity, pendingPlans: pendingPlans.length, pendingQuantity, distribution };
  });

  constructor() {
  }

  get selectedDeliveryItem() {
    return this.findStockItem(this.deliveredItemForm.value['itemCode']);
  }

  get selectedPlannedItem() {
    return this.findStockItem(this.plannedForm.value['itemCode']);
  }

  changeTab(id: string): void {
    const found = this.tabs.find((tab) => tab.id === id);
    if (found) {
      this.activeTab.set(found.id);
    }
  }

  populateBeneficiary(beneficiary: Beneficiary): void {
    this.selectedBeneficiary.set(beneficiary);
    this.beneficiarySearch.set(beneficiary.name);
    this.identificationForm.patchValue({ beneficiaryName: beneficiary.name });
  }

  async onBeneficiaryInput(value: string): Promise<void> {
    this.beneficiarySearch.set(value);
    this.identificationForm.get('beneficiaryName')?.setValue(value);
    await this.lookupBeneficiaries(value);
  }

  private mapBeneficiary(payload: BeneficiarioApiPayload): Beneficiary {
    const address = this.buildAddress([
      payload.logradouro,
      payload.numero,
      payload.bairro,
      payload.municipio,
      payload.uf
    ]);

    return {
      id: payload.id_beneficiario ?? '',
      name: payload.nome_completo || payload.nome_social || 'Beneficiário',
      document: payload.cpf || payload.nis || 'Documento não informado',
      phone: payload.telefone_principal,
      address,
      family: payload.composicao_familiar,
      type: 'beneficiario'
    };
  }

  private mapFamily(payload: FamiliaPayload): Beneficiary {
    const address = this.buildAddress([
      payload.logradouro,
      payload.numero,
      payload.bairro,
      payload.municipio,
      payload.uf
    ]);

    return {
      id: payload.id_familia ?? '',
      name: payload.nome_familia,
      document: payload.municipio ? `Município: ${payload.municipio}` : 'Família cadastrada',
      phone: undefined,
      address,
      family: payload.nome_familia,
      type: 'familia'
    };
  }

  private buildAddress(parts: (string | undefined)[]): string | undefined {
    const formatted = parts.filter(Boolean).join(', ');
    return formatted || undefined;
  }

  private async lookupBeneficiaries(term: string): Promise<void> {
    const query = term.trim();
    this.beneficiarySearchError.set(null);

    if (!query) {
      this.beneficiaries.set([]);
      this.selectedBeneficiary.set(null);
      return;
    }

    this.searchingBeneficiaries.set(true);
    try {
      const [beneficiaryResponse, familyResponse] = await Promise.all([
        firstValueFrom(this.beneficiaryService.list({ nome: query })),
        firstValueFrom(this.familyService.list({ nome_familia: query }))
      ]);

      const beneficiaryResults = (beneficiaryResponse.beneficiarios ?? []).map((item) => this.mapBeneficiary(item));
      const familyResults = (familyResponse.familias ?? []).map((item) => this.mapFamily(item));
      const merged = [...beneficiaryResults, ...familyResults];

      this.beneficiaries.set(merged);

      const exactMatch = merged.find((item) => item.name.toLowerCase() === query.toLowerCase());
      if (exactMatch) {
        this.populateBeneficiary(exactMatch);
      } else if (merged.length === 1) {
        this.populateBeneficiary(merged[0]);
      }
    } catch (error) {
      console.error('Failed to search beneficiaries', error);
      this.beneficiarySearchError.set('Não foi possível buscar beneficiários no momento.');
    } finally {
      this.searchingBeneficiaries.set(false);
    }
  }

  openStockModal(context: 'deliver' | 'plan'): void {
    this.stockModalContext.set(context);
    this.stockModalOpen.set(true);
    this.stockSearch.set('');
    this.stockCategory.set('todos');
    this.stockStatus.set('todos');
  }

  closeStockModal(): void {
    this.stockModalOpen.set(false);
  }

  selectStockItem(item: StockItem): void {
    if (this.stockModalContext() === 'deliver') {
      this.deliveredItemForm.patchValue({ itemCode: item.code });
    } else {
      this.plannedForm.patchValue({ itemCode: item.code });
    }
    this.closeStockModal();
  }

  addDeliveredItem(): void {
    this.itemError.set(null);
    if (this.deliveredItemForm.invalid || !this.selectedDeliveryItem) {
      this.deliveredItemForm.markAllAsTouched();
      return;
    }

    if (!this.ensureStockAvailable(this.selectedDeliveryItem.code, this.deliveredItemForm.value['quantity'])) {
      return;
    }

    const newItem: DonationItem = {
      stockCode: this.selectedDeliveryItem.code,
      description: this.selectedDeliveryItem.description,
      unit: this.selectedDeliveryItem.unit,
      quantity: this.deliveredItemForm.value['quantity'],
      notes: this.deliveredItemForm.value['notes']
    };

    this.deliveredItems.update((current) => [...current, newItem]);
    this.deliveredItemForm.reset({ itemCode: '', quantity: 1, notes: '' });
  }

  removeDeliveredItem(index: number): void {
    this.deliveredItems.update((items) => items.filter((_, i) => i !== index));
  }

  registerDonation(): void {
    this.itemError.set(null);
    if (!this.selectedBeneficiary()) {
      this.identificationForm.markAllAsTouched();
      return;
    }

    if (this.identificationForm.invalid || this.deliveredItems().length === 0) {
      this.identificationForm.markAllAsTouched();
      return;
    }

    const items = this.deliveredItems();
    for (const item of items) {
      if (!this.ensureStockAvailable(item.stockCode, item.quantity)) {
        return;
      }
    }

    const formValue = this.identificationForm.value;
    const record: DonationRecord = {
      id: `DOA-${new Date().getFullYear()}-${String(this.donationHistory().length + 1).padStart(4, '0')}`,
      beneficiaryId: this.selectedBeneficiary()!.id,
      beneficiaryName: this.selectedBeneficiary()!.name,
      beneficiaryDocument: this.selectedBeneficiary()!.document,
      date: new Date().toISOString(),
      deliveryDate: this.todayIso,
      donationType: formValue['donationType'],
      status: formValue['donationStatus'],
      responsible: formValue['responsible'],
      notes: formValue['notes'],
      items
    };

    items.forEach((item) => this.adjustStock(item.stockCode, -item.quantity));

    this.donationHistory.update((history) => [record, ...history]);
    this.deliveredItems.set([]);
    this.identificationForm.patchValue({ notes: '' });
    if (record.donationType?.toLowerCase().includes('cesta')) {
      this.scheduleNextBasicBasket(record);
    }
    this.changeTab('historico');
  }

  submitPlannedDonation(): void {
    this.plannedError.set(null);
    if (!this.selectedBeneficiary()) {
      this.plannedError.set('Selecione um beneficiário primeiro.');
      return;
    }

    if (this.plannedForm.invalid || !this.selectedPlannedItem) {
      this.plannedForm.markAllAsTouched();
      return;
    }

    const payload: PlannedDonation = {
      id: this.editingPlanId ?? `PLAN-${String(this.plannedDonations().length + 1).padStart(3, '0')}`,
      beneficiaryId: this.selectedBeneficiary()!.id,
      beneficiaryName: this.selectedBeneficiary()!.name,
      beneficiaryDocument: this.selectedBeneficiary()!.document,
      itemCode: this.selectedPlannedItem.code,
      itemDescription: this.selectedPlannedItem.description,
      unit: this.selectedPlannedItem.unit,
      quantity: this.plannedForm.value['quantity'],
      dueDate: this.plannedForm.value['dueDate'],
      priority: this.plannedForm.value['priority'],
      status: this.plannedForm.value['status'],
      notes: this.plannedForm.value['notes']
    };

    if (this.editingPlanId) {
      this.plannedDonations.update((plans) => plans.map((plan) => (plan.id === this.editingPlanId ? payload : plan)));
    } else {
      this.plannedDonations.update((plans) => [payload, ...plans]);
    }

    this.clearPlanForm();
  }

  editPlan(plan: PlannedDonation): void {
    this.editingPlanId = plan.id;
    this.plannedForm.patchValue({
      itemCode: plan.itemCode,
      quantity: plan.quantity,
      dueDate: plan.dueDate,
      priority: plan.priority,
      status: plan.status,
      notes: plan.notes ?? ''
    });
    this.stockModalContext.set('plan');
  }

  cancelPlan(plan: PlannedDonation): void {
    this.plannedDonations.update((plans) =>
      plans.map((p) => (p.id === plan.id ? { ...p, status: 'cancelado', notes: p.notes ?? 'Cancelado' } : p))
    );
  }

  markPlanAsDelivered(plan: PlannedDonation): void {
    if (!this.ensureStockAvailable(plan.itemCode, plan.quantity)) {
      return;
    }

    const deliveredRecord: DonationRecord = {
      id: `DOA-${new Date().getFullYear()}-${String(this.donationHistory().length + 1).padStart(4, '0')}`,
      beneficiaryId: plan.beneficiaryId,
      beneficiaryName: plan.beneficiaryName,
      beneficiaryDocument: plan.beneficiaryDocument,
      date: new Date().toISOString(),
      deliveryDate: this.todayIso,
      donationType: 'Entrega planejada',
      status: 'entregue',
      responsible: this.identificationForm.value['responsible'] || 'Usuário logado',
      notes: plan.notes,
      items: [
        {
          stockCode: plan.itemCode,
          description: plan.itemDescription,
          unit: plan.unit,
          quantity: plan.quantity
        }
      ]
    };

    this.adjustStock(plan.itemCode, -plan.quantity);
    this.donationHistory.update((history) => [deliveredRecord, ...history]);
    this.plannedDonations.update((plans) => plans.map((p) => (p.id === plan.id ? { ...p, status: 'entregue' } : p)));
    this.changeTab('historico');
  }

  getStockBalance(code: string): number {
    return this.findStockItem(code)?.currentStock ?? 0;
  }

  clearPlanForm(): void {
    this.editingPlanId = null;
    this.plannedForm.reset({
      itemCode: '',
      quantity: 1,
      dueDate: this.offsetDate(3),
      priority: 'media',
      status: 'pendente',
      notes: ''
    });
  }

  private findStockItem(code?: string): StockItem | undefined {
    if (!code) return undefined;
    return this.stockItems().find((item) => item.code === code);
  }

  private ensureStockAvailable(code: string, quantity: number): boolean {
    const item = this.findStockItem(code);
    if (!item) {
      this.itemError.set('Item não encontrado no almoxarifado.');
      return false;
    }

    if (quantity > item.currentStock) {
      this.itemError.set(
        `Estoque insuficiente para ${item.description}. Disponível: ${item.currentStock} ${item.unit}.`
      );
      return false;
    }

    return true;
  }

  private adjustStock(code: string, delta: number): void {
    this.stockItems.update((items) =>
      items.map((item) => (item.code === code ? { ...item, currentStock: Math.max(0, item.currentStock + delta) } : item))
    );
  }

  private offsetDate(days: number): string {
    const base = new Date();
    base.setDate(base.getDate() + days);
    return base.toISOString().substring(0, 10);
  }

  private scheduleNextBasicBasket(record: DonationRecord): void {
    if (!this.selectedBeneficiary()) return;
    const templateItem = record.items[0];
    if (!templateItem) return;

    const payload: PlannedDonation = {
      id: `PLAN-${String(this.plannedDonations().length + 1).padStart(3, '0')}`,
      beneficiaryId: this.selectedBeneficiary()!.id,
      beneficiaryName: this.selectedBeneficiary()!.name,
      beneficiaryDocument: this.selectedBeneficiary()!.document,
      itemCode: templateItem.stockCode,
      itemDescription: templateItem.description,
      unit: templateItem.unit,
      quantity: templateItem.quantity,
      dueDate: this.offsetDate(30),
      priority: 'media',
      status: 'pendente',
      notes: 'Próxima cesta básica programada automaticamente após a entrega.'
    };

    this.plannedDonations.update((plans) => [payload, ...plans]);
    this.plannedForm.patchValue({
      itemCode: payload.itemCode,
      quantity: payload.quantity,
      dueDate: payload.dueDate,
      priority: payload.priority,
      status: payload.status,
      notes: ''
    });
  }

}
