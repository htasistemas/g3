import { CommonModule } from '@angular/common';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
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
import { AlmoxarifadoService } from '../../services/almoxarifado.service';
import { DoacaoRealizadaResponse, DoacaoRealizadaService } from '../../services/doacao-realizada.service';
import { AuthService } from '../../services/auth.service';
import { PopupErrorBuilder } from '../../utils/popup-error.builder';

import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';
interface Beneficiary {
  id: string;
  name: string;
  document: string;
  cpf?: string;
  birthDate?: string;
  age?: number | null;
  photoUrl?: string;
  phone?: string;
  address?: string;
  family?: string;
  status?: string;
  blockReason?: string;
  type: 'beneficiario' | 'familia';
}

interface StockItem {
  id: number;
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
  beneficiaryType?: 'beneficiario' | 'familia';
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
  cancelReason?: string;
}

type TabId = 'identificacao' | 'historico' | 'planejamento' | 'dashboard';

@Component({
  selector: 'app-donation-management',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, FontAwesomeModule, TelaPadraoComponent, PopupMessagesComponent],
  templateUrl: './donation-management.component.html',
  styleUrl: './donation-management.component.scss'
})
export class DonationManagementComponent implements OnInit {
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
  private readonly almoxarifadoService = inject(AlmoxarifadoService);
  private readonly doacaoRealizadaService = inject(DoacaoRealizadaService);
  private readonly authService = inject(AuthService);
  private readonly todayIso = new Date().toISOString().substring(0, 10);

  tabs: { id: TabId; label: string }[] = [
    { id: 'identificacao', label: 'Identificacao' },
    { id: 'historico', label: 'Histórico de doações' },
    { id: 'planejamento', label: 'Doações a realizar' },
    { id: 'dashboard', label: 'Dashboard' }
  ];

  activeTab = signal<TabId>('identificacao');
  activeTabIndex = computed(() => this.tabs.findIndex((tab) => tab.id === this.activeTab()));
  hasNextTab = computed(() => this.activeTabIndex() < this.tabs.length - 1);
  hasPreviousTab = computed(() => this.activeTabIndex() > 0);
  nextTabLabel = computed(() => this.tabs[this.activeTabIndex() + 1]?.label ?? '');
  stockModalOpen = signal(false);
  stockModalContext = signal<'deliver' | 'plan'>('deliver');
  stockSearch = signal('');
  stockCategory = signal('todos');
  stockStatus = signal<'todos' | StockItem['status']>('todos');
  itemError = signal<string | null>(null);
  plannedError = signal<string | null>(null);
  historicoErro = signal<string | null>(null);
  popupErros: string[] = [];

  beneficiaries = signal<Beneficiary[]>([]);

  stockItems = signal<StockItem[]>([]);

  donationHistory = signal<DonationRecord[]>([]);

  plannedDonations = signal<PlannedDonation[]>([]);

