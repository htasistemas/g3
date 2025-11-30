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

  assetForm = {
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
  movementForm = { tipo: 'MOVIMENTACAO' as 'MOVIMENTACAO' | 'MANUTENCAO' | 'BAIXA', destino: '', responsavel: '', observacao: '' };
  selectedAsset: Patrimonio | null = null;
  isSaving = false;
  isLoading = false;

  filePreview: string | ArrayBuffer | null = null;
  qrCodeValue = 'QR-PATRIMONIO-001';
  searchTerm = '';

  constructor(private readonly patrimonioService: PatrimonioService) {}

  ngOnInit(): void {
    this.loadAssets();
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

  saveAsset(): void {
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

    this.patrimonioService.create(payload).subscribe({
      next: (patrimonio) => {
        this.assetLibrary = [patrimonio, ...this.assetLibrary];
        this.selectedAsset = patrimonio;
        this.resetForm();
        this.isSaving = false;
      },
      error: () => {
        this.isSaving = false;
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
  }

  setSelected(asset: Patrimonio): void {
    this.selectedAsset = asset;
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
}
