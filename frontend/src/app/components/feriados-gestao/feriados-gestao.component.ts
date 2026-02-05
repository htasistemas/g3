import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { FeriadoPayload, FeriadoService } from '../../services/feriado.service';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { ConfigAcoesCrud, EstadoAcoesCrud, TelaBaseComponent } from '../compartilhado/tela-base.component';
import { PopupErrorBuilder } from '../../utils/popup-error.builder';

interface StepTab {
  id: string;
  label: string;
}

@Component({
  selector: 'app-feriados-gestao',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, TelaPadraoComponent, PopupMessagesComponent],
  templateUrl: './feriados-gestao.component.html',
  styleUrl: './feriados-gestao.component.scss'
})
export class FeriadosGestaoComponent extends TelaBaseComponent implements OnInit {
  form: FormGroup;
  filtroForm: FormGroup;
  feriados: FeriadoPayload[] = [];
  feriadosFiltrados: FeriadoPayload[] = [];
  feriadoSelecionado: FeriadoPayload | null = null;
  editingId: string | null = null;
  saving = false;
  popupErros: string[] = [];
  popupTitulo = 'Aviso';

  tabs: StepTab[] = [
    { id: 'cadastro', label: 'Cadastro de feriados' },
    { id: 'listagem', label: 'Feriados cadastrados' }
  ];
  activeTab = 'cadastro';

