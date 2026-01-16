import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import {
  CourseRecord,
  CursosAtendimentosService,
  Enrollment,
  EnrollmentStatus,
  PresencaResponse,
  PresencaStatus,
  WaitlistEntry,
} from '../../services/cursos-atendimentos.service';
import { BeneficiaryPayload } from '../../services/beneficiary.service';
import {
  BeneficiarioApiPayload,
  BeneficiarioApiService,
} from '../../services/beneficiario-api.service';
import { SalaRecord, SalasService } from '../../services/salas.service';
import { AssistanceUnitService } from '../../services/assistance-unit.service';
import { ProfessionalRecord, ProfessionalService } from '../../services/professional.service';
import { catchError, debounceTime, distinctUntilChanged, finalize, firstValueFrom, of, Subscription, switchMap, tap } from 'rxjs';
import { titleCaseWords } from '../../utils/capitalization.util';

import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { DialogComponent } from '../compartilhado/dialog/dialog.component';
import { ConfigAcoesCrud, EstadoAcoesCrud, TelaBaseComponent } from '../compartilhado/tela-base.component';
import { ReportService } from '../../services/report.service';
import { AuthService } from '../../services/auth.service';
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
  imports: [CommonModule, ReactiveFormsModule, FormsModule, TelaPadraoComponent, DialogComponent],
  templateUrl: './cursos-atendimentos.component.html',
  styleUrl: './cursos-atendimentos.component.scss'
})
export class CursosAtendimentosComponent extends TelaBaseComponent implements OnInit, OnDestroy {
  readonly tabs: StepTab[] = [
    { id: 'dashboard', label: 'Dashboard' },
    { id: 'dados', label: 'Dados de Matriculas' },
    { id: 'catalogo', label: 'Catalogo e Vagas' },
    { id: 'inscricoes', label: 'Inscricoes e Lista de Espera' },
    { id: 'presenca', label: 'Presenca' },
    { id: 'listagem', label: 'Listagem de matriculas' }
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

  readonly faixasEtarias = [
    '0 a 5 anos',
    '6 a 11 anos',
    '12 a 17 anos',
    '18 a 29 anos',
    '30 a 59 anos',
    '60 anos ou mais'
  ];

  activeTab: StepTab['id'] = 'dashboard';
  feedback: string | null = null;
  printDialogOpen = false;
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
  dataPresencaSelecionada = '';
  presencaCarregando = false;
  presencaSalvando = false;
  private presencasPorMatricula: Record<string, PresencaStatus> = {};

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

  readonly acoesToolbar: Required<ConfigAcoesCrud> = this.criarConfigAcoes({
    buscar: true,
    novo: true,
    salvar: true,
    cancelar: true,
    excluir: true,
    imprimir: true
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly service: CursosAtendimentosService,
    private readonly beneficiarioApiService: BeneficiarioApiService,
    private readonly salasService: SalasService,
    private readonly professionalService: ProfessionalService,
    private readonly unitService: AssistanceUnitService,
    private readonly reportService: ReportService,
    private readonly authService: AuthService
  ) {
    super();
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
      faixasEtarias: this.fb.control<string[]>([]),
      vagaPreferencialIdosos: [false],
      sexoPermitido: ['Todos'],
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
  get estadoAcoesToolbar(): EstadoAcoesCrud {
    return {
      excluir: !this.editingId,
      imprimir: true
    };
  }

  get activeTabIndex(): number {
    return Math.max(
      0,
      this.tabs.findIndex((tab) => tab.id === this.activeTab)
    );
  }

  get hasPreviousTab(): boolean {
    return this.activeTabIndex > 0;
  }

  get hasNextTab(): boolean {
    return this.activeTabIndex < this.tabs.length - 1;
  }

  get nextTabLabel(): string {
    if (!this.hasNextTab) return '';
    return this.tabs[this.activeTabIndex + 1].label;
  }

  get currentCourse(): CourseRecord | null {
    const courseId = this.editingId ?? this.enrollmentForm.value.courseId;
    if (!courseId) return null;
    return this.records.find((record) => record.id === courseId) ?? null;
  }

  get matriculasAtivas(): Enrollment[] {
    return (this.currentCourse?.enrollments ?? []).filter((matricula) => matricula.status === 'Ativo');
  }

  get visibleWidgets(): DashboardWidget[] {
    const order = this.normalizeWidgetState();
    return order
      .map((id) => this.dashboardWidgets.find((widget) => widget.id === id))
      .filter((widget): widget is DashboardWidget => Boolean(widget))
      .filter((widget) => !this.widgetState.hidden.includes(widget.id));
  }

  trackWidget(_: number, widget: DashboardWidget): string {
    return widget.id;
  }

  isWidgetEnabled(id: string): boolean {
    return !this.widgetState.hidden.includes(id);
  }

  toggleWidget(id: string): void {
    const hidden = new Set(this.widgetState.hidden);
    if (hidden.has(id)) {
      hidden.delete(id);
    } else {
      hidden.add(id);
    }
    this.widgetState = { ...this.widgetState, hidden: Array.from(hidden) };
    this.persistWidgetPreferences();
  }

  moveWidget(id: string, direction: 'up' | 'down'): void {
    const order = [...this.normalizeWidgetState()];
    const index = order.indexOf(id);
    if (index < 0) return;

    const targetIndex = direction === 'up' ? index - 1 : index + 1;
    if (targetIndex < 0 || targetIndex >= order.length) return;

    const [removed] = order.splice(index, 1);
    order.splice(targetIndex, 0, removed);
    this.widgetState = { ...this.widgetState, order };
    this.persistWidgetPreferences();
  }

  changeTab(tabId: StepTab['id']): void {
    if (this.activeTab === tabId) return;
    this.activeTab = tabId;
  }

  goToPreviousTab(): void {
    if (!this.hasPreviousTab) return;
    this.changeTab(this.tabs[this.activeTabIndex - 1].id);
  }

  goToNextTab(): void {
    if (!this.hasNextTab) return;
    this.changeTab(this.tabs[this.activeTabIndex + 1].id);
  }

  dismissFeedback(): void {
    this.feedback = null;
  }

  selectionChecked(path: string[], value: string): boolean {
    const control = this.courseForm.get(path);
    const current = control?.value as string[] | null;
    return Array.isArray(current) && current.includes(value);
  }

  toggleSelection(path: string[], value: string): void {
    const control = this.courseForm.get(path);
    if (!control) return;
    const current = (control.value as string[] | null) ?? [];
    const updated = current.includes(value)
      ? current.filter((item) => item !== value)
      : [...current, value];
    control.setValue(updated);
    control.markAsDirty();
  }

  getRoomName(course: CourseRecord): string {
    const roomId = course.salaId ?? course.sala?.id;
    if (roomId) {
      const room = this.rooms.find((item) => item.id === roomId);
      if (room) return room.nome;
    }
    return course.sala?.nome ?? 'Nao informado';
  }

  submit(): void {
    this.feedback = null;
    if (this.courseForm.invalid) {
      this.courseForm.markAllAsTouched();
      this.feedback = 'Preencha os campos obrigatorios antes de salvar.';
      this.changeTab('dados');
      return;
    }

    const formValue = this.courseForm.getRawValue();
    const basePayload = {
      ...formValue,
      diasSemana: formValue.diasSemana ?? [],
      imagem: formValue.imagem || null,
      restricoes: formValue.restricoes || null,
      salaId: formValue.salaId || null
    };
    const existing = this.currentCourse;
    const payload = {
      ...basePayload,
      enrollments: existing?.enrollments ?? [],
      waitlist: existing?.waitlist ?? [],
      vagasDisponiveis: existing?.vagasDisponiveis ?? basePayload.vagasTotais
    };

    this.saving = true;
    const request$ = this.editingId
      ? this.service.update(this.editingId, payload)
      : this.service.create(payload);

    request$.subscribe({
      next: (record) => {
        const normalized = this.normalizeRecord(record);
        if (this.editingId) {
          this.replaceRecord(normalized);
        } else {
          this.records = [normalized, ...this.records];
          this.refreshDashboardSnapshot();
          this.loadCourse(normalized.id);
        }
        this.saving = false;
      },
      error: () => {
        this.feedback = 'Nao foi possivel salvar o cadastro. Tente novamente.';
        this.saving = false;
      }
    });
  }

  fetchRecords(): void {
    this.feedback = null;
    this.service.list().subscribe({
      next: (records) => {
        this.records = records.map((record) => this.normalizeRecord(record));
        this.refreshDashboardSnapshot();
        if (this.editingId) {
          this.loadCourse(this.editingId);
        }
      },
      error: () => {
        this.feedback = 'Nao foi possivel carregar os registros. Tente novamente.';
      }
    });
  }

  onBuscar(): void {
    this.changeTab('catalogo');
  }

  onCancelar(): void {
    this.startNew();
  }

  onExcluir(): void {
    if (!this.currentCourse) return;
    this.deleteCourse(this.currentCourse);
  }

  onImprimir(): void {
    this.printDialogOpen = true;
  }

  closePrintDialog(): void {
    this.printDialogOpen = false;
  }

  async imprimirRelacao(): Promise<void> {
    this.printDialogOpen = false;
    try {
      const blob = await firstValueFrom(
        this.reportService.generateCursosAtendimentosList({
          usuarioEmissor: this.usuarioEmissor()
        })
      );
      this.openPdfInNewWindow(blob);
    } catch (error) {
      console.error('Erro ao gerar relacao de cursos e atendimentos', error);
      this.feedback = 'Falha ao gerar a relacao de cursos e atendimentos.';
    }
  }

  async imprimirFichaSelecionada(): Promise<void> {
    const cursoId = this.currentCourse?.id;
    if (!cursoId) {
      this.feedback = 'Selecione um curso/atendimento antes de gerar a ficha.';
      this.printDialogOpen = false;
      return;
    }
    this.printDialogOpen = false;
    try {
      const blob = await firstValueFrom(
        this.reportService.generateCursoAtendimentoFicha({
          cursoId: String(cursoId),
          usuarioEmissor: this.usuarioEmissor()
        })
      );
      this.openPdfInNewWindow(blob);
    } catch (error) {
      console.error('Erro ao gerar ficha do curso/atendimento', error);
      this.feedback = 'Falha ao gerar a ficha do curso/atendimento.';
    }
  }

  private usuarioEmissor(): string {
    return (
      this.authService.user()?.nome ||
      this.authService.user()?.nomeUsuario ||
      'Sistema'
    );
  }

  private openPdfInNewWindow(blob: Blob): void {
    const url = URL.createObjectURL(blob);
    const documentWindow = window.open(url, '_blank', 'width=900,height=1100');
    if (!documentWindow) {
      this.feedback = 'Permita a abertura de pop-ups para visualizar o relatorio.';
      URL.revokeObjectURL(url);
      return;
    }

    const triggerPrint = () => {
      documentWindow.focus();
      documentWindow.print();
    };

    documentWindow.addEventListener('load', triggerPrint, { once: true });
    setTimeout(() => URL.revokeObjectURL(url), 60_000);
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
      faixasEtarias: course.faixasEtarias ?? [],
      vagaPreferencialIdosos: course.vagaPreferencialIdosos ?? false,
      sexoPermitido: course.sexoPermitido ?? 'Todos',
      restricoes: course.restricoes ?? '',
      profissional: course.profissional,
      salaId: course.salaId ?? course.sala?.id ?? null
    });
    this.enrollmentForm.patchValue({ courseId: course.id });
    this.carregarPresencas();
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
      faixasEtarias: [],
      vagaPreferencialIdosos: false,
      sexoPermitido: 'Todos',
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

  atualizarDataPresenca(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.dataPresencaSelecionada = input.value;
    this.carregarPresencas();
  }

  atualizarPresenca(matricula: Enrollment, status: PresencaStatus): void {
    const curso = this.currentCourse;
    if (!curso || !this.dataPresencaSelecionada) return;
    const atual = this.presencasPorMatricula[matricula.id];
    this.presencasPorMatricula[matricula.id] = atual === status ? 'AUSENTE' : status;
    this.salvarPresencas();
  }

  obterPresenca(matricula: Enrollment): PresencaStatus {
    return this.presencasPorMatricula[matricula.id] ?? 'AUSENTE';
  }

  imprimirListaPresenca(): void {
    this.feedback = null;
    const curso = this.currentCourse;
    if (!curso) {
      this.feedback = 'Selecione um curso/atendimento para gerar a lista de presenca.';
      return;
    }
    if (!this.dataPresencaSelecionada) {
      this.feedback = 'Informe a data da aula para imprimir a lista de presenca.';
      return;
    }
    const linhas = this.matriculasAtivas
      .map((matricula, index) => {
        const status = this.obterPresenca(matricula);
        const presente = status === 'PRESENTE' ? 'Presente' : 'Ausente';
        return `
          <tr>
            <td>${index + 1}</td>
            <td>${matricula.beneficiaryName}</td>
            <td>${matricula.cpf || ''}</td>
            <td>${presente}</td>
            <td></td>
          </tr>
        `;
      })
      .join('');

    const janela = window.open('', '_blank', 'width=900,height=1100');
    if (!janela) {
      this.feedback = 'Permita a abertura de pop-ups para visualizar a impressao.';
      return;
    }

    janela.document.write(`
      <!doctype html>
      <html lang="pt-BR">
        <head>
          <meta charset="utf-8" />
          <title>Lista de presenca</title>
          <style>
            @page { size: A4; margin: 20mm; }
            body { font-family: Arial, sans-serif; color: #111827; }
            header { margin-bottom: 12px; }
            h1 { font-size: 18px; margin: 0 0 4px; }
            .subtitulo { font-size: 12px; margin: 0 0 6px; }
            .linha-info { font-size: 12px; margin: 0; color: #374151; }
            table { width: 100%; border-collapse: collapse; margin-top: 12px; }
            th, td { border: 1px solid #d1d5db; padding: 6px 8px; font-size: 12px; }
            th { background: #f3f4f6; text-align: left; }
            thead { display: table-header-group; }
            tfoot { display: table-footer-group; }
            footer {
              position: fixed;
              bottom: 0;
              left: 0;
              right: 0;
              font-size: 11px;
              color: #6b7280;
              display: flex;
              justify-content: space-between;
            }
            footer .pagina:after {
              content: counter(page) " de " counter(pages);
            }
          </style>
        </head>
        <body>
          <header>
            <div class="linha-info">G3 Assistencial</div>
            <h1>Lista de presenca</h1>
            <p class="subtitulo">Curso/Atendimento: ${curso.nome}</p>
            <p class="linha-info">Data da aula: ${this.dataPresencaSelecionada}</p>
          </header>
          <main>
            <table>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Beneficiario</th>
                  <th>CPF</th>
                  <th>Status</th>
                  <th>Assinatura</th>
                </tr>
              </thead>
              <tbody>
                ${linhas || '<tr><td colspan="5">Nenhuma matricula ativa.</td></tr>'}
              </tbody>
            </table>
          </main>
          <footer>
            <span>G3 Assistencial</span>
            <span>Pagina <span class="pagina"></span></span>
          </footer>
        </body>
      </html>
    `);
    janela.document.close();
    janela.focus();
    janela.print();
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

  private carregarPresencas(): void {
    const curso = this.currentCourse;
    if (!curso || !this.dataPresencaSelecionada) return;
    this.presencaCarregando = true;
    this.service
      .listarPresencas(curso.id, this.dataPresencaSelecionada)
      .pipe(finalize(() => (this.presencaCarregando = false)))
      .subscribe({
        next: (response) => {
          this.presencasPorMatricula = {};
          response.presencas.forEach((presenca) => {
            this.presencasPorMatricula[presenca.matriculaId] = presenca.status;
          });
          this.matriculasAtivas.forEach((matricula) => {
            if (!this.presencasPorMatricula[matricula.id]) {
              this.presencasPorMatricula[matricula.id] = 'AUSENTE';
            }
          });
        },
        error: () => {
          this.feedback = 'Nao foi possivel carregar a presenca desta data.';
        },
      });
  }

  salvarPresencas(): void {
    const curso = this.currentCourse;
    if (!curso || !this.dataPresencaSelecionada) return;
    const payload = this.montarPayloadPresenca();
    this.presencaSalvando = true;
    this.service
      .salvarPresencas(curso.id, payload)
      .pipe(finalize(() => (this.presencaSalvando = false)))
      .subscribe({
        next: (response) => {
          this.presencasPorMatricula = {};
          response.presencas.forEach((presenca) => {
            this.presencasPorMatricula[presenca.matriculaId] = presenca.status;
          });
        },
        error: () => {
          this.feedback = 'Nao foi possivel salvar a presenca.';
        },
      });
  }

  private montarPayloadPresenca(): PresencaResponse {
    const presencas = this.matriculasAtivas.map((matricula) => ({
      matriculaId: matricula.id,
      status: this.obterPresenca(matricula),
    }));
    return {
      dataAula: this.dataPresencaSelecionada,
      presencas,
    };
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
          return this.beneficiarioApiService
            .list({ nome: term })
            .pipe(catchError(() => of({ beneficiarios: [] })));
        })
      )
      .subscribe(({ beneficiarios }) => {
        this.beneficiaryResults = beneficiarios
          .map((beneficiario) => this.mapearBeneficiarioParaBusca(beneficiario))
          .slice(0, 5);
        this.beneficiarySearchLoading = false;
      });
  }

  private mapearBeneficiarioParaBusca(beneficiario: BeneficiarioApiPayload): BeneficiaryPayload {
    return {
      id: beneficiario.id_beneficiario ? Number(beneficiario.id_beneficiario) : undefined,
      nomeCompleto: beneficiario.nome_completo || '',
      nomeSocial: beneficiario.nome_social,
      cpf: beneficiario.cpf || undefined,
      cep: beneficiario.cep || '',
      documentos: '',
      dataNascimento: beneficiario.data_nascimento || '',
      email: '',
      documentosAnexos: []
    };
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
        }),
        switchMap((term) => {
          if (!term) return of([]);
          return this.professionalService.list(term).pipe(catchError(() => of([])));
        })
      )
      .subscribe((records) => {
        this.professionalResults = (records ?? []).slice(0, 8);
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
      faixasEtarias: record.faixasEtarias ?? [],
      vagaPreferencialIdosos: record.vagaPreferencialIdosos ?? false,
      sexoPermitido: record.sexoPermitido ?? 'Todos',
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




