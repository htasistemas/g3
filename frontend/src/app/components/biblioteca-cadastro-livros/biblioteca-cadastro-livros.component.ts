import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { LibraryService, Livro } from '../../services/library.service';

interface StepTab {
  id: 'dados' | 'localizacao' | 'controle';
  label: string;
}

@Component({
  selector: 'app-biblioteca-cadastro-livros',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './biblioteca-cadastro-livros.component.html',
  styleUrl: './biblioteca-cadastro-livros.component.scss'
})
export class BibliotecaCadastroLivrosComponent {
  form: FormGroup;
  activeTab: StepTab['id'] = 'dados';
  books: Livro[] = [];
  editingId: string | null = null;
  feedback: { type: 'success' | 'error'; message: string } | null = null;

  readonly tabs: StepTab[] = [
    { id: 'dados', label: 'Dados do Livro' },
    { id: 'localizacao', label: 'Localização física' },
    { id: 'controle', label: 'Controle e observações' }
  ];

  readonly situacoes = ['Ativo', 'Em manutenção', 'Perdido', 'Danificado', 'Desativado'];

  constructor(private readonly fb: FormBuilder, private readonly library: LibraryService) {
    this.form = this.fb.group({
      dados: this.fb.group({
        codigoInterno: ['', Validators.required],
        titulo: ['', Validators.required],
        subtitulo: [''],
        autores: ['', Validators.required],
        editora: [''],
        anoPublicacao: [''],
        edicao: [''],
        isbn: [''],
        categoria: [''],
        palavrasChave: [''],
        quantidadeExemplares: [1, [Validators.required, Validators.min(1)]]
      }),
      localizacao: this.fb.group({
        unidade: [''],
        setor: [''],
        estante: [''],
        prateleira: [''],
        observacoesLocalizacao: ['']
      }),
      controle: this.fb.group({
        situacao: ['Ativo'],
        permiteEmprestimo: [true],
        observacoesGerais: ['']
      })
    });

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
    this.books = this.library.list();
  }

  edit(book: Livro): void {
    this.editingId = book.id;
    this.form.patchValue({
      dados: {
        codigoInterno: book.codigoInterno,
        titulo: book.titulo,
        subtitulo: book.subtitulo,
        autores: book.autores,
        editora: book.editora,
        anoPublicacao: book.anoPublicacao,
        edicao: book.edicao,
        isbn: book.isbn,
        categoria: book.categoria,
        palavrasChave: book.palavrasChave?.join(', '),
        quantidadeExemplares: book.quantidadeExemplares
      },
      localizacao: {
        unidade: book.unidade,
        setor: book.setor,
        estante: book.estante,
        prateleira: book.prateleira,
        observacoesLocalizacao: book.observacoesLocalizacao
      },
      controle: {
        situacao: book.situacao,
        permiteEmprestimo: book.permiteEmprestimo,
        observacoesGerais: book.observacoesGerais
      }
    });
    this.activeTab = 'dados';
  }

  remove(book: Livro): void {
    this.library.delete(book.id);
    this.refresh();
    this.feedback = { type: 'success', message: 'Livro removido com sucesso.' };
  }

  resetForm(): void {
    this.editingId = null;
    this.form.reset({
      dados: {
        codigoInterno: '',
        titulo: '',
        subtitulo: '',
        autores: '',
        editora: '',
        anoPublicacao: '',
        edicao: '',
        isbn: '',
        categoria: '',
        palavrasChave: '',
        quantidadeExemplares: 1
      },
      localizacao: {
        unidade: '',
        setor: '',
        estante: '',
        prateleira: '',
        observacoesLocalizacao: ''
      },
      controle: {
        situacao: 'Ativo',
        permiteEmprestimo: true,
        observacoesGerais: ''
      }
    });
    this.feedback = null;
  }

  save(): void {
    this.feedback = null;
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.feedback = { type: 'error', message: 'Preencha os campos obrigatórios para salvar o livro.' };
      return;
    }

    const payload = this.buildPayload();
    try {
      if (this.editingId) {
        this.library.update(this.editingId, payload);
        this.feedback = { type: 'success', message: 'Livro atualizado com sucesso.' };
      } else {
        this.library.create(payload);
        this.feedback = { type: 'success', message: 'Livro cadastrado com sucesso.' };
      }
      this.refresh();
      this.resetForm();
    } catch (error: any) {
      this.feedback = { type: 'error', message: error?.message ?? 'Não foi possível salvar o livro.' };
    }
  }

  private buildPayload(): Livro {
    const value = this.form.value;
    const dados = value.dados ?? {};
    const localizacao = value.localizacao ?? {};
    const controle = value.controle ?? {};

    const palavrasChave = (dados.palavrasChave as string | undefined)
      ?.split(',')
      .map((item) => item.trim())
      .filter(Boolean);

    return {
      id: this.editingId ?? crypto.randomUUID(),
      codigoInterno: dados.codigoInterno ?? '',
      titulo: dados.titulo ?? '',
      subtitulo: dados.subtitulo ?? '',
      autores: dados.autores ?? '',
      editora: dados.editora ?? '',
      anoPublicacao: dados.anoPublicacao ?? '',
      edicao: dados.edicao ?? '',
      isbn: dados.isbn ?? '',
      categoria: dados.categoria ?? '',
      palavrasChave: palavrasChave ?? [],
      quantidadeExemplares: Number(dados.quantidadeExemplares ?? 0) || 0,
      unidade: localizacao.unidade ?? '',
      setor: localizacao.setor ?? '',
      estante: localizacao.estante ?? '',
      prateleira: localizacao.prateleira ?? '',
      observacoesLocalizacao: localizacao.observacoesLocalizacao ?? '',
      situacao: controle.situacao ?? 'Ativo',
      permiteEmprestimo: Boolean(controle.permiteEmprestimo),
      observacoesGerais: controle.observacoesGerais ?? ''
    };
  }
}
