import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  PlanoTrabalhoService,
  PlanoTrabalho,
  PlanoTrabalhoPayload,
  PlanoStatus
} from '../../services/plano-trabalho.service';
import { TermoFomentoPayload, TermoFomentoService } from '../../services/termo-fomento.service';

import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
interface StepTab {
  id: string;
  label: string;
}

@Component({
  selector: 'app-plano-trabalho-gestao',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TelaPadraoComponent],
  templateUrl: './plano-trabalho-gestao.component.html',
  styleUrl: './plano-trabalho-gestao.component.scss'
})
export class PlanoTrabalhoGestaoComponent implements OnInit {
  form: FormGroup;
  activeTab = 'identificacao';
  saving = false;
  feedback: string | null = null;
  editingId: string | null = null;

  tabs: StepTab[] = [
    { id: 'identificacao', label: 'Identificação' },
    { id: 'vinculo', label: 'Vinculação ao Termo' },
    { id: 'metas', label: 'Metas e Atividades' },
    { id: 'cronograma', label: 'Cronograma Físico-Financeiro' },
    { id: 'equipe', label: 'Equipe / Responsáveis' },
    { id: 'arquivos', label: 'Arquivos e Exportação' }
  ];

  statusOptions: { value: PlanoStatus; label: string }[] = [
    { value: 'EM_ELABORACAO', label: 'Em elaboração' },
    { value: 'ENVIADO_ANALISE', label: 'Enviado para análise' },
    { value: 'APROVADO', label: 'Aprovado' },
    { value: 'EM_EXECUCAO', label: 'Em execução' },
    { value: 'CONCLUIDO', label: 'Concluído' },
    { value: 'REPROVADO', label: 'Reprovado' }
  ];

  orgaoOptions = ['União', 'Estado', 'Município', 'Outro'];
  statusEtapas = ['NAO_INICIADA', 'EM_ANDAMENTO', 'CONCLUIDA', 'ATRASADA'];
  fonteRecursos = ['União', 'Estado', 'Município', 'Contrapartida', 'Outros'];

  planos: PlanoTrabalho[] = [];
  termos: TermoFomentoPayload[] = [];

  constructor(
    private readonly fb: FormBuilder,
    private readonly planoService: PlanoTrabalhoService,
    private readonly termoService: TermoFomentoService
  ) {
    this.form = this.buildForm();
  }

