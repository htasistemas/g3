import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faArrowDown,
  faArrowUp,
  faBuilding,
  faCloudArrowUp,
  faDownload,
  faFileInvoice,
  faFloppyDisk,
  faMagnifyingGlass,
  faPrint,
  faQrcode,
  faTags,
  faTools,
  faTrash,
  faTruck
} from '@fortawesome/free-solid-svg-icons';
import { Patrimonio, PatrimonioService } from '../../services/patrimonio.service';
import { AssistanceUnitPayload, AssistanceUnitService } from '../../services/assistance-unit.service';
import { finalize, timeout } from 'rxjs/operators';

type AssetFormTextField =
  | 'patrimonyNumber'
  | 'name'
  | 'category'
  | 'subcategory'
  | 'description'
  | 'individualNumbers'
  | 'acquisitionDate'
  | 'origin'
  | 'invoiceNumber'
  | 'supplier'
  | 'unit'
  | 'room'
  | 'address'
  | 'responsibleName'
  | 'responsibleContact'
  | 'internalCode'
  | 'observations'
  | 'warranty'
  | 'manualUrl'
  | 'lastMovement'
  | 'lastResponsible'
  | 'lastMaintenance';

interface AssetForm {
  patrimonyNumber: string;
  name: string;
  category: string;
  subcategory: string;
  description: string;
  conservation: string;
  status: string;
  quantity: number;
  individualNumbers: string;
  acquisitionDate: string;
  acquisitionValue: number;
  origin: string;
  invoiceNumber: string;
  supplier: string;
  usefulLife: number;
  depreciationStartDate: string;
  depreciationRate: number;
  unit: string;
  room: string;
  address: string;
  responsibleName: string;
  responsibleContact: string;
  internalCode: string;
  observations: string;
  warranty: string;
  manualUrl: string;
  lastMovement: string;
  lastResponsible: string;
  lastMaintenance: string;
}

@Component({
  selector: 'app-patrimonio',
  standalone: true,
  imports: [CommonModule, FormsModule, FontAwesomeModule],
  templateUrl: './patrimonio.component.html',
  styleUrl: './patrimonio.component.scss'
})
export class PatrimonioComponent implements OnInit {
  readonly faArrowDown = faArrowDown;
  readonly faArrowUp = faArrowUp;
  readonly faDownload = faDownload;
  readonly faFileInvoice = faFileInvoice;
  readonly faFloppyDisk = faFloppyDisk;
  readonly faMagnifyingGlass = faMagnifyingGlass;
  readonly faPrint = faPrint;
  readonly faQrcode = faQrcode;
  readonly faTools = faTools;
  readonly faTruck = faTruck;
  readonly faTags = faTags;
  readonly faBuilding = faBuilding;
  readonly faCloudArrowUp = faCloudArrowUp;
  readonly faTrash = faTrash;

  conservationOptions = ['Novo', 'Bom', 'Regular', 'Ruim', 'Inservível'];
  statusOptions = ['Ativo', 'Em manutenção', 'Em empréstimo', 'Baixado / Inativo'];
  origins = ['Compra', 'Doação', 'Convênio', 'Transferência'];
  categories = [
    { label: 'Móveis', subcategories: ['Mesa', 'Cadeira', 'Armário', 'Estante'] },
    { label: 'Eletrodomésticos', subcategories: ['Geladeira', 'Micro-ondas', 'Bebedouro'] },
    { label: 'Informática', subcategories: ['Notebook', 'Desktop', 'Monitor', 'Impressora'] },
    { label: 'Veículos', subcategories: ['Automóvel', 'Caminhão', 'Motocicleta'] }
  ];

