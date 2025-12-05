import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ChecklistItem, TaskRecord, TarefasPendenciasService } from '../../services/tarefas-pendencias.service';
import { FormsModule } from '@angular/forms';
import { ProfessionalService } from '../../services/professional.service';

interface StepTab {
  id: string;
  label: string;
}

interface DashboardSnapshot {
  total: number;
  abertas: number;
  andamento: number;
  concluidas: number;
  atrasadas: number;
  proximas: number;
}

@Component({
  selector: 'app-tarefas-pendencias',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './tarefas-pendencias.component.html',
  styleUrl: './tarefas-pendencias.component.scss'
})
export class TarefasPendenciasComponent {
  form: FormGroup;
  tasks: TaskRecord[] = [];
  checklistDraft: ChecklistItem[] = [];
  checklistEntry = '';
  feedback: string | null = null;
  editingId: string | null = null;
  selectedTask: TaskRecord | null = null;
  responsaveisSugeridos: string[] = [];
  activeTab: StepTab['id'] = 'dados';

  tabs: StepTab[] = [
    { id: 'dados', label: 'Dados da pendência' },
    { id: 'checklist', label: 'Checklist e andamento' },
    { id: 'dashboard', label: 'Dashboard & histórico' }
  ];

  readonly prioridades: TaskRecord['prioridade'][] = ['Alta', 'Média', 'Baixa'];
  readonly statusOptions: TaskRecord['status'][] = ['Aberta', 'Em andamento', 'Concluída', 'Em atraso'];

  constructor(
    private readonly fb: FormBuilder,
    private readonly tarefasService: TarefasPendenciasService,
    private readonly professionalService: ProfessionalService
  ) {
    this.form = this.fb.group({
      titulo: ['', Validators.required],
      descricao: ['', Validators.required],
      responsavel: ['', Validators.required],
      prioridade: ['Média', Validators.required],
      prazo: ['', Validators.required],
      status: ['Aberta', Validators.required]
    });

    this.loadTasks();
    this.loadResponsaveis();
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

  get dashboard(): DashboardSnapshot {
    const total = this.tasks.length;
    const abertas = this.tasks.filter((task) => task.status === 'Aberta').length;
    const andamento = this.tasks.filter((task) => task.status === 'Em andamento').length;
    const concluidas = this.tasks.filter((task) => task.status === 'Concluída').length;
    const atrasadas = this.tasks.filter((task) => this.isOverdue(task) && task.status !== 'Concluída').length;
    const proximas = this.tasks.filter((task) => this.isDueSoon(task) && task.status !== 'Concluída').length;

    return { total, abertas, andamento, concluidas, atrasadas, proximas };
  }

  get alertas(): TaskRecord[] {
    return this.tasks.filter((task) => this.isOverdue(task) || this.isDueSoon(task)).slice(0, 4);
  }

  get checklistProgresso(): string {
    const totalItens = this.tasks.reduce((acc, task) => acc + (task.checklist?.length ?? 0), 0);
    const concluidos = this.tasks.reduce(
      (acc, task) => acc + (task.checklist?.filter((item) => item.concluido).length ?? 0),
      0
    );
    if (!totalItens) return '0%';
    return `${Math.round((concluidos / totalItens) * 100)}%`;
  }

  saveTask(): void {
    this.feedback = null;
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.feedback = 'Preencha os campos obrigatórios para registrar a pendência.';
      return;
    }

    const payload = { ...this.form.value, checklist: this.checklistDraft };

    if (this.editingId) {
      const updated = this.tarefasService.update(this.editingId, payload, 'Pendência atualizada');
      this.tasks = this.tasks.map((task) => (task.id === updated.id ? updated : task));
      this.selectedTask = updated;
      this.feedback = 'Pendência atualizada com sucesso.';
    } else {
      const created = this.tarefasService.create(payload);
      this.tasks = [created, ...this.tasks];
      this.selectedTask = created;
      this.feedback = 'Pendência registrada com sucesso.';
    }

    this.normalizeStatuses();
    this.resetForm();
    this.changeTab('dashboard');
  }

