import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import { BeneficiarioPayload, BeneficiarioService, DocumentoObrigatorio } from '../../services/beneficiario.service';

interface UploadedDocument {
  tipo: string;
  numero?: string;
  dataEmissao?: string;
  nomeArquivo?: string;
  file?: File;
}

@Component({
  selector: 'app-beneficiary-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './beneficiary-form.component.html',
  styleUrl: './beneficiary-form.component.scss'
})
export class BeneficiaryFormComponent implements OnInit, OnDestroy {
  beneficiaryForm: FormGroup;
  documentForm: FormGroup;
  requiredDocuments: DocumentoObrigatorio[] = [];
  uploadedDocuments: UploadedDocument[] = [];
  feedback: { type: 'success' | 'error'; message: string } | null = null;
  activeTab:
    | 'personal'
    | 'address'
    | 'contact'
    | 'documents'
    | 'family'
    | 'education'
    | 'health'
    | 'benefits'
    | 'notes' = 'personal';
  editingId: number | null = null;
  isSubmitting = false;
  photoFile: File | null = null;
  private destroy$ = new Subject<void>();

  readonly states = [
    'AC',
    'AL',
    'AP',
    'AM',
    'BA',
    'CE',
    'DF',
    'ES',
    'GO',
    'MA',
    'MT',
    'MS',
    'MG',
    'PA',
    'PB',
    'PR',
    'PE',
    'PI',
    'RJ',
    'RN',
    'RS',
    'RO',
    'RR',
    'SC',
    'SP',
    'SE',
    'TO'
  ];

  constructor(
    private readonly fb: FormBuilder,
    private readonly beneficiaryService: BeneficiarioService,
    private readonly router: Router,
    private readonly route: ActivatedRoute
  ) {
    this.beneficiaryForm = this.fb.group({
      // Dados pessoais
      nomeCompleto: ['', [Validators.required, Validators.minLength(3)]],
      nomeSocial: [''],
      apelido: [''],
      cpf: ['', [Validators.required, this.cpfValidator]],
      rg: [''],
      orgaoEmissor: [''],
      ufEmissor: [''],
      dataEmissaoRg: [''],
      dataNascimento: ['', Validators.required],
      idade: [{ value: null, disabled: true }],
      sexoBiologico: [''],
      identidadeGenero: [''],
      corRaca: [''],
      estadoCivil: [''],
      nacionalidade: [''],
      naturalidade: [''],
      nomeMae: ['', Validators.required],
      nomePai: [''],
      responsavelLegal: [false],
      responsavelPor: [''],
      situacaoEscolarCrianca: [''],
      responsavelLegalPor: [''],

      // Endereço
      cep: [''],
      logradouro: [''],
      numero: [''],
      complemento: [''],
      bairro: [''],
      cidade: [''],
      uf: [''],
      pontoReferencia: [''],
      zona: [''],
      situacaoImovel: [''],
      tipoMoradia: [''],
      condicoesMoradia: this.fb.control<string[]>([]),
      acessoAgua: [''],
      esgoto: [''],
      coletaLixo: [''],
      energiaEletrica: [''],
      internet: [''],

      // Contato
      telefoneFixo: [''],
      celular: ['', [Validators.required, this.phoneValidator]],
      contatoWhatsApp: [true],
      telefoneRecado: [''],
      nomeRecado: [''],
      email: ['', Validators.email],
      contatoPreferencias: this.fb.control<string[]>([]),
      horarioContato: [''],
      autorizacaoContato: [false],

      // Documentos e identificação social
      nis: [''],
      nisBeneficio: [''],
      certidaoTipo: [''],
      certidaoLivro: [''],
      certidaoFolha: [''],
      certidaoTermo: [''],
      certidaoCartorio: [''],
      certidaoMunicipio: [''],
      certidaoUf: [''],
      carteiraTrabalhoNumero: [''],
      carteiraTrabalhoSerie: [''],
      carteiraTrabalhoUf: [''],
      tituloEleitor: [''],
      cnhNumero: [''],
      cartaoSus: [''],

      // Situação familiar e social
      moraComFamilia: [''],
      responsavelFamiliar: [false],
      vinculoFamiliar: [''],
      situacoesVulnerabilidade: this.fb.control<string[]>([]),
      acompanhamentos: this.fb.control<string[]>([]),
      tempoAcompanhamento: [''],
      origemEncaminhamento: [''],

      // Escolaridade e trabalho
      alfabetizado: [''],
      serieConcluida: [''],
      situacaoEscolarAtual: [''],
      nivelEnsino: [''],
      situacaoTrabalho: [''],
      ocupacao: [''],
      localTrabalho: [''],
      rendaIndividual: [null],
      fonteRenda: [''],
      pessoasResidencia: [null],
      rendaFamiliar: [null],
      rendaPerCapita: [{ value: null, disabled: true }],

      // Saúde
      possuiDeficiencia: [''],
      tiposDeficiencia: this.fb.control<string[]>([]),
      cidPrincipal: [''],
      usaMedicacao: [''],
      medicacaoDescricao: [''],
      acompanhamentoSaude: this.fb.control<string[]>([]),

      // Programas sociais e benefícios
      recebeBeneficio: [''],
      beneficiosAtuais: this.fb.control<string[]>([]),
      valorBeneficios: [null],
      tempoBeneficios: [''],
      historicoBeneficio: [''],

      // Observações
      observacoes: ['']
    });

    this.documentForm = this.fb.group({
      tipo: ['', Validators.required],
      numero: [''],
      dataEmissao: [''],
      arquivo: [null]
    });
  }

