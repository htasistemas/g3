import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { OficioPayload, OficioService, TramiteRegistro } from '../../services/oficio.service';

import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';
import { PopupErrorBuilder } from '../../utils/popup-error.builder';
import { ConfigAcoesCrud, EstadoAcoesCrud, TelaBaseComponent } from '../compartilhado/tela-base.component';

interface StepTab {
  id: string;
  label: string;
}

@Component({
  selector: 'app-oficios-gestao',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, TelaPadraoComponent, PopupMessagesComponent],
  templateUrl: './oficios-gestao.component.html',
  styleUrl: './oficios-gestao.component.scss'
})
export class OficiosGestaoComponent extends TelaBaseComponent {
  form: FormGroup;
  tabs: StepTab[] = [
    { id: 'identificacao', label: 'Identificacao e protocolo' },
    { id: 'conteudo', label: 'Redacao do oficio' },
    { id: 'tramitacao', label: 'Tramitacao e acompanhamento' },
    { id: 'confirmacao', label: 'Resumo e emissao' },
    { id: 'listagem', label: 'Oficios registrados' }
  ];
  activeTab = 'identificacao';
  saving = false;
  popupErros: string[] = [];
  popupTitulo = 'Aviso';
  tramites: TramiteRegistro[] = [];
  oficios: OficioPayload[] = [];
  editingId: string | null = null;

  readonly statusOptions = ['Rascunho', 'Em preparacao', 'Enviado', 'Recebido', 'Em analise', 'Arquivado'];
  readonly meiosEnvio = ['E-mail', 'Correio', 'Entrega presencial', 'Sistema SEI', 'Mensageria'];
  readonly acoesToolbar: Required<ConfigAcoesCrud> = this.criarConfigAcoes({
    buscar: true,
    novo: true,
    salvar: true,
    cancelar: true,
    excluir: true,
    imprimir: true
  });

