import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Subject, takeUntil } from 'rxjs';
import {
  ProfessionalPayload,
  ProfessionalRecord,
  ProfessionalService,
  ProfessionalStatus
} from '../../services/professional.service';
import { titleCaseWords } from '../../utils/capitalization.util';

import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { ConfigAcoesCrud, EstadoAcoesCrud, TelaBaseComponent } from '../compartilhado/tela-base.component';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';
import { PopupErrorBuilder } from '../../utils/popup-error.builder';
import { AssistanceUnitPayload, AssistanceUnitService } from '../../services/assistance-unit.service';
import { VoluntariadoTermoPrintService } from '../../shared/print/voluntariado-termo-print.service';
interface StepTab {
  id: string;
  label: string;
}

@Component({
  selector: 'app-profissionais-cadastro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TelaPadraoComponent, PopupMessagesComponent],
  templateUrl: './profissionais-cadastro.component.html',
  styleUrl: './profissionais-cadastro.component.scss'
})
export class ProfissionaisCadastroComponent extends TelaBaseComponent implements OnInit, OnDestroy {
  form: FormGroup;
  feedback: string | null = null;
  popupErros: string[] = [];
  professionals: ProfessionalRecord[] = [];
  editingId: string | null = null;
  saving = false;
  unidadeAtual: AssistanceUnitPayload | null = null;
  private readonly capitalizationSubs: Array<() => void> = [];
  private readonly destroy$ = new Subject<void>();

  tabs: StepTab[] = [
    { id: 'perfil', label: 'Perfil profissional' },
    { id: 'agenda', label: 'Agenda e canais' },
    { id: 'resumo', label: 'Resumo e observacoes' }
  ];
  activeTab: StepTab['id'] = 'perfil';

  categorias = ['Assistente social', 'Psicologo(a)', 'Pedagogo(a)', 'Medico(a)', 'Nutricionista'];
  readonly disponibilidades = ['Manha', 'Tarde', 'Noite'];
  readonly canais = ['Presencial', 'Online', 'Telefone'];
  readonly statuses: ProfessionalStatus[] = ['Disponivel', 'Em atendimento', 'Em intervalo', 'Indisponivel'];
  readonly tagsSugeridas = ['Acolhimento', 'Triagem', 'Familias', 'Juventude', 'Visitas', 'Oficinas'];

  constructor(
    private readonly fb: FormBuilder,
    private readonly professionalService: ProfessionalService,
    private readonly assistanceUnitService: AssistanceUnitService,
    private readonly termoPrintService: VoluntariadoTermoPrintService
  ) {
    super();
    this.form = this.fb.group({
      nome: ['', Validators.required],
      categoria: [this.categorias[0], Validators.required],
      registroConselho: [''],
      especialidade: [''],
      email: ['', [Validators.required, Validators.email]],
      telefone: [''],
      unidade: [''],
      cargaHoraria: [20, [Validators.min(1)]],
      disponibilidade: this.fb.control<string[]>([]),
      canaisAtendimento: this.fb.control<string[]>(['Presencial']),
      status: ['Disponivel', Validators.required],
      tags: this.fb.control<string[]>([]),
      resumo: [''],
      observacoes: ['']
    });

    this.loadProfessionals();
    this.setupCapitalizationRules();
  }

  ngOnInit(): void {
    this.assistanceUnitService.get().pipe(takeUntil(this.destroy$)).subscribe({
      next: ({ unidade }) => {
        this.unidadeAtual = unidade ?? null;
      },
      error: () => {
        this.unidadeAtual = null;
      }
    });
  }

  ngOnDestroy(): void {
    this.capitalizationSubs.forEach((unsubscribe) => unsubscribe());
    this.destroy$.next();
    this.destroy$.complete();
  }

  get activeTabIndex(): number {
    return this.tabs.findIndex((tab) => tab.id === this.activeTab);
  }

  get hasPreviousTab(): boolean {
    return this.activeTabIndex > 0;
  }

  get hasNextTab(): boolean {
    return this.activeTabIndex < this.tabs.length - 1;
  }

  get nextTabLabel(): string {
    return this.hasNextTab ? this.tabs[this.activeTabIndex + 1].label : '';
  }

  readonly acoesToolbar: Required<ConfigAcoesCrud> = this.criarConfigAcoes({
    salvar: true,
    excluir: true,
    novo: true,
    cancelar: true,
    imprimir: true
  });

