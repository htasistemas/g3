import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { PopupErrorBuilder } from '../../utils/popup-error.builder';
import { finalize, forkJoin } from 'rxjs';
import {
  ControleVeiculosService,
  RegistroDiarioBordo,
  RegistroDiarioBordoEntrada,
  VeiculoCadastro,
  VeiculoCadastroEntrada
} from '../../services/controle-veiculos.service';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { ConfigAcoesCrud, EstadoAcoesCrud, TelaBaseComponent } from '../compartilhado/tela-base.component';
import { DialogComponent } from '../compartilhado/dialog/dialog.component';

type AbaControleVeiculos = 'cadastro' | 'diario';

@Component({
  selector: 'app-controle-veiculos',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    TelaPadraoComponent,
    PopupMessagesComponent,
    DialogComponent
  ],
  templateUrl: './controle-veiculos.component.html',
  styleUrl: './controle-veiculos.component.scss'
})
export class ControleVeiculosComponent extends TelaBaseComponent implements OnInit {
  readonly abas: { id: AbaControleVeiculos; label: string; descricao: string }[] = [
    {
      id: 'cadastro',
      label: 'Cadastro de veiculos',
      descricao: 'Gerencie dados, condicao e status dos veiculos da frota.'
    },
    {
      id: 'diario',
      label: 'Mapa de bordo',
      descricao: 'Registre viagens e acompanhe km rodados e consumo.'
    }
  ];

  abaAtiva: AbaControleVeiculos = 'cadastro';

  readonly formularioVeiculo: FormGroup;
  readonly formularioDiario: FormGroup;

  veiculos: VeiculoCadastro[] = [];
  registros: RegistroDiarioBordo[] = [];

  veiculoSelecionadoId: number | null = null;
  registroSelecionadoId: number | null = null;

  popupErros: string[] = [];
  mensagemFeedback: string | null = null;
  carregando = false;

  dialogoExclusaoAberto = false;
  dialogoTitulo = '';
  dialogoMensagem = '';
  dialogoConfirmarLabel = 'Excluir';
  tipoExclusao: 'veiculo' | 'registro' | null = null;

