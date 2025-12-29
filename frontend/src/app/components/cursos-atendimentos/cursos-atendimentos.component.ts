import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { CourseRecord, CursosAtendimentosService, Enrollment, EnrollmentStatus, WaitlistEntry } from '../../services/cursos-atendimentos.service';
import { BeneficiaryPayload, BeneficiaryService } from '../../services/beneficiary.service';
import { SalaRecord, SalasService } from '../../services/salas.service';
import { AssistanceUnitService } from '../../services/assistance-unit.service';
import { ProfessionalRecord, ProfessionalService } from '../../services/professional.service';
import { catchError, debounceTime, distinctUntilChanged, of, Subscription, switchMap, tap } from 'rxjs';
import { titleCaseWords } from '../../utils/capitalization.util';

import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
const generateId = (): string => {
  if (typeof crypto !== 'undefined' && typeof crypto.randomUUID === 'function') {
    return crypto.randomUUID();
  }

  // Fallback for environments without crypto.randomUUID
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (char) => {
    const random = (Math.random() * 16) | 0;
    const value = char === 'x' ? random : (random & 0x3) | 0x8;
    return value.toString(16);
  });
};

interface StepTab {
  id: string;
  label: string;
}

interface DashboardSnapshot {
  totalVagas: number;
  vagasDisponiveis: number;
  vagasEmUso: number;
  ocupacao: number;
  totalMatriculas: number;
  totalInscricoes: number;
  concluidos: number;
  cancelados: number;
  waitlist: number;
  waitlistPressao: number;
  cursos: number;
  atendimentos: number;
  oficinas: number;
  profissionais: number;
  mediaCargaHoraria: number;
  taxaConclusao: number;
  engajamento: number;
}

interface DashboardWidget {
  id: string;
  title: string;
  description: string;
  gradient: string;
  getValue(snapshot: DashboardSnapshot): string;
  getHelper(snapshot: DashboardSnapshot): string;
  getProgress?: (snapshot: DashboardSnapshot) => number;
}

interface WidgetState {
  order: string[];
  hidden: string[];
}

@Component({
  selector: 'app-cursos-atendimentos',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, TelaPadraoComponent],
  templateUrl: './cursos-atendimentos.component.html',
  styleUrl: './cursos-atendimentos.component.scss'
})
export class CursosAtendimentosComponent implements OnInit, OnDestroy {
  readonly tabs: StepTab[] = [
    { id: 'dados', label: 'Dados do Curso/Atendimento/Oficinas' },
    { id: 'catalogo', label: 'Catálogo e Vagas' },
    { id: 'inscricoes', label: 'Inscrições e Lista de Espera' },
    { id: 'dashboard', label: 'Dashboard' }
  ];

  readonly diasSemana = [
    'Domingo',
    'Segunda-feira',
    'Terça-feira',
    'Quarta-feira',
    'Quinta-feira',
    'Sexta-feira',
    'Sábado'
  ];

  activeTab: StepTab['id'] = 'dados';
  feedback: string | null = null;
  saving = false;
  editingId: string | null = null;
  private readonly capitalizationSubs: Subscription[] = [];

  courseForm: FormGroup;
  enrollmentForm: FormGroup;
  beneficiaryResults: BeneficiaryPayload[] = [];
  beneficiarySearchLoading = false;
  private beneficiarySearchSub?: Subscription;
  professionalResults: ProfessionalRecord[] = [];
  professionalSearchLoading = false;
  private professionalSearchSub?: Subscription;
  rooms: SalaRecord[] = [];
  roomsLoading = false;
  unidadeAtualId: number | null = null;

