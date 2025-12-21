import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
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
    private readonly beneficiaryService: BeneficiarioApiService
  ) {
    this.familyForm = this.fb.group({
      nome_familia: ['', Validators.required],
      id_referencia_familiar: ['', Validators.required],
      cep: [''],
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

    return this.beneficiaryService.list({ nome: query }).pipe(
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
}
