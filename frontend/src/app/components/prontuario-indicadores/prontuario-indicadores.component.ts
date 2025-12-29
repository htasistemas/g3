import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-prontuario-indicadores',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './prontuario-indicadores.component.html',
  styleUrl: './prontuario-indicadores.component.scss'
})
export class ProntuarioIndicadoresComponent {
  @Input() contagens: Record<string, number> = {};

  get indicadores(): { label: string; value: number }[] {
    const mapping: { key: string; label: string }[] = [
      { key: 'atendimento', label: 'Atendimentos' },
      { key: 'procedimento', label: 'Procedimentos' },
      { key: 'encaminhamento', label: 'Encaminhamentos' },
      { key: 'evolucao', label: 'Evoluções' },
      { key: 'visita_domiciliar', label: 'Visitas domiciliares' },
      { key: 'outro', label: 'Outros registros' }
    ];

    return mapping.map((item) => ({
      label: item.label,
      value: this.contagens[item.key] ?? 0
    }));
  }
}
