import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
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
  readonly statusOptions: StatusOption[] = ['TRIAGEM', 'EM_ANDAMENTO', 'ENCAMINHADO', 'EM_VISITA', 'CONCLUIDO'];
  justification: Record<string, string> = {};

  constructor(private readonly service: CursosAtendimentosService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.service.list().subscribe({
      next: (records) => {
        this.records = records.filter((r) => r.tipo === 'Atendimento');
        this.loading = false;
      },
      error: () => {
        this.feedback = 'Não foi possível carregar os atendimentos.';
        this.loading = false;
      }
    });
  }

  updateStatus(record: CourseRecord, status: StatusOption): void {
    this.feedback = null;
    this.service.updateStatus(record.id, { status, justification: this.justification[record.id] }).subscribe({
      next: (updated) => {
        this.records = this.records.map((item) => (item.id === updated.id ? updated : item));
        this.justification[record.id] = '';
      },
      error: (err) => {
        this.feedback = err?.error?.message ?? 'Falha ao atualizar status.';
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
}