  assetForm: AssetForm = {
    patrimonyNumber: '',
    name: '',
    category: '',
    subcategory: '',
    description: '',
    conservation: this.conservationOptions[0],
    status: this.statusOptions[0],
    quantity: 1,
    individualNumbers: '',
    acquisitionDate: '',
    acquisitionValue: 0,
    origin: this.origins[0],
    invoiceNumber: '',
    supplier: '',
    usefulLife: 0,
    depreciationStartDate: '',
    depreciationRate: 2,
    unit: '',
    room: '',
    address: '',
    responsibleName: '',
    responsibleContact: '',
    internalCode: '',
    observations: '',
    warranty: '',
    manualUrl: '',
    lastMovement: 'Não registrado',
    lastResponsible: 'Não registrado',
    lastMaintenance: 'Não registrada'
  };

  assetLibrary: Patrimonio[] = [];
  movementForm = this.createMovementForm();
  selectedAsset: Patrimonio | null = null;
  isSaving = false;
  isLoading = false;
  errorMessage: string | null = null;

  filePreview: string | ArrayBuffer | null = null;
  qrCodeValue = 'QR-PATRIMONIO-001';
  searchTerm = '';
  displayedAcquisitionValue = '';
  cessionTermPreview = '';
  loanTermPreview = '';
  printOrder: 'alphabetical' | 'patrimony' | 'value' | 'location' = 'alphabetical';
  printLocationFilter: string = 'all';
  assistanceUnit: AssistanceUnitPayload | null = null;
  editingAssetId: string | null = null;
  isRegisteringMovement = false;
  movementError: string | null = null;

  constructor(
    private readonly patrimonioService: PatrimonioService,
    private readonly assistanceUnitService: AssistanceUnitService
  ) {}

  ngOnInit(): void {
    this.loadAssets();
    this.loadAssistanceUnit();
    this.displayedAcquisitionValue = this.formatCurrency(this.assetForm.acquisitionValue);
    this.updateCessionTermPreview();
  }

  get filteredAssets(): Patrimonio[] {
    const term = this.searchTerm.toLowerCase();
    return this.assetLibrary.filter((asset) =>
      [asset.numeroPatrimonio, asset.nome, asset.responsavel ?? ''].some((value) =>
        (value || '').toLowerCase().includes(term)
      )
    );
  }

  get totalAcquisitionValue(): number {
    return this.assetLibrary.reduce((sum, asset) => sum + Number(asset.valorAquisicao ?? 0), 0);
  }

  get monthlyDepreciation(): number {
    const totalRate = this.assetLibrary.reduce((sum, asset) => sum + Number(asset.taxaDepreciacao ?? 0), 0);
    return this.assetLibrary.length ? totalRate / this.assetLibrary.length : 0;
  }

  get activeAssets(): number {
    return this.assetLibrary.filter((asset) => (asset.status ?? '').toLowerCase().includes('ativo')).length;
  }

  get maintenanceAssets(): number {
    return this.assetLibrary.filter((asset) => (asset.status ?? '').toLowerCase().includes('manuten')).length;
  }

  get disposedAssets(): number {
    return this.assetLibrary.filter((asset) => (asset.status ?? '').toLowerCase().includes('baix')).length;
  }

  get availableLocations(): string[] {
    const unique = new Set(
      this.assetLibrary
        .map((asset) => this.getLocationLabel(asset))
        .filter((location) => location.trim().length > 0)
    );

    return Array.from(unique);
  }

  loadAssets(): void {
    this.isLoading = true;
    this.patrimonioService.list().subscribe({
      next: (patrimonios) => {
        this.assetLibrary = patrimonios;
        this.isLoading = false;
        this.selectedAsset = patrimonios[0] ?? null;
        this.updateCessionTermPreview();
      },
      error: () => {
        this.isLoading = false;
      }
    });
  }

  loadAssistanceUnit(): void {
    this.assistanceUnitService.get().subscribe(({ unidade }) => {
      this.assistanceUnit = unidade ?? null;
    });
  }

  private createMovementForm(asset?: Patrimonio) {
    return {
      tipo: 'MOVIMENTACAO' as 'MOVIMENTACAO' | 'MANUTENCAO' | 'BAIXA',
      destino: asset?.unidade ?? '',
      responsavel: asset?.responsavel ?? '',
      observacao: ''
    };
  }

