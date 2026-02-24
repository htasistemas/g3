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
  faGift,
  faMagnifyingGlass,
  faNotesMedical,
  faPeopleGroup,
  faPlus,
  faPrint,
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
import {
  VisitaDomiciliar,
  VisitaDomiciliarService
} from '../../services/visita-domiciliar.service';
import { AssistanceUnitPayload, AssistanceUnitService } from '../../services/assistance-unit.service';
import { AutocompleteComponent, AutocompleteOpcao } from '../compartilhado/autocomplete/autocomplete.component';

import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';
import {
  ConfigAcoesCrud,
  EstadoAcoesCrud,
  TelaBaseComponent
} from '../compartilhado/tela-base.component';
interface Beneficiary {
  id: string;
  name: string;
  document: string;
  cpf?: string;
  city?: string;
  state?: string;
  optaReceberCestaBasica?: boolean;
  aptoReceberCestaBasica?: boolean;
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
  itemId?: number;
  stockCode: string;
  description: string;
  unit: string;
  quantity: number;
  notes?: string;
}

interface DonationItemEdicao {
  itemId: number;
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
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    FontAwesomeModule,
    TelaPadraoComponent,
    PopupMessagesComponent,
    AutocompleteComponent
  ],
  templateUrl: './donation-management.component.html',
  styleUrl: './donation-management.component.scss'
})
export class DonationManagementComponent extends TelaBaseComponent implements OnInit {
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
  readonly faGift = faGift;
  readonly faPrint = faPrint;
  readonly Math = Math;
  readonly acoesToolbar: Required<ConfigAcoesCrud> = this.criarConfigAcoes({
    salvar: true,
    excluir: true,
    novo: true,
    cancelar: true,
    imprimir: true,
    buscar: true
  });

  get acoesDesabilitadas(): EstadoAcoesCrud {
    return {
      salvar: false,
      excluir: !this.selectedBeneficiary(),
      novo: false,
      cancelar: false,
      imprimir: false,
      buscar: false
    };
  }

  private readonly fb = new FormBuilder();
  private readonly beneficiaryService = inject(BeneficiarioApiService);
  private readonly familyService = inject(FamilyService);
  private readonly almoxarifadoService = inject(AlmoxarifadoService);
  private readonly doacaoRealizadaService = inject(DoacaoRealizadaService);
  private readonly authService = inject(AuthService);
  private readonly visitaService = inject(VisitaDomiciliarService);
  private readonly unidadeService = inject(AssistanceUnitService);
  private readonly todayIso = new Date().toISOString().substring(0, 10);