  ngOnInit(): void {
    this.listenToIncomeChanges();
    this.listenToBirthDate();
    this.loadRequiredDocuments();
    this.listenToRouteParams();
  }

  get requiredMandatoryCount(): number {
    return this.requiredDocuments.filter((doc) => doc.required).length;
  }

  documentIsRequired(tipo: string): boolean {
    return this.requiredDocuments.some((doc) => doc.required && doc.nome === tipo);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  setActiveTab(tab: typeof this.activeTab): void {
    this.activeTab = tab;
  }

  formatTitleCase(controlName: string): void {
    const control = this.beneficiaryForm.get(controlName);
    const value = control?.value as string;

    if (!control || typeof value !== 'string') {
      return;
    }

    const formatted = value
      .toLowerCase()
      .replace(/\S+/g, (word) => word.charAt(0).toUpperCase() + word.slice(1));

    if (formatted !== value) {
      control.setValue(formatted, { emitEvent: false });
    }
  }

  onCpfInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 11);

    let formatted = digits;
    if (digits.length > 9) {
      formatted = digits.replace(/(\d{3})(\d{3})(\d{3})(\d{0,2}).*/, '$1.$2.$3-$4');
    } else if (digits.length > 6) {
      formatted = digits.replace(/(\d{3})(\d{3})(\d{0,3}).*/, '$1.$2.$3');
    } else if (digits.length > 3) {
      formatted = digits.replace(/(\d{3})(\d{0,3}).*/, '$1.$2');
    }

