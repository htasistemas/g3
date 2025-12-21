import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormControl, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { BeneficiarioApiPayload, BeneficiarioApiService } from '../../services/beneficiario-api.service';
import { FamiliaMembroPayload, FamiliaPayload, FamilyService } from '../../services/family.service';
import { Subject, of } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, finalize, map, switchMap, takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-vinculo-familiar',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './vinculo-familiar.component.html',
  styleUrl: './vinculo-familiar.component.scss'
})
export class VinculoFamiliarComponent implements OnInit, OnDestroy {
  familyForm: FormGroup;
  feedback: string | null = null;
  saving = false;
  cepLookupError: string | null = null;
  principalSearch = new FormControl('');
  memberSearch = new FormControl('');
  principalResults: BeneficiarioApiPayload[] = [];
  memberResults: BeneficiarioApiPayload[] = [];
  principalLoading = false;
  memberLoading = false;
  principalError: string | null = null;
  memberError: string | null = null;
  principal: BeneficiarioApiPayload | null = null;
  applyAddressToMembers = true;
  activeTab = 'cadastro';
  readonly tabs = [
    { id: 'cadastro', label: 'Cadastro da família' },
    { id: 'endereco', label: 'Endereço da família' },
    { id: 'membros', label: 'Membros vinculados' },
    { id: 'indicadores', label: 'Indicadores sociais' }
  ];
  readonly relationshipOptions = [
    'Responsável familiar',
    'Cônjuge/companheiro(a)',
    'Filho(a)',
    'Enteado(a)',
    'Pai/Mãe',
    'Avô/Avó',
    'Irmão(ã)',
    'Outro'
  ];
  readonly housingOptions = ['Próprio', 'Alugado', 'Cedido', 'Financiado', 'Ocupação', 'Outro'];
  readonly housingTypes = ['Casa', 'Apartamento', 'Cômodo', 'Barraco', 'Casa de madeira', 'Sítio/Chácara', 'Outro'];
  private readonly destroy$ = new Subject<void>();

  constructor(
    private readonly fb: FormBuilder,
    private readonly familyService: FamilyService,
    private readonly beneficiaryService: BeneficiarioApiService,
    private readonly http: HttpClient
  ) {
    this.familyForm = this.fb.group({
      nome_familia: ['', Validators.required],
      id_referencia_familiar: ['', Validators.required],
      cep: ['', [this.cepValidator]],
      logradouro: [''],
      numero: [''],
      complemento: [''],
      bairro: [''],
      ponto_referencia: [''],
      municipio: [''],
      uf: [''],
      zona: [''],
      situacao_imovel: [''],
      tipo_moradia: [''],
      agua_encanada: [false],
      esgoto_tipo: [''],
      coleta_lixo: [''],
      energia_eletrica: [false],
      internet: [false],
      arranjo_familiar: [''],
      qtd_membros: [null],
      qtd_criancas: [null],
      qtd_adolescentes: [null],
      qtd_idosos: [null],
      qtd_pessoas_deficiencia: [null],
      renda_familiar_total: [''],
      renda_per_capita: [''],
      faixa_renda_per_capita: [''],
      principais_fontes_renda: [''],
      situacao_inseguranca_alimentar: [''],
      possui_dividas_relevantes: [false],
      descricao_dividas: [''],
      vulnerabilidades_familia: [''],
      servicos_acompanhamento: [''],
      tecnico_responsavel: [''],
      periodicidade_atendimento: [''],
      proxima_visita_prevista: [''],
      observacoes: [''],
      membros: this.fb.array([])
    });
  }