  handleFileInput(event: Event): void {
    const target = event.target as HTMLInputElement;
    const file = target.files?.[0];

    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        this.filePreview = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }

  generateQrCode(): void {
    const uniqueSuffix = Math.random().toString(36).substring(2, 6).toUpperCase();
    this.qrCodeValue = `QR-${this.assetForm.patrimonyNumber || 'PATRIMONIO'}-${uniqueSuffix}`;
  }

  formatField(field: AssetFormTextField): void {
    const value = this.assetForm[field];
    if (typeof value === 'string') {
      this.assetForm[field] = this.toProperCase(value);
      this.updateCessionTermPreview();
    }
  }

  formatMovementField(field: 'destino' | 'responsavel'): void {
    const value = this.movementForm[field];
    this.movementForm[field] = this.toProperCase(value);
  }

  saveAsset(): void {
    if (this.isSaving) return;

    if (!this.assetForm.patrimonyNumber.trim() || !this.assetForm.name.trim()) {
      this.errorMessage = 'Informe o número e o nome do patrimônio para salvar.';
      return;
    }

    this.errorMessage = null;
    this.isSaving = true;
    const payload = {
      numeroPatrimonio: this.assetForm.patrimonyNumber || `PAT-${Date.now()}`,
      nome: this.assetForm.name,
      categoria: this.assetForm.category,
      subcategoria: this.assetForm.subcategory,
      conservacao: this.assetForm.conservation,
      status: this.assetForm.status,
      dataAquisicao: this.assetForm.acquisitionDate,
      valorAquisicao: this.assetForm.acquisitionValue,
      origem: this.assetForm.origin,
      responsavel: this.assetForm.responsibleName,
      unidade: this.assetForm.unit,
      sala: this.assetForm.room,
      taxaDepreciacao: this.assetForm.depreciationRate,
      observacoes: this.assetForm.observations
    };

    const save$ = this.editingAssetId
      ? this.patrimonioService.update(this.editingAssetId, payload)
      : this.patrimonioService.create(payload);

    save$
      .pipe(
        timeout(10000),
        finalize(() => {
          this.isSaving = false;
        })
      )
      .subscribe({
        next: (patrimonio) => {
          this.assetLibrary = this.editingAssetId
            ? this.assetLibrary.map((asset) => (asset.idPatrimonio === patrimonio.idPatrimonio ? patrimonio : asset))
            : [patrimonio, ...this.assetLibrary];
          this.selectedAsset = patrimonio;
          this.resetForm();
          this.updateCessionTermPreview();
        },
        error: (error) => {
          this.errorMessage =
            error?.error?.message ??
            `Não foi possível ${this.editingAssetId ? 'atualizar' : 'salvar'} o patrimônio. Tente novamente.`;
        }
      });
  }

  resetForm(): void {
    this.assetForm = {
      patrimonyNumber: '',
      name: '',
      category: '',
      subcategory: '',
      description: '',
      conservation: this.conservationOptions[0],
      status: this.statusOptions[0],
      quantity: 1,
      individualNumbers: '',
      acquisitionDate: '',
      acquisitionValue: 0,
      origin: this.origins[0],
      invoiceNumber: '',
      supplier: '',
      usefulLife: 0,
      depreciationStartDate: '',
      depreciationRate: 2,
      unit: '',
      room: '',
      address: '',
      responsibleName: '',
      responsibleContact: '',
      internalCode: '',
      observations: '',
      warranty: '',
      manualUrl: '',
      lastMovement: 'Não registrado',
      lastResponsible: 'Não registrado',
      lastMaintenance: 'Não registrada'
    };
    this.filePreview = null;
    this.displayedAcquisitionValue = this.formatCurrency(0);
    this.editingAssetId = null;
  }

  setSelected(asset: Patrimonio): void {
    this.selectedAsset = asset;
    this.movementForm = this.createMovementForm(asset);
    this.updateCessionTermPreview();
  }

  startEditingSelected(): void {
    if (!this.selectedAsset) return;
    this.populateFormFromAsset(this.selectedAsset);
    this.editingAssetId = this.selectedAsset.idPatrimonio;
    this.errorMessage = null;
  }

