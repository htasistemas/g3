import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Subject, firstValueFrom } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { AssistanceUnitPayload, AssistanceUnitService } from '../../services/assistance-unit.service';
import {
  FotoEventoDetalheResponse,
  FotoEventoFotoResponse,
  FotoEventoRequestPayload,
  FotoEventoResponse,
  FotosEventosService
} from '../../services/fotos-eventos.service';
import { environment } from '../../../environments/environment';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';
import { DialogComponent } from '../compartilhado/dialog/dialog.component';
import { PopupErrorBuilder } from '../../utils/popup-error.builder';
import { ConfigAcoesCrud, EstadoAcoesCrud, TelaBaseComponent } from '../compartilhado/tela-base.component';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';

type FotoUploadItem = {
  arquivo: File;
  preview: string;
  legenda: string;
  ordem: number | null;
  upload: { nomeArquivo: string; contentType: string; conteudo: string };
};

@Component({
  selector: 'app-fotos-eventos',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    TelaPadraoComponent,
    PopupMessagesComponent,
    DialogComponent
  ],
  templateUrl: './fotos-eventos.component.html',
  styleUrl: './fotos-eventos.component.scss'
})
export class FotosEventosComponent extends TelaBaseComponent implements OnInit, OnDestroy {
  filtrosForm: FormGroup;
  eventoForm: FormGroup;
  fotoForm: FormGroup;
  feedback: string | null = null;
  popupErros: string[] = [];
  listLoading = false;
  eventos: FotoEventoResponse[] = [];
  unidades: AssistanceUnitPayload[] = [];
  paginaAtual = 0;
  totalPaginas = 1;
  totalRegistros = 0;

  detalheAberto = false;
  eventoSelecionado: FotoEventoResponse | null = null;
  fotosEvento: FotoEventoFotoResponse[] = [];
  detalheLoading = false;

  formularioAberto = false;
  uploadPrincipalPreview: string | null = null;
  eventoEmEdicaoId: number | null = null;
  salvandoEvento = false;

  galeriaUploadAberta = false;
  fotosUpload: FotoUploadItem[] = [];
  enviandoFotos = false;

  lightboxAberto = false;
  lightboxUrl: string | null = null;
  lightboxLegenda: string | null = null;

  editarFotoAberto = false;
  fotoEmEdicao: FotoEventoFotoResponse | null = null;

  confirmarExclusaoAberta = false;
  confirmarExclusaoMensagem = '';
  eventoParaExcluir: FotoEventoResponse | null = null;
  fotoParaExcluir: FotoEventoFotoResponse | null = null;

  private readonly destroy$ = new Subject<void>();
  private feedbackTimeout?: ReturnType<typeof setTimeout>;

  readonly acoesToolbar: Required<ConfigAcoesCrud> = this.criarConfigAcoes({
    novo: true
  });

  get acoesDesabilitadas(): EstadoAcoesCrud {
    return {
      novo: this.salvandoEvento || this.enviandoFotos
    };
  }

  constructor(
    private readonly fb: FormBuilder,
    private readonly service: FotosEventosService,
    private readonly unidadeService: AssistanceUnitService
  ) {
    super();
    this.filtrosForm = this.fb.group({
      busca: [''],
      dataInicio: [''],
      dataFim: [''],
      unidadeId: ['']
    });
    this.eventoForm = this.fb.group({
      titulo: ['', Validators.required],
      dataEvento: ['', Validators.required],
      local: [''],
      descricao: [''],
      tags: [''],
      unidadeId: [''],
      fotoPrincipalUpload: [null as FotoEventoRequestPayload['fotoPrincipalUpload'] | null, Validators.required]
    });
    this.fotoForm = this.fb.group({
      legenda: [''],
      ordem: ['']
    });
  }