  readonly acoesToolbar: Required<ConfigAcoesCrud> = this.criarConfigAcoes({
    buscar: true,
    novo: true,
    salvar: true,
    cancelar: true,
    excluir: true,
    imprimir: true
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly controleVeiculosService: ControleVeiculosService
  ) {
    super();
    const hojeIso = new Date().toISOString().substring(0, 10);

    this.formularioVeiculo = this.fb.group({
      placa: ['', Validators.required],
      modelo: ['', Validators.required],
      marca: ['', Validators.required],
      ano: [new Date().getFullYear(), [Validators.required, Validators.min(1900)]],
      tipoCombustivel: ['', Validators.required],
      mediaConsumoPadrao: [null, [Validators.required, Validators.min(0.1)]],
      capacidadeTanqueLitros: [null],
      observacoes: [''],
      ativo: [true]
    });

    this.formularioDiario = this.fb.group({
      veiculoId: [null, Validators.required],
      data: [hojeIso, Validators.required],
      condutor: ['', Validators.required],
      horarioSaida: ['', Validators.required],
      kmInicial: [0, [Validators.required, Validators.min(0)]],
      horarioChegada: ['', Validators.required],
      kmFinal: [0, [Validators.required, Validators.min(0)]],
      destino: ['', Validators.required],
      observacoes: ['']
    });
  }

  ngOnInit(): void {
    this.carregarDados();
  }

  get acoesDesabilitadas(): EstadoAcoesCrud {
    const veiculoValido = this.formularioVeiculo.valid;
    const diarioValido = this.formularioDiario.valid && this.kmRodadosCalculado > 0;
    return {
      salvar: this.carregando || (this.abaAtiva === 'cadastro' ? !veiculoValido : !diarioValido),
      excluir: this.carregando || (this.abaAtiva === 'cadastro' ? !this.veiculoSelecionadoId : !this.registroSelecionadoId),
      novo: this.carregando,
      cancelar: this.carregando,
      imprimir: this.carregando,
      buscar: this.carregando
    };
  }

  mudarAba(tabId: AbaControleVeiculos): void {
    this.abaAtiva = tabId;
    this.popupErros = [];
    this.mensagemFeedback = null;
    this.carregarDados();
  }

  get indiceAbaAtiva(): number {
    return this.abas.findIndex((tab) => tab.id === this.abaAtiva);
  }

  selecionarVeiculo(veiculo: VeiculoCadastro): void {
    this.veiculoSelecionadoId = veiculo.id;
    this.formularioVeiculo.reset({
      placa: veiculo.placa,
      modelo: veiculo.modelo,
      marca: veiculo.marca,
      ano: veiculo.ano,
      tipoCombustivel: veiculo.tipoCombustivel,
      mediaConsumoPadrao: veiculo.mediaConsumoPadrao ?? null,
      capacidadeTanqueLitros: veiculo.capacidadeTanqueLitros ?? null,
      observacoes: veiculo.observacoes ?? '',
      ativo: veiculo.ativo
    });
    this.abaAtiva = 'cadastro';
  }

  selecionarRegistro(registro: RegistroDiarioBordo): void {
    this.registroSelecionadoId = registro.id;
    this.formularioDiario.reset({
      veiculoId: registro.veiculoId,
      data: registro.data,
      condutor: registro.condutor,
      horarioSaida: registro.horarioSaida,
      kmInicial: registro.kmInicial,
      horarioChegada: registro.horarioChegada,
      kmFinal: registro.kmFinal,
      destino: registro.destino,
      observacoes: registro.observacoes ?? ''
    });
    this.abaAtiva = 'diario';
  }

  salvar(): void {
    this.popupErros = [];
    this.mensagemFeedback = null;
    if (this.abaAtiva === 'cadastro') {
      this.salvarVeiculo();
      return;
    }
    this.salvarDiario();
  }

  novo(): void {
    if (this.abaAtiva === 'cadastro') {
      this.limparFormularioVeiculo();
      return;
    }
    this.limparFormularioDiario();
  }

  cancelar(): void {
    if (this.abaAtiva === 'cadastro') {
      this.limparFormularioVeiculo();
      return;
    }
    this.limparFormularioDiario();
  }

  buscar(): void {
    this.popupErros = [];
    this.mensagemFeedback = 'Dados atualizados automaticamente.';
  }

  excluir(): void {
    this.popupErros = [];
    if (this.abaAtiva === 'cadastro') {
      if (!this.veiculoSelecionadoId) {
        this.popupErros = new PopupErrorBuilder()
          .adicionar('Selecione um veiculo para excluir.')
          .build();
        return;
      }
      this.tipoExclusao = 'veiculo';
      this.dialogoTitulo = 'Excluir veiculo';
      this.dialogoMensagem =
        'Deseja excluir este veiculo? Todos os registros de bordo vinculados serao removidos.';
      this.dialogoExclusaoAberto = true;
      return;
    }

    if (!this.registroSelecionadoId) {
      this.popupErros = new PopupErrorBuilder()
        .adicionar('Selecione um registro de bordo para excluir.')
        .build();
      return;
    }
    this.tipoExclusao = 'registro';
    this.dialogoTitulo = 'Excluir registro';
    this.dialogoMensagem = 'Deseja excluir este registro do diario de bordo?';
    this.dialogoExclusaoAberto = true;
  }

  imprimir(): void {
    this.popupErros = new PopupErrorBuilder()
      .adicionar('Funcao de impressao ainda nao disponivel para o controle de veiculos.')
      .build();
  }

  fechar(): void {
    window.history.back();
  }

  confirmarExclusao(): void {
    if (this.tipoExclusao === 'veiculo' && this.veiculoSelecionadoId) {
      this.carregando = true;
      this.controleVeiculosService
        .removerVeiculo(this.veiculoSelecionadoId)
        .pipe(finalize(() => (this.carregando = false)))
        .subscribe({
          next: () => {
            this.limparFormularioVeiculo();
            this.mensagemFeedback = 'Veiculo removido com sucesso.';
            this.carregarDados();
          },
          error: () => {
            this.popupErros = new PopupErrorBuilder()
              .adicionar('Nao foi possivel excluir o veiculo.')
              .build();
          }
        });
    }

    if (this.tipoExclusao === 'registro' && this.registroSelecionadoId) {
      this.carregando = true;
      this.controleVeiculosService
        .removerRegistro(this.registroSelecionadoId)
        .pipe(finalize(() => (this.carregando = false)))
        .subscribe({
          next: () => {
            this.limparFormularioDiario();
            this.mensagemFeedback = 'Registro removido com sucesso.';
            this.carregarDados();
          },
          error: () => {
            this.popupErros = new PopupErrorBuilder()
              .adicionar('Nao foi possivel excluir o registro.')
              .build();
          }
        });
    }

    this.fecharDialogoExclusao();
  }

  cancelarExclusao(): void {
    this.fecharDialogoExclusao();
  }

  fecharPopup(): void {
    this.popupErros = [];
  }

  get kmRodadosCalculado(): number {
    const kmInicial = Number(this.formularioDiario.get('kmInicial')?.value || 0);
    const kmFinal = Number(this.formularioDiario.get('kmFinal')?.value || 0);
    const distancia = kmFinal - kmInicial;
    return Number.isFinite(distancia) && distancia > 0 ? distancia : 0;
  }

  get mediaConsumoCalculada(): number {
    const veiculoId = Number(this.formularioDiario.get('veiculoId')?.value || 0);
    const veiculo = this.veiculos.find((item) => item.id === veiculoId);
    const mediaPadrao = Number(veiculo?.mediaConsumoPadrao || 0);
    return mediaPadrao > 0 ? mediaPadrao : 0;
  }

  get combustivelConsumidoCalculado(): number {
    const media = this.mediaConsumoCalculada;
    if (media <= 0) {
      return 0;
    }
    return this.kmRodadosCalculado / media;
  }

  obterDescricaoVeiculo(veiculoId: number | string): string {
    const veiculoIdNumero = Number(veiculoId);
    const veiculo = this.veiculos.find((item) => item.id === veiculoIdNumero);
    if (!veiculo) {
      return 'Veiculo nao encontrado';
    }
    return `${veiculo.placa} - ${veiculo.modelo}`;
  }

  formatarNumero(valor: number, casas = 1): string {
    if (!Number.isFinite(valor) || valor === 0) {
      return '--';
    }
    return valor.toFixed(casas).replace('.', ',');
  }

  private salvarVeiculo(): void {
    const erros = this.validarFormularioVeiculo();
    if (erros.length) {
      const builder = new PopupErrorBuilder();
      erros.forEach((erro) => builder.adicionar(erro));
      this.popupErros = builder.build();
      this.formularioVeiculo.markAllAsTouched();
      return;
    }

    const placa = String(this.formularioVeiculo.get('placa')?.value || '').trim();
    const mediaConsumoPadrao = Number(
      this.formularioVeiculo.get('mediaConsumoPadrao')?.value || 0
    );
    if (mediaConsumoPadrao <= 0) {
      this.popupErros = new PopupErrorBuilder()
        .adicionar('Informe a media de consumo padrao do veiculo.')
        .build();
      return;
    }
    const duplicado = this.veiculos.find(
      (item) =>
        item.placa.toLowerCase() === placa.toLowerCase() &&
        item.id !== this.veiculoSelecionadoId
    );
    if (duplicado) {
      this.popupErros = new PopupErrorBuilder()
        .adicionar('Ja existe um veiculo cadastrado com esta placa.')
        .build();
      return;
    }

    const dadosVeiculo: VeiculoCadastroEntrada = {
      placa,
      modelo: this.formularioVeiculo.get('modelo')?.value,
      marca: this.formularioVeiculo.get('marca')?.value,
      ano: Number(this.formularioVeiculo.get('ano')?.value || 0),
      tipoCombustivel: this.formularioVeiculo.get('tipoCombustivel')?.value,
      mediaConsumoPadrao: mediaConsumoPadrao,
      capacidadeTanqueLitros: this.formularioVeiculo.get('capacidadeTanqueLitros')?.value ?? null,
      observacoes: this.formularioVeiculo.get('observacoes')?.value || undefined,
      ativo: !!this.formularioVeiculo.get('ativo')?.value
    };

    this.carregando = true;
    const requisicao = this.veiculoSelecionadoId
      ? this.controleVeiculosService.atualizarVeiculo(this.veiculoSelecionadoId, dadosVeiculo)
      : this.controleVeiculosService.criarVeiculo(dadosVeiculo);

    requisicao.pipe(finalize(() => (this.carregando = false))).subscribe({
      next: (veiculo) => {
        this.veiculoSelecionadoId = veiculo.id;
        this.mensagemFeedback = 'Veiculo salvo com sucesso.';
        this.carregarDados();
      },
      error: (erro) => {
        const mensagem =
          erro?.error?.message || 'Nao foi possivel salvar o veiculo. Verifique os dados.';
        this.popupErros = new PopupErrorBuilder().adicionar(mensagem).build();
      }
    });
  }

  private salvarDiario(): void {
    const erros = this.validarFormularioDiario();
    if (erros.length) {
      const builder = new PopupErrorBuilder();
      erros.forEach((erro) => builder.adicionar(erro));
      this.popupErros = builder.build();
      this.formularioDiario.markAllAsTouched();
      return;
    }

    const dadosDiario: RegistroDiarioBordoEntrada = {
      veiculoId: Number(this.formularioDiario.get('veiculoId')?.value || 0),
      data: this.formularioDiario.get('data')?.value,
      condutor: this.formularioDiario.get('condutor')?.value,
      horarioSaida: this.formularioDiario.get('horarioSaida')?.value,
      kmInicial: Number(this.formularioDiario.get('kmInicial')?.value || 0),
      horarioChegada: this.formularioDiario.get('horarioChegada')?.value,
      kmFinal: Number(this.formularioDiario.get('kmFinal')?.value || 0),
      destino: this.formularioDiario.get('destino')?.value,
      observacoes: this.formularioDiario.get('observacoes')?.value || undefined
    };

    this.carregando = true;
    const requisicao = this.registroSelecionadoId
      ? this.controleVeiculosService.atualizarRegistro(this.registroSelecionadoId, dadosDiario)
      : this.controleVeiculosService.criarRegistro(dadosDiario);

    requisicao.pipe(finalize(() => (this.carregando = false))).subscribe({
      next: (registro) => {
        this.registroSelecionadoId = registro.id;
        this.mensagemFeedback = 'Registro de bordo salvo com sucesso.';
        this.carregarDados();
      },
      error: (erro) => {
        const mensagem =
          erro?.error?.message || 'Nao foi possivel salvar o registro de bordo.';
        this.popupErros = new PopupErrorBuilder().adicionar(mensagem).build();
      }
    });
  }

  private limparFormularioVeiculo(): void {
    this.veiculoSelecionadoId = null;
    this.formularioVeiculo.reset({
      placa: '',
      modelo: '',
      marca: '',
      ano: new Date().getFullYear(),
      tipoCombustivel: '',
      mediaConsumoPadrao: null,
      capacidadeTanqueLitros: null,
      observacoes: '',
      ativo: true
    });
  }

  private limparFormularioDiario(): void {
    const hojeIso = new Date().toISOString().substring(0, 10);
    this.registroSelecionadoId = null;
    this.formularioDiario.reset({
      veiculoId: null,
      data: hojeIso,
      condutor: '',
      horarioSaida: '',
      kmInicial: 0,
      horarioChegada: '',
      kmFinal: 0,
      destino: '',
      observacoes: ''
    });
  }

  private validarFormularioVeiculo(): string[] {
    const erros: string[] = [];
    const camposObrigatorios = [
      'placa',
      'modelo',
      'marca',
      'ano',
      'tipoCombustivel',
      'mediaConsumoPadrao'
    ];
    camposObrigatorios.forEach((campo) => {
      const valor = this.formularioVeiculo.get(campo)?.value;
      if (valor === null || valor === undefined || String(valor).trim() === '') {
        erros.push(`Informe o campo obrigatorio: ${this.traduzirCampoVeiculo(campo)}.`);
      }
    });
    return erros;
  }

  private validarFormularioDiario(): string[] {
    const erros: string[] = [];
    const camposObrigatorios = [
      'veiculoId',
      'data',
      'condutor',
      'horarioSaida',
      'kmInicial',
      'horarioChegada',
      'kmFinal',
      'destino'
    ];
    camposObrigatorios.forEach((campo) => {
      const valor = this.formularioDiario.get(campo)?.value;
      if (valor === null || valor === undefined || String(valor).trim() === '') {
        erros.push(`Informe o campo obrigatorio: ${this.traduzirCampoDiario(campo)}.`);
      }
    });

    if (this.kmRodadosCalculado <= 0) {
      erros.push('O km final deve ser maior que o km inicial.');
    }
    return erros;
  }

  private traduzirCampoVeiculo(campo: string): string {
    const mapa: Record<string, string> = {
      placa: 'Placa',
      modelo: 'Modelo',
      marca: 'Marca',
      ano: 'Ano',
      tipoCombustivel: 'Tipo de combustivel',
      mediaConsumoPadrao: 'Media de consumo'
    };
    return mapa[campo] || campo;
  }

  private traduzirCampoDiario(campo: string): string {
    const mapa: Record<string, string> = {
      veiculoId: 'Veiculo',
      data: 'Data',
      condutor: 'Condutor',
      horarioSaida: 'Horario de saida',
      kmInicial: 'Km inicial',
      horarioChegada: 'Horario de chegada',
      kmFinal: 'Km final',
      destino: 'Destino',
      combustivelConsumidoLitros: 'Combustivel consumido'
    };
    return mapa[campo] || campo;
  }

  private validarSelecoesAposAtualizar(): void {
    if (this.veiculoSelecionadoId) {
      const existe = this.veiculos.some((item) => item.id === this.veiculoSelecionadoId);
      if (!existe) {
        this.veiculoSelecionadoId = null;
      }
    }
    if (this.registroSelecionadoId) {
      const existe = this.registros.some((item) => item.id === this.registroSelecionadoId);
      if (!existe) {
        this.registroSelecionadoId = null;
      }
    }
  }

  private carregarDados(): void {
    this.carregando = true;
    forkJoin({
      veiculos: this.controleVeiculosService.listarVeiculos(),
      registros: this.controleVeiculosService.listarRegistros()
    })
      .pipe(finalize(() => (this.carregando = false)))
      .subscribe({
        next: ({ veiculos, registros }) => {
          this.veiculos = veiculos;
          this.registros = registros;
          this.validarSelecoesAposAtualizar();
        },
        error: () => {
          this.popupErros = new PopupErrorBuilder()
            .adicionar('Nao foi possivel carregar o controle de veiculos.')
            .build();
        }
      });
  }

  private fecharDialogoExclusao(): void {
    this.dialogoExclusaoAberto = false;
    this.tipoExclusao = null;
  }
}
