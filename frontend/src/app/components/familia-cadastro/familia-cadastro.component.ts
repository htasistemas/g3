import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FamilyService, FamiliaMembroPayload, FamiliaPayload } from '../../services/family.service';
import { BeneficiarioApiService, BeneficiarioApiPayload } from '../../services/beneficiario-api.service';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-familia-cadastro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './familia-cadastro.component.html',
  styleUrl: './familia-cadastro.component.scss'
})
export class FamiliaCadastroComponent implements OnInit {
  form: FormGroup;
  activeTab = 'familia';
  familiaId: string | null = null;
  feedback: string | null = null;
  saving = false;
  membrosFiltrados: BeneficiarioApiPayload[] = [];
  referenciaResultados: BeneficiarioApiPayload[] = [];
  cepLookupError: string | null = null;

  tabs = [
    { id: 'familia', label: 'Dados da Família' },
    { id: 'endereco', label: 'Endereço' },
    { id: 'membros', label: 'Composição Familiar' },
    { id: 'renda', label: 'Renda & Condições' },
    { id: 'vulnerabilidades', label: 'Vulnerabilidades / Acompanhamento' }
  ];

  constructor(
    private readonly fb: FormBuilder,
    private readonly service: FamilyService,
    private readonly beneficiarioService: BeneficiarioApiService,
    private readonly http: HttpClient,
    private readonly route: ActivatedRoute,
    private readonly router: Router
  ) {
    this.form = this.fb.group({
      familia: this.fb.group({
        nome_familia: ['', Validators.required],
        referencia_familiar_nome: [''],
        id_referencia_familiar: ['', Validators.required],
        arranjo_familiar: ['']
      }),
      endereco: this.fb.group({
        cep: ['', [Validators.required, this.cepValidator]],
        logradouro: [''],
        numero: [''],
        complemento: [''],
        bairro: [''],
        ponto_referencia: [''],
        municipio: [''],
        uf: [''],
        zona: ['URBANA'],
        situacao_imovel: [''],
        tipo_moradia: [''],
        agua_encanada: [false],
        esgoto_tipo: [''],
        coleta_lixo: [''],
        energia_eletrica: [false],
        internet: [false]
      }),
      membros: this.fb.array([]),
      renda: this.fb.group({
        renda_familiar_total: [{ value: '', disabled: true }],
        renda_per_capita: [{ value: '', disabled: true }],
        situacao_inseguranca_alimentar: [''],
        possui_dividas_relevantes: [false],
        descricao_dividas: [''],
        principais_fontes_renda: ['']
      }),
      vulnerabilidades: this.fb.group({
        vulnerabilidades_familia: [''],
        servicos_acompanhamento: [''],
        tecnico_responsavel: [''],
        periodicidade_atendimento: [''],
        proxima_visita_prevista: [''],
        observacoes: ['']
      })
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

  getTabLabel(id: string): string {
    return this.tabs.find((tab) => tab.id === id)?.label ?? '';
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.familiaId = id;
        this.service.getById(id).subscribe(({ familia }) => {
          this.populateForm(familia);
        });
      }
    });
  }

  get membros(): FormArray {
    return this.form.get('membros') as FormArray;
  }

  addMembro(membro?: FamiliaMembroPayload) {
    const group = this.fb.group({
      id_familia_membro: [membro?.id_familia_membro],
      id_beneficiario: [membro?.id_beneficiario || '', Validators.required],
      parentesco: [membro?.parentesco || '', Validators.required],
      responsavel_familiar: [membro?.responsavel_familiar || false],
      contribui_renda: [membro?.contribui_renda || false],
      renda_individual: [membro?.renda_individual || ''],
      participa_servicos: [membro?.participa_servicos || false],
      observacoes: [membro?.observacoes || ''],
      beneficiario: [membro?.beneficiario],
      beneficiario_nome: [
        membro?.beneficiario?.nome_completo ||
          membro?.beneficiario?.nome_social ||
          (membro as any)?.beneficiario_nome ||
          ''
      ]
    });
    this.membros.push(group);
  }

