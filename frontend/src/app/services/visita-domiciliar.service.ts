import { Injectable } from '@angular/core';

export type SituacaoVisita = 'Agendada' | 'Em andamento' | 'Realizada' | 'Cancelada';

export interface EnderecoVisita {
  logradouro?: string;
  numero?: string;
  bairro?: string;
  cidade?: string;
  uf?: string;
  cep?: string;
}

export interface VisitaDomiciliar {
  id: string;
  unidade: string;
  beneficiario: string;
  responsavel: string;
  dataVisita: string;
  horarioInicial: string;
  horarioFinal?: string;
  tipoVisita?: string;
  situacao: SituacaoVisita;
  usarEnderecoBeneficiario: boolean;
  endereço: EnderecoVisita;
  observacoesIniciais?: string;
  condicoes: {
    tipoMoradia?: string;
    situacaoPosse?: string;
    comodos?: number | null;
    saneamento?: string;
    abastecimentoAgua?: string;
    energiaEletrica?: string;
    condicoesHigiene?: string;
    situacaoRisco?: string[];
    observacoes?: string;
  };
  situacaoSocial: {
    rendaFamiliar?: string;
    faixaRenda?: string;
    beneficios?: string[];
    redeApoio?: string;
    vinculos?: string;
    observacoes?: string;
  };
  registro: {
    relato?: string;
    necessidades?: string;
    encaminhamentos?: string;
    orientacoes?: string;
    plano?: string;
  };
  anexos: VisitaAnexo[];
  createdAt: string;
  createdBy?: string;
}

export interface VisitaAnexo {
  id: string;
  nome: string;
  tipo: string;
  tamanho?: string;
}

@Injectable({ providedIn: 'root' })
export class VisitaDomiciliarService {
  private readonly storageKey = 'g3.visitas-domiciliares';

  list(): VisitaDomiciliar[] {
    try {
      const saved = typeof localStorage !== 'undefined' ? localStorage.getItem(this.storageKey) : null;
      return saved ? (JSON.parse(saved) as VisitaDomiciliar[]) : [];
    } catch (error) {
      console.error('Erro ao recuperar visitas domiciliares', error);
      return [];
    }
  }

  create(payload: VisitaDomiciliar): VisitaDomiciliar {
    const record = { ...payload, id: payload.id ?? crypto.randomUUID() };
    const all = [record, ...this.list()];
    this.persist(all);
    return record;
  }

  update(id: string, payload: VisitaDomiciliar): VisitaDomiciliar {
    const updated = { ...payload, id };
    const all = this.list().map((item) => (item.id === id ? updated : item));
    this.persist(all);
    return updated;
  }

  delete(id: string): void {
    const remaining = this.list().filter((visit) => visit.id !== id);
    this.persist(remaining);
  }

  private persist(data: VisitaDomiciliar[]): void {
    try {
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem(this.storageKey, JSON.stringify(data));
      }
    } catch (error) {
      console.error('Erro ao salvar visitas domiciliares', error);
    }
  }
}
