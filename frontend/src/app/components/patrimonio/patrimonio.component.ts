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
  movementForm = {
    tipo: 'MOVIMENTACAO' as 'MOVIMENTACAO' | 'MANUTENCAO' | 'BAIXA',
    destino: '',
    responsavel: '',
    observacao: ''
  };
  selectedAsset: Patrimonio | null = null;
  isSaving = false;
  isLoading = false;
  errorMessage: string | null = null;

  filePreview: string | ArrayBuffer | null = null;
  qrCodeValue = 'QR-PATRIMONIO-001';
  searchTerm = '';
  displayedAcquisitionValue = '';
  cessionTermPreview = '';
  printOrder: 'alphabetical' | 'patrimony' | 'value' | 'location' = 'alphabetical';

  constructor(private readonly patrimonioService: PatrimonioService) {}

  ngOnInit(): void {
    this.loadAssets();
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
      taxaDepreciacao: this.assetForm.depreciationRate,
      observacoes: this.assetForm.observations
    };

    this.patrimonioService
      .create(payload)
      .pipe(
        timeout(10000),
        finalize(() => {
          this.isSaving = false;
        })
      )
      .subscribe({
        next: (patrimonio) => {
          this.assetLibrary = [patrimonio, ...this.assetLibrary];
          this.selectedAsset = patrimonio;
          this.resetForm();
          this.updateCessionTermPreview();
        },
        error: (error) => {
          this.errorMessage = error?.error?.message ?? 'Não foi possível salvar o patrimônio. Tente novamente.';
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
  }

  setSelected(asset: Patrimonio): void {
    this.selectedAsset = asset;
    this.updateCessionTermPreview();
  }

  quickMovement(asset: Patrimonio, tipo: 'MOVIMENTACAO' | 'MANUTENCAO' | 'BAIXA'): void {
    this.movementForm = { tipo, destino: asset.unidade ?? '', responsavel: asset.responsavel ?? '', observacao: '' };
    this.selectedAsset = asset;
    this.registerMovement();
  }

  registerMovement(): void {
    if (!this.selectedAsset) return;

    this.patrimonioService
      .registerMovement(this.selectedAsset.idPatrimonio, {
        tipo: this.movementForm.tipo,
        destino: this.movementForm.destino,
        responsavel: this.movementForm.responsavel,
        observacao: this.movementForm.observacao
      })
      .subscribe({
        next: (patrimonio) => {
          this.assetLibrary = this.assetLibrary.map((asset) =>
            asset.idPatrimonio === patrimonio.idPatrimonio ? patrimonio : asset
          );
          this.selectedAsset = patrimonio;
          this.movementForm.observacao = '';
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

  generateCessionTerm(): void {
    const asset = this.selectedAsset ?? this.buildAssetSnapshot();
    const content = `Termo de cessão do bem ${asset.nome || 'Não identificado'} (patrimônio ${
      asset.numeroPatrimonio || 'não informado'
    }) localizado na unidade ${asset.unidade || 'Não informada'}, sob responsabilidade de ${
      asset.responsavel || this.assetForm.responsibleName || 'Não informado'
    }. Valor de aquisição: ${this.formatCurrency(asset.valorAquisicao || this.assetForm.acquisitionValue)}.`;

    this.cessionTermPreview = content;
    this.openPrintWindow('Termo de cessão', `<p style="font-size:14px; line-height:1.6">${content}</p>`);
  }

  printAssets(): void {
    const assets = [...this.assetLibrary].sort((a, b) => {
      switch (this.printOrder) {
        case 'patrimony':
          return (a.numeroPatrimonio || '').localeCompare(b.numeroPatrimonio || '');
        case 'value':
          return (Number(b.valorAquisicao) || 0) - (Number(a.valorAquisicao) || 0);
        case 'location':
          return (a.unidade || '').localeCompare(b.unidade || '');
        default:
          return (a.nome || '').localeCompare(b.nome || '');
      }
    });

    const rows = assets
      .map(
        (asset) =>
          `<tr>
            <td style="padding:8px; border:1px solid #e5e7eb;">${asset.numeroPatrimonio}</td>
            <td style="padding:8px; border:1px solid #e5e7eb;">${asset.nome}</td>
            <td style="padding:8px; border:1px solid #e5e7eb;">${asset.categoria || ''} ${
            asset.subcategoria ? ' / ' + asset.subcategoria : ''
          }</td>
            <td style="padding:8px; border:1px solid #e5e7eb;">${asset.unidade || ''}</td>
            <td style="padding:8px; border:1px solid #e5e7eb;">${this.formatCurrency(asset.valorAquisicao || 0)}</td>
          </tr>`
      )
      .join('');

    const table = `<table style="border-collapse:collapse; width:100%; font-family:Arial, sans-serif; font-size:12px;">
      <thead>
        <tr style="background:#f8fafc;">
          <th style="padding:8px; border:1px solid #e5e7eb; text-align:left;">Número</th>
          <th style="padding:8px; border:1px solid #e5e7eb; text-align:left;">Nome</th>
          <th style="padding:8px; border:1px solid #e5e7eb; text-align:left;">Categoria</th>
          <th style="padding:8px; border:1px solid #e5e7eb; text-align:left;">Local</th>
          <th style="padding:8px; border:1px solid #e5e7eb; text-align:left;">Valor</th>
        </tr>
      </thead>
      <tbody>${rows}</tbody>
    </table>`;

    this.openPrintWindow('Bens patrimoniais', table);
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
      responsavel: this.assetForm.responsibleName,
      taxaDepreciacao: this.assetForm.depreciationRate,
      observacoes: this.assetForm.observations
    } as Patrimonio;
  }

  private updateCessionTermPreview(): void {
    const asset = this.selectedAsset ?? this.buildAssetSnapshot();
    this.cessionTermPreview = `Declaro que o bem ${asset.nome || 'Não identificado'} (patrimônio ${
      asset.numeroPatrimonio || 'não informado'
    }) está sob responsabilidade de ${asset.responsavel || this.assetForm.responsibleName || 'Não informado'}.`;
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