  identificationForm: FormGroup = this.fb.group({
    beneficiaryName: ['', Validators.required],
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
  cancelDialogOpen = signal(false);
  cancelReason = signal('');
  planToCancel = signal<PlannedDonation | null>(null);

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
    if (!this.selectedBeneficiary()) {
      return [];
    }
    const selectedId = String(this.selectedBeneficiary()!.id);
    history = history.filter((record) => String(record.beneficiaryId) === selectedId);

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
    plans = plans.filter((plan) => plan.status !== 'entregue');
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

  ngOnInit(): void {
    this.loadStockItems();
    this.loadDoacoesRealizadas();
    this.preencherResponsavelLogado();
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
      if (found.id === 'historico') {
        this.loadDoacoesRealizadas();
      }
    }
  }

  applyHistoryFilters(): void {
    this.historicoErro.set(null);
    this.historyFilters.markAllAsTouched();
    this.historyFilters.updateValueAndValidity();
    this.loadDoacoesRealizadas();
  }

  goToNextTab(): void {
    if (!this.hasNextTab()) {
      return;
    }
    const nextTab = this.tabs[this.activeTabIndex() + 1];
    if (nextTab) {
      this.activeTab.set(nextTab.id);
    }
  }

  goToPreviousTab(): void {
    if (!this.hasPreviousTab()) {
      return;
    }
    const previousTab = this.tabs[this.activeTabIndex() - 1];
    if (previousTab) {
      this.activeTab.set(previousTab.id);
    }
  }

  populateBeneficiary(beneficiary: Beneficiary): void {
    this.selectedBeneficiary.set(beneficiary);
    this.beneficiarySearch.set(beneficiary.name);
    this.identificationForm.patchValue({ beneficiaryName: beneficiary.name });
    this.loadDoacoesRealizadas();
    this.handleBlockedBeneficiary(beneficiary);
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
      name: payload.nome_completo || payload.nome_social || 'Beneficiario',
      document: payload.cpf || payload.nis || 'Documento n?o informado',
      cpf: payload.cpf || undefined,
      birthDate: payload.data_nascimento,
      age: this.calculateAge(payload.data_nascimento),
      photoUrl: payload.foto_3x4 || undefined,
      phone: payload.telefone_principal,
      address,
      family: payload.composicao_familiar,
      status: payload.status,
      blockReason: payload.motivo_bloqueio,
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
      document: payload.municipio ? `Município: ${payload.municipio}` : 'Familia cadastrada',
      cpf: undefined,
      birthDate: undefined,
      age: null,
      photoUrl: undefined,
      phone: undefined,
      address,
      family: payload.nome_familia,
      status: undefined,
      blockReason: undefined,
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
      this.beneficiarySearchError.set('Nao foi possível buscar beneficiarios no momento.');
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
      this.atualizarDataPrevistaPorUltimaRetirada(item.code);
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
      this.beneficiarySearchError.set('Selecione um beneficiario ou familia para continuar.');
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
    const selected = this.selectedBeneficiary()!;
    const payloadItens = items.map((item) => {
      const estoqueItem = this.findStockItem(item.stockCode);
      return {
        itemId: estoqueItem ? estoqueItem.id : 0,
        quantidade: item.quantity,
        observacoes: item.notes
      };
    });

    if (payloadItens.some((item) => item.itemId === 0)) {
      this.itemError.set('Selecione itens validos do almoxarifado antes de registrar a doacao.');
      return;
    }

    const payload = {
      beneficiarioId: selected.type === 'beneficiario' ? Number(selected.id) : undefined,
      vinculoFamiliarId: selected.type === 'familia' ? Number(selected.id) : undefined,
      tipoDoacao: 'Nao informado',
      situacao: 'entregue',
      responsavel: formValue['responsible'],
      observacoes: formValue['notes'],
      dataDoacao: this.todayIso,
      itens: payloadItens
    };

    this.doacaoRealizadaService.criar(payload).subscribe({
      next: (response) => {
        items.forEach((item) => this.adjustStock(item.stockCode, -item.quantity));
        this.donationHistory.update((history) => [this.mapDoacao(response), ...history]);
        this.loadDoacoesRealizadas();
        this.deliveredItems.set([]);
        this.identificationForm.patchValue({ notes: '' });
        if (response.tipoDoacao?.toLowerCase().includes('cesta')) {
          this.scheduleNextBasicBasket(this.mapDoacao(response));
        }
        this.changeTab('historico');
      },
      error: () => {
        this.itemError.set('Nao foi possível registrar a doacao agora.');
      }
    });
  }

  submitPlannedDonation(): void {
    this.plannedError.set(null);
    if (!this.selectedBeneficiary()) {
      this.plannedError.set('Selecione um Beneficiario primeiro.');
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
      plans.map((p) =>
        p.id === plan.id
          ? { ...p, status: 'cancelado', cancelReason: this.cancelReason() || p.cancelReason, notes: p.notes }
          : p
      )
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

  realizarDoacaoPlanejada(plan: PlannedDonation): void {
    this.plannedError.set(null);
    if (!this.selectedBeneficiary()) {
      this.plannedError.set('Selecione um Beneficiario primeiro.');
      return;
    }

    if (plan.status === 'entregue') {
      this.plannedError.set('Esta doacao planejada ja foi entregue.');
      return;
    }

    if (!this.ensureStockAvailable(plan.itemCode, plan.quantity)) {
      return;
    }

    const estoqueItem = this.findStockItem(plan.itemCode);
    if (!estoqueItem) {
      this.plannedError.set('Item n?o encontrado no almoxarifado.');
      return;
    }

    const selected = this.selectedBeneficiary()!;
    const payload = {
      beneficiarioId: selected.type === 'beneficiario' ? Number(selected.id) : undefined,
      vinculoFamiliarId: selected.type === 'familia' ? Number(selected.id) : undefined,
      tipoDoacao: 'Doação planejada',
      situacao: 'entregue',
      responsavel: this.identificationForm.value['responsible'],
      observacoes: plan.notes,
      dataDoacao: this.todayIso,
      itens: [
        {
          itemId: estoqueItem.id,
          quantidade: plan.quantity,
          observacoes: plan.notes
        }
      ]
    };

    this.doacaoRealizadaService.criar(payload).subscribe({
      next: (response) => {
        this.adjustStock(plan.itemCode, -plan.quantity);
        this.donationHistory.update((history) => [this.mapDoacao(response), ...history]);
        this.plannedDonations.update((plans) =>
          plans.map((item) => (item.id === plan.id ? { ...item, status: 'entregue' } : item))
        );
        this.changeTab('historico');
      },
      error: () => {
        this.plannedError.set('Nao foi possível registrar a doacao planejada.');
      }
    });
  }

  openCancelDialog(plan: PlannedDonation): void {
    this.plannedError.set(null);
    this.cancelReason.set('');
    this.planToCancel.set(plan);
    this.cancelDialogOpen.set(true);
  }

  closeCancelDialog(): void {
    this.cancelDialogOpen.set(false);
    this.planToCancel.set(null);
  }

  confirmCancelPlan(): void {
    const plan = this.planToCancel();
    const reason = this.cancelReason().trim();
    if (!plan) {
      this.closeCancelDialog();
      return;
    }
    if (!reason) {
      this.plannedError.set('Informe o motivo do cancelamento.');
      return;
    }
    this.cancelReason.set(reason);
    this.cancelPlan(plan);
    this.closeCancelDialog();
  }

  formatPlanStatus(status: PlannedDonation['status']): string {
    if (status === 'cancelado') {
      return 'Doação cancelada';
    }
    if (status === 'entregue') {
      return 'Doação entregue';
    }
    return status;
  }

  getPlanStatusClass(status: PlannedDonation['status']): string {
    if (status === 'cancelado') {
      return 'status-chip status-chip--danger';
    }
    return 'status-chip';
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

  private loadStockItems(): void {
    this.almoxarifadoService.listItems().subscribe({
      next: (items) => {
        const mapped = items.map((item) => ({
          id: Number(item.id),
          code: item.code,
          description: item.description,
          unit: item.unit || '',
          currentStock: item.currentStock,
          category: item.category || 'Nao informado',
          status: item.status
        }));
        this.stockItems.set(mapped);
      },
      error: () => {
        this.itemError.set('Nao foi possível carregar itens do almoxarifado.');
      }
    });
  }

  private loadDoacoesRealizadas(): void {
    this.historicoErro.set(null);
    this.doacaoRealizadaService.listar().subscribe({
      next: (records) => {
        const mapped = records.map((record) => this.mapDoacao(record));
        this.donationHistory.set(mapped);
      },
      error: () => {
        this.historicoErro.set('Nao foi possível carregar as doações realizadas.');
      }
    });
  }

  private mapDoacao(record: DoacaoRealizadaResponse): DonationRecord {
    const beneficiarioNome =
      record.beneficiarioNome ||
      record.familiaNome ||
      (record.beneficiarioId ? `Beneficiario ${record.beneficiarioId}` : 'Familia');

    return {
      id: String(record.id),
      beneficiaryId: String(record.beneficiarioId ?? record.vinculoFamiliarId ?? ''),
      beneficiaryName: beneficiarioNome,
      beneficiaryDocument: 'Nao informado',
      beneficiaryType: record.beneficiarioId ? 'beneficiario' : 'familia',
      date: record.dataDoacao,
      deliveryDate: record.dataDoacao,
      donationType: record.tipoDoacao,
      status: record.situacao,
      responsible: record.responsavel || 'Nao informado',
      notes: record.observacoes,
      items: (record.itens || []).map((item) => ({
        stockCode: item.codigoItem || '',
        description: item.descricaoItem || 'Item',
        unit: item.unidadeItem || '-',
        quantity: item.quantidade,
        notes: item.observacoes
      }))
    };
  }

  private preencherResponsavelLogado(): void {
    const usuario = this.authService.user()?.nomeUsuario;
    if (!usuario) {
      return;
    }
    this.identificationForm.patchValue({ responsible: usuario });
  }

  private ensureStockAvailable(code: string, quantity: number): boolean {
    const item = this.findStockItem(code);
    if (!item) {
      this.itemError.set('Item n?o encontrado no almoxarifado.');
      return false;
    }

    if (quantity > item.currentStock) {
      this.itemError.set(
        `Estoque insuficiente para ${item.description}. Disponivel: ${item.currentStock} ${item.unit}.`
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

  private atualizarDataPrevistaPorUltimaRetirada(itemCode: string): void {
    if (!this.selectedBeneficiary()) {
      return;
    }
    const selectedId = String(this.selectedBeneficiary()!.id);
    const historico = this.donationHistory().filter((record) => String(record.beneficiaryId) === selectedId);
    if (!historico.length) {
      return;
    }
    const lastDelivery = historico
      .filter((record) => record.items.some((item) => item.stockCode === itemCode))
      .sort((a, b) => b.deliveryDate.localeCompare(a.deliveryDate))[0];
    if (!lastDelivery) {
      return;
    }
    const base = new Date(lastDelivery.deliveryDate);
    if (Number.isNaN(base.getTime())) {
      return;
    }
    base.setDate(base.getDate() + 30);
    this.plannedForm.patchValue({ dueDate: base.toISOString().substring(0, 10) });
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
      notes: 'Proxima cesta basica programada automaticamente apos a entrega.'
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

  private calculateAge(dateValue?: string): number | null {
    if (!dateValue) {
      return null;
    }
    const parsed = new Date(dateValue);
    if (Number.isNaN(parsed.getTime())) {
      return null;
    }
    const today = new Date();
    let age = today.getFullYear() - parsed.getFullYear();
    const monthDiff = today.getMonth() - parsed.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < parsed.getDate())) {
      age -= 1;
    }
    return age;
  }

  getInitials(name?: string): string {
    if (!name) return '';
    const parts = name.trim().split(/\s+/);
    if (!parts.length) return '';
    const first = parts[0][0] || '';
    const last = parts.length > 1 ? parts[parts.length - 1][0] || '' : '';
    return `${first}${last}`.toUpperCase();
  }

  fecharPopupErros(): void {
    this.popupErros = [];
  }

  private handleBlockedBeneficiary(beneficiary: Beneficiary): void {
    this.popupErros = [];
    if (beneficiary.type !== 'beneficiario') {
      return;
    }
    if (String(beneficiary.status).toUpperCase() !== 'BLOQUEADO') {
      return;
    }
    const reason = beneficiary.blockReason || 'Nao informado';
    const builder = new PopupErrorBuilder();
    builder.adicionar(`Beneficiario bloqueado. Motivo: ${reason}.`);
    this.popupErros = builder.build();
  }

  formatCpf(value?: string | null): string {
    const digits = (value ?? '').replace(/\D/g, '').slice(0, 11);
    if (!digits) return '';
    const part1 = digits.slice(0, 3);
    const part2 = digits.slice(3, 6);
    const part3 = digits.slice(6, 9);
    const part4 = digits.slice(9, 11);
    return [part1, part2, part3].filter(Boolean).join('.') + (part4 ? `-${part4}` : '');
  }

  formatPhoneValue(value?: string | null): string {
    const digits = (value ?? '').replace(/\D/g, '').slice(0, 11);
    if (!digits) return '';
    const hasNineDigits = digits.length > 10;
    const part1 = digits.slice(0, 2);
    const part2 = digits.slice(2, hasNineDigits ? 7 : 6);
    const part3 = digits.slice(hasNineDigits ? 7 : 6, hasNineDigits ? 11 : 10);
    return part3 ? `(${part1}) ${part2}-${part3}` : part2 ? `(${part1}) ${part2}` : `(${part1}`;
  }

}




