  removeMembro(index: number) {
    this.membros.removeAt(index);
  }

  changeTab(tab: string) {
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

  populateForm(familia: FamiliaPayload) {
    this.form.patchValue({
      familia: {
        nome_familia: familia.nome_familia,
        referencia_familiar_nome: (familia as any)?.referencia_familiar_nome,
        id_referencia_familiar: familia.id_referencia_familiar,
        arranjo_familiar: familia.arranjo_familiar
      },
      endereco: {
        cep: familia.cep,
        logradouro: familia.logradouro,
        numero: familia.numero,
        complemento: familia.complemento,
        bairro: familia.bairro,
        ponto_referencia: familia.ponto_referencia,
        municipio: familia.municipio,
        uf: familia.uf,
        zona: familia.zona,
        situacao_imovel: familia.situacao_imovel,
        tipo_moradia: familia.tipo_moradia,
        agua_encanada: familia.agua_encanada,
        esgoto_tipo: familia.esgoto_tipo,
        coleta_lixo: familia.coleta_lixo,
        energia_eletrica: familia.energia_eletrica,
        internet: familia.internet
      },
      renda: {
        renda_familiar_total: familia.renda_familiar_total,
        renda_per_capita: familia.renda_per_capita,
        situacao_inseguranca_alimentar: familia.situacao_inseguranca_alimentar,
        possui_dividas_relevantes: familia.possui_dividas_relevantes,
        descricao_dividas: familia.descricao_dividas,
        principais_fontes_renda: familia.principais_fontes_renda
      },
      vulnerabilidades: {
        vulnerabilidades_familia: familia.vulnerabilidades_familia,
        servicos_acompanhamento: familia.servicos_acompanhamento,
        tecnico_responsavel: familia.tecnico_responsavel,
        periodicidade_atendimento: familia.periodicidade_atendimento,
        proxima_visita_prevista: familia.proxima_visita_prevista,
        observacoes: familia.observacoes
      }
    });

    this.membros.clear();
    (familia.membros || []).forEach((m) => this.addMembro(m));

    if (familia.id_referencia_familiar && !this.form.get(['familia', 'referencia_familiar_nome'])?.value) {
      this.beneficiarioService.getById(familia.id_referencia_familiar).subscribe(({ beneficiario }) => {
        this.form
          .get(['familia', 'referencia_familiar_nome'])
          ?.setValue(beneficiario.nome_completo || beneficiario.nome_social || '');
      });
    }
  }

  buscarBeneficiarios(term: string, index?: number) {
    if (!term?.trim()) {
      this.membrosFiltrados = [];
      return;
    }

    this.beneficiarioService.list({ nome: term }).subscribe(({ beneficiarios }) => {
      this.membrosFiltrados = beneficiarios.map((beneficiario) => ({
        ...beneficiario,
        _membroIndex: index
      })) as any;
    });
  }

  selecionarReferencia(beneficiario: BeneficiarioApiPayload) {
    this.form.get(['familia', 'id_referencia_familiar'])?.setValue(beneficiario.id_beneficiario || '');
    this.form
      .get(['familia', 'referencia_familiar_nome'])
      ?.setValue(beneficiario.nome_completo || beneficiario.nome_social || '');
    this.referenciaResultados = [];
  }

  selecionarMembro(index: number, beneficiario: BeneficiarioApiPayload) {
    const grupo = this.membros.at(index) as FormGroup;
    grupo.patchValue({
      id_beneficiario: beneficiario.id_beneficiario || '',
      beneficiario,
      beneficiario_nome: beneficiario.nome_completo || beneficiario.nome_social || ''
    });
    this.membrosFiltrados = [];
  }

  submit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.feedback = 'Preencha os campos obrigatórios';
      return;
    }
    this.saving = true;
    const payload = this.toPayload();
    const request = this.familiaId
      ? this.service.update(this.familiaId, payload)
      : this.service.create(payload);

