import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, NgZone, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { Subject, finalize, takeUntil } from 'rxjs';
import { VolunteerPayload, VolunteerService } from '../../services/volunteer.service';
import { ProfessionalRecord, ProfessionalService } from '../../services/professional.service';
import { AssistanceUnitPayload, AssistanceUnitService } from '../../services/assistance-unit.service';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { ConfigAcoesCrud, EstadoAcoesCrud, TelaBaseComponent } from '../compartilhado/tela-base.component';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';
import { PopupErrorBuilder } from '../../utils/popup-error.builder';
import { titleCaseWords } from '../../utils/capitalization.util';

interface StepTab {
  id: string;
  label: string;
}

type VolunteerRecord = VolunteerPayload & { id: string };

@Component({
  selector: 'app-voluntariado-cadastro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TelaPadraoComponent, PopupMessagesComponent],
  templateUrl: './voluntariado-cadastro.component.html',
  styleUrl: './voluntariado-cadastro.component.scss'
})
export class VoluntariadoCadastroComponent extends TelaBaseComponent implements OnInit, OnDestroy {
  form: FormGroup;
  activeTab = 'lista';
  saving = false;
  feedback: string | null = null;
  popupErros: string[] = [];
  private feedbackTimeout?: ReturnType<typeof setTimeout>;
  editingVolunteerId: string | null = null;
  photoPreview: string | null = null;
  cameraActive = false;
  captureError: string | null = null;
  cepLookupError: string | null = null;
  private videoStream?: MediaStream;
  profissionaisVoluntarios: ProfessionalRecord[] = [];
  profissionalResultados: ProfessionalRecord[] = [];
  profissionalBusca = '';
  carregandoProfissionais = false;
  voluntarioSelecionado: VolunteerRecord | null = null;
  volunteersFiltrados: VolunteerRecord[] = [];
  documentoIdentificacaoNome = '';
  comprovanteEnderecoNome = '';
  paginaAtual = 1;
  readonly tamanhoPagina = 10;
  private readonly capitalizationSubs: Array<() => void> = [];
  private readonly destroy$ = new Subject<void>();
  @ViewChild('videoElement') videoElement?: ElementRef<HTMLVideoElement>;
  @ViewChild('canvasElement') canvasElement?: ElementRef<HTMLCanvasElement>;

  tabs: StepTab[] = [
    { id: 'lista', label: 'Listagem de voluntarios' },
    { id: 'dados', label: 'Dados Pessoais' },
    { id: 'contato', label: 'Contato e Competencias' },
    { id: 'endereço', label: 'Endereco' },
    { id: 'disponibilidade', label: 'Disponibilidade' },
    { id: 'termos', label: 'Termos e Documentos' }
  ];

  volunteers: VolunteerRecord[] = [];
  listagemForm: FormGroup;
  readonly acoesToolbar: Required<ConfigAcoesCrud> = this.criarConfigAcoes({    
    salvar: true,
    excluir: true,
    novo: true,
    cancelar: true,
    imprimir: true,
    buscar: true
  });

  readonly diasSemana = ['Segunda-feira', 'Terca-feira', 'Quarta-feira', 'Quinta-feira', 'Sexta-feira', 'Sabado'];
  readonly periodos = ['Manha', 'Tarde', 'Noite'];
  readonly habilidadesSugestao = ['Atendimento direto', 'Captacao de recursos', 'Comunicacao', 'Administrativo', 'Tecnologia', 'Eventos'];
  readonly statusOptions = ['ATIVO', 'INATIVO', 'BLOQUEADO'];

  unidadePrincipal: AssistanceUnitPayload | null = null;

  constructor(
    private readonly fb: FormBuilder,
    private readonly volunteerService: VolunteerService,
    private readonly professionalService: ProfessionalService,
    private readonly assistanceUnitService: AssistanceUnitService,
    private readonly ngZone: NgZone,
    private readonly http: HttpClient
  ) {
    super();
    this.form = this.fb.group({
      profissionalId: [null],
      foto_3x4: [''],
      dadosPessoais: this.fb.group({
        status: ['ATIVO', Validators.required],
        nomeCompleto: ['', Validators.required],
        cpf: ['', [Validators.required, this.cpfValidator]],
        rg: [''],
        dataNascimento: [''],
        genero: [''],
        profissao: [''],
        motivacao: ['']
      }),
      endereço: this.fb.group({
        cep: ['', [this.cepValidator]],
        logradouro: [''],
        numero: [''],
        complemento: [''],
        bairro: [''],
        pontoReferencia: [''],
        municipio: [''],
        zona: [''],
        uf: ['']
      }),
      contato: this.fb.group({
        telefone: [''],
        email: ['', [Validators.required, Validators.email]],
        cidade: [''],
        estado: [''],
        areaInteresse: [''],
        habilidades: [''],
        idiomas: [''],
        linkedin: ['']
      }),
      disponibilidade: this.fb.group({
        dias: this.fb.control<string[]>([]),
        periodos: this.fb.control<string[]>([]),
        cargaHorariaSemanal: [''],
        presencial: [true],
        remoto: [false],
        inicioPrevisto: [''],
        observacoes: ['']
      }),
      termos: this.fb.group({
        documentoIdentificacao: [''],
        comprovanteEndereco: [''],
        aceiteVoluntariado: [false, Validators.requiredTrue],
        aceiteImagem: [false, Validators.requiredTrue],
        assinaturaDigital: ['']
      })
    });
    this.listagemForm = this.fb.group({
      nome: [''],
      cpf: [''],
      codigo: [''],
      dataNascimento: [''],
      status: ['']
    });
    this.setupCapitalizationRules();
  }

  ngOnInit(): void {
    this.loadVolunteers();
    this.loadProfissionaisVoluntarios();
    this.loadUnidadePrincipal();
  }