  tabs: { id: TabId; label: string }[] = [
    { id: 'identificacao', label: 'Identificação' },
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
  stockModalContext = signal<'deliver' | 'plan' | 'edit'>('deliver');
  stockSearch = signal('');
  stockCategory = signal('todos');
  stockStatus = signal<'todos' | StockItem['status']>('todos');
  itemError = signal<string | null>(null);
  plannedError = signal<string | null>(null);
  historicoErro = signal<string | null>(null);
  editError = signal<string | null>(null);
  popupErros: string[] = [];

  beneficiaries = signal<Beneficiary[]>([]);

  stockItems = signal<StockItem[]>([]);
  termoBuscaItemEntrega = '';
  itensEntregaOpcoes: AutocompleteOpcao[] = [];
  unidadeAtual: AssistanceUnitPayload | null = null;

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

  editForm: FormGroup = this.fb.group({
    dataDoacao: ['', Validators.required],
    tipoDoacao: ['', Validators.required],
    situacao: ['', Validators.required],
    responsavel: ['', Validators.required],
    observacoes: ['']
  });

  editItemForm: FormGroup = this.fb.group({
    itemCode: ['', Validators.required],
    quantity: [1, [Validators.required, Validators.min(1)]],
    notes: ['']
  });

  historyFilters: FormGroup = this.fb.group({
    startDate: [''],
    endDate: [''],
    donationType: ['']
  });

  deliveredItems = signal<DonationItem[]>([]);
  editItems = signal<DonationItemEdicao[]>([]);
  selectedBeneficiary = signal<Beneficiary | null>(null);
  beneficiarySearch = signal('');
  beneficiarySearchError = signal<string | null>(null);
  searchingBeneficiaries = signal(false);
  motivoCestaBasica = signal<string | null>(null);
  mostrarMotivoCestaBasica = signal(false);
  editingPlanId: string | null = null;
  cancelDialogOpen = signal(false);
  cancelReason = signal('');
  planToCancel = signal<PlannedDonation | null>(null);
  editModalOpen = signal(false);
  editandoDoacaoId: string | null = null;
  editBeneficiarioId: number | null = null;
  editFamiliaId: number | null = null;
  fotoBeneficiarioAberta = signal(false);
  fotoBeneficiarioUrl = signal<string | null>(null);

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

  get itensEntregaFiltrados(): AutocompleteOpcao[] {
    const termo = this.normalizarTexto(this.termoBuscaItemEntrega || '');
    const itens = this.itensEntregaOpcoes;
    if (!termo) {
      return itens.slice(0, 15);
    }
    return itens
      .filter((item) => {
        const normalizado = this.normalizarTexto(item.label);
        const sublabel = this.normalizarTexto(item.sublabel || '');
        return normalizado.includes(termo) || sublabel.includes(termo);
      })
      .slice(0, 15);
  }

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
    super();
  }

  ngOnInit(): void {
    this.loadStockItems();
    this.loadDoacoesRealizadas();
    this.preencherResponsavelLogado();
    this.carregarUnidadeAtual();
  }

  get selectedDeliveryItem() {
    return this.findStockItem(this.deliveredItemForm.value['itemCode']);
  }

  get selectedPlannedItem() {
    return this.findStockItem(this.plannedForm.value['itemCode']);
  }

  get selectedEditItemDescription(): string {
    return this.findStockItem(this.editItemForm.value['itemCode'])?.description ?? '';
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
    this.carregarInformacoesCestaBasica(beneficiary.name);
  }

  onItemEntregaTermoChange(termo: string): void {
    this.termoBuscaItemEntrega = termo;
    const selecionado = this.selectedDeliveryItem;
    if (!termo || (selecionado && selecionado.description !== termo)) {
      this.deliveredItemForm.patchValue({ itemCode: '' });
    }
  }

  onItemEntregaSelecionado(opcao: AutocompleteOpcao): void {
    const codigo = String(opcao.id || '');
    this.deliveredItemForm.patchValue({ itemCode: codigo });
    this.termoBuscaItemEntrega = opcao.label;
  }

