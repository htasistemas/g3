import { CommonModule } from '@angular/common';
import { Component, OnDestroy, QueryList, ViewChildren } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { CdkDragDrop, CdkDropList, DragDropModule } from '@angular/cdk/drag-drop';
import { finalize } from 'rxjs/operators';
import { TarefasPendenciasService, ChecklistItem, TaskPayload, TaskRecord } from '../../services/tarefas-pendencias.service';
import { ProfessionalService } from '../../services/professional.service';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';
import { PopupErrorBuilder } from '../../utils/popup-error.builder';
import { ConfigAcoesCrud, EstadoAcoesCrud, TelaBaseComponent } from '../compartilhado/tela-base.component';
import { titleCaseWords } from '../../utils/capitalization.util';

interface StepTab {
  id: 'cadastro' | 'acompanhamento' | 'listagem' | 'dashboard';
  label: string;
}

@Component({
  selector: 'app-tarefas-pendencias',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, TelaPadraoComponent, PopupMessagesComponent, DragDropModule],
  templateUrl: './tarefas-pendencias.component.html',
  styleUrl: './tarefas-pendencias.component.scss'
})
export class TarefasPendenciasComponent extends TelaBaseComponent implements OnDestroy {
  form: FormGroup;
  checklistDraft: ChecklistItem[] = [];
  checklistEntry = '';
  popupErros: string[] = [];
  feedback: string | null = null;
  tasks: TaskRecord[] = [];
  selectedTask: TaskRecord | null = null;
  editingId: string | null = null;
  responsaveisSugeridos: string[] = [];
  saving = false;
  listLoading = false;
  activeTab: StepTab['id'] = 'cadastro';
  tabs: StepTab[] = [
    { id: 'cadastro', label: 'Cadastro e controle' },
    { id: 'acompanhamento', label: 'Acompanhamento das tarefas' },
    { id: 'listagem', label: 'Listagem das tarefas' },
    { id: 'dashboard', label: 'Dashboard e alertas' }
  ];
  imprimindoRelatorio = false;
  private feedbackTimeout?: ReturnType<typeof setTimeout>;
  private readonly capitalizationSubs: Array<() => void> = [];

  readonly prioridades: TaskPayload['prioridade'][] = ['Alta', 'Média', 'Baixa'];
  readonly statusOptions: TaskPayload['status'][] = ['Aberta', 'Em andamento', 'Concluída', 'Em atraso'];
  readonly processStages = [
    { label: 'Aberta', status: 'Aberta' as TaskRecord['status'], next: 'Em andamento' as TaskRecord['status'] },
    {
      label: 'Em andamento',
      status: 'Em andamento' as TaskRecord['status'],
      prev: 'Aberta' as TaskRecord['status'],
      next: 'Em atraso' as TaskRecord['status']
    },
    {
      label: 'Em atraso',
      status: 'Em atraso' as TaskRecord['status'],
      prev: 'Em andamento' as TaskRecord['status'],
      next: 'Concluída' as TaskRecord['status']
    },
    { label: 'Concluída', status: 'Concluída' as TaskRecord['status'], prev: 'Em atraso' as TaskRecord['status'] }
  ];

  readonly acoesToolbar: Required<ConfigAcoesCrud> = this.criarConfigAcoes({    
    salvar: true,
    excluir: true,
    novo: true,
    cancelar: true,
    imprimir: true,
    buscar: true
  });

  get acoesDesabilitadas(): EstadoAcoesCrud {
    return {
      salvar: this.saving,
      excluir: !this.editingId,
      novo: this.saving,
      cancelar: this.saving,
      imprimir: this.imprimindoRelatorio,
      buscar: this.saving
    };
  }

  constructor(
    private readonly fb: FormBuilder,
    private readonly tarefasService: TarefasPendenciasService,
    private readonly professionalService: ProfessionalService
  ) {
    super();

    this.form = this.fb.group({
      titulo: ['', Validators.required],
      descricao: ['', Validators.required],
      responsavel: ['', Validators.required],
      prioridade: [this.prioridades[1], Validators.required],
      prazo: [null, Validators.required],
      status: [this.statusOptions[0], Validators.required]
    });

    this.loadTasks();
    this.loadResponsaveis();
    this.setupCapitalizationRules();
  }