  readonly acoesToolbar: Required<ConfigAcoesCrud> = this.criarConfigAcoes({
    buscar: true,
    novo: true,
    salvar: true,
    cancelar: true,
    excluir: true,
    imprimir: true
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly feriadoService: FeriadoService
  ) {
    super();
    this.form = this.fb.group({
      data: ['', Validators.required],
      descricao: ['', Validators.required]
    });
    this.filtroForm = this.fb.group({
      data: [''],
      descricao: ['']
    });
  }

  ngOnInit(): void {
    this.carregarFeriados();
  }

  get acoesDesabilitadas(): EstadoAcoesCrud {
    return {
      salvar: this.saving,
      cancelar: this.saving,
      novo: this.saving,
      excluir: !this.editingId,
      imprimir: this.saving,
      buscar: this.saving
    };
  }

  get activeTabIndex(): number {
    return this.tabs.findIndex((tab) => tab.id === this.activeTab);
  }

  get hasNextTab(): boolean {
    return this.activeTabIndex < this.tabs.length - 1;
  }

  get hasPreviousTab(): boolean {
    return this.activeTabIndex > 0;
  }

  get nextTabLabel(): string {
    return this.hasNextTab ? this.tabs[this.activeTabIndex + 1].label : '';
  }

  changeTab(tab: string): void {
    this.activeTab = tab;
  }

  goToNextTab(): void {
    if (this.hasNextTab) {
      this.changeTab(this.tabs[this.activeTabIndex + 1].id);
    }
  }

  goToPreviousTab(): void {
    if (this.hasPreviousTab) {
      this.changeTab(this.tabs[this.activeTabIndex - 1].id);
    }
  }

  submit(): void {
    if (!this.form.valid) {
      this.form.markAllAsTouched();
      this.popupTitulo = 'Campos obrigatorios';
      this.popupErros = new PopupErrorBuilder()
        .adicionar('Preencha os campos obrigatorios antes de salvar.')
        .build();
      return;
    }
    this.saving = true;
    const payload: FeriadoPayload = {
      data: this.form.value.data,
      descricao: this.form.value.descricao
    };

    if (this.editingId) {
      this.feriadoService.atualizar(this.editingId, payload).subscribe({
        next: () => {
          this.popupTitulo = 'Sucesso';
          this.popupErros = new PopupErrorBuilder()
            .adicionar('Feriado atualizado com sucesso.')
            .build();
          this.carregarFeriados();
          this.resetForm();
        },
        error: () => {
          this.popupTitulo = 'Erro ao salvar';
          this.popupErros = new PopupErrorBuilder()
            .adicionar('Nao foi possivel salvar o feriado.')
            .build();
        },
        complete: () => {
          this.saving = false;
        }
      });
      return;
    }

    this.feriadoService.criar(payload).subscribe({
      next: () => {
        this.popupTitulo = 'Sucesso';
        this.popupErros = new PopupErrorBuilder()
          .adicionar('Feriado cadastrado com sucesso.')
          .build();
        this.carregarFeriados();
        this.resetForm();
      },
      error: () => {
        this.popupTitulo = 'Erro ao salvar';
        this.popupErros = new PopupErrorBuilder()
          .adicionar('Nao foi possivel salvar o feriado.')
          .build();
      },
      complete: () => {
        this.saving = false;
      }
    });
  }

  excluirSelecionado(): void {
    if (!this.editingId) return;
    this.feriadoService.excluir(this.editingId).subscribe({
      next: () => {
        this.popupTitulo = 'Sucesso';
        this.popupErros = new PopupErrorBuilder()
          .adicionar('Feriado excluido com sucesso.')
          .build();
        this.carregarFeriados();
        this.resetForm();
      },
      error: () => {
        this.popupTitulo = 'Erro ao excluir';
        this.popupErros = new PopupErrorBuilder()
          .adicionar('Nao foi possivel excluir o feriado.')
          .build();
      }
    });
  }

  startNew(): void {
    this.resetForm();
  }

  resetForm(): void {
    this.form.reset({ data: '', descricao: '' });
    this.editingId = null;
    this.feriadoSelecionado = null;
    this.changeTab('cadastro');
  }

  onBuscar(): void {
    this.changeTab('listagem');
  }

  closeForm(): void {
    window.history.back();
  }

  fecharPopupErros(): void {
    this.popupErros = [];
  }

  aplicarFiltro(): void {
    const filtroData = (this.filtroForm.value.data || '').trim();
    const filtroDescricao = (this.filtroForm.value.descricao || '').trim().toLowerCase();
    this.feriadosFiltrados = this.feriados.filter((feriado) => {
      const dataOk = !filtroData || feriado.data === filtroData;
      const descOk =
        !filtroDescricao || (feriado.descricao || '').toLowerCase().includes(filtroDescricao);
      return dataOk && descOk;
    });
  }

  limparFiltro(): void {
    this.filtroForm.reset({ data: '', descricao: '' });
    this.feriadosFiltrados = [...this.feriados];
  }

  selecionarFeriado(feriado: FeriadoPayload): void {
    this.feriadoSelecionado = feriado;
    this.editingId = feriado.id ?? null;
    this.form.patchValue({
      data: feriado.data,
      descricao: feriado.descricao
    });
    this.changeTab('cadastro');
  }

  imprimirLista(): void {
    const linhas = this.feriadosFiltrados.length ? this.feriadosFiltrados : this.feriados;
    const w = window.open('', '_blank', 'width=900,height=900');
    if (!w) return;
    const linhasHtml = linhas
      .map(
        (item) =>
          `<tr><td>${item.data}</td><td>${item.descricao}</td></tr>`
      )
      .join('');
    w.document.write(`
      <html>
        <head>
          <title>Relatorio de feriados</title>
          <style>
            @page { size: A4; margin: 20mm; }
            body { font-family: Arial, sans-serif; font-size: 12pt; color: #000; }
            h1 { font-size: 14pt; margin-bottom: 12px; }
            table { width: 100%; border-collapse: collapse; }
            th, td { border: 1px solid #000; padding: 6px; text-align: left; }
            th { background: #f3f4f6; }
          </style>
        </head>
        <body>
          <h1>Relatorio de feriados</h1>
          <table>
            <thead>
              <tr><th>Data</th><th>Descricao</th></tr>
            </thead>
            <tbody>${linhasHtml}</tbody>
          </table>
        </body>
      </html>
    `);
    w.document.close();
    w.focus();
    w.onload = () => w.print();
  }

  private carregarFeriados(): void {
    this.feriadoService.listar().subscribe({
      next: (feriados) => {
        this.feriados = feriados ?? [];
        this.feriadosFiltrados = [...this.feriados];
      },
      error: () => {
        this.feriados = [];
        this.feriadosFiltrados = [];
        this.popupTitulo = 'Erro ao carregar';
        this.popupErros = new PopupErrorBuilder()
          .adicionar('Nao foi possivel carregar os feriados.')
          .build();
      }
    });
  }
}