  ngOnInit(): void {
    this.setupSearch(
      this.principalSearch,
      'principal'
    );
    this.setupSearch(
      this.memberSearch,
      'member'
    );

    this.membros.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(() => {
      if (!this.familyForm.get('qtd_membros')?.value) {
        this.familyForm.get('qtd_membros')?.setValue(this.membros.length, { emitEvent: false });
      }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  get membros(): FormArray {
    return this.familyForm.get('membros') as FormArray;
  }

  get activeTabIndex(): number {
    return Math.max(
      this.tabs.findIndex((tab) => tab.id === this.activeTab),
      0
    );
  }

  get hasPreviousTab(): boolean {
    return this.activeTabIndex > 0;
  }

  get hasNextTab(): boolean {
    return this.activeTabIndex < this.tabs.length - 1;
  }

  get nextTabLabel(): string {
    return this.tabs[this.activeTabIndex + 1]?.label ?? '';
  }

  get familyAddressLabel(): string {
    const value = this.familyForm.getRawValue();
    const parts = [
      value.logradouro,
      value.numero,
      value.bairro,
      value.municipio,
      value.uf,
      value.cep
    ].filter(Boolean);
    return parts.join(', ');
  }

  get principalLabel(): string {
    if (!this.principal) return 'Selecione o responsável principal';
    const doc = this.principal.cpf || this.principal.nis || this.principal.codigo || '';
    return `${this.principal.nome_completo || this.principal.nome_social}${doc ? ` • ${doc}` : ''}`;
  }

  selectPrincipal(beneficiary: BeneficiarioApiPayload): void {
    this.principal = beneficiary;
    this.familyForm.get('id_referencia_familiar')?.setValue(beneficiary.id_beneficiario ?? '');
    if (!this.familyForm.get('nome_familia')?.value) {
      this.familyForm.get('nome_familia')?.setValue(
        beneficiary.nome_familia || `Família ${beneficiary.nome_completo || beneficiary.nome_social || ''}`.trim()
      );
    }
    this.principalSearch.setValue(beneficiary.nome_completo || beneficiary.nome_social || '', { emitEvent: false });
    this.principalResults = [];
    this.upsertMember(beneficiary, true);
  }

  changeTab(tabId: string): void {
    this.activeTab = tabId;
  }

  goToNextTab(): void {
    if (!this.hasNextTab) return;
    this.activeTab = this.tabs[this.activeTabIndex + 1]?.id ?? this.activeTab;
  }

  goToPreviousTab(): void {
    if (!this.hasPreviousTab) return;
    this.activeTab = this.tabs[this.activeTabIndex - 1]?.id ?? this.activeTab;
  }

  addMember(beneficiary: BeneficiarioApiPayload): void {
    this.upsertMember(beneficiary, false);
    this.memberSearch.setValue('', { emitEvent: false });
    this.memberResults = [];
  }

  removeMember(index: number): void {
    const member = this.membros.at(index)?.value as FamiliaMembroPayload | undefined;
    if (member?.id_beneficiario && member.id_beneficiario === this.principal?.id_beneficiario) {
      this.feedback = 'O responsável principal não pode ser removido da família.';
      return;
    }
    this.membros.removeAt(index);
  }

  toggleAddressForAll(): void {
    this.applyAddressToMembers = !this.applyAddressToMembers;
    this.membros.controls.forEach((control) => {
      control.get('usa_endereco_familia')?.setValue(this.applyAddressToMembers);
    });
  }

  onFamilyCepInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 8);
    let formatted = digits;

    if (digits.length > 5) {
      formatted = `${digits.slice(0, 5)}-${digits.slice(5)}`;
    }

    this.familyForm.get('cep')?.setValue(formatted, { emitEvent: false });
    this.cepLookupError = null;

    if (digits.length === 8) {
      this.lookupAddressByCep(digits);
    }
  }

  onFamilyCepBlur(): void {
    const cepControl = this.familyForm.get('cep');
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

  saveFamily(): void {
    if (this.familyForm.invalid) {
      this.familyForm.markAllAsTouched();
      this.feedback = 'Revise os campos obrigatórios antes de salvar o vínculo familiar.';
      return;
    }

    if (!this.membros.length) {
      this.feedback = 'Inclua pelo menos um beneficiário vinculado à família.';
      return;
    }

    this.saving = true;
    this.feedback = null;

    const payload = this.buildPayload();

    this.familyService.create(payload).pipe(
      finalize(() => {
        this.saving = false;
      })
    ).subscribe({
      next: ({ familia }) => {
        this.feedback = `Família ${familia.nome_familia} registrada com sucesso.`;
      },
      error: () => {
        this.feedback = 'Não foi possível salvar o vínculo familiar. Tente novamente.';
      }
    });
  }

  private buildPayload(): FamiliaPayload {
    const rawValue = this.familyForm.getRawValue();
    const membros = this.membros.controls.map((control) => this.mapMemberPayload(control.value));

    return {
      ...(rawValue as FamiliaPayload),
      membros,
      qtd_membros: rawValue.qtd_membros || membros.length
    };
  }

  private mapMemberPayload(member: any): FamiliaMembroPayload {
    return {
      id_beneficiario: member.id_beneficiario,
      parentesco: member.parentesco,
      responsavel_familiar: member.responsavel_familiar,
      contribui_renda: member.contribui_renda,
      renda_individual: member.renda_individual,
      participa_servicos: member.participa_servicos,
      observacoes: member.observacoes,
      usa_endereco_familia: member.usa_endereco_familia
    };
  }

  private setupSearch(control: FormControl, context: 'principal' | 'member'): void {
    control.valueChanges
      .pipe(
        debounceTime(300),
        distinctUntilChanged(),
        switchMap((value) => this.searchBeneficiaries(value ?? '', context)),
        takeUntil(this.destroy$)
      )
      .subscribe((results) => {
        if (context === 'principal') {
          this.principalResults = results;
        } else {
          this.memberResults = results;
        }
      });
  }

  private searchBeneficiaries(term: string, context: 'principal' | 'member') {
    const query = term.trim();

    if (!query) {
      this.setSearchState(context, false, null);
      return of([] as BeneficiarioApiPayload[]);
    }

    this.setSearchState(context, true, null);

    const digits = query.replace(/\D/g, '');
    const params =
      digits.length === 11
        ? { cpf: digits, nis: digits }
        : digits.length >= 4 && /^\d+$/.test(digits)
          ? { codigo: digits }
          : { nome: query };

    return this.beneficiaryService.list(params).pipe(
      map(({ beneficiarios }) => beneficiarios ?? []),
      catchError(() => {
        this.setSearchState(context, false, 'Não foi possível buscar beneficiários agora.');
        return of([] as BeneficiarioApiPayload[]);
      }),
      finalize(() => {
        if (context === 'principal') {
          this.principalLoading = false;
        } else {
          this.memberLoading = false;
        }
      })
    );
  }

  private setSearchState(context: 'principal' | 'member', loading: boolean, error: string | null): void {
    if (context === 'principal') {
      this.principalLoading = loading;
      this.principalError = error;
    } else {
      this.memberLoading = loading;
      this.memberError = error;
    }
  }

  private upsertMember(beneficiary: BeneficiarioApiPayload, isReference: boolean): void {
    if (!beneficiary.id_beneficiario) return;

    const existingIndex = this.membros.controls.findIndex(
      (control) => control.get('id_beneficiario')?.value === beneficiary.id_beneficiario
    );

    if (existingIndex >= 0) {
      const control = this.membros.at(existingIndex);
      if (isReference) {
        control.get('responsavel_familiar')?.setValue(true);
        control.get('parentesco')?.setValue('Responsável familiar');
      }
      this.feedback = 'Este beneficiário já está vinculado à família.';
      return;
    }

    this.membros.push(this.buildMemberControl(beneficiary, isReference));
    if (isReference && !this.principal) {
      this.principal = beneficiary;
    }
  }

  private buildMemberControl(beneficiary: BeneficiarioApiPayload, isReference: boolean): FormGroup {
    return this.fb.group({
      id_beneficiario: [beneficiary.id_beneficiario, Validators.required],
      nome: [beneficiary.nome_completo || beneficiary.nome_social || 'Beneficiário'],
      documento: [beneficiary.cpf || beneficiary.nis || beneficiary.codigo || ''],
      parentesco: [isReference ? 'Responsável familiar' : '', Validators.required],
      responsavel_familiar: [isReference],
      contribui_renda: [false],
      renda_individual: [''],
      participa_servicos: [false],
      observacoes: [''],
      usa_endereco_familia: [this.applyAddressToMembers]
    });
  }

  private lookupAddressByCep(cep: string): void {
    this.cepLookupError = null;
    this.http
      .get<ViaCepResponse>(`https://viacep.com.br/ws/${cep}/json/`)
      .pipe(finalize(() => this.familyForm.get('cep')?.markAsTouched()))
      .subscribe({
        next: (response) => {
          if (response?.erro) {
            this.cepLookupError = 'CEP não encontrado.';
            return;
          }

          this.familyForm.patchValue({
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

  private normalizeCep(value?: string | null): string | undefined {
    const digits = (value ?? '').replace(/\D/g, '');
    return digits || undefined;
  }

  private cepValidator = (control: AbstractControl): ValidationErrors | null => {
    const value = (control.value as string | null | undefined)?.replace(/\D/g, '') ?? '';
    if (!value) return null;
    return value.length === 8 ? null : { cep: true };
  };
}

interface ViaCepResponse {
  logradouro?: string;
  bairro?: string;
  localidade?: string;
  uf?: string;
  erro?: boolean;
}
