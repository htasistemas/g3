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
      'TERMO DE CESSÃƒO DE BEM PATRIMONIAL\n\n' +
      'Pelo presente instrumento, fica registrado que o bem {{nome}}, identificado pelo nÃºmero de patrimÃ´nio {{numeroPatrimonio}}, ' +
      'foi cedido para uso e responsabilidade de {{responsavel}}, alocado em {{local}}, com valor de aquisiÃ§Ã£o de {{valor}}.\n\n' +
      'O responsÃ¡vel declara estar ciente de que o bem permanece de propriedade da instituiÃ§Ã£o, comprometendo-se a zelar pela sua conservaÃ§Ã£o, ' +
      'utilizÃ¡-lo exclusivamente para fins institucionais, comunicar imediatamente qualquer dano, perda ou necessidade de manutenÃ§Ã£o e devolvÃª-lo ' +
      'quando solicitado ou ao tÃ©rmino da cessÃ£o.\n\n' +
      'Dados do bem: Categoria {{categoria}} {{subcategoria}}. Estado de conservaÃ§Ã£o {{conservacao}}. SituaÃ§Ã£o {{status}}. Data de aquisiÃ§Ã£o {{dataAquisicao}}.\n\n' +
      'Este termo Ã© emitido na data de {{dataEmissao}}.',
    loan:
      'Eu, {{responsavel}}, assumo responsabilidade pelo emprÃ©stimo do bem {{nome}} (patrimÃ´nio {{numeroPatrimonio}}), alocado em {{local}}, comprometendo-me a devolvÃª-lo nas mesmas condiÃ§Ãµes. Valor de referÃªncia: {{valor}}.'
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
        console.warn('NÃ£o foi possÃ­vel carregar modelos de texto salvos', error);
      }
    }

    return this.defaults;
  }
}

