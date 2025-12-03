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
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
type ViaCepResponse = {
  logradouro?: string;
  complemento?: string;
  bairro?: string;
  localidade?: string;
  uf?: string;
  erro?: boolean;
};

type PrintOrder = 'nome' | 'data_nascimento' | 'idade' | 'bairro';

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
  beneficiaryCode: string | null = null;
  beneficiaryBarcode: string | null = null;
  beneficiarios: BeneficiarioApiPayload[] = [];
  filteredBeneficiarios: BeneficiarioApiPayload[] = [];
  createdAt: string | null = null;
  lastUpdatedAt: string | null = null;
  assistanceUnit: AssistanceUnitPayload | null = null;
  printOrderBy: PrintOrder = 'nome';
  readonly printOrderOptions: { value: PrintOrder; label: string }[] = [
    { value: 'nome', label: 'Nome' },
    { value: 'data_nascimento', label: 'Data de nascimento' },
    { value: 'idade', label: 'Idade' },
    { value: 'bairro', label: 'Bairro' }
  ];
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
  private hasLoadedExistingDocuments = false;
  @ViewChild('videoElement') videoElement?: ElementRef<HTMLVideoElement>;
  @ViewChild('canvasElement') canvasElement?: ElementRef<HTMLCanvasElement>;
  private readonly sentenceCaseFields: (string | number)[][] = [
    ['dadosPessoais', 'nome_completo'],
    ['dadosPessoais', 'nome_social'],
    ['dadosPessoais', 'apelido'],
    ['dadosPessoais', 'naturalidade_cidade'],
    ['dadosPessoais', 'nome_mae'],
    ['dadosPessoais', 'nome_pai'],
    ['endereco', 'logradouro'],
    ['endereco', 'complemento'],
    ['endereco', 'bairro'],
    ['endereco', 'ponto_referencia'],
    ['endereco', 'municipio'],
    ['contato', 'telefone_recado_nome'],
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
    ['escolaridade', 'ocupacao'],
    ['escolaridade', 'situacao_trabalho'],
    ['escolaridade', 'local_trabalho'],
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
      codigo: [''],
      cpf: [''],
      data_nascimento: [''],
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
    this.setupEducationControls();
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
          this.applyLoadedDocuments(this.getDocumentList(beneficiario));
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
    this.beneficiaryCode = beneficiario.codigo ?? null;
    this.beneficiaryBarcode = beneficiario.codigo_barras ?? null;

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
        if (this.hasLoadedExistingDocuments) {
          this.mergeRequiredDocumentsWithExisting();
        } else {
          this.resetDocumentArray();
        }
      },
      error: () => {
        this.documentosObrigatorios = [];
        if (!this.hasLoadedExistingDocuments) {
          this.resetDocumentArray();
        }
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
      const normalized = this.normalizeDocumentData(doc);
      this.anexos.push(
        this.buildDocumentControl({
          nome: normalized.nome,
          obrigatorio: normalized.obrigatorio,
          nomeArquivo: normalized.nomeArquivo,
          conteudo: normalized.conteudo,
          contentType: normalized.contentType
        })
      );
    });
  }

  private mergeRequiredDocumentsWithExisting(): void {
    if (!this.documentosObrigatorios.length) return;

    this.documentosObrigatorios.forEach((doc) => {
      const normalized = this.normalizeDocumentData(doc);
      const existingControl = this.anexos.controls.find(
        (control) => control.get('nome')?.value === normalized.nome
      ) as FormGroup | undefined;

      if (existingControl) {
        existingControl.patchValue(
          {
            obrigatorio: normalized.obrigatorio,
            nomeArquivo: existingControl.get('nomeArquivo')?.value || normalized.nomeArquivo,
            contentType: existingControl.get('contentType')?.value || normalized.contentType
          },
          { emitEvent: false }
        );
        return;
      }

      this.anexos.push(
        this.buildDocumentControl({
          nome: normalized.nome,
          obrigatorio: normalized.obrigatorio,
          nomeArquivo: normalized.nomeArquivo,
          conteudo: normalized.conteudo,
          contentType: normalized.contentType
        })
      );
    });
  }

  private normalizeDocumentData(doc: Partial<DocumentoObrigatorio>): DocumentoObrigatorio {
    const obrigatorio = doc.obrigatorio ?? doc.required ?? doc.baseRequired ?? false;
    const nomeArquivo = doc.nomeArquivo ?? (doc.conteudo ? doc.nome : '');

    return {
      nome: doc.nome ?? '',
      obrigatorio,
      nomeArquivo: nomeArquivo ?? '',
      conteudo: doc.conteudo,
      contentType: doc.contentType
    } as DocumentoObrigatorio;
  }

  private buildDocumentControl(doc: Partial<DocumentoObrigatorio>): FormGroup {
    return this.fb.group({
      nome: [doc.nome ?? '', Validators.required],
      obrigatorio: [doc.obrigatorio ?? false],
      nomeArquivo: [doc.nomeArquivo ?? ''],
      conteudo: [doc.conteudo ?? ''],
      contentType: [doc.contentType ?? ''],
      file: [doc.file ?? null]
    });
  }

  addOptionalDocument(): void {
    this.anexos.push(this.buildDocumentControl({ nome: 'Documento adicional', obrigatorio: false }));
  }

  applyLoadedDocuments(documents?: DocumentoObrigatorio[]): void {
    if (documents?.length) {
      this.hasLoadedExistingDocuments = true;
      const normalizedDocuments = documents.map((doc) => this.normalizeDocumentData(doc));
      this.resetDocumentArray(normalizedDocuments);
    }
  }

  private getDocumentList(beneficiario: BeneficiarioApiPayload): DocumentoObrigatorio[] | undefined {
    return (beneficiario as any).documentos_obrigatorios ?? beneficiario.documentosObrigatorios;
  }

  onDocumentFileSelected(event: Event, index: number): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    const control = this.anexos.at(index) as FormGroup;

    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        const result = reader.result as string;
        control.patchValue({
          file,
          nomeArquivo: file.name,
          conteudo: result,
          contentType: file.type
        });
      };
      reader.readAsDataURL(file);
      control.markAsDirty();
    }
  }

  viewDocument(index: number): void {
    const control = this.anexos.at(index) as FormGroup;
    const content = control.get('conteudo')?.value as string;
    const contentType = (control.get('contentType')?.value as string) || 'application/octet-stream';
    const fileName = (control.get('nomeArquivo')?.value as string) || (control.get('nome')?.value as string) || 'documento';

    if (!content) {
      this.feedback = 'Nenhum arquivo disponível para este documento.';
      return;
    }

    const dataUrl = content.startsWith('data:') ? content : `data:${contentType};base64,${content}`;
    const documentWindow = window.open('', '_blank', 'width=900,height=1100');
    if (!documentWindow) return;

    documentWindow.document.write(`
      <html>
        <head>
          <title>${fileName}</title>
        </head>
        <body style="margin:0; padding:0;">
          <iframe src="${dataUrl}" style="border:0; width:100%; height:100%;"></iframe>
        </body>
      </html>
    `);
    documentWindow.document.close();
    documentWindow.focus();
  }

  private getMissingRequiredDocuments(): string[] {
    return this.anexos.controls
      .filter(
        (control) =>
          control.get('obrigatorio')?.value &&
          !control.get('nomeArquivo')?.value &&
          !control.get('conteudo')?.value
      )
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

    this.submit(true, true);
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

  private determineStatusForSave(allowStatusOnlyUpdate = false): BeneficiarioApiPayload['status'] {
    const missingDocuments = allowStatusOnlyUpdate ? [] : this.getMissingRequiredDocuments();
    const hasPending = (!allowStatusOnlyUpdate && this.form.invalid) || missingDocuments.length > 0;

    const manualStatus = (this.form.get('status')?.value as BeneficiarioApiPayload['status']) ?? 'EM_ANALISE';

    if (!this.beneficiarioId) {
      return hasPending ? 'INCOMPLETO' : 'EM_ANALISE';
    }

    if (allowStatusOnlyUpdate) {
      return manualStatus;
    }

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

    const sorted = [...this.filteredBeneficiarios].sort((a, b) => this.sortBeneficiariesForPrint(a, b));
        const unit = this.assistanceUnit;
        const logo = unit?.logomarcaRelatorio || unit?.logomarca;
        const socialName = unit?.razaoSocial || unit?.nomeFantasia || 'Instituição';
        const orderLabel = this.printOrderOptions.find((option) => option.value === this.printOrderBy)?.label ?? 'Nome';
        const today = this.formatDate(new Date().toISOString());

        const rows = sorted
          .map((beneficiario) => {
            return `
              <tr>
                <td>${beneficiario.nome_completo || beneficiario.nome_social || '---'}</td>
                <td>${beneficiario.codigo || '---'}</td>
                <td>${this.formatDate(beneficiario.data_nascimento)}</td>
                <td>${this.formatAge(beneficiario.data_nascimento)}</td>
                <td>${beneficiario.cpf || beneficiario.nis || '---'}</td>
                <td>${this.formatStatusLabel(beneficiario.status)}</td>
                <td>${beneficiario.bairro || '---'}</td>
          </tr>
        `;
      })
      .join('');

    const header = `
      <header class="report-header report-header--center">
        ${logo ? `<img class="report-header__logo" src="${logo}" alt="Logomarca da unidade" />` : ''}
        <div class="report-header__identity">
          <p class="report-header__name">${socialName}</p>
          ${unit?.nomeFantasia && unit?.nomeFantasia !== unit?.razaoSocial ? `<p class="report-header__fantasy">${unit.nomeFantasia}</p>` : ''}
        </div>
      </header>
    `;

    const documentWindow = window.open('', '_blank', 'width=1000,height=900');
    if (!documentWindow) return;

    documentWindow.document.write(`
      <html>
        <head>
          <title>Relação de beneficiários</title>
          ${this.printStyles()}
        </head>
        <body>
          ${header}
          <h1>Relação de beneficiários</h1>
          <p class="muted">Dados gerados em ${today} | Ordenado por ${orderLabel.toLowerCase()}</p>
          <table class="print-table">
            <thead>
              <tr>
                <th>Nome completo</th>
                <th>Código</th>
                <th>Data de nascimento</th>
                <th>Idade</th>
                <th>CPF/NIS</th>
                <th>Status</th>
                <th>Bairro</th>
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

  async printIndividualRecord(): Promise<void> {
    const value = this.form.value as any;
    const personal = value.dadosPessoais ?? {};
    const address = value.endereco ?? {};
    const contact = value.contato ?? {};
    const documents = value.documentos ?? {};
    const family = value.familiar ?? {};
    const education = value.escolaridade ?? {};
    const health = value.saude ?? {};
    const benefits = value.beneficios ?? {};
    const unit = this.assistanceUnit;
    const logo = unit?.logomarcaRelatorio || unit?.logomarca;
    const socialName = unit?.razaoSocial || unit?.nomeFantasia || 'Instituição';
    const generatedAt = new Date();
    const formattedGeneratedAt = `${generatedAt.toLocaleDateString('pt-BR')} às ${generatedAt.toLocaleTimeString('pt-BR')}`;
    const age = this.getAgeFromDate(personal.data_nascimento);
    const beneficiaryName = personal.nome_completo || personal.nome_social || 'Beneficiário';
    const statusLabel = value.status ? this.formatStatusLabel(value.status) : '---';
    const codigo = this.beneficiaryCode || '---';
    const formattedBirthDate = this.formatDate(personal.data_nascimento);
    const formattedInclusionDate = this.formatDate(this.createdAt ?? null);
    const formattedNaturalidade =
      this.hasValue(personal.naturalidade_cidade) || this.hasValue(personal.naturalidade_uf)
        ? this.formatCity(personal.naturalidade_cidade, personal.naturalidade_uf)
        : null;
    const formattedCity =
      this.hasValue(address.municipio) || this.hasValue(address.uf)
        ? this.formatCity(address.municipio, address.uf)
        : null;

    const displayValue = (val?: string | number | null, placeholder = '---'): string =>
      this.hasValue(val) ? String(val) : placeholder;

    const photoUrl =
      this.photoPreview ||
      `https://ui-avatars.com/api/?name=${encodeURIComponent(beneficiaryName)}&background=0284c7&color=fff&size=256`;

    const rendaFamiliar = this.formatCurrencyValue(benefits.renda_familiar);
    const rendaPerCapita = this.formatCurrencyValue(benefits.renda_per_capita);
    const familyMembers = family.composicao_familiar || family.total_moradores;
    const institutionAddress = unit ? this.formatInstitutionAddress(unit) : '---';

    const htmlContent = this.buildIndividualRecordHtml({
      beneficiaryName,
      photoUrl,
      statusLabel,
      codigo,
      cpf: displayValue(documents.cpf),
      inclusionDate: formattedInclusionDate,
      category: displayValue(personal.categoria || personal.tipo_cadastro),
      program: displayValue(personal.programa_vinculado),
      birthDate: formattedBirthDate,
      age,
      rg: this.joinParts([documents.rg_numero, documents.rg_orgao_emissor], ' / ') || '---',
      rgUf: displayValue(documents.rg_uf),
      sexo: displayValue(personal.sexo_biologico),
      mae: displayValue(personal.nome_mae),
      pai: displayValue(personal.nome_pai),
      estadoCivil: displayValue(personal.estado_civil),
      naturalidade: displayValue(formattedNaturalidade),
      endereco: this.joinParts([
        this.joinParts([address.logradouro, address.numero], ', '),
        address.complemento,
        address.bairro,
        formattedCity,
        address.cep
      ]) || '---',
      referencia: displayValue(address.ponto_referencia),
      zona: displayValue(address.zona),
      telefone: displayValue(contact.telefone_principal),
      email: displayValue(contact.email),
      rendaFamiliar: displayValue(rendaFamiliar),
      rendaPerCapita: displayValue(rendaPerCapita),
      moradores: displayValue(familyMembers),
      trabalho: displayValue(education.situacao_trabalho || education.ocupacao),
      escolaridade: displayValue(education.nivel_escolaridade),
      saude: displayValue(health.cid_principal || health.tipo_deficiencia),
      beneficios: displayValue(benefits.beneficios_descricao),
      unidade: displayValue(unit?.nomeFantasia || unit?.razaoSocial),
      contatoUnidade: displayValue(unit?.telefone || unit?.email),
      enderecoUnidade: institutionAddress,
      generatedAt: formattedGeneratedAt,
      logo,
      socialName,
      statusRaw: value.status,
      codigoBarras: this.beneficiaryBarcode ?? undefined
    });

    const parser = new DOMParser();
    const parsed = parser.parseFromString(htmlContent, 'text/html');
    const container = document.createElement('div');
    container.style.position = 'absolute';
    container.style.left = '-9999px';
    container.innerHTML = parsed.body.innerHTML;
    document.body.appendChild(container);

    (window as any).html2canvas = html2canvas;
    const pdf = new jsPDF({ unit: 'pt', format: 'a4' });
    const pageWidth = pdf.internal.pageSize.getWidth();
    const usableWidth = pageWidth - 40;
    const contentWidth = container.getBoundingClientRect().width || usableWidth;
    const scale = Math.max(Math.min(usableWidth / contentWidth, 1.25), 0.6);

    await pdf.html(container, {
      x: 20,
      y: 20,
      html2canvas: { scale },
      callback: (docInstance) => {
        docInstance.output('dataurlnewwindow');
        document.body.removeChild(container);
      }
    });
  }

  private buildIndividualRecordHtml(data: {
    beneficiaryName: string;
    photoUrl: string;
    statusLabel: string;
    statusRaw?: string;
    codigo: string;
    cpf: string;
    inclusionDate: string | null;
    category: string;
    program: string;
    birthDate: string | null;
    age: number | null;
    rg: string;
    rgUf: string;
    sexo: string;
    mae: string;
    pai: string;
    estadoCivil: string;
    naturalidade: string | null;
    endereco: string;
    referencia: string;
    zona: string;
    telefone: string;
    email: string;
    rendaFamiliar: string;
    rendaPerCapita: string;
    moradores: string;
    trabalho: string;
    escolaridade: string;
    saude: string;
    beneficios: string;
    unidade: string;
    contatoUnidade: string;
    enderecoUnidade: string;
    generatedAt: string;
    logo?: string | null;
    socialName: string;
    codigoBarras?: string;
  }): string {
    const statusColor = data.statusRaw === 'BLOQUEADO' ? '#fca5a5' : '#bbf7d0';
    const statusTextColor = data.statusRaw === 'BLOQUEADO' ? '#991b1b' : '#166534';

    return `
      <style>
        @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap');
        * { box-sizing: border-box; }
        body { font-family: 'Inter', sans-serif; background: #f8fafc; color: #0f172a; }
        .pdf-root { max-width: 900px; margin: 0 auto; padding: 16px; }
        .header { display: flex; justify-content: space-between; gap: 12px; align-items: center; border-bottom: 3px solid #0284c7; padding-bottom: 12px; margin-bottom: 16px; }
        .header__logo { width: 64px; height: 64px; border: 1px solid #e2e8f0; border-radius: 12px; display: flex; align-items: center; justify-content: center; background: #f1f5f9; overflow: hidden; }
        .header__logo img { max-width: 100%; max-height: 100%; object-fit: contain; }
        .section { background: #fff; border: 1px solid #e2e8f0; border-radius: 14px; padding: 14px; margin-bottom: 12px; }
        .section__title { margin: 0 0 8px; font-size: 1rem; color: #0f172a; font-weight: 700; }
        .grid-2 { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 12px; }
        .grid-3 { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 12px; }
        .grid-4 { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); gap: 12px; }
        .data-label { font-size: 0.7rem; text-transform: uppercase; letter-spacing: 0.05em; color: #64748b; margin-bottom: 4px; font-weight: 700; }
        .data-value { font-size: 0.95rem; color: #0f172a; border-bottom: 1px dashed #e2e8f0; padding-bottom: 4px; min-height: 20px; }
        .identity { display: grid; grid-template-columns: 160px 1fr; gap: 16px; align-items: start; }
        .photo { width: 160px; height: 180px; border-radius: 12px; overflow: hidden; border: 1px solid #e2e8f0; }
        .photo img { width: 100%; height: 100%; object-fit: cover; }
        .status-badge { display: inline-flex; align-items: center; gap: 6px; padding: 6px 10px; border-radius: 9999px; font-size: 0.75rem; font-weight: 700; background: ${statusColor}; color: ${statusTextColor}; }
        .barcode { border: 1px dashed #cbd5e1; padding: 8px; display: inline-flex; margin-top: 6px; border-radius: 8px; background: #f8fafc; }
        .barcode img { max-height: 64px; }
      </style>
      <div class="pdf-root">
        <div class="header">
          <div style="display:flex; align-items:center; gap:10px;">
            <div class="header__logo">
              ${data.logo ? `<img src="${data.logo}" alt="Logomarca da unidade" />` : '<span style="font-weight:700;color:#0284c7">G3</span>'}
            </div>
            <div>
              <p style="margin:0;font-size:0.9rem;color:#334155;">${data.socialName}</p>
              <h2 style="margin:2px 0 0;font-size:1.3rem;color:#0f172a;">Ficha individual</h2>
            </div>
          </div>
          <div style="text-align:right;">
            <p style="margin:0;font-size:0.9rem;color:#0f172a;font-weight:700;">${data.beneficiaryName}</p>
            <p style="margin:2px 0 0;font-size:0.8rem;color:#64748b;">Gerado em ${data.generatedAt}</p>
          </div>
        </div>

        <div class="section">
          <div class="identity">
            <div>
              <div class="photo"><img src="${data.photoUrl}" alt="Foto do beneficiário" /></div>
              <div class="status-badge">${data.statusLabel}</div>
            </div>
            <div>
              <h3 style="margin:0 0 8px;font-size:1.1rem;color:#0f172a;">Identificação</h3>
              <div class="grid-3">
                <div>
                  <div class="data-label">Código</div>
                  <div class="data-value">${data.codigo}</div>
                  ${data.codigoBarras ? `<div class="barcode"><img src="${data.codigoBarras}" alt="Código de barras" /></div>` : ''}
                </div>
                <div>
                  <div class="data-label">CPF</div>
                  <div class="data-value">${data.cpf}</div>
                </div>
                <div>
                  <div class="data-label">Inclusão</div>
                  <div class="data-value">${data.inclusionDate ?? '---'}</div>
                </div>
                <div>
                  <div class="data-label">Categoria</div>
                  <div class="data-value">${data.category}</div>
                </div>
                <div>
                  <div class="data-label">Programa</div>
                  <div class="data-value">${data.program}</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="section">
          <p class="section__title">Dados pessoais</p>
          <div class="grid-3">
            <div>
              <div class="data-label">RG / Órgão emissor</div>
              <div class="data-value">${data.rg}</div>
            </div>
            <div>
              <div class="data-label">UF emissor</div>
              <div class="data-value">${data.rgUf}</div>
            </div>
            <div>
              <div class="data-label">Data de nascimento</div>
              <div class="data-value">${data.birthDate ?? '---'}${data.age !== null ? ` (${data.age} anos)` : ''}</div>
            </div>
            <div>
              <div class="data-label">Sexo</div>
              <div class="data-value">${data.sexo}</div>
            </div>
            <div>
              <div class="data-label">Nome da mãe</div>
              <div class="data-value">${data.mae}</div>
            </div>
            <div>
              <div class="data-label">Nome do pai</div>
              <div class="data-value">${data.pai}</div>
            </div>
            <div>
              <div class="data-label">Estado civil</div>
              <div class="data-value">${data.estadoCivil}</div>
            </div>
            <div>
              <div class="data-label">Naturalidade</div>
              <div class="data-value">${data.naturalidade ?? '---'}</div>
            </div>
          </div>
        </div>

        <div class="section">
          <p class="section__title">Endereço e contato</p>
          <div class="grid-2">
            <div>
              <div class="data-label">Endereço</div>
              <div class="data-value">${data.endereco}</div>
            </div>
            <div>
              <div class="data-label">Ponto de referência</div>
              <div class="data-value">${data.referencia}</div>
            </div>
            <div>
              <div class="data-label">Zona</div>
              <div class="data-value">${data.zona}</div>
            </div>
            <div>
              <div class="data-label">Telefone</div>
              <div class="data-value">${data.telefone}</div>
            </div>
            <div>
              <div class="data-label">E-mail</div>
              <div class="data-value">${data.email}</div>
            </div>
          </div>
        </div>

        <div class="section">
          <p class="section__title">Dados socioeconômicos</p>
          <div class="grid-4">
            <div>
              <div class="data-label">Renda familiar</div>
              <div class="data-value">${data.rendaFamiliar}</div>
            </div>
            <div>
              <div class="data-label">Renda per capita</div>
              <div class="data-value">${data.rendaPerCapita}</div>
            </div>
            <div>
              <div class="data-label">Pessoas na residência</div>
              <div class="data-value">${data.moradores}</div>
            </div>
            <div>
              <div class="data-label">Trabalho</div>
              <div class="data-value">${data.trabalho}</div>
            </div>
            <div>
              <div class="data-label">Escolaridade</div>
              <div class="data-value">${data.escolaridade}</div>
            </div>
            <div>
              <div class="data-label">Condição de saúde</div>
              <div class="data-value">${data.saude}</div>
            </div>
            <div style="grid-column: span 2;">
              <div class="data-label">Benefícios recebidos</div>
              <div class="data-value">${data.beneficios}</div>
            </div>
          </div>
        </div>

        <div class="section">
          <p class="section__title">Unidade de atendimento</p>
          <div class="grid-2">
            <div>
              <div class="data-label">Nome</div>
              <div class="data-value">${data.unidade}</div>
            </div>
            <div>
              <div class="data-label">Contato</div>
              <div class="data-value">${data.contatoUnidade}</div>
            </div>
            <div style="grid-column: span 2;">
              <div class="data-label">Endereço</div>
              <div class="data-value">${data.enderecoUnidade}</div>
            </div>
          </div>
        </div>
      </div>
    `;
  }

  printBeneficiaryList(): void {
    if (!this.filteredBeneficiarios.length) {
      this.feedback = 'Nenhum beneficiário encontrado para imprimir.';
      return;
    }

    const sorted = [...this.filteredBeneficiarios].sort((a, b) => this.sortBeneficiariesForPrint(a, b));
        const unit = this.assistanceUnit;
        const logo = unit?.logomarcaRelatorio || unit?.logomarca;
        const socialName = unit?.razaoSocial || unit?.nomeFantasia || 'Instituição';
        const orderLabel = this.printOrderOptions.find((option) => option.value === this.printOrderBy)?.label ?? 'Nome';
        const today = this.formatDate(new Date().toISOString());

        const rows = sorted
          .map((beneficiario) => {
            return `
              <tr>
                <td>${beneficiario.nome_completo || beneficiario.nome_social || '---'}</td>
                <td>${beneficiario.codigo || '---'}</td>
                <td>${this.formatDate(beneficiario.data_nascimento)}</td>
                <td>${this.formatAge(beneficiario.data_nascimento)}</td>
                <td>${beneficiario.cpf || beneficiario.nis || '---'}</td>
                <td>${this.formatStatusLabel(beneficiario.status)}</td>
                <td>${beneficiario.bairro || '---'}</td>
          </tr>
        `;
      })
      .join('');

    const header = `
      <header class="report-header report-header--center">
        ${logo ? `<img class="report-header__logo" src="${logo}" alt="Logomarca da unidade" />` : ''}
        <div class="report-header__identity">
          <p class="report-header__name">${socialName}</p>
          ${unit?.nomeFantasia && unit?.nomeFantasia !== unit?.razaoSocial ? `<p class="report-header__fantasy">${unit.nomeFantasia}</p>` : ''}
        </div>
      </header>
    `;

    const documentWindow = window.open('', '_blank', 'width=1000,height=900');
    if (!documentWindow) return;

    documentWindow.document.write(`
      <html>
        <head>
          <title>Relação de beneficiários</title>
          ${this.printStyles()}
        </head>
        <body>
          ${header}
          <h1>Relação de beneficiários</h1>
          <p class="muted">Dados gerados em ${today} | Ordenado por ${orderLabel.toLowerCase()}</p>
          <table class="print-table">
            <thead>
              <tr>
                <th>Nome completo</th>
                <th>Código</th>
                <th>Data de nascimento</th>
                <th>Idade</th>
                <th>CPF/NIS</th>
                <th>Status</th>
                <th>Bairro</th>
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

  private buildConsentHtml(): string {
    const value = this.form.value as any;
    const personal = value.dadosPessoais ?? {};
    const documents = value.documentos ?? {};
    const contact = value.contato ?? {};
    const address = value.endereco ?? {};
    const unit = this.assistanceUnit;

    const today = this.formatDate(new Date().toISOString());
    const beneficiaryName = personal.nome_completo || 'Beneficiário';
    const logo = unit?.logomarcaRelatorio || unit?.logomarca;
    const socialName = unit?.razaoSocial || unit?.nomeFantasia || 'Instituição';
    const footer = unit
      ? `
        <footer class="report-footer">
          <p>${socialName}</p>
          <p>CNPJ: ${unit.cnpj || '---'} | ${this.formatInstitutionAddress(unit)}</p>
          <p>Contato: ${unit.telefone || '---'}${unit.email ? ' | ' + unit.email : ''}</p>
        </footer>
      `
      : '';
    const header = `
      <header class="report-header">
        ${logo ? `<img class="report-header__logo" src="${logo}" alt="Logomarca da unidade" />` : ''}
        <div class="report-header__identity">
          <p class="report-header__name">${socialName}</p>
          ${unit?.nomeFantasia && unit?.nomeFantasia !== unit?.razaoSocial ? `<p class="report-header__fantasy">${unit.nomeFantasia}</p>` : ''}
        </div>
      </header>
    `;

    return `
      <html>
        <head>
          <title>Termo de autorização e consentimento</title>
          ${this.printStyles()}
        </head>
        <body>
          ${header}
          <h1>Termo de Autorização</h1>
          <p class="muted">Emitido em ${today}</p>
          <section class="report-section">
            <h2>1. Identificação do(a) beneficiário(a)</h2>
            <p><strong>Nome:</strong> ${beneficiaryName}</p>
            <p><strong>CPF:</strong> ${documents.cpf || '---'} | <strong>RG:</strong> ${documents.rg_numero || '---'} | <strong>NIS:</strong> ${documents.nis || '---'}</p>
            <p><strong>Nascimento:</strong> ${this.formatDate(personal.data_nascimento)} | <strong>Nome da mãe:</strong> ${personal.nome_mae || '---'}</p>
            <p><strong>Endereço:</strong> ${this.formatAddress(address)}</p>
            <p><strong>Contato:</strong> ${contact.telefone_principal || '---'}${contact.email ? ' | ' + contact.email : ''}</p>
          </section>
          <section class="report-section">
            <h2>2. Objeto</h2>
            <p>Autorizo a coleta, o uso, o armazenamento e o compartilhamento controlado de meus dados pessoais e sensíveis pela instituição acima identificada para fins de atendimento socioassistencial, registros administrativos, cumprimento de obrigações legais e prestação de contas.</p>
          </section>
          <section class="report-section">
            <h2>3. Termos e condições</h2>
            <p>O tratamento observará a Lei nº 13.709/2018 (LGPD), com base em finalidade, necessidade e transparência. As informações poderão ser utilizadas para identificação, segurança, comprovação de atendimento e registros institucionais, vedado o uso comercial.</p>
            <p>O compartilhamento ocorrerá apenas quando necessário à execução de políticas públicas, convênios ou exigências legais, assegurando sigilo e segurança da informação.</p>
            <p>Sei que posso solicitar acesso, correção ou eliminação dos dados, bem como revogar esta autorização, exceto quando houver fundamento legal para sua manutenção.</p>
          </section>
          <section class="report-section">
            <h2>4. Declaração</h2>
            <p>Declaro ciência sobre a guarda segura dos dados, prazos de retenção e canais para exercício de direitos, assumindo a veracidade das informações prestadas. Autorizo a utilização da minha assinatura em registros físicos ou digitais necessários aos serviços prestados.</p>
          </section>
          <section class="signature">
            <p>${beneficiaryName}</p>
            <p class="muted">${today}</p>
            <p class="muted">Assinatura do beneficiário ou responsável legal</p>
          </section>
          ${footer}
        </body>
      </html>
    `;
  }

  private printStyles(): string {
    return `
      <style>
        * { box-sizing: border-box; }
        body { font-family: 'Inter', 'Segoe UI', system-ui, -apple-system, sans-serif; padding: 28px; line-height: 1.6; color: #0f172a; background: #f8fafc; }
        h1 { margin: 0; }
        h2 { margin: 0; }
        p { margin: 0; }
        .muted { color: #475569; font-size: 12px; }
        .report-wrapper { max-width: 1024px; margin: 0 auto; background: #ffffff; padding: 32px; border-radius: 16px; box-shadow: 0 12px 30px rgba(15, 23, 42, 0.08); }
        .report-header { display: flex; justify-content: space-between; align-items: center; gap: 16px; border-bottom: 1px solid #e2e8f0; padding-bottom: 16px; margin-bottom: 20px; }
        .report-brand { display: flex; align-items: center; gap: 12px; }
        .report-brand__logo { width: 64px; height: 64px; object-fit: contain; border-radius: 8px; border: 1px solid #e2e8f0; padding: 6px; }
        .report-brand__name { font-weight: 700; font-size: 18px; color: #0f172a; }
        .report-brand__fantasy { font-size: 14px; color: #1f2937; }
        .report-brand__cnpj { font-size: 12px; color: #475569; }
        .report-meta { text-align: center; margin-bottom: 16px; }
        .report-title { font-size: 26px; font-weight: 800; letter-spacing: 0.5px; text-transform: uppercase; color: #0f172a; }
        .profile { display: flex; justify-content: space-between; align-items: center; background: linear-gradient(135deg, #f8fafc, #eef2ff); padding: 16px; border-radius: 12px; margin-bottom: 16px; border: 1px solid #e2e8f0; }
        .profile__name { font-size: 20px; font-weight: 700; margin: 0 0 4px; }
        .profile__status { font-size: 13px; color: #1e3a8a; margin: 0; text-transform: uppercase; letter-spacing: 0.3px; }
        .profile__photo { width: 96px; height: 96px; border-radius: 12px; overflow: hidden; border: 1px solid #e2e8f0; background: #fff; box-shadow: 0 8px 20px rgba(15, 23, 42, 0.08); }
        .profile__photo img { width: 100%; height: 100%; object-fit: cover; }
        .info-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 12px; }
        .info-card { background: #ffffff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 12px; box-shadow: 0 6px 16px rgba(15, 23, 42, 0.04); }
        .info-card__title { font-weight: 700; color: #0f172a; margin-bottom: 10px; font-size: 15px; letter-spacing: 0.2px; }
        .info-card__content { display: grid; gap: 8px; }
        .info-row { padding: 8px 10px; border-radius: 10px; background: #f8fafc; border: 1px solid #e2e8f0; }
        .info-label { font-size: 12px; color: #475569; margin: 0 0 2px; text-transform: uppercase; letter-spacing: 0.3px; }
        .info-value { font-size: 14px; color: #0f172a; margin: 0; word-break: break-word; }
        .report-footer { border-top: 1px solid #e2e8f0; margin-top: 20px; padding-top: 12px; text-align: center; color: #475569; font-size: 13px; }
        .report-footer__name { font-weight: 700; color: #0f172a; margin-bottom: 4px; }
        .print-table { width: 100%; border-collapse: collapse; margin-top: 12px; font-size: 12px; }
        .print-table th, .print-table td { border: 1px solid #e2e8f0; padding: 8px; text-align: left; }
        .signature { margin-top: 36px; text-align: center; }
        .signature p { text-align: center; }
        .report-section { margin-top: 10px; }
        .report-header--center { justify-content: center; }
        .report-header__logo { max-height: 64px; object-fit: contain; }
        .report-header__identity { text-align: center; }
        .report-header__name { font-weight: bold; font-size: 14pt; margin: 0; }
        .report-header__fantasy { margin: 0; font-size: 12pt; color: #1f2937; }
        .print-table th:nth-child(3), .print-table td:nth-child(3) { text-align: center; }
        .print-table th:nth-child(2), .print-table th:nth-child(4) { width: 18%; }
      </style>
    `;
  }

  private formatInstitutionAddress(unit: AssistanceUnitPayload): string {
    const parts = [unit.endereco, unit.numeroEndereco, unit.bairro, unit.cidade, unit.estado, unit.cep]
      .filter(Boolean)
      .join(', ');
    return parts || '---';
  }

  onPrintOrderChange(order: PrintOrder): void {
    this.printOrderBy = order;
  }

  private formatAddress(address: any): string {
    const parts = [address.logradouro, address.numero, address.bairro, address.municipio, address.uf, address.cep]
      .filter(Boolean)
      .join(', ');
    return parts || '---';
  }

  private joinParts(parts: (string | number | null | undefined)[], separator = ', '): string | null {
    const filtered = parts
      .filter((part) => this.hasValue(part))
      .map((part) => part?.toString().trim() ?? '')
      .filter((part) => part && part !== '---');
    if (!filtered.length) return null;
    return filtered.join(separator);
  }

  private hasValue(value: any): boolean {
    if (value === null || value === undefined) return false;
    if (typeof value === 'string') return value.trim().length > 0;
    if (Array.isArray(value)) return value.length > 0;
    if (typeof value === 'boolean') return value === true;
    return true;
  }

  private formatCurrencyValue(value: any): string | null {
    if (value === null || value === undefined || value === '') return null;
    const numeric = Number(value);
    if (Number.isNaN(numeric)) return null;
    return numeric.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
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

  private formatAge(value?: string | null): string {
    const age = this.getAgeFromDate(value ?? null);
    if (age === null) return '---';
    return `${age} anos`;
  }

  private getAgeFromDate(dateValue: string | null): number | null {
    if (!dateValue) return null;

    const birthDate = new Date(dateValue);
    if (isNaN(birthDate.getTime())) return null;

    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    const dayDiff = today.getDate() - birthDate.getDate();

    if (monthDiff < 0 || (monthDiff === 0 && dayDiff < 0)) {
      age--;
    }

    return Math.max(age, 0);
  }

  private normalizeSortString(value?: string | null): string {
    return (value ?? '').toString().toLowerCase().trim();
  }

  private sortBeneficiariesForPrint(a: BeneficiarioApiPayload, b: BeneficiarioApiPayload): number {
    switch (this.printOrderBy) {
      case 'bairro':
        return this.normalizeSortString(a.bairro).localeCompare(this.normalizeSortString(b.bairro));
      case 'data_nascimento': {
        const aTime = new Date(a.data_nascimento ?? '').getTime();
        const bTime = new Date(b.data_nascimento ?? '').getTime();
        return (isNaN(aTime) ? Infinity : aTime) - (isNaN(bTime) ? Infinity : bTime);
      }
      case 'idade': {
        const ageA = this.getAgeFromDate(a.data_nascimento);
        const ageB = this.getAgeFromDate(b.data_nascimento);
        return (ageB ?? -1) - (ageA ?? -1);
      }
      case 'nome':
      default:
        return this.normalizeSortString(a.nome_completo || a.nome_social).localeCompare(
          this.normalizeSortString(b.nome_completo || b.nome_social)
        );
    }
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

  async submit(skipValidation = false, statusOnly = false) {
    const statusForSave = this.determineStatusForSave(skipValidation || statusOnly);

    if (!skipValidation && this.form.invalid) {
      this.form.get('status')?.setValue(statusForSave);
      this.feedback = 'Preencha os campos obrigatórios.';
      return;
    }
    if (!skipValidation && !this.validateRequiredDocuments()) {
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
      const payload = await this.toPayload(statusForSave, statusOnly);
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

  private async toPayload(
    statusForSave: BeneficiarioApiPayload['status'],
    statusOnly = false
  ): Promise<BeneficiarioApiPayload> {
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
      documentosObrigatorios,
      statusOnly
    };
  }

  private async buildDocumentPayload(): Promise<DocumentoObrigatorio[]> {
    const documents = this.anexos.controls.map((control) => control.value as DocumentoObrigatorio & { file?: File | null });

    return documents.map((doc) => {
      const file = doc.file as File | undefined;
      return {
        nome: doc.nome,
        obrigatorio: doc.obrigatorio,
        nomeArquivo: file ? file.name : doc.nomeArquivo,
        conteudo: doc.conteudo,
        contentType: doc.contentType
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

  private setupEducationControls(): void {
    const literacyControl = this.form.get(['escolaridade', 'sabe_ler_escrever']);
    const levelControl = this.form.get(['escolaridade', 'nivel_escolaridade']);

    const setNoEducation = () => {
      levelControl?.setValue('Sem escolaridade formal', { emitEvent: false });
      levelControl?.disable({ emitEvent: false });
    };

    if (!literacyControl?.value) {
      setNoEducation();
    }

    literacyControl?.valueChanges.pipe(takeUntil(this.destroy$)).subscribe((canRead) => {
      if (canRead) {
        levelControl?.enable({ emitEvent: false });
        levelControl?.markAsUntouched();
        return;
      }

      setNoEducation();
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
    this.beneficiaryAge = this.getAgeFromDate(dateValue);
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
    const { nome, cpf, data_nascimento, codigo } = this.searchForm.value;
    this.listLoading = true;
    this.listError = null;

    this.service
      .list({
        nome: nome || undefined,
        cpf: cpf || undefined,
        data_nascimento: data_nascimento || undefined,
        codigo: codigo || undefined
      })
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
      this.applyLoadedDocuments(this.getDocumentList(details));
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
          this.beneficiaryCode = null;
          this.beneficiaryBarcode = null;
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
