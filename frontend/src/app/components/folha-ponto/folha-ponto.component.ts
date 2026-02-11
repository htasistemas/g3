import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faClock, faLocationDot, faFileExcel, faUserClock } from '@fortawesome/free-solid-svg-icons';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { DialogComponent } from '../compartilhado/dialog/dialog.component';
import { BarraAcoesCrudComponent } from '../compartilhado/barra-acoes-crud/barra-acoes-crud.component';
import { PopupErrorBuilder } from '../../utils/popup-error.builder';
import { AuthService } from '../../services/auth.service';
import {
  FolhaPontoService,
  RhConfiguracaoPontoResponse,
  RhLocalPontoResponse,
  RhPontoDiaResumoResponse,
  RhPontoEspelhoResponse,
  UnidadeAssistencialResponse
} from '../../services/folha-ponto.service';
import { UserPayload, UserService } from '../../services/user.service';

interface TabItem {
  id: 'registro' | 'espelho' | 'locais' | 'configuracao';
  label: string;
}

@Component({
  selector: 'app-folha-ponto',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    FontAwesomeModule,
    TelaPadraoComponent,
    PopupMessagesComponent,
    DialogComponent,
    BarraAcoesCrudComponent
  ],
  templateUrl: './folha-ponto.component.html',
  styleUrl: './folha-ponto.component.scss'
})
export class FolhaPontoComponent implements OnInit {
  readonly faClock = faClock;
  readonly faLocationDot = faLocationDot;
  readonly faFileExcel = faFileExcel;
  readonly faUserClock = faUserClock;

  tabs: TabItem[] = [
    { id: 'registro', label: 'Registro diário' },
    { id: 'espelho', label: 'Espelho mensal' },
    { id: 'locais', label: 'Locais de ponto' },
    { id: 'configuracao', label: 'Configuração' }
  ];

  activeTab: TabItem['id'] = 'registro';

  acoesToolbar = {
    buscar: true,
    imprimir: true,
    salvar: false,
    excluir: false,
    novo: false,
    cancelar: false
  };

  acoesDesabilitadas = {
    buscar: false,
    imprimir: false,
    salvar: true,
    excluir: true,
    novo: true,
    cancelar: true
  };

  popupErros: string[] = [];
  popupTitulo = 'Campos obrigatórios';

  dialogBatidaAberta = false;
  dialogOcorrenciaAberta = false;
  senhaBatida = '';
  tipoBatidaAtual: 'E1' | 'S1' | 'E2' | 'S2' | null = null;
  localizacaoAtual: { latitude: number; longitude: number; accuracy: number; distancia: number; dentro: boolean } | null = null;

  espelho: RhPontoEspelhoResponse | null = null;
  localAtivo: RhLocalPontoResponse | null = null;
  configuracao: RhConfiguracaoPontoResponse | null = null;
  unidadeAssistencial: UnidadeAssistencialResponse | null = null;
  unidadesAssistenciais: UnidadeAssistencialResponse[] = [];
  unidadeSelecionadaId: number | null = null;
  usuarios: UserPayload[] = [];
  funcionarioSelecionadoId: number | null = null;

  localForm: FormGroup;
  configuracaoForm: FormGroup;
  ocorrenciaForm: FormGroup;
  localEditandoId: number | null = null;
  pontoDiaEditandoId: number | null = null;

  private _locais: RhLocalPontoResponse[] = [];

  constructor(
    private readonly fb: FormBuilder,
    private readonly service: FolhaPontoService,
    private readonly userService: UserService,
    private readonly auth: AuthService,
    private readonly router: Router
  ) {
    this.localForm = this.fb.group({
      nome: ['', Validators.required],
      endereco: [''],
      latitude: [null, Validators.required],
      longitude: [null, Validators.required],
      raioMetros: [80, [Validators.required, Validators.min(10)]],
      accuracyMaxMetros: [80, [Validators.required, Validators.min(10)]],
      ativo: [true]
    });

    this.configuracaoForm = this.fb.group({
      cargaSemanalMinutos: [2400, Validators.required],
      cargaSegQuiMinutos: [540, Validators.required],
      cargaSextaMinutos: [240, Validators.required],
      cargaSabadoMinutos: [0, Validators.required],
      cargaDomingoMinutos: [0, Validators.required],
      toleranciaMinutos: [10, Validators.required]
    });

    this.ocorrenciaForm = this.fb.group({
      ocorrencia: ['NORMAL', Validators.required],
      observacoes: ['']
    });
  }