    request.subscribe({
      next: () => {
        this.feedback = 'Família salva com sucesso';
        this.saving = false;
        this.router.navigate(['/cadastros/familias']);
      },
      error: (error) => {
        this.feedback = error?.error?.message || 'Erro ao salvar família';
        this.saving = false;
      }
    });
  }

  private toPayload(): FamiliaPayload {
    const value = this.form.value;
    const { referencia_familiar_nome, ...familiaDados } = value.familia as any;

    return {
      ...(familiaDados as any),
      ...(value.endereco as any),
      ...(value.renda as any),
      ...(value.vulnerabilidades as any),
      membros: (value.membros as any[]).map((m) => ({
        id_familia_membro: m.id_familia_membro,
        id_beneficiario: m.id_beneficiario,
        parentesco: m.parentesco,
        responsavel_familiar: m.responsavel_familiar,
        contribui_renda: m.contribui_renda,
        renda_individual: m.renda_individual,
        participa_servicos: m.participa_servicos,
        observacoes: m.observacoes
      }))
    } as FamiliaPayload;
  }

  onReferenciaInput(term: string) {
    this.form.get(['familia', 'referencia_familiar_nome'])?.setValue(term);
    this.form.get(['familia', 'id_referencia_familiar'])?.setValue('');

    if (term.trim().length < 2) {
      this.referenciaResultados = [];
      return;
    }

    this.beneficiarioService.list({ nome: term }).subscribe(({ beneficiarios }) => {
      this.referenciaResultados = beneficiarios;
    });
  }

  onCepInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 8);
    let formatted = digits;

    if (digits.length > 5) {
      formatted = `${digits.slice(0, 5)}-${digits.slice(5)}`;
    }

    this.form.get(['endereco', 'cep'])?.setValue(formatted, { emitEvent: false });
    this.cepLookupError = null;

    if (digits.length === 8) {
      this.lookupAddressByCep(digits);
    }
  }

  onCepBlur(): void {
    const cepControl = this.form.get(['endereco', 'cep']);
    if (!cepControl) return;

    if (cepControl.invalid) {
      this.cepLookupError = cepControl.value ? 'Informe um CEP válido para consultar o endereço.' : null;
      return;
    }

    const digits = this.normalizeCep(cepControl.value as string);
    if (digits?.length === 8) {
      this.lookupAddressByCep(digits);
    }
  }

  cepValidator(control: AbstractControl) {
    const digits = this.normalizeCep(control.value);
    return digits && digits.length === 8 ? null : { cepInvalido: true };
  }

  private lookupAddressByCep(cep: string): void {
    this.cepLookupError = null;
    this.http
      .get<{ logradouro?: string; bairro?: string; localidade?: string; uf?: string; erro?: boolean }>(
        `https://viacep.com.br/ws/${cep}/json/`
      )
      .pipe(finalize(() => this.form.get(['endereco', 'cep'])?.markAsTouched()))
      .subscribe({
        next: (response) => {
          if (response?.erro) {
            this.cepLookupError = 'CEP não encontrado.';
            return;
          }

          this.form.get('endereco')?.patchValue({
            logradouro: response.logradouro ?? '',
            bairro: response.bairro ?? '',
            municipio: response.localidade ?? '',
            uf: response.uf ?? ''
          });
        },
        error: () => {
          this.cepLookupError = 'Não foi possível consultar o CEP.';
        }
      });
  }

  private normalizeCep(value?: string | null): string {
    return (value ?? '').replace(/\D/g, '');
  }

  getNomeBeneficiario(index: number): string {
    const grupo = this.membros.at(index) as FormGroup;
    const nome =
      grupo.get('beneficiario_nome')?.value ||
      (grupo.get('beneficiario')?.value as BeneficiarioApiPayload)?.nome_completo ||
      (grupo.get('beneficiario')?.value as BeneficiarioApiPayload)?.nome_social;
    return nome || 'Beneficiário não selecionado';
  }
}
