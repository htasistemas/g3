import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  AditivoPayload,
  SituacaoTermo,
  TermoDocumento,
  TermoFomentoPayload,
  TermoFomentoService,
  TipoTermo
} from '../../services/termo-fomento.service';

interface StepTab {
  id: string;
  label: string;
}

@Component({
  selector: 'app-termos-fomento-gestao',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './termos-fomento-gestao.component.html',
  styleUrl: './termos-fomento-gestao.component.scss'
})
export class TermosFomentoGestaoComponent {
  form: FormGroup;
  activeTab = 'dados';
  saving = false;
  feedback: string | null = null;
  editingTermoId: string | null = null;

  tabs: StepTab[] = [
    { id: 'dados', label: 'Dados do Termo' },
    { id: 'anexos', label: 'Anexos' },
    { id: 'aditivos', label: 'Histórico / Aditivos' }
  ];

  termos: TermoFomentoPayload[] = [];
  filteredTermos: TermoFomentoPayload[] = [];

  search = '';
  situacaoFiltro: SituacaoTermo | '' = '';
  somenteVencidos = false;

  constructor(private readonly fb: FormBuilder, private readonly termoService: TermoFomentoService) {
    this.form = this.fb.group({
      dadosTermo: this.fb.group({
        numeroTermo: ['', Validators.required],
        tipoTermo: ['Uniao' as TipoTermo, Validators.required],
        orgaoConcedente: [''],
        dataAssinatura: [''],
        dataInicioVigencia: [''],
        dataFimVigencia: [''],
        situacao: ['Ativo' as SituacaoTermo, Validators.required],
        descricaoObjeto: [''],
        valorGlobal: [''],
        responsavelInterno: ['']
      }),
      anexos: this.fb.group({
        termoDocumento: this.fb.control<TermoDocumento | null>(null),
        documentosRelacionados: this.fb.control<TermoDocumento[]>([])
      }),
      aditivo: this.fb.group({
        tipoAditivo: ['', Validators.required],
        dataAditivo: ['', Validators.required],
        novaDataFim: [''],
        novoValor: [''],
        observacoes: [''],
        anexo: this.fb.control<TermoDocumento | null>(null)
      })
    });

    this.loadTermos();
  }

  get activeTabIndex(): number {
    return this.tabs.findIndex((tab) => tab.id === this.activeTab);
  }

  get hasPreviousTab(): boolean {
    return this.activeTabIndex > 0;
  }

  get hasNextTab(): boolean {
    return this.activeTabIndex < this.tabs.length - 1;
  }

  get nextTabLabel(): string {
    return this.hasNextTab ? this.tabs[this.activeTabIndex + 1].label : '';
  }