  cancelEdit(): void {
    this.resetForm();
  }

  quickMovement(asset: Patrimonio, tipo: 'MOVIMENTACAO' | 'MANUTENCAO' | 'BAIXA'): void {
    this.movementForm = { ...this.createMovementForm(asset), tipo };
    this.selectedAsset = asset;
    this.movementError = null;
    this.registerMovement();
  }

  private populateFormFromAsset(asset: Patrimonio): void {
    this.assetForm = {
      patrimonyNumber: asset.numeroPatrimonio,
      name: asset.nome,
      category: asset.categoria || '',
      subcategory: asset.subcategoria || '',
      description: asset.observacoes || '',
      conservation: asset.conservacao || this.conservationOptions[0],
      status: asset.status || this.statusOptions[0],
      quantity: 1,
      individualNumbers: '',
      acquisitionDate: asset.dataAquisicao || '',
      acquisitionValue: Number(asset.valorAquisicao ?? 0),
      origin: asset.origem || this.origins[0],
      invoiceNumber: '',
      supplier: '',
      usefulLife: 0,
      depreciationStartDate: '',
      depreciationRate: Number(asset.taxaDepreciacao ?? 0),
      unit: asset.unidade || '',
      room: asset.sala || '',
      address: '',
      responsibleName: asset.responsavel || '',
      responsibleContact: '',
      internalCode: '',
      observations: asset.observacoes || '',
      warranty: '',
      manualUrl: '',
      lastMovement: asset.movimentos?.[0]?.dataMovimento || 'Não registrado',
      lastResponsible: asset.movimentos?.[0]?.responsavel || 'Não registrado',
      lastMaintenance: asset.movimentos?.find((m) => m.tipo === 'MANUTENCAO')?.dataMovimento || 'Não registrada'
    };

    this.displayedAcquisitionValue = this.formatCurrency(this.assetForm.acquisitionValue);
    this.updateCessionTermPreview();
  }

  registerMovement(): void {
    if (!this.selectedAsset || this.isRegisteringMovement) return;

    this.isRegisteringMovement = true;
    this.movementError = null;

    this.patrimonioService
      .registerMovement(this.selectedAsset.idPatrimonio, {
        tipo: this.movementForm.tipo,
        destino: this.movementForm.destino,
        responsavel: this.movementForm.responsavel,
        observacao: this.movementForm.observacao
      })
      .pipe(finalize(() => (this.isRegisteringMovement = false)))
      .subscribe({
        next: (patrimonio) => {
          this.assetLibrary = this.assetLibrary.map((asset) =>
            asset.idPatrimonio === patrimonio.idPatrimonio ? patrimonio : asset
          );
          this.selectedAsset = patrimonio;
          this.movementForm = this.createMovementForm(patrimonio);
        },
        error: (error) => {
          this.movementError = error?.error?.message ?? 'Não foi possível registrar a movimentação';
        }
      });
  }

  onAcquisitionValueChange(rawValue: string): void {
    const numericValue = Number(rawValue.replace(/\D/g, '')) / 100;
    this.assetForm.acquisitionValue = numericValue;
    this.displayedAcquisitionValue = this.formatCurrency(numericValue);
  }

  addCategory(): void {
    const input = prompt('Informe o nome da nova categoria');
    if (!input) return;

    const formatted = this.toProperCase(input.trim());
    if (!formatted) return;

    const exists = this.categories.some((category) => category.label.toLowerCase() === formatted.toLowerCase());
    if (!exists) {
      this.categories = [...this.categories, { label: formatted, subcategories: [] }];
    }
    this.assetForm.category = formatted;
    this.assetForm.subcategory = '';
  }

