import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { environment } from '../../../environments/environment';

interface ReleaseNote {
  version: string;
  date: string;
  highlights: string[];
}

@Component({
  selector: 'app-system-version',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './system-version.component.html',
  styleUrl: './system-version.component.scss'
})
export class SystemVersionComponent {
  readonly currentVersion = environment.version;
  readonly formattedVersion = this.formatVersion(environment.version);
  readonly releaseNotes: ReleaseNote[] = [
    {
      version: this.formattedVersion,
      date: 'Última atualização',
      highlights: [
        'Ajustes visuais na navegação do cadastro de beneficiários',
        'Padronização automática de texto durante o preenchimento dos formulários',
        'Cálculo instantâneo de idade a partir da data de nascimento'
      ]
    },
    {
      version: '1.004',
      date: '15/04/2024',
      highlights: [
        'Melhorias de desempenho no carregamento das listas',
        'Otimizações gerais na experiência de cadastro'
      ]
    }
  ];

  private formatVersion(version: string): string {
    const cleaned = version.trim().replace(/^v/i, '');
    const segments = cleaned.split('.').filter(Boolean);

    if (!segments.length) {
      return '0.000';
    }

    const [major = '0', minor = '0', patch = '0'] = segments;
    const numericCombined = `${minor}${patch}`.replace(/\D/g, '');
    const paddedValue = numericCombined.padStart(3, '0').slice(-3);

    return `${major}.${paddedValue}`;
  }
}
