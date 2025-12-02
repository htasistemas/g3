import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { OficioPayload, OficioService, TramiteRegistro } from '../../services/oficio.service';

interface StepTab {
  id: string;
  label: string;
}

@Component({
  selector: 'app-oficios-gestao',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './oficios-gestao.component.html',
  styleUrl: './oficios-gestao.component.scss'
})
export class OficiosGestaoComponent {
  form: FormGroup;
  tabs: StepTab[] = [
    { id: 'identificacao', label: 'Identificação e protocolo' },
    { id: 'conteudo', label: 'Redação do ofício' },
    { id: 'tramitacao', label: 'Tramitação e acompanhamento' },
    { id: 'confirmacao', label: 'Resumo e emissão' }
  ];
  activeTab = 'identificacao';
  saving = false;
  feedback: string | null = null;
  tramites: TramiteRegistro[] = [];
  oficios: OficioPayload[] = [];
  editingId: string | null = null;

  readonly statusOptions = ['Rascunho', 'Em preparação', 'Enviado', 'Recebido', 'Em análise', 'Arquivado'];
  readonly meiosEnvio = ['E-mail', 'Correio', 'Entrega presencial', 'Sistema SEI', 'Mensageria'];

  constructor(private readonly fb: FormBuilder, private readonly oficioService: OficioService) {
    const hoje = new Date().toISOString().split('T')[0];
    this.form = this.fb.group({
      identificacao: this.fb.group({
        tipo: ['emissao', Validators.required],
        numero: ['', Validators.required],
        data: [hoje, Validators.required],
        setorOrigem: ['Núcleo de Uberlândia', Validators.required],
        responsavel: ['Adriano Martins Torres', Validators.required],
        destinatario: ['Bítrvic Brasil', Validators.required],
        meioEnvio: ['E-mail', Validators.required],
        prazoResposta: [''],
        classificacao: ['Solicitação']
      }),
      conteudo: this.fb.group({
        razaoSocial: ['Justiça em Ação - ADRA Núcleo Uberlândia', Validators.required],
        logoUrl: [''],
        titulo: ['OFÍCIO DE SOLICITAÇÃO DE DOAÇÕES'],
        saudacao: ['Prezado(a),'],
        assunto: ['Solicitação de doação de alimentos e serviços para Feira Solidária'],
        corpo: [
          'A ADRA - Agência Adventista de Desenvolvimento e Recursos Assistenciais Unidade Uberlândia promove a Feira Solidária visando apoiar famílias em situação de vulnerabilidade. Solicitamos apoio na forma de alimentos, serviços e equipe para atendimento ao público durante o evento.',
          Validators.required
        ],
        finalizacao: ['Agradecemos antecipadamente e permanecemos à disposição para esclarecimentos.'],
        assinaturaNome: ['Adriano Martins Torres'],
        assinaturaCargo: ['Diretor - ADRA Núcleo Uberlândia'],
        rodape: ['ADRA Núcleo de Uberlândia | CNPJ: 60.974.622/0001-71 | Avenida João Pinheiro, 400 - Uberlândia/MG | (34) 3229-9958 | uberlandia@adra.org.br']
      }),
      protocolo: this.fb.group({
        status: ['Rascunho', Validators.required],
        protocoloEnvio: [''],
        dataEnvio: [''],
        protocoloRecebimento: [''],
        dataRecebimento: [''],
        proximoDestino: [''],
        observacoes: ['']
      }),
      tramitacao: this.fb.group({
        data: [hoje],
        origem: [''],
        destino: [''],
        responsavel: [''],
        acao: ['', Validators.required],
        observacoes: ['']
      })
    });

    this.loadOficios();
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

  changeTab(tab: string): void {
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

  addTramite(): void {
    const grupo = this.form.get('tramitacao') as FormGroup;
    if (!grupo.valid) {
      grupo.markAllAsTouched();
      return;
    }

    const novo: TramiteRegistro = { ...grupo.value };
    this.tramites = [novo, ...this.tramites];
    grupo.reset({ data: new Date().toISOString().split('T')[0], acao: '' });
  }

  submit(): void {
    if (!this.form.valid) {
      this.form.markAllAsTouched();
      this.feedback = 'Preencha os campos obrigatórios antes de salvar.';
      return;
    }

    this.saving = true;
    const payload: OficioPayload = {
      identificacao: this.form.value.identificacao,
      conteudo: this.form.value.conteudo,
      protocolo: {
        ...this.form.value.protocolo,
        status: this.form.value.protocolo?.status ?? 'Rascunho'
      },
      tramites: this.tramites
    };

    if (this.editingId) {
      this.oficioService.update(this.editingId, payload);
      this.feedback = 'Registro de ofício atualizado com sucesso!';
    } else {
      this.oficioService.create(payload);
      this.feedback = 'Ofício cadastrado e protocolado localmente.';
    }

    this.loadOficios();
    this.saving = false;
    this.editingId = null;
    this.form.reset({
      identificacao: {
        tipo: 'emissao',
        numero: '',
        data: new Date().toISOString().split('T')[0],
        setorOrigem: 'Núcleo de Uberlândia',
        responsavel: 'Adriano Martins Torres',
        destinatario: 'Bítrvic Brasil',
        meioEnvio: 'E-mail',
        prazoResposta: '',
        classificacao: 'Solicitação'
      },
      conteudo: {
        razaoSocial: 'Justiça em Ação - ADRA Núcleo Uberlândia',
        logoUrl: '',
        titulo: 'OFÍCIO DE SOLICITAÇÃO DE DOAÇÕES',
        saudacao: 'Prezado(a),',
        assunto: 'Solicitação de doação de alimentos e serviços para Feira Solidária',
        corpo:
          'A ADRA - Agência Adventista de Desenvolvimento e Recursos Assistenciais Unidade Uberlândia promove a Feira Solidária visando apoiar famílias em situação de vulnerabilidade. Solicitamos apoio na forma de alimentos, serviços e equipe para atendimento ao público durante o evento.',
        finalizacao: 'Agradecemos antecipadamente e permanecemos à disposição para esclarecimentos.',
        assinaturaNome: 'Adriano Martins Torres',
        assinaturaCargo: 'Diretor - ADRA Núcleo Uberlândia',
        rodape:
          'ADRA Núcleo de Uberlândia | CNPJ: 60.974.622/0001-71 | Avenida João Pinheiro, 400 - Uberlândia/MG | (34) 3229-9958 | uberlandia@adra.org.br'
      },
      protocolo: {
        status: 'Rascunho',
        protocoloEnvio: '',
        dataEnvio: '',
        protocoloRecebimento: '',
        dataRecebimento: '',
        proximoDestino: '',
        observacoes: ''
      },
      tramitacao: {
        data: new Date().toISOString().split('T')[0],
        origem: '',
        destino: '',
        responsavel: '',
        acao: '',
        observacoes: ''
      }
    });
    this.tramites = [];
    this.changeTab('identificacao');
  }

  imprimirRascunho(): void {
    if (!this.form.value.identificacao || !this.form.value.conteudo || !this.form.value.protocolo) {
      return;
    }

    const payload: OficioPayload = {
      identificacao: this.form.value.identificacao,
      conteudo: this.form.value.conteudo,
      protocolo: this.form.value.protocolo,
      tramites: this.tramites
    };

    this.imprimirModelo(payload);
  }

  edit(oficio: OficioPayload): void {
    this.editingId = oficio.id ?? null;
    this.tramites = oficio.tramites ?? [];
    this.form.patchValue({
      identificacao: oficio.identificacao,
      conteudo: oficio.conteudo,
      protocolo: oficio.protocolo,
      tramitacao: {
        data: new Date().toISOString().split('T')[0],
        origem: oficio.identificacao.setorOrigem,
        destino: oficio.identificacao.destinatario,
        responsavel: oficio.identificacao.responsavel,
        acao: '',
        observacoes: ''
      }
    });
    this.activeTab = 'identificacao';
    this.feedback = 'Edição ativa. Atualize os dados e salve para registrar uma nova versão local.';
  }

  delete(id: string | undefined): void {
    if (!id) return;
    this.oficioService.delete(id);
    this.loadOficios();
  }

  registrarEnvio(): void {
    const protocoloGroup = this.form.get('protocolo');
    const hoje = new Date().toISOString().split('T')[0];
    protocoloGroup?.patchValue({
      dataEnvio: hoje,
      protocoloEnvio: protocoloGroup?.value.protocoloEnvio || crypto.randomUUID().slice(0, 8),
      status: 'Enviado'
    });
  }

  registrarRecebimento(): void {
    const protocoloGroup = this.form.get('protocolo');
    const hoje = new Date().toISOString().split('T')[0];
    protocoloGroup?.patchValue({
      dataRecebimento: hoje,
      protocoloRecebimento:
        protocoloGroup?.value.protocoloRecebimento || crypto.randomUUID().slice(0, 8),
      status: 'Recebido'
    });
  }

  loadOficios(): void {
    this.oficios = this.oficioService.list();
  }

  getLocalAtual(oficio: OficioPayload): string {
    if (oficio.tramites && oficio.tramites.length > 0) {
      return oficio.tramites[0].destino || oficio.tramites[0].origem || 'Em trânsito';
    }
    return oficio.protocolo?.proximoDestino || oficio.identificacao.destinatario || '—';
  }

  imprimirModelo(oficio: OficioPayload): void {
    const w = window.open('', '_blank', 'width=900,height=1200');
    if (!w) return;
    const { identificacao, conteudo, protocolo } = oficio;
    const logo = conteudo.logoUrl
      ? `<img src="${conteudo.logoUrl}" alt="Logo" class="header-logo" />`
      : '';

    w.document.write(`
      <html>
        <head>
          <title>${conteudo.titulo || 'Modelo de ofício'}</title>
          <style>
            body { font-family: 'Times New Roman', serif; margin: 2.5cm; font-size: 12pt; line-height: 1.6; }
            .header { display: flex; align-items: center; gap: 16px; margin-bottom: 12px; }
            .header h1 { margin: 0; font-size: 16pt; text-transform: uppercase; letter-spacing: 0.02em; }
            .meta { font-size: 11pt; color: #2f3640; margin-bottom: 12px; }
            .assunto { margin: 16px 0; font-weight: bold; text-transform: uppercase; }
            .saudacao { margin: 12px 0; }
            .corpo { text-align: justify; white-space: pre-line; }
            .assinatura { margin-top: 36px; font-weight: bold; }
            .rodape { margin-top: 48px; font-size: 10pt; border-top: 1px solid #d1d5db; padding-top: 8px; color: #4b5563; }
            .header-logo { height: 60px; }
          </style>
        </head>
        <body>
          <div class="header">
            ${logo}
            <div>
              <h1>${conteudo.razaoSocial}</h1>
              <p class="meta">Ofício nº ${identificacao.numero} | ${identificacao.setorOrigem}</p>
              <p class="meta">Data: ${this.formatDate(identificacao.data)}</p>
            </div>
          </div>
          <div class="meta">Destinatário: ${identificacao.destinatario}</div>
          <div class="assunto">Assunto: ${conteudo.assunto}</div>
          <p class="saudacao">${conteudo.saudacao}</p>
          <div class="corpo">${conteudo.corpo}</div>
          <p class="saudacao">${conteudo.finalizacao}</p>
          <div class="assinatura">
            ${conteudo.assinaturaNome}<br />
            ${conteudo.assinaturaCargo}
          </div>
          <div class="rodape">
            ${conteudo.rodape}<br />
            Status: ${protocolo.status}${
      protocolo.protocoloEnvio ? ` | Protocolo de envio: ${protocolo.protocoloEnvio}` : ''
    }${protocolo.protocoloRecebimento ? ` | Protocolo de recebimento: ${protocolo.protocoloRecebimento}` : ''}
          </div>
        </body>
      </html>
    `);
    w.document.close();
    w.focus();
  }

  formatDate(value?: string): string {
    if (!value) return '';
    const [year, month, day] = value.split('-').map(Number);
    return `${String(day).padStart(2, '0')}/${String(month).padStart(2, '0')}/${year}`;
  }
}