  editTask(task: TaskRecord): void {
    this.editingId = task.id;
    this.selectedTask = task;
    this.changeTab('dados');
    this.form.patchValue({
      titulo: task.titulo,
      descricao: task.descricao,
      responsavel: task.responsavel,
      prioridade: task.prioridade,
      prazo: task.prazo,
      status: task.status
    });
    this.checklistDraft = task.checklist ? structuredClone(task.checklist) : [];
  }

  removeTask(task: TaskRecord): void {
    if (!window.confirm(`Remover a pendência "${task.titulo}"?`)) return;
    this.tarefasService.delete(task.id);
    this.tasks = this.tasks.filter((item) => item.id !== task.id);
    if (this.selectedTask?.id === task.id) this.selectedTask = null;
    if (this.editingId === task.id) this.resetForm();
  }

  addChecklistItem(): void {
    const titulo = this.checklistEntry.trim();
    if (!titulo) return;
    this.checklistDraft = [
      ...this.checklistDraft,
      { id: crypto.randomUUID(), titulo, concluido: false }
    ];
    this.checklistEntry = '';
  }

  toggleDraftChecklist(item: ChecklistItem): void {
    this.checklistDraft = this.checklistDraft.map((entry) =>
      entry.id === item.id
        ? { ...entry, concluido: !entry.concluido, concluidoEm: !entry.concluido ? new Date().toISOString() : undefined }
        : entry
    );
  }

  toggleChecklist(task: TaskRecord, item: ChecklistItem): void {
    const updated = this.tarefasService.toggleChecklist(task.id, item.id);
    if (!updated) return;
    this.tasks = this.tasks.map((t) => (t.id === updated.id ? updated : t));
    if (this.selectedTask?.id === updated.id) this.selectedTask = updated;
  }

  changeStatus(task: TaskRecord, status: TaskRecord['status']): void {
    const updated = this.tarefasService.updateStatus(task.id, status);
    if (!updated) return;
    this.tasks = this.tasks.map((t) => (t.id === updated.id ? updated : t));
    if (this.selectedTask?.id === updated.id) this.selectedTask = updated;
  }

  resetForm(): void {
    this.editingId = null;
    this.form.reset({
      titulo: '',
      descricao: '',
      responsavel: '',
      prioridade: 'Média',
      prazo: '',
      status: 'Aberta'
    });
    this.checklistDraft = [];
    this.changeTab('dados');
  }

  selectTask(task: TaskRecord): void {
    this.selectedTask = task;
  }

  progress(task: TaskRecord): string {
    const total = task.checklist?.length ?? 0;
    const done = task.checklist?.filter((item) => item.concluido).length ?? 0;
    if (!total) return 'Sem checklist';
    return `${done}/${total} (${Math.round((done / total) * 100)}%)`;
  }

  dueLabel(task: TaskRecord): string {
    const date = new Date(task.prazo);
    return isNaN(date.getTime()) ? 'Sem data' : date.toLocaleDateString('pt-BR');
  }

  statusTone(task: TaskRecord): 'green' | 'amber' | 'red' {
    if (task.status === 'Concluída') return 'green';
    if (this.isOverdue(task)) return 'red';
    if (task.status === 'Em andamento') return 'amber';
    return 'green';
  }

  private loadTasks(): void {
    this.tasks = this.tarefasService.list();
    this.normalizeStatuses();
  }

  private normalizeStatuses(): void {
    this.tasks = this.tasks.map((task) => {
      if (task.status !== 'Concluída' && this.isOverdue(task) && task.status !== 'Em atraso') {
        return this.tarefasService.update(task.id, { status: 'Em atraso' }, 'Status ajustado por prazo vencido');
      }
      return task;
    });
  }

  private loadResponsaveis(): void {
    this.responsaveisSugeridos = this.professionalService.list().map((prof) => prof.nome);
  }

  isOverdue(task: TaskRecord): boolean {
    const prazo = new Date(task.prazo);
    if (isNaN(prazo.getTime())) return false;
    const hoje = new Date();
    return prazo < hoje && task.status !== 'Concluída';
  }

  private isDueSoon(task: TaskRecord): boolean {
    const prazo = new Date(task.prazo);
    if (isNaN(prazo.getTime())) return false;
    const hoje = new Date();
    const diff = prazo.getTime() - hoje.getTime();
    const doisDias = 1000 * 60 * 60 * 24 * 2;
    return diff > 0 && diff <= doisDias && task.status !== 'Concluída';
  }
}
