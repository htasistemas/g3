import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import {
  CourseRecord,
  CursosAtendimentosService,
  Enrollment,
  Enrollment as EnrollmentRecord,
  EnrollmentStatus,
  WaitlistEntry
} from '../../services/cursos-atendimentos.service';

interface StepTab {
  id: string;
  label: string;
}

type CourseType = 'Curso' | 'Atendimento' | 'Oficina';

@Component({
  selector: 'app-cursos-atendimentos',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './cursos-atendimentos.component.html',
  styleUrl: './cursos-atendimentos.component.scss'
})
export class CursosAtendimentosComponent implements OnInit {
  readonly tabs: StepTab[] = [
    { id: 'dados', label: 'Dados do Curso/Atendimento/Oficinas' },
    { id: 'inscricoes', label: 'Inscrições & Lista de Espera' },
    { id: 'documentos', label: 'Certificados & Atestados' },
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

  courseForm: FormGroup;
  enrollmentForm: FormGroup;

  records: CourseRecord[] = [];

  constructor(
    private readonly fb: FormBuilder,
    private readonly service: CursosAtendimentosService
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
      profissional: ['', Validators.required]
    });

    this.enrollmentForm = this.fb.group({
      beneficiaryName: ['', Validators.required],
      cpf: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.fetchRecords();
  }

  fetchRecords(): void {
    this.service.list().subscribe({
      next: (records) => {
        this.records = records.map((record) => this.normalizeRecord(record));
        if (this.records.length) {
          this.loadCourse(this.records[0].id);
        }
      },
      error: () => {
        this.feedback = 'Não foi possível carregar os registros. Tente novamente.';
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

  submit(): void {
    this.feedback = null;
    if (this.courseForm.invalid) {
      this.courseForm.markAllAsTouched();
      this.feedback = 'Preencha os campos obrigatórios antes de salvar.';
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
            this.feedback = 'Não foi possível salvar o cadastro. Tente novamente.';
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
            this.feedback = 'Cadastro salvo com sucesso!';
            this.saving = false;
          },
          error: () => {
            this.feedback = 'Não foi possível salvar o cadastro. Tente novamente.';
            this.saving = false;
          }
        });
    }
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
      profissional: course.profissional
    });
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
      profissional: ''
    });
    this.changeTab('dados');
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
      return;
    }

    const { beneficiaryName, cpf } = this.enrollmentForm.value;

    if (this.currentCourse.vagasDisponiveis > 0) {
      const enrollment: EnrollmentRecord = {
        id: crypto.randomUUID(),
        beneficiaryName,
        cpf,
        status: 'Ativo',
        enrolledAt: new Date().toISOString()
      };
      this.currentCourse.enrollments.push(enrollment);
      this.currentCourse.vagasDisponiveis = this.calculateAvailable(this.currentCourse);
      this.persistCurrentCourse();
      this.enrollmentForm.reset();
      this.enrollmentForm.patchValue({ beneficiaryName: '', cpf: '' });
    } else {
      const confirmWaitlist = window.confirm(
        'Não há vagas disponíveis. Deseja incluir o beneficiário na lista de espera?'
      );
      if (confirmWaitlist) {
        const entry: WaitlistEntry = {
          id: crypto.randomUUID(),
          beneficiaryName,
          cpf,
          joinedAt: new Date().toISOString()
        };
        this.currentCourse.waitlist.push(entry);
        this.persistCurrentCourse();
        this.enrollmentForm.reset();
        this.enrollmentForm.patchValue({ beneficiaryName: '', cpf: '' });
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

  removeFromWaitlist(entry: WaitlistEntry): void {
    if (!this.currentCourse) return;
    this.currentCourse.waitlist = this.currentCourse.waitlist.filter((item) => item.id !== entry.id);
    this.persistCurrentCourse();
  }

  tryPromoteWaitlist(): void {
    if (!this.currentCourse) return;
    if (this.currentCourse.vagasDisponiveis <= 0) return;
    if (this.currentCourse.waitlist.length === 0) return;

    const [first, ...rest] = this.currentCourse.waitlist;
    const enrollment: EnrollmentRecord = {
      id: crypto.randomUUID(),
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

    this.removeFromWaitlist(entry);
    const enrollment: EnrollmentRecord = {
      id: crypto.randomUUID(),
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

  eligibleForDocument(enrollment: Enrollment): boolean {
    if (!this.currentCourse) return false;
    if (this.currentCourse.tipo === 'Curso') {
      return enrollment.status === 'Concluído';
    }
    return enrollment.status === 'Ativo' || enrollment.status === 'Concluído';
  }

  emitDocument(enrollment: Enrollment): void {
    if (!this.currentCourse || !this.eligibleForDocument(enrollment)) return;

    const title = this.currentCourse.tipo === 'Curso' ? 'Certificado de Conclusão' : 'Atestado de Comparecimento';
    const payload = {
      beneficiario: enrollment.beneficiaryName,
      cpf: enrollment.cpf,
      curso: this.currentCourse.nome,
      tipo: this.currentCourse.tipo,
      cargaHoraria: this.currentCourse.cargaHoraria,
      horario: this.currentCourse.horarioInicial,
      duracao: this.currentCourse.duracaoHoras,
      profissional: this.currentCourse.profissional
    };

    const documentWindow = window.open('', '_blank', 'width=900,height=1200');
    if (!documentWindow) return;
    documentWindow.document.write(`
      <html>
        <head>
          <title>${title}</title>
          <style>
            body { font-family: Arial, sans-serif; margin: 40px; }
            header { border-bottom: 2px solid #16a34a; padding-bottom: 12px; margin-bottom: 24px; }
            h1 { margin: 0; color: #166534; }
            .meta { color: #555; margin: 0; }
            .section { margin-top: 16px; }
            .label { font-weight: bold; }
          </style>
        </head>
        <body>
          <header>
            <h1>${title}</h1>
            <p class="meta">${this.currentCourse.tipo === 'Curso' ? 'Conclusão' : 'Comparecimento'} registrado para ${payload.beneficiario}</p>
          </header>
          <section class="section">
            <p><span class="label">Beneficiário:</span> ${payload.beneficiario} (${payload.cpf})</p>
            <p><span class="label">${this.currentCourse.tipo}:</span> ${payload.curso}</p>
            <p><span class="label">Carga horária:</span> ${payload.cargaHoraria ?? 'Não informada'} horas</p>
            <p><span class="label">Horário:</span> ${payload.horario} • Duração ${payload.duracao}h</p>
            <p><span class="label">Profissional responsável:</span> ${payload.profissional}</p>
            <p class="meta">Documento gerado para fins institucionais.</p>
          </section>
        </body>
      </html>
    `);
    documentWindow.document.close();
  }

  dashboardTotals() {
    const ativosCurso = this.records.filter((c) => c.tipo === 'Curso').length;
    const ativosAtendimento = this.records.filter((c) => c.tipo === 'Atendimento').length;
    const totalMatriculas = this.records.reduce(
      (sum, c) => sum + c.enrollments.filter((e) => e.status === 'Ativo').length,
      0
    );
    const totalVagas = this.records.reduce((sum, c) => sum + c.vagasTotais, 0);
    const vagasDisponiveis = this.records.reduce((sum, c) => sum + c.vagasDisponiveis, 0);
    const listaEspera = this.records.reduce((sum, c) => sum + c.waitlist.length, 0);

    return {
      ativosCurso,
      ativosAtendimento,
      totalMatriculas,
      totalVagas,
      vagasDisponiveis,
      listaEspera
    };
  }

  fillFromCourse(course: CourseRecord): void {
    this.loadCourse(course.id);
    this.changeTab('dados');
  }

  private normalizeRecord(record: CourseRecord): CourseRecord {
    return {
      ...record,
      imagem: record.imagem ?? null,
      diasSemana: record.diasSemana ?? [],
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
    this.loadCourse(normalized.id);
  }

  private persistCurrentCourse(): void {
    if (!this.currentCourse) return;
    this.service.update(this.currentCourse.id, this.currentCourse).subscribe({
      next: (record) => this.replaceRecord(record),
      error: () => {
        this.feedback = 'Não foi possível salvar as alterações. Tente novamente.';
      }
    });
  }
}
