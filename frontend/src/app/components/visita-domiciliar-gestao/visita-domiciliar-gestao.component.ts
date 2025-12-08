import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import {
  SituacaoVisita,
  VisitaAnexo,
  VisitaDomiciliar,
  VisitaDomiciliarService
} from '../../services/visita-domiciliar.service';
import { BeneficiaryService } from '../../services/beneficiary.service';

interface StepTab {
  id: string;
  label: string;
}

@Component({
  selector: 'app-visita-domiciliar-gestao',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './visita-domiciliar-gestao.component.html',
  styleUrl: './visita-domiciliar-gestao.component.scss'
})
export class VisitaDomiciliarGestaoComponent implements OnInit {
  tabs: StepTab[] = [
    { id: 'identificacao', label: 'Identificação da visita' },
    { id: 'condicoes', label: 'Condições do domicílio' },
    { id: 'social', label: 'Situação familiar e social' },
    { id: 'registro', label: 'Registro da visita' },
    { id: 'anexos', label: 'Anexos' },
    { id: 'historico', label: 'Histórico do beneficiário' }
  ];

  readonly tiposVisita = ['Social', 'Técnica', 'Acompanhamento', 'Retorno'];
  readonly situacoes: SituacaoVisita[] = ['Agendada', 'Em andamento', 'Realizada', 'Cancelada'];
  readonly unidades = ['Unidade Central', 'Polo Norte', 'Polo Sul'];
  readonly responsaveis = ['Equipe Social', 'Equipe Técnica', 'Voluntário dedicado'];
  beneficiarios: string[] = [];
  beneficiariosLoading = false;
  beneficiariosError: string | null = null;

  readonly situacaoMoradia = ['Própria', 'Alugada', 'Cedida', 'Ocupação'];
  readonly situacaoPosse = ['Regular', 'Irregular'];
  readonly saneamento = ['Rede geral', 'Fossa', 'Céu aberto'];
  readonly abastecimentoAgua = ['Rede geral', 'Poço', 'Carro-pipa'];
  readonly energia = ['Regular', 'Ligação alternativa', 'Sem energia'];
  readonly higiene = ['Boa', 'Regular', 'Ruim'];
  readonly riscos = ['Risco estrutural', 'Risco ambiental', 'Violência', 'Sem risco estrutural'];
  readonly beneficiosSociais = ['Bolsa Família', 'BPC', 'Auxílio aluguel', 'Outros'];
  readonly faixasRenda = ['Até 1 salário', '1 a 2 salários', '2 a 3 salários', 'Acima de 3 salários'];

  activeTab: StepTab['id'] = 'identificacao';
  visitForm: FormGroup;
  filterForm: FormGroup;
  anexoForm: FormGroup;

  visitas: VisitaDomiciliar[] = [];
  filteredVisitas: VisitaDomiciliar[] = [];

  feedback: string | null = null;
  saving = false;
  editingId: string | null = null;

  constructor(
    private readonly fb: FormBuilder,
    private readonly visitaService: VisitaDomiciliarService,
    private readonly beneficiaryService: BeneficiaryService
  ) {
    this.visitForm = this.fb.group({
      identificacao: this.fb.group({
        unidade: ['', Validators.required],
        beneficiario: ['', Validators.required],
        responsavel: ['', Validators.required],
        dataVisita: ['', Validators.required],
        horarioInicial: ['', Validators.required],
        horarioFinal: [''],
        tipoVisita: ['Social'],
        situacao: ['Agendada', Validators.required],
        usarEnderecoBeneficiario: [true],
        endereco: this.fb.group({
          logradouro: [''],
          numero: [''],
          bairro: [''],
          cidade: [''],
          uf: [''],
          cep: ['']
        }),
        observacoesIniciais: ['']
      }),
      condicoes: this.fb.group({
        tipoMoradia: [''],
        situacaoPosse: [''],
        comodos: [null],
        saneamento: [''],
        abastecimentoAgua: [''],
        energiaEletrica: [''],
        condicoesHigiene: [''],
        situacaoRisco: this.fb.control<string[]>([]),
        observacoes: ['']
      }),
      situacaoSocial: this.fb.group({
        rendaFamiliar: [''],
        faixaRenda: [''],
        beneficios: this.fb.control<string[]>([]),
        redeApoio: [''],
        vinculos: [''],
        observacoes: ['']
      }),
      registro: this.fb.group({
        relato: [''],
        necessidades: [''],
        encaminhamentos: [''],
        orientacoes: [''],
        plano: ['']
      }),
      anexos: this.fb.control<VisitaAnexo[]>([])
    });

    this.filterForm = this.fb.group({
      dataInicial: [''],
      dataFinal: [''],
      unidade: [''],
      situacao: [''],
      beneficiario: [''],
      responsavel: ['']
    });

    this.anexoForm = this.fb.group({
      nome: ['', Validators.required],
      tipo: ['PDF', Validators.required],
      tamanho: ['']
    });
  }

