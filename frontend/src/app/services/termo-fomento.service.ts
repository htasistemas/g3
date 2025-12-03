import { Injectable } from '@angular/core';

export type TipoTermo = 'Uniao' | 'Estado' | 'Municipio';
export type SituacaoTermo = 'Ativo' | 'Aditivado' | 'Encerrado' | 'Cancelado';

export interface TermoDocumento {
  id?: string;
  nome: string;
  dataUrl?: string;
  tipo?: 'termo' | 'aditivo' | 'outro';
}

export interface AditivoPayload {
  id?: string;
  tipoAditivo: string;
  dataAditivo: string;
  novaDataFim?: string;
  novoValor?: number;
  observacoes?: string;
  anexo?: TermoDocumento | null;
}

export interface TermoFomentoPayload {
  id?: string;
  numeroTermo: string;
  tipoTermo: TipoTermo;
  orgaoConcedente?: string;
  dataAssinatura?: string;
  dataInicioVigencia?: string;
  dataFimVigencia?: string;
  situacao: SituacaoTermo;
  descricaoObjeto?: string;
  valorGlobal?: number;
  responsavelInterno?: string;
  termoDocumento?: TermoDocumento | null;
  documentosRelacionados?: TermoDocumento[];
  aditivos?: AditivoPayload[];
}

@Injectable({ providedIn: 'root' })
export class TermoFomentoService {
  private readonly storageKey = 'g3.termosFomento';

  list(): TermoFomentoPayload[] {
    try {
      const saved = typeof localStorage !== 'undefined' ? localStorage.getItem(this.storageKey) : null;
      return saved ? (JSON.parse(saved) as TermoFomentoPayload[]) : [];
    } catch (error) {
      console.error('Erro ao ler termos de fomento do armazenamento local', error);
      return [];
    }
  }

  getById(id: string): TermoFomentoPayload | undefined {
    return this.list().find((item) => item.id === id);
  }

  create(payload: TermoFomentoPayload): TermoFomentoPayload {
    const record: TermoFomentoPayload = {
      ...payload,
      id: payload.id ?? crypto.randomUUID(),
      aditivos: payload.aditivos ?? []
    };
    const all = [record, ...this.list()];
    this.persist(all);
    return record;
  }

  update(id: string, payload: TermoFomentoPayload): TermoFomentoPayload {
    const updated: TermoFomentoPayload = { ...payload, id };
    const all = this.list().map((item) => (item.id === id ? updated : item));
    this.persist(all);
    return updated;
  }

  delete(id: string): void {
    const filtered = this.list().filter((item) => item.id !== id);
    this.persist(filtered);
  }

  addAditivo(termoId: string, aditivo: AditivoPayload): TermoFomentoPayload | undefined {
    const termo = this.getById(termoId);
    if (!termo) return undefined;

    const aditivoRegistrado: AditivoPayload = { ...aditivo, id: aditivo.id ?? crypto.randomUUID() };
    const aditivosAtualizados = [aditivoRegistrado, ...(termo.aditivos ?? [])];

    const termoAtualizado: TermoFomentoPayload = {
      ...termo,
      aditivos: aditivosAtualizados,
      dataFimVigencia: aditivo.novaDataFim || termo.dataFimVigencia,
      valorGlobal: aditivo.novoValor ?? termo.valorGlobal,
      situacao: termo.situacao === 'Encerrado' || termo.situacao === 'Cancelado' ? termo.situacao : 'Aditivado'
    };

    this.update(termoId, termoAtualizado);
    return termoAtualizado;
  }

  private persist(data: TermoFomentoPayload[]): void {
    try {
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem(this.storageKey, JSON.stringify(data));
      }
    } catch (error) {
      console.error('Erro ao salvar termos de fomento no armazenamento local', error);
    }
  }
}