  ngOnDestroy(): void {
    this.capitalizationSubs.forEach((unsubscribe) => unsubscribe());
    this.destroy$.next();
    this.destroy$.complete();
    this.clearFeedbackTimeout();
    this.stopCamera();
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

  get voluntariosPaginados(): VolunteerRecord[] {
    const inicio = (this.paginaAtual - 1) * this.tamanhoPagina;
    return this.volunteersFiltrados.slice(inicio, inicio + this.tamanhoPagina);
  }

  get totalPaginas(): number {
    return Math.max(1, Math.ceil(this.volunteersFiltrados.length / this.tamanhoPagina));
  }

  get acoesDesabilitadas(): EstadoAcoesCrud {
    return {
      salvar: this.form?.invalid ?? false,
      excluir: !this.editingVolunteerId,
      novo: this.saving,
      cancelar: this.saving,
      imprimir: this.saving,
      buscar: this.saving
    };
  }

  fecharPopupErros(): void {
    this.popupErros = [];
  }

  changeTab(tab: string): void {
    this.activeTab = tab;
    if (tab === 'lista') {
      this.loadVolunteers();
    }
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

  paginaAnterior(): void {
    this.paginaAtual = Math.max(1, this.paginaAtual - 1);
  }

  proximaPagina(): void {
    this.paginaAtual = Math.min(this.totalPaginas, this.paginaAtual + 1);
  }

  toggleSelection(path: (string | number)[], option: string): void {
    const control = this.form.get(path);
    const current = new Set(control?.value ?? []);

    if (current.has(option)) {
      current.delete(option);
    } else {
      current.add(option);
    }

    control?.setValue(Array.from(current));
  }

  selectionChecked(path: (string | number)[], option: string): boolean {
    const control = this.form.get(path);
    return (control?.value as string[] | undefined)?.includes(option) ?? false;
  }

  openTermo(): void {
    const value = this.form.value;
    const termoWindow = window.open('', '_blank', 'width=900,height=1200');
    if (!termoWindow) return;

    termoWindow.document.write(this.buildTermoHtml(value, this.unidadePrincipal));
    termoWindow.document.close();
    termoWindow.focus();
    termoWindow.print();
  }

  submit(): void {
    this.popupErros = [];
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      const builder = new PopupErrorBuilder();
      const requiredFields: { path: string; label: string }[] = [
        { path: 'dadosPessoais.nomeCompleto', label: 'Nome completo' },
        { path: 'dadosPessoais.cpf', label: 'CPF' },
        { path: 'contato.email', label: 'E-mail' },
        { path: 'termos.aceiteVoluntariado', label: 'Aceite do termo de voluntariado' },
        { path: 'termos.aceiteImagem', label: 'Aceite de uso de imagem' }
      ];

      requiredFields.forEach(({ path, label }) => {
        const control = this.form.get(path);
        const value = control?.value;
        const hasValue = typeof value === 'boolean' ? value : String(value ?? '').trim();
        if (!hasValue) {
          builder.adicionar(`${label} e obrigatorio.`);
        }
      });
      this.popupErros = builder.build();
      this.setFeedback('Revise os campos obrigatorios para concluir o cadastro.');
      return;
    }

    this.clearFeedback();
    this.saving = true;
    const record = this.buildVolunteerRecord();

    if (this.editingVolunteerId) {
      this.volunteerService
        .update(this.editingVolunteerId, record)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => {
            this.setFeedback('Cadastro de voluntario atualizado com sucesso.');
            this.finalizarSubmit();
          },
          error: () => {
            this.setFeedback('NÃ£o foi possÃ­vel atualizar o voluntario.');
            this.saving = false;
          }
        });
      return;
    }

