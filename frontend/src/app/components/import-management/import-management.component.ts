import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import {
  faBoxOpen,
  faCheckCircle,
  faCircleNotch,
  faCloudArrowUp,
  faClipboardList,
  faDatabase,
  faFileArrowDown,
  faFileImport,
  faPlug,
  faUserPlus,
  faWarehouse
} from '@fortawesome/free-solid-svg-icons';

type ImportEntity = 'beneficiarios' | 'patrimonio' | 'almoxarifado';

interface ImportField {
  key: string;
  label: string;
  description?: string;
}

interface ParsedFile {
  headers: string[];
  rows: Record<string, string>[];
}

interface TargetConfig {
  id: ImportEntity;
  label: string;
  description: string;
  fields: ImportField[];
}

const TARGET_CONFIGS: TargetConfig[] = [
  {
    id: 'beneficiarios',
    label: 'Cadastro de beneficiário',
    description: 'Dados essenciais para pré-preencher a ficha de beneficiário.',
    fields: [
      { key: 'nome_completo', label: 'Nome completo', description: 'Nome civil ou social' },
      { key: 'cpf', label: 'CPF' },
      { key: 'data_nascimento', label: 'Data de nascimento' },
      { key: 'telefone_principal', label: 'Telefone principal' },
      { key: 'email', label: 'E-mail' },
      { key: 'cep', label: 'CEP' },
      { key: 'logradouro', label: 'Logradouro' },
      { key: 'bairro', label: 'Bairro' },
      { key: 'municipio', label: 'Município' }
    ]
  },
  {
    id: 'patrimonio',
    label: 'Cadastro de patrimônio',
    description: 'Informações mínimas para registrar bens e tombamento.',
    fields: [
      { key: 'numero_tombo', label: 'Nº tombo' },
      { key: 'descricao', label: 'Descrição do bem' },
      { key: 'categoria', label: 'Categoria' },
      { key: 'setor', label: 'Setor / local' },
      { key: 'responsavel', label: 'Responsável' },
      { key: 'data_aquisicao', label: 'Data de aquisição' },
      { key: 'valor', label: 'Valor (R$)' },
      { key: 'estado_conservacao', label: 'Estado de conservação' }
    ]
  },
  {
    id: 'almoxarifado',
    label: 'Cadastro de almoxarifado',
    description: 'Itens de estoque com quantidade, lote e localização.',
    fields: [
      { key: 'codigo_item', label: 'Código do item' },
      { key: 'descricao', label: 'Descrição do item' },
      { key: 'quantidade', label: 'Quantidade' },
      { key: 'unidade', label: 'Unidade de medida' },
      { key: 'localizacao', label: 'Endereço / localização' },
      { key: 'lote', label: 'Lote / série' },
      { key: 'validade', label: 'Validade' },
      { key: 'fornecedor', label: 'Fornecedor' }
    ]
  }
];

const FIELD_SYNONYMS: Record<ImportEntity, Record<string, string[]>> = {
  beneficiarios: {
    nome_completo: ['nome', 'beneficiario', 'nome completo'],
    cpf: ['cpf', 'documento', 'numero_cpf'],
    data_nascimento: ['data_nascimento', 'nascimento', 'dt_nasc', 'data nasc'],
    telefone_principal: ['telefone', 'celular', 'telefone1'],
    email: ['email', 'e-mail'],
    cep: ['cep', 'codigo_postal'],
    logradouro: ['endereco', 'rua', 'logradouro'],
    bairro: ['bairro'],
    municipio: ['municipio', 'cidade']
  },
  patrimonio: {
    numero_tombo: ['tombo', 'n_tombo', 'codigo_bem'],
    descricao: ['descricao', 'bem', 'item'],
    categoria: ['categoria', 'classe', 'grupo'],
    setor: ['setor', 'local', 'sala'],
    responsavel: ['responsavel', 'guardiao'],
    data_aquisicao: ['data_aquisicao', 'aquisicao', 'dt_aquisicao'],
    valor: ['valor', 'valor_aquisicao', 'custo'],
    estado_conservacao: ['estado', 'conservacao', 'status']
  },
  almoxarifado: {
    codigo_item: ['codigo', 'item', 'sku'],
    descricao: ['descricao', 'produto', 'material'],
    quantidade: ['quantidade', 'qtd', 'qtde'],
    unidade: ['unidade', 'un', 'unidade_medida'],
    localizacao: ['localizacao', 'estoque', 'prateleira', 'rua'],
    lote: ['lote', 'serie'],
    validade: ['validade', 'vencimento'],
    fornecedor: ['fornecedor', 'origem']
  }
};