  ngOnInit(): void {
    if (!this.isRhAdmin) {
      this.acoesToolbar.imprimir = false;
      this.acoesDesabilitadas.imprimir = true;
      this.tabs = this.tabs.filter((tab) => tab.id === 'registro' || tab.id === 'espelho');
    }
    this.carregarLocalAtivo();
    this.carregarConfiguracao();
    this.carregarUnidadeAssistencial();
    this.carregarUnidadesAssistenciais();
    this.carregarEspelhoAtual();
    if (this.isRhAdmin) {
      this.carregarUsuarios();
      this.carregarLocais();
    }
  }

  get locais(): RhLocalPontoResponse[] {
    return this._locais;
  }

  set locais(value: RhLocalPontoResponse[]) {
    this._locais = value;
  }

  get isRhAdmin(): boolean {
    const permissoes = this.auth.user()?.permissoes ?? [];
    return permissoes.includes('RH_ADMIN') || permissoes.includes('ADMINISTRADOR');
  }

  get usuarioIdAtual(): number | null {
    const id = this.auth.user()?.id;
    return id ? Number(id) : null;
  }

  get funcionarioIdAtual(): number | null {
    return this.isRhAdmin
      ? this.funcionarioSelecionadoId ?? this.usuarioIdAtual
      : this.usuarioIdAtual;
  }

  getTabIndex(id: TabItem['id']): number {
    return this.tabs.findIndex((tab) => tab.id === id);
  }

  changeTab(tab: TabItem['id']): void {
    this.activeTab = tab;
    if (tab === 'espelho') {
      this.carregarEspelhoAtual();
    }
    if (tab === 'locais') {
      this.carregarLocais();
      this.carregarUnidadeAssistencial();
      this.carregarUnidadesAssistenciais();
    }
    if (tab === 'configuracao') {
      this.carregarConfiguracao();
    }
  }

  onBuscar(): void {
    this.carregarEspelhoAtual();
  }

  onImprimir(): void {
    if (!this.funcionarioIdAtual) {
      return;
    }
    const referencia = this.referenciaAtual();
    this.service.exportarExcel(referencia.mes, referencia.ano, this.funcionarioIdAtual).subscribe({
      next: (arquivo) => {
        const url = window.URL.createObjectURL(arquivo);
        const link = document.createElement('a');
        link.href = url;
        link.download = 'folha_ponto.xlsx';
        link.click();
        window.URL.revokeObjectURL(url);
      },
      error: () => {
        this.popupTitulo = 'Erro';
        this.popupErros = new PopupErrorBuilder().adicionar('Não foi possível exportar o relatório.').build();
      }
    });
  }

  onFechar(): void {
    this.router.navigate(['/dashboard/visao-geral']);
  }

  fecharPopupErros(): void {
    this.popupErros = [];
    this.popupTitulo = 'Campos obrigatórios';
  }

  carregarUsuarios(): void {
    this.userService.list().subscribe({
      next: (lista) => {
        this.usuarios = lista ?? [];
        if (!this.funcionarioSelecionadoId && this.usuarioIdAtual) {
          this.funcionarioSelecionadoId = this.usuarioIdAtual;
        }
      },
      error: () => {
        this.usuarios = [];
      }
    });
  }

  carregarLocais(): void {
    this.service.listarLocais().subscribe({
      next: (lista) => {
        this.locais = lista ?? [];
      },
      error: () => {
        this.locais = [];
      }
    });
  }

  carregarUnidadeAssistencial(): void {
    this.service.buscarUnidadeAtual().subscribe({
      next: (response) => {
        this.unidadeAssistencial = response?.unidade ?? null;
        if (this.unidadeAssistencial && !this.unidadeSelecionadaId) {
          this.unidadeSelecionadaId = this.unidadeAssistencial.id;
        }
      },
      error: () => {
        this.unidadeAssistencial = null;
      }
    });
  }