  get acoesDesabilitadas(): EstadoAcoesCrud {
    return {
      salvar: this.saving,
      excluir: !this.editingId,
      novo: this.saving,
      cancelar: this.saving,
      imprimir: this.saving
    };
  }

  fecharPopupErros(): void {
    this.popupErros = [];
  }

  changeTab(tab: StepTab['id']): void {
    this.activeTab = tab;
  }

  goToNextTab(): void {
    if (this.hasNextTab) {
      this.changeTab(this.tabs[this.activeTabIndex + 1].id);
    }
  }

  goToPreviousTab(): void {
    if (this.hasPreviousTab) {
      this.changeTab(this.tabs[this.activeTabIndex - 1].id);
    }
  }

  get totalDisponiveis(): number {
    return this.professionals.filter((p) => p.status === 'Disponivel').length;
  }

  get totalOnline(): number {
    return this.professionals.filter((p) => p.canaisAtendimento?.includes('Online')).length;
  }

  get totalPresencial(): number {
    return this.professionals.filter((p) => p.canaisAtendimento?.includes('Presencial')).length;
  }

  submit(): void {
    this.feedback = null;
    this.popupErros = [];
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      const builder = new PopupErrorBuilder();
      const requiredFields: { path: string; label: string }[] = [
        { path: 'nome', label: 'Nome completo' },
        { path: 'categoria', label: 'Categoria' },
        { path: 'email', label: 'E-mail' },
        { path: 'status', label: 'Status' }
      ];

      requiredFields.forEach(({ path, label }) => {
        const control = this.form.get(path);
        const value = String(control?.value ?? '').trim();
        if (!value) {
          builder.adicionar(`${label} e obrigatorio.`);
        }
      });
      this.popupErros = builder.build();
      this.feedback = 'Preencha os campos obrigatorios para salvar o profissional.';
      return;
    }

    this.saving = true;
    const payload = this.form.value as ProfessionalPayload;
    const request = this.editingId
      ? this.professionalService.update(this.editingId, payload)
      : this.professionalService.create(payload);

