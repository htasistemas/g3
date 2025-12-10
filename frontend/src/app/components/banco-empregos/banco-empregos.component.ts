import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-banco-empregos',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './banco-empregos.component.html',
  styleUrl: './banco-empregos.component.scss'
})
export class BancoEmpregosComponent {
  readonly tabs = [
    { id: 'dadosVaga', label: 'Dados da Vaga' },
    { id: 'empresaLocal', label: 'Empresa e Local' },
    { id: 'requisitos', label: 'Requisitos e Descrição' },
    { id: 'encaminhamentos', label: 'Encaminhamentos' }
  ] as const;

  activeTab: (typeof this.tabs)[number]['id'] = 'dadosVaga';
  feedback: string | null = null;
  form: FormGroup;
  encaminhamentos = [
    { beneficiario: '—', data: '—', status: '—', observacoes: '—' }
  ];

  constructor(private readonly fb: FormBuilder, private readonly router: Router) {
    this.form = this.fb.group({
      dadosVaga: this.fb.group({
        titulo: [''],
        status: ['Aberta'],
        dataAbertura: [''],
        dataEncerramento: [''],
        tipoContrato: [''],
        cargaHoraria: [''],
        salario: ['']
      }),
      empresaLocal: this.fb.group({
        nomeEmpresa: [''],
        cnpj: [''],
        cidade: [''],
        endereco: [''],
        bairro: ['']
      }),
      requisitos: this.fb.group({
        requisitos: [''],
        descricao: [''],
        observacoes: ['']
      })
    });
  }

  get activeTabIndex(): number {
    return this.tabs.findIndex((tab) => tab.id === this.activeTab);
  }

  changeTab(tabId: (typeof this.tabs)[number]['id']): void {
    this.activeTab = tabId;
  }

  startNew(): void {
    this.form.reset({
      dadosVaga: { status: 'Aberta' },
      empresaLocal: {},
      requisitos: {}
    });
    this.feedback = null;
    this.activeTab = 'dadosVaga';
  }

  save(): void {
    this.feedback = 'Layout preparado. Integração com o backend permanece inalterada.';
  }

  cancel(): void {
    this.startNew();
  }

  close(): void {
    this.router.navigate(['/atendimentos/cursos']);
  }

  print(): void {
    window.print();
  }
}
