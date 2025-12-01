import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface TextTemplates {
  cession: string;
  loan: string;
}

@Injectable({ providedIn: 'root' })
export class TextTemplateService {
  private readonly storageKey = 'g3_text_templates';
  private readonly defaults: TextTemplates = {
    cession:
      'Declaro que o bem {{nome}} (patrimônio {{numeroPatrimonio}}) está sob responsabilidade de {{responsavel}} no local {{local}}. Valor de aquisição: {{valor}}.',
    loan:
      'Eu, {{responsavel}}, assumo responsabilidade pelo empréstimo do bem {{nome}} (patrimônio {{numeroPatrimonio}}), alocado em {{local}}, comprometendo-me a devolvê-lo nas mesmas condições. Valor de referência: {{valor}}.'
  };

  private readonly templatesSubject = new BehaviorSubject<TextTemplates>(this.loadTemplates());
  readonly templates$ = this.templatesSubject.asObservable();

  getTemplates(): TextTemplates {
    return this.templatesSubject.value;
  }

  updateTemplates(partial: Partial<TextTemplates>): TextTemplates {
    const merged = { ...this.templatesSubject.value, ...partial } as TextTemplates;
    this.templatesSubject.next(merged);
    localStorage.setItem(this.storageKey, JSON.stringify(merged));
    return merged;
  }

  getDefaults(): TextTemplates {
    return this.defaults;
  }

  reset(): TextTemplates {
    this.templatesSubject.next(this.defaults);
    localStorage.removeItem(this.storageKey);
    return this.defaults;
  }

  private loadTemplates(): TextTemplates {
    const stored = localStorage.getItem(this.storageKey);
    if (stored) {
      try {
        const parsed = JSON.parse(stored) as Partial<TextTemplates>;
        return { ...this.defaults, ...parsed } as TextTemplates;
      } catch (error) {
        console.warn('Não foi possível carregar modelos de texto salvos', error);
      }
    }

    return this.defaults;
  }
}