  ngOnDestroy(): void {
    this.capitalizationSubs.forEach((unsubscribe) => unsubscribe());
    this.clearFeedbackTimeout();
  }

  get dashboard() {
    const total = this.tasks.length;
    const abertas = this.tasks.filter((task) => task.status === 'Aberta').length;
    const andamento = this.tasks.filter((task) => task.status === 'Em andamento').length;
    const concluidas = this.tasks.filter((task) => task.status === 'Concluída').length;
    const atrasadas = this.tasks.filter((task) => this.isOverdue(task) && task.status !== 'Concluída').length;
    const proximas = this.tasks.filter((task) => this.isDueSoon(task) && task.status !== 'Concluída').length;
    return { total, abertas, andamento, concluidas, atrasadas, proximas };
  }

  get alertas() {
    return this.tasks.filter((task) => this.isOverdue(task) || this.isDueSoon(task)).slice(0, 4);
  }

  get checklistProgresso(): string {
    const totalItens = this.tasks.reduce((acc, task) => acc + task.checklist.length, 0);
    const concluidos = this.tasks.reduce(
      (acc, task) => acc + task.checklist.filter((item) => item.concluido).length,
      0
    );
    if (!totalItens) return '0%';
    return `${Math.round((concluidos / totalItens) * 100)}%`;
  }

  @ViewChildren(CdkDropList)
  private dropLists?: QueryList<CdkDropList<TaskRecord[]>>;

  getTasksByStage(status: TaskRecord['status']): TaskRecord[] {
    return this.tasks.filter((task) => task.status === status);
  }

  getConnectedKanbanLists(current: CdkDropList<TaskRecord[]>): CdkDropList<TaskRecord[]>[] {
    const lists = this.dropLists?.toArray() ?? [];
    return lists.filter((list) => list !== current);
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
    return this.hasNextTab ? this.tabs[this.activeTabIndex + 1].label : '';
  }

  changeTab(tabId: StepTab['id']): void {
    this.activeTab = tabId;
  }

  onBuscar(): void {
    this.changeTab('listagem');
  }

  goToNextTab(): void {
    if (!this.hasNextTab) return;
    this.changeTab(this.tabs[this.activeTabIndex + 1].id);
  }

  goToPreviousTab(): void {
    if (!this.hasPreviousTab) return;
    this.changeTab(this.tabs[this.activeTabIndex - 1].id);
  }

  moveTaskToStage(task: TaskRecord, targetStatus: TaskRecord['status']): void {
    if (task.status === targetStatus) return;
    this.changeStatus(task, targetStatus);
  }

  onKanbanDrop(event: CdkDragDrop<TaskRecord[]>, targetStatus: TaskRecord['status']): void {
    if (event.previousContainer === event.container) return;
    const task = event.item.data as TaskRecord;
    if (!task) return;
    this.moveTaskToStage(task, targetStatus);
  }

  onSave(): void {
    this.saveTask();
  }

  onCancel(): void {
    this.resetForm();
  }

  onNew(): void {
    this.startNewTask();
  }

  onDelete(): void {
    if (!this.editingId) return;
    const current = this.tasks.find((task) => task.id === this.editingId);
    if (current) {
      this.removeTask(current);
    }
  }

  imprimirRelatorio(): void {
    if (this.imprimindoRelatorio) return;
    this.imprimindoRelatorio = true;
    this.tarefasService
      .imprimirPendencias()
      .pipe(finalize(() => (this.imprimindoRelatorio = false)))
      .subscribe({
        next: (blob) => this.baixarRelatorio(blob),
        error: () => this.setFeedback('Não foi possível gerar o relatório de pendências.')
      });
  }

  fecharPopupErros(): void {
    this.popupErros = [];
  }

