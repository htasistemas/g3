import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { BeneficiarioPayload, BeneficiarioService } from '../../services/beneficiario.service';
import { FamilyService, FamiliaMembroPayload, FamiliaPayload } from '../../services/family.service';

@Component({
  selector: 'app-family-composition',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, FormsModule],
  templateUrl: './family-composition.component.html',
  styleUrl: './family-composition.component.scss'
})
export class FamilyCompositionComponent implements OnInit, OnDestroy {
  activeTab: 'family' | 'address' | 'members' | 'income' | 'vulnerability' = 'family';
  familyForm = this.buildForm();
  beneficiaries: BeneficiarioPayload[] = [];
  filteredBeneficiaries: BeneficiarioPayload[] = [];
  memberSearchTerm = '';
  isSaving = false;
  feedback: { type: 'success' | 'error'; message: string } | null = null;

  private destroy$ = new Subject<void>();

  constructor(
    private readonly fb: FormBuilder,
    private readonly beneficiaryService: BeneficiarioService,
    private readonly familyService: FamilyService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.loadBeneficiaries();
    this.members.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(() => this.updateIncomeTotals());
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  setActiveTab(tab: FamilyCompositionComponent['activeTab']): void {
    this.activeTab = tab;
  }

  get members(): FormArray<FormGroup> {
    return this.familyForm.get('members') as FormArray<FormGroup>;
  }

  get incomeGroup(): FormGroup {
    return this.familyForm.get('income') as FormGroup;
  }

  get vulnerabilityGroup(): FormGroup {
    return this.familyForm.get('vulnerability') as FormGroup;
  }

  get addressGroup(): FormGroup {
    return this.familyForm.get('address') as FormGroup;
  }

  get responsibleMember(): FormGroup | undefined {
    return this.members.controls.find((member) => member.get('responsavel')?.value);
  }

  get totalMembers(): number {
    return this.members.length;
  }

  get childrenCount(): number {
    return this.members.controls.filter((member) => this.ageFromMember(member) <= 12).length;
  }

  get teensCount(): number {
    return this.members.controls.filter((member) => {
      const age = this.ageFromMember(member);
      return age >= 13 && age <= 17;
    }).length;
  }

  get seniorsCount(): number {
    return this.members.controls.filter((member) => this.ageFromMember(member) >= 60).length;
  }

  get disabledCount(): number {
    return this.members.controls.filter((member) => member.get('possuiDeficiencia')?.value).length;
  }

  get rendaPerCapita(): number {
    const rendaTotal = Number(this.incomeGroup.get('rendaTotal')?.value) || 0;
    return this.totalMembers > 0 ? rendaTotal / this.totalMembers : 0;
  }

  get rendaTotalFromMembers(): number {
    return this.members.controls.reduce((acc, member) => {
      const contributes = member.get('contribuiRenda')?.value;
      const renda = Number(member.get('rendaIndividual')?.value) || 0;
      return contributes ? acc + renda : acc;
    }, 0);
  }

  loadBeneficiaries(): void {
    this.beneficiaryService.list().subscribe({
      next: ({ beneficiarios }) => {
        this.beneficiaries = beneficiarios ?? [];
        this.filteredBeneficiaries = [...this.beneficiaries];
      },
      error: () => {
        this.feedback = {
          type: 'error',
          message: 'Não foi possível carregar a base de beneficiários para vincular à família.'
        };
      }
    });
  }

  filterBeneficiaries(): void {
    const term = this.memberSearchTerm.trim().toLowerCase();
    if (!term) {
      this.filteredBeneficiaries = [...this.beneficiaries];
      return;
    }

    this.filteredBeneficiaries = this.beneficiaries.filter((beneficiary) => {
      const name = beneficiary.nomeCompleto?.toLowerCase() ?? '';
      const cpf = beneficiary.cpf?.toLowerCase() ?? beneficiary.documentos?.toLowerCase() ?? '';
      return name.includes(term) || cpf.includes(term);
    });
  }

  addMember(beneficiary: BeneficiarioPayload): void {
    if (!beneficiary.id) {
      return;
    }

    const memberGroup = this.createMemberGroup(beneficiary);
    this.members.push(memberGroup);
    this.memberSearchTerm = '';
    this.filterBeneficiaries();
    this.updateIncomeTotals();
  }

  createMemberGroup(beneficiary?: BeneficiarioPayload): FormGroup {
    return this.fb.group({
      beneficiaryId: [beneficiary?.id ?? null, Validators.required],
      nome: [beneficiary?.nomeCompleto ?? '', Validators.required],
      cpf: [beneficiary?.cpf ?? beneficiary?.documentos ?? ''],
      dataNascimento: [beneficiary?.dataNascimento ?? ''],
      parentesco: [''],
      condicaoTrabalho: [''],
      rendaIndividual: [0],
      escolaridade: [''],
      possuiDeficiencia: [false],
      responsavel: [false],
      contribuiRenda: [false],
      participaServicos: [false],
      observacoes: ['']
    });
  }

  removeMember(index: number): void {
    this.members.removeAt(index);
    this.updateIncomeTotals();
  }

  markAsResponsible(index: number): void {
    this.members.controls.forEach((member, idx) => {
      member.get('responsavel')?.setValue(idx === index);
    });

    const member = this.members.at(index);
    const beneficiaryId = member.get('beneficiaryId')?.value;
    if (beneficiaryId) {
      this.familyForm.get('responsavelFamiliarId')?.setValue(beneficiaryId);
      this.familyForm.get('referenceBeneficiaryId')?.setValue(beneficiaryId);
    }
  }

  updateIncomeTotals(): void {
    const rendaTotal = this.rendaTotalFromMembers;
    this.incomeGroup.get('rendaTotal')?.setValue(rendaTotal, { emitEvent: false });
    this.incomeGroup.get('rendaPerCapita')?.setValue(this.rendaPerCapita.toFixed(2), { emitEvent: false });
  }

  selectReferenceBeneficiary(beneficiaryId: number | null): void {
    if (!beneficiaryId) {
      return;
    }

    const existing = this.members.controls.find(
      (member) => member.get('beneficiaryId')?.value === beneficiaryId
    );

    if (existing) {
      this.markAsResponsible(this.members.controls.indexOf(existing));
      return;
    }

    const beneficiary = this.beneficiaries.find((item) => item.id === beneficiaryId);
    if (beneficiary) {
      this.addMember(beneficiary);
      this.markAsResponsible(this.members.length - 1);
    }
  }

  addEmptyMember(): void {
    this.members.push(this.createMemberGroup());
  }

  startNewFamily(): void {
    this.familyForm.reset();
    this.addressGroup.patchValue({ zona: 'urbana' });
    this.incomeGroup.patchValue({ rendaTotal: 0, rendaPerCapita: 0 });
    this.members.clear();
    this.activeTab = 'family';
    this.feedback = null;
  }

  submit(): void {
    if (this.familyForm.invalid || this.members.length === 0) {
      this.familyForm.markAllAsTouched();
      this.feedback = {
        type: 'error',
        message: 'Preencha os dados obrigatórios e inclua pelo menos um membro da família.'
      };
      return;
    }

    this.isSaving = true;
    const payload = this.buildPayload();

    const request = payload.id_familia
      ? this.familyService.update(payload.id_familia, payload)
      : this.familyService.create(payload);

    request.subscribe({
      next: () => {
        this.feedback = { type: 'success', message: 'Composição familiar salva com sucesso.' };
        this.isSaving = false;
      },
      error: () => {
        this.feedback = {
          type: 'error',
          message: 'Não foi possível salvar a família. Tente novamente mais tarde.'
        };
        this.isSaving = false;
      }
    });
  }

  openBeneficiaryForm(): void {
    this.router.navigate(['/beneficiarios/novo']);
  }

  private buildForm() {
    return this.fb.group({
      id: [null],
      referenceName: ['', Validators.required],
      referenceBeneficiaryId: [null],
      responsavelFamiliarId: [null],
      arrangementType: [''],
      arrangementOther: [''],
      members: this.fb.array<FormGroup>([]),
      address: this.fb.group({
        cep: [''],
        logradouro: [''],
        numero: [''],
        complemento: [''],
        bairro: [''],
        municipio: [''],
        uf: [''],
        pontoReferencia: [''],
        zona: ['urbana'],
        situacaoImovel: [''],
        tipoMoradia: [''],
        saneamento: this.fb.group({
          agua: [false],
          esgoto: [false],
          lixo: [false],
          energia: [false]
        })
      }),
      income: this.fb.group({
        rendaTotal: [0],
        rendaPerCapita: [{ value: 0, disabled: true }],
        faixaRenda: [''],
        fontes: this.fb.group({
          trabalhoFormal: [false],
          trabalhoInformal: [false],
          previdenciario: [false],
          assistencial: [false],
          doacoes: [false],
          outros: [false]
        }),
        despesas: this.fb.group({
          aluguel: [''],
          alimentacao: [''],
          aguaLuz: [''],
          transporte: [''],
          medicamentos: [''],
          outros: ['']
        }),
        insegurancaAlimentar: [''],
        possuiDividas: [false],
        detalhesDividas: ['']
      }),
      vulnerability: this.fb.group({
        violenciaDomestica: [false],
        trabalhoInfantil: [false],
        situacaoRua: [false],
        desempregoLongo: [false],
        moradiaPrecaria: [false],
        dependenciaQuimica: [false],
        outras: [''],
        programas: [''],
        historico: [''],
        tecnicoResponsavel: [''],
        periodicidade: [''],
        proximaVisita: ['']
      })
    });
  }

  ageFromMember(member: FormGroup): number {
    const birth = member.get('dataNascimento')?.value;
    if (!birth) {
      return 0;
    }

    const birthDate = new Date(birth);
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }

    return age;
  }

