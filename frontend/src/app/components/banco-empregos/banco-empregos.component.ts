import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { Subject, catchError, debounceTime, distinctUntilChanged, of, takeUntil } from 'rxjs';
import { BeneficiarioApiPayload, BeneficiarioApiService } from '../../services/beneficiario-api.service';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import {
  BancoEmpregosService,
  JobEncaminhamento,
  JobPayload,
  JobRecord,
  JobStatus
} from '../../services/banco-empregos.service';

@Component({
  selector: 'app-banco-empregos',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, TelaPadraoComponent],
  templateUrl: './banco-empregos.component.html',
  styleUrl: './banco-empregos.component.scss'
})
export class BancoEmpregosComponent implements OnInit, OnDestroy {
  readonly tabs = [
    { id: 'dadosVaga', label: 'Dados da Vaga' },
    { id: 'empresaLocal', label: 'Empresa e Local' },
    { id: 'requisitos', label: 'Requisitos e Descrição' },
    { id: 'encaminhamentos', label: 'Encaminhamentos' }
  ] as const;

  activeTab: (typeof this.tabs)[number]['id'] = 'dadosVaga';
  feedback: string | null = null;
  saving = false;
  listLoading = false;
  deletingId: string | null = null;
  form: FormGroup;
  encaminhamentoForm: FormGroup;
  encaminhamentos: JobEncaminhamento[] = [];
  records: JobRecord[] = [];
  filteredRecords: JobRecord[] = [];
  selectedJobId: string | null = null;
  beneficiarios: BeneficiarioApiPayload[] = [];
  beneficiarioPesquisa = new FormControl('');
  beneficiarioSelecionado: BeneficiarioApiPayload | null = null;
  buscandoBeneficiarios = false;
  beneficiarioErro: string | null = null;
  listSearch = new FormControl('');
  statusFilter = new FormControl<'todos' | JobStatus>('todos');
  private readonly destroy$ = new Subject<void>();
  private readonly today = new Date().toISOString().substring(0, 10);

  constructor(
    private readonly fb: FormBuilder,
    private readonly router: Router,
    private readonly service: BancoEmpregosService,
    private readonly beneficiarioService: BeneficiarioApiService
  ) {
    this.form = this.fb.group({
      dadosVaga: this.fb.group({
        titulo: ['', Validators.required],
        status: ['Aberta', Validators.required],
        dataAbertura: ['', Validators.required],
        dataEncerramento: [''],
        tipoContrato: [''],
        cargaHoraria: [''],
        salario: ['']
      }),
      empresaLocal: this.fb.group({
        nomeEmpresa: ['', Validators.required],
        cnpj: [''],
        cidade: ['', Validators.required],
        endereco: [''],
        bairro: ['']
      }),
      requisitos: this.fb.group({
        requisitos: [''],
        descricao: ['', Validators.required],
        observacoes: ['']
      })
    });

    this.encaminhamentoForm = this.fb.group({
      beneficiarioId: ['', Validators.required],
      beneficiarioNome: ['', Validators.required],
      data: [this.today, Validators.required],
      status: ['Aguardando contato', Validators.required],
      observacoes: ['']
    });
  }