  ngOnInit(): void {
    this.carregarUnidades();
    this.carregarEventos();
    this.filtrosForm.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(() => {
      this.paginaAtual = 0;
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    if (this.feedbackTimeout) {
      clearTimeout(this.feedbackTimeout);
    }
  }

  carregarEventos(): void {
    const filtros = this.filtrosForm.value;
    this.listLoading = true;
    this.service
      .listar({
        busca: filtros.busca?.trim() || undefined,
        dataInicio: filtros.dataInicio || undefined,
        dataFim: filtros.dataFim || undefined,
        unidadeId: filtros.unidadeId ? Number(filtros.unidadeId) : undefined,
        pagina: this.paginaAtual,
        tamanho: 12
      })
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          this.eventos = (response.eventos ?? []).map((evento) => this.normalizarEvento(evento));
          this.totalPaginas = response.totalPaginas || 1;
          this.totalRegistros = response.total || 0;
          this.listLoading = false;
        },
        error: () => {
          this.listLoading = false;
          this.popupErros = new PopupErrorBuilder()
            .adicionar('Nao foi possivel carregar os eventos.')
            .build();
        }
      });
  }

  carregarUnidades(): void {
    this.unidadeService
      .list()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (unidades) => {
          this.unidades = unidades ?? [];
        },
        error: () => {
          this.unidades = [];
        }
      });
  }

  aplicarFiltros(): void {
    this.paginaAtual = 0;
    this.carregarEventos();
  }

  limparFiltros(): void {
    this.filtrosForm.reset({ busca: '', dataInicio: '', dataFim: '', unidadeId: '' });
    this.paginaAtual = 0;
    this.carregarEventos();
  }

  proximaPagina(): void {
    if (this.paginaAtual + 1 >= this.totalPaginas) return;
    this.paginaAtual += 1;
    this.carregarEventos();
  }

  paginaAnterior(): void {
    if (this.paginaAtual <= 0) return;
    this.paginaAtual -= 1;
    this.carregarEventos();
  }

  abrirDetalhe(evento: FotoEventoResponse): void {
    this.detalheAberto = true;
    this.eventoSelecionado = evento;
    this.carregarDetalhe(evento.id);
  }

  fecharDetalhe(): void {
    this.detalheAberto = false;
    this.eventoSelecionado = null;
    this.fotosEvento = [];
  }

  carregarDetalhe(id: number): void {
    this.detalheLoading = true;
    this.service
      .obter(id)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: FotoEventoDetalheResponse) => {
          this.eventoSelecionado = this.normalizarEvento(response.evento);
          this.fotosEvento = (response.fotos ?? []).map((foto) => this.normalizarFoto(foto));
          this.detalheLoading = false;
        },
        error: () => {
          this.detalheLoading = false;
          this.popupErros = new PopupErrorBuilder()
            .adicionar('Nao foi possivel carregar o detalhe do evento.')
            .build();
        }
      });
  }

  abrirFormularioNovo(): void {
    this.formularioAberto = true;
    this.eventoEmEdicaoId = null;
    this.atualizarValidacaoFotoPrincipal(true);
    this.eventoForm.reset({
      titulo: '',
      dataEvento: '',
      local: '',
      descricao: '',
      tags: '',
      unidadeId: '',
      fotoPrincipalUpload: null
    });
    this.uploadPrincipalPreview = null;
  }

  abrirFormularioEdicao(): void {
    if (!this.eventoSelecionado) return;
    this.formularioAberto = true;
    this.eventoEmEdicaoId = this.eventoSelecionado.id;
    this.atualizarValidacaoFotoPrincipal(false);
    this.eventoForm.reset({
      titulo: this.eventoSelecionado.titulo || '',
      dataEvento: this.eventoSelecionado.dataEvento || '',
      local: this.eventoSelecionado.local || '',
      descricao: this.eventoSelecionado.descricao || '',
      tags: this.eventoSelecionado.tags || '',
      unidadeId: this.eventoSelecionado.unidadeId ? String(this.eventoSelecionado.unidadeId) : '',
      fotoPrincipalUpload: null
    });
    this.uploadPrincipalPreview = this.eventoSelecionado.fotoPrincipalUrl || null;
  }

  fecharFormulario(): void {
    this.formularioAberto = false;
    this.eventoEmEdicaoId = null;
    this.uploadPrincipalPreview = null;
    this.atualizarValidacaoFotoPrincipal(true);
  }

  async salvarEvento(): Promise<void> {
    if (this.eventoForm.invalid) {
      this.eventoForm.markAllAsTouched();
      this.popupErros = new PopupErrorBuilder()
        .adicionar('Preencha os campos obrigatorios do evento.')
        .build();
      return;
    }

    const payload = this.montarPayloadEvento();
    if (!payload.fotoPrincipalUpload && !this.eventoEmEdicaoId) {
      this.popupErros = new PopupErrorBuilder()
        .adicionar('Informe a foto principal do evento.')
        .build();
      return;
    }

    this.salvandoEvento = true;
    try {
      const response = this.eventoEmEdicaoId
        ? await firstValueFrom(this.service.atualizar(this.eventoEmEdicaoId, payload))
        : await firstValueFrom(this.service.criar(payload));
      this.salvandoEvento = false;
      this.formularioAberto = false;
      this.mostrarFeedback('Evento salvo com sucesso.');
      this.carregarEventos();
      this.abrirDetalhe(response);
    } catch {
      this.salvandoEvento = false;
      this.popupErros = new PopupErrorBuilder()
        .adicionar('Nao foi possivel salvar o evento.')
        .build();
    }
  }

  confirmarExcluirEvento(): void {
    if (!this.eventoSelecionado) return;
    this.confirmarExclusaoMensagem = 'Deseja excluir este evento?';
    this.eventoParaExcluir = this.eventoSelecionado;
    this.fotoParaExcluir = null;
    this.confirmarExclusaoAberta = true;
  }

  confirmarExcluirFoto(foto: FotoEventoFotoResponse): void {
    this.confirmarExclusaoMensagem = 'Deseja remover esta foto?';
    this.eventoParaExcluir = null;
    this.fotoParaExcluir = foto;
    this.confirmarExclusaoAberta = true;
  }

  cancelarExcluir(): void {
    this.confirmarExclusaoAberta = false;
    this.eventoParaExcluir = null;
    this.fotoParaExcluir = null;
  }

  async executarExcluir(): Promise<void> {
    if (this.eventoParaExcluir) {
      const id = this.eventoParaExcluir.id;
      this.confirmarExclusaoAberta = false;
      try {
        await firstValueFrom(this.service.excluir(id));
        this.mostrarFeedback('Evento removido com sucesso.');
        this.fecharDetalhe();
        this.carregarEventos();
      } catch {
        this.popupErros = new PopupErrorBuilder()
          .adicionar('Nao foi possivel excluir o evento.')
          .build();
      }
      return;
    }

    if (this.fotoParaExcluir && this.eventoSelecionado) {
      const eventoId = this.eventoSelecionado.id;
      const fotoId = this.fotoParaExcluir.id;
      this.confirmarExclusaoAberta = false;
      try {
        await firstValueFrom(this.service.removerFoto(eventoId, fotoId));
        this.mostrarFeedback('Foto removida com sucesso.');
        this.carregarDetalhe(eventoId);
      } catch {
        this.popupErros = new PopupErrorBuilder()
          .adicionar('Nao foi possivel remover a foto.')
          .build();
      }
    }
  }

  abrirUploadFotos(): void {
    if (!this.eventoSelecionado) return;
    this.galeriaUploadAberta = true;
    this.fotosUpload = [];
  }

  fecharUploadFotos(): void {
    this.galeriaUploadAberta = false;
    this.fotosUpload = [];
  }

  async enviarFotos(): Promise<void> {
    if (!this.eventoSelecionado || !this.fotosUpload.length) {
      this.mostrarFeedback('Selecione ao menos uma foto para enviar.');
      return;
    }

    this.enviandoFotos = true;
    try {
      for (const foto of this.fotosUpload) {
        await firstValueFrom(
          this.service.adicionarFoto(this.eventoSelecionado.id, {
            arquivo: foto.upload,
            legenda: foto.legenda,
            ordem: foto.ordem
          })
        );
      }
      this.enviandoFotos = false;
      this.galeriaUploadAberta = false;
      this.mostrarFeedback('Fotos adicionadas com sucesso.');
      this.carregarDetalhe(this.eventoSelecionado.id);
    } catch {
      this.enviandoFotos = false;
      this.popupErros = new PopupErrorBuilder()
        .adicionar('Nao foi possivel enviar as fotos.')
        .build();
    }
  }

  abrirLightbox(foto: FotoEventoFotoResponse): void {
    this.lightboxAberto = true;
    this.lightboxUrl = foto.arquivoUrl || null;
    this.lightboxLegenda = foto.legenda || null;
  }

  fecharLightbox(): void {
    this.lightboxAberto = false;
    this.lightboxUrl = null;
    this.lightboxLegenda = null;
  }

  abrirEdicaoFoto(foto: FotoEventoFotoResponse): void {
    this.fotoEmEdicao = foto;
    this.editarFotoAberto = true;
    this.fotoForm.reset({
      legenda: foto.legenda || '',
      ordem: foto.ordem ?? ''
    });
  }

  fecharEdicaoFoto(): void {
    this.editarFotoAberto = false;
    this.fotoEmEdicao = null;
  }

  async salvarEdicaoFoto(): Promise<void> {
    if (!this.fotoEmEdicao || !this.eventoSelecionado) return;
    const legenda = (this.fotoForm.get('legenda')?.value as string) || '';
    const ordemRaw = this.fotoForm.get('ordem')?.value as string;
    const ordem = ordemRaw !== '' ? Number(ordemRaw) : null;
    try {
      await firstValueFrom(
        this.service.atualizarFoto(this.eventoSelecionado.id, this.fotoEmEdicao.id, {
          legenda,
          ordem
        })
      );
      this.fecharEdicaoFoto();
      this.mostrarFeedback('Foto atualizada com sucesso.');
      this.carregarDetalhe(this.eventoSelecionado.id);
    } catch {
      this.popupErros = new PopupErrorBuilder()
        .adicionar('Nao foi possivel atualizar a foto.')
        .build();
    }
  }

  async definirFotoPrincipal(foto: FotoEventoFotoResponse): Promise<void> {
    if (!this.eventoSelecionado) return;
    try {
      await firstValueFrom(
        this.service.atualizar(this.eventoSelecionado.id, {
          ...this.montarPayloadEventoSelecionado(),
          fotoPrincipalId: foto.id
        })
      );
      this.mostrarFeedback('Foto principal atualizada.');
      this.carregarDetalhe(this.eventoSelecionado.id);
      this.carregarEventos();
    } catch {
      this.popupErros = new PopupErrorBuilder()
        .adicionar('Nao foi possivel atualizar a foto principal.')
        .build();
    }
  }

  onFotoPrincipalSelecionada(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;
    this.converterArquivo(file).then((upload) => {
      this.eventoForm.get('fotoPrincipalUpload')?.setValue(upload);
      this.uploadPrincipalPreview = upload.conteudo;
      input.value = '';
    });
  }

  onFotosSelecionadas(event: Event): void {
    const input = event.target as HTMLInputElement;
    const files = Array.from(input.files ?? []);
    if (!files.length) return;
    files.forEach(async (file, index) => {
      const upload = await this.converterArquivo(file);
      this.fotosUpload.push({
        arquivo: file,
        preview: upload.conteudo,
        legenda: '',
        ordem: this.fotosUpload.length + index + 1,
        upload
      });
    });
    input.value = '';
  }

  removerFotoUpload(index: number): void {
    this.fotosUpload.splice(index, 1);
  }

  fecharPopupErros(): void {
    this.popupErros = [];
  }

  dismissFeedback(): void {
    this.feedback = null;
  }

  obterTags(tags?: string | null): string[] {
    if (!tags) return [];
    return tags
      .split(',')
      .map((item) => item.trim())
      .filter((item) => item);
  }

  recortarTexto(texto?: string | null, limite = 120): string {
    if (!texto) return '';
    if (texto.length <= limite) return texto;
    return `${texto.slice(0, limite)}...`;
  }

  private montarPayloadEvento(): FotoEventoRequestPayload {
    const value = this.eventoForm.value;
    return {
      titulo: value.titulo,
      descricao: value.descricao || undefined,
      dataEvento: value.dataEvento,
      local: value.local || undefined,
      tags: value.tags || undefined,
      unidadeId: value.unidadeId ? Number(value.unidadeId) : null,
      fotoPrincipalUpload: value.fotoPrincipalUpload || undefined
    };
  }

  private montarPayloadEventoSelecionado(): FotoEventoRequestPayload {
    const evento = this.eventoSelecionado;
    if (!evento) {
      return {
        titulo: '',
        dataEvento: '',
        fotoPrincipalUpload: undefined
      };
    }
    return {
      titulo: evento.titulo,
      descricao: evento.descricao || undefined,
      dataEvento: evento.dataEvento,
      local: evento.local || undefined,
      tags: evento.tags || undefined,
      unidadeId: evento.unidadeId ?? null,
      fotoPrincipalUpload: undefined
    };
  }

  private atualizarValidacaoFotoPrincipal(obrigatorio: boolean): void {
    const control = this.eventoForm.get('fotoPrincipalUpload') as FormControl | null;
    if (!control) return;
    control.clearValidators();
    if (obrigatorio) {
      control.setValidators([Validators.required]);
    }
    control.updateValueAndValidity({ emitEvent: false });
  }

  private normalizarEvento(evento: FotoEventoResponse): FotoEventoResponse {
    return {
      ...evento,
      fotoPrincipalUrl: this.normalizarUrl(evento.fotoPrincipalUrl)
    };
  }

  private normalizarFoto(foto: FotoEventoFotoResponse): FotoEventoFotoResponse {
    return {
      ...foto,
      arquivoUrl: this.normalizarUrl(foto.arquivoUrl)
    };
  }

  private normalizarUrl(url?: string | null): string | null {
    if (!url) return null;
    if (url.startsWith('http')) return url;
    return `${environment.apiUrl}${url}`;
  }

  private async converterArquivo(file: File): Promise<{ nomeArquivo: string; contentType: string; conteudo: string }> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => {
        const conteudo = reader.result as string;
        resolve({ nomeArquivo: file.name, contentType: file.type, conteudo });
      };
      reader.onerror = () => reject(reader.error);
      reader.readAsDataURL(file);
    });
  }

  private mostrarFeedback(mensagem: string): void {
    this.feedback = mensagem;
    if (this.feedbackTimeout) {
      clearTimeout(this.feedbackTimeout);
    }
    this.feedbackTimeout = setTimeout(() => {
      this.feedback = null;
    }, 10000);
  }
}