  private buildPayload(): FamiliaPayload {
    const formValue = this.familyForm.getRawValue();
    const address = formValue.address;
    const income = formValue.income;
    const vulnerability = formValue.vulnerability as {
      violenciaDomestica: boolean | null;
      trabalhoInfantil: boolean | null;
      situacaoRua: boolean | null;
      desempregoLongo: boolean | null;
      moradiaPrecaria: boolean | null;
      dependenciaQuimica: boolean | null;
      outras: string | null;
      programas: string | null;
      historico: string | null;
      tecnicoResponsavel: string | null;
      periodicidade: string | null;
      proximaVisita: string | null;
      observacoes: string | null;
    };

    const membros: FamiliaMembroPayload[] = this.members.controls.map((member) => ({
      id_beneficiario: member.get('beneficiaryId')?.value,
      parentesco: member.get('parentesco')?.value,
      responsavel_familiar: member.get('responsavel')?.value,
      contribui_renda: member.get('contribuiRenda')?.value,
      renda_individual: Number(member.get('rendaIndividual')?.value) || 0,
      participa_servicos: member.get('participaServicos')?.value,
      observacoes: member.get('observacoes')?.value
    }));

    const fontes = income?.fontes as Record<string, boolean>;
    const fontesSelecionadas = Object.entries(fontes || {})
      .filter(([, selecionada]) => selecionada)
      .map(([fonte]) => fonte)
      .join(', ');

    const vulnerabilidadesMarcadas = [
      vulnerability?.violenciaDomestica ? 'Violência doméstica' : null,
      vulnerability?.trabalhoInfantil ? 'Trabalho infantil' : null,
      vulnerability?.situacaoRua ? 'Situação de rua' : null,
      vulnerability?.desempregoLongo ? 'Desemprego prolongado' : null,
      vulnerability?.moradiaPrecaria ? 'Moradia precária' : null,
      vulnerability?.dependenciaQuimica ? 'Dependência química' : null,
      vulnerability?.outras ? `Outras: ${vulnerability.outras}` : null
    ].filter((item): item is string => Boolean(item));

    const payload: FamiliaPayload = {
      id_familia: formValue.id ?? undefined,
      nome_familia: formValue.referenceName || '',
      id_referencia_familiar: formValue.responsavelFamiliarId ?? formValue.referenceBeneficiaryId ?? undefined,
      arranjo_familiar:
        (formValue.arrangementType === 'outra'
          ? formValue.arrangementOther || 'outro'
          : formValue.arrangementType) || undefined,
      cep: address?.cep ?? '',
      logradouro: address?.logradouro ?? '',
      numero: address?.numero ?? '',
      complemento: address?.complemento || undefined,
      bairro: address?.bairro ?? '',
      ponto_referencia: address?.pontoReferencia || undefined,
      municipio: address?.municipio ?? '',
      uf: address?.uf ?? '',
      zona: address?.zona ?? '',
      situacao_imovel: address?.situacaoImovel || undefined,
      tipo_moradia: address?.tipoMoradia || undefined,
      agua_encanada: address?.saneamento?.agua ?? false,
      esgoto_tipo: address?.saneamento?.esgoto ? 'com_rede' : 'sem_rede',
      coleta_lixo: address?.saneamento?.lixo ? 'coletado' : 'nao_coletado',
      energia_eletrica: address?.saneamento?.energia ?? false,
      renda_familiar_total: Number(income?.rendaTotal) || 0,
      renda_per_capita: Number(income?.rendaPerCapita) || 0,
      faixa_renda_per_capita: income?.faixaRenda || undefined,
      principais_fontes_renda: fontesSelecionadas || undefined,
      situacao_inseguranca_alimentar: income?.insegurancaAlimentar || undefined,
      possui_dividas_relevantes: income?.possuiDividas ?? false,
      descricao_dividas: income?.detalhesDividas || undefined,
      vulnerabilidades_familia: vulnerabilidadesMarcadas.join('; ') || undefined,
      servicos_acompanhamento: vulnerability?.programas || undefined,
      tecnico_responsavel: vulnerability?.tecnicoResponsavel || undefined,
      periodicidade_atendimento: vulnerability?.periodicidade || undefined,
      proxima_visita_prevista: vulnerability?.proximaVisita || undefined,
      observacoes: vulnerability?.observacoes || undefined,
      membros
    };

    return payload;
  }
}
