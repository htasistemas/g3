import { Injectable } from '@angular/core';

export interface Livro {
  id: string;
  codigoInterno: string;
  titulo: string;
  subtitulo?: string;
  autores: string;
  editora?: string;
  anoPublicacao?: string;
  edicao?: string;
  isbn?: string;
  categoria?: string;
  palavrasChave?: string[];
  quantidadeExemplares: number;
  unidade?: string;
  setor?: string;
  estante?: string;
  prateleira?: string;
  observacoesLocalizacao?: string;
  situacao: string;
  permiteEmprestimo: boolean;
  observacoesGerais?: string;
}

@Injectable({ providedIn: 'root' })
export class LibraryService {
  private readonly storageKey = 'g3.library.books';

  list(): Livro[] {
    try {
      const raw = typeof localStorage !== 'undefined' ? localStorage.getItem(this.storageKey) : null;
      return raw ? (JSON.parse(raw) as Livro[]) : [];
    } catch (error) {
      console.error('Erro ao carregar livros do armazenamento local', error);
      return [];
    }
  }

  getById(id: string): Livro | undefined {
    return this.list().find((book) => book.id === id);
  }

  create(payload: Livro): Livro {
    const books = this.list();
    if (books.some((book) => book.codigoInterno === payload.codigoInterno && book.id !== payload.id)) {
      throw new Error('Já existe um livro com este código interno.');
    }

    const record = { ...payload, id: payload.id || crypto.randomUUID() };
    this.persist([record, ...books]);
    return record;
  }

  update(id: string, payload: Livro): Livro {
    const books = this.list();
    if (books.some((book) => book.codigoInterno === payload.codigoInterno && book.id !== id)) {
      throw new Error('Já existe um livro com este código interno.');
    }

    const updated = books.map((book) => (book.id === id ? { ...payload, id } : book));
    this.persist(updated);
    return payload;
  }

  delete(id: string): void {
    const filtered = this.list().filter((book) => book.id !== id);
    this.persist(filtered);
  }

  private persist(data: Livro[]): void {
    try {
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem(this.storageKey, JSON.stringify(data));
      }
    } catch (error) {
      console.error('Erro ao salvar livros no armazenamento local', error);
    }
  }
}