  ngOnInit(): void {
    this.loadRecords();
    this.setupBeneficiaryLookup();
    this.setupFilters();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get activeTabIndex(): number {
    return this.tabs.findIndex((tab) => tab.id === this.activeTab);
  }

  changeTab(tabId: (typeof this.tabs)[number]['id']): void {
    this.activeTab = tabId;
  }

  startNew(): void {
    this.form.reset({
      dadosVaga: { status: 'Aberta', dataAbertura: this.today },
      empresaLocal: {},
      requisitos: {}
    });
    this.feedback = null;
    this.activeTab = 'dadosVaga';
    this.selectedJobId = null;
    this.encaminhamentos = [];
    this.encaminhamentoForm.reset({
      beneficiarioId: '',
      beneficiarioNome: '',
      data: this.today,
      status: 'Aguardando contato',
      observacoes: ''
    });
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.feedback = 'Preencha os campos obrigatórios para salvar a vaga.';
      return;
    }

    const payload: JobPayload = {
      ...(this.form.getRawValue() as JobPayload),
      encaminhamentos: this.encaminhamentos
    };

    this.saving = true;
    const request$ = this.selectedJobId
      ? this.service.update(this.selectedJobId, payload)
      : this.service.create(payload);

    request$
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (record: JobRecord) => {
          this.selectedJobId = record.id;
          this.feedback = 'Dados da vaga salvos com sucesso.';
          this.applyFilters();
        },
        error: () => {
          this.feedback = 'Não foi possível salvar a vaga no momento.';
        }
      })
      .add(() => (this.saving = false));
  }

  cancel(): void {
    if (this.selectedJobId) {
      const record = this.records.find((item) => item.id === this.selectedJobId);
      if (record) {
        this.populateForm(record);
        return;
      }
    }
    this.startNew();
  }

  close(): void {
    this.router.navigate(['/atendimentos/cursos']);
  }

  print(): void {
    window.print();
  }

  addEncaminhamento(): void {
    if (this.encaminhamentoForm.invalid) {
      this.encaminhamentoForm.markAllAsTouched();
      return;
    }

    const { beneficiarioId, beneficiarioNome, data, status, observacoes } = this.encaminhamentoForm.getRawValue();

    const novoEncaminhamento: JobEncaminhamento = {
      id: this.generateId(),
      beneficiarioId: beneficiarioId ?? '',
      beneficiarioNome: beneficiarioNome ?? '',
      data: data ?? this.today,
      status: status ?? 'Aguardando contato',
      observacoes: observacoes ?? ''
    };

    this.encaminhamentos = [novoEncaminhamento, ...this.encaminhamentos];
    this.encaminhamentoForm.patchValue({
      data: this.today,
      status: 'Aguardando contato',
      observacoes: ''
    });
  }

  removeEncaminhamento(id: string): void {
    this.encaminhamentos = this.encaminhamentos.filter((item) => item.id !== id);
  }

  selectBeneficiario(beneficiario: BeneficiarioApiPayload): void {
    const id = beneficiario.id_beneficiario ?? beneficiario.codigo ?? beneficiario.cpf ?? '';
    const nome = beneficiario.nome_completo ?? beneficiario.nome_social ?? 'Beneficiário sem nome';
    this.beneficiarioSelecionado = beneficiario;
    this.encaminhamentoForm.patchValue({ beneficiarioId: id, beneficiarioNome: nome });
  }

  editRecord(record: JobRecord): void {
    this.populateForm(record);
    this.activeTab = 'dadosVaga';
  }

  deleteRecord(record: JobRecord): void {
    this.deletingId = record.id;
    this.service
      .remove(record.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          if (this.selectedJobId === record.id) {
            this.startNew();
          }
        },
        error: () => {
          this.feedback = 'Não foi possível remover a vaga.';
        }
      })
      .add(() => (this.deletingId = null));
  }

  private populateForm(record: JobRecord): void {
    this.selectedJobId = record.id;
    this.encaminhamentos = [...(record.encaminhamentos ?? [])];
    this.form.patchValue({
      dadosVaga: record.dadosVaga,
      empresaLocal: record.empresaLocal,
      requisitos: record.requisitos
    });
  }

  private loadRecords(): void {
    this.listLoading = true;
    this.service
      .list()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (records) => {
          this.records = records;
          this.applyFilters();
        },
        error: () => {
          this.feedback = 'Não foi possível carregar a listagem de vagas.';
        }
      })
      .add(() => (this.listLoading = false));
  }

  private setupFilters(): void {
    this.listSearch.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(() => this.applyFilters());
    this.statusFilter.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(() => this.applyFilters());
  }

  private applyFilters(): void {
    const term = (this.listSearch.value ?? '').toString().toLowerCase().trim();
    const status = this.statusFilter.value ?? 'todos';

    this.filteredRecords = this.records.filter((record) => {
      const matchesTerm = term
        ? [
            record.dadosVaga?.titulo,
            record.empresaLocal?.nomeEmpresa,
            record.empresaLocal?.cidade,
            record.requisitos?.descricao
          ]
            .filter(Boolean)
            .some((field) => field!.toString().toLowerCase().includes(term))
        : true;

      const matchesStatus = status === 'todos' ? true : record.dadosVaga?.status === status;
      return matchesTerm && matchesStatus;
    });
  }

  private setupBeneficiaryLookup(): void {
    this.beneficiarioPesquisa.valueChanges
      .pipe(debounceTime(300), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe((value) => this.buscarBeneficiarios(value ?? ''));
  }

  private buscarBeneficiarios(term: string): void {
    const query = term.trim();
    this.beneficiarioErro = null;

    if (!query) {
      this.beneficiarios = [];
      this.beneficiarioSelecionado = null;
      return;
    }

    this.buscandoBeneficiarios = true;
    this.beneficiarioService
      .list({ nome: query })
      .pipe(
        catchError(() => {
          this.beneficiarioErro = 'Não foi possível buscar beneficiários no momento.';
          return of({ beneficiarios: [] });
        }),
        takeUntil(this.destroy$)
      )
      .subscribe((response: { beneficiarios?: BeneficiarioApiPayload[] }) => {
        this.beneficiarios = response.beneficiarios ?? [];
        this.buscandoBeneficiarios = false;
      });
  }

  private generateId(): string {
    if (typeof crypto !== 'undefined' && 'randomUUID' in crypto) {
      return crypto.randomUUID();
    }

    return `enc-${Date.now()}-${Math.random().toString(16).slice(2, 8)}`;
  }
}