  get termoEmEdicao(): TermoFomentoPayload | undefined {
    return this.editingTermoId ? this.termos.find((t) => t.id === this.editingTermoId) : undefined;
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

  loadTermos(): void {
    this.termos = this.termoService.list();
    this.applyFilters();
  }

  applyFilters(): void {
    const query = this.search.toLowerCase();
    this.filteredTermos = this.termos.filter((termo) => {
      const matchesQuery =
        !query ||
        termo.numeroTermo.toLowerCase().includes(query) ||
        (termo.orgaoConcedente ?? '').toLowerCase().includes(query) ||
        (termo.responsavelInterno ?? '').toLowerCase().includes(query);
      const matchesSituacao = !this.situacaoFiltro || termo.situacao === this.situacaoFiltro;
      const matchesVencido = !this.somenteVencidos || this.isVencido(termo);
      return matchesQuery && matchesSituacao && matchesVencido;
    });
  }

  resetForm(): void {
    this.form.reset({
      dadosTermo: {
        numeroTermo: '',
        tipoTermo: 'Uniao' as TipoTermo,
        orgaoConcedente: '',
        dataAssinatura: '',
        dataInicioVigencia: '',
        dataFimVigencia: '',
        situacao: 'Ativo' as SituacaoTermo,
        descricaoObjeto: '',
        valorGlobal: '',
        responsavelInterno: ''
      },
      anexos: {
        termoDocumento: null,
        documentosRelacionados: []
      },
      aditivo: {
        tipoAditivo: '',
        dataAditivo: '',
        novaDataFim: '',
        novoValor: '',
        observacoes: '',
        anexo: null
      }
    });
    this.editingTermoId = null;
    this.feedback = null;
    this.activeTab = 'dados';
  }

  submit(): void {
    if (this.form.invalid) {
      this.feedback = 'Preencha os campos obrigatórios antes de salvar.';
      return;
    }

    this.saving = true;
    const value = this.form.value;
    const payload: TermoFomentoPayload = {
      id: this.editingTermoId ?? undefined,
      numeroTermo: value.dadosTermo?.numeroTermo ?? '',
      tipoTermo: value.dadosTermo?.tipoTermo ?? 'Uniao',
      orgaoConcedente: value.dadosTermo?.orgaoConcedente ?? '',
      dataAssinatura: value.dadosTermo?.dataAssinatura ?? '',
      dataInicioVigencia: value.dadosTermo?.dataInicioVigencia ?? '',
      dataFimVigencia: value.dadosTermo?.dataFimVigencia ?? '',
      situacao: value.dadosTermo?.situacao ?? 'Ativo',
      descricaoObjeto: value.dadosTermo?.descricaoObjeto ?? '',
      valorGlobal: value.dadosTermo?.valorGlobal ? Number(value.dadosTermo.valorGlobal) : undefined,
      responsavelInterno: value.dadosTermo?.responsavelInterno ?? '',
      termoDocumento: value.anexos?.termoDocumento ?? null,
      documentosRelacionados: value.anexos?.documentosRelacionados ?? [],
      aditivos: this.editingTermoId ? this.termoService.getById(this.editingTermoId)?.aditivos ?? [] : []
    };

    if (this.editingTermoId) {
      this.termoService.update(this.editingTermoId, payload);
      this.feedback = 'Termo atualizado com sucesso.';
    } else {
      const created = this.termoService.create(payload);
      this.editingTermoId = created.id ?? null;
      this.feedback = 'Termo criado e salvo.';
    }

    this.loadTermos();
    this.saving = false;
  }

  editTermo(termo: TermoFomentoPayload): void {
    this.editingTermoId = termo.id ?? null;
    this.form.patchValue({
      dadosTermo: {
        numeroTermo: termo.numeroTermo,
        tipoTermo: termo.tipoTermo,
        orgaoConcedente: termo.orgaoConcedente,
        dataAssinatura: termo.dataAssinatura,
        dataInicioVigencia: termo.dataInicioVigencia,
        dataFimVigencia: termo.dataFimVigencia,
        situacao: termo.situacao,
        descricaoObjeto: termo.descricaoObjeto,
        valorGlobal: termo.valorGlobal,
        responsavelInterno: termo.responsavelInterno
      },
      anexos: {
        termoDocumento: termo.termoDocumento ?? null,
        documentosRelacionados: termo.documentosRelacionados ?? []
      }
    });
    this.activeTab = 'dados';
    this.feedback = 'Editando termo selecionado.';
  }

  deleteTermo(termo: TermoFomentoPayload): void {
    if (!termo.id) return;
    this.termoService.delete(termo.id);
    this.loadTermos();
    if (this.editingTermoId === termo.id) {
      this.resetForm();
    }
  }

  saveAditivo(): void {
    if (!this.editingTermoId) {
      this.feedback = 'Salve o termo antes de registrar um aditivo.';
      return;
    }

    const value = this.form.get('aditivo')?.value;
    if (!value?.tipoAditivo || !value?.dataAditivo) {
      this.feedback = 'Preencha os campos obrigatórios do aditivo.';
      return;
    }

    const aditivo: AditivoPayload = {
      tipoAditivo: value.tipoAditivo,
      dataAditivo: value.dataAditivo,
      novaDataFim: value.novaDataFim,
      novoValor: value.novoValor ? Number(value.novoValor) : undefined,
      observacoes: value.observacoes,
      anexo: value.anexo ?? null
    };

    const updated = this.termoService.addAditivo(this.editingTermoId, aditivo);
    if (updated) {
      this.feedback = 'Aditivo salvo e histórico atualizado.';
      this.loadTermos();
      const termoAtualizado = this.termoService.getById(this.editingTermoId);
      this.form.patchValue({
        dadosTermo: {
          dataFimVigencia: termoAtualizado?.dataFimVigencia,
          valorGlobal: termoAtualizado?.valorGlobal,
          situacao: termoAtualizado?.situacao
        }
      });
      this.form.get('aditivo')?.reset({
        tipoAditivo: '',
        dataAditivo: '',
        novaDataFim: '',
        novoValor: '',
        observacoes: '',
        anexo: null
      });
      this.activeTab = 'aditivos';
    }
  }

  onFileSelected(event: Event, controlPath: (string | number)[], tipo: TermoDocumento['tipo']): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      const documento: TermoDocumento = {
        id: crypto.randomUUID(),
        nome: file.name,
        dataUrl: typeof reader.result === 'string' ? reader.result : undefined,
        tipo
      };

      const control = this.form.get(controlPath);
      if (Array.isArray(control?.value)) {
        control?.setValue([documento, ...(control.value as TermoDocumento[])]);
      } else {
        control?.setValue(documento);
      }
    };

    reader.readAsDataURL(file);
    input.value = '';
  }

  isVencido(termo: TermoFomentoPayload): boolean {
    if (!termo.dataFimVigencia) return false;
    const fim = new Date(termo.dataFimVigencia);
    const hoje = new Date();
    return fim.getTime() < hoje.setHours(0, 0, 0, 0);
  }

  isProximoVencimento(termo: TermoFomentoPayload): boolean {
    if (!termo.dataFimVigencia || this.isVencido(termo)) return false;
    const fim = new Date(termo.dataFimVigencia).getTime();
    const hoje = new Date().setHours(0, 0, 0, 0);
    const diasRestantes = (fim - hoje) / (1000 * 60 * 60 * 24);
    return diasRestantes <= 30;
  }

  statusBadge(termo: TermoFomentoPayload): { label: string; tone: 'danger' | 'warning' | 'info' | 'success' } | null {
    if (this.isVencido(termo)) {
      return { label: 'Vencido', tone: 'danger' };
    }
    if (this.isProximoVencimento(termo)) {
      return { label: 'Próximo do vencimento', tone: 'warning' };
    }
    if (termo.situacao === 'Aditivado') {
      return { label: 'Aditivado', tone: 'info' };
    }
    return { label: 'Em vigor', tone: 'success' };
  }

  formatCurrency(value?: number): string {
    if (value === undefined || value === null || Number.isNaN(value)) return '—';
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
  }

  formatDate(value?: string): string {
    if (!value) return '—';
    const date = new Date(value);
    if (Number.isNaN(date.getTime())) return value;
    return new Intl.DateTimeFormat('pt-BR').format(date);
  }

  visualizarTermo(termo: TermoFomentoPayload): void {
    if (!termo.termoDocumento?.dataUrl) {
      alert('Ainda não há PDF do termo de fomento vinculado.');
      return;
    }

    const win = window.open('', '_blank');
    if (!win) return;
    win.document.write(`<iframe src="${termo.termoDocumento.dataUrl}" style="width:100%;height:100%" frameborder="0"></iframe>`);
  }
}
