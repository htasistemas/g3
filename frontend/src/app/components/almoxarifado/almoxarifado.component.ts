import { CommonModule } from '@angular/common';
import { Component, computed, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import {
  faArrowTrendUp,
  faBoxArchive,
  faCircleCheck,
  faCircleExclamation,
  faClipboardList,
  faDownload,
  faFilter,
  faMagnifyingGlass,
  faMoneyBillTrendUp,
  faPlus,
  faRotate,
  faSliders,
  faTriangleExclamation,
  faUser,
  faWarehouse
} from '@fortawesome/free-solid-svg-icons';
import {
  AdjustmentDirection,
  AlmoxarifadoService,
  MovementType,
  StockItem,
  StockItemPayload,
  StockItemStatus,
  StockMovement
} from '../../services/almoxarifado.service';

type AlmoxTabId = 'cadastro' | 'itens' | 'movimentacoes' | 'dashboards';

interface ItemFilters {
  term: string;
  category: string;
  status: 'all' | StockItemStatus;
  belowMinOnly: boolean;
}

interface MovementFilters {
  startDate: string;
  endDate: string;
  type: MovementType | 'Todos';
  itemSearch: string;
  responsible: string;
}

interface ItemFormState {
  code: string;
  description: string;
  category?: string;
  unit?: string;
  location?: string;
  locationDetail?: string;
  currentStock: number;
  minStock: number;
  unitValue: number;
  status: StockItemStatus;
  notes?: string;
}

interface MovementFormState {
  date: string;
  type: MovementType;
  itemCode: string;
  quantity: number;
  reference: string;
  responsible: string;
  notes: string;
  adjustmentDirection: AdjustmentDirection;
}

@Component({
  selector: 'app-almoxarifado',
  standalone: true,
  imports: [CommonModule, FormsModule, FontAwesomeModule, TelaPadraoComponent],
  templateUrl: './almoxarifado.component.html',
  styleUrl: './almoxarifado.component.scss'
})
export class AlmoxarifadoComponent implements OnInit {
  readonly faBoxArchive = faBoxArchive;
  readonly faTriangleExclamation = faTriangleExclamation;
  readonly faMoneyBillTrendUp = faMoneyBillTrendUp;
  readonly faArrowTrendUp = faArrowTrendUp;
  readonly faMagnifyingGlass = faMagnifyingGlass;
  readonly faFilter = faFilter;
  readonly faPlus = faPlus;
  readonly faDownload = faDownload;
  readonly faClipboardList = faClipboardList;
  readonly faRotate = faRotate;
  readonly faSliders = faSliders;
  readonly faWarehouse = faWarehouse;
  readonly faCircleCheck = faCircleCheck;
  readonly faCircleExclamation = faCircleExclamation;
  readonly faUser = faUser;

  constructor(private readonly almoxarifadoService: AlmoxarifadoService) {}

  readonly tabs: { id: AlmoxTabId; label: string; description: string }[] = [
    { id: 'cadastro', label: 'Cadastros de itens', description: 'Estruture o item com campos obrigatórios e validações.' },
    { id: 'itens', label: 'Itens do almoxarifado', description: 'Consulte rapidamente os itens ativos e críticos.' },
    { id: 'movimentacoes', label: 'Movimentações', description: 'Registre e acompanhe entradas, saídas e ajustes.' },
    { id: 'dashboards', label: 'Dashboards', description: 'Indicadores operacionais e visão consolidada do estoque.' }
  ];

  activeTab: AlmoxTabId = 'cadastro';

  private readonly todayIso = new Date().toISOString().substring(0, 10);

  items = signal<StockItem[]>([]);

  movements = signal<StockMovement[]>([]);

  itemFilters: ItemFilters = {
    term: '',
    category: 'all',
    status: 'all',
    belowMinOnly: false
  };

  movementFilters: MovementFilters = {
    startDate: '',
    endDate: '',
    type: 'Todos',
    itemSearch: '',
    responsible: ''
  };

  formError: string | null = null;
  movementError: string | null = null;
  successMessage: string | null = null;

  itemForm: ItemFormState = this.createEmptyItemForm();
  editingItemId: string | null = null;
  editingItemCode: string | null = null;

  movementForm: MovementFormState = {
    date: this.todayIso,
    type: 'Entrada',
    itemCode: '',
    quantity: 1,
    reference: '',
    responsible: '',
    notes: '',
    adjustmentDirection: 'increase'
  };

  showMovementModal = false;

  ngOnInit(): void {
    this.loadItems();
    this.loadMovements();
    this.loadNextItemCode();
  }

  private loadItems(): void {
    this.formError = null;
    this.almoxarifadoService.listItems().subscribe({
      next: (items) => this.items.set(items),
      error: () => {
        this.formError = 'Não foi possível carregar os itens do almoxarifado.';
      }
    });
  }

  private loadNextItemCode(): void {
    if (this.editingItemId) {
      return;
    }

    this.almoxarifadoService.getNextItemCode().subscribe({
      next: (code) => {
        if (!this.editingItemId) {
          this.itemForm = { ...this.itemForm, code };
        }
      },
      error: () => {
        this.formError = this.formError || 'Não foi possível gerar o próximo código do item.';
      }
    });
  }

  private loadMovements(): void {
    this.movementError = null;
    this.almoxarifadoService.listMovements().subscribe({
      next: (movements) => this.movements.set(movements),
      error: () => {
        this.movementError = 'Não foi possível carregar as movimentações do almoxarifado.';
      }
    });
  }

  changeTab(tabId: AlmoxTabId): void {
    this.activeTab = tabId;
  }

  goToNextTab(): void {
    if (this.hasNextTab) {
      this.activeTab = this.tabs[this.activeTabIndex + 1].id;
    }
  }

  goToPreviousTab(): void {
    if (this.hasPreviousTab) {
      this.activeTab = this.tabs[this.activeTabIndex - 1].id;
    }
  }

  get activeTabIndex(): number {
    return this.tabs.findIndex((tab) => tab.id === this.activeTab);
  }

  get hasNextTab(): boolean {
    return this.activeTabIndex < this.tabs.length - 1;
  }

  get hasPreviousTab(): boolean {
    return this.activeTabIndex > 0;
  }

  get nextTabLabel(): string {
    const nextTab = this.tabs[this.activeTabIndex + 1];
    return nextTab ? nextTab.label : '';
  }

  readonly indicatorTotals = computed(() => {
    const allItems = this.items();
    const belowMin = allItems.filter((item) => item.currentStock < item.minStock).length;
    const totalValue = allItems.reduce(
      (sum, item) => sum + item.currentStock * (Number(item.unitValue) || 0),
      0
    );
    const thirtyDaysAgo = new Date();
    thirtyDaysAgo.setDate(thirtyDaysAgo.getDate() - 30);
    const recentMovements = this.movements().filter((movement) =>
      new Date(movement.date) >= thirtyDaysAgo
    ).length;

    return {
      totalItems: allItems.length,
      belowMin,
      totalValue,
      recentMovements
    };
  });

  get categories(): string[] {
    const raw = this.items().map((item) => item.category).filter(Boolean) as string[];
    const unique = Array.from(new Set(raw));
    return unique.sort();
  }

  get filteredItems(): StockItem[] {
    const term = this.itemFilters.term.toLowerCase();
    return this.items().filter((item) => {
      const matchesTerm =
        !term || item.description.toLowerCase().includes(term) || item.code.toLowerCase().includes(term);
      const matchesCategory =
        this.itemFilters.category === 'all' || item.category === this.itemFilters.category;
      const matchesStatus =
        this.itemFilters.status === 'all' || item.status === this.itemFilters.status;
      const matchesMin = !this.itemFilters.belowMinOnly || item.currentStock < item.minStock;
      return matchesTerm && matchesCategory && matchesStatus && matchesMin;
    });
  }

  get filteredMovements(): StockMovement[] {
    return this.movements().filter((movement) => {
      if (this.movementFilters.type !== 'Todos' && movement.type !== this.movementFilters.type) {
        return false;
      }

      if (this.movementFilters.startDate && movement.date < this.movementFilters.startDate) {
        return false;
      }

      if (this.movementFilters.endDate && movement.date > this.movementFilters.endDate) {
        return false;
      }

      if (
        this.movementFilters.itemSearch &&
        !(`${movement.itemCode} ${movement.itemDescription}`
          .toLowerCase()
          .includes(this.movementFilters.itemSearch.toLowerCase()))
      ) {
        return false;
      }

      if (
        this.movementFilters.responsible &&
        !(movement.responsible ?? '').toLowerCase().includes(this.movementFilters.responsible.toLowerCase())
      ) {
        return false;
      }

      return true;
    });
  }

  editItem(item: StockItem): void {
    this.itemForm = {
      code: item.code,
      description: item.description,
      category: item.category ?? '',
      unit: item.unit ?? '',
      location: item.location ?? '',
      locationDetail: item.locationDetail ?? '',
      currentStock: item.currentStock,
      minStock: item.minStock,
      unitValue: item.unitValue,
      status: item.status,
      notes: item.notes ?? ''
    };
    this.editingItemId = item.id;
    this.editingItemCode = item.code;
    this.formError = null;
    this.successMessage = null;
  }

  resetItemForm(): void {
    this.itemForm = this.createEmptyItemForm();
    this.editingItemId = null;
    this.editingItemCode = null;
    this.formError = null;
    this.successMessage = null;
    this.loadNextItemCode();
  }

  saveItem(): void {
    const validationErrors = this.validateItemForm();

    if (validationErrors.length) {
      this.formError = validationErrors.join(' • ');
      return;
    }

    this.formError = null;
    const formData: StockItemPayload = {
      code: this.itemForm.code,
      description: this.itemForm.description,
      category: this.itemForm.category ?? '',
      unit: this.itemForm.unit ?? '',
      location: this.itemForm.location || undefined,
      locationDetail: this.itemForm.locationDetail || undefined,
      currentStock: Number(this.itemForm.currentStock) || 0,
      minStock: Number(this.itemForm.minStock),
      unitValue: Number(this.itemForm.unitValue) || 0,
      status: this.itemForm.status,
      notes: this.itemForm.notes || undefined
    };

    const request$ = this.editingItemId
      ? this.almoxarifadoService.updateItem(this.editingItemId, formData)
      : this.almoxarifadoService.createItem(formData);

    request$.subscribe({
      next: (item) => {
        const updatedItems = this.editingItemId
          ? this.items().map((existing) => (existing.id === item.id ? item : existing))
          : [item, ...this.items()];

        this.items.set(updatedItems);
        this.successMessage = this.editingItemId
          ? 'Item atualizado com sucesso e pronto para novas movimentações.'
          : 'Item cadastrado com sucesso no almoxarifado.';
        this.resetItemForm();
      },
      error: (error) => {
        this.formError = error?.error?.message || 'Não foi possível salvar o item de estoque.';
      }
    });
  }

  openMovementModal(item?: StockItem): void {
    this.movementError = null;
    this.movementForm = {
      date: this.todayIso,
      type: 'Entrada',
      itemCode: item?.code ?? '',
      quantity: 1,
      reference: '',
      responsible: '',
      notes: '',
      adjustmentDirection: 'increase'
    };
    this.showMovementModal = true;
  }

  saveMovement(): void {
    this.movementError = null;
    const { date, type, itemCode, quantity, reference, responsible, notes, adjustmentDirection } =
      this.movementForm;

    if (!type || !itemCode || !quantity || quantity <= 0) {
      this.movementError = 'Preencha os campos obrigatórios e informe uma quantidade válida.';
      return;
    }

    if (type === 'Ajuste') {
      const adjustNegative = adjustmentDirection === 'decrease';

      const message = adjustNegative
        ? 'Confirma reduzir o estoque deste item? Este ajuste é permanente.'
        : 'Confirma aumentar o estoque deste item com este ajuste?';

      const confirmed = window.confirm(message);
      if (!confirmed) {
        return;
      }

    }

    this.almoxarifadoService
      .registerMovement({
        date,
        type,
        itemCode,
        quantity,
        reference,
        responsible,
        notes: notes || undefined,
        adjustmentDirection
      })
      .subscribe({
        next: ({ movement, item }) => {
          this.movements.set([movement, ...this.movements()]);
          this.items.set(this.items().map((existing) => (existing.id === item.id ? item : existing)));
          this.showMovementModal = false;
        },
        error: (error) => {
          this.movementError = error?.error?.message || 'Não foi possível registrar a movimentação.';
        }
      });
  }

  private validateItemForm(): string[] {
    const errors: string[] = [];
    const requiredFields: Array<keyof ItemFormState> = [
      'code',
      'description',
      'category',
      'unit',
      'minStock'
    ];

    requiredFields.forEach((field) => {
      const value = this.itemForm[field];
      const isEmpty = value === '' || value === null || value === undefined;
      if (isEmpty || (typeof value === 'string' && !value.trim())) {
        errors.push('Campo obrigatório não preenchido: ' + this.translateFieldLabel(field));
      }
    });

    if (this.itemForm.minStock < 0) {
      errors.push('O estoque mínimo deve ser maior ou igual a zero.');
    }

    if (this.itemForm.currentStock < 0) {
      errors.push('O estoque atual não pode ser negativo.');
    }

    return errors;
  }

  private translateFieldLabel(field: keyof ItemFormState): string {
    const map: Record<keyof ItemFormState, string> = {
      code: 'Código',
      description: 'Descrição',
      category: 'Categoria',
      unit: 'Unidade de medida',
      location: 'Localização',
      locationDetail: 'Localização interna',
      currentStock: 'Estoque atual',
      minStock: 'Estoque mínimo',
      unitValue: 'Valor unitário',
      status: 'Situação',
      notes: 'Observações'
    };

    return map[field];
  }

  private createEmptyItemForm(): ItemFormState {
    return {
      code: '',
      description: '',
      category: '',
      unit: '',
      location: '',
      locationDetail: '',
      currentStock: 0,
      minStock: 0,
      unitValue: 0,
      status: 'Ativo',
      notes: ''
    };
  }
}