  constructor(private readonly fb: FormBuilder, private readonly oficioService: OficioService) {
    super();
    const initialState = this.buildInitialFormValue();
    this.form = this.fb.group({
      identificacao: this.fb.group({
        tipo: [initialState.identificacao.tipo, Validators.required],
        numero: [initialState.identificacao.numero, Validators.required],
        data: [initialState.identificacao.data, Validators.required],
        setorOrigem: [initialState.identificacao.setorOrigem, Validators.required],
        responsavel: [initialState.identificacao.responsavel, Validators.required],
        destinatario: [initialState.identificacao.destinatario, Validators.required],
        meioEnvio: [initialState.identificacao.meioEnvio, Validators.required],
        prazoResposta: [initialState.identificacao.prazoResposta],
        classificacao: [initialState.identificacao.classificacao]
      }),
      conteudo: this.fb.group({
        razaoSocial: [initialState.conteudo.razaoSocial, Validators.required],
        logoUrl: [initialState.conteudo.logoUrl],
        titulo: [initialState.conteudo.titulo],
        saudacao: [initialState.conteudo.saudacao],
        assunto: [initialState.conteudo.assunto],
        corpo: [initialState.conteudo.corpo, Validators.required],
        finalizacao: [initialState.conteudo.finalizacao],
        assinaturaNome: [initialState.conteudo.assinaturaNome],
        assinaturaCargo: [initialState.conteudo.assinaturaCargo],
        rodape: [initialState.conteudo.rodape]
      }),
      protocolo: this.fb.group({
        status: [initialState.protocolo.status, Validators.required],
        protocoloEnvio: [initialState.protocolo.protocoloEnvio],
        dataEnvio: [initialState.protocolo.dataEnvio],
        protocoloRecebimento: [initialState.protocolo.protocoloRecebimento],
        dataRecebimento: [initialState.protocolo.dataRecebimento],
        proximoDestino: [initialState.protocolo.proximoDestino],
        observacoes: [initialState.protocolo.observacoes]
      }),
      tramitacao: this.fb.group({
        data: [initialState.tramitacao.data],
        origem: [initialState.tramitacao.origem],
        destino: [initialState.tramitacao.destino],
        responsavel: [initialState.tramitacao.responsavel],
        acao: [initialState.tramitacao.acao, Validators.required],
        observacoes: [initialState.tramitacao.observacoes]
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

  get acoesDesabilitadas(): EstadoAcoesCrud {
    return {
      salvar: this.saving,
      cancelar: this.saving,
      novo: this.saving,
      excluir: !this.editingId,
      imprimir: this.saving,
      buscar: this.saving
    };
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
      this.popupTitulo = 'Campos obrigatorios';
      this.popupErros = new PopupErrorBuilder().adicionar('Preencha os campos obrigatorios antes de salvar.').build();
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
      this.oficioService.update(this.editingId, payload).subscribe({
        next: () => {
          this.popupTitulo = 'Sucesso';
          this.popupErros = new PopupErrorBuilder().adicionar('Registro de oficio atualizado com sucesso.').build();
          this.loadOficios();
          this.resetState();
        },
        error: () => {
          this.popupTitulo = 'Erro ao salvar';
          this.popupErros = new PopupErrorBuilder().adicionar('Nao foi possivel salvar o oficio.').build();
        },
        complete: () => {
          this.saving = false;
        }
      });
      return;
    }

    this.oficioService.create(payload).subscribe({
      next: () => {
        this.popupTitulo = 'Sucesso';
        this.popupErros = new PopupErrorBuilder().adicionar('Oficio cadastrado e protocolado com sucesso.').build();
        this.loadOficios();
        this.resetState();
      },
      error: () => {
        this.popupTitulo = 'Erro ao salvar';
        this.popupErros = new PopupErrorBuilder().adicionar('Nao foi possivel salvar o oficio.').build();
      },
      complete: () => {
        this.saving = false;
      }
    });
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
    this.popupTitulo = 'Edicao ativa';
    this.popupErros = new PopupErrorBuilder().adicionar('Edicao ativa. Atualize os dados e salve para registrar a nova versao.').build();
  }

  delete(id: string | undefined): void {
    if (!id) return;
    this.oficioService.delete(id).subscribe({
      next: () => {
        this.popupTitulo = 'Sucesso';
        this.popupErros = new PopupErrorBuilder().adicionar('Oficio excluido com sucesso.').build();
        this.loadOficios();
        if (this.editingId === id) {
          this.resetState();
        }
      },
      error: () => {
        this.popupTitulo = 'Erro ao excluir';
        this.popupErros = new PopupErrorBuilder().adicionar('Nao foi possivel excluir o oficio.').build();
      }
    });
  }

  printCurrent(): void {
    if (!this.form.valid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: OficioPayload = {
      identificacao: this.form.value.identificacao,
      conteudo: this.form.value.conteudo,
      protocolo: {
        ...this.form.value.protocolo,
        status: this.form.value.protocolo?.status ?? 'Rascunho'
      },
      tramites: this.tramites
    };

    this.imprimirModelo(payload);
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
      protocoloRecebimento: protocoloGroup?.value.protocoloRecebimento || crypto.randomUUID().slice(0, 8),
      status: 'Recebido'
    });
  }

  loadOficios(): void {
    this.oficioService.list().subscribe({
      next: (oficios) => {
        this.oficios = oficios;
      },
      error: () => {
        this.popupTitulo = 'Erro ao carregar';
        this.popupErros = new PopupErrorBuilder().adicionar('Nao foi possivel carregar os oficios.').build();
      }
    });
  }

  resetForm(): void {
    if (this.editingId) {
      const registro = this.oficios.find((oficio) => oficio.id === this.editingId);
      if (registro) {
        this.tramites = registro.tramites ?? [];
        this.form.reset({
          identificacao: registro.identificacao,
          conteudo: registro.conteudo,
          protocolo: registro.protocolo,
          tramitacao: {
            data: new Date().toISOString().split('T')[0],
            origem: registro.identificacao.setorOrigem,
            destino: registro.identificacao.destinatario,
            responsavel: registro.identificacao.responsavel,
            acao: '',
            observacoes: ''
          }
        });
        this.changeTab('identificacao');
        this.popupErros = [];
        return;
      }
    }

    this.resetState();
  }

  startNew(): void {
    this.resetState();
  }

  onBuscar(): void {
    this.changeTab('listagem');
  }

  closeForm(): void {
    window.history.back();
  }

  getLocalAtual(oficio: OficioPayload): string {
    if (oficio.tramites && oficio.tramites.length > 0) {
      return oficio.tramites[0].destino || oficio.tramites[0].origem || 'Em transito';
    }
    return oficio.protocolo?.proximoDestino || oficio.identificacao.destinatario || '-';
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
          <title>${conteudo.titulo || 'Modelo de oficio'}</title>
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
              <p class="meta">Oficio n° ${identificacao.numero} | ${identificacao.setorOrigem}</p>
              <p class="meta">Data: ${this.formatDate(identificacao.data)}</p>
            </div>
          </div>
          <div class="meta">Destinatario: ${identificacao.destinatario}</div>
          <div class="assunto">Assunto: ${conteudo.assunto}</div>
          <p class="saudacao">${conteudo.saudacao || ''}</p>
          <div class="corpo">${conteudo.corpo}</div>
          <p class="saudacao">${conteudo.finalizacao || ''}</p>
          <div class="assinatura">
            ${conteudo.assinaturaNome || ''}<br />
            ${conteudo.assinaturaCargo || ''}
          </div>
          <div class="rodape">
            ${conteudo.rodape || ''}<br />
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

  private buildInitialFormValue() {
    const today = new Date().toISOString().split('T')[0];

    return {
      identificacao: {
        tipo: 'emissao',
        numero: '',
        data: today,
        setorOrigem: '',
        responsavel: '',
        destinatario: '',
        meioEnvio: 'E-mail',
        prazoResposta: '',
        classificacao: ''
      },
      conteudo: {
        razaoSocial: '',
        logoUrl: '',
        titulo: '',
        saudacao: '',
        assunto: '',
        corpo: '',
        finalizacao: '',
        assinaturaNome: '',
        assinaturaCargo: '',
        rodape: ''
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
        data: today,
        origem: '',
        destino: '',
        responsavel: '',
        acao: '',
        observacoes: ''
      }
    };
  }

  private resetState(): void {
    const initialState = this.buildInitialFormValue();
    this.tramites = [];
    this.editingId = null;
    this.form.reset(initialState);
    this.popupErros = [];
    this.saving = false;
    this.changeTab('identificacao');
  }

  fecharPopupErros(): void {
    this.popupErros = [];
  }
}