  carregarUnidadesAssistenciais(): void {
    this.service.listarUnidades().subscribe({
      next: (lista) => {
        this.unidadesAssistenciais = lista ?? [];
        if (!this.unidadeSelecionadaId && this.unidadesAssistenciais.length) {
          this.unidadeSelecionadaId = this.unidadesAssistenciais[0].id;
        }
      },
      error: () => {
        this.unidadesAssistenciais = [];
      }
    });
  }

  selecionarUnidade(unidadeId: number): void {
    this.unidadeSelecionadaId = unidadeId;
    const unidade = this.unidadesAssistenciais.find((item) => item.id === unidadeId);
    if (!unidade) {
      return;
    }
    const enderecoTexto = [
      unidade.endereco,
      unidade.numeroEndereco,
      unidade.bairro,
      unidade.cidade,
      unidade.estado
    ]
      .filter(Boolean)
      .join(' ');
    this.localForm.patchValue({
      nome: unidade.nomeFantasia,
      endereco: enderecoTexto
    });
    this.service.geocodificarEnderecoUnidade(unidade.id).subscribe({
      next: (atualizada) => {
        this.localForm.patchValue({
          latitude: atualizada.latitude ?? '',
          longitude: atualizada.longitude ?? ''
        });
      },
      error: (erro) => {
        const mensagem =
          erro?.error?.mensagem ||
          erro?.error?.message ||
          'Não foi possível localizar a latitude e longitude do endereço.';
        this.popupTitulo = 'Erro';
        this.popupErros = new PopupErrorBuilder().adicionar(mensagem).build();
      }
    });
  }

  carregarLocalAtivo(): void {
    this.service.buscarLocalAtivo().subscribe({
      next: (local) => {
        this.localAtivo = local;
      },
      error: () => {
        this.localAtivo = null;
      }
    });
  }

  carregarConfiguracao(): void {
    this.service.buscarConfiguracao().subscribe({
      next: (config) => {
        this.configuracao = config;
        this.configuracaoForm.patchValue({
          cargaSemanalMinutos: config.cargaSemanalMinutos,
          cargaSegQuiMinutos: config.cargaSegQuiMinutos,
          cargaSextaMinutos: config.cargaSextaMinutos,
          cargaSabadoMinutos: config.cargaSabadoMinutos,
          cargaDomingoMinutos: config.cargaDomingoMinutos,
          toleranciaMinutos: config.toleranciaMinutos
        });
      },
      error: () => {
        this.configuracao = null;
      }
    });
  }

  carregarEspelhoAtual(): void {
    const referencia = this.referenciaAtual();
    const funcionarioId = this.funcionarioIdAtual;
    if (!funcionarioId) {
      return;
    }
    this.service.consultarEspelho(referencia.mes, referencia.ano, funcionarioId).subscribe({
      next: (espelho) => {
        this.espelho = espelho;
      },
      error: () => {
        this.espelho = null;
      }
    });
  }

  iniciarBatida(tipo: 'E1' | 'S1' | 'E2' | 'S2'): void {
    if (!this.funcionarioIdAtual) {
      return;
    }
    if (!navigator.geolocation) {
      this.popupTitulo = 'Erro';
      this.popupErros = new PopupErrorBuilder().adicionar('Geolocalização indisponível no navegador.').build();
      return;
    }
    navigator.geolocation.getCurrentPosition(
      (posicao) => {
        const { latitude, longitude, accuracy } = posicao.coords;
        const local = this.localAtivo;
        if (!local) {
          this.popupTitulo = 'Erro';
          this.popupErros = new PopupErrorBuilder().adicionar('Local de ponto ativo não configurado.').build();
          return;
        }
        if (accuracy > local.accuracyMaxMetros) {
          this.popupTitulo = 'Localização';
          this.popupErros = new PopupErrorBuilder()
            .adicionar('Precisão do GPS insuficiente para registrar o ponto.')
            .build();
          return;
        }
        const distancia = this.calcularDistanciaMetros(latitude, longitude, local.latitude, local.longitude);
        const dentro = distancia <= local.raioMetros;
        if (!dentro) {
          this.popupTitulo = 'Localização';
          this.popupErros = new PopupErrorBuilder()
            .adicionar('Você precisa estar na instituição para registrar o ponto.')
            .build();
          return;
        }
        this.localizacaoAtual = { latitude, longitude, accuracy, distancia, dentro };
        this.tipoBatidaAtual = tipo;
        this.senhaBatida = '';
        this.dialogBatidaAberta = true;
      },
      () => {
        this.popupTitulo = 'Localização';
        this.popupErros = new PopupErrorBuilder()
          .adicionar('Permita o acesso à localização para registrar o ponto.')
          .build();
      },
      { enableHighAccuracy: true, timeout: 10000 }
    );
  }

