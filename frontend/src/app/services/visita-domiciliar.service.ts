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
  endereco: EnderecoVisita;
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
      return saved ? (JSON.parse(saved) as VisitaDomiciliar[]) : this.seed();
    } catch (error) {
      console.error('Erro ao recuperar visitas domiciliares', error);
      return this.seed();
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

  private seed(): VisitaDomiciliar[] {
    const sample: VisitaDomiciliar = {
      id: crypto.randomUUID(),
      unidade: 'Unidade Central',
      beneficiario: 'Maria Souza',
      responsavel: 'Equipe Social',
      dataVisita: new Date().toISOString().slice(0, 10),
      horarioInicial: '09:00',
      horarioFinal: '10:15',
      tipoVisita: 'Acompanhamento',
      situacao: 'Realizada',
      usarEnderecoBeneficiario: true,
      endereco: {
        logradouro: 'Rua das Flores',
        numero: '123',
        bairro: 'Centro',
        cidade: 'São Paulo',
        uf: 'SP',
        cep: '01000-000'
      },
      observacoesIniciais: 'Visita de rotina para atualização de cadastro.',
      condicoes: {
        tipoMoradia: 'Alugada',
        situacaoPosse: 'Regular',
        comodos: 4,
        saneamento: 'Rede geral',
        abastecimentoAgua: 'Rede geral',
        energiaEletrica: 'Regular',
        condicoesHigiene: 'Boa',
        situacaoRisco: ['Sem risco estrutural'],
        observacoes: 'Ambiente organizado e ventilado.'
      },
      situacaoSocial: {
        rendaFamiliar: 'R$ 1.800,00',
        faixaRenda: '1 a 2 salários',
        beneficios: ['Bolsa Família'],
        redeApoio: 'Vizinhos e igreja local',
        vinculos: 'Participa do grupo comunitário',
        observacoes: 'Beneficiária engajada nas ações comunitárias.'
      },
      registro: {
        relato: 'Visita realizada com atualização cadastral e orientação sobre cursos.',
        necessidades: 'Acompanhamento escolar para filhos.',
        encaminhamentos: 'Encaminhada para oficina de informática.',
        orientacoes: 'Organizar documentos para próximo atendimento.',
        plano: 'Revisar evolução em 30 dias.'
      },
      anexos: [
        { id: crypto.randomUUID(), nome: 'Foto da fachada.jpg', tipo: 'Imagem', tamanho: '1.2 MB' },
        { id: crypto.randomUUID(), nome: 'Comprovante de endereço.pdf', tipo: 'PDF', tamanho: '320 KB' }
      ],
      createdAt: new Date().toISOString(),
      createdBy: 'admin'
    };

    this.persist([sample]);
    return [sample];
  }
}