const SAMPLE_DATA: Record<ImportEntity, Record<string, string>[]> = {
  beneficiarios: [
    {
      nome_completo: 'Marina Alves',
      cpf: '123.456.789-10',
      data_nascimento: '1991-07-11',
      telefone: '(11) 98877-6655',
      email: 'marina.alves@email.com',
      cep: '04696-000',
      logradouro: 'Rua das Mangueiras, 200',
      bairro: 'Jardim do Sol',
      municipio: 'São Paulo'
    },
    {
      nome_completo: 'Paulo Henrique',
      cpf: '987.654.321-00',
      data_nascimento: '1985-03-04',
      telefone: '(11) 97788-4411',
      email: 'paulo.henrique@email.com',
      cep: '07090-120',
      logradouro: 'Av. Central, 950',
      bairro: 'Centro',
      municipio: 'Guarulhos'
    }
  ],
  patrimonio: [
    {
      numero_tombo: 'P-001',
      descricao: 'Notebook Dell Latitude',
      categoria: 'Informática',
      setor: 'Tecnologia',
      responsavel: 'Equipe de TI',
      data_aquisicao: '2023-02-10',
      valor: '5800,00',
      estado_conservacao: 'Em uso'
    },
    {
      numero_tombo: 'P-002',
      descricao: 'Projeto Elétrico Prédio A',
      categoria: 'Infraestrutura',
      setor: 'Engenharia',
      responsavel: 'Carlos Souza',
      data_aquisicao: '2021-09-22',
      valor: '12000,00',
      estado_conservacao: 'Arquivado'
    }
  ],
  almoxarifado: [
    {
      codigo_item: 'ALM-1001',
      descricao: 'Máscara descartável',
      quantidade: '500',
      unidade: 'cx',
      localizacao: 'Sala 2 - Prateleira A',
      lote: 'L2024-01',
      validade: '2025-01-31',
      fornecedor: 'Saúde Total'
    },
    {
      codigo_item: 'ALM-1002',
      descricao: 'Luvas de látex',
      quantidade: '200',
      unidade: 'caixa',
      localizacao: 'Sala 2 - Prateleira B',
      lote: 'L2024-05',
      validade: '2025-05-30',
      fornecedor: 'MediPlus'
    }
  ]
};

@Component({
  selector: 'app-import-management',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FontAwesomeModule],
  templateUrl: './import-management.component.html',
  styleUrl: './import-management.component.scss'
})
export class ImportManagementComponent {
  readonly faFileImport = faFileImport;
  readonly faCloudArrowUp = faCloudArrowUp;
  readonly faUserPlus = faUserPlus;
  readonly faClipboardList = faClipboardList;
  readonly faWarehouse = faWarehouse;
  readonly faBoxOpen = faBoxOpen;
  readonly faDatabase = faDatabase;
  readonly faCircleNotch = faCircleNotch;
  readonly faCheckCircle = faCheckCircle;
  readonly faFileArrowDown = faFileArrowDown;
  readonly faPlug = faPlug;

  importForm: FormGroup;
  mappingForm?: FormGroup;
  parsedHeaders: string[] = [];
  parsedRows: Record<string, string>[] = [];
  mappedPreview: Record<string, string>[] = [];
  selectedFileName: string | null = null;
  importFeedback: string | null = null;
  lastAppliedEntity: ImportEntity | null = null;

  constructor(private readonly fb: FormBuilder) {
    this.importForm = this.fb.group({
      entity: new FormControl<ImportEntity>('beneficiarios', { nonNullable: true, validators: Validators.required }),
      applyUppercase: [false]
    });

    this.importForm.get('entity')?.valueChanges.subscribe(() => {
      this.resetImport(false);
    });
  }

  get targets(): TargetConfig[] {
    return TARGET_CONFIGS;
  }

  get activeTarget(): TargetConfig {
    const entity = this.importForm.get('entity')?.value as ImportEntity;
    return this.targets.find((target) => target.id === entity) ?? this.targets[0];
  }

  get hasParsedData(): boolean {
    return this.parsedRows.length > 0;
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    if (!file) {
      return;
    }

    this.selectedFileName = file.name;
    const reader = new FileReader();
    reader.onload = () => {
      const text = reader.result as string;
      this.ingestRawFile(text);
    };
    reader.readAsText(file);
  }

