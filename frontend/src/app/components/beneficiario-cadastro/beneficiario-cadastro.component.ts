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
  listLoading = false;
  listError: string | null = null;
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
  lastStatus: BeneficiarioApiPayload['status'] | null = 'ATIVO';
  previousStatusBeforeBlock: BeneficiarioApiPayload['status'] | null = 'ATIVO';
  photoPreview: string | null = null;
  cameraActive = false;
  captureError: string | null = null;
  cepLookupError: string | null = null;
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
    private readonly http: HttpClient
  ) {
    this.searchForm = this.fb.group({
      nome: [''],
      cpf: [''],
      nis: [''],
      status: ['']
    });
    this.form = this.fb.group({
      status: ['ATIVO', Validators.required],
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
        internet: [false]
      }),
      contato: this.fb.group({
        telefone_principal: [''],
        telefone_principal_whatsapp: [false],
        telefone_secundario: [''],
        telefone_recado_nome: [''],
        telefone_recado_numero: [''],
        email: [''],
        permite_contato_tel: [true],
        permite_contato_whatsapp: [true],
        permite_contato_sms: [false],
        permite_contato_email: [false],
        horario_preferencial_contato: ['']
      }),
      documentos: this.fb.group({
        cpf: [''],
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
        aceite_lgpd: [false],
        data_aceite_lgpd: [''],
        observacoes: ['']
      })
    });
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

  ngOnInit(): void {
    this.loadRequiredDocuments();
    this.watchBirthDate();
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
          this.lastStatus = beneficiario.status ?? 'ATIVO';
          this.previousStatusBeforeBlock = this.lastStatus;
          this.applyLoadedDocuments(beneficiario.documentosObrigatorios);
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
    return {
      status: beneficiario.status ?? 'ATIVO',
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
        zona: beneficiario.zona,
        situacao_imovel: beneficiario.situacao_imovel,
        tipo_moradia: beneficiario.tipo_moradia,
        agua_encanada: beneficiario.agua_encanada,
        esgoto_tipo: beneficiario.esgoto_tipo,
        coleta_lixo: beneficiario.coleta_lixo,
        energia_eletrica: beneficiario.energia_eletrica,
        internet: beneficiario.internet
      },
      contato: {
        telefone_principal: beneficiario.telefone_principal,
        telefone_principal_whatsapp: beneficiario.telefone_principal_whatsapp,
        telefone_secundario: beneficiario.telefone_secundario,
        telefone_recado_nome: beneficiario.telefone_recado_nome,
        telefone_recado_numero: beneficiario.telefone_recado_numero,
        email: beneficiario.email,
        permite_contato_tel: beneficiario.permite_contato_tel,
        permite_contato_whatsapp: beneficiario.permite_contato_whatsapp,
        permite_contato_sms: beneficiario.permite_contato_sms,
        permite_contato_email: beneficiario.permite_contato_email,
        horario_preferencial_contato: beneficiario.horario_preferencial_contato
      },
      documentos: {
        cpf: beneficiario.cpf,
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

  private validateRequiredDocuments(): boolean {
    const missing = this.anexos.controls
      .filter((control) => control.get('obrigatorio')?.value && !control.get('nomeArquivo')?.value)
      .map((control) => control.get('nome')?.value as string);

    if (missing.length) {
      this.feedback = `Envie os documentos obrigatórios: ${missing.join(', ')}`;
      this.changeTab('documentos');
      return false;
    }

    return true;
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
    if (input.checked) {
      this.printLgpdTerms();
    }
  }

  private printLgpdTerms(): void {
    const documentWindow = window.open('', '_blank', 'width=800,height=900');
    if (!documentWindow) return;

    documentWindow.document.write(`
      <html>
        <head>
          <title>Termo de autorização</title>
          <style>
            body { font-family: Arial, sans-serif; padding: 24px; line-height: 1.6; }
            h1 { color: #0f766e; }
            h2 { margin-top: 24px; color: #0ea5e9; }
            p { margin: 12px 0; }
            ul { margin: 12px 0 24px; padding-left: 20px; }
          </style>
        </head>
        <body>
          <h1>Termo de autorização e consentimento</h1>
          <h2>Autorização de imagem</h2>
          <p>Autorizo a instituição a coletar, armazenar e utilizar imagem para fins institucionais e de registro de atendimento.</p>
          <h2>Tratamento de dados pessoais (LGPD)</h2>
          <ul>
            <li>Confirmo o conhecimento sobre a finalidade do tratamento de dados.</li>
            <li>Sei que posso solicitar revisão, correção ou exclusão dos meus dados pessoais.</li>
            <li>Autorizo o compartilhamento estritamente necessário com órgãos públicos ou parceiros ligados às políticas sociais.</li>
          </ul>
          <h2>Autorização de envio de dados</h2>
          <p>Autorizo o envio das informações para a instituição e sistemas integrados (e-SUS, OSCIP e assistência social) para fins de acompanhamento.</p>
          <p>__________________________________________</p>
          <p>Assinatura do beneficiário ou responsável</p>
        </body>
      </html>
    `);

    documentWindow.document.close();
    documentWindow.focus();
    documentWindow.print();
  }

  changeTab(tab: string) {
    this.activeTab = tab;
  }

  async submit() {
    if (this.form.invalid) {
      this.feedback = 'Preencha os campos obrigatórios.';
      return;
    }
    if (!this.validateRequiredDocuments()) {
      return;
    }
    if (this.form.get('status')?.value === 'BLOQUEADO' && !this.form.get('motivo_bloqueio')?.value) {
      this.feedback = 'Informe o motivo do bloqueio antes de salvar.';
      this.blockReasonModalOpen = true;
      return;
    }
    this.saving = true;
    const payload = await this.toPayload();
    const isDuplicate = await this.hasDuplicate(payload);
    if (isDuplicate) {
      this.saving = false;
      return;
    }
    const request = this.beneficiarioId
      ? this.service.update(this.beneficiarioId, payload)
      : this.service.create(payload);

    request.subscribe({
      next: () => {
        this.feedback = 'Registro salvo com sucesso';
        this.saving = false;
        this.router.navigate(['/cadastros/beneficiarios']);
      },
      error: (error) => {
        this.feedback = error?.error?.message || 'Erro ao salvar beneficiário';
        this.saving = false;
      }
    });
  }

  private async toPayload(): Promise<BeneficiarioApiPayload> {
    const value = this.form.value;
    const documentosObrigatorios = await this.buildDocumentPayload();
    const endereco = {
      ...(value.endereco as any),
      cep: this.normalizeCep(value.endereco?.cep as string)
    };

    return {
      status: value.status,
      motivo_bloqueio: value.motivo_bloqueio,
      foto_3x4: value.foto_3x4,
      ...(value.dadosPessoais as any),
      ...(endereco as any),
      ...(value.contato as any),
      ...(value.documentos as any),
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
    this.lastStatus = control?.value ?? 'ATIVO';

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
    this.form.get('status')?.setValue(this.previousStatusBeforeBlock ?? 'ATIVO');
    this.form.get('motivo_bloqueio')?.setValue('');
  }

  searchBeneficiaries(): void {
    const { nome, cpf, nis } = this.searchForm.value;
    this.listLoading = true;
    this.listError = null;

    this.service
      .list({ nome: nome || undefined, cpf: cpf || undefined, nis: nis || undefined })
      .subscribe({
        next: ({ beneficiarios }) => {
          this.beneficiarios = beneficiarios ?? [];
          this.applyListFilters();
          this.listLoading = false;
        },
        error: () => {
          this.listError = 'Não foi possível carregar os beneficiários. Tente novamente.';
          this.listLoading = false;
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
      this.lastStatus = details.status ?? 'ATIVO';
      this.previousStatusBeforeBlock = this.lastStatus;
      this.applyLoadedDocuments(details.documentosObrigatorios);
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
          this.form.reset({ status: 'ATIVO', motivo_bloqueio: '', foto_3x4: '' });
          this.beneficiarioId = null;
          this.photoPreview = null;
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
      this.videoStream = await navigator.mediaDevices.getUserMedia({ video: true });
      const video = this.videoElement?.nativeElement;
      if (video) {
        video.srcObject = this.videoStream;
        await video.play();
        this.cameraActive = true;
      }
    } catch (error) {
      console.error('Erro ao iniciar câmera', error);
      this.captureError = 'Não foi possível acessar a câmera.';
    }
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
}