  records: CourseRecord[] = [];
  dashboardSnapshot: DashboardSnapshot = this.buildDashboardSnapshot();
  widgetState: WidgetState = { order: [], hidden: [] };
  readonly dashboardWidgets: DashboardWidget[] = [
    {
      id: 'ocupacao',
      title: 'Taxa de ocupação',
      description: 'Quanto das vagas está preenchido nos cursos e atendimentos.',
      gradient: 'emerald',
      getValue: (snapshot) => `${snapshot.ocupacao}%`,
      getHelper: (snapshot) => `${snapshot.vagasEmUso}/${snapshot.totalVagas || 0} vagas em uso`,
      getProgress: (snapshot) => snapshot.ocupacao
    },
    {
      id: 'profissionais',
      title: 'Profissionais ativos',
      description: 'Responsáveis vinculados às ofertas cadastradas.',
      gradient: 'teal',
      getValue: (snapshot) => `${snapshot.profissionais}`,
      getHelper: (snapshot) => `${snapshot.mediaCargaHoraria}h média de carga semanal`
    },
    {
      id: 'matriculas',
      title: 'Inscrições ativas',
      description: 'Matrículas em andamento e fila de espera.',
      gradient: 'sky',
      getValue: (snapshot) => `${snapshot.totalMatriculas}`,
      getHelper: (snapshot) => `${snapshot.totalInscricoes} registros + ${snapshot.waitlist} em espera`,
      getProgress: (snapshot) => Math.min(100, Math.max(10, 100 - snapshot.waitlistPressao))
    },
    {
      id: 'conclusao',
      title: 'Taxa de conclusão',
      description: 'Percentual de beneficiarios que concluiram as atividades.',
      gradient: 'indigo',
      getValue: (snapshot) => `${snapshot.taxaConclusao}%`,
      getHelper: (snapshot) => `${snapshot.concluidos} concluídos • ${snapshot.cancelados} cancelamentos`,
      getProgress: (snapshot) => snapshot.taxaConclusao
    },
    {
      id: 'portfolio',
      title: 'Portfólio de ofertas',
      description: 'Distribuição entre cursos, atendimentos e oficinas.',
      gradient: 'violet',
      getValue: (snapshot) => `${snapshot.cursos + snapshot.atendimentos + snapshot.oficinas}`,
      getHelper: (snapshot) => `${snapshot.cursos} cursos • ${snapshot.atendimentos} atendimentos • ${snapshot.oficinas} oficinas`
    },
    {
      id: 'engajamento',
      title: 'Engajamento',
      description: 'Combinação de ocupação, fila e retenção.',
      gradient: 'fuchsia',
      getValue: (snapshot) => `${snapshot.engajamento}%`,
      getHelper: (snapshot) => `${snapshot.waitlistPressao}% pressão de fila`,
      getProgress: (snapshot) => snapshot.engajamento
    }
  ];
  private readonly widgetPrefsKey = 'g3.cursos.dashboard.widgets';

