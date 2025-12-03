import { Injectable, signal } from '@angular/core';

export interface DashboardTop12 {
  beneficiariosAtendidosPeriodo: number;
  familiasExtremaPobreza: number;
  rendaMediaFamiliar: number;
  cursosAtivos: number;
  taxaMediaOcupacaoCursos: number;
  certificadosEmitidos: number;
  doacoesPeriodo: number;
  itensDoadoResumo: Record<string, number>;
  visitasDomiciliares: number;
  termosVencendo: number;
  execucaoFinanceira: number;
  absenteismo: number;
}

export interface DashboardAtendimento {
  totalBeneficiarios: number;
  ativos: number;
  pendentes: number;
  bloqueados: number;
  emAnalise: number;
  desatualizados: number;
  cadastroCompletoPercentual: number;
  beneficiariosPeriodo: number;
  novosBeneficiarios: number;
  reincidentes: number;
  faixaEtaria: Record<string, number>;
  vulnerabilidades: Record<string, number>;
}

export interface DashboardFamilias {
  total: number;
  mediaPessoas: number;
  rendaMediaFamiliar: number;
  rendaPerCapitaMedia: number;
  insegurancaAlimentar: Record<string, number>;
  faixaRenda: Record<string, number>;
}

export interface DashboardTermos {
  ativos: number;
  valorTotal: number;
  alertas: { numero: string; vigenciaFim?: string | null; status?: string | null }[];
}

export interface DashboardAssistenciaResponse {
  filters: { startDate: string | null; endDate: string | null };
  top12: DashboardTop12;
  atendimento: DashboardAtendimento;
  familias: DashboardFamilias;
  termos: DashboardTermos;
}

@Injectable({ providedIn: 'root' })
export class DashboardAssistenciaService {
  readonly data = signal<DashboardAssistenciaResponse | null>(null);
  readonly loading = signal(false);
  readonly error = signal<string | null>(null);

  fetch(filters?: { startDate?: string | null; endDate?: string | null }) {
    this.loading.set(true);
    this.error.set(null);

    setTimeout(() => {
      this.data.set(this.buildMockData(filters));
      this.loading.set(false);
    }, 300);
  }

  private buildMockData(filters?: { startDate?: string | null; endDate?: string | null }): DashboardAssistenciaResponse {
    const startDate = filters?.startDate || null;
    const endDate = filters?.endDate || null;

    return {
      filters: { startDate, endDate },
      top12: {
        beneficiariosAtendidosPeriodo: 1240,
        familiasExtremaPobreza: 318,
        rendaMediaFamiliar: 1450.35,
        cursosAtivos: 22,
        taxaMediaOcupacaoCursos: 78,
        certificadosEmitidos: 432,
        doacoesPeriodo: 84500,
        itensDoadoResumo: {
          cestas: 65,
          livros: 28,
          uniformes: 42,
          kitsHigiene: 51
        },
        visitasDomiciliares: 37,
        termosVencendo: 4,
        execucaoFinanceira: 86,
        absenteismo: 6.5
      },
      atendimento: {
        totalBeneficiarios: 3280,
        ativos: 2975,
        pendentes: 165,
        bloqueados: 24,
        emAnalise: 62,
        desatualizados: 54,
        cadastroCompletoPercentual: 88,
        beneficiariosPeriodo: 412,
        novosBeneficiarios: 156,
        reincidentes: 41,
        faixaEtaria: {
          '0-5 anos': 420,
          '6-12 anos': 870,
          '13-17 anos': 680,
          '18-29 anos': 540,
          '30-59 anos': 620,
          '60+ anos': 150
        },
        vulnerabilidades: {
          'Insegurança alimentar': 680,
          'Desemprego': 740,
          'Moradia precária': 295,
          'Violência doméstica': 115,
          'Pessoa com deficiência': 180
        }
      },
      familias: {
        total: 910,
        mediaPessoas: 3.4,
        rendaMediaFamiliar: 1895.75,
        rendaPerCapitaMedia: 556.1,
        insegurancaAlimentar: {
          Grave: 124,
          Moderada: 228,
          Leve: 310,
          'Não identificada': 248
        },
        faixaRenda: {
          'Até 1/2 SM': 362,
          'Até 1 SM': 271,
          '1 a 2 SM': 190,
          '2 a 3 SM': 62,
          'Acima de 3 SM': 25
        }
      },
      termos: {
        ativos: 18,
        valorTotal: 925000,
        alertas: [
          { numero: 'TF-2024-018', vigenciaFim: '2024-09-30', status: 'A vencer' },
          { numero: 'TF-2024-011', vigenciaFim: '2024-10-12', status: 'A vencer' },
          { numero: 'TF-2023-027', vigenciaFim: '2024-08-22', status: 'Revisão' }
        ]
      }
    };
  }
}