  confirmarBatida(): void {
    if (!this.tipoBatidaAtual || !this.localizacaoAtual || !this.usuarioIdAtual) {
      return;
    }
    if (!this.senhaBatida) {
      this.popupTitulo = 'Campos obrigatórios';
      this.popupErros = new PopupErrorBuilder().adicionar('Informe a senha para confirmar a batida.').build();
      return;
    }
    const payload = {
      tipo: this.tipoBatidaAtual,
      senha: this.senhaBatida,
      latitude: this.localizacaoAtual.latitude,
      longitude: this.localizacaoAtual.longitude,
      accuracy: this.localizacaoAtual.accuracy
    };
    this.service.baterPonto(this.usuarioIdAtual, payload).subscribe({
      next: () => {
        this.dialogBatidaAberta = false;
        this.popupTitulo = 'Sucesso';
        this.popupErros = new PopupErrorBuilder().adicionar('Ponto registrado com sucesso.').build();
        this.carregarEspelhoAtual();
      },
      error: (erro) => {
        this.popupTitulo = 'Erro';
        const mensagem = erro?.error?.mensagem || erro?.message || 'Não foi possível registrar o ponto.';
        this.popupErros = new PopupErrorBuilder().adicionar(mensagem).build();
      }
    });
  }

  cancelarBatida(): void {
    this.dialogBatidaAberta = false;
    this.tipoBatidaAtual = null;
  }

  abrirEditarOcorrencia(dia: RhPontoDiaResumoResponse): void {
    if (!dia.pontoDiaId) {
      return;
    }
    this.pontoDiaEditandoId = dia.pontoDiaId;
    this.ocorrenciaForm.patchValue({
      ocorrencia: dia.ocorrencia || 'NORMAL',
      observacoes: dia.observacoes || ''
    });
    this.dialogOcorrenciaAberta = true;
  }

  confirmarEditarOcorrencia(): void {
    if (!this.pontoDiaEditandoId || !this.usuarioIdAtual) {
      return;
    }
    const payload = this.ocorrenciaForm.getRawValue();
    this.service.atualizarDia(this.pontoDiaEditandoId, this.usuarioIdAtual, payload).subscribe({
      next: () => {
        this.dialogOcorrenciaAberta = false;
        this.carregarEspelhoAtual();
        this.popupTitulo = 'Sucesso';
        this.popupErros = new PopupErrorBuilder().adicionar('Ocorrência atualizada com sucesso.').build();
      },
      error: (erro) => {
        const mensagem = erro?.error?.mensagem || 'Não foi possível atualizar a ocorrência.';
        this.popupTitulo = 'Erro';
        this.popupErros = new PopupErrorBuilder().adicionar(mensagem).build();
      }
    });
  }

  cancelarEditarOcorrencia(): void {
    this.dialogOcorrenciaAberta = false;
  }

  salvarLocal(): void {
    if (this.localForm.invalid) {
      this.localForm.markAllAsTouched();
      return;
    }
    const payload = this.localForm.getRawValue();
    const acao$ = this.localEditandoId
      ? this.service.atualizarLocal(this.localEditandoId, payload)
      : this.service.criarLocal(payload);
    acao$.subscribe({
      next: () => {
        this.localEditandoId = null;
        this.localForm.reset({ ativo: true, raioMetros: 80, accuracyMaxMetros: 80 });
        this.carregarLocais();
        this.popupTitulo = 'Sucesso';
        this.popupErros = new PopupErrorBuilder().adicionar('Local de ponto salvo com sucesso.').build();
      },
      error: (erro) => {
        const mensagem = erro?.error?.mensagem || 'Não foi possível salvar o local.';
        this.popupTitulo = 'Erro';
        this.popupErros = new PopupErrorBuilder().adicionar(mensagem).build();
      }
    });
  }

