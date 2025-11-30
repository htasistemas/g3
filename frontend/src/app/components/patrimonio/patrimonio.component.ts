import { Component } from '@angular/core';
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

interface AssetRecord {
  patrimonyNumber: string;
  name: string;
  category: string;
  subcategory?: string;
  conservation: string;
  status: string;
  acquisitionDate: string;
  acquisitionValue: number;
  origin: string;
  responsible: string;
  unit: string;
  depreciationRate: number;
}

@Component({
  selector: 'app-patrimonio',
  standalone: true,
  imports: [CommonModule, FormsModule, FontAwesomeModule],
  templateUrl: './patrimonio.component.html',
  styleUrl: './patrimonio.component.scss'
})
export class PatrimonioComponent {
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

  assetLibrary: AssetRecord[] = [
    {
      patrimonyNumber: 'PAT-2024-001',
      name: 'Notebook Dell Latitude',
      category: 'Informática',
      subcategory: 'Notebook',
      conservation: 'Bom',
      status: 'Ativo',
      acquisitionDate: '2024-01-12',
      acquisitionValue: 5200,
      origin: 'Compra',
      responsible: 'Marina Costa',
      unit: 'TI',
      depreciationRate: 2
    },
    {
      patrimonyNumber: 'PAT-2023-143',
      name: 'Mesa de Reunião',
      category: 'Móveis',
      subcategory: 'Mesa',
      conservation: 'Regular',
      status: 'Em manutenção',
      acquisitionDate: '2023-08-05',
      acquisitionValue: 1250,
      origin: 'Transferência',
      responsible: 'Carlos Lima',
      unit: 'Administração',
      depreciationRate: 1.2
    }
  ];

  filePreview: string | ArrayBuffer | null = null;
  qrCodeValue = 'QR-PATRIMONIO-001';
  searchTerm = '';

  get filteredAssets(): AssetRecord[] {
    const term = this.searchTerm.toLowerCase();
    return this.assetLibrary.filter(
      (asset) =>
        asset.patrimonyNumber.toLowerCase().includes(term) ||
        asset.name.toLowerCase().includes(term) ||
        asset.responsible.toLowerCase().includes(term)
    );
  }

  get totalAcquisitionValue(): number {
    return this.assetLibrary.reduce((sum, asset) => sum + asset.acquisitionValue, 0);
  }

  get monthlyDepreciation(): number {
    const totalRate = this.assetLibrary.reduce((sum, asset) => sum + asset.depreciationRate, 0);
    return this.assetLibrary.length ? totalRate / this.assetLibrary.length : 0;
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
    const newAsset: AssetRecord = {
      patrimonyNumber: this.assetForm.patrimonyNumber || `PAT-${Date.now()}`,
      name: this.assetForm.name,
      category: this.assetForm.category,
      subcategory: this.assetForm.subcategory,
      conservation: this.assetForm.conservation,
      status: this.assetForm.status,
      acquisitionDate: this.assetForm.acquisitionDate,
      acquisitionValue: this.assetForm.acquisitionValue,
      origin: this.assetForm.origin,
      responsible: this.assetForm.responsibleName,
      unit: this.assetForm.unit,
      depreciationRate: this.assetForm.depreciationRate
    };

    this.assetLibrary = [newAsset, ...this.assetLibrary];
    this.resetForm();
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
}