  async onBeneficiaryInput(value: string): Promise<void> {
    this.beneficiarySearch.set(value);
    this.identificationForm.get('beneficiaryName')?.setValue(value);
    this.motivoCestaBasica.set(null);
    this.mostrarMotivoCestaBasica.set(false);
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
      cpf: payload.cpf || undefined,
      city: payload.municipio || undefined,
      state: payload.uf || undefined,
      optaReceberCestaBasica: payload.opta_receber_cesta_basica,
      aptoReceberCestaBasica: payload.apto_receber_cesta_basica,
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
      document: payload.municipio ? `Município: ${payload.municipio}` : 'Família cadastrada',
      cpf: undefined,
      city: payload.municipio || undefined,
      state: payload.uf || undefined,
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
      this.beneficiarySearchError.set('Não foi possível buscar beneficiários no momento.');
    } finally {
      this.searchingBeneficiaries.set(false);
    }
  }

  openStockModal(context: 'deliver' | 'plan' | 'edit'): void {
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
    } else if (this.stockModalContext() === 'plan') {
      this.plannedForm.patchValue({ itemCode: item.code });
      this.atualizarDataPrevistaPorUltimaRetirada(item.code);
    } else {
      this.editItemForm.patchValue({ itemCode: item.code });
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
    this.termoBuscaItemEntrega = '';
  }

  removeDeliveredItem(index: number): void {
    this.deliveredItems.update((items) => items.filter((_, i) => i !== index));
  }

  registerDonation(): void {
    this.itemError.set(null);
    if (!this.selectedBeneficiary()) {
      this.beneficiarySearchError.set('Selecione um beneficiário ou família para continuar.');
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
      this.itemError.set('Selecione itens válidos do almoxarifado antes de registrar a doação.');
      return;
    }

    const payload = {
      beneficiarioId: selected.type === 'beneficiario' ? Number(selected.id) : undefined,
      vinculoFamiliarId: selected.type === 'familia' ? Number(selected.id) : undefined,
      tipoDoacao: 'Não informado',
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
        this.itemError.set('Não foi possível registrar a doação agora.');
      }
    });
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
      this.plannedError.set('Selecione um beneficiário primeiro.');
      return;
    }

    if (plan.status === 'entregue') {
      this.plannedError.set('Esta doação planejada já foi entregue.');
      return;
    }

    if (!this.ensureStockAvailable(plan.itemCode, plan.quantity)) {
      return;
    }

    const estoqueItem = this.findStockItem(plan.itemCode);
    if (!estoqueItem) {
      this.plannedError.set('Item não encontrado no almoxarifado.');
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
        this.plannedError.set('Não foi possível registrar a doação planejada.');
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
          category: item.category || 'Não informado',
          status: item.status
        }));
        this.stockItems.set(mapped);
        this.itensEntregaOpcoes = mapped.map((item) => ({
          id: item.code,
          label: item.description,
          sublabel: item.code
        }));
      },
      error: () => {
        this.itemError.set('Não foi possível carregar itens do almoxarifado.');
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
        this.historicoErro.set('Não foi possível carregar as doações realizadas.');
      }
    });
  }

  private mapDoacao(record: DoacaoRealizadaResponse): DonationRecord {
    const beneficiarioNome =
      record.beneficiarioNome ||
      record.familiaNome ||
      (record.beneficiarioId ? `Beneficiário ${record.beneficiarioId}` : 'Família');

    return {
      id: String(record.id),
      beneficiaryId: String(record.beneficiarioId ?? record.vinculoFamiliarId ?? ''),
      beneficiaryName: beneficiarioNome,
      beneficiaryDocument: 'Não informado',
      beneficiaryType: record.beneficiarioId ? 'beneficiario' : 'familia',
      date: record.dataDoacao,
      deliveryDate: record.dataDoacao,
      donationType: record.tipoDoacao,
      status: record.situacao,
      responsible: record.responsavel || 'Não informado',
      notes: record.observacoes,
      items: (record.itens || []).map((item) => ({
        itemId: item.itemId ?? undefined,
        stockCode: item.codigoItem || '',
        description: item.descricaoItem || 'Item',
        unit: item.unidadeItem || '-',
        quantity: item.quantidade,
        notes: item.observacoes
      }))
    };
  }

  private preencherResponsavelLogado(): void {
    const usuario = this.authService.user()?.nome || this.authService.user()?.nomeUsuario;
    if (!usuario) {
      return;
    }
    this.identificationForm.patchValue({ responsible: usuario });
  }

  private carregarUnidadeAtual(): void {
    this.unidadeService.get().subscribe({
      next: (resposta) => {
        this.unidadeAtual = resposta?.unidade ?? null;
      },
      error: () => {
        this.unidadeAtual = null;
      }
    });
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
    const reason = beneficiary.blockReason || 'Não informado';
    const builder = new PopupErrorBuilder();
    builder.adicionar(`Beneficiário bloqueado. Motivo: ${reason}.`);
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

  podeEditarDoacao(record: DonationRecord): boolean {
    const dias = this.diasDesdeDoacao(record);
    return dias !== null && dias <= 10;
  }

  abrirEdicaoDoacao(record: DonationRecord): void {
    if (!this.podeEditarDoacao(record)) {
      this.historicoErro.set('Esta doação não pode mais ser alterada. Prazo máximo: 10 dias.');
      return;
    }
    this.historicoErro.set(null);
    this.editError.set(null);
    this.editandoDoacaoId = record.id;
    this.editBeneficiarioId = record.beneficiaryType === 'beneficiario' ? Number(record.beneficiaryId) : null;
    this.editFamiliaId = record.beneficiaryType === 'familia' ? Number(record.beneficiaryId) : null;
    this.editForm.patchValue({
      dataDoacao: record.deliveryDate,
      tipoDoacao: record.donationType,
      situacao: record.status,
      responsavel: record.responsible,
      observacoes: record.notes ?? ''
    });
    this.editItems.set(
      record.items.map((item) => ({
        itemId: item.itemId ?? this.findStockItem(item.stockCode)?.id ?? 0,
        stockCode: item.stockCode,
        description: item.description,
        unit: item.unit,
        quantity: item.quantity,
        notes: item.notes
      }))
    );
    this.editItemForm.reset({ itemCode: '', quantity: 1, notes: '' });
    this.editModalOpen.set(true);
  }

  fecharEdicaoDoacao(): void {
    this.editModalOpen.set(false);
    this.editandoDoacaoId = null;
    this.editBeneficiarioId = null;
    this.editFamiliaId = null;
    this.editItems.set([]);
    this.editError.set(null);
  }

  abrirFotoBeneficiario(url?: string | null): void {
    if (!url) {
      return;
    }
    this.fotoBeneficiarioUrl.set(url);
    this.fotoBeneficiarioAberta.set(true);
  }

  fecharFotoBeneficiario(): void {
    this.fotoBeneficiarioAberta.set(false);
    this.fotoBeneficiarioUrl.set(null);
  }

  addEditItem(): void {
    this.editError.set(null);
    if (this.editItemForm.invalid) {
      this.editItemForm.markAllAsTouched();
      return;
    }
    const item = this.findStockItem(this.editItemForm.value['itemCode']);
    if (!item) {
      this.editError.set('Selecione um item válido do almoxarifado.');
      return;
    }
    const jaExiste = this.editItems().some((current) => current.itemId === item.id);
    if (jaExiste) {
      this.editError.set('Item já adicionado na edição.');
      return;
    }
    this.editItems.update((current) => [
      ...current,
      {
        itemId: item.id,
        stockCode: item.code,
        description: item.description,
        unit: item.unit,
        quantity: this.editItemForm.value['quantity'],
        notes: this.editItemForm.value['notes']
      }
    ]);
    this.editItemForm.reset({ itemCode: '', quantity: 1, notes: '' });
  }

  removerEditItem(index: number): void {
    this.editItems.update((items) => items.filter((_, i) => i !== index));
  }

  atualizarQuantidadeEditItem(index: number, valor: number): void {
    this.editItems.update((items) =>
      items.map((current, idx) =>
        idx === index ? { ...current, quantity: Number(valor) } : current
      )
    );
  }

  atualizarObservacoesEditItem(index: number, valor: string): void {
    this.editItems.update((items) =>
      items.map((current, idx) => (idx === index ? { ...current, notes: valor } : current))
    );
  }

  salvarEdicaoDoacao(): void {
    this.editError.set(null);
    if (!this.editandoDoacaoId) {
      this.editError.set('Selecione uma doação para editar.');
      return;
    }
    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      return;
    }
    if (!this.editItems().length) {
      this.editError.set('Informe ao menos um item para a doação.');
      return;
    }
    if (this.editItems().some((item) => item.quantity <= 0)) {
      this.editError.set('Quantidade dos itens deve ser maior que zero.');
      return;
    }
    const itens = this.editItems().map((item) => ({
      itemId: item.itemId,
      quantidade: item.quantity,
      observacoes: item.notes
    }));
    if (itens.some((item) => item.itemId === 0)) {
      this.editError.set('Informe itens válidos do almoxarifado.');
      return;
    }
    const payload = {
      beneficiarioId: this.editBeneficiarioId ?? undefined,
      vinculoFamiliarId: this.editFamiliaId ?? undefined,
      tipoDoacao: this.editForm.value['tipoDoacao'],
      situacao: this.editForm.value['situacao'],
      responsavel: this.editForm.value['responsavel'],
      observacoes: this.editForm.value['observacoes'],
      dataDoacao: this.editForm.value['dataDoacao'],
      itens
    };
    this.doacaoRealizadaService.atualizar(Number(this.editandoDoacaoId), payload).subscribe({
      next: (response) => {
        const atualizado = this.mapDoacao(response);
        this.donationHistory.update((history) =>
          history.map((item) => (item.id === String(atualizado.id) ? atualizado : item))
        );
        this.fecharEdicaoDoacao();
      },
      error: () => {
        this.editError.set('Não foi possível atualizar a doação agora.');
      }
    });
  }

  private diasDesdeDoacao(record: DonationRecord): number | null {
    const data = record.deliveryDate || record.date;
    if (!data) {
      return null;
    }
    const dataBase = data.split('T')[0];
    const partes = dataBase.split('-').map((valor) => Number(valor));
    if (partes.length < 3 || partes.some((valor) => Number.isNaN(valor))) {
      return null;
    }
    const [ano, mes, dia] = partes;
    const base = new Date(ano, mes - 1, dia);
    if (Number.isNaN(base.getTime())) {
      return null;
    }
    const hoje = new Date();
    const hojeBase = new Date(hoje.getFullYear(), hoje.getMonth(), hoje.getDate());
    const diff = hojeBase.getTime() - base.getTime();
    return Math.floor(diff / (1000 * 60 * 60 * 24));
  }

  formatarAptidaoDoacao(beneficiary: Beneficiary): string {
    if (beneficiary.optaReceberCestaBasica === false) {
      return 'Não opta por cesta básica';
    }
    if (beneficiary.optaReceberCestaBasica === true && beneficiary.aptoReceberCestaBasica == null) {
      return 'Opta por cesta básica';
    }
    if (beneficiary.aptoReceberCestaBasica === true) {
      return 'Apto para Receber Doações';
    }
    if (beneficiary.aptoReceberCestaBasica === false) {
      return 'Não Apto para Receber Doações';
    }
    return 'Não informado';
  }

  alternarMotivoCestaBasica(): void {
    this.mostrarMotivoCestaBasica.set(!this.mostrarMotivoCestaBasica());
  }

  private carregarInformacoesCestaBasica(nomeBeneficiario: string): void {
    const termo = this.normalizarTexto(nomeBeneficiario);
    if (!termo) {
      this.motivoCestaBasica.set(null);
      return;
    }

    this.visitaService.list().subscribe({
      next: (visitas: VisitaDomiciliar[]) => {
        const encontrada = visitas
          .filter((visita) => this.normalizarTexto(visita.beneficiario) === termo)
          .sort((a, b) => (b.dataVisita || '').localeCompare(a.dataVisita || ''))[0];
        const registro = encontrada?.registro;
        const opta = registro?.optaReceberCestaBasica;
        const apto = registro?.aptoReceberCestaBasica;
        if (opta != null || apto != null) {
          this.selectedBeneficiary.update((current) =>
            current
              ? {
                  ...current,
                  optaReceberCestaBasica: opta ?? current.optaReceberCestaBasica,
                  aptoReceberCestaBasica: apto ?? current.aptoReceberCestaBasica
                }
              : current
          );
        }
        const motivo = registro?.motivoNaoReceberCestaBasica;
        this.motivoCestaBasica.set(
          motivo && motivo.trim().length ? motivo : null
        );
        this.mostrarMotivoCestaBasica.set(false);
      },
      error: () => {
        this.motivoCestaBasica.set(null);
        this.mostrarMotivoCestaBasica.set(false);
      }
    });
  }

  private normalizarTexto(valor: string): string {
    return valor
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .trim();
  }

  onSalvar(): void {
    this.registerDonation();
  }

  onExcluir(): void {
    this.selectedBeneficiary.set(null);
    this.beneficiarySearch.set('');
    this.motivoCestaBasica.set(null);
    this.mostrarMotivoCestaBasica.set(false);
    this.identificationForm.reset({
      beneficiaryName: '',
      responsible: this.identificationForm.value['responsible'] || 'Usuário logado',
      notes: ''
    });
    this.deliveredItems.set([]);
    this.itemError.set(null);
  }

  onNovo(): void {
    this.onExcluir();
    this.activeTab.set('identificacao');
  }

  onCancelar(): void {
    this.onExcluir();
  }

  onBuscar(): void {
    this.changeTab('historico');
    this.loadDoacoesRealizadas();
  }

  onImprimir(): void {
    window.print();
  }

  imprimirTermoRecebimento(record: DonationRecord): void {
    const html = this.buildTermoRecebimentoHtml(record);

    const printWindow = window.open('', '_blank', 'width=900,height=700');
    if (!printWindow) {
      return;
    }
    printWindow.document.write(html);
    printWindow.document.close();
    printWindow.focus();
    printWindow.onload = () => {
      const imagens = Array.from(printWindow.document.images || []);
      const acionar = () => {
        printWindow.onafterprint = () => {
          printWindow.close();
        };
        printWindow.print();
        setTimeout(() => {
          if (!printWindow.closed) {
            printWindow.close();
          }
        }, 1500);
      };
      if (!imagens.length) {
        acionar();
        return;
      }
      let carregadas = 0;
      const concluir = () => {
        carregadas += 1;
        if (carregadas >= imagens.length) {
          acionar();
        }
      };
      imagens.forEach((img) => {
        if (img.complete) {
          concluir();
        } else {
          img.addEventListener('load', concluir);
          img.addEventListener('error', concluir);
        }
      });
    };
  }

  private buildTermoRecebimentoHtml(record: DonationRecord): string {
    const unidade = this.unidadeAtual;
    const logo = unidade?.logomarcaRelatorio || unidade?.logomarca || '';
    const nomeFantasia = unidade?.nomeFantasia || 'Instituição';
    const razaoSocial = unidade?.razaoSocial || nomeFantasia;
    const cnpj = unidade?.cnpj || '---';
    const endereco = [unidade?.endereco, unidade?.numeroEndereco, unidade?.bairro, unidade?.cidade, unidade?.estado]
      .filter((valor) => (valor ?? '').toString().trim().length > 0)
      .join(' - ');
    const telefoneUnidade = unidade?.telefone || '';
    const emailUnidade = unidade?.email || '';

    const dataEntrega = record.deliveryDate || record.date;
    const dataFormatada = dataEntrega ? this.formatarDataExtenso(dataEntrega) : '';
    const beneficiario = this.obterDadosBeneficiario(record);
    const beneficiarioDocumento = beneficiario?.cpf
      ? this.formatCpf(beneficiario.cpf)
      : record.beneficiaryDocument || 'Não informado';
    const codigoBeneficiario = beneficiario?.id || record.beneficiaryId || '---';
    const responsavel = record.responsible || 'Não informado';
    const enderecoCompleto = beneficiario?.address || 'Não informado';
    const cidade = beneficiario?.city || 'Não informado';
    const uf = beneficiario?.state || 'Não informado';
    const telefoneBeneficiario = beneficiario?.phone ? this.formatPhoneValue(beneficiario.phone) : 'Não informado';
    const nomeRelatorio = 'TERMO DE RECEBIMENTO DE DOAÇÕES';

    const itensHtml = record.items
      .map(
        (item) => `
          <tr>
            <td>${this.escapeHtml(item.description)}</td>
            <td class="center">${this.escapeHtml(item.unit)}</td>
            <td class="center">${item.quantity}</td>
            <td>${this.escapeHtml(item.notes || '')}</td>
          </tr>
        `
      )
      .join('');

    const termo = `Eu, ${record.beneficiaryName}, portador do CPF nº ${beneficiarioDocumento}, declaro para os devidos fins que recebi gratuitamente da ${razaoSocial}, a doação acima discriminada, destinada a suprir minhas necessidades básicas.`;

    const logoHtml = logo
      ? `<img src="${logo}" alt="Logomarca da unidade" />`
      : `<div class="logo-placeholder">Logomarca</div>`;

    return `
      <!DOCTYPE html>
      <html lang="pt-BR">
        <head>
          <meta charset="utf-8" />
          <title>${nomeRelatorio}</title>
          <style>
            @page { size: A4; margin: 20mm; }
            * { box-sizing: border-box; }
            body { margin: 0; font-family: Arial, sans-serif; color: #111827; }
            .report { display: flex; flex-direction: column; gap: 14px; }
            header.report-header { display: grid; grid-template-columns: 120px 1fr; gap: 16px; align-items: center; border-bottom: 2px solid #e2e8f0; padding-bottom: 10px; }
            header.report-header img { width: 120px; height: auto; object-fit: contain; }
            .logo-placeholder { width: 120px; height: 80px; border: 1px dashed #94a3b8; display: flex; align-items: center; justify-content: center; color: #64748b; font-size: 12px; }
            .report-title { font-size: 18px; font-weight: 700; margin: 0; text-transform: uppercase; text-align: center; }
            .report-subtitle { font-size: 16px; color: #111827; margin: 6px 0 0; text-align: center; text-transform: uppercase; letter-spacing: 0.05em; font-weight: 800; }
            .meta { display: flex; gap: 14px; flex-wrap: wrap; font-size: 12px; color: #4b5563; }
            section { border: 1px solid #e2e8f0; border-radius: 10px; padding: 12px; }
            h2 { font-size: 13px; margin: 0 0 8px 0; text-transform: uppercase; letter-spacing: 0.04em; color: #0f7a43; }
            table { width: 100%; border-collapse: collapse; font-size: 12px; }
            th, td { padding: 8px; border-bottom: 1px solid #e2e8f0; text-align: left; vertical-align: top; }
            th { background: #f8fafc; font-weight: 700; text-transform: uppercase; font-size: 11px; }
            td.center, th.center { text-align: center; }
            .termo { margin-top: 12px; font-size: 12px; line-height: 1.6; }
            .assinaturas { margin-top: 24px; display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 24px; }
            .assinatura { text-align: center; font-size: 12px; }
            .linha { border-top: 1px solid #111827; margin-top: 34px; }
            .beneficiario-destaque { display: flex; align-items: center; justify-content: space-between; gap: 12px; margin: 0 0 6px; }
            .beneficiario-nome { font-size: 14px; font-weight: 800; color: #0f172a; text-transform: uppercase; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
            .beneficiario-codigo { font-size: 14px; font-weight: 800; color: #0f172a; text-transform: uppercase; white-space: nowrap; }
            footer.report-footer { text-align: center; padding: 6px 0; border-top: 1px solid #e2e8f0; margin-top: 12px; font-size: 10px; color: #111827; page-break-inside: avoid; font-weight: 400; position: fixed; left: 20mm; right: 20mm; bottom: 8mm; background: #fff; }
            footer.report-footer .footer-name { font-weight: 400; letter-spacing: 0; color: #111827; margin: 0 0 2px; text-transform: none; }
            footer.report-footer .footer-info { margin: 0; }
            footer.report-footer .rodape-texto { margin-top: 6px; font-size: 10px; color: #9ca3af; }
            footer.report-footer .page-number { display: block; text-align: right; }
            .page-number:before { content: "Página " counter(page) " de " counter(pages); }
          </style>
        </head>
        <body>
          <div class="report">
            <header class="report-header">
              ${logoHtml}
              <div>
                <div class="report-title">${this.escapeHtml(razaoSocial)}</div>
                <p class="report-subtitle">${nomeRelatorio}</p>
              </div>
            </header>

            <section>
              <h2>Dados do beneficiário</h2>
              <div class="beneficiario-destaque">
                <span class="beneficiario-nome">${this.escapeHtml(record.beneficiaryName)}</span>
                <span class="beneficiario-codigo">Código: ${this.escapeHtml(String(codigoBeneficiario))}</span>
              </div>
              <div class="meta">
                <span><strong>CPF:</strong> ${this.escapeHtml(beneficiarioDocumento)}</span>
                <span><strong>Telefone:</strong> ${this.escapeHtml(telefoneBeneficiario)}</span>
                <span><strong>Cidade/UF:</strong> ${this.escapeHtml(`${cidade} / ${uf}`)}</span>
                <span><strong>Responsável:</strong> ${this.escapeHtml(responsavel)}</span>
                <span><strong>Data da doação:</strong> ${this.escapeHtml(dataFormatada)}</span>
                <span><strong>Endereço:</strong> ${this.escapeHtml(enderecoCompleto)}</span>
              </div>
            </section>

            <section>
              <h2>Itens doados</h2>
              <table>
                <thead>
                  <tr>
                    <th>Item</th>
                    <th class="center">Unidade</th>
                    <th class="center">Quantidade</th>
                    <th>Observações</th>
                  </tr>
                </thead>
                <tbody>
                  ${itensHtml}
                </tbody>
              </table>
              <p class="termo">${this.escapeHtml(termo)}</p>
              <div class="assinaturas">
                <div class="assinatura">
                  <div class="linha"></div>
                  ${this.escapeHtml(record.beneficiaryName)}
                </div>
                <div class="assinatura">
                  <div class="linha"></div>
                  ADRA
                </div>
              </div>
            </section>

            <footer class="report-footer">
              <p class="footer-name">${this.escapeHtml(razaoSocial)}</p>
              <p class="footer-info">
                ${this.escapeHtml(this.joinParts([
                  cnpj ? `CNPJ: ${cnpj}` : '',
                  endereco,
                  this.joinParts([telefoneUnidade, emailUnidade], ' | ')
                ], ' | ') || 'Endereço não informado')}
              </p>
              <p class="footer-info"><span class="page-number"></span></p>
            </footer>
          </div>
        </body>
      </html>
    `;
  }

  onFechar(): void {
    window.history.back();
  }

  private escapeHtml(valor: string): string {
    return (valor || '')
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;')
      .replace(/'/g, '&#39;');
  }

  private formatarDataExtenso(data: string): string {
    const base = data.split('T')[0];
    const partes = base.split('-').map((valor) => Number(valor));
    if (partes.length < 3 || partes.some((valor) => Number.isNaN(valor))) {
      return data;
    }
    const [ano, mes, dia] = partes;
    const dataBase = new Date(ano, mes - 1, dia);
    if (Number.isNaN(dataBase.getTime())) {
      return data;
    }
    return dataBase.toLocaleDateString('pt-BR', { day: '2-digit', month: 'long', year: 'numeric' });
  }

  private obterDadosBeneficiario(record: DonationRecord): Beneficiary | null {
    const selecionado = this.selectedBeneficiary();
    if (selecionado && String(selecionado.id) === String(record.beneficiaryId)) {
      return selecionado;
    }
    const lista = this.beneficiaries();
    const encontrado = lista.find((item) => String(item.id) === String(record.beneficiaryId));
    return encontrado ?? null;
  }

  private joinParts(parts: Array<string | null | undefined>, separador: string): string {
    return parts
      .filter((valor) => (valor ?? '').toString().trim().length > 0)
      .join(separador);
  }

}