    this.volunteerService
      .create(record)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.setFeedback('Cadastro de voluntario salvo com sucesso.');
          this.finalizarSubmit();
        },
        error: () => {
          this.setFeedback('NÃ£o foi possÃ­vel salvar o voluntario.');
          this.saving = false;
        }
      });
  }

  private finalizarSubmit(): void {
    this.loadVolunteers();
    this.saving = false;
    this.editingVolunteerId = null;
    this.resetForm();
  }

  printCurrentVolunteer(): void {
    this.openTermo();
  }

  cancelEdit(): void {
    this.editingVolunteerId = null;
    this.resetForm();
  }

  deleteCurrentVolunteer(): void {
    if (!this.editingVolunteerId) return;

    const volunteer = this.volunteers.find((item) => item.id === this.editingVolunteerId);
    if (!volunteer) {
      this.startNewVolunteer();
      return;
    }

    if (!window.confirm(`Remover ${volunteer.nome} do cadastro?`)) return;

    this.volunteerService
      .delete(volunteer.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.loadVolunteers();
          this.startNewVolunteer();
        },
        error: () => {
          this.setFeedback('NÃ£o foi possÃ­vel remover o voluntario.');
        }
      });
  }

  startNewVolunteer(): void {
    this.editingVolunteerId = null;
    this.resetForm();
  }

  closeForm(): void {
    window.history.back();
  }

  editVolunteer(volunteer: VolunteerRecord): void {
    this.editingVolunteerId = volunteer.id;
    this.changeTab('dados');
    this.documentoIdentificacaoNome = volunteer.documentoIdentificacao ? 'Arquivo carregado' : '';
    this.comprovanteEnderecoNome = volunteer.comprovanteEndereco ? 'Arquivo carregado' : '';
    this.photoPreview = volunteer.foto_3x4 ?? null;
    this.form.patchValue({
      profissionalId: volunteer.profissionalId ?? null,
      foto_3x4: volunteer.foto_3x4 ?? '',
      dadosPessoais: {
        status: volunteer.status ?? 'ATIVO',
        nomeCompleto: volunteer.nome,
        cpf: this.formatCpf(volunteer.cpf),
        rg: volunteer.rg ?? '',
        dataNascimento: volunteer.dataNascimento ?? '',
        genero: volunteer.genero ?? '',
        profissao: volunteer.profissao ?? '',
        motivacao: volunteer.motivacao ?? ''
      },
      endereço: {
        cep: volunteer.cep ?? '',
        logradouro: volunteer.logradouro ?? '',
        numero: volunteer.numero ?? '',
        complemento: volunteer.complemento ?? '',
        bairro: volunteer.bairro ?? '',
        pontoReferencia: volunteer.pontoReferencia ?? '',
        municipio: volunteer.municipio ?? '',
        zona: volunteer.zona ?? '',
        uf: volunteer.uf ?? ''
      },
      contato: {
        telefone: volunteer.telefone ?? '',
        email: volunteer.email ?? '',
        cidade: volunteer.cidade ?? '',
        estado: volunteer.estado ?? '',
        areaInteresse: volunteer.areaInteresse ?? '',
        habilidades: volunteer.habilidades ?? '',
        idiomas: volunteer.idiomas ?? '',
        linkedin: volunteer.linkedin ?? ''
      },
      disponibilidade: {
        dias: volunteer.disponibilidadeDias ?? [],
        periodos: volunteer.disponibilidadePeriodos ?? [],
        cargaHorariaSemanal: volunteer.cargaHoraria ?? '',
        inicioPrevisto: volunteer.inicioPrevisto ?? '',
        presencial: volunteer.presencial ?? false,
        remoto: volunteer.remoto ?? false,
        observacoes: volunteer.observacoes ?? ''
      },
      termos: {
        documentoIdentificacao: volunteer.documentoIdentificacao ?? '',
        comprovanteEndereco: volunteer.comprovanteEndereco ?? '',
        aceiteVoluntariado: volunteer.aceiteVoluntariado ?? false,
        aceiteImagem: volunteer.aceiteImagem ?? false,
        assinaturaDigital: volunteer.assinaturaDigital ?? ''
      }
    });
  }

  deleteVolunteer(volunteer: VolunteerRecord): void {
    this.volunteerService
      .delete(volunteer.id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.loadVolunteers();
          if (this.editingVolunteerId === volunteer.id) {
            this.editingVolunteerId = null;
            this.resetForm();
          }
        },
        error: () => {
          this.setFeedback('NÃ£o foi possÃ­vel remover o voluntario.');
        }
      });
  }

  printVolunteer(volunteer: VolunteerRecord): void {
    const printWindow = window.open('', '_blank', 'width=900,height=1200');
    if (!printWindow) return;

    printWindow.document.write(this.buildVolunteerPrintHtml(volunteer));
    printWindow.document.close();
    printWindow.focus();
    printWindow.print();
  }

  printVolunteerList(): void {
    const printWindow = window.open('', '_blank', 'width=900,height=1200');
    if (!printWindow) return;

    const rows = this.volunteers
      .map(
        (volunteer, index) => `\n          <tr>\n            <td>${index + 1}</td>\n            <td>${volunteer.nome}</td>\n            <td>${volunteer.areaInteresse || '---'}</td>\n            <td>${volunteer.disponibilidadeDias?.join(', ') || '---'}</td>\n            <td>${volunteer.telefone || '---'}</td>\n          </tr>\n        `
      )
      .join('');

    printWindow.document.write(`\n      <html>\n        <head>\n          <title>Relacao de voluntarios</title>\n          <style>\n            ${this.abntStyles()}\n            table { width: 100%; border-collapse: collapse; margin-top: 16px; }\n            th, td { border: 1px solid #cbd5e1; padding: 8px; text-align: left; }\n            th { background: #f1f5f9; font-weight: 700; }\n          </style>\n        </head>\n        <body>\n          <header class="abnt-header">\n            <div>\n              <p class="abnt-organization">${this.unidadePrincipal?.razaoSocial || 'ADRA'}</p>\n              <p class="abnt-organization">CNPJ ${this.unidadePrincipal?.cnpj || '---'}</p>\n              <h1>RELAÇÃO DE VOLUNTÁRIOS</h1>\n            </div>\n            <p class="abnt-subtitle">Lista de voluntarios cadastrados para impressÃ£o.</p>\n          </header>\n          <table>\n            <thead>\n              <tr>\n                <th>#</th>\n                <th>Nome</th>\n                <th>Área</th>\n                <th>Disponibilidade</th>\n                <th>Contato</th>\n              </tr>\n            </thead>\n            <tbody>${rows}</tbody>\n          </table>\n        </body>\n      </html>\n    `);

    printWindow.document.close();
    printWindow.focus();
    printWindow.print();
  }

  abntStyles(): string {
    return [
      '@page { margin: 3cm 2cm 2cm 3cm; }',
      "body { font-family: 'Times New Roman', serif; font-size: 12pt; line-height: 1.5; color: #0f172a; }",
      'h1 { margin: 0 0 12px; font-size: 14pt; text-align: center; font-weight: 700; letter-spacing: 0.02em; }',
      'h2 { margin: 16px 0 8px; font-size: 12pt; text-transform: uppercase; }',
      'p, li { margin: 6px 0; }',
      'ul { margin: 6px 0 6px 18px; padding: 0; }',
      '.abnt-header { display: flex; align-items: flex-start; justify-content: space-between; gap: 12px; border-bottom: 1px solid #cbd5e1; padding-bottom: 8px; margin-bottom: 12px; }',
      '.abnt-organization { margin: 0; text-transform: uppercase; font-size: 10pt; letter-spacing: 0.04em; }',
      '.abnt-subtitle { margin: 0; font-size: 10pt; color: #475569; text-align: right; }',
      '.abnt-subtitle--center { text-align: center; width: 100%; }',
      '.abnt-section { margin-top: 8px; border: 1px solid #e2e8f0; padding: 10px 12px; border-radius: 8px; }',
      '.signature { margin-top: 24px; padding-top: 12px; border-top: 1px solid #cbd5e1; display: grid; gap: 10px; }',
      '.abnt-footer { margin-top: 18px; padding-top: 10px; border-top: 1px solid #cbd5e1; color: #475569; font-size: 10pt; text-align: center; }'
    ].join('\n');
  }

  private buildVolunteerRecord(): VolunteerRecord {
    const value = this.form.value;
    return {
      id: this.editingVolunteerId ?? '',
      foto_3x4: value.foto_3x4 ?? '',
      status: value.dadosPessoais?.status || 'ATIVO',
      profissionalId: value.profissionalId ?? null,
      nome: value.dadosPessoais?.nomeCompleto || 'Voluntario sem nome',
      cpf: this.normalizeCpf(value.dadosPessoais?.cpf || ''),
      rg: value.dadosPessoais?.rg,
      dataNascimento: value.dadosPessoais?.dataNascimento,
      genero: value.dadosPessoais?.genero,
      profissao: value.dadosPessoais?.profissao,
      motivacao: value.dadosPessoais?.motivacao,
      cep: value.endereço?.cep,
      logradouro: value.endereço?.logradouro,
      numero: value.endereço?.numero,
      complemento: value.endereço?.complemento,
      bairro: value.endereço?.bairro,
      pontoReferencia: value.endereço?.pontoReferencia,
      municipio: value.endereço?.municipio,
      zona: value.endereço?.zona,
      uf: value.endereço?.uf,
      email: value.contato?.email || '---',
      telefone: value.contato?.telefone,
      cidade: value.contato?.cidade,
      estado: value.contato?.estado,
      areaInteresse: value.contato?.areaInteresse,
      habilidades: value.contato?.habilidades,
      idiomas: value.contato?.idiomas,
      linkedin: value.contato?.linkedin,
      disponibilidadeDias: value.disponibilidade?.dias,
      disponibilidadePeriodos: value.disponibilidade?.periodos,
      cargaHoraria: value.disponibilidade?.cargaHorariaSemanal,
      presencial: value.disponibilidade?.presencial,
      remoto: value.disponibilidade?.remoto,
      inicioPrevisto: value.disponibilidade?.inicioPrevisto,
      observacoes: value.disponibilidade?.observacoes,
      documentoIdentificacao: value.termos?.documentoIdentificacao,
      comprovanteEndereco: value.termos?.comprovanteEndereco,
      aceiteVoluntariado: value.termos?.aceiteVoluntariado,
      aceiteImagem: value.termos?.aceiteImagem,
      assinaturaDigital: value.termos?.assinaturaDigital
    };
  }

  private formatArray(values?: string[]): string {
    return values?.length ? values.join(', ') : 'NÃ£o informado';
  }

  private lookupAddressByCep(cep: string): void {
    this.cepLookupError = null;
    this.http
      .get<ViaCepResponse>(`https://viacep.com.br/ws/${cep}/json/`)
      .pipe(finalize(() => this.form.get(['endereço', 'cep'])?.markAsTouched()))
      .subscribe({
        next: (response) => {
          if (!response || response.erro) {
            this.cepLookupError = 'CEP não encontrado.';
            return;
          }

          this.form.patchValue({
            endereço: {
              cep: this.formatCep(response.cep ?? cep),
              logradouro: response.logradouro ?? '',
              complemento: response.complemento ?? '',
              bairro: response.bairro ?? '',
              municipio: response.localidade ?? '',
              uf: response.uf ?? ''
            }
          });
        },
        error: () => {
          this.cepLookupError = 'NÃ£o foi possÃ­vel consultar o CEP.';
        }
      });
  }

  private formatCep(value?: string | null): string {
    const digits = (value ?? '').replace(/\D/g, '').slice(0, 8);
    if (digits.length <= 5) return digits;
    return `${digits.slice(0, 5)}-${digits.slice(5)}`;
  }

  private normalizeCep(value?: string | null): string {
    return (value ?? '').replace(/\D/g, '').slice(0, 8);
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

  private cepValidator = (control: AbstractControl): ValidationErrors | null => {
    const value = this.normalizeCep(control.value as string);
    return value.length === 8 ? null : { cep: true };
  };

  private buildTermoHtml(value: any, unidade: AssistanceUnitPayload | null): string {
    const nome = value.dadosPessoais?.nomeCompleto || '_____________________';
    const cpf = value.dadosPessoais?.cpf || '_____________________';
    const rg = value.dadosPessoais?.rg || '_____________________';
    const dataNascimento = value.dadosPessoais?.dataNascimento || '____/____/______';
    const email = value.contato?.email || '_____________________';
    const telefone = value.contato?.telefone || '_____________________';
    const cidade = value.contato?.cidade || '_____________________';
    const estado = value.contato?.estado || '___';
    const areaInteresse = value.contato?.areaInteresse || '_____________________';
    const inicioPrevisto = value.disponibilidade?.inicioPrevisto || '____/____/______';
    const disponibilidadeDias = (value.disponibilidade?.dias ?? []).join(', ') || '_____________________';
    const disponibilidadePeriodos = (value.disponibilidade?.periodos ?? []).join(', ') || '_____________________';
    const aceiteLgpd = value.termos?.aceiteVoluntariado ? 'X' : ' ';
    const aceiteImagem = value.termos?.aceiteImagem ? 'X' : ' ';
    const razaoSocial = unidade?.razaoSocial || 'AgÃªncia Adventista de Desenvolvimento e Recursos Assistenciais';
    const cnpj = unidade?.cnpj || '_____________________';
    const logomarca = this.normalizarLogomarca(
      unidade?.logomarcaRelatorio || unidade?.logomarca || ''
    );
    const endereço = [unidade?.endereco, unidade?.numeroEndereco, unidade?.bairro, unidade?.cidade, unidade?.estado]
      .filter((item) => (item ?? '').toString().trim().length > 0)
      .join(', ');
    const dataAtual = this.formatarDataAtual();

    return `
      <html>
        <head>
          <title>Termo de voluntariado</title>
          <style>
            ${this.abntStyles()}
            @page { size: A4; margin: 25mm 20mm; }
            body { font-family: Arial, Helvetica, sans-serif; }
            .termo-header { display: grid; grid-template-columns: 120px 1fr 120px; align-items: center; gap: 12px; margin-bottom: 16px; }
            .termo-header__logo { width: 110px; max-height: 80px; height: auto; object-fit: contain; }
            .termo-header__center { text-align: center; }
            .termo-header__center .abnt-organization { font-size: 11pt; letter-spacing: 0.02em; }
            .term-title { text-align: center; font-size: 14pt; font-weight: 700; margin: 0 0 12px; }
            .term-subtitle { text-align: center; font-size: 10pt; margin: 0 0 12px; color: #475569; }
            .section-title { font-weight: 700; text-transform: uppercase; font-size: 11pt; margin-bottom: 6px; }
            .checkbox-line { display: flex; gap: 10px; align-items: center; margin-top: 6px; }
            .checkbox-box { border: 1px solid #0f172a; width: 16px; height: 16px; display: inline-flex; align-items: center; justify-content: center; font-weight: 700; }
            .signature-grid { margin-top: 18px; display: grid; grid-template-columns: 1fr 1fr; gap: 18px; }
            .signature-line { border-top: 1px solid #0f172a; padding-top: 6px; text-align: center; }
            .footnote { margin-top: 16px; font-size: 9pt; color: #475569; text-align: center; }
            .page-break { page-break-before: always; break-before: page; }
          </style>
        </head>
        <body>
          <header class="termo-header">
            <div>
              ${logomarca ? `<img class="termo-header__logo" src="${logomarca}" alt="Logomarca da instituicao" />` : ''}
            </div>
            <div class="termo-header__center">
              <p class="abnt-organization">${razaoSocial}</p>
              <p class="abnt-organization">CNPJ ${cnpj}</p>
              <p class="abnt-subtitle abnt-subtitle--center">Documento para assinatura do voluntário.</p>
            </div>
            <div></div>
          </header>

          <h1 class="term-title">TERMO DE ADESAO AO SERVICO VOLUNTARIO</h1>
          <p class="term-subtitle">Nos termos da Lei n.o 9.608/1998</p>

          <section class="abnt-section">
            <p class="section-title">1. Identificacao do(a) Voluntario(a)</p>
            <p><strong>Nome completo:</strong> ${nome}</p>
            <p><strong>CPF:</strong> ${cpf} | <strong>Data de nascimento:</strong> ${dataNascimento}</p>
            <p><strong>Contato:</strong> ${email} | ${telefone}</p>
            <p><strong>Cidade/UF:</strong> ${cidade} / ${estado}</p>
            <p><strong>Área de interesse:</strong> ${areaInteresse}</p>
            <p><strong>Disponibilidade:</strong> ${disponibilidadeDias} | ${disponibilidadePeriodos}</p>
            <p><strong>Início previsto:</strong> ${inicioPrevisto}</p>
          </section>

          <section class="abnt-section">
            <p class="section-title">2. Condicoes gerais</p>
            <ol>
              <li>O trabalho voluntario a ser desempenhado junto a ADRA (${razaoSocial}), de acordo com a Lei 9.608 de 18/02/1998, é atividade não remunerada, e não gera vínculo empregatício nem funcional, ou quaisquer obrigações trabalhistas, previdenciárias ou afins.</li>
              <li>O Voluntario se compromete a agir de acordo com os Princípios Humanitários de Humanidade, Imparcialidade, Neutralidade e Independência, bem como de acordo com o CÃ³digo de Conduta e Política de Proteção da ADRA enquanto estiver a serviço dela.</li>
              <li>Compete ao Voluntario participar das atividades e cumprir com empenho e interesse a função estabelecida.</li>
              <li>O Voluntario isenta a ADRA de qualquer responsabilidade referente a acidentes pessoais ou materiais, que porventura, venham a ocorrer no desempenho de suas atividades.</li>
              <li>O desligamento do Voluntario das atividades da ADRA podera ocorrer a qualquer momento, bastando apenas o desejo expresso de uma das partes, sendo necessaria a assinatura do Termo de Desligamento.</li>
              <li>Eventuais despesas decorrentes da atividade voluntária podem ser ressarcidas, desde que combinadas e autorizadas antecipadamente pela ADRA.</li>
              <li>O presente Termo de Adesao estara em vigor e validade para o ano referido na assinatura. Para um novo ano, devera ser renovado, caso seja de interesse de ambas as partes.</li>
            </ol>
            <p>De minha livre e espontânea vontade, movido(a) pela vontade de ser útil à comunidade, especialmente no atendimento assistencial a pessoas em vulnerabilidade social, nos termos da Lei n.o 9.608 de 18/02/1998, assino o presente TERMO DE VOLUNTARIADO declarando que dedicarei parte do meu tempo disponível, conforme indicado, aos servicos voluntarios coordenados pela ADRA, como minha colaboração espontânea, sem qualquer interesse remuneratório.</p>
            <p>Declaro estar ciente da legislação específica, regimento interno e descritivo da função, e que aceito atuar como Voluntario, não havendo entre mim e a ADRA, vínculo de emprego, direito e/ou obrigação de natureza trabalhista, previdenciária e/ou outras afins, razão por que firmo, em testemunho da verdade, do que dou fé.</p>
          </section>

          <section class="abnt-section">
            <p><strong>Local e data:</strong> ____________________________________________, ${dataAtual}</p>
            <div class="signature-grid">
              <div class="signature-line">Assinatura do(a) Voluntario(a) ou Responsavel Legal</div>
              <div class="signature-line">Assinatura da instituicao</div>
            </div>
          </section>

          <div class="page-break"></div>

          <section class="abnt-section">
            <p class="section-title">DeclaraÃ§Ã£o - LGPD (Lei Geral de ProteÃ§Ã£o de Dados)</p>
            <p>Sobre os dados pessoais fornecidos a ADRA, declaramos nosso compromisso etico no que se refere a esse documento, quanto ao cuidado com os dados pessoais de nossos Voluntarios.</p>
            <p>Esta excluida do conceito de privacidade a informacao: a) publica por determinacao legal; b) publica para o tratamento e uso compartilhado de dados de execucao de politicas publicas; c) necessaria a protecao da vida ou seguranca fisica de pessoas; d) necessaria a tutela da saude.</p>
            <p>Diante do exposto, consinto com o tratamento dos meus dados pessoais pela ADRA, manifestando meu consentimento com o aceite neste formulario.</p>
            <p><strong>Nome do(a) Voluntario(a):</strong> ${nome}</p>
            <div class="checkbox-line"><span class="checkbox-box">${aceiteLgpd}</span> Aceito</div>
            <div class="checkbox-line"><span class="checkbox-box">${aceiteLgpd === 'X' ? ' ' : 'X'}</span> NÃ£o aceito</div>
            <p><strong>Local e data:</strong> ____________________________________________, ${dataAtual}</p>
            <div class="signature-grid">
              <div class="signature-line">Assinatura do(a) Voluntario(a) ou Responsavel Legal</div>
              <div class="signature-line">Assinatura da instituicao</div>
            </div>
          </section>

          <div class="page-break"></div>

          <section class="abnt-section">
            <p class="section-title">DeclaraÃ§Ã£o - Termo de CessÃ£o de Imagem</p>
            <p>O voluntario expressamente autoriza (ou não) a utilização de sua voz e imagem para fins exclusivos de divulgação das atividades deste projeto ou da Instituição em mídia interna ou externa, na Internet, em Jornais, Revistas, folders e demais meios de comunica??o, livre de qualquer ônus para com a ADRA.</p>
            <p><strong>Nome do(a) Voluntario(a):</strong> ${nome}</p>
            <div class="checkbox-line"><span class="checkbox-box">${aceiteImagem}</span> Autorizo o uso de imagem</div>
            <div class="checkbox-line"><span class="checkbox-box">${aceiteImagem === 'X' ? ' ' : 'X'}</span> NÃ£o autorizo o uso de imagem</div>
            <p><strong>Local e data:</strong> ____________________________________________, ${dataAtual}</p>
            <div class="signature-grid">
              <div class="signature-line">Assinatura do(a) Voluntario(a) ou Responsavel Legal</div>
              <div class="signature-line">Assinatura da instituicao</div>
            </div>
          </section>

          <footer class="abnt-footer">
            <p><strong>${razaoSocial}</strong> - CNPJ ${cnpj}</p>
            <p>${endereço || 'EndereÃ§o não informado'}</p>
            <p>Contato: ${unidade?.telefone || '---'} - ${unidade?.email || '---'}</p>
          </footer>
          <p class="footnote">Termo de Voluntariado | ADRA Brasil | ${unidade?.estado || 'MG'}</p>
        </body>
      </html>
    `;
  }

  private buildVolunteerPrintHtml(volunteer: VolunteerRecord): string {
    return `\n      <html>\n        <head>\n          <title>Ficha do voluntario ${volunteer.nome}</title>\n          <style>${this.abntStyles()}</style>\n        </head>\n        <body>\n          <header class="abnt-header">\n            <div>\n              <p class="abnt-organization">${this.unidadePrincipal?.razaoSocial || 'ADRA'}</p>\n              <p class="abnt-organization">CNPJ ${this.unidadePrincipal?.cnpj || '---'}</p>\n              <h1>FICHA DE VOLUNTÁRIO</h1>\n            </div>\n            <p class="abnt-subtitle">Registro completo do voluntario para controle interno.</p>\n          </header>\n          <section class="abnt-section">\n            <h2>1. IDENTIFICAÇÃO</h2>\n            <p><strong>Nome:</strong> ${volunteer.nome}</p>\n            <p><strong>CPF:</strong> ${volunteer.cpf}</p>\n            <p><strong>E-mail:</strong> ${volunteer.email}</p>\n            <p><strong>Telefone:</strong> ${volunteer.telefone || 'NÃ£o informado'}</p>\n            <p><strong>Cidade/UF:</strong> ${volunteer.cidade || '---'} / ${volunteer.estado || '--'}</p>\n          </section>\n          <section class="abnt-section">\n            <h2>2. COMPETÊNCIAS E INTERESSES</h2>\n            <p><strong>Área de interesse:</strong> ${volunteer.areaInteresse || 'NÃ£o informado'}</p>\n            <p><strong>Habilidades:</strong> ${volunteer.habilidades || 'NÃ£o informado'}</p>\n            <p><strong>Idiomas:</strong> ${volunteer.idiomas || 'NÃ£o informado'}</p>\n            <p><strong>Motivação:</strong> ${volunteer.motivacao || 'NÃ£o informado'}</p>\n          </section>\n          <section class="abnt-section">\n            <h2>3. DISPONIBILIDADE</h2>\n            <p><strong>Dias:</strong> ${this.formatArray(volunteer.disponibilidadeDias)}</p>\n            <p><strong>Períodos:</strong> ${this.formatArray(volunteer.disponibilidadePeriodos)}</p>\n            <p><strong>Carga horária:</strong> ${volunteer.cargaHoraria || 'NÃ£o informado'}</p>\n            <p><strong>Formato:</strong> ${(volunteer.presencial ? 'Presencial ' : '') + (volunteer.remoto ? 'Remoto' : '') || 'NÃ£o informado'}</p>\n            <p><strong>Início previsto:</strong> ${volunteer.inicioPrevisto || 'NÃ£o informado'}</p>\n            <p><strong>ObservaÃ§Ãµes:</strong> ${volunteer.observacoes || '---'}</p>\n          </section>\n        </body>\n      </html>\n    `;
  }

  private loadVolunteers(): void {
    this.volunteerService
      .list()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (records) => {
          const unique = new Map<string, VolunteerRecord>();
          (records ?? []).forEach((volunteer) => {
            const normalized = {
              ...volunteer,
              id: String(volunteer.id ?? '')
            } as VolunteerRecord;
            const key = normalized.id || normalized.cpf || `${normalized.nome}-${normalized.email}`;
            if (!unique.has(key)) {
              unique.set(key, normalized);
            }
          });
          this.volunteers = Array.from(unique.values());
          this.applyListFilters();
        },
        error: () => {
          this.setFeedback('NÃ£o foi possÃ­vel carregar voluntarios.');
        }
      });
  }

  private resetForm(): void {
    this.form.reset({
      profissionalId: null,
      foto_3x4: '',
      dadosPessoais: {
        status: 'ATIVO',
        nomeCompleto: '',
        cpf: '',
        rg: '',
        dataNascimento: '',
        genero: '',
        profissao: '',
        motivacao: ''
      },
      endereço: {
        cep: '',
        logradouro: '',
        numero: '',
        complemento: '',
        bairro: '',
        pontoReferencia: '',
        municipio: '',
        zona: '',
        uf: ''
      },
      contato: {
        telefone: '',
        email: '',
        cidade: '',
        estado: '',
        areaInteresse: '',
        habilidades: '',
        idiomas: '',
        linkedin: ''
      },
      disponibilidade: {
        dias: [],
        periodos: [],
        cargaHorariaSemanal: '',
        inicioPrevisto: '',
        presencial: false,
        remoto: false,
        observacoes: ''
      },
      termos: {
        documentoIdentificacao: '',
        comprovanteEndereco: '',
        aceiteVoluntariado: false,
        aceiteImagem: false,
        assinaturaDigital: ''
      }
    });

    this.activeTab = 'lista';
    this.profissionalResultados = [];
    this.profissionalBusca = '';
    this.voluntarioSelecionado = null;
    this.documentoIdentificacaoNome = '';
    this.comprovanteEnderecoNome = '';
    this.photoPreview = null;
  }

  formatarStatusLabel(status?: string | null): string {
    if (!status) return '---';
    return status
      .toLowerCase()
      .split('_')
      .map((part) => part.charAt(0).toUpperCase() + part.slice(1))
      .join(' ');
  }

  calcularIdade(dataNascimento?: string | null): string {
    if (!dataNascimento) return '--';
    const data = new Date(dataNascimento);
    if (Number.isNaN(data.getTime())) return '--';

    const hoje = new Date();
    let idade = hoje.getFullYear() - data.getFullYear();
    const mes = hoje.getMonth() - data.getMonth();
    if (mes < 0 || (mes === 0 && hoje.getDate() < data.getDate())) {
      idade -= 1;
    }
    return idade >= 0 ? String(idade) : '--';
  }

  getStatusClass(status?: string | null): string {
    switch (status) {
      case 'ATIVO':
        return 'pill--status pill--status-ativo';
      case 'INATIVO':
        return 'pill--status pill--status-inativo';
      case 'BLOQUEADO':
        return 'pill--status pill--status-bloqueado';
      default:
        return 'pill--status';
    }
  }

  clearFeedback(): void {
    this.feedback = null;
    this.clearFeedbackTimeout();
  }

  private setFeedback(message: string, timeoutMs = 10000): void {
    this.feedback = message;
    this.clearFeedbackTimeout();
    this.feedbackTimeout = setTimeout(() => {
      this.feedback = null;
      this.feedbackTimeout = undefined;
    }, timeoutMs);
  }

  private clearFeedbackTimeout(): void {
    if (this.feedbackTimeout) {
      clearTimeout(this.feedbackTimeout);
      this.feedbackTimeout = undefined;
    }
  }

  onCpfInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const formatted = this.formatCpf(input.value);
    input.value = formatted;
    this.form.get(['dadosPessoais', 'cpf'])?.setValue(formatted, { emitEvent: false });
  }

  private normalizeCpf(value?: string | null): string {
    return (value ?? '').toString().replace(/\D/g, '');
  }

  private formatCpf(value?: string | null): string {
    const digits = this.normalizeCpf(value);
    const p1 = digits.slice(0, 3);
    const p2 = digits.slice(3, 6);
    const p3 = digits.slice(6, 9);
    const p4 = digits.slice(9, 11);

    if (digits.length <= 3) return p1;
    if (digits.length <= 6) return `${p1}.${p2}`;
    if (digits.length <= 9) return `${p1}.${p2}.${p3}`;
    return `${p1}.${p2}.${p3}-${p4}`;
  }

  private cpfValidator = (control: AbstractControl): ValidationErrors | null => {
    const value = this.normalizeCpf(control.value as string);
    if (!value) return null;
    if (value.length !== 11 || /^(\d)\1+$/.test(value)) return { cpfInvalid: true };

    let sum = 0;
    for (let i = 0; i < 9; i += 1) sum += Number(value.charAt(i)) * (10 - i);
    let digit = 11 - (sum % 11);
    if (digit > 9) digit = 0;
    if (digit !== Number(value.charAt(9))) return { cpfInvalid: true };

    sum = 0;
    for (let i = 0; i < 10; i += 1) sum += Number(value.charAt(i)) * (11 - i);
    digit = 11 - (sum % 11);
    if (digit > 9) digit = 0;
    return digit === Number(value.charAt(10)) ? null : { cpfInvalid: true };
  };

  onBuscaProfissional(term: string): void {
    this.profissionalBusca = term;
    const normalized = this.normalizeText(term);
    if (!normalized) {
      this.profissionalResultados = [];
      return;
    }
    this.profissionalResultados = this.profissionaisVoluntarios
      .filter((item) => {
        const nome = this.normalizeText(item.nomeCompleto);
        const cpf = this.normalizeText(item.cpf);
        return nome.includes(normalized) || cpf.includes(normalized);
      })
      .slice(0, 6);
  }

  selecionarProfissional(profissional: ProfessionalRecord): void {
    const emailProfissional = (profissional.email ?? '').trim();
    const telefoneProfissional = (profissional.telefone ?? '').trim();
    this.form.patchValue({
      profissionalId: Number(profissional.id),
      dadosPessoais: {
        nomeCompleto: profissional.nomeCompleto,
        cpf: this.formatCpf(profissional.cpf ?? ''),
        dataNascimento: profissional.dataNascimento ?? '',
        genero: this.normalizarSexoProfissional(profissional.sexoBiologico),
        profissao: profissional.especialidade ?? ''
      },
      endereço: {
        cep: profissional.cep ?? '',
        logradouro: profissional.logradouro ?? '',
        numero: profissional.numero ?? '',
        complemento: profissional.complemento ?? '',
        bairro: profissional.bairro ?? '',
        pontoReferencia: profissional.pontoReferencia ?? '',
        municipio: profissional.municipio ?? '',
        zona: profissional.zona ?? '',
        uf: profissional.uf ?? ''
      },
      contato: {
        email: emailProfissional || this.form.get(['contato', 'email'])?.value || '',
        telefone: telefoneProfissional || this.form.get(['contato', 'telefone'])?.value || '',
        cidade: profissional.municipio ?? '',
        estado: profissional.uf ?? ''
      }
    });
    this.profissionalBusca = profissional.nomeCompleto;
    this.profissionalResultados = [];
  }

  private normalizarSexoProfissional(sexo?: string | null): string {
    if (!sexo) return '';
    const normalizado = sexo.trim().toLowerCase();
    if (!normalizado) return '';
    if (normalizado.startsWith('f')) return 'Feminino';
    if (normalizado.startsWith('m')) return 'Masculino';
    if (normalizado.startsWith('o') || normalizado.includes('out')) return 'Outro';
    if (normalizado.includes('nao') || normalizado.includes('n/a') || normalizado.includes('indef')) return '';
    return 'Outro';
  }

  onDocumentoSelecionado(controlName: 'documentoIdentificacao' | 'comprovanteEndereco', event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) {
      return;
    }

    const reader = new FileReader();
    reader.onload = () => {
      const base64 = reader.result as string;
      this.form.get(['termos', controlName])?.setValue(base64);
      if (controlName === 'documentoIdentificacao') {
        this.documentoIdentificacaoNome = file.name;
      } else {
        this.comprovanteEnderecoNome = file.name;
      }
    };
    reader.readAsDataURL(file);
  }

  onCepInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = (input.value ?? '').replace(/\D/g, '').slice(0, 8);
    const formatted = this.formatCep(digits);

    this.form.get(['endereço', 'cep'])?.setValue(formatted, { emitEvent: false });
    input.value = formatted;
    this.cepLookupError = null;
  }

  onCepBlur(): void {
    const cepControl = this.form.get(['endereço', 'cep']);
    if (!cepControl) return;

    if (cepControl.invalid) {
      this.cepLookupError = cepControl.value ? 'Informe um CEP valido para consultar o endereço.' : null;
      return;
    }

    const digits = this.normalizeCep(cepControl.value as string);
    if (digits.length !== 8) {
      this.cepLookupError = 'Informe um CEP valido para consultar o endereço.';
      return;
    }

    this.lookupAddressByCep(digits);
  }

  onTelefoneInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = (input.value ?? '').replace(/\D/g, '').slice(0, 11);
    const formatted = this.formatPhoneValue(digits);

    this.form.get(['contato', 'telefone'])?.setValue(formatted, { emitEvent: false });
    input.value = formatted;
  }

  async startCamera(): Promise<void> {
    this.captureError = null;
    if (!(navigator && navigator.mediaDevices && navigator.mediaDevices.getUserMedia)) {
      this.captureError = 'Seu navegador nÃ£o permite capturar a camera.';
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
      console.error('Erro ao iniciar camera', error);
      this.captureError = 'NÃ£o foi possÃ­vel acessar a camera.';
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
      this.ngZone.run(() => {
        this.setPhotoPreview(dataUrl);
        input.value = '';
      });
    };
    reader.readAsDataURL(file);
  }

  removePhoto(): void {
    this.photoPreview = null;
    this.form.get('foto_3x4')?.reset();
  }

  private setPhotoPreview(dataUrl: string): void {
    this.ngZone.run(() => {
      this.photoPreview = dataUrl;
      this.form.get('foto_3x4')?.setValue(dataUrl);
    });
  }

  aplicarFiltrosListagem(): void {
    this.applyListFilters();
  }

  limparFiltrosListagem(): void {
    this.listagemForm.reset({ nome: '', cpf: '', codigo: '', dataNascimento: '', status: '' });
    this.applyListFilters();
  }

  onBuscar(): void {
    this.changeTab('lista');
    this.applyListFilters();
  }

  selecionarVoluntarioNaLista(voluntario: VolunteerRecord): void {
    this.voluntarioSelecionado = voluntario;
  }

  editarVoluntarioNaLista(voluntario: VolunteerRecord): void {
    this.editVolunteer(voluntario);
    this.changeTab('dados');
  }

  private loadUnidadePrincipal(): void {
    this.assistanceUnitService
      .list()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (unidades) => {
          this.unidadePrincipal = (unidades ?? []).find((item) => item.unidadePrincipal) ?? null;
        },
        error: () => {
          this.unidadePrincipal = null;
        }
      });
  }

  private applyListFilters(): void {
    const { nome, cpf } = this.listagemForm.value;
    const filtroNome = this.normalizeText(nome);
    const filtroCpf = this.normalizeText(cpf);

    this.volunteersFiltrados = (this.volunteers ?? [])
      .filter((item) => {
        if (filtroNome && !this.normalizeText(item.nome).includes(filtroNome)) return false;
        if (filtroCpf && !this.normalizeText(item.cpf).includes(filtroCpf)) return false;
        return true;
      })
      .sort((a, b) => this.normalizeText(a.nome).localeCompare(this.normalizeText(b.nome), 'pt-BR'));

    this.paginaAtual = 1;
  }

  private normalizeText(value?: string | null): string {
    return (value ?? '')
      .toString()
      .trim()
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '');
  }

  private setupCapitalizationRules(): void {
    const applyRule = (controlName: string) => {
      const control = this.form.get(controlName);
      if (!control) return;

      const subscription = control.valueChanges.subscribe((value) => {
        if (typeof value !== 'string') return;
        if (controlName === 'contato.email') return;
        this.applyCapitalization(controlName);
      });

      this.capitalizationSubs.push(() => subscription.unsubscribe());
    };

    [
      'dadosPessoais.nomeCompleto',
      'dadosPessoais.profissao',
      'endereço.logradouro',
      'endereço.complemento',
      'endereço.bairro',
      'endereço.pontoReferencia',
      'endereço.municipio',
      'contato.cidade',
      'contato.areaInteresse',
      'contato.habilidades',
      'contato.idiomas'
    ].forEach(applyRule);
  }

  private applyCapitalization(controlName: string): void {
    const control = this.form.get(controlName);
    if (!control) return;

    const currentValue = control.value;
    if (typeof currentValue !== 'string') return;

    const transformed = titleCaseWords(currentValue);
    if (transformed && transformed !== currentValue) {
      control.setValue(transformed, { emitEvent: false });
    }
  }

  private loadProfissionaisVoluntarios(): void {
    this.carregandoProfissionais = true;
    this.professionalService
      .list()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (records) => {
          this.profissionaisVoluntarios = (records ?? []).filter(
            (item) => {
              const vinculo = (item.vinculo ?? '').trim().toUpperCase();
              return !vinculo || vinculo === 'VOLUNTARIO';
            }
          );
          this.carregandoProfissionais = false;
        },
        error: () => {
          this.profissionaisVoluntarios = [];
          this.carregandoProfissionais = false;
        }
      });
  }

  private formatarDataAtual(): string {
    const agora = new Date();
    const dia = String(agora.getDate()).padStart(2, '0');
    const mes = String(agora.getMonth() + 1).padStart(2, '0');
    const ano = agora.getFullYear();
    return `${dia}/${mes}/${ano}`;
  }

  private normalizarLogomarca(valor: string): string {
    const texto = (valor ?? '').toString().trim();
    if (!texto) return '';
    if (texto.startsWith('data:')) return texto;
    if (texto.startsWith('http://') || texto.startsWith('https://')) return texto;
    return `data:image/png;base64,${texto}`;
  }
}

interface ViaCepResponse {
  cep?: string;
  logradouro?: string;
  complemento?: string;
  bairro?: string;
  localidade?: string;
  uf: string;
  erro?: boolean;
}

