import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { LibraryService, Livro } from '../../services/library.service';
import { Emprestimo, LoanService, SituacaoEmprestimo } from '../../services/loan.service';

interface StepTab {
  id: 'dados' | 'devolucao' | 'historico';
  label: string;
}

@Component({
  selector: 'app-biblioteca-emprestimos',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './biblioteca-emprestimos.component.html',
  styleUrl: './biblioteca-emprestimos.component.scss'
})
export class BibliotecaEmprestimosComponent implements OnInit {
  form: FormGroup;
  activeTab: StepTab['id'] = 'dados';
  loans: Emprestimo[] = [];
  booksDisponiveis: Livro[] = [];
  editingId: string | null = null;
  feedback: { type: 'success' | 'error'; message: string } | null = null;

  readonly tabs: StepTab[] = [
    { id: 'dados', label: 'Dados do Empréstimo' },
    { id: 'devolucao', label: 'Devolução e acompanhamento' },
    { id: 'historico', label: 'Histórico do livro' }
  ];

  readonly situacoesEmprestimo: SituacaoEmprestimo[] = ['Emprestado', 'Devolvido', 'Atrasado', 'Perdido', 'Danificado'];

  filtroStatus: SituacaoEmprestimo | 'todos' = 'todos';
  filtroBeneficiario = '';
  filtroLivro = '';

  constructor(
    private readonly fb: FormBuilder,
    private readonly loansService: LoanService,
    private readonly library: LibraryService
  ) {
    this.form = this.fb.group({
      dados: this.fb.group({
        beneficiario: ['', Validators.required],
        livroId: ['', Validators.required],
        exemplar: [''],
        dataEmprestimo: [new Date().toISOString().substring(0, 10), Validators.required],
        dataPrevistaDevolucao: ['', Validators.required],
        status: ['Emprestado', Validators.required],
        responsavel: [''],
        observacoes: ['']
      }),
      devolucao: this.fb.group({
        dataDevolucao: [''],
        statusDevolucao: [''],
        devolucaoObservacoes: ['']
      })
    });
  }

  ngOnInit(): void {
    this.refresh();
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

  refresh(): void {
    this.loans = this.loansService.list();
    this.booksDisponiveis = this.library
      .list()
      .filter((book) => book.permiteEmprestimo && book.situacao === 'Ativo');
  }

  edit(loan: Emprestimo): void {
    this.editingId = loan.id;
    this.form.patchValue({
      dados: {
        beneficiario: loan.beneficiario,
        livroId: loan.livroId,
        exemplar: loan.exemplar,
        dataEmprestimo: loan.dataEmprestimo,
        dataPrevistaDevolucao: loan.dataPrevistaDevolucao,
        status: loan.status,
        responsavel: loan.responsavel,
        observacoes: loan.observacoes
      },
      devolucao: {
        dataDevolucao: loan.dataDevolucao,
        statusDevolucao: loan.status,
        devolucaoObservacoes: loan.devolucaoObservacoes
      }
    });
    this.activeTab = 'dados';
  }

  remove(loan: Emprestimo): void {
    this.loansService.delete(loan.id);
    this.refresh();
    this.feedback = { type: 'success', message: 'Empréstimo removido com sucesso.' };
  }

  resetForm(): void {
    this.editingId = null;
    this.form.reset({
      dados: {
        beneficiario: '',
        livroId: '',
        exemplar: '',
        dataEmprestimo: new Date().toISOString().substring(0, 10),
        dataPrevistaDevolucao: '',
        status: 'Emprestado',
        responsavel: '',
        observacoes: ''
      },
      devolucao: {
        dataDevolucao: '',
        statusDevolucao: '',
        devolucaoObservacoes: ''
      }
    });
    this.feedback = null;
  }

  save(): void {
    this.feedback = null;
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.feedback = { type: 'error', message: 'Preencha os campos obrigatórios para salvar o empréstimo.' };
      return;
    }

    const payload = this.buildPayload();

    try {
      if (this.editingId) {
        this.loansService.update(this.editingId, payload);
        this.feedback = { type: 'success', message: 'Empréstimo atualizado com sucesso.' };
      } else {
        this.loansService.create(payload);
        this.feedback = { type: 'success', message: 'Empréstimo registrado com sucesso.' };
      }
      this.refresh();
      this.resetForm();
    } catch (error: any) {
      this.feedback = { type: 'error', message: error?.message ?? 'Não foi possível salvar o empréstimo.' };
    }
  }

  disponibilidadeLivro(livroId: string): number {
    if (!livroId) return 0;
    return this.loansService.availableCopies(livroId, this.editingId ?? undefined);
  }

  get historicoFiltrado(): Emprestimo[] {
    return this.loans.filter((loan) => {
      const byStatus = this.filtroStatus === 'todos' || loan.status === this.filtroStatus;
      const byBeneficiary = this.filtroBeneficiario
        ? loan.beneficiario.toLowerCase().includes(this.filtroBeneficiario.toLowerCase())
        : true;
      const bookTitle = this.library.getById(loan.livroId)?.titulo ?? '';
      const byBook = this.filtroLivro ? bookTitle.toLowerCase().includes(this.filtroLivro.toLowerCase()) : true;
      return byStatus && byBeneficiary && byBook;
    });
  }

  livroTitulo(livroId: string): string {
    return this.library.getById(livroId)?.titulo || 'Livro removido';
  }

  private buildPayload(): Emprestimo {
    const dados = this.form.value.dados ?? {};
    const devolucao = this.form.value.devolucao ?? {};
    const status = (devolucao.statusDevolucao as SituacaoEmprestimo) || (dados.status as SituacaoEmprestimo);

    return {
      id: this.editingId ?? crypto.randomUUID(),
      beneficiario: dados.beneficiario ?? '',
      livroId: dados.livroId ?? '',
      exemplar: dados.exemplar ?? '',
      dataEmprestimo: dados.dataEmprestimo ?? '',
      dataPrevistaDevolucao: dados.dataPrevistaDevolucao ?? '',
      dataDevolucao: devolucao.dataDevolucao ?? '',
      status,
      responsavel: dados.responsavel ?? '',
      observacoes: dados.observacoes ?? '',
      devolucaoObservacoes: devolucao.devolucaoObservacoes ?? ''
    };
  }
}