    request.pipe(takeUntil(this.destroy$)).subscribe({
      next: (record) => {
        if (this.editingId) {
          this.professionals = this.professionals.map((item) => (item.id === record.id ? record : item));
          this.feedback = 'Profissional atualizado com sucesso.';
        } else {
          this.professionals = [record, ...this.professionals];
          this.feedback = 'Profissional cadastrado com sucesso.';
        }
        this.saving = false;
        this.resetForm();
        this.changeTab('perfil');
      },
      error: () => {
        this.feedback = 'Nao foi possivel salvar o profissional. Tente novamente.';
        this.saving = false;
      }
    });
  }

  addCategoria(value: string): void {
    const formatted = titleCaseWords(value.trim());
    if (!formatted) return;

    if (this.categorias.includes(formatted)) {
      this.form.patchValue({ categoria: formatted });
      return;
    }

    this.categorias = [...this.categorias, formatted];
    this.form.patchValue({ categoria: formatted });
  }

  editCategoria(actual: string): void {
    const next = window.prompt('Atualizar categoria', actual);
    const formatted = titleCaseWords((next ?? '').trim());
    if (!formatted || actual === formatted) return;

    this.categorias = this.categorias.map((item) => (item === actual ? formatted : item));

    if (this.form.value.categoria === actual) {
      this.form.patchValue({ categoria: formatted });
    }
  }

  removeCategoria(target: string): void {
    if (this.categorias.length === 1) return;
    if (!window.confirm(`Remover a categoria "${target}"?`)) return;

    this.categorias = this.categorias.filter((categoria) => categoria !== target);

    if (this.form.value.categoria === target) {
      this.form.patchValue({ categoria: this.categorias[0] ?? '' });
    }
  }

  startNew(): void {
    this.editingId = null;
    this.changeTab('perfil');
    this.resetForm();
  }

  edit(record: ProfessionalRecord): void {
    this.editingId = record.id;
    this.changeTab('perfil');
    this.form.patchValue({
      nome: record.nome,
      categoria: record.categoria,
      registroConselho: record.registroConselho,
      especialidade: record.especialidade,
      email: record.email,
      telefone: record.telefone,
      unidade: record.unidade,
      cargaHoraria: record.cargaHoraria,
      disponibilidade: record.disponibilidade ?? [],
      canaisAtendimento: record.canaisAtendimento ?? [],
      status: record.status,
      tags: record.tags ?? [],
      resumo: record.resumo,
      observacoes: record.observacoes
    });
  }

  remove(record: ProfessionalRecord): void {
    if (!window.confirm(`Remover ${record.nome} do cadastro?`)) return;
    this.professionalService
      .delete(record.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.professionals = this.professionals.filter((item) => item.id !== record.id);
          this.resetForm();
          this.changeTab('perfil');
        },
        error: () => {
          this.feedback = 'Nao foi possivel remover o profissional. Tente novamente.';
        }
      });
  }

  toggleSelection(path: (string | number)[], option: string): void {
    const control = this.form.get(path);
    const current = new Set(control?.value ?? []);
    current.has(option) ? current.delete(option) : current.add(option);
    control?.setValue(Array.from(current));
  }

  selectionChecked(path: (string | number)[], option: string): boolean {
    const control = this.form.get(path);
    return (control?.value as string[] | undefined)?.includes(option) ?? false;
  }

  addTag(tag: string): void {
    if (!tag.trim()) return;
    const control = this.form.get('tags');
    const formatted = titleCaseWords(tag.trim());
    const current = new Set(control?.value ?? []);
    current.add(formatted || tag.trim());
    control?.setValue(Array.from(current));
  }

  removeTag(tag: string): void {
    const control = this.form.get('tags');
    const current = new Set(control?.value ?? []);
    current.delete(tag);
    control?.setValue(Array.from(current));
  }

  resetForm(): void {
    this.editingId = null;
    this.form.reset({
      nome: '',
      categoria: this.categorias[0],
      registroConselho: '',
      especialidade: '',
      email: '',
      telefone: '',
      unidade: '',
      cargaHoraria: 20,
      disponibilidade: [],
      canaisAtendimento: ['Presencial'],
      status: 'Disponivel',
      tags: [],
      resumo: '',
      observacoes: ''
    });
  }

  printProfessional(): void {
    const record = this.editingId ? this.professionals.find((item) => item.id === this.editingId) : null;
    const data = record ?? (this.form.value as ProfessionalPayload);

    const disponibilidade = (data.disponibilidade ?? []).join(' | ') || 'Nao informado';
    const canais = (data.canaisAtendimento ?? []).join(' | ') || 'Nao informado';
    const tags = (data.tags ?? []).join(', ') || 'Sem tags definidas';

    const printWindow = window.open('', '_blank', 'width=900,height=700');
    if (!printWindow) return;

    printWindow.document.write(`
      <html>
        <head>
          <title>Dados do profissional</title>
          <style>
            @page { size: A4; margin: 12mm; }
            body { font-family: 'Inter', 'Segoe UI', system-ui, -apple-system, sans-serif; color: #0f172a; }
            h1 { font-size: 1.4rem; margin-bottom: 0.25rem; }
            p { margin: 0.1rem 0 0.35rem; color: #475569; }
            dl { display: grid; grid-template-columns: 180px 1fr; gap: 0.25rem 0.75rem; margin: 1rem 0; }
            dt { font-weight: 700; color: #0f172a; }
            dd { margin: 0; color: #1f2937; }
            .pill { display: inline-flex; align-items: center; padding: 0.2rem 0.6rem; border-radius: 999px; background: #ecfdf3; color: #166534; font-weight: 700; font-size: 0.82rem; }
          </style>
        </head>
        <body>
          <h1>Perfil profissional</h1>
          <p class="pill">${data.status || 'Sem status'}</p>
          <dl>
            <dt>Nome completo</dt>
            <dd>${data.nome || 'Nao informado'}</dd>
            <dt>Categoria</dt>
            <dd>${data.categoria || 'Nao informado'}</dd>
            <dt>Especialidade</dt>
            <dd>${data.especialidade || 'Nao informado'}</dd>
            <dt>Registro em conselho</dt>
            <dd>${data.registroConselho || 'Nao informado'}</dd>
            <dt>E-mail</dt>
            <dd>${data.email || 'Nao informado'}</dd>
            <dt>Telefone/WhatsApp</dt>
            <dd>${data.telefone || 'Nao informado'}</dd>
            <dt>Unidade de atendimento</dt>
            <dd>${data.unidade || 'Nao informado'}</dd>
            <dt>Carga horaria</dt>
            <dd>${data.cargaHoraria ? data.cargaHoraria + 'h semanais' : 'Nao informado'}</dd>
            <dt>Disponibilidade</dt>
            <dd>${disponibilidade}</dd>
            <dt>Canais de atendimento</dt>
            <dd>${canais}</dd>
            <dt>Palavras-chave</dt>
            <dd>${tags}</dd>
            <dt>Resumo breve</dt>
            <dd>${data.resumo || 'Sem resumo'}</dd>
            <dt>Observacoes internas</dt>
            <dd>${data.observacoes || 'Sem observacoes'}</dd>
          </dl>
        </body>
      </html>
    `);

    printWindow.document.close();
    printWindow.focus();
    printWindow.print();
  }

  removeCurrent(): void {
    if (!this.editingId) return;

    const current = this.professionals.find((item) => item.id === this.editingId);
    if (!current) {
      this.startNew();
      return;
    }

    if (!window.confirm(`Remover ${current.nome} do cadastro?`)) return;

    this.professionalService
      .delete(current.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.professionals = this.professionals.filter((item) => item.id !== current.id);
          this.startNew();
        },
        error: () => {
          this.feedback = 'Nao foi possivel remover o profissional. Tente novamente.';
        }
      });
  }

  closeForm(): void {
    window.history.back();
  }

  onSave(): void {
    this.submit();
  }

  onCancel(): void {
    this.resetForm();
  }

  onDelete(): void {
    this.removeCurrent();
  }

  onNew(): void {
    this.startNew();
  }

  onPrint(): void {
    const profissionalAtual = this.editingId
      ? this.professionals.find((item) => item.id === this.editingId)
      : (this.form.value as ProfessionalPayload);
    if (!profissionalAtual) {
      this.feedback = 'Nao foi possivel obter os dados do profissional para imprimir.';
      return;
    }

    this.termoPrintService.printTermoVoluntariado(this.mapUnidadeParaTermo(this.unidadeAtual), this.mapProfissionalParaTermo(profissionalAtual));
  }

  onClose(): void {
    this.closeForm();
  }

  private mapUnidadeParaTermo(unidade: AssistanceUnitPayload | null): any {
    return {
      nomeFantasia: unidade?.nomeFantasia,
      razaoSocial: unidade?.razaoSocial,
      cnpj: unidade?.cnpj,
      endereco: unidade?.endereco,
      numeroEndereco: unidade?.numeroEndereco,
      complemento: unidade?.complemento,
      bairro: unidade?.bairro,
      cidade: unidade?.cidade,
      estado: unidade?.estado,
      inscricaoMunicipal: (unidade as any)?.inscricaoMunicipal,
      coordenadorNome: (unidade as any)?.coordenadorNome
    };
  }

  private mapProfissionalParaTermo(profissional: ProfessionalRecord | ProfessionalPayload): any {
    const value = profissional as ProfessionalPayload;
    return {
      nome: value.nome,
      email: value.email,
      telefoneCelular: value.telefone,
      voluntariadoAtividades: value.tags ?? [],
      voluntariadoOutros: value.resumo,
      voluntariadoPeriodo: value.unidade
    };
  }

  private loadProfessionals(): void {
    this.professionalService
      .list()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (records) => {
          this.professionals = records;
        },
        error: () => {
          this.feedback = 'Nao foi possivel carregar os profissionais.';
        }
      });
  }

  private setupCapitalizationRules(): void {
    const applyRule = (controlName: string) => {
      const control = this.form.get(controlName);
      if (!control) return;

      const subscription = control.valueChanges.subscribe((value) => {
        if (typeof value !== 'string') return;
        if (controlName === 'email') return;

        this.applyCapitalization(controlName);
      });

      this.capitalizationSubs.push(() => subscription.unsubscribe());
    };

    ['nome', 'categoria', 'registroConselho', 'especialidade', 'telefone', 'unidade'].forEach(applyRule);
  }

  applyCapitalization(controlName: string, rawValue?: string): void {
    const control = this.form.get(controlName);
    if (!control) return;
    if (controlName === 'email') return;

    const currentValue = rawValue ?? control.value;
    if (typeof currentValue !== 'string') return;

    const transformed = titleCaseWords(currentValue);
    if (transformed && transformed !== currentValue) {
      control.setValue(transformed, { emitEvent: false });
    }
  }
}