  ngOnInit(): void {
    this.loadBeneficiarios();
    this.loadVisitas();
  }

  private loadBeneficiarios(): void {
    this.beneficiariosLoading = true;
    this.beneficiariosError = null;

    this.beneficiaryService.list().subscribe({
      next: ({ beneficiarios }) => {
        this.beneficiarios = (beneficiarios ?? []).map(
          (beneficiario) => beneficiario.nomeCompleto || (beneficiario as any).nome_completo || 'Beneficiário'
        );
        this.beneficiariosLoading = false;
      },
      error: () => {
        this.beneficiarios = [];
        this.beneficiariosError = 'Não foi possível carregar os beneficiários.';
        this.beneficiariosLoading = false;
      }
    });
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

  get historicoBeneficiario(): VisitaDomiciliar[] {
    const beneficiario = this.visitForm.get('identificacao.beneficiario')?.value;
    if (!beneficiario) return [];
    return this.visitas.filter((visit) => visit.beneficiario === beneficiario);
  }

  changeTab(tab: StepTab['id']): void {
    this.activeTab = tab;
  }

  goToNextTab(): void {
    if (this.hasNextTab) this.changeTab(this.tabs[this.activeTabIndex + 1].id);
  }

  goToPreviousTab(): void {
    if (this.hasPreviousTab) this.changeTab(this.tabs[this.activeTabIndex - 1].id);
  }

  toggleSelection(path: (string | number)[], option: string): void {
    const control = this.visitForm.get(path);
    const current = new Set(control?.value ?? []);
    current.has(option) ? current.delete(option) : current.add(option);
    control?.setValue(Array.from(current));
  }

  selectionChecked(path: (string | number)[], option: string): boolean {
    const control = this.visitForm.get(path);
    return (control?.value as string[] | undefined)?.includes(option) ?? false;
  }

  addAnexo(): void {
    if (this.anexoForm.invalid) {
      this.anexoForm.markAllAsTouched();
      return;
    }

    const anexos = this.visitForm.get('anexos')?.value ?? [];
    const novo: VisitaAnexo = {
      id: crypto.randomUUID(),
      nome: this.anexoForm.value.nome,
      tipo: this.anexoForm.value.tipo,
      tamanho: this.anexoForm.value.tamanho
    };
    this.visitForm.get('anexos')?.setValue([novo, ...anexos]);
    this.anexoForm.reset({ tipo: 'PDF' });
  }

  removerAnexo(anexo: VisitaAnexo): void {
    const anexos = (this.visitForm.get('anexos')?.value as VisitaAnexo[]) ?? [];
    this.visitForm
      .get('anexos')
      ?.setValue(anexos.filter((item) => item.id !== anexo.id));
  }

  aplicarFiltros(): void {
    const filtros = this.filterForm.value;
    this.filteredVisitas = this.visitas.filter((visita) => {
      const dataVisita = new Date(visita.dataVisita);
      const dataInicial = filtros.dataInicial ? new Date(filtros.dataInicial) : null;
      const dataFinal = filtros.dataFinal ? new Date(filtros.dataFinal) : null;

      const matchDataInicial = dataInicial ? dataVisita >= dataInicial : true;
      const matchDataFinal = dataFinal ? dataVisita <= dataFinal : true;
      const matchUnidade = filtros.unidade ? visita.unidade === filtros.unidade : true;
      const matchSituacao = filtros.situacao ? visita.situacao === filtros.situacao : true;
      const matchBeneficiario = filtros.beneficiario
        ? visita.beneficiario.toLowerCase().includes(filtros.beneficiario.toLowerCase())
        : true;
      const matchResponsavel = filtros.responsavel
        ? visita.responsavel.toLowerCase().includes(filtros.responsavel.toLowerCase())
        : true;

      return (
        matchDataInicial &&
        matchDataFinal &&
        matchUnidade &&
        matchSituacao &&
        matchBeneficiario &&
        matchResponsavel
      );
    });

    this.filteredVisitas.sort((a, b) => (a.dataVisita < b.dataVisita ? 1 : -1));
  }

  submit(): void {
    if (this.visitForm.invalid) {
      this.visitForm.markAllAsTouched();
      this.feedback = 'Preencha os campos obrigatórios para salvar a visita.';
      return;
    }

    const situacao = this.visitForm.get('identificacao.situacao')?.value as SituacaoVisita;
    const relato = this.visitForm.get('registro.relato')?.value as string;

    if (situacao === 'Realizada' && !relato?.trim()) {
      this.feedback = 'Visitas realizadas exigem o relato técnico preenchido.';
      this.changeTab('registro');
      return;
    }

    this.feedback = null;
    this.saving = true;

    const payload = this.buildPayload();

    if (this.editingId) {
      this.visitaService.update(this.editingId, payload);
      this.feedback = 'Visita domiciliar atualizada com sucesso.';
    } else {
      this.visitaService.create(payload);
      this.feedback = 'Visita domiciliar registrada com sucesso.';
    }

    this.loadVisitas();
    this.saving = false;
    this.editingId = null;
    this.resetForm();
  }

  editarVisita(visita: VisitaDomiciliar): void {
    this.editingId = visita.id;
    this.changeTab('identificacao');
    this.visitForm.patchValue({
      identificacao: {
        unidade: visita.unidade,
        beneficiario: visita.beneficiario,
        responsavel: visita.responsavel,
        dataVisita: visita.dataVisita,
        horarioInicial: visita.horarioInicial,
        horarioFinal: visita.horarioFinal,
        tipoVisita: visita.tipoVisita,
        situacao: visita.situacao,
        usarEnderecoBeneficiario: visita.usarEnderecoBeneficiario,
        endereco: visita.endereco,
        observacoesIniciais: visita.observacoesIniciais
      },
      condicoes: visita.condicoes,
      situacaoSocial: visita.situacaoSocial,
      registro: visita.registro,
      anexos: visita.anexos
    });
  }

  cancelarVisita(visita: VisitaDomiciliar): void {
    if (visita.situacao === 'Realizada') {
      this.feedback = 'Visitas realizadas não podem ser canceladas ou excluídas.';
      return;
    }

    const atualizado = { ...visita, situacao: 'Cancelada' as SituacaoVisita };
    this.visitaService.update(visita.id, atualizado);
    this.feedback = 'Visita cancelada e registrada com sucesso.';
    this.loadVisitas();
  }

  excluirVisita(visita: VisitaDomiciliar): void {
    if (visita.situacao === 'Realizada') {
      this.feedback = 'Visitas realizadas não podem ser excluídas. Utilize a ação de cancelamento se necessário.';
      return;
    }

    this.visitaService.delete(visita.id);
    this.feedback = 'Visita removida do histórico.';
    if (this.editingId === visita.id) {
      this.editingId = null;
      this.resetForm();
    }
    this.loadVisitas();
  }

  novaVisita(): void {
    this.editingId = null;
    this.resetForm();
  }

  private loadVisitas(): void {
    this.visitas = this.visitaService.list();
    this.aplicarFiltros();
  }

  private resetForm(): void {
    this.visitForm.reset({
      identificacao: {
        unidade: '',
        beneficiario: '',
        responsavel: '',
        dataVisita: '',
        horarioInicial: '',
        horarioFinal: '',
        tipoVisita: 'Social',
        situacao: 'Agendada',
        usarEnderecoBeneficiario: true,
        endereco: {
          logradouro: '',
          numero: '',
          bairro: '',
          cidade: '',
          uf: '',
          cep: ''
        },
        observacoesIniciais: ''
      },
      condicoes: {
        tipoMoradia: '',
        situacaoPosse: '',
        comodos: null,
        saneamento: '',
        abastecimentoAgua: '',
        energiaEletrica: '',
        condicoesHigiene: '',
        situacaoRisco: [],
        observacoes: ''
      },
      situacaoSocial: {
        rendaFamiliar: '',
        faixaRenda: '',
        beneficios: [],
        redeApoio: '',
        vinculos: '',
        observacoes: ''
      },
      registro: {
        relato: '',
        necessidades: '',
        encaminhamentos: '',
        orientacoes: '',
        plano: ''
      },
      anexos: []
    });
    this.anexoForm.reset({ tipo: 'PDF' });
    this.activeTab = 'identificacao';
  }

  private buildPayload(): VisitaDomiciliar {
    const value = this.visitForm.value;
    return {
      id: this.editingId ?? crypto.randomUUID(),
      unidade: value.identificacao?.unidade || '',
      beneficiario: value.identificacao?.beneficiario || '',
      responsavel: value.identificacao?.responsavel || '',
      dataVisita: value.identificacao?.dataVisita || '',
      horarioInicial: value.identificacao?.horarioInicial || '',
      horarioFinal: value.identificacao?.horarioFinal || '',
      tipoVisita: value.identificacao?.tipoVisita || 'Social',
      situacao: (value.identificacao?.situacao as SituacaoVisita) || 'Agendada',
      usarEnderecoBeneficiario: value.identificacao?.usarEnderecoBeneficiario ?? true,
      endereco: value.identificacao?.endereco || {},
      observacoesIniciais: value.identificacao?.observacoesIniciais,
      condicoes: value.condicoes || {},
      situacaoSocial: value.situacaoSocial || {},
      registro: value.registro || {},
      anexos: value.anexos ?? [],
      createdAt: this.editingId
        ? this.visitas.find((v) => v.id === this.editingId)?.createdAt ?? new Date().toISOString()
        : new Date().toISOString(),
      createdBy: 'admin'
    };
  }
}
