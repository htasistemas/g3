import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface SystemParameters {
  habilitarModuloBiblioteca: boolean;
}

const STORAGE_KEY = 'g3.system-parameters';
const DEFAULT_PARAMETERS: SystemParameters = {
  habilitarModuloBiblioteca: false
};

@Injectable({ providedIn: 'root' })
export class SystemParameterService {
  private readonly parametersSubject = new BehaviorSubject<SystemParameters>(this.load());
  readonly parameters$ = this.parametersSubject.asObservable();

  current(): SystemParameters {
    return this.parametersSubject.value;
  }

  isLibraryEnabled(): boolean {
    return this.parametersSubject.value.habilitarModuloBiblioteca;
  }

  update(partial: Partial<SystemParameters>): void {
    const updated = { ...this.parametersSubject.value, ...partial };
    this.parametersSubject.next(updated);
    this.persist(updated);
  }

  reset(): void {
    this.parametersSubject.next(DEFAULT_PARAMETERS);
    this.persist(DEFAULT_PARAMETERS);
  }

  private load(): SystemParameters {
    try {
      const stored = typeof localStorage !== 'undefined' ? localStorage.getItem(STORAGE_KEY) : null;
      if (stored) {
        const parsed = JSON.parse(stored) as SystemParameters;
        return { ...DEFAULT_PARAMETERS, ...parsed };
      }
    } catch (error) {
      console.error('Erro ao carregar parâmetros do sistema', error);
    }

    return DEFAULT_PARAMETERS;
  }

  private persist(params: SystemParameters): void {
    try {
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem(STORAGE_KEY, JSON.stringify(params));
      }
    } catch (error) {
      console.error('Erro ao salvar parâmetros do sistema', error);
    }
  }
}