  ngOnInit(): void {
    this.loadPlanos();
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

  get metas(): FormArray {
    return this.form.get('metas') as FormArray;
  }

  get cronograma(): FormArray {
    return this.form.get('cronograma') as FormArray;
  }

  get equipe(): FormArray {
    return this.form.get('equipe') as FormArray;
  }

  get selectedTermo(): TermoFomentoPayload | undefined {
    const termoId = this.form.get('vinculo.termoFomentoId')?.value;
    return this.termos.find((t) => t.id === termoId);
  }

  get edicaoRestrita(): boolean {
    const status = this.form.get('identificacao.status')?.value as PlanoStatus;
    return status === 'APROVADO' || status === 'EM_EXECUCAO' || status === 'CONCLUIDO';
  }

  changeTab(tab: string): void {
    this.activeTab = tab;
  }

  goToNextTab(): void {
    if (this.hasNextTab) this.changeTab(this.tabs[this.activeTabIndex + 1].id);
  }

  goToPreviousTab(): void {
    if (this.hasPreviousTab) this.changeTab(this.tabs[this.activeTabIndex - 1].id);
  }

  buildForm(): FormGroup {
    return this.fb.group({
      identificacao: this.fb.group({
        codigoInterno: [{ value: '', disabled: true }],
        titulo: ['', Validators.required],
        descricaoGeral: ['', Validators.required],
        status: ['EM_ELABORACAO', Validators.required],
        orgaoConcedente: ['União'],
        orgaoOutroDescricao: [''],
        areaPrograma: [''],
        dataElaboracao: [''],
        dataAprovacao: [''],
        vigenciaInicio: [''],
        vigenciaFim: ['']
      }),
      vinculo: this.fb.group({
        termoFomentoId: ['', Validators.required],
        numeroProcesso: [''],
        modalidade: [''],
        observacoesVinculacao: ['']
      }),
      metas: this.fb.array([]),
      cronograma: this.fb.array([]),
      equipe: this.fb.array([]),
      arquivos: this.fb.group({
        formato: ['PDF']
      })
    });
  }

  addMeta(meta?: any): void {
    this.metas.push(
      this.fb.group({
        id: [meta?.id],
        codigo: [meta?.codigo || ''],
        descricao: [meta?.descricao || '', Validators.required],
        indicador: [meta?.indicador || ''],
        unidadeMedida: [meta?.unidadeMedida || ''],
        quantidadePrevista: [meta?.quantidadePrevista || null],
        resultadoEsperado: [meta?.resultadoEsperado || ''],
        atividades: this.fb.array([])
      })
    );

    const metaIndex = this.metas.length - 1;
    (meta?.atividades || []).forEach((atividade: any) => this.addAtividade(metaIndex, atividade));
  }

  removeMeta(index: number): void {
    this.metas.removeAt(index);
  }

  atividades(metaIndex: number): FormArray {
    return this.metas.at(metaIndex).get('atividades') as FormArray;
  }

  addAtividade(metaIndex: number, atividade?: any): void {
    this.atividades(metaIndex).push(
      this.fb.group({
        id: [atividade?.id],
        descricao: [atividade?.descricao || '', Validators.required],
        justificativa: [atividade?.justificativa || ''],
        publicoAlvo: [atividade?.publicoAlvo || ''],
        localExecucao: [atividade?.localExecucao || ''],
        produtoEsperado: [atividade?.produtoEsperado || ''],
        etapas: this.fb.array([])
      })
    );

    const atividadeIndex = this.atividades(metaIndex).length - 1;
    (atividade?.etapas || []).forEach((etapa: any) => this.addEtapa(metaIndex, atividadeIndex, etapa));
  }

  removeAtividade(metaIndex: number, atividadeIndex: number): void {
    this.atividades(metaIndex).removeAt(atividadeIndex);
  }

  etapas(metaIndex: number, atividadeIndex: number): FormArray {
    return this.atividades(metaIndex).at(atividadeIndex).get('etapas') as FormArray;
  }

  addEtapa(metaIndex: number, atividadeIndex: number, etapa?: any): void {
    this.etapas(metaIndex, atividadeIndex).push(
      this.fb.group({
        id: [etapa?.id],
        descricao: [etapa?.descricao || '', Validators.required],
        status: [etapa?.status || 'NAO_INICIADA'],
        dataInicioPrevista: [etapa?.dataInicioPrevista || ''],
        dataFimPrevista: [etapa?.dataFimPrevista || ''],
        dataConclusao: [etapa?.dataConclusao || ''],
        responsavel: [etapa?.responsavel || '']
      })
    );
  }

  removeEtapa(metaIndex: number, atividadeIndex: number, etapaIndex: number): void {
    this.etapas(metaIndex, atividadeIndex).removeAt(etapaIndex);
  }

  addCronogramaItem(item?: any): void {
    this.cronograma.push(
      this.fb.group({
        id: [item?.id],
        referenciaTipo: [item?.referenciaTipo || ''],
        referenciaId: [item?.referenciaId || ''],
        referenciaDescricao: [item?.referenciaDescricao || ''],
        competencia: [item?.competencia || '', Validators.required],
        descricaoResumida: [item?.descricaoResumida || ''],
        valorPrevisto: [item?.valorPrevisto ?? null],
        fonteRecurso: [item?.fonteRecurso || ''],
        naturezaDespesa: [item?.naturezaDespesa || ''],
        observacoes: [item?.observacoes || '']
      })
    );
  }

  removeCronogramaItem(index: number): void {
    this.cronograma.removeAt(index);
  }

  addResponsavel(membro?: any): void {
    this.equipe.push(
      this.fb.group({
        id: [membro?.id],
        nome: [membro?.nome || '', Validators.required],
        funcao: [membro?.funcao || ''],
        cpf: [membro?.cpf || ''],
        cargaHoraria: [membro?.cargaHoraria || ''],
        tipoVinculo: [membro?.tipoVinculo || ''],
        contato: [membro?.contato || '']
      })
    );
  }

  removeResponsavel(index: number): void {
    this.equipe.removeAt(index);
  }

  referenciasPlanejamento(): { id: string; label: string; tipo: string }[] {
    const refs: { id: string; label: string; tipo: string }[] = [];
    this.metas.controls.forEach((metaControl, metaIndex) => {
      const metaValue = metaControl.value;
      refs.push({ id: metaValue.id || `meta-${metaIndex}`, label: `Meta: ${metaValue.descricao}`, tipo: 'Meta' });
      this.atividades(metaIndex).controls.forEach((atividadeControl, atvIndex) => {
        const atvValue = atividadeControl.value;
        const atvId = atvValue.id || `meta-${metaIndex}-atv-${atvIndex}`;
        refs.push({ id: atvId, label: `Atividade: ${atvValue.descricao}`, tipo: 'Atividade' });
        this.etapas(metaIndex, atvIndex).controls.forEach((etapaControl, etapaIndex) => {
          const etapaValue = etapaControl.value;
          refs.push({
            id: etapaValue.id || `${atvId}-etapa-${etapaIndex}`,
            label: `Etapa: ${etapaValue.descricao}`,
            tipo: 'Etapa'
          });
        });
      });
    });
    return refs;
  }

  submit(): void {
    this.feedback = null;
    this.saving = true;
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.feedback = 'Preencha os campos obrigatórios para prosseguir.';
      this.saving = false;
      return;
    }

    const identificacao = this.form.get('identificacao')!.getRawValue();
    const vinculo = this.form.get('vinculo')!.value;

    if (identificacao.vigenciaInicio && identificacao.vigenciaFim && identificacao.vigenciaInicio > identificacao.vigenciaFim) {
      this.feedback = 'A data inicial da vigência deve ser anterior ou igual à final.';
      this.saving = false;
      this.activeTab = 'identificacao';
      return;
    }

    if (!vinculo.termoFomentoId) {
      this.feedback = 'Selecione um Termo de Fomento para salvar o plano de trabalho.';
      this.saving = false;
      this.activeTab = 'vinculo';
      return;
    }

    const payload: PlanoTrabalhoPayload = {
      ...identificacao,
      termoFomentoId: vinculo.termoFomentoId,
      numeroProcesso: vinculo.numeroProcesso,
      modalidade: vinculo.modalidade,
      observacoesVinculacao: vinculo.observacoesVinculacao,
      metas: this.metas.value,
      cronograma: this.cronograma.value,
      equipe: this.equipe.value,
      status: identificacao.status
    };

    const request$ = this.editingId
      ? this.planoService.update(this.editingId, payload)
      : this.planoService.create(payload);

    request$.subscribe({
      next: (plano) => {
        this.saving = false;
        this.feedback = 'Plano de trabalho salvo com sucesso.';
        this.editingId = plano.id;
        this.loadPlanos();
        this.patchForm(plano);
      },
      error: () => {
        this.saving = false;
        this.feedback = 'Não foi possível salvar o plano de trabalho.';
      }
    });
  }

