import { CommonModule } from '@angular/common';
import { Component, computed, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
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

type MovementType = 'Entrada' | 'Saída' | 'Ajuste';

type StockItemStatus = 'Ativo' | 'Inativo';

type AdjustmentDirection = 'increase' | 'decrease';

interface StockItem {
  code: string;
  description: string;
  category: string;
  unit: string;
  location: string;
  locationDetail: string;
  currentStock: number;
  minStock: number;
  unitValue: number;
  status: StockItemStatus;
  notes?: string;
}

interface StockMovement {
  date: string;
  type: MovementType;
  itemCode: string;
  itemDescription: string;
  quantity: number;
  balanceAfter: number;
  reference: string;
  responsible: string;
  notes?: string;
}

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
  category: string;
  unit: string;
  location: string;
  locationDetail: string;
  currentStock: number;
  minStock: number;
  unitValue: number;
  status: StockItemStatus;
  notes: string;
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
  imports: [CommonModule, FormsModule, FontAwesomeModule],
  templateUrl: './almoxarifado.component.html',
  styleUrl: './almoxarifado.component.scss'
})
export class AlmoxarifadoComponent {
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

  private readonly todayIso = new Date().toISOString().substring(0, 10);

  items = signal<StockItem[]>([
    {
      code: 'ALM-001',
      description: 'Álcool em gel 70% 500ml',
      category: 'Higiene',
      unit: 'Frasco',
      location: 'Sala de materiais',
      locationDetail: 'Prateleira A1',
      currentStock: 85,
      minStock: 40,
      unitValue: 12.5,
      status: 'Ativo',
      notes: 'Reposição semanal preferencial'
    },
    {
      code: 'ALM-002',
      description: 'Luvas descartáveis tamanho M',
      category: 'EPI',
      unit: 'Caixa',
      location: 'Depósito central',
      locationDetail: 'Estante B2',
      currentStock: 18,
      minStock: 30,
      unitValue: 32.9,
      status: 'Ativo'
    },
    {
      code: 'ALM-003',
      description: 'Papel sulfite A4 75g',
      category: 'Escritório',
      unit: 'Resma',
      location: 'Almoxarifado administrativo',
      locationDetail: 'Prateleira C1',
      currentStock: 120,
      minStock: 60,
      unitValue: 24.5,
      status: 'Ativo'
    },
    {
      code: 'ALM-004',
      description: 'Cartucho de impressora HP 664',
      category: 'Escritório',
      unit: 'Unidade',
      location: 'Almoxarifado administrativo',
      locationDetail: 'Prateleira C3',
      currentStock: 6,
      minStock: 10,
      unitValue: 95.0,
      status: 'Inativo',
      notes: 'Modelo sendo descontinuado'
    }
  ]);

  movements = signal<StockMovement[]>([
    {
      date: this.todayIso,
      type: 'Entrada',
      itemCode: 'ALM-001',
      itemDescription: 'Álcool em gel 70% 500ml',
      quantity: 20,
      balanceAfter: 85,
      reference: 'NF 02314',
      responsible: 'Maria Silva',
      notes: 'Reposição mensal'
    },
    {
      date: this.offsetDate(-3),
      type: 'Saída',
      itemCode: 'ALM-002',
      itemDescription: 'Luvas descartáveis tamanho M',
      quantity: 12,
      balanceAfter: 18,
      reference: 'Req 0081',
      responsible: 'Equipe Saúde',
      notes: 'Atendimento interno'
    },
    {
      date: this.offsetDate(-15),
      type: 'Entrada',
      itemCode: 'ALM-003',
      itemDescription: 'Papel sulfite A4 75g',
      quantity: 60,
      balanceAfter: 120,
      reference: 'NF 01998',
      responsible: 'João Mendes'
    }
  ]);

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

  readonly indicatorTotals = computed(() => {
    const allItems = this.items();
    const belowMin = allItems.filter((item) => item.currentStock < item.minStock).length;
    const totalValue = allItems.reduce((sum, item) => sum + item.currentStock * item.unitValue, 0);
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
    const raw = this.items().map((item) => item.category);
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
        !movement.responsible.toLowerCase().includes(this.movementFilters.responsible.toLowerCase())
      ) {
        return false;
      }

      return true;
    });
  }

  editItem(item: StockItem): void {
    this.itemForm = { ...item, notes: item.notes ?? '' };
    this.editingItemCode = item.code;
    this.formError = null;
    this.successMessage = null;
  }

  resetItemForm(): void {
    this.itemForm = this.createEmptyItemForm();
    this.editingItemCode = null;
    this.formError = null;
    this.successMessage = null;
  }

  saveItem(): void {
    const validationErrors = this.validateItemForm();

    if (validationErrors.length) {
      this.formError = validationErrors.join(' • ');
      return;
    }

    this.formError = null;
    const formData: StockItem = {
      ...this.itemForm,
      currentStock: Number(this.itemForm.currentStock) || 0,
      minStock: Number(this.itemForm.minStock),
      unitValue: Number(this.itemForm.unitValue) || 0,
      notes: this.itemForm.notes || undefined
    };

    const updatedItems = [...this.items()];
    const existingIndex = updatedItems.findIndex((item) => item.code === this.editingItemCode);

    if (existingIndex >= 0) {
      updatedItems[existingIndex] = formData;
      this.successMessage = 'Item atualizado com sucesso e pronto para novas movimentações.';
    } else {
      updatedItems.push(formData);
      this.successMessage = 'Item cadastrado com sucesso no almoxarifado.';
    }

    this.items.set(updatedItems);
    this.resetItemForm();
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

    const items = [...this.items()];
    const targetItem = items.find((item) => item.code === itemCode);

    if (!targetItem) {
      this.movementError = 'Selecione um item cadastrado para registrar a movimentação.';
      return;
    }

    let quantityChange = quantity;
    if (type === 'Saída') {
      if (quantity > targetItem.currentStock) {
        this.movementError = 'Não é possível registrar saída maior que o estoque atual do item.';
        return;
      }
      quantityChange = -quantity;
    }

    if (type === 'Ajuste') {
      const adjustNegative = adjustmentDirection === 'decrease';
      if (adjustNegative && quantity > targetItem.currentStock) {
        this.movementError = 'Ajuste negativo não pode deixar o estoque negativo. Revise a quantia.';
        return;
      }

      const message = adjustNegative
        ? 'Confirma reduzir o estoque deste item? Este ajuste é permanente.'
        : 'Confirma aumentar o estoque deste item com este ajuste?';

      const confirmed = window.confirm(message);
      if (!confirmed) {
        return;
      }

      quantityChange = adjustNegative ? -quantity : quantity;
    }

    const newBalance = targetItem.currentStock + quantityChange;
    targetItem.currentStock = newBalance;

    const newMovement: StockMovement = {
      date,
      type,
      itemCode,
      itemDescription: targetItem.description,
      quantity,
      balanceAfter: newBalance,
      reference,
      responsible,
      notes: notes || undefined
    };

    this.movements.set([newMovement, ...this.movements()]);
    this.items.set(items);
    this.showMovementModal = false;
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

  private offsetDate(days: number): string {
    const date = new Date();
    date.setDate(date.getDate() + days);
    return date.toISOString().substring(0, 10);
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
