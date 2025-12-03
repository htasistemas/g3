import { Injectable } from '@angular/core';
import { LibraryService } from './library.service';

export type SituacaoEmprestimo = 'Emprestado' | 'Devolvido' | 'Atrasado' | 'Perdido' | 'Danificado';

export interface Emprestimo {
  id: string;
  beneficiario: string;
  livroId: string;
  exemplar?: string;
  dataEmprestimo: string;
  dataPrevistaDevolucao: string;
  dataDevolucao?: string;
  status: SituacaoEmprestimo;
  responsavel?: string;
  observacoes?: string;
  devolucaoObservacoes?: string;
}

@Injectable({ providedIn: 'root' })
export class LoanService {
  private readonly storageKey = 'g3.library.loans';

  constructor(private readonly library: LibraryService) {}

  list(): Emprestimo[] {
    const loans = this.load();
    const evaluated: Emprestimo[] = [];
    let changed = false;

    for (const loan of loans) {
      const updated = this.applyOverdueRule(loan);
      evaluated.push(updated);
      if (updated.status !== loan.status) {
        changed = true;
      }
    }

    if (changed) {
      this.persist(evaluated);
    }

    return evaluated;
  }

  create(payload: Emprestimo): Emprestimo {
    const book = this.library.getById(payload.livroId);
    if (!book) {
      throw new Error('Livro não encontrado para empréstimo.');
    }

    if (!book.permiteEmprestimo) {
      throw new Error('Este livro não está habilitado para empréstimo.');
    }

    const available = this.availableCopies(payload.livroId);
    if (available <= 0 && payload.status !== 'Devolvido') {
      throw new Error('Não há exemplares disponíveis para empréstimo.');
    }

    const loans = this.list();
    const record: Emprestimo = { ...payload, id: payload.id || crypto.randomUUID() };
    this.persist([record, ...loans]);
    return record;
  }

  update(id: string, payload: Emprestimo): Emprestimo {
    const book = this.library.getById(payload.livroId);
    if (!book) {
      throw new Error('Livro não encontrado para empréstimo.');
    }

    if (!book.permiteEmprestimo) {
      throw new Error('Este livro não está habilitado para empréstimo.');
    }

    const available = this.availableCopies(payload.livroId, id);
    if (available <= 0 && payload.status !== 'Devolvido') {
      throw new Error('Não há exemplares disponíveis para empréstimo.');
    }

    const loans = this.list().map((loan) => (loan.id === id ? { ...payload, id } : loan));
    this.persist(loans);
    return payload;
  }

  delete(id: string): void {
    const filtered = this.list().filter((loan) => loan.id !== id);
    this.persist(filtered);
  }

  availableCopies(livroId: string, editingId?: string): number {
    const book = this.library.getById(livroId);
    if (!book) return 0;

    const activeLoans = this.list().filter(
      (loan) =>
        loan.livroId === livroId &&
        (!editingId || loan.id !== editingId) &&
        (loan.status === 'Emprestado' || loan.status === 'Atrasado') &&
        !loan.dataDevolucao
    );

    return Math.max(book.quantidadeExemplares - activeLoans.length, 0);
  }

  private applyOverdueRule(loan: Emprestimo): Emprestimo {
    if (
      (loan.status === 'Emprestado' || loan.status === 'Atrasado') &&
      loan.dataPrevistaDevolucao &&
      !loan.dataDevolucao
    ) {
      const due = new Date(loan.dataPrevistaDevolucao);
      const today = new Date();
      if (today > due) {
        return { ...loan, status: 'Atrasado' };
      }
    }

    return loan;
  }

  private load(): Emprestimo[] {
    try {
      const raw = typeof localStorage !== 'undefined' ? localStorage.getItem(this.storageKey) : null;
      return raw ? (JSON.parse(raw) as Emprestimo[]) : [];
    } catch (error) {
      console.error('Erro ao carregar empréstimos do armazenamento local', error);
      return [];
    }
  }

  private persist(data: Emprestimo[]): void {
    try {
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem(this.storageKey, JSON.stringify(data));
      }
    } catch (error) {
      console.error('Erro ao salvar empréstimos no armazenamento local', error);
    }
  }
}