  patchForm(plano: PlanoTrabalho): void {
    this.metas.clear();
    this.cronograma.clear();
    this.equipe.clear();

    const termoId = (plano as any).termoFomentoId || plano.termoFomento?.id || '';

    this.form.patchValue({
      identificacao: {
        codigoInterno: plano.codigoInterno,
        titulo: plano.titulo,
        descricaoGeral: plano.descricaoGeral,
        status: plano.status,
        orgaoConcedente: plano.orgaoConcedente,
        orgaoOutroDescricao: plano.orgaoOutroDescricao,
        areaPrograma: plano.areaPrograma,
        dataElaboracao: plano.dataElaboracao,
        dataAprovacao: plano.dataAprovacao,
        vigenciaInicio: plano.vigenciaInicio,
        vigenciaFim: plano.vigenciaFim
      },
      vinculo: {
        termoFomentoId: termoId,
        numeroProcesso: plano.numeroProcesso,
        modalidade: plano.modalidade,
        observacoesVinculacao: plano.observacoesVinculacao
      }
    });

    (plano.metas || []).forEach((meta) => this.addMeta(meta));
    (plano.cronograma || []).forEach((item) => this.addCronogramaItem(item));
    (plano.equipe || []).forEach((membro) => this.addResponsavel(membro));
  }

  editPlano(plano: PlanoTrabalho): void {
    this.editingId = plano.id;
    this.feedback = null;
    this.patchForm(plano);
    this.activeTab = 'identificacao';
  }

  startNovo(): void {
    this.editingId = null;
    this.feedback = null;
    this.form = this.buildForm();
    this.metas.clear();
    this.cronograma.clear();
    this.equipe.clear();
    this.changeTab('identificacao');
  }

  deletePlano(plano: PlanoTrabalho): void {
    if (!confirm('Deseja remover o plano selecionado?')) return;
    this.planoService.delete(plano.id).subscribe(() => {
      this.loadPlanos();
      if (this.editingId === plano.id) {
        this.startNovo();
      }
    });
  }

  loadPlanos(): void {
    this.planoService.list().subscribe((planos) => {
      this.planos = planos;
    });
  }

  loadTermos(): void {
    this.termos = this.termoService.list();
  }

  totalCronogramaPorFonte(): Record<string, number> {
    return this.cronograma.value.reduce((acc: Record<string, number>, item: any) => {
      const fonte = item.fonteRecurso || 'Não informado';
      const valor = item.valorPrevisto ? Number(item.valorPrevisto) : 0;
      acc[fonte] = (acc[fonte] || 0) + valor;
      return acc;
    }, {});
  }

  totalCronograma(): number {
    return Object.values(this.totalCronogramaPorFonte()).reduce((sum, value) => sum + value, 0);
  }

  statusLabel(status: string): string {
    return this.statusOptions.find((item) => item.value === status)?.label || status;
  }

  gerarArquivo(planoSelecionado?: PlanoTrabalho): void {
    const targetId = planoSelecionado?.id || this.editingId;
    if (!targetId) {
      this.feedback = 'Salve o plano antes de gerar o arquivo de exportação.';
      this.changeTab('identificacao');
      return;
    }

    this.planoService.export(targetId).subscribe((payload) => {
      const popup = window.open('', '_blank', 'width=900,height=700');
      if (!popup) return;
      popup.document.write('<pre>' + JSON.stringify(payload, null, 2) + '</pre>');
    });
  }
}