  saveTask(): void {
    this.popupErros = [];
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      const builder = new PopupErrorBuilder();
      [
        { control: 'titulo', label: 'Título' },
        { control: 'descricao', label: 'Descrição' },
        { control: 'responsavel', label: 'Responsável' },
        { control: 'prioridade', label: 'Prioridade' },
        { control: 'prazo', label: 'Prazo previsto' },
        { control: 'status', label: 'Status' }
      ].forEach(({ control, label }) => {
        const field = this.form.get(control);
        if (!field?.value) {
          builder.adicionar(`${label} é obrigatório.`);
        }
      });
      this.popupErros = builder.build();
      this.setFeedback('Preencha os campos obrigatórios para registrar a pendência.');
      return;
    }

    const payload = this.buildPayload();
    this.saving = true;
    const request = this.editingId
      ? this.tarefasService.update(this.editingId, payload)
      : this.tarefasService.create(payload);

    request
      .pipe(finalize(() => (this.saving = false)))
      .subscribe({
        next: (record) => {
          if (this.editingId) {
            this.tasks = this.tasks.map((task) => (task.id === record.id ? record : task));
            this.setFeedback('Pendência atualizada com sucesso.');
          } else {
            this.tasks = [record, ...this.tasks];
            this.setFeedback('Pendência registrada com sucesso.');
          }
          this.selectedTask = record;
          this.resetForm();
          this.changeTab('listagem');
        },
        error: () => {
          this.setFeedback('Não foi possível salvar a pendência. Tente novamente.');
        }
      });
  }

  editTask(task: TaskRecord): void {
    this.editingId = task.id;
    this.selectedTask = task;
    this.form.patchValue({
      titulo: task.titulo,
      descricao: task.descricao,
      responsavel: task.responsavel,
      prioridade: task.prioridade,
      prazo: task.prazo || null,
      status: task.status
    });
    this.checklistDraft = task.checklist.map((item) => ({ ...item }));
    this.changeTab('cadastro');
  }

  removeTask(task: TaskRecord): void {
    if (!window.confirm(`Remover a pendência "${task.titulo}"?`)) return;
    this.tarefasService
      .delete(task.id)
      .pipe(finalize(() => {}))
      .subscribe({
        next: () => {
          this.tasks = this.tasks.filter((item) => item.id !== task.id);
          if (this.selectedTask?.id === task.id) {
            this.selectedTask = null;
          }
          if (this.editingId === task.id) {
            this.resetForm();
          }
          this.setFeedback('Pendência removida.');
        },
        error: () => {
          this.setFeedback('Não foi possível remover a pendência. Tente novamente.');
        }
      });
  }

  addChecklistItem(): void {
    const titulo = this.checklistEntry.trim();
    if (!titulo) return;
    this.checklistDraft = [
      ...this.checklistDraft,
      { id: crypto.randomUUID(), titulo, concluido: false, ordem: this.checklistDraft.length }
    ];
    this.checklistEntry = '';
  }

  toggleDraftChecklist(item: ChecklistItem): void {
    this.checklistDraft = this.checklistDraft.map((entry) =>
      entry.id === item.id
        ? {
            ...entry,
            concluido: !entry.concluido,
            concluidoEm: !entry.concluido ? new Date().toISOString() : undefined
          }
        : entry
    );
  }

  toggleChecklist(task: TaskRecord, item: ChecklistItem): void {
    this.popupErros = [];
    this.tarefasService.toggleChecklist(task, item).subscribe({
      next: (updated) => this.updateTaskInList(updated),
      error: () => this.setFeedback('Não foi possível atualizar o checklist.')
    });
  }

  changeStatus(task: TaskRecord, status: TaskRecord['status']): void {
    this.tarefasService.updateStatus(task, status).subscribe({
      next: (updated) => this.updateTaskInList(updated),
      error: () => this.setFeedback('Não foi possível atualizar o status.')
    });
  }

  selectTask(task: TaskRecord): void {
    this.selectedTask = task;
  }

  startNewTask(): void {
    this.resetForm();
    this.changeTab('cadastro');
  }

  private loadTasks(): void {
    this.listLoading = true;
    this.tarefasService
      .list()
      .pipe(finalize(() => (this.listLoading = false)))
      .subscribe({
        next: (records) => {
          this.tasks = records;
          if (!this.selectedTask && records.length) {
            this.selectedTask = records[0];
          }
        },
        error: () => {
          this.setFeedback('Não foi possível carregar as pendências no momento.');
        }
      });
  }

  private loadResponsaveis(): void {
    this.professionalService.list().subscribe({
      next: (profissionais) => {
        this.responsaveisSugeridos = profissionais.map((profissional) => profissional.nomeCompleto);
      },
      error: () => {
        this.responsaveisSugeridos = [];
      }
    });
  }

  private updateTaskInList(updated: TaskRecord): void {
    this.tasks = this.tasks.map((task) => (task.id === updated.id ? updated : task));
    if (this.selectedTask?.id === updated.id) {
      this.selectedTask = updated;
    }
  }

  private buildPayload(): TaskPayload {
    return {
      titulo: this.form.get('titulo')?.value?.trim() ?? '',
      descricao: this.form.get('descricao')?.value ?? '',
      responsavel: this.form.get('responsavel')?.value ?? '',
      prioridade: this.form.get('prioridade')?.value,
      prazo: this.form.get('prazo')?.value,
      status: this.form.get('status')?.value,
      checklist: this.checklistDraft
    };
  }

  private resetForm(): void {
    this.editingId = null;
    this.popupErros = [];
    this.form.reset({
      titulo: '',
      descricao: '',
      responsavel: '',
      prioridade: this.prioridades[1],
      prazo: null,
      status: this.statusOptions[0]
    });
    this.checklistDraft = [];
    this.checklistEntry = '';
  }

  private baixarRelatorio(blob: Blob): void {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = 'relatorio-tarefas-pendencias.pdf';
    link.click();
    window.URL.revokeObjectURL(url);
  }

  private setFeedback(message: string, timeoutMs = 10000): void {
    this.feedback = message;
    this.clearFeedbackTimeout();
    this.feedbackTimeout = setTimeout(() => {
      this.feedback = null;
      this.feedbackTimeout = undefined;
    }, timeoutMs);
  }

  private clearFeedbackTimeout(): void {
    if (this.feedbackTimeout) {
      clearTimeout(this.feedbackTimeout);
      this.feedbackTimeout = undefined;
    }
  }

  private setupCapitalizationRules(): void {
    ['titulo', 'responsavel'].forEach((field) => {
      const control = this.form.get(field);
      if (!control) return;
      const subscription = control.valueChanges.subscribe((value) => {
        if (typeof value !== 'string') return;
        const formatted = titleCaseWords(value);
        if (formatted && formatted !== value) {
          control.setValue(formatted, { emitEvent: false });
        }
      });
      this.capitalizationSubs.push(() => subscription.unsubscribe());
    });
  }

  private isOverdue(task: TaskRecord): boolean {
    const prazo = new Date(task.prazo ?? '');
    if (Number.isNaN(prazo.getTime())) return false;
    const hoje = new Date();
    return prazo < hoje && task.status !== 'Concluída';
  }

  private isDueSoon(task: TaskRecord): boolean {
    const prazo = new Date(task.prazo ?? '');
    if (Number.isNaN(prazo.getTime())) return false;
    const hoje = new Date();
    const diff = prazo.getTime() - hoje.getTime();
    const doisDias = 1000 * 60 * 60 * 24 * 2;
    return diff > 0 && diff <= doisDias && task.status !== 'Concluída';
  }

  dueLabel(task: TaskRecord): string {
    const prazo = new Date(task.prazo ?? '');
    return Number.isNaN(prazo.getTime()) ? 'Sem data' : prazo.toLocaleDateString('pt-BR');
  }

  progress(task: TaskRecord): string {
    const total = task.checklist.length;
    const done = task.checklist.filter((item) => item.concluido).length;
    if (!total) return 'Sem checklist';
    return `${done}/${total} (${Math.round((done / total) * 100)}%)`;
  }

  statusTone(task: TaskRecord): 'green' | 'amber' | 'red' {
    if (task.status === 'Concluída') return 'green';
    if (this.isOverdue(task)) return 'red';
    if (task.status === 'Em andamento') return 'amber';
    return 'green';
  }
}