  addSubcategory(): void {
    if (!this.assetForm.category) return;

    const input = prompt('Informe o nome da nova subcategoria');
    if (!input) return;

    const formatted = this.toProperCase(input.trim());
    if (!formatted) return;

    this.categories = this.categories.map((category) => {
      if (category.label === this.assetForm.category) {
        const exists = category.subcategories.some((sub) => sub.toLowerCase() === formatted.toLowerCase());
        if (!exists) {
          return { ...category, subcategories: [...category.subcategories, formatted] };
        }
      }
      return category;
    });

    this.assetForm.subcategory = formatted;
  }

  editCategories(): void {
    const current = this.categories.map((category) => category.label).join(', ');
    const input = prompt('Edite as categorias separadas por vírgula', current);
    if (input === null) return;

    const existingSubcategories = new Map(this.categories.map((category) => [category.label, category.subcategories]));
    const formatted = input
      .split(',')
      .map((item) => this.toProperCase(item.trim()))
      .filter((item) => item.length > 0);

    if (!formatted.length) return;

    this.categories = formatted.map((label) => ({
      label,
      subcategories: existingSubcategories.get(label) ?? []
    }));

    if (!formatted.includes(this.assetForm.category)) {
      this.assetForm.category = '';
      this.assetForm.subcategory = '';
    }
  }

  editSubcategories(): void {
    if (!this.assetForm.category) return;

    const currentCategory = this.categories.find((category) => category.label === this.assetForm.category);
    const input = prompt(
      'Edite as subcategorias separadas por vírgula',
      (currentCategory?.subcategories || []).join(', ')
    );
    if (input === null) return;

    const formatted = input
      .split(',')
      .map((item) => this.toProperCase(item.trim()))
      .filter((item) => item.length > 0);

    this.categories = this.categories.map((category) =>
      category.label === this.assetForm.category ? { ...category, subcategories: formatted } : category
    );

    if (!formatted.includes(this.assetForm.subcategory)) {
      this.assetForm.subcategory = '';
    }
  }

  generateCessionTerm(): void {
    const asset = this.selectedAsset ?? this.buildAssetSnapshot();
    const location = this.getLocationLabel(asset) || 'Local não informado';
    const content = `Termo de cessão do bem ${asset.nome || 'Não identificado'} (patrimônio ${
      asset.numeroPatrimonio || 'não informado'
    }) localizado em ${location}, sob responsabilidade de ${
      asset.responsavel || this.assetForm.responsibleName || 'Não informado'
    }. Valor de aquisição: ${this.formatCurrency(asset.valorAquisicao || this.assetForm.acquisitionValue)}.`;

    this.cessionTermPreview = content;
    this.openPrintWindow('Termo de cessão', `<p style="font-size:14px; line-height:1.6">${content}</p>`);
  }

  generateLoanTerm(): void {
    const asset = this.selectedAsset ?? this.buildAssetSnapshot();
    const location = this.getLocationLabel(asset) || 'Local não informado';
    const responsible = asset.responsavel || this.assetForm.responsibleName || 'Não informado';
    const content = `Eu, ${responsible}, assumo responsabilidade pelo empréstimo do bem ${
      asset.nome || 'Não identificado'
    } (patrimônio ${asset.numeroPatrimonio || 'não informado'}) a terceiros. O item está alocado em ${location} e deverá ser devolvido nas mesmas condições de conservação. Valor de referência: ${this.formatCurrency(
      asset.valorAquisicao || this.assetForm.acquisitionValue
    )}.`;

    this.loanTermPreview = content;
    this.openPrintWindow(
      'Termo de responsabilidade de empréstimo de bens a terceiros',
      `<p style="font-size:14px; line-height:1.6">${content}</p>`
    );
  }

