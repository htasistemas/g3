import { CommonModule } from '@angular/common';
import { Component, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  ProfessionalPayload,
  ProfessionalRecord,
  ProfessionalService,
  ProfessionalStatus
} from '../../services/professional.service';

interface StepTab {
  id: string;
  label: string;
}

@Component({
  selector: 'app-profissionais-cadastro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profissionais-cadastro.component.html',
  styleUrl: './profissionais-cadastro.component.scss'
})
export class ProfissionaisCadastroComponent implements OnDestroy {
  form: FormGroup;
  feedback: string | null = null;
  professionals: ProfessionalRecord[] = [];
  editingId: string | null = null;
  saving = false;
  private readonly capitalizationSubs: Array<() => void> = [];

  tabs: StepTab[] = [
    { id: 'perfil', label: 'Perfil profissional' },
    { id: 'agenda', label: 'Agenda e canais' },
    { id: 'resumo', label: 'Resumo e observações' }
  ];
  activeTab: StepTab['id'] = 'perfil';

  categorias = ['Assistente social', 'Psicólogo(a)', 'Pedagogo(a)', 'Médico(a)', 'Nutricionista'];
  readonly disponibilidades = ['Manhã', 'Tarde', 'Noite'];
  readonly canais = ['Presencial', 'Online', 'Telefone'];
  readonly statuses: ProfessionalStatus[] = ['Disponível', 'Em atendimento', 'Em intervalo', 'Indisponível'];
  readonly tagsSugeridas = ['Acolhimento', 'Triagem', 'Famílias', 'Juventude', 'Visitas', 'Oficinas'];

  constructor(private readonly fb: FormBuilder, private readonly professionalService: ProfessionalService) {
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
      status: ['Disponível', Validators.required],
      tags: this.fb.control<string[]>([]),
      resumo: [''],
      observacoes: ['']
    });

    this.loadProfessionals();
    this.setupCapitalizationRules();
  }

  ngOnDestroy(): void {
    this.capitalizationSubs.forEach((unsubscribe) => unsubscribe());
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
    return this.professionals.filter((p) => p.status === 'Disponível').length;
  }

  get totalOnline(): number {
    return this.professionals.filter((p) => p.canaisAtendimento?.includes('Online')).length;
  }

  get totalPresencial(): number {
    return this.professionals.filter((p) => p.canaisAtendimento?.includes('Presencial')).length;
  }

  submit(): void {
    this.feedback = null;
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.feedback = 'Preencha os campos obrigatórios para salvar o profissional.';
      return;
    }

    this.saving = true;
    const payload = this.form.value as ProfessionalPayload;
    if (this.editingId) {
      const updated = this.professionalService.update(this.editingId, payload);
      this.professionals = this.professionals.map((item) => (item.id === updated.id ? updated : item));
      this.feedback = 'Profissional atualizado com sucesso.';
    } else {
      const created = this.professionalService.create(payload);
      this.professionals = [created, ...this.professionals];
      this.feedback = 'Profissional cadastrado com sucesso.';
    }

    this.saving = false;
    this.resetForm();
    this.changeTab('perfil');
  }

  addCategoria(value: string): void {
    const formatted = this.capitalize(value);
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
    const formatted = this.capitalize(next ?? '');
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
    this.professionalService.delete(record.id);
    this.professionals = this.professionals.filter((item) => item.id !== record.id);
    this.resetForm();
    this.changeTab('perfil');
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
    const formatted = this.capitalize(tag);
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
      status: 'Disponível',
      tags: [],
      resumo: '',
      observacoes: ''
    });
  }

  private loadProfessionals(): void {
    this.professionals = this.professionalService.list();
  }

  private setupCapitalizationRules(): void {
    const applyRule = (controlName: string) => {
      const control = this.form.get(controlName);
      if (!control) return;

      const subscription = control.valueChanges.subscribe((value) => {
        if (typeof value !== 'string') return;
        if (controlName === 'email') return;

        const transformed = this.capitalize(value);
        if (transformed && transformed !== value) {
          control.setValue(transformed, { emitEvent: false });
        }
      });

      this.capitalizationSubs.push(() => subscription.unsubscribe());
    };

    ['nome', 'categoria', 'registroConselho', 'especialidade', 'telefone', 'unidade', 'resumo', 'observacoes'].forEach(
      applyRule
    );
  }

  private capitalize(value: string): string {
    if (!value || typeof value !== 'string') return '';
    const trimmed = value.trimStart();
    if (!trimmed) return '';
    return `${trimmed.charAt(0).toUpperCase()}${trimmed.slice(1).toLowerCase()}`;
  }
}
