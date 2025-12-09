import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { catchError, map, of } from 'rxjs';
import { CursosAtendimentosService, CourseRecord } from '../../services/cursos-atendimentos.service';

type StatusOption = CourseRecord['status'];

@Component({
  selector: 'app-atendimento-fluxo',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './atendimento-fluxo.component.html',
  styleUrl: './atendimento-fluxo.component.scss'
})
export class AtendimentoFluxoComponent implements OnInit {
  records: CourseRecord[] = [];
  loading = false;
  feedback: string | null = null;
  offlineMode = false;
  readonly statusOptions: StatusOption[] = ['TRIAGEM', 'EM_ANDAMENTO', 'ENCAMINHADO', 'EM_VISITA', 'CONCLUIDO'];
  justification: Record<string, string> = {};

  private readonly fallbackRecords: CourseRecord[] = [
    {
      id: 'att-001',
      tipo: 'Atendimento',
      nome: 'Acolhimento inicial',
      descricao: 'Primeira escuta e registro das demandas do beneficiário.',
      imagem: null,
      vagasTotais: 0,
      vagasDisponiveis: 0,
      cargaHoraria: null,
      horarioInicial: '2024-07-01T08:00:00Z',
      duracaoHoras: 1,
      diasSemana: ['Segunda-feira', 'Quarta-feira'],
      restricoes: null,
      profissional: 'Equipe de recepção',
      salaId: null,
      sala: null,
      createdAt: '2024-07-01T08:00:00Z',
      status: 'TRIAGEM',
      statusHistory: [
        { status: 'TRIAGEM', changedAt: '2024-07-01T08:00:00Z' },
        { status: 'EM_ANDAMENTO', changedAt: '2024-07-02T10:30:00Z' }
      ],
      dataTriagem: '2024-07-01',
      dataEncaminhamento: null,
      dataConclusao: null,
      enrollments: [],
      waitlist: []
    },
    {
      id: 'att-002',
      tipo: 'Atendimento',
      nome: 'Encaminhamento psicossocial',
      descricao: 'Orientação e acompanhamento com a equipe de psicologia.',
      imagem: null,
      vagasTotais: 0,
      vagasDisponiveis: 0,
      cargaHoraria: null,
      horarioInicial: '2024-07-03T09:00:00Z',
      duracaoHoras: 1,
      diasSemana: ['Terça-feira'],
      restricoes: null,
      profissional: 'Psicóloga responsável',
      salaId: null,
      sala: null,
      createdAt: '2024-07-03T09:00:00Z',
      status: 'EM_VISITA',
      statusHistory: [
        { status: 'TRIAGEM', changedAt: '2024-07-03T09:00:00Z' },
        { status: 'ENCAMINHADO', changedAt: '2024-07-05T14:00:00Z' },
        { status: 'EM_VISITA', changedAt: '2024-07-06T11:00:00Z' }
      ],
      dataTriagem: '2024-07-03',
      dataEncaminhamento: '2024-07-05',
      dataConclusao: null,
      enrollments: [],
      waitlist: []
    },
    {
      id: 'att-003',
      tipo: 'Atendimento',
      nome: 'Visita domiciliar',
      descricao: 'Avaliação socioeconômica na residência do beneficiário.',
      imagem: null,
      vagasTotais: 0,
      vagasDisponiveis: 0,
      cargaHoraria: null,
      horarioInicial: '2024-07-07T14:00:00Z',
      duracaoHoras: 1,
      diasSemana: ['Sexta-feira'],
      restricoes: null,
      profissional: 'Assistente social',
      salaId: null,
      sala: null,
      createdAt: '2024-07-07T14:00:00Z',
      status: 'CONCLUIDO',
      statusHistory: [
        { status: 'TRIAGEM', changedAt: '2024-07-07T14:00:00Z' },
        { status: 'EM_ANDAMENTO', changedAt: '2024-07-08T09:00:00Z' },
        { status: 'CONCLUIDO', changedAt: '2024-07-10T16:00:00Z', justification: 'Relatório entregue' }
      ],
      dataTriagem: '2024-07-07',
      dataEncaminhamento: '2024-07-08',
      dataConclusao: '2024-07-10',
      enrollments: [],
      waitlist: []
    }
  ];

  constructor(private readonly service: CursosAtendimentosService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.service
      .list()
      .pipe(
        map((records) => records.filter((r) => r.tipo === 'Atendimento')),
        catchError(() => {
          this.offlineMode = true;
          this.feedback =
            'Não foi possível conectar à API. Exibindo dados de demonstração para garantir a continuidade do fluxo.';
          return of(JSON.parse(JSON.stringify(this.fallbackRecords)) as CourseRecord[]);
        })
      )
      .subscribe((records) => {
        this.records = records;
        this.loading = false;
      });
  }

  updateStatus(record: CourseRecord, status: StatusOption): void {
    this.feedback = null;
    const justification = this.justification[record.id];

    this.service.updateStatus(record.id, { status, justification }).subscribe({
      next: (updated) => {
        this.records = this.records.map((item) => (item.id === updated.id ? updated : item));
        this.justification[record.id] = '';
      },
      error: (err) => {
        this.feedback = err?.error?.message ?? 'Sem conexão com a API. Status atualizado apenas localmente.';
        this.applyLocalStatus(record.id, status, justification);
      }
    });
  }

  timeline(record: CourseRecord): { label: string; date: string }[] {
    const history = record.statusHistory ?? [];
    return history.map((entry) => ({
      label: entry.status,
      date: new Date(entry.changedAt).toLocaleString()
    }));
  }

  statusSummary(): { status: StatusOption; total: number }[] {
    return this.statusOptions.map((status) => ({
      status,
      total: this.records.filter((record) => record.status === status).length
    }));
  }

  private applyLocalStatus(recordId: string, status: StatusOption, justification?: string): void {
    const now = new Date().toISOString();
    this.records = this.records.map((item) =>
      item.id === recordId
        ? {
            ...item,
            status,
            statusHistory: [...(item.statusHistory ?? []), { status, changedAt: now, justification }]
          }
        : item
    );
    this.justification[recordId] = '';
  }
}
