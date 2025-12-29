import { Injectable } from '@angular/core';

export interface TramiteRegistro {
  data: string;
  origem?: string;
  destino?: string;
  responsavel?: string;
  acao: string;
  observacoes?: string;
}

export interface OficioPayload {
  id?: string;
  identificacao: {
    tipo: 'emissao' | 'recebimento';
    numero: string;
    data: string;
    setorOrigem: string;
    responsavel: string;
    destinatario: string;
    meioEnvio: string;
    prazoResposta?: string;
    classificacao?: string;
  };
  conteudo: {
    razaoSocial: string;
    logoUrl?: string;
    titulo?: string;
    saudacao: string;
    assunto: string;
    corpo: string;
    finalizacao: string;
    assinaturaNome: string;
    assinaturaCargo: string;
    rodape: string;
  };
  protocolo: {
    status: string;
    protocoloEnvio?: string;
    dataEnvio?: string;
    protocoloRecebimento?: string;
    dataRecebimento?: string;
    proximoDestino?: string;
    observacoes?: string;
  };
  tramites: TramiteRegistro[];
}

@Injectable({ providedIn: 'root' })
export class OficioService {
  private readonly storageKey = 'g3.oficios';

  list(): OficioPayload[] {
    try {
      const saved = typeof localStorage !== 'undefined' ? localStorage.getItem(this.storageKey) : null;
      return saved ? (JSON.parse(saved) as OficioPayload[]) : [];
    } catch (error) {
      console.error('Erro ao ler oficios do armazenamento local', error);
      return [];
    }
  }

  create(payload: OficioPayload): OficioPayload {
    const record: OficioPayload = {
      ...payload,
      id: payload.id ?? crypto.randomUUID(),
      tramites: payload.tramites ?? []
    };
    const all = [record, ...this.list()];
    this.persist(all);
    return record;
  }

  update(id: string, payload: OficioPayload): OficioPayload {
    const updated: OficioPayload = { ...payload, id };
    const all = this.list().map((item) => (item.id === id ? updated : item));
    this.persist(all);
    return updated;
  }

  delete(id: string): void {
    const filtered = this.list().filter((item) => item.id !== id);
    this.persist(filtered);
  }

  private persist(data: OficioPayload[]): void {
    try {
      if (typeof localStorage !== 'undefined') {
        localStorage.setItem(this.storageKey, JSON.stringify(data));
      }
    } catch (error) {
      console.error('Erro ao salvar oficios no armazenamento local', error);
    }
  }
}
