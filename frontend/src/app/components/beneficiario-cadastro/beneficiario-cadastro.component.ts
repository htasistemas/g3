import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {
  AbstractControl,
  FormArray,
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { BeneficiarioApiService, BeneficiarioApiPayload } from '../../services/beneficiario-api.service';
import { BeneficiaryService, DocumentoObrigatorio } from '../../services/beneficiary.service';
import {
  AssistanceUnitPayload,
  AssistanceUnitService
} from '../../services/assistance-unit.service';
import { Subject, firstValueFrom } from 'rxjs';
import { finalize, takeUntil } from 'rxjs/operators';
type ViaCepResponse = {
  logradouro?: string;
  complemento?: string;
  bairro?: string;
  localidade?: string;
  uf?: string;
  erro?: boolean;
};

@Component({
  selector: 'app-beneficiario-cadastro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './beneficiario-cadastro.component.html',
  styleUrl: './beneficiario-cadastro.component.scss'
})
export class BeneficiarioCadastroComponent implements OnInit, OnDestroy {
  form: FormGroup;
  searchForm: FormGroup;
  activeTab = 'dados';
  saving = false;
  feedback: string | null = null;
  beneficiarioId: string | null = null;
  documentosObrigatorios: DocumentoObrigatorio[] = [];
  beneficiaryAge: number | null = null;
  beneficiarios: BeneficiarioApiPayload[] = [];
  filteredBeneficiarios: BeneficiarioApiPayload[] = [];
  createdAt: string | null = null;
  lastUpdatedAt: string | null = null;
  assistanceUnit: AssistanceUnitPayload | null = null;
  genderIdentityOptions = [
    'Mulher cisgênero',
    'Homem cisgênero',
    'Mulher transgênero',
    'Homem transgênero',
    'Pessoa não binária',
    'Travesti',
    'Gênero fluido',
    'Outro',
    'Prefiro não informar'
  ];
  maritalStatusOptions = [
    'Solteiro(a)',
    'Casado(a)',
    'União estável',
    'Separado(a)',
    'Divorciado(a)',
    'Viúvo(a)'
  ];
  nationalityOptions = [
    'Afegã(o)',
    'Alemã(o)',
    'Angolana(o)',
    'Argentina(o)',
    'Australiana(o)',
    'Belga',
    'Boliviana(o)',
    'Brasileira',
    'Brasileiro',
    'Canadense',
    'Chilena(o)',
    'Chinesa(o)',
    'Colombiana(o)',
    'Coreana(o)',
    'Costa-riquenha(o)',
    'Cubana(o)',
    'Dinamarquesa(o)',
    'Egípcia(o)',
    'Espanhola(o)',
    'Estadunidense',
    'Filipina(o)',
    'Finlandesa(o)',
    'Francesa(o)',
    'Grega(o)',
    'Haitiana(o)',
    'Holandesa(o)',
    'Indiana(o)',
    'Inglesa(o)',
    'Iraniana(o)',
    'Iraquiana(o)',
    'Irlandesa(o)',
    'Israelense',
    'Italiana(o)',
    'Japonesa(e)',
    'Marroquina(o)',
    'Mexicana(o)',
    'Moçambicana(o)',
    'Norueguesa(o)',
    'Paraguaia(o)',
    'Peruana(o)',
    'Polonesa(o)',
    'Portuguesa(o)',
    'Russa(o)',
    'Senegalesa(o)',
    'Sul-africana(o)',
    'Sueca(o)',
    'Suíça(o)',
    'Turca(o)',
    'Uruguaia(o)',
    'Venezuelana(o)'
  ];
  brazilianStates = [
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
  listLoading = false;
  listError: string | null = null;
  preferredContactOptions = [
    { value: 'MANHA', label: 'Manhã' },
    { value: 'TARDE', label: 'Tarde' },
    { value: 'NOITE', label: 'Noite' },
    { value: 'COMERCIAL', label: 'Horário comercial' },
    { value: 'QUALQUER', label: 'Qualquer horário' }
  ];
  statusOptions: BeneficiarioApiPayload['status'][] = [
    'ATIVO',
    'INATIVO',
    'DESATUALIZADO',
    'INCOMPLETO',
    'EM_ANALISE',
    'BLOQUEADO'
  ];
  blockReasonModalOpen = false;
  blockReasonError: string | null = null;
  lastStatus: BeneficiarioApiPayload['status'] | null = 'EM_ANALISE';
  previousStatusBeforeBlock: BeneficiarioApiPayload['status'] | null = 'EM_ANALISE';
  photoPreview: string | null = null;
  cameraActive = false;
  captureError: string | null = null;
  cepLookupError: string | null = null;
  situacaoImovelOptions = ['Próprio', 'Alugado', 'Cedido', 'Financiado', 'Ocupação', 'Outro'];
  tipoMoradiaOptions = ['Casa', 'Apartamento', 'Cômodo', 'Barraco', 'Casa de madeira', 'Sítio/Chácara', 'Outro'];
  private isUpdatingNationality = false;
  private nationalityManuallyChanged = false;
  private videoStream?: MediaStream;
  private readonly destroy$ = new Subject<void>();
  @ViewChild('videoElement') videoElement?: ElementRef<HTMLVideoElement>;
  @ViewChild('canvasElement') canvasElement?: ElementRef<HTMLCanvasElement>;
  private readonly sentenceCaseFields: (string | number)[][] = [
    ['dadosPessoais', 'nome_completo'],
    ['dadosPessoais', 'nome_social'],
    ['dadosPessoais', 'apelido'],
    ['dadosPessoais', 'identidade_genero'],
    ['dadosPessoais', 'estado_civil'],
    ['dadosPessoais', 'nacionalidade'],
    ['dadosPessoais', 'naturalidade_cidade'],
    ['dadosPessoais', 'nome_mae'],
    ['dadosPessoais', 'nome_pai'],
    ['endereco', 'logradouro'],
    ['endereco', 'complemento'],
    ['endereco', 'bairro'],
    ['endereco', 'ponto_referencia'],
    ['endereco', 'municipio'],
    ['endereco', 'situacao_imovel'],
    ['endereco', 'tipo_moradia'],
    ['contato', 'telefone_recado_nome'],
    ['contato', 'horario_preferencial_contato'],
    ['documentos', 'certidao_tipo'],
    ['documentos', 'certidao_livro'],
    ['documentos', 'certidao_folha'],
    ['documentos', 'certidao_termo'],
    ['documentos', 'certidao_cartorio'],
    ['documentos', 'certidao_municipio'],
    ['documentos', 'titulo_eleitor'],
    ['documentos', 'cnh'],
    ['documentos', 'cartao_sus'],
    ['familiar', 'vinculo_familiar'],
    ['familiar', 'composicao_familiar'],
    ['familiar', 'participa_comunidade'],
    ['familiar', 'rede_apoio'],
    ['familiar', 'situacao_vulnerabilidade'],
    ['escolaridade', 'nivel_escolaridade'],
    ['escolaridade', 'ocupacao'],
    ['escolaridade', 'situacao_trabalho'],
    ['escolaridade', 'local_trabalho'],
    ['escolaridade', 'fonte_renda'],
    ['saude', 'tipo_deficiencia'],
    ['saude', 'descricao_medicacao'],
    ['saude', 'servico_saude_referencia'],
    ['beneficios', 'beneficios_descricao'],
    ['observacoes', 'observacoes']
  ];
  educationLevelOptions: string[] = [
    'Sem escolaridade formal',
    'Ensino fundamental incompleto',
    'Ensino fundamental completo',
    'Ensino médio incompleto',
    'Ensino médio completo',
    'Ensino técnico',
    'Ensino superior incompleto',
    'Ensino superior completo',
    'Pós-graduação',
    'Mestrado',
    'Doutorado'
  ];
  availableBenefits: string[] = [
    'Bolsa Família / PTR',
    'BPC - Idoso',
    'BPC - Pessoa com deficiência',
    'Benefício eventual',
    'Programa de moradia',
    'Auxílio-doença',
    'Seguro-desemprego',
    'Outros'
  ];

  tabs = [
    { id: 'dados', label: 'Dados Pessoais' },
    { id: 'endereco', label: 'Endereço' },
    { id: 'contato', label: 'Contato' },
    { id: 'documentos', label: 'Documentos' },
    { id: 'familiar', label: 'Situação Familiar e Social' },
    { id: 'escolaridade', label: 'Escolaridade & Trabalho' },
    { id: 'saude', label: 'Saúde' },
    { id: 'beneficios', label: 'Benefícios' },
    { id: 'observacoes', label: 'Observações & Anexos' }
  ];

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

  get motivoBloqueioControl(): FormControl<string | null> {
    return this.form.get('motivo_bloqueio') as FormControl<string | null>;
  }

  constructor(
    private readonly fb: FormBuilder,
    private readonly service: BeneficiarioApiService,
    private readonly beneficiaryService: BeneficiaryService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    private readonly http: HttpClient,
    private readonly assistanceUnitService: AssistanceUnitService
  ) {
    this.searchForm = this.fb.group({
      nome: [''],
      cpf: [''],
      nis: [''],
      status: ['']
    });
    this.form = this.fb.group({
      status: ['EM_ANALISE', Validators.required],
      motivo_bloqueio: [''],
      foto_3x4: [''],
      dadosPessoais: this.fb.group({
        nome_completo: ['', Validators.required],
        nome_social: [''],
        apelido: [''],
        data_nascimento: ['', Validators.required],
        sexo_biologico: [''],
        identidade_genero: [''],
        cor_raca: [''],
        estado_civil: [''],
        nacionalidade: [''],
        naturalidade_cidade: [''],
        naturalidade_uf: [''],
        nome_mae: ['', Validators.required],
        nome_pai: ['']
      }),
      endereco: this.fb.group({
        usa_endereco_familia: [true],
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
      contato: this.fb.group({
        telefone_principal: ['', Validators.required],
        telefone_principal_whatsapp: [false],
        telefone_secundario: [''],
        telefone_recado_nome: [''],
        telefone_recado_numero: [''],
        email: ['', [Validators.email]],
        permite_contato_tel: [true],
        permite_contato_whatsapp: [true],
        permite_contato_sms: [false],
        permite_contato_email: [false],
        horario_preferencial_contato: ['']
      }),
      documentos: this.fb.group({
        cpf: ['', [Validators.required, this.cpfValidator]],
        rg_numero: [''],
        rg_orgao_emissor: [''],
        rg_uf: [''],
        rg_data_emissao: [''],
        nis: [''],
        certidao_tipo: [''],
        certidao_livro: [''],
        certidao_folha: [''],
        certidao_termo: [''],
        certidao_cartorio: [''],
        certidao_municipio: [''],
        certidao_uf: [''],
        titulo_eleitor: [''],
        cnh: [''],
        cartao_sus: [''],
        anexos: this.fb.array([])
      }),
      familiar: this.fb.group({
        mora_com_familia: [false],
        responsavel_legal: [false],
        vinculo_familiar: [''],
        situacao_vulnerabilidade: [''],
        composicao_familiar: [''],
        criancas_adolescentes: [''],
        idosos: [''],
        acompanhamento_cras: [false],
        acompanhamento_saude: [false],
        participa_comunidade: [''],
        rede_apoio: ['']
      }),
      escolaridade: this.fb.group({
        sabe_ler_escrever: [false],
        nivel_escolaridade: [''],
        estuda_atualmente: [false],
        ocupacao: [''],
        situacao_trabalho: [''],
        local_trabalho: [''],
        renda_mensal: [''],
        fonte_renda: ['']
      }),
      saude: this.fb.group({
        possui_deficiencia: [false],
        tipo_deficiencia: [''],
        cid_principal: [''],
        usa_medicacao_continua: [false],
        descricao_medicacao: [''],
        servico_saude_referencia: ['']
      }),
      beneficios: this.fb.group({
        recebe_beneficio: [false],
        beneficios_descricao: [''],
        valor_total_beneficios: [''],
        beneficios_recebidos: this.fb.control<string[]>([])
      }),
      observacoes: this.fb.group({
        aceite_lgpd: [false, Validators.requiredTrue],
        data_aceite_lgpd: [''],
        observacoes: ['']
      })
    });
  }

  private loadAssistanceUnit(): void {
    this.assistanceUnitService.get().subscribe({
      next: ({ unidade }) => {
        this.assistanceUnit = unidade ?? null;
      },
      error: () => {
        this.assistanceUnit = null;
      }
    });
  }

  goToNextTab(): void {
    if (!this.hasNextTab) return;

    const targetTab = this.tabs[this.activeTabIndex + 1].id;
    if (!this.validateCurrentTabRequirements(targetTab)) return;

    this.changeTab(targetTab);
  }

  goToPreviousTab(): void {
    if (this.hasPreviousTab) {
      this.changeTab(this.tabs[this.activeTabIndex - 1].id);
    }
  }

  ngOnInit(): void {
    this.loadRequiredDocuments();
    this.loadAssistanceUnit();
    this.watchBirthDate();
    this.setupNationalityAutomation();
    this.watchStatusChanges();
    this.setupSentenceCaseFormatting();
    this.searchBeneficiaries();
    this.searchForm
      .get('status')
      ?.valueChanges.pipe(takeUntil(this.destroy$))
      .subscribe(() => this.applyListFilters());
    this.route.paramMap.pipe(takeUntil(this.destroy$)).subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.beneficiarioId = id;
        this.service.getById(id).subscribe(({ beneficiario }) => {
          this.form.patchValue(this.mapToForm(beneficiario));
          this.photoPreview = beneficiario.foto_3x4 ?? null;
          this.lastStatus = beneficiario.status ?? 'EM_ANALISE';
          this.previousStatusBeforeBlock = this.lastStatus;
          this.nationalityManuallyChanged = !!beneficiario.nacionalidade;
          this.applyLoadedDocuments(beneficiario.documentosObrigatorios);
          this.applyAutomaticStatusFromDates(beneficiario.status);
        });
      }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.stopCamera();
  }

  get anexos(): FormArray {
    return this.form.get(['documentos', 'anexos']) as FormArray;
  }

  mapToForm(beneficiario: BeneficiarioApiPayload) {
    this.createdAt = beneficiario.data_cadastro ?? null;
    this.lastUpdatedAt = beneficiario.data_atualizacao ?? beneficiario.data_cadastro ?? null;

    return {
      status: beneficiario.status ?? 'EM_ANALISE',
      motivo_bloqueio: beneficiario.motivo_bloqueio,
      foto_3x4: beneficiario.foto_3x4,
      dadosPessoais: {
        nome_completo: beneficiario.nome_completo,
        nome_social: beneficiario.nome_social,
        apelido: beneficiario.apelido,
        data_nascimento: beneficiario.data_nascimento,
        sexo_biologico: beneficiario.sexo_biologico,
        identidade_genero: beneficiario.identidade_genero,
        cor_raca: beneficiario.cor_raca,
        estado_civil: beneficiario.estado_civil,
        nacionalidade: beneficiario.nacionalidade,
        naturalidade_cidade: beneficiario.naturalidade_cidade,
        naturalidade_uf: beneficiario.naturalidade_uf,
        nome_mae: beneficiario.nome_mae,
        nome_pai: beneficiario.nome_pai
      },
      endereco: {
        usa_endereco_familia: beneficiario.usa_endereco_familia,
        cep: this.formatCep(beneficiario.cep),
        logradouro: beneficiario.logradouro,
        numero: beneficiario.numero,
        complemento: beneficiario.complemento,
        bairro: beneficiario.bairro,
        ponto_referencia: beneficiario.ponto_referencia,
        municipio: beneficiario.municipio,
        uf: beneficiario.uf,
        zona: beneficiario.zona || 'URBANA',
        situacao_imovel: beneficiario.situacao_imovel,
        tipo_moradia: beneficiario.tipo_moradia,
        agua_encanada: beneficiario.agua_encanada,
        esgoto_tipo: beneficiario.esgoto_tipo,
        coleta_lixo: beneficiario.coleta_lixo,
        energia_eletrica: beneficiario.energia_eletrica,
        internet: beneficiario.internet
      },
      contato: {
        telefone_principal: this.formatPhoneValue(beneficiario.telefone_principal),
        telefone_principal_whatsapp: beneficiario.telefone_principal_whatsapp,
        telefone_secundario: this.formatPhoneValue(beneficiario.telefone_secundario),
        telefone_recado_nome: beneficiario.telefone_recado_nome,
        telefone_recado_numero: this.formatPhoneValue(beneficiario.telefone_recado_numero),
        email: beneficiario.email,
        permite_contato_tel: beneficiario.permite_contato_tel,
        permite_contato_whatsapp: beneficiario.permite_contato_whatsapp,
        permite_contato_sms: beneficiario.permite_contato_sms,
        permite_contato_email: beneficiario.permite_contato_email,
        horario_preferencial_contato: beneficiario.horario_preferencial_contato
      },
      documentos: {
        cpf: this.formatCpf(beneficiario.cpf),
        rg_numero: beneficiario.rg_numero,
        rg_orgao_emissor: beneficiario.rg_orgao_emissor,
        rg_uf: beneficiario.rg_uf,
        rg_data_emissao: beneficiario.rg_data_emissao,
        nis: beneficiario.nis,
        certidao_tipo: beneficiario.certidao_tipo,
        certidao_livro: beneficiario.certidao_livro,
        certidao_folha: beneficiario.certidao_folha,
        certidao_termo: beneficiario.certidao_termo,
        certidao_cartorio: beneficiario.certidao_cartorio,
        certidao_municipio: beneficiario.certidao_municipio,
        certidao_uf: beneficiario.certidao_uf,
        titulo_eleitor: beneficiario.titulo_eleitor,
        cnh: beneficiario.cnh,
        cartao_sus: beneficiario.cartao_sus
      },
      familiar: {
        mora_com_familia: beneficiario.mora_com_familia,
        responsavel_legal: beneficiario.responsavel_legal,
        vinculo_familiar: beneficiario.vinculo_familiar,
        situacao_vulnerabilidade: beneficiario.situacao_vulnerabilidade,
        composicao_familiar: beneficiario.composicao_familiar,
        criancas_adolescentes: beneficiario.criancas_adolescentes,
        idosos: beneficiario.idosos,
        acompanhamento_cras: beneficiario.acompanhamento_cras,
        acompanhamento_saude: beneficiario.acompanhamento_saude,
        participa_comunidade: beneficiario.participa_comunidade,
        rede_apoio: beneficiario.rede_apoio
      },
      escolaridade: {
        sabe_ler_escrever: beneficiario.sabe_ler_escrever,
        nivel_escolaridade: beneficiario.nivel_escolaridade,
        estuda_atualmente: beneficiario.estuda_atualmente,
        ocupacao: beneficiario.ocupacao,
        situacao_trabalho: beneficiario.situacao_trabalho,
        local_trabalho: beneficiario.local_trabalho,
        renda_mensal: beneficiario.renda_mensal,
        fonte_renda: beneficiario.fonte_renda
      },
      saude: {
        possui_deficiencia: beneficiario.possui_deficiencia,
        tipo_deficiencia: beneficiario.tipo_deficiencia,
        cid_principal: beneficiario.cid_principal,
        usa_medicacao_continua: beneficiario.usa_medicacao_continua,
        descricao_medicacao: beneficiario.descricao_medicacao,
        servico_saude_referencia: beneficiario.servico_saude_referencia
      },
      beneficios: {
        recebe_beneficio: beneficiario.recebe_beneficio,
        beneficios_descricao: beneficiario.beneficios_descricao,
        valor_total_beneficios: beneficiario.valor_total_beneficios,
        beneficios_recebidos: beneficiario.beneficios_recebidos || []
      },
      observacoes: {
        aceite_lgpd: beneficiario.aceite_lgpd,
        data_aceite_lgpd: beneficiario.data_aceite_lgpd,
        observacoes: beneficiario.observacoes
      }
    };
  }

  private loadRequiredDocuments(): void {
    this.beneficiaryService.getRequiredDocuments().subscribe({
      next: ({ documents }) => {
        this.documentosObrigatorios = documents;
        this.resetDocumentArray();
      },
      error: () => {
        this.documentosObrigatorios = [];
        this.resetDocumentArray();
      }
    });
  }

  private resetDocumentArray(existing?: DocumentoObrigatorio[]): void {
    this.anexos.clear();
    const baseDocs = existing?.length ? existing : this.documentosObrigatorios;

    if (!baseDocs.length) {
      this.anexos.push(this.buildDocumentControl({ nome: 'Documento de identificação', obrigatorio: true }));
      this.anexos.push(this.buildDocumentControl({ nome: 'Comprovante de residência', obrigatorio: true }));
      return;
    }

    baseDocs.forEach((doc) => {
      this.anexos.push(
        this.buildDocumentControl({
          nome: doc.nome,
          obrigatorio: doc.obrigatorio ?? doc.required ?? doc.baseRequired,
          nomeArquivo: doc.nomeArquivo
        })
      );
    });
  }

  private buildDocumentControl(doc: Partial<DocumentoObrigatorio>): FormGroup {
    return this.fb.group({
      nome: [doc.nome ?? '', Validators.required],
      obrigatorio: [doc.obrigatorio ?? false],
      nomeArquivo: [doc.nomeArquivo ?? ''],
      file: [doc.file ?? null]
    });
  }

  addOptionalDocument(): void {
    this.anexos.push(this.buildDocumentControl({ nome: 'Documento adicional', obrigatorio: false }));
  }

  applyLoadedDocuments(documents?: DocumentoObrigatorio[]): void {
    if (documents?.length) {
      this.resetDocumentArray(documents);
    }
  }

  onDocumentFileSelected(event: Event, index: number): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    const control = this.anexos.at(index) as FormGroup;

    if (file) {
      control.patchValue({ file, nomeArquivo: file.name });
      control.markAsDirty();
    }
  }

  private getMissingRequiredDocuments(): string[] {
    return this.anexos.controls
      .filter((control) => control.get('obrigatorio')?.value && !control.get('nomeArquivo')?.value)
      .map((control) => control.get('nome')?.value as string);
  }

  private validateRequiredDocuments(): boolean {
    const missing = this.getMissingRequiredDocuments();

    if (missing.length) {
      this.feedback = `Envie os documentos obrigatórios: ${missing.join(', ')}`;
      this.changeTab('documentos');
      return false;
    }

    return true;
  }

  saveStatusChange(): void {
    if (this.saving) return;
    if (!this.beneficiarioId) {
      this.feedback = 'Selecione um beneficiário salvo para atualizar o status.';
      return;
    }

    this.submit();
  }

  private isOutdatedDate(dateValue?: string | null): boolean {
    if (!dateValue) return false;
    const date = new Date(dateValue);
    if (isNaN(date.getTime())) return false;

    const oneYearMs = 1000 * 60 * 60 * 24 * 365;
    return Date.now() - date.getTime() > oneYearMs;
  }

  private applyAutomaticStatusFromDates(statusFromApi?: string | null): void {
    const outdated = this.isOutdatedDate(this.lastUpdatedAt || this.createdAt);

    if (outdated && statusFromApi !== 'BLOQUEADO' && statusFromApi !== 'INCOMPLETO') {
      this.form.get('status')?.setValue('DESATUALIZADO');
      this.lastStatus = 'DESATUALIZADO';
    }
  }

  private determineStatusForSave(): BeneficiarioApiPayload['status'] {
    const missingDocuments = this.getMissingRequiredDocuments();
    const hasPending = this.form.invalid || missingDocuments.length > 0;

    if (!this.beneficiarioId) {
      return hasPending ? 'INCOMPLETO' : 'EM_ANALISE';
    }

    const manualStatus = (this.form.get('status')?.value as BeneficiarioApiPayload['status']) ?? 'EM_ANALISE';

    if (hasPending) {
      return 'INCOMPLETO';
    }

    if (this.isOutdatedDate(this.lastUpdatedAt || this.createdAt) && manualStatus !== 'BLOQUEADO') {
      return 'DESATUALIZADO';
    }

    return manualStatus;
  }

  toggleBenefit(option: string): void {
    const control = this.form.get(['beneficios', 'beneficios_recebidos']);
    const current = new Set(control?.value ?? []);

    if (current.has(option)) {
      current.delete(option);
    } else {
      current.add(option);
    }

    control?.setValue(Array.from(current));
  }

  selectionChecked(option: string): boolean {
    const control = this.form.get(['beneficios', 'beneficios_recebidos']);
    return (control?.value as string[] | undefined)?.includes(option) ?? false;
  }

  formatCurrency(event: Event, groupName = 'beneficios', controlName = 'valor_total_beneficios'): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '');
    const numeric = Number(digits || '0') / 100;
    const formatted = numeric
      .toFixed(2)
      .replace('.', ',')
      .replace(/\B(?=(\d{3})+(?!\d))/g, '.');

    const control = this.form.get([groupName, controlName]);
    control?.setValue(numeric ? numeric.toFixed(2) : '');
    input.value = numeric ? `R$ ${formatted}` : '';
  }

  handleLgpdToggle(event: Event): void {
    const input = event.target as HTMLInputElement;
    const dateControl = this.form.get(['observacoes', 'data_aceite_lgpd']);
    if (input.checked) {
      if (!dateControl?.value) {
        dateControl?.setValue(this.getCurrentLocalDateTime());
      }
      this.printConsentDocument();
    } else {
      dateControl?.setValue('');
    }
  }

  private getCurrentLocalDateTime(): string {
    const now = new Date();
    const offsetMs = now.getTimezoneOffset() * 60000;
    const local = new Date(now.getTime() - offsetMs);
    return local.toISOString().slice(0, 16);
  }

  printConsentDocument(): void {
    const documentWindow = window.open('', '_blank', 'width=900,height=1100');
    if (!documentWindow) return;

    documentWindow.document.write(this.buildConsentHtml());
    documentWindow.document.close();
    documentWindow.focus();
    documentWindow.print();
  }

  printBeneficiaryList(): void {
    if (!this.filteredBeneficiarios.length) {
      this.feedback = 'Nenhum beneficiário encontrado para imprimir.';
      return;
    }

    const rows = this.filteredBeneficiarios
      .map((beneficiario) => {
        const birth = this.formatDate(beneficiario.data_nascimento);
        return `
          <tr>
            <td>${beneficiario.nome_completo || beneficiario.nome_social || '---'}</td>
            <td>${beneficiario.cpf || beneficiario.nis || '---'}</td>
            <td>${birth}</td>
            <td>${this.formatStatusLabel(beneficiario.status)}</td>
          </tr>
        `;
      })
      .join('');

    const documentWindow = window.open('', '_blank', 'width=1000,height=900');
    if (!documentWindow) return;

    documentWindow.document.write(`
      <html>
        <head>
          <title>Relação de beneficiários</title>
          ${this.printStyles()}
        </head>
        <body>
          <h1>Relação de beneficiários</h1>
          <p class="muted">Dados gerados em ${this.formatDate(new Date().toISOString())}</p>
          <table class="print-table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Documento/NIS</th>
                <th>Nascimento</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>${rows}</tbody>
          </table>
        </body>
      </html>
    `);

    documentWindow.document.close();
    documentWindow.focus();
    documentWindow.print();
  }

  printIndividualRecord(): void {
    const value = this.form.value as any;
    const personal = value.dadosPessoais ?? {};
    const address = value.endereco ?? {};
    const contact = value.contato ?? {};
    const documents = value.documentos ?? {};

    const documentWindow = window.open('', '_blank', 'width=1000,height=1200');
    if (!documentWindow) return;

    documentWindow.document.write(`
      <html>
        <head>
          <title>Ficha individual do beneficiário</title>
          ${this.printStyles()}
        </head>
        <body>
          <h1>Ficha individual do beneficiário</h1>
          <section>
            <h2>Dados pessoais</h2>
            <ul class="detail-list">
              <li><strong>Nome:</strong> ${personal.nome_completo || '---'}</li>
              <li><strong>Nome social:</strong> ${personal.nome_social || '---'}</li>
              <li><strong>Apelido:</strong> ${personal.apelido || '---'}</li>
              <li><strong>Data de nascimento:</strong> ${this.formatDate(personal.data_nascimento)}</li>
              <li><strong>Nome da mãe:</strong> ${personal.nome_mae || '---'}</li>
              <li><strong>Nome do pai:</strong> ${personal.nome_pai || '---'}</li>
              <li><strong>Sexo biológico:</strong> ${personal.sexo_biologico || '---'}</li>
              <li><strong>Identidade de gênero:</strong> ${personal.identidade_genero || '---'}</li>
              <li><strong>Estado civil:</strong> ${personal.estado_civil || '---'}</li>
              <li><strong>Nacionalidade:</strong> ${personal.nacionalidade || '---'}</li>
              <li><strong>Naturalidade:</strong> ${this.formatCity(personal.naturalidade_cidade, personal.naturalidade_uf)}</li>
            </ul>
          </section>

          <section>
            <h2>Documentos</h2>
            <ul class="detail-list">
              <li><strong>CPF:</strong> ${documents.cpf || '---'}</li>
              <li><strong>RG:</strong> ${documents.rg_numero || '---'}</li>
              <li><strong>Órgão emissor:</strong> ${documents.rg_orgao_emissor || '---'}</li>
              <li><strong>UF emissor:</strong> ${documents.rg_uf || '---'}</li>
              <li><strong>Data emissão RG:</strong> ${this.formatDate(documents.rg_data_emissao)}</li>
              <li><strong>NIS:</strong> ${documents.nis || '---'}</li>
              <li><strong>Certidão:</strong> ${documents.certidao_tipo || '---'} ${documents.certidao_livro || ''} ${documents.certidao_folha || ''} ${documents.certidao_termo || ''}</li>
              <li><strong>Título de eleitor:</strong> ${documents.titulo_eleitor || '---'}</li>
              <li><strong>CNH:</strong> ${documents.cnh || '---'}</li>
              <li><strong>Cartão SUS:</strong> ${documents.cartao_sus || '---'}</li>
            </ul>
          </section>

          <section>
            <h2>Endereço</h2>
            <p>${this.formatAddress(address)}</p>
            <ul class="detail-list">
              <li><strong>Zona:</strong> ${address.zona || '---'}</li>
              <li><strong>Situação do imóvel:</strong> ${address.situacao_imovel || '---'}</li>
              <li><strong>Tipo de moradia:</strong> ${address.tipo_moradia || '---'}</li>
              <li><strong>Infraestrutura:</strong> Água encanada ${this.formatBoolean(address.agua_encanada)}, Esgoto ${address.esgoto_tipo || '---'}, Coleta de lixo ${address.coleta_lixo || '---'}, Energia elétrica ${this.formatBoolean(address.energia_eletrica)}, Internet ${this.formatBoolean(address.internet)}</li>
            </ul>
          </section>

          <section>
            <h2>Contato</h2>
            <ul class="detail-list">
              <li><strong>Telefone principal:</strong> ${contact.telefone_principal || '---'}</li>
              <li><strong>WhatsApp:</strong> ${this.formatBoolean(contact.telefone_principal_whatsapp)}</li>
              <li><strong>Telefone secundário:</strong> ${contact.telefone_secundario || '---'}</li>
              <li><strong>Contato para recado:</strong> ${contact.telefone_recado_nome || '---'} (${contact.telefone_recado_numero || '---'})</li>
              <li><strong>Email:</strong> ${contact.email || '---'}</li>
              <li><strong>Preferência de contato:</strong> ${contact.horario_preferencial_contato || '---'}</li>
              <li><strong>Autorizações:</strong> Tel ${this.formatBoolean(contact.permite_contato_tel)} | WhatsApp ${this.formatBoolean(contact.permite_contato_whatsapp)} | SMS ${this.formatBoolean(contact.permite_contato_sms)} | Email ${this.formatBoolean(contact.permite_contato_email)}</li>
            </ul>
          </section>

          <section>
            <h2>Status e observações</h2>
            <ul class="detail-list">
              <li><strong>Status:</strong> ${this.formatStatusLabel(value.status)}</li>
              <li><strong>Motivo do bloqueio:</strong> ${value.motivo_bloqueio || '---'}</li>
              <li><strong>Observações:</strong> ${(value.observacoes?.observacoes || '---')}</li>
            </ul>
          </section>

          <p class="muted">Gerado em ${this.formatDate(new Date().toISOString())}</p>
        </body>
      </html>
    `);

    documentWindow.document.close();
    documentWindow.focus();
    documentWindow.print();
  }

  private buildConsentHtml(): string {
    const value = this.form.value as any;
    const personal = value.dadosPessoais ?? {};
    const documents = value.documentos ?? {};
    const contact = value.contato ?? {};
    const address = value.endereco ?? {};
    const unit = this.assistanceUnit;

    const today = this.formatDate(new Date().toISOString());
    const beneficiaryName = personal.nome_completo || 'Beneficiário';

    const institutionInfo = unit
      ? `
        <div class="card">
          <h3>${unit.nomeFantasia || unit.razaoSocial || 'Instituição'}</h3>
          <p><strong>CNPJ:</strong> ${unit.cnpj || '---'}</p>
          <p><strong>Contato:</strong> ${unit.telefone || '---'} | ${unit.email || '---'}</p>
          <p><strong>Endereço:</strong> ${this.formatInstitutionAddress(unit)}</p>
          <p><strong>Responsável legal:</strong> ${unit.responsavelNome || '---'}</p>
        </div>`
      : '';

    return `
      <html>
        <head>
          <title>Termo de autorização e consentimento</title>
          ${this.printStyles()}
          <style>
            .card { border: 1px solid #e2e8f0; padding: 12px; border-radius: 10px; margin: 12px 0; }
          </style>
        </head>
        <body>
          <h1>Termo de autorização, tratamento de dados e consentimento informado</h1>
          <p class="muted">Documento emitido em ${today}</p>
          ${institutionInfo}
          <div class="card">
            <h2>Identificação do beneficiário</h2>
            <p><strong>Nome:</strong> ${beneficiaryName}</p>
            <p><strong>CPF:</strong> ${documents.cpf || '---'} | <strong>RG:</strong> ${documents.rg_numero || '---'} | <strong>NIS:</strong> ${documents.nis || '---'}</p>
            <p><strong>Data de nascimento:</strong> ${this.formatDate(personal.data_nascimento)} | <strong>Nome da mãe:</strong> ${personal.nome_mae || '---'}</p>
            <p><strong>Endereço:</strong> ${this.formatAddress(address)}</p>
            <p><strong>Contato:</strong> ${contact.telefone_principal || '---'} ${contact.email ? ' | ' + contact.email : ''}</p>
          </div>
          <h2>Objeto do consentimento</h2>
          <p>Autorizo a coleta, utilização e armazenamento dos meus dados pessoais e sensíveis pela instituição acima identificada para fins de atendimento socioassistencial, registros administrativos, cumprimento de obrigações legais e prestação de contas junto a órgãos públicos.</p>
          <h2>Cláusulas e esclarecimentos legais</h2>
          <ul>
            <li>Os dados serão tratados conforme a Lei Geral de Proteção de Dados (Lei nº 13.709/2018), respeitando princípios de finalidade, necessidade e transparência.</li>
            <li>Imagens e registros poderão ser usados para identificação, segurança, comprovação de atendimento e divulgação institucional sem fins comerciais.</li>
            <li>O compartilhamento de informações ocorrerá apenas quando indispensável para políticas públicas, programas sociais e convênios, garantindo sigilo e segurança.</li>
            <li>Tenho ciência de que posso solicitar acesso, correção ou eliminação de dados, bem como revogar este consentimento a qualquer tempo, exceto quando houver fundamento legal para sua manutenção.</li>
            <li>Declaro ter sido informado(a) sobre a guarda segura dos dados, prazos de retenção e canais para exercício dos meus direitos.</li>
          </ul>
          <h2>Autorização expressa</h2>
          <p>Autorizo o tratamento descrito, assumindo responsabilidade pela veracidade das informações prestadas, e permito o uso da minha assinatura e identificação em registros físicos ou digitais necessários aos serviços prestados.</p>
          <div class="signature">
            <p>${beneficiaryName}</p>
            <p class="muted">${today}</p>
            <p class="muted">Assinatura do beneficiário ou responsável</p>
          </div>
        </body>
      </html>
    `;
  }

  private printStyles(): string {
    return `
      <style>
        body { font-family: Arial, sans-serif; padding: 24px; line-height: 1.6; color: #0f172a; }
        h1 { color: #0f766e; margin-bottom: 8px; }
        h2 { margin-top: 20px; color: #0ea5e9; }
        h3 { margin: 8px 0; color: #0f172a; }
        p { margin: 6px 0; }
        .muted { color: #64748b; }
        ul { padding-left: 18px; }
        ul li { margin: 6px 0; }
        .print-table { width: 100%; border-collapse: collapse; margin-top: 12px; }
        .print-table th, .print-table td { border: 1px solid #e2e8f0; padding: 8px; text-align: left; }
        .detail-list { list-style: none; padding: 0; margin: 0; }
        .detail-list li { margin: 6px 0; }
        .signature { margin-top: 40px; text-align: center; }
      </style>
    `;
  }

  private formatInstitutionAddress(unit: AssistanceUnitPayload): string {
    const parts = [unit.endereco, unit.numeroEndereco, unit.bairro, unit.cidade, unit.estado, unit.cep]
      .filter(Boolean)
      .join(', ');
    return parts || '---';
  }

  private formatAddress(address: any): string {
    const parts = [address.logradouro, address.numero, address.bairro, address.municipio, address.uf, address.cep]
      .filter(Boolean)
      .join(', ');
    return parts || '---';
  }

  private formatCity(city?: string, uf?: string): string {
    if (!city && !uf) return '---';
    return [city, uf].filter(Boolean).join(' - ');
  }

  private formatBoolean(value?: boolean): string {
    return value ? 'Sim' : 'Não';
  }

  private formatDate(value?: string | null): string {
    if (!value) return '---';
    const date = new Date(value);
    if (isNaN(date.getTime())) return '---';
    return date.toLocaleDateString('pt-BR');
  }

  changeTab(tab: string) {
    const documentsIndex = this.tabs.findIndex((item) => item.id === 'documentos');
    const targetIndex = this.tabs.findIndex((item) => item.id === tab);
    const cpfControl = this.form.get(['documentos', 'cpf']);

    if (!this.validateCurrentTabRequirements(tab)) {
      return;
    }

    if (documentsIndex >= 0 && targetIndex > documentsIndex && (cpfControl?.invalid || !cpfControl?.value)) {
      this.feedback = 'Informe um CPF válido antes de continuar.';
      cpfControl?.markAsTouched();
      this.form.get('documentos')?.markAllAsTouched();
      this.activeTab = 'documentos';
      return;
    }

    if (this.feedback === 'Informe um CPF válido antes de continuar.' && cpfControl?.valid) {
      this.feedback = null;
    }

    this.activeTab = tab;
  }

  private validateCurrentTabRequirements(targetTab: string): boolean {
    const currentIndex = this.activeTabIndex;
    const targetIndex = this.tabs.findIndex((item) => item.id === targetTab);

    if (targetIndex <= currentIndex) return true;

    const tabId = this.tabs[currentIndex]?.id;
    const requirements: Record<
      string,
      { controlPath: (string | number)[]; message: string; markGroup?: string }
    > = {
      endereco: {
        controlPath: ['endereco', 'cep'],
        message: 'Preencha o CEP (campo obrigatório) antes de avançar.',
        markGroup: 'endereco'
      },
      contato: {
        controlPath: ['contato', 'telefone_principal'],
        message: 'Informe o telefone principal (campo obrigatório) antes de avançar.',
        markGroup: 'contato'
      },
      observacoes: {
        controlPath: ['observacoes', 'aceite_lgpd'],
        message: 'Confirme o aceite LGPD antes de avançar.',
        markGroup: 'observacoes'
      }
    };

    const requirement = tabId ? requirements[tabId] : undefined;
    if (!requirement) return true;

    const control = this.form.get(requirement.controlPath);

    if (control?.valid && this.feedback === requirement.message) {
      this.feedback = null;
    }

    if (!control || control.valid) return true;

    this.feedback = requirement.message;
    if (requirement.markGroup) {
      this.form.get(requirement.markGroup)?.markAllAsTouched();
    }
    return false;
  }

  async submit() {
    const statusForSave = this.determineStatusForSave();

    if (this.form.invalid) {
      this.form.get('status')?.setValue(statusForSave);
      this.feedback = 'Preencha os campos obrigatórios.';
      return;
    }
    if (!this.validateRequiredDocuments()) {
      this.form.get('status')?.setValue('INCOMPLETO');
      return;
    }
    if (this.form.get('status')?.value === 'BLOQUEADO' && !this.form.get('motivo_bloqueio')?.value) {
      this.feedback = 'Informe o motivo do bloqueio antes de salvar.';
      this.blockReasonModalOpen = true;
      return;
    }

    this.saving = true;

    try {
      const payload = await this.toPayload(statusForSave);
      const isDuplicate = await this.hasDuplicate(payload);
      if (isDuplicate) {
        this.saving = false;
        return;
      }

      const request = this.beneficiarioId
        ? this.service.update(this.beneficiarioId, payload)
        : this.service.create(payload);

      request.pipe(finalize(() => (this.saving = false))).subscribe({
        next: () => {
          this.feedback = 'Registro salvo com sucesso';
          this.router.navigate(['/cadastros/beneficiarios']);
        },
        error: (error) => {
          this.feedback = error?.error?.message || 'Erro ao salvar beneficiário';
        }
      });
    } catch (error) {
      console.error('Erro ao preparar salvamento', error);
      this.feedback = 'Não foi possível salvar o beneficiário. Tente novamente.';
      this.saving = false;
    }
  }

  private async toPayload(statusForSave: BeneficiarioApiPayload['status']): Promise<BeneficiarioApiPayload> {
    const value = this.form.value;
    const documentosObrigatorios = await this.buildDocumentPayload();
    const endereco = {
      ...(value.endereco as any),
      cep: this.normalizeCep(value.endereco?.cep as string)
    };
    const contato = {
      ...(value.contato as any),
      telefone_principal: this.normalizePhone(value.contato?.telefone_principal as string),
      telefone_secundario: this.normalizePhone(value.contato?.telefone_secundario as string),
      telefone_recado_numero: this.normalizePhone(value.contato?.telefone_recado_numero as string)
    };
    const documentos = {
      ...(value.documentos as any),
      cpf: this.normalizeCpf(value.documentos?.cpf as string)
    };

    return {
      status: statusForSave,
      motivo_bloqueio: value.motivo_bloqueio,
      foto_3x4: value.foto_3x4,
      ...(value.dadosPessoais as any),
      ...(endereco as any),
      ...(contato as any),
      ...(documentos as any),
      ...(value.familiar as any),
      ...(value.escolaridade as any),
      ...(value.saude as any),
      ...(value.beneficios as any),
      ...(value.observacoes as any),
      documentosObrigatorios
    };
  }

  private async buildDocumentPayload(): Promise<DocumentoObrigatorio[]> {
    const documents = this.anexos.controls.map((control) => control.value as DocumentoObrigatorio & { file?: File | null });

    return documents.map((doc) => {
      const file = doc.file as File | undefined;
      return {
        nome: doc.nome,
        obrigatorio: doc.obrigatorio,
        nomeArquivo: file ? file.name : doc.nomeArquivo
      } as DocumentoObrigatorio;
    });
  }

  private setupSentenceCaseFormatting(): void {
    this.sentenceCaseFields.forEach((path) => {
      const control = this.form.get(path);

      control?.valueChanges.pipe(takeUntil(this.destroy$)).subscribe((value) => {
        if (typeof value !== 'string') return;

        const formatted = this.toSentenceCase(value);
        if (formatted !== value) {
          control.setValue(formatted, { emitEvent: false });
        }
      });
    });
  }

  private toSentenceCase(value: string): string {
    const normalized = value.toLowerCase();
    if (!normalized.trim()) return '';

    return normalized.replace(/(^|\s)([A-Za-zÀ-ÿ])/g, (match) => match.toUpperCase());
  }

  private watchBirthDate(): void {
    const control = this.form.get(['dadosPessoais', 'data_nascimento']);
    control?.valueChanges.pipe(takeUntil(this.destroy$)).subscribe((value) => {
      this.calculateAge(value as string);
    });
  }

  private setupNationalityAutomation(): void {
    const nationalityControl = this.form.get(['dadosPessoais', 'nacionalidade']);
    const sexControl = this.form.get(['dadosPessoais', 'sexo_biologico']);

    nationalityControl?.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(() => {
      if (this.isUpdatingNationality) return;
      this.nationalityManuallyChanged = true;
    });

    sexControl?.valueChanges.pipe(takeUntil(this.destroy$)).subscribe((sex) => {
      this.applyNationalityDefault(sex as string | null);
    });

    this.applyNationalityDefault(sexControl?.value as string | null);
  }

  private applyNationalityDefault(sex: string | null | undefined): void {
    const nationalityControl = this.form.get(['dadosPessoais', 'nacionalidade']);
    if (!nationalityControl) return;

    if (this.nationalityManuallyChanged && nationalityControl.value) return;

    const suggested =
      sex === 'FEMININO' ? 'Brasileira' : sex === 'MASCULINO' ? 'Brasileiro' : nationalityControl.value || '';

    this.isUpdatingNationality = true;
    nationalityControl.setValue(suggested, { emitEvent: false });
    this.isUpdatingNationality = false;
  }

  private calculateAge(dateValue: string | null): void {
    if (!dateValue) {
      this.beneficiaryAge = null;
      return;
    }

    const birthDate = new Date(dateValue);
    if (isNaN(birthDate.getTime())) {
      this.beneficiaryAge = null;
      return;
    }

    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    const dayDiff = today.getDate() - birthDate.getDate();

    if (monthDiff < 0 || (monthDiff === 0 && dayDiff < 0)) {
      age--;
    }

    this.beneficiaryAge = Math.max(age, 0);
  }

  private watchStatusChanges(): void {
    const control = this.form.get('status');
    this.lastStatus = control?.value ?? 'EM_ANALISE';

    control?.valueChanges.pipe(takeUntil(this.destroy$)).subscribe((status) => {
      const previous = this.lastStatus;
      this.lastStatus = status ?? null;

      if (status === 'BLOQUEADO') {
        this.previousStatusBeforeBlock = previous;
        this.blockReasonModalOpen = true;
        this.blockReasonError = null;
      } else {
        this.form.get('motivo_bloqueio')?.setValue('');
      }
    });
  }

  confirmBlockReason(): void {
    const reason = this.form.get('motivo_bloqueio')?.value as string | undefined;
    if (!reason?.trim()) {
      this.blockReasonError = 'Informe o motivo do bloqueio.';
      return;
    }

    this.blockReasonModalOpen = false;
    this.blockReasonError = null;
  }

  cancelBlockReason(): void {
    this.blockReasonModalOpen = false;
    this.blockReasonError = null;
    this.form.get('status')?.setValue(this.previousStatusBeforeBlock ?? 'EM_ANALISE');
    this.form.get('motivo_bloqueio')?.setValue('');
  }

  searchBeneficiaries(): void {
    const { nome, cpf, nis } = this.searchForm.value;
    this.listLoading = true;
    this.listError = null;

    this.service
      .list({ nome: nome || undefined, cpf: cpf || undefined, nis: nis || undefined })
      .pipe(finalize(() => (this.listLoading = false)))
      .subscribe({
        next: ({ beneficiarios }) => {
          this.beneficiarios = beneficiarios ?? [];
          this.applyListFilters();
        },
        error: () => {
          this.listError = 'Não foi possível carregar os beneficiários. Tente novamente.';
        }
      });
  }

  applyListFilters(): void {
    const status = this.searchForm.get('status')?.value;
    this.filteredBeneficiarios = (this.beneficiarios ?? []).filter(
      (beneficiario) => !status || beneficiario.status === status
    );
  }

  selectBeneficiario(beneficiario: BeneficiarioApiPayload): void {
    if (!beneficiario.id_beneficiario) return;

    this.beneficiarioId = beneficiario.id_beneficiario;
    this.service.getById(beneficiario.id_beneficiario).subscribe(({ beneficiario: details }) => {
      this.form.patchValue(this.mapToForm(details));
      this.photoPreview = details.foto_3x4 ?? null;
      this.lastStatus = details.status ?? 'EM_ANALISE';
      this.previousStatusBeforeBlock = this.lastStatus;
      this.applyLoadedDocuments(details.documentosObrigatorios);
      this.applyAutomaticStatusFromDates(details.status);
    });
  }

  deleteBeneficiario(beneficiario: BeneficiarioApiPayload): void {
    if (!beneficiario.id_beneficiario) return;
    const confirmDelete = window.confirm(
      `Excluir o beneficiário ${beneficiario.nome_completo || 'selecionado'}?`
    );
    if (!confirmDelete) return;

    this.listLoading = true;
    this.service.delete(beneficiario.id_beneficiario).subscribe({
      next: () => {
        if (this.beneficiarioId === beneficiario.id_beneficiario) {
          this.form.reset({ status: 'EM_ANALISE', motivo_bloqueio: '', foto_3x4: '', endereco: { zona: 'URBANA' } });
          this.beneficiarioId = null;
          this.photoPreview = null;
          this.createdAt = null;
          this.lastUpdatedAt = null;
        }
        this.searchBeneficiaries();
      },
      error: () => {
        this.listError = 'Não foi possível excluir o beneficiário.';
        this.listLoading = false;
      }
    });
  }

  editBeneficiario(beneficiario: BeneficiarioApiPayload): void {
    if (!beneficiario.id_beneficiario) return;
    this.router.navigate(['/cadastros/beneficiarios', beneficiario.id_beneficiario]);
  }

  async startCamera(): Promise<void> {
    this.captureError = null;
    if (!(navigator && navigator.mediaDevices && navigator.mediaDevices.getUserMedia)) {
      this.captureError = 'Seu navegador não permite capturar a câmera.';
      return;
    }

    try {
      this.cameraActive = true;

      await new Promise((resolve) => setTimeout(resolve, 0));
      this.videoStream = await navigator.mediaDevices.getUserMedia({ video: true });
      const video = this.videoElement?.nativeElement;

      if (video) {
        video.srcObject = this.videoStream;
        await video.play();
      }
    } catch (error) {
      console.error('Erro ao iniciar câmera', error);
      this.captureError = 'Não foi possível acessar a câmera.';
      this.cameraActive = false;

      if (this.videoStream) {
        this.videoStream.getTracks().forEach((track) => track.stop());
        this.videoStream = undefined;
      }
    }
  }

  async handleCameraCapture(): Promise<void> {
    if (this.cameraActive) {
      this.capturePhoto();
      return;
    }

    await this.startCamera();
  }

  capturePhoto(): void {
    const video = this.videoElement?.nativeElement;
    const canvas = this.canvasElement?.nativeElement;
    if (!video || !canvas) return;

    const context = canvas.getContext('2d');
    if (!context) return;

    canvas.width = 480;
    canvas.height = 640;
    context.drawImage(video, 0, 0, canvas.width, canvas.height);
    const dataUrl = canvas.toDataURL('image/png');
    this.setPhotoPreview(dataUrl);
    this.stopCamera();
  }

  stopCamera(): void {
    if (this.videoStream) {
      this.videoStream.getTracks().forEach((track) => track.stop());
      this.videoStream = undefined;
    }
    const video = this.videoElement?.nativeElement;
    if (video) {
      video.srcObject = null;
    }
    this.cameraActive = false;
  }

  onPhotoSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      const dataUrl = reader.result as string;
      this.setPhotoPreview(dataUrl);
    };
    reader.readAsDataURL(file);
  }

  removePhoto(): void {
    this.photoPreview = null;
    this.form.get('foto_3x4')?.reset();
  }

  private setPhotoPreview(dataUrl: string): void {
    this.photoPreview = dataUrl;
    this.form.get('foto_3x4')?.setValue(dataUrl);
  }

  private async hasDuplicate(payload: BeneficiarioApiPayload): Promise<boolean> {
    try {
      const { nome_completo, cpf, nis, data_nascimento, nome_mae } = payload;
      const { beneficiarios } = await firstValueFrom(
        this.service.list({ nome: nome_completo, cpf: cpf ?? undefined, nis: nis ?? undefined })
      );
      const duplicates = (beneficiarios ?? []).filter((item) => {
        if (this.beneficiarioId && item.id_beneficiario === this.beneficiarioId) return false;

        const sameCpf = cpf && item.cpf && item.cpf === cpf;
        const sameIdentity =
          this.normalizeString(item.nome_completo) === this.normalizeString(nome_completo) &&
          item.data_nascimento === data_nascimento &&
          this.normalizeString(item.nome_mae) === this.normalizeString(nome_mae);

        return sameCpf || sameIdentity;
      });

      if (duplicates.length) {
        this.feedback = 'Beneficiário já cadastrado. Utilize a lista para editar ou excluir.';
        this.changeTab('dados');
        return true;
      }
    } catch (error) {
      console.warn('Não foi possível verificar duplicidade', error);
    }

    return false;
  }

  private normalizeString(value?: string | null): string {
    return (value ?? '').trim().toLowerCase();
  }

  onCpfInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 11);
    const formatted = this.formatCpf(digits);

    this.form.get(['documentos', 'cpf'])?.setValue(formatted, { emitEvent: false });
    input.value = formatted;

    if (this.feedback === 'Informe um CPF válido antes de continuar.' && this.form.get(['documentos', 'cpf'])?.valid) {
      this.feedback = null;
    }
  }

  onPhoneInput(event: Event, controlName: 'telefone_principal' | 'telefone_secundario' | 'telefone_recado_numero'): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 11);
    const formatted = this.formatPhoneValue(digits);

    this.form.get(['contato', controlName])?.setValue(formatted, { emitEvent: false });
    input.value = formatted;
  }

  private formatPhoneValue(value?: string | null): string {
    const digits = (value ?? '').replace(/\D/g, '').slice(0, 11);
    if (!digits) return '';

    const hasNineDigits = digits.length > 10;
    const part1 = digits.slice(0, 2);
    const part2 = digits.slice(2, hasNineDigits ? 7 : 6);
    const part3 = digits.slice(hasNineDigits ? 7 : 6, hasNineDigits ? 11 : 10);

    return part3 ? `(${part1}) ${part2}-${part3}` : part2 ? `(${part1}) ${part2}` : `(${part1}`;
  }

  private normalizePhone(value?: string | null): string | undefined {
    const digits = (value ?? '').replace(/\D/g, '');
    return digits || undefined;
  }

  private formatCpf(value?: string | null): string {
    const digits = (value ?? '').replace(/\D/g, '').slice(0, 11);
    if (!digits) return '';
    const part1 = digits.slice(0, 3);
    const part2 = digits.slice(3, 6);
    const part3 = digits.slice(6, 9);
    const part4 = digits.slice(9, 11);

    return [part1, part2, part3].filter(Boolean).join('.') + (part4 ? `-${part4}` : '');
  }

  private normalizeCpf(value?: string | null): string | undefined {
    const digits = (value ?? '').replace(/\D/g, '');
    return digits || undefined;
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

  private lookupAddressByCep(cep: string): void {
    this.cepLookupError = null;
    this.http
      .get<ViaCepResponse>(`https://viacep.com.br/ws/${cep}/json/`)
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

  private formatCep(value?: string | null): string {
    const digits = this.normalizeCep(value ?? '');
    if (!digits) return '';
    return digits.length === 8 ? `${digits.slice(0, 5)}-${digits.slice(5)}` : digits;
  }

  private normalizeCep(value?: string | null): string | undefined {
    const digits = (value ?? '').replace(/\D/g, '');
    return digits || undefined;
  }

  private cpfValidator = (control: AbstractControl): ValidationErrors | null => {
    const value = (control.value as string | null | undefined)?.replace(/\D/g, '') ?? '';

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

  private cepValidator = (control: AbstractControl): ValidationErrors | null => {
    const value = (control.value as string | null | undefined)?.replace(/\D/g, '') ?? '';
    if (!value) return null;
    return value.length === 8 ? null : { cep: true };
  };

  formatStatusLabel(status?: string | null): string {
    if (!status) return '—';

    return status
      .toLowerCase()
      .split('_')
      .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
      .join(' ');
  }

  getStatusClass(status?: string | null): string {
    switch (status) {
      case 'ATIVO':
        return 'pill--status pill--status-ativo';
      case 'INATIVO':
        return 'pill--status pill--status-inativo';
      case 'DESATUALIZADO':
        return 'pill--status pill--status-desatualizado';
      case 'BLOQUEADO':
        return 'pill--status pill--status-bloqueado';
      case 'INCOMPLETO':
      case 'EM_ANALISE':
        return 'pill--status pill--status-analise';
      default:
        return 'pill--status';
    }
  }
}