    this.beneficiaryForm.get('cpf')?.setValue(formatted, { emitEvent: false });
  }

  onPhoneInput(controlName: 'telefoneFixo' | 'celular' | 'telefoneRecado', event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 11);

    let formatted = digits;
    if (digits.length > 10) {
      formatted = digits.replace(/(\d{2})(\d{5})(\d{0,4}).*/, '($1) $2-$3');
    } else if (digits.length > 6) {
      formatted = digits.replace(/(\d{2})(\d{4})(\d{0,4}).*/, '($1) $2-$3');
    } else if (digits.length > 2) {
      formatted = digits.replace(/(\d{2})(\d{0,5}).*/, '($1) $2');
    }

    this.beneficiaryForm.get(controlName)?.setValue(formatted, { emitEvent: false });
  }

  handleCepInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 8);

    let formatted = digits;
    if (digits.length > 5) {
      formatted = `${digits.slice(0, 5)}-${digits.slice(5)}`;
    }

    this.beneficiaryForm.get('cep')?.setValue(formatted, { emitEvent: false });

    if (digits.length === 8) {
      this.fetchAddressByCep(digits);
    }
  }

  addDocument(): void {
    if (this.documentForm.invalid) {
      this.documentForm.markAllAsTouched();
      return;
    }

    const { tipo, numero, dataEmissao, arquivo } = this.documentForm.value;
    const newDocument: UploadedDocument = {
      tipo,
      numero,
      dataEmissao,
      nomeArquivo: (arquivo as File | null)?.name || undefined,
      file: (arquivo as File | null) || undefined
    };

    this.uploadedDocuments = [...this.uploadedDocuments, newDocument];
    this.documentForm.reset();
  }

  removeDocument(index: number): void {
    this.uploadedDocuments = this.uploadedDocuments.filter((_, idx) => idx !== index);
  }

  onDocumentFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0] ?? null;
    this.documentForm.get('arquivo')?.setValue(file);
  }

  onPhotoSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.photoFile = input.files?.[0] ?? null;
  }

  submit(): void {
    this.feedback = null;

    if (this.beneficiaryForm.invalid) {
      this.beneficiaryForm.markAllAsTouched();
      this.activeTab = this.findFirstInvalidTab();
      return;
    }

    const missingRequired = this.requiredDocuments
      .filter((doc) => doc.required)
      .filter((doc) => !this.uploadedDocuments.some((item) => item.tipo === doc.nome && item.nomeArquivo))
      .map((doc) => doc.nome);

    if (missingRequired.length) {
      this.activeTab = 'documents';
      this.feedback = {
        type: 'error',
        message: `Inclua os documentos obrigatórios: ${missingRequired.join(', ')}`
      };
      return;
    }

    const formValue = this.beneficiaryForm.getRawValue();
    const payload: BeneficiarioPayload = {
      ...formValue,
      id: this.editingId ?? undefined,
      documentos: formValue.cpf,
      documentosAnexos: this.uploadedDocuments.map((doc) => ({
        nome: doc.tipo,
        nomeArquivo: doc.nomeArquivo,
        file: doc.file,
        required: this.requiredDocuments.find((requiredDoc) => requiredDoc.nome === doc.tipo)?.required
      }))
    };

    this.isSubmitting = true;
    this.beneficiaryService.save(payload, this.photoFile).subscribe({
      next: () => {
        this.isSubmitting = false;
        this.feedback = { type: 'success', message: 'Beneficiário salvo com sucesso.' };
        this.router.navigate(['/beneficiarios']);
      },
      error: () => {
        this.isSubmitting = false;
        this.feedback = { type: 'error', message: 'Não foi possível salvar o beneficiário.' };
      }
    });
  }

  startNew(): void {
    this.editingId = null;
    this.feedback = null;
    this.uploadedDocuments = [];
    this.photoFile = null;
    this.beneficiaryForm.reset({
      contatoWhatsApp: true,
      responsavelLegal: false,
      responsavelFamiliar: false,
      autorizacaoContato: false,
      condicoesMoradia: [],
      contatoPreferencias: [],
      situacoesVulnerabilidade: [],
      acompanhamentos: [],
      tiposDeficiencia: [],
      acompanhamentoSaude: [],
      beneficiosAtuais: [],
      idade: null,
      rendaPerCapita: null,
      rendaFamiliar: null,
      pessoasResidencia: null
    });
    this.activeTab = 'personal';
  }

  cancel(): void {
    if (this.editingId) {
      this.loadBeneficiary(this.editingId);
    } else {
      this.startNew();
    }
  }

  goBack(): void {
    this.router.navigate(['/beneficiarios']);
  }

  private findFirstInvalidTab(): typeof this.activeTab {
    const tabControlMap: Record<typeof this.activeTab, string[]> = {
      personal: ['nomeCompleto', 'cpf', 'dataNascimento', 'nomeMae'],
      address: ['cep', 'logradouro', 'numero'],
      contact: ['celular'],
      documents: [],
      family: [],
      education: [],
      health: [],
      benefits: [],
      notes: []
    };

    const target = (Object.keys(tabControlMap) as Array<keyof typeof tabControlMap>).find((tab) =>
      tabControlMap[tab].some((controlName) => this.beneficiaryForm.get(controlName)?.invalid)
    );

    return target ?? this.activeTab;
  }

  private listenToIncomeChanges(): void {
    this.beneficiaryForm
      .get('rendaFamiliar')
      ?.valueChanges.pipe(takeUntil(this.destroy$))
      .subscribe(() => this.calculatePerCapita());

    this.beneficiaryForm
      .get('pessoasResidencia')
      ?.valueChanges.pipe(takeUntil(this.destroy$))
      .subscribe(() => this.calculatePerCapita());
  }

  private listenToBirthDate(): void {
    this.beneficiaryForm
      .get('dataNascimento')
      ?.valueChanges.pipe(takeUntil(this.destroy$))
      .subscribe((value) => this.calculateAge(value as string));
  }

  private calculatePerCapita(): void {
    const renda = Number(this.beneficiaryForm.get('rendaFamiliar')?.value ?? 0);
    const pessoas = Number(this.beneficiaryForm.get('pessoasResidencia')?.value ?? 0);

    if (!pessoas || Number.isNaN(renda)) {
      this.beneficiaryForm.get('rendaPerCapita')?.setValue(null, { emitEvent: false });
      return;
    }

    const perCapita = renda / pessoas;
    this.beneficiaryForm.get('rendaPerCapita')?.setValue(Number.isFinite(perCapita) ? perCapita : null, {
      emitEvent: false
    });
  }

  private loadRequiredDocuments(): void {
    this.beneficiaryService
      .getRequiredDocuments()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: ({ documents }) => {
          this.requiredDocuments = documents.map((doc) => ({
            nome: doc.nome,
            required: Boolean(doc.obrigatorio ?? doc.required)
          }));
        },
        error: () => {
          this.requiredDocuments = [];
        }
      });
  }

  private listenToRouteParams(): void {
    this.route.paramMap.pipe(takeUntil(this.destroy$)).subscribe((params) => {
      const id = Number(params.get('id'));
      if (Number.isFinite(id) && id > 0) {
        this.editingId = id;
        this.loadBeneficiary(id);
      }
    });
  }

  private loadBeneficiary(id: number): void {
    this.beneficiaryService
      .getById(id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (beneficiary) => this.applyBeneficiary(beneficiary),
        error: () => {
          this.feedback = { type: 'error', message: 'Não foi possível carregar o beneficiário.' };
        }
      });
  }

  private applyBeneficiary(beneficiary: BeneficiarioPayload): void {
    this.beneficiaryForm.patchValue({
      nomeCompleto: beneficiary.nomeCompleto || '',
      nomeSocial: beneficiary.nomeSocial || '',
      apelido: beneficiary.apelido || '',
      cpf: beneficiary.cpf || beneficiary.documentos || '',
      rg: beneficiary.rg || '',
      orgaoEmissor: beneficiary.orgaoEmissor || '',
      ufEmissor: beneficiary.ufEmissor || '',
      dataEmissaoRg: beneficiary.dataEmissaoRg || '',
      dataNascimento: beneficiary.dataNascimento || '',
      idade: beneficiary.idade || '',
      sexoBiologico: beneficiary.sexoBiologico || beneficiary.sexo || '',
      identidadeGenero: beneficiary.identidadeGenero || '',
      corRaca: beneficiary.corRaca || '',
      estadoCivil: beneficiary.estadoCivil || '',
      nacionalidade: beneficiary.nacionalidade || '',
      naturalidade: beneficiary.naturalidade || '',
      nomeMae: beneficiary.nomeMae || '',
      nomePai: beneficiary.nomePai || '',
      responsavelLegal: beneficiary.responsavelLegal || false,
      responsavelPor: beneficiary.responsavelPor || '',
      situacaoEscolarCrianca: beneficiary.situacaoEscolarCrianca || '',
      responsavelLegalPor: beneficiary.responsavelLegalPor || '',
      nis: beneficiary.nis || beneficiary.nisBeneficio || '',
      nisBeneficio: beneficiary.nisBeneficio || '',
      cep: beneficiary.cep || '',
      logradouro: beneficiary.logradouro || beneficiary.endereco || '',
      numero: beneficiary.numero || beneficiary.numeroEndereco || '',
      complemento: beneficiary.complemento || '',
      bairro: beneficiary.bairro || '',
      cidade: beneficiary.cidade || '',
      uf: beneficiary.uf || beneficiary.estado || '',
      pontoReferencia: beneficiary.pontoReferencia || '',
      zona: beneficiary.zona || '',
      situacaoImovel: beneficiary.situacaoImovel || beneficiary.situacaoMoradia || '',
      tipoMoradia: beneficiary.tipoMoradia || '',
      condicoesMoradia: beneficiary.condicoesMoradia || [],
      acessoAgua: beneficiary.acessoAgua || '',
      esgoto: beneficiary.esgoto || '',
      coletaLixo: beneficiary.coletaLixo || '',
      energiaEletrica: beneficiary.energiaEletrica || '',
      internet: beneficiary.internet || '',
      telefoneFixo: beneficiary.telefoneFixo || '',
      celular: beneficiary.celular || beneficiary.telefone || '',
      contatoWhatsApp: beneficiary.contatoWhatsApp ?? true,
      telefoneRecado: beneficiary.telefoneRecado || '',
      nomeRecado: beneficiary.nomeRecado || '',
      email: beneficiary.email || '',
      contatoPreferencias: beneficiary.contatoPreferencias || [],
      horarioContato: beneficiary.horarioContato || '',
      autorizacaoContato: beneficiary.autorizacaoContato || false,
      pessoasResidencia: beneficiary.pessoasResidencia || '',
      rendaFamiliar: beneficiary.rendaFamiliar || beneficiary.renda || '',
      rendaPerCapita: beneficiary.rendaPerCapita || '',
      situacaoTrabalho: beneficiary.situacaoTrabalho || '',
      escolaridade: beneficiary.escolaridade || '',
      ocupacao: beneficiary.ocupacao || '',
      localTrabalho: beneficiary.localTrabalho || '',
      rendaIndividual: beneficiary.rendaIndividual || '',
      fonteRenda: beneficiary.fonteRenda || '',
      alfabetizado: beneficiary.alfabetizado || '',
      serieConcluida: beneficiary.serieConcluida || '',
      situacaoEscolarAtual: beneficiary.situacaoEscolarAtual || '',
      nivelEnsino: beneficiary.nivelEnsino || '',
      moraComFamilia: beneficiary.moraComFamilia || '',
      responsavelFamiliar: beneficiary.responsavelFamiliar || false,
      vinculoFamiliar: beneficiary.vinculoFamiliar || '',
      situacoesVulnerabilidade: beneficiary.situacoesVulnerabilidade || [],
      acompanhamentos: beneficiary.acompanhamentos || [],
      tempoAcompanhamento: beneficiary.tempoAcompanhamento || '',
      origemEncaminhamento: beneficiary.origemEncaminhamento || '',
      possuiDeficiencia: beneficiary.possuiDeficiencia || '',
      tiposDeficiencia: beneficiary.tiposDeficiencia || [],
      cidPrincipal: beneficiary.cidPrincipal || '',
      usaMedicacao: beneficiary.usaMedicacao || '',
      medicacaoDescricao: beneficiary.medicacaoDescricao || '',
      acompanhamentoSaude: beneficiary.acompanhamentoSaude || [],
      recebeBeneficio: beneficiary.recebeBeneficio || '',
      beneficiosAtuais: beneficiary.beneficiosAtuais || [],
      valorBeneficios: beneficiary.valorBeneficios || '',
      tempoBeneficios: beneficiary.tempoBeneficios || '',
      historicoBeneficio: beneficiary.historicoBeneficio || beneficiary.programasSociais || '',
      observacoes: beneficiary.observacoes || ''
    });

    this.uploadedDocuments = (beneficiary.documentosAnexos || []).map((doc) => ({
      tipo: doc.nome,
      nomeArquivo: doc.nomeArquivo
    }));
    this.calculatePerCapita();
    this.calculateAge(beneficiary.dataNascimento || '');
  }

  toggleSelection(controlName: string, value: string): void {
    const control = this.beneficiaryForm.get(controlName);
    if (!control) {
      return;
    }

    const current = (control.value as string[]) || [];
    const exists = current.includes(value);
    const updated = exists ? current.filter((item) => item !== value) : [...current, value];
    control.setValue(updated);
  }

  selectionChecked(controlName: string, value: string): boolean {
    const control = this.beneficiaryForm.get(controlName);
    if (!control) {
      return false;
    }

    return ((control.value as string[]) || []).includes(value);
  }

  private calculateAge(dateString: string | null | undefined): void {
    if (!dateString) {
      this.beneficiaryForm.get('idade')?.setValue(null, { emitEvent: false });
      return;
    }

    const birthDate = new Date(dateString);
    if (Number.isNaN(birthDate.getTime())) {
      this.beneficiaryForm.get('idade')?.setValue(null, { emitEvent: false });
      return;
    }

    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
      age -= 1;
    }

    this.beneficiaryForm.get('idade')?.setValue(age, { emitEvent: false });
  }

  private async fetchAddressByCep(cep: string): Promise<void> {
    try {
      const response = await fetch(`https://viacep.com.br/ws/${cep}/json/`);
      const data = await response.json();

      if (!response.ok || data?.erro) {
        throw new Error('CEP não encontrado');
      }

      this.beneficiaryForm.patchValue({
        logradouro: data.logradouro || '',
        bairro: data.bairro || '',
        cidade: data.localidade || '',
        uf: data.uf || ''
      });
    } catch (error) {
      console.error('Erro ao buscar CEP', error);
    }
  }

  private cepValidator = (control: { value: string }): Record<string, boolean> | null => {
    const digits = control.value?.replace(/\D/g, '');
    if (!digits) {
      return { cep: true };
    }
    return digits.length === 8 ? null : { cep: true };
  };

  private cpfValidator = (control: { value: string }): Record<string, boolean> | null => {
    const value = control.value?.replace(/\D/g, '');
    if (!value || value.length !== 11 || /^([0-9])\1*$/.test(value)) {
      return { cpf: true };
    }

    const calculateDigit = (slice: number) => {
      const numbers = value.slice(0, slice).split('').map(Number);
      const factorStart = slice + 1;
      const sum = numbers.reduce((acc, num, index) => acc + num * (factorStart - index), 0);
      const remainder = (sum * 10) % 11;
      return remainder === 10 ? 0 : remainder;
    };

    const digit1 = calculateDigit(9);
    const digit2 = calculateDigit(10);

    return digit1 === Number(value[9]) && digit2 === Number(value[10]) ? null : { cpf: true };
  };

  private phoneValidator = (control: { value: string }): Record<string, boolean> | null => {
    const value = control.value?.replace(/\D/g, '');
    if (!value) {
      return { phone: true };
    }

    return value.length === 10 || value.length === 11 ? null : { phone: true };
  };
}