  useSampleData(): void {
    const entity = this.importForm.get('entity')?.value as ImportEntity;
    const sampleRows = SAMPLE_DATA[entity] ?? [];
    const headers = Object.keys(sampleRows[0] ?? {});

    this.parsedHeaders = headers;
    this.parsedRows = sampleRows.map((row) => ({ ...row }));
    this.selectedFileName = `amostra-${entity}.csv`;
    this.buildMappingForm(headers);
    this.generatePreview();
    this.importFeedback = 'Amostra carregada. Ajuste o mapeamento se necessário e aplique a importação.';
  }

  applyImport(): void {
    if (!this.mappingForm || !this.hasParsedData) {
      this.importFeedback = 'Selecione um arquivo ou carregue uma amostra para mapear os campos.';
      return;
    }

    const mapped = this.mappingForm.value as Record<string, string>;
    const entity = this.importForm.get('entity')?.value as ImportEntity;

    this.mappedPreview = this.parsedRows.slice(0, 5).map((row) => this.projectRow(row, mapped));
    this.lastAppliedEntity = entity;
    this.importFeedback =
      'Importação preparada. Os campos disponíveis serão preenchidos e os não mapeados permanecerão em branco.';
  }

  buildMappingForm(headers: string[]): void {
    const controls: Record<string, FormControl<string>> = {};
    const entity = this.importForm.get('entity')?.value as ImportEntity;

    this.activeTarget.fields.forEach((field) => {
      controls[field.key] = new FormControl<string>(this.suggestColumn(field, headers, entity) ?? '', {
        nonNullable: true
      });
    });

    this.mappingForm = this.fb.group(controls);
    this.mappingForm.valueChanges.subscribe(() => this.generatePreview());
  }

  ingestRawFile(text: string): void {
    const parsed = this.parseDelimitedFile(text);
    this.parsedHeaders = parsed.headers;
    this.parsedRows = parsed.rows;
    this.buildMappingForm(parsed.headers);
    this.generatePreview();
    this.importFeedback = 'Arquivo lido com sucesso. Confirme o mapeamento antes de aplicar a importação.';
  }

  private parseDelimitedFile(content: string): ParsedFile {
    const trimmed = content.trim();
    if (!trimmed) {
      return { headers: [], rows: [] };
    }

    const lines = trimmed.split(/\r?\n/).filter(Boolean);
    if (lines.length === 0) {
      return { headers: [], rows: [] };
    }

    const delimiter = this.detectDelimiter(lines[0]);
    const headers = lines[0].split(delimiter).map((h) => h.trim());
    const rows = lines.slice(1).map((line) => {
      const cells = line.split(delimiter).map((cell) => cell.trim());
      const record: Record<string, string> = {};
      headers.forEach((header, index) => {
        record[header] = cells[index] ?? '';
      });
      return record;
    });

    return { headers, rows };
  }

  private detectDelimiter(line: string): string {
    const semicolons = line.split(';').length;
    const commas = line.split(',').length;
    return semicolons > commas ? ';' : ',';
  }

  private normalizeKey(value: string): string {
    return value
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .toLowerCase()
      .replace(/[^a-z0-9]+/g, '_');
  }

  private suggestColumn(field: ImportField, headers: string[], entity: ImportEntity): string {
    const normalizedHeaders = headers.map((h) => ({
      raw: h,
      normalized: this.normalizeKey(h)
    }));

    const synonyms = [
      this.normalizeKey(field.key),
      ...((FIELD_SYNONYMS[entity]?.[field.key] ?? []).map((value) => this.normalizeKey(value)))
    ];

    const match = normalizedHeaders.find((header) => synonyms.some((syn) => header.normalized.includes(syn)));
    return match?.raw ?? '';
  }

  private projectRow(row: Record<string, string>, mapping: Record<string, string>): Record<string, string> {
    const applyUppercase = this.importForm.get('applyUppercase')?.value;
    const projected: Record<string, string> = {};

    this.activeTarget.fields.forEach((field) => {
      const sourceKey = mapping[field.key];
      const value = sourceKey ? row[sourceKey] ?? '' : '';
      projected[field.key] = value && applyUppercase ? value.toUpperCase() : value;
    });

    return projected;
  }

  private generatePreview(): void {
    if (!this.mappingForm || !this.parsedRows.length) {
      this.mappedPreview = [];
      return;
    }

    const mapped = this.mappingForm.value as Record<string, string>;
    this.mappedPreview = this.parsedRows.slice(0, 3).map((row) => this.projectRow(row, mapped));
  }

  private resetImport(clearFile = true): void {
    if (clearFile) {
      this.selectedFileName = null;
    }
    this.mappingForm = undefined;
    this.parsedHeaders = [];
    this.parsedRows = [];
    this.mappedPreview = [];
    this.importFeedback = null;
    this.lastAppliedEntity = null;
  }
}