  editarLocal(local: RhLocalPontoResponse): void {
    this.localEditandoId = local.id;
    this.localForm.patchValue({
      nome: local.nome,
      endereco: local.endereco,
      latitude: local.latitude,
      longitude: local.longitude,
      raioMetros: local.raioMetros,
      accuracyMaxMetros: local.accuracyMaxMetros,
      ativo: local.ativo
    });
  }

  removerLocal(local: RhLocalPontoResponse): void {
    this.service.removerLocal(local.id).subscribe({
      next: () => {
        this.carregarLocais();
      },
      error: () => {
        this.popupTitulo = 'Erro';
        this.popupErros = new PopupErrorBuilder().adicionar('Não foi possível remover o local.').build();
      }
    });
  }

  salvarConfiguracao(): void {
    if (this.configuracaoForm.invalid || !this.usuarioIdAtual) {
      this.configuracaoForm.markAllAsTouched();
      return;
    }
    this.service.atualizarConfiguracao(this.usuarioIdAtual, this.configuracaoForm.getRawValue()).subscribe({
      next: (config) => {
        this.configuracao = config;
        this.popupTitulo = 'Sucesso';
        this.popupErros = new PopupErrorBuilder().adicionar('Configuração atualizada com sucesso.').build();
      },
      error: (erro) => {
        const mensagem = erro?.error?.mensagem || 'Não foi possível atualizar a configuração.';
        this.popupTitulo = 'Erro';
        this.popupErros = new PopupErrorBuilder().adicionar(mensagem).build();
      }
    });
  }

  selecionarFuncionario(id: string): void {
    this.funcionarioSelecionadoId = id ? Number(id) : null;
    this.carregarEspelhoAtual();
  }

  getResumoDiaAtual(): RhPontoDiaResumoResponse | null {
    if (!this.espelho) {
      return null;
    }
    const dataHoje = this.dataHojeIsoLocal();

    return this.espelho.dias.find((dia) => dia.data === dataHoje) ?? null;
  }

  proximoTipo(): 'E1' | 'S1' | 'E2' | 'S2' | null {
    const resumo = this.getResumoDiaAtual();
    if (!resumo) {
      return 'E1';
    }
    if (!resumo.entradaManha) {
      return 'E1';
    }
    if (!resumo.saidaManha) {
      return 'S1';
    }
    if (!resumo.entradaTarde) {
      return 'E2';
    }
    if (!resumo.saidaTarde) {
      return 'S2';
    }
    return null;
  }

  podeRegistrar(tipo: 'E1' | 'S1' | 'E2' | 'S2'): boolean {
    return this.proximoTipo() === tipo;
  }

  formatarMinutos(minutos?: number | null): string {
    if (!minutos) {
      return '00:00';
    }
    const total = Math.max(0, minutos);
    const horas = Math.floor(total / 60);
    const resto = total % 60;
    return `${horas.toString().padStart(2, '0')}:${resto.toString().padStart(2, '0')}`;
  }

  private referenciaAtual(): { mes: number; ano: number } {
    const hoje = new Date();
    return { mes: hoje.getMonth() + 1, ano: hoje.getFullYear() };
  }

  private dataHojeIsoLocal(): string {
    const hoje = new Date();
    const ano = hoje.getFullYear();
    const mes = String(hoje.getMonth() + 1).padStart(2, '0');
    const dia = String(hoje.getDate()).padStart(2, '0');
    return `${ano}-${mes}-${dia}`;
  }

  private calcularDistanciaMetros(lat1: number, lon1: number, lat2: number, lon2: number): number {
    const rad = Math.PI / 180;
    const dLat = (lat2 - lat1) * rad;
    const dLon = (lon2 - lon1) * rad;
    const a =
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(lat1 * rad) * Math.cos(lat2 * rad) *
      Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return 6371000 * c;
  }
}