  printAssets(): void {
    const filtered = this.assetLibrary.filter((asset) => {
      if (this.printLocationFilter === 'all') return true;
      return this.getLocationLabel(asset) === this.printLocationFilter;
    });

    const assets = [...filtered].sort((a, b) => {
      switch (this.printOrder) {
        case 'patrimony':
          return (a.numeroPatrimonio || '').localeCompare(b.numeroPatrimonio || '');
        case 'value':
          return (Number(b.valorAquisicao) || 0) - (Number(a.valorAquisicao) || 0);
        case 'location':
          return this.getLocationLabel(a).localeCompare(this.getLocationLabel(b));
        default:
          return (a.nome || '').localeCompare(b.nome || '');
      }
    });

    const unitTitle = [this.assistanceUnit?.nomeFantasia, this.assistanceUnit?.cidade]
      .filter(Boolean)
      .join(' • ');

    const items = assets
      .map(
        (asset) =>
          `<p style="margin-bottom:10px;">
            <strong>${asset.numeroPatrimonio || 'Sem número'}</strong> - ${asset.nome || 'Sem nome'}<br/>
            Categoria: ${asset.categoria || 'Não informada'} ${asset.subcategoria ? ' / ' + asset.subcategoria : ''}<br/>
            Local: ${this.getLocationLabel(asset) || 'Não informado'}<br/>
            Responsável: ${asset.responsavel || 'Não informado'}<br/>
            Valor de aquisição: ${this.formatCurrency(asset.valorAquisicao || 0)}
          </p>`
      )
      .join('');

    const header = `<div style="font-family:Arial,sans-serif; color:#0f172a;">${
      unitTitle ? `<h1 style="font-size:20px; margin-bottom:4px;">${unitTitle}</h1>` : ''
    }<p style="margin-top:0; margin-bottom:12px; font-weight:600;">Relação de bens patrimoniais</p></div>`;

    this.openPrintWindow('Bens patrimoniais', `${header}${items}`);
  }

  private toProperCase(value: string): string {
    return value
      .toLowerCase()
      .split(' ')
      .filter((word) => word.trim().length)
      .map((word) => word[0].toUpperCase() + word.substring(1))
      .join(' ');
  }

  private formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 }).format(value || 0);
  }

  private buildAssetSnapshot(): Patrimonio {
    return {
      idPatrimonio: '',
      numeroPatrimonio: this.assetForm.patrimonyNumber,
      nome: this.assetForm.name,
      categoria: this.assetForm.category,
      subcategoria: this.assetForm.subcategory,
      conservacao: this.assetForm.conservation,
      status: this.assetForm.status,
      dataAquisicao: this.assetForm.acquisitionDate,
      valorAquisicao: this.assetForm.acquisitionValue,
      origem: this.assetForm.origin,
      unidade: this.assetForm.unit,
      sala: this.assetForm.room,
      responsavel: this.assetForm.responsibleName,
      taxaDepreciacao: this.assetForm.depreciationRate,
      observacoes: this.assetForm.observations
    } as Patrimonio;
  }

  private updateCessionTermPreview(): void {
    const asset = this.selectedAsset ?? this.buildAssetSnapshot();
    const responsible = asset.responsavel || this.assetForm.responsibleName || 'Não informado';
    const location = this.getLocationLabel(asset) || 'Local não informado';
    this.cessionTermPreview = `Declaro que o bem ${asset.nome || 'Não identificado'} (patrimônio ${
      asset.numeroPatrimonio || 'não informado'
    }) está sob responsabilidade de ${responsible} no local ${location}.`;

    this.loanTermPreview = `Comprometo-me a devolver em perfeitas condições o bem ${asset.nome || 'Não identificado'} (patrimônio ${
      asset.numeroPatrimonio || 'não informado'
    }) emprestado, mantendo-o em ${location} e comunicando qualquer movimentação.`;
  }

  getLocationLabel(asset: Patrimonio): string {
    return [asset.unidade, asset.sala].filter((value) => (value ?? '').trim().length > 0).join(' - ');
  }

  private openPrintWindow(title: string, content: string): void {
    const win = window.open('', '_blank', 'width=900,height=700');
    if (!win) return;

    win.document.write(`<!doctype html>
      <html>
        <head>
          <title>${title}</title>
          <style>
            body { font-family: Arial, sans-serif; padding: 24px; color: #0f172a; }
            h1 { font-size: 20px; margin-bottom: 16px; }
          </style>
        </head>
        <body>
          <h1>${title}</h1>
          ${content}
        </body>
      </html>`);
    win.document.close();
    win.print();
  }
}