  constructor(
    private readonly fb: FormBuilder,
    private readonly service: CursosAtendimentosService,
    private readonly beneficiaryService: BeneficiaryService,
    private readonly salasService: SalasService,
    private readonly professionalService: ProfessionalService,
    private readonly unitService: AssistanceUnitService
  ) {
    this.courseForm = this.fb.group({
      tipo: ['Curso', Validators.required],
      nome: ['', Validators.required],
      descricao: ['', Validators.required],
      imagem: [''],
      vagasTotais: [10, [Validators.required, Validators.min(1)]],
      cargaHoraria: [null],
      horarioInicial: ['', Validators.required],
      duracaoHoras: [1, [Validators.required, Validators.min(1)]],
      diasSemana: this.fb.control<string[]>([], Validators.required),
      restricoes: [''],
      profissional: ['', Validators.required],
      salaId: [null, Validators.required]
    });

    this.enrollmentForm = this.fb.group({
      courseId: [null, Validators.required],
      beneficiaryName: ['', Validators.required],
      cpf: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.setupCapitalizationRules();
    this.loadWidgetPreferences();
    this.fetchRecords();
    this.setupBeneficiarySearch();
    this.setupProfessionalSearch();
    this.unitService.get().subscribe({
      next: ({ unidade }) => {
        this.unidadeAtualId = unidade?.id ?? null;
        this.fetchRooms();
      },
      error: () => {
        this.unidadeAtualId = null;
        this.fetchRooms();
      }
    });
  }

  ngOnDestroy(): void {
    this.beneficiarySearchSub?.unsubscribe();
    this.professionalSearchSub?.unsubscribe();
    this.capitalizationSubs.forEach((sub) => sub.unsubscribe());
  }

  fetchRooms(): void {
    this.roomsLoading = true;
    this.salasService.list(this.unidadeAtualId ?? undefined).subscribe({
      next: (rooms) => {
        this.rooms = rooms;
        this.roomsLoading = false;
      },
      error: () => {
        this.roomsLoading = false;
        this.feedback = 'Nao foi possível carregar as salas. Tente novamente.';
      }
    });
  }

  addRoom(): void {
    if (!this.unidadeAtualId) {
      this.feedback = 'Selecione uma unidade ativa para cadastrar salas.';
      return;
    }
    const nome = window.prompt('Nome da sala');
    if (!nome || !nome.trim()) return;

    this.roomsLoading = true;
    this.salasService.create({ nome: nome.trim(), unidadeId: this.unidadeAtualId }).subscribe({
      next: (room) => {
        this.rooms = [...this.rooms, room].sort((a, b) => a.nome.localeCompare(b.nome));
        this.courseForm.patchValue({ salaId: room.id });
        this.roomsLoading = false;
      },
      error: (error) => {
        this.roomsLoading = false;
        const message = error?.error?.message || 'Nao foi possível salvar a sala.';
        this.feedback = message;
      }
    });
  }

  fetchRecords(): void {
    this.service.list().subscribe({
      next: (records) => {
        this.records = records.map((record) => this.normalizeRecord(record));
        this.refreshDashboardSnapshot();
      },
      error: () => {
        this.feedback = 'Nao foi possível carregar os registros. Tente novamente.';
      }
    });
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

  get currentCourse(): CourseRecord | undefined {
    return this.records.find((course) => course.id === this.editingId);
  }

  changeTab(tab: StepTab['id']): void {
    this.activeTab = tab;
  }

  get visibleWidgets(): DashboardWidget[] {
    const map = new Map(this.dashboardWidgets.map((widget) => [widget.id, widget] as const));
    const hidden = new Set(this.widgetState.hidden);

    return this.normalizeWidgetState()
      .map((id) => map.get(id))
      .filter((widget): widget is DashboardWidget => !!widget && !hidden.has(widget.id));
  }

  isWidgetEnabled(widgetId: string): boolean {
    return !this.widgetState.hidden.includes(widgetId);
  }

  toggleWidget(widgetId: string): void {
    const hidden = new Set(this.widgetState.hidden);
    hidden.has(widgetId) ? hidden.delete(widgetId) : hidden.add(widgetId);
    this.widgetState.hidden = Array.from(hidden);
    this.persistWidgetPreferences();
  }

  moveWidget(widgetId: string, direction: 'up' | 'down'): void {
    const order = this.normalizeWidgetState();
    const currentIndex = order.indexOf(widgetId);
    if (currentIndex < 0) return;

    const targetIndex = direction === 'up' ? currentIndex - 1 : currentIndex + 1;
    if (targetIndex < 0 || targetIndex >= order.length) return;

    [order[currentIndex], order[targetIndex]] = [order[targetIndex], order[currentIndex]];
    this.widgetState.order = order;
    this.persistWidgetPreferences();
  }

  trackWidget(_index: number, widget: DashboardWidget): string {
    return widget.id;
  }

  goToNextTab(): void {
    if (this.hasNextTab) this.changeTab(this.tabs[this.activeTabIndex + 1].id);
  }

  goToPreviousTab(): void {
    if (this.hasPreviousTab) this.changeTab(this.tabs[this.activeTabIndex - 1].id);
  }

  toggleSelection(path: (string | number)[], option: string): void {
    const control = this.courseForm.get(path);
    const current = new Set(control?.value ?? []);
    current.has(option) ? current.delete(option) : current.add(option);
    control?.setValue(Array.from(current));
  }

  selectionChecked(path: (string | number)[], option: string): boolean {
    const control = this.courseForm.get(path);
    return (control?.value as string[] | undefined)?.includes(option) ?? false;
  }

  getRoomName(course: CourseRecord): string {
    return course.sala?.nome ?? this.rooms.find((room) => room.id === course.salaId)?.nome ?? 'Sem sala';
  }

  private hasRoomConflict(
    payload: Pick<CourseRecord, 'salaId' | 'horarioInicial' | 'duracaoHoras' | 'diasSemana'>,
    excludeId?: string | null
  ): boolean {
    if (!payload.salaId) return false;
    const [candidateHour, candidateMinute] = (payload.horarioInicial || '00:00').split(':').map(Number);
    const candidateStart = candidateHour * 60 + (candidateMinute || 0);
    const candidateEnd = candidateStart + Number(payload.duracaoHoras ?? 0) * 60;
    const candidateDays = new Set(payload.diasSemana || []);

    return this.records.some((record) => {
      if (record.id === excludeId) return false;
      if (!record.salaId || record.salaId !== payload.salaId) return false;
      const sharesDay = record.diasSemana.some((dia) => candidateDays.has(dia));
      if (!sharesDay) return false;
      const [hour, minute] = record.horarioInicial.split(':').map(Number);
      const start = hour * 60 + (minute || 0);
      const end = start + record.duracaoHoras * 60;
      return candidateStart < end && start < candidateEnd;
    });
  }

  submit(): void {
    this.feedback = null;
    if (this.courseForm.invalid) {
      this.courseForm.markAllAsTouched();
      this.feedback = 'Preencha os campos obrigatórios antes de salvar.';
      return;
    }

    if (this.hasRoomConflict(this.courseForm.value, this.editingId)) {
      this.feedback = 'ja existe um curso/atendimento na mesma sala e horário.';
      return;
    }

    const payload = this.courseForm.value;
    this.saving = true;

    if (this.editingId) {
      this.service
        .update(this.editingId, {
          ...payload,
          diasSemana: payload.diasSemana ?? [],
          enrollments: this.currentCourse?.enrollments ?? [],
          waitlist: this.currentCourse?.waitlist ?? []
        })
        .subscribe({
          next: (updated) => {
            this.replaceRecord(updated);
            this.feedback = 'Cadastro salvo com sucesso!';
            this.saving = false;
          },
          error: () => {
            this.feedback = 'Nao foi possível salvar o cadastro. Tente novamente.';
            this.saving = false;
          }
        });
    } else {
      this.service
        .create({
          ...payload,
          diasSemana: payload.diasSemana ?? [],
          enrollments: [],
          waitlist: []
        })
        .subscribe({
          next: (created) => {
            this.records = [this.normalizeRecord(created), ...this.records];
            this.editingId = created.id;
            this.enrollmentForm.patchValue({ courseId: created.id });
            this.feedback = 'Cadastro salvo com sucesso!';
            this.saving = false;
            this.refreshDashboardSnapshot();
          },
          error: () => {
            this.feedback = 'Nao foi possível salvar o cadastro. Tente novamente.';
            this.saving = false;
          }
        });
    }
  }

  dismissFeedback(): void {
    this.feedback = null;
  }

  loadCourse(courseId: string): void {
    const course = this.records.find((item) => item.id === courseId);
    if (!course) return;

    this.editingId = course.id;
    this.courseForm.reset({
      tipo: course.tipo,
      nome: course.nome,
      descricao: course.descricao,
      imagem: course.imagem ?? '',
      vagasTotais: course.vagasTotais,
      cargaHoraria: course.cargaHoraria ?? null,
      horarioInicial: course.horarioInicial,
      duracaoHoras: course.duracaoHoras,
      diasSemana: course.diasSemana,
      restricoes: course.restricoes ?? '',
      profissional: course.profissional,
      salaId: course.salaId ?? course.sala?.id ?? null
    });
    this.enrollmentForm.patchValue({ courseId: course.id });
  }

  startNew(): void {
    this.editingId = null;
    this.courseForm.reset({
      tipo: 'Curso',
      nome: '',
      descricao: '',
      imagem: '',
      vagasTotais: 10,
      cargaHoraria: null,
      horarioInicial: '',
      duracaoHoras: 1,
      diasSemana: [],
      restricoes: '',
      profissional: '',
      salaId: null
    });
    this.enrollmentForm.patchValue({ courseId: null });
    this.changeTab('dados');
  }

  getCardAccent(course: CourseRecord): string {
    switch (course.tipo) {
      case 'Atendimento':
        return 'accent-sky';
      case 'Oficina':
        return 'accent-violet';
      default:
        return 'accent-emerald';
    }
  }

  deleteCourse(course: CourseRecord): void {
    if (!window.confirm(`Remover ${course.tipo.toLowerCase()} "${course.nome}"?`)) return;

    this.saving = true;
    this.service.delete(course.id).subscribe({
      next: () => {
        this.records = this.records.filter((item) => item.id !== course.id);

        if (this.editingId === course.id) {
          if (this.records.length) {
            this.loadCourse(this.records[0].id);
          } else {
            this.startNew();
          }
        }

        this.refreshDashboardSnapshot();
        this.saving = false;
      },
      error: () => {
        this.feedback = 'Nao foi possível excluir o cadastro. Tente novamente.';
        this.saving = false;
      }
    });
  }

  private setupCapitalizationRules(): void {
    const applyRule = (form: FormGroup, controlName: string) => {
      const control = form.get(controlName);
      if (!control) return;

      const sub = control.valueChanges.subscribe((value) => {
        if (typeof value !== 'string') return;
        this.applyCapitalization(form, controlName);
      });

      this.capitalizationSubs.push(sub);
    };

    ['nome', 'profissional'].forEach((controlName) => applyRule(this.courseForm, controlName));
    applyRule(this.enrollmentForm, 'beneficiaryName');
  }

  applyCapitalization(form: FormGroup, controlName: string, rawValue?: string): void {
    const control = form.get(controlName);
    if (!control) return;
    if (controlName.toLowerCase().includes('descricao')) return;

    const currentValue = rawValue ?? control.value;
    if (typeof currentValue !== 'string') return;

    const transformed = titleCaseWords(currentValue);

    if (transformed && transformed !== currentValue) {
      control.setValue(transformed, { emitEvent: false });
    }
  }

  handleImage(event: Event): void {
    const file = (event.target as HTMLInputElement)?.files?.[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => this.courseForm.patchValue({ imagem: reader.result as string });
    reader.readAsDataURL(file);
  }

  enroll(): void {
    this.feedback = null;
    if (!this.currentCourse) {
      this.feedback = 'Selecione um curso/atendimento para gerenciar as inscrições.';
      return;
    }

    if (this.enrollmentForm.invalid) {
      this.enrollmentForm.markAllAsTouched();
      if (!this.enrollmentForm.value.courseId) {
        this.feedback = 'Selecione o curso/atendimento antes de inscrever.';
      }
      return;
    }

    const { beneficiaryName, cpf } = this.enrollmentForm.value;
    const normalizedCpf = (cpf || '').replace(/\D/g, '');
    const alreadyRegistered = this.currentCourse.enrollments.some(
      (enrollment) =>
        (normalizedCpf && (enrollment.cpf || '').replace(/\D/g, '') === normalizedCpf) ||
        enrollment.beneficiaryName.toLowerCase() === beneficiaryName.toLowerCase()
    );
    const alreadyOnWaitlist = this.currentCourse.waitlist.some(
      (entry) =>
        (normalizedCpf && (entry.cpf || '').replace(/\D/g, '') === normalizedCpf) ||
        entry.beneficiaryName.toLowerCase() === beneficiaryName.toLowerCase()
    );

    if (alreadyRegistered || alreadyOnWaitlist) {
      this.feedback = 'Este beneficiario ja está inscrito ou aguardando neste curso/atendimento.';
      return;
    }

    if (this.currentCourse.vagasDisponiveis > 0) {
      const enrollment: Enrollment = {
        id: generateId(),
        beneficiaryName,
        cpf,
        status: 'Ativo',
        enrolledAt: new Date().toISOString()
      };
      this.currentCourse.enrollments.push(enrollment);
      this.currentCourse.vagasDisponiveis = this.calculateAvailable(this.currentCourse);
      this.persistCurrentCourse();
      this.enrollmentForm.reset({ courseId: this.currentCourse.id, beneficiaryName: '', cpf: '' });
    } else {
      const confirmWaitlist = window.confirm(
        'Nao há vagas disponiveis. Deseja incluir o beneficiario na lista de espera?'
      );
      if (confirmWaitlist) {
        const entry: WaitlistEntry = {
          id: generateId(),
          beneficiaryName,
          cpf,
          joinedAt: new Date().toISOString()
        };
        this.currentCourse.waitlist.push(entry);
        this.persistCurrentCourse();
        this.enrollmentForm.reset({ courseId: this.currentCourse.id, beneficiaryName: '', cpf: '' });
      }
    }
  }

  updateEnrollmentStatus(enrollment: Enrollment, status: EnrollmentStatus): void {
    if (!this.currentCourse) return;

    const previous = enrollment.status;
    enrollment.status = status;

    if (previous === 'Ativo' && status !== 'Ativo') {
      this.currentCourse.vagasDisponiveis = Math.min(
        this.currentCourse.vagasTotais,
        this.currentCourse.vagasDisponiveis + 1
      );
    }

    if (status === 'Ativo') {
      if (this.currentCourse.vagasDisponiveis > 0) {
        this.currentCourse.vagasDisponiveis -= 1;
      } else {
        enrollment.status = previous;
        return;
      }
    }

    this.tryPromoteWaitlist();
    this.persistCurrentCourse();
  }

  removeFromWaitlist(entry: WaitlistEntry, course?: CourseRecord): void {
    const targetCourse = course ?? this.currentCourse;
    if (!targetCourse) return;
    targetCourse.waitlist = targetCourse.waitlist.filter((item) => item.id !== entry.id);
    this.persistCourse(targetCourse);
  }

  tryPromoteWaitlist(): void {
    if (!this.currentCourse) return;
    if (this.currentCourse.vagasDisponiveis <= 0) return;
    if (this.currentCourse.waitlist.length === 0) return;

    const [first, ...rest] = this.currentCourse.waitlist;
    const enrollment: Enrollment = {
      id: generateId(),
      beneficiaryName: first.beneficiaryName,
      cpf: first.cpf,
      status: 'Ativo',
      enrolledAt: new Date().toISOString()
    };
    this.currentCourse.waitlist = rest;
    this.currentCourse.enrollments.push(enrollment);
    this.currentCourse.vagasDisponiveis = this.calculateAvailable(this.currentCourse);
    this.persistCurrentCourse();
  }

  convertWaitlist(entry: WaitlistEntry): void {
    if (!this.currentCourse || this.currentCourse.vagasDisponiveis <= 0) return;

    this.currentCourse.waitlist = this.currentCourse.waitlist.filter((item) => item.id !== entry.id);
    const enrollment: Enrollment = {
      id: generateId(),
      beneficiaryName: entry.beneficiaryName,
      cpf: entry.cpf,
      status: 'Ativo',
      enrolledAt: new Date().toISOString()
    };
    this.currentCourse.enrollments.push(enrollment);
    this.currentCourse.vagasDisponiveis = this.calculateAvailable(this.currentCourse);
    this.persistCurrentCourse();
  }

  calculateAvailable(course: CourseRecord): number {
    const active = course.enrollments.filter((e) => e.status === 'Ativo').length;
    return Math.max(course.vagasTotais - active, 0);
  }

  dashboardTotals() {
    const ativosCurso = this.records.filter((c) => c.tipo === 'Curso').length;
    const ativosAtendimento = this.records.filter((c) => c.tipo === 'Atendimento').length;
    const snapshot = this.dashboardSnapshot;

    return {
      ativosCurso,
      ativosAtendimento,
      totalMatriculas: snapshot.totalMatriculas,
      totalVagas: snapshot.totalVagas,
      vagasDisponiveis: snapshot.vagasDisponiveis,
      listaEspera: snapshot.waitlist
    };
  }

  fillFromCourse(course: CourseRecord): void {
    this.loadCourse(course.id);
    this.changeTab('dados');
  }

  selectCourseForEnrollment(courseId: string): void {
    if (!courseId) {
      this.editingId = null;
      return;
    }
    this.loadCourse(courseId);
  }

  selectFromCatalog(courseId: string): void {
    this.loadCourse(courseId);
    this.changeTab('inscricoes');
  }

  selectBeneficiary(beneficiary: BeneficiaryPayload): void {
    this.enrollmentForm.patchValue({
      beneficiaryName: beneficiary.nomeCompleto || beneficiary.nomeSocial || '',
      cpf: beneficiary.cpf || ''
    });
    this.beneficiaryResults = [];
  }

  selectProfessional(professional: ProfessionalRecord): void {
    this.courseForm.patchValue({ profissional: professional.nomeCompleto });
    this.professionalResults = [];
  }

  courseStatus(course: CourseRecord): { label: string; tone: 'green' | 'amber' | 'red' } {
    if (course.vagasDisponiveis <= 0) return { label: 'Esgotado', tone: 'red' };
    if (course.vagasDisponiveis <= Math.max(1, Math.floor(course.vagasTotais * 0.3))) {
      return { label: 'Encerrando', tone: 'amber' };
    }
    return { label: 'Abertas', tone: 'green' };
  }

  private setupBeneficiarySearch(): void {
    const beneficiaryControl = this.enrollmentForm.get('beneficiaryName');
    if (!beneficiaryControl) return;

    this.beneficiarySearchSub = beneficiaryControl.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        tap((term) => {
          if (!term) {
            this.beneficiaryResults = [];
            this.beneficiarySearchLoading = false;
          } else {
            this.beneficiarySearchLoading = true;
          }
        }),
        switchMap((term) => {
          if (!term) return of({ beneficiarios: [] });
          return this.beneficiaryService
            .list({ nome: term })
            .pipe(catchError(() => of({ beneficiarios: [] })));
        })
      )
      .subscribe(({ beneficiarios }) => {
        this.beneficiaryResults = beneficiarios.slice(0, 5);
        this.beneficiarySearchLoading = false;
      });
  }

  private setupProfessionalSearch(): void {
    const professionalControl = this.courseForm.get('profissional');
    if (!professionalControl) return;

    this.professionalSearchSub = professionalControl.valueChanges
      .pipe(
        debounceTime(250),
        distinctUntilChanged(),
        tap((term) => {
          if (!term) {
            this.professionalResults = [];
            this.professionalSearchLoading = false;
          } else {
            this.professionalSearchLoading = true;
          }
        })
      )
      .subscribe((term) => {
        if (!term) return;
        this.professionalResults = this.professionalService.search(term);
        this.professionalSearchLoading = false;
      });
  }

  private loadWidgetPreferences(): void {
    try {
      const saved = typeof localStorage !== 'undefined' ? localStorage.getItem(this.widgetPrefsKey) : null;
      if (saved) {
        this.widgetState = { ...this.widgetState, ...(JSON.parse(saved) as WidgetState) };
      }
    } catch (error) {
      console.warn('Nao foi possível carregar as preferências do dashboard', error);
    }

    this.normalizeWidgetState();
  }

  private persistWidgetPreferences(): void {
    try {
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem(this.widgetPrefsKey, JSON.stringify(this.widgetState));
      }
    } catch (error) {
      console.warn('Nao foi possível salvar as preferências do dashboard', error);
    }
  }

  private normalizeWidgetState(): string[] {
    const availableIds = this.dashboardWidgets.map((widget) => widget.id);
    const order = this.widgetState.order.filter((id) => availableIds.includes(id));
    availableIds.forEach((id) => {
      if (!order.includes(id)) order.push(id);
    });

    const hidden = this.widgetState.hidden.filter((id) => availableIds.includes(id));
    this.widgetState = { order, hidden };
    return order;
  }

  private buildDashboardSnapshot(): DashboardSnapshot {
    const totalVagas = this.records.reduce((sum, c) => sum + c.vagasTotais, 0);
    const vagasDisponiveis = this.records.reduce((sum, c) => sum + c.vagasDisponiveis, 0);
    const vagasEmUso = Math.max(totalVagas - vagasDisponiveis, 0);
    const totalMatriculas = this.records.reduce(
      (sum, c) => sum + c.enrollments.filter((e) => e.status === 'Ativo').length,
      0
    );
    const totalInscricoes = this.records.reduce((sum, c) => sum + c.enrollments.length, 0);
    const concluidos = this.records.reduce(
      (sum, c) => sum + c.enrollments.filter((e) => e.status === 'Concluído').length,
      0
    );
    const cancelados = this.records.reduce(
      (sum, c) => sum + c.enrollments.filter((e) => e.status === 'Cancelado').length,
      0
    );
    const waitlist = this.records.reduce((sum, c) => sum + c.waitlist.length, 0);

    const cursos = this.records.filter((c) => c.tipo === 'Curso').length;
    const atendimentos = this.records.filter((c) => c.tipo === 'Atendimento').length;
    const oficinas = this.records.filter((c) => c.tipo === 'Oficina').length;
    const profissionais = new Set(this.records.map((c) => c.profissional).filter(Boolean)).size;
    const mediaCargaHoraria = this.records.length
      ? Math.round(
          this.records.reduce((sum, c) => sum + (c.cargaHoraria ?? 0), 0) / this.records.length
        )
      : 0;

    const ocupacao = totalVagas ? Math.round((vagasEmUso / totalVagas) * 100) : 0;
    const baseConclusao = totalMatriculas + concluidos + cancelados;
    const taxaConclusao = baseConclusao ? Math.round((concluidos / baseConclusao) * 100) : 0;
    const waitlistPressao = totalMatriculas
      ? Math.min(100, Math.round((waitlist / totalMatriculas) * 100))
      : 0;
    const engajamento = Math.min(
      100,
      Math.round((ocupacao * 0.4 + (100 - waitlistPressao) * 0.25 + taxaConclusao * 0.35))
    );

    return {
      totalVagas,
      vagasDisponiveis,
      vagasEmUso,
      ocupacao,
      totalMatriculas,
      totalInscricoes,
      concluidos,
      cancelados,
      waitlist,
      waitlistPressao,
      cursos,
      atendimentos,
      oficinas,
      profissionais,
      mediaCargaHoraria,
      taxaConclusao,
      engajamento
    };
  }

  private refreshDashboardSnapshot(): void {
    this.dashboardSnapshot = this.buildDashboardSnapshot();
  }

  private normalizeRecord(record: CourseRecord): CourseRecord {
    return {
      ...record,
      imagem: record.imagem ?? null,
      restricoes: record.restricoes ?? null,
      diasSemana: record.diasSemana ?? [],
      salaId: record.salaId ?? record.sala?.id ?? null,
      enrollments: (record.enrollments ?? []).map((enrollment) => ({
        ...enrollment,
        enrolledAt: enrollment.enrolledAt ?? new Date().toISOString()
      })),
      waitlist: (record.waitlist ?? []).map((entry) => ({
        ...entry,
        joinedAt: entry.joinedAt ?? new Date().toISOString()
      }))
    };
  }

  private replaceRecord(updated: CourseRecord): void {
    const normalized = this.normalizeRecord(updated);
    this.records = this.records.map((record) => (record.id === normalized.id ? normalized : record));
    this.refreshDashboardSnapshot();
    if (!this.editingId) this.editingId = normalized.id;
    if (this.editingId === normalized.id) {
      this.loadCourse(normalized.id);
    }
  }

  private persistCourse(course: CourseRecord): void {
    this.service.update(course.id, course).subscribe({
      next: (record) => this.replaceRecord(record),
      error: () => {
        this.feedback = 'Nao foi possível salvar as alterações. Tente novamente.';
      }
    });
  }

  private persistCurrentCourse(): void {
    if (!this.currentCourse) return;
    this.persistCourse(this.currentCourse);
  }
}

