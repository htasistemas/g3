import { CommonModule } from '@angular/common';
import {
  AfterViewInit,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
} from '@angular/core';
import { from, of } from 'rxjs';
import { catchError, concatMap, finalize } from 'rxjs/operators';
import {
  BeneficiarioApiPayload,
  BeneficiarioApiService,
} from '../../services/beneficiario-api.service';
import { PopupErrorBuilder } from '../../utils/popup-error.builder';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import {
  ConfigAcoesCrud,
  EstadoAcoesCrud,
  TelaBaseComponent,
} from '../compartilhado/tela-base.component';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';

interface CoordenadasMapa {
  latitude: number;
  longitude: number;
}

interface BeneficiarioMapa {
  id?: string;
  nome: string;
  endereco: string;
  logradouro?: string;
  numero?: string;
  municipio?: string;
  uf?: string;
  coordenadas?: CoordenadasMapa;
}

interface LeafletMapa {
  setView(center: [number, number], zoom: number): void;
  fitBounds(bounds: LeafletBounds, options?: { padding?: [number, number] }): void;
  invalidateSize(): void;
  remove(): void;
}

interface LeafletMarker {
  bindPopup(conteudo: string): LeafletMarker;
  addTo(map: LeafletMapa): LeafletMarker;
  remove(): void;
  openPopup(): void;
}

interface LeafletLayer {
  addTo(map: LeafletMapa): LeafletLayer;
}

interface LeafletBounds {}

declare const L: {
  map(container: HTMLElement, options?: { zoomControl?: boolean }): LeafletMapa;
  tileLayer(url: string, options: { attribution: string; maxZoom: number }): LeafletLayer;
  marker(coords: [number, number]): LeafletMarker;
  latLngBounds(coords: [number, number][]): LeafletBounds;
};

@Component({
  selector: 'app-mapa-beneficiarios',
  standalone: true,
  imports: [CommonModule, TelaPadraoComponent, PopupMessagesComponent],
  templateUrl: './mapa-beneficiarios.component.html',
  styleUrl: './mapa-beneficiarios.component.scss',
})
export class MapaBeneficiariosComponent
  extends TelaBaseComponent
  implements OnInit, AfterViewInit, OnDestroy
{
  readonly acoesToolbar: Required<ConfigAcoesCrud> = this.criarConfigAcoes({
    buscar: true,
    imprimir: true,
    cancelar: false,
    excluir: false,
    novo: false,
    salvar: false,
  });

  popupErros: string[] = [];
  beneficiarios: BeneficiarioMapa[] = [];
  carregando = false;
  mapaMensagem = 'Nenhuma localizacao disponivel para exibir no mapa.';
  geocodificadosNaSessao = 0;
  geocodificacaoFalhaNaSessao = 0;
  beneficiariosSemId = 0;
  beneficiariosEnderecoIncompleto = 0;
  @ViewChild('mapaContainer') mapaContainer?: ElementRef<HTMLDivElement>;
  private mapa: LeafletMapa | null = null;
  private marcadores: LeafletMarker[] = [];
  private geocodificando = false;

  constructor(
    private readonly beneficiarioService: BeneficiarioApiService,
  ) {
    super();
  }

  ngOnInit(): void {
    this.carregarBeneficiarios();
  }

  ngAfterViewInit(): void {
    this.inicializarMapa();
    this.atualizarMapaCompleto();
  }

  ngOnDestroy(): void {
    this.marcadores.forEach((marcador) => marcador.remove());
    this.marcadores = [];
    this.mapa?.remove();
    this.mapa = null;
  }

  get acoesDesabilitadas(): EstadoAcoesCrud {
    return {
      buscar: this.carregando,
      imprimir: this.carregando,
      cancelar: true,
      excluir: true,
      novo: true,
      salvar: true,
    };
  }

  get geocodificandoAtivo(): boolean {
    return this.geocodificando;
  }

  get totalBeneficiarios(): number {
    return this.beneficiarios.length;
  }

  get totalComCoordenadas(): number {
    return this.beneficiarios.filter((beneficiario) => Boolean(beneficiario.coordenadas)).length;
  }

  get totalSemCoordenadas(): number {
    return this.totalBeneficiarios - this.totalComCoordenadas;
  }

  carregarBeneficiarios(): void {
    this.popupErros = [];
    this.carregando = true;
    this.geocodificadosNaSessao = 0;
    this.geocodificacaoFalhaNaSessao = 0;
    this.beneficiariosSemId = 0;
    this.beneficiariosEnderecoIncompleto = 0;
    this.beneficiarioService
      .list()
      .pipe(finalize(() => (this.carregando = false)))
      .subscribe({
        next: ({ beneficiarios }) => {
          this.beneficiarios = (beneficiarios ?? [])
            .map((beneficiario) => this.mapearBeneficiario(beneficiario))
            .sort((a, b) =>
              a.nome.localeCompare(b.nome, 'pt-BR', { sensitivity: 'base' }),
            );
          this.beneficiariosSemId = this.beneficiarios.filter((item) => !item.id).length;
          this.beneficiariosEnderecoIncompleto = this.beneficiarios.filter(
            (item) => !this.enderecoCompleto(item),
          ).length;
          this.atualizarMapaCompleto();
          this.atualizarCoordenadasPendentes(true);
          this.notificarEnderecoIncompleto();
        },
        error: () => {
          const builder = new PopupErrorBuilder();
          builder.adicionar('Nao foi possivel carregar os beneficiarios do mapa.');
          this.popupErros = builder.build();
          this.beneficiarios = [];
          this.limparMapa('Nao foi possivel carregar os dados do mapa.');
        },
      });
  }

  imprimirMapa(): void {
    window.print();
  }

  fecharTela(): void {
    window.history.back();
  }

  fecharPopup(): void {
    this.popupErros = [];
  }

  forcarGeocodificacao(): void {
    this.atualizarCoordenadasPendentes(true);
  }

  private atualizarMapaCompleto(): void {
    if (!this.mapa) {
      this.inicializarMapa();
    }
    if (!this.mapa) return;
    this.marcadores.forEach((marcador) => marcador.remove());
    this.marcadores = [];
    const coordenadas = this.beneficiarios
      .map((beneficiario) => beneficiario.coordenadas)
      .filter((item): item is CoordenadasMapa => Boolean(item));
    if (!coordenadas.length) {
      this.limparMapa('Nenhuma localizacao disponivel para exibir no mapa.');
      return;
    }
    const bounds = L.latLngBounds(
      coordenadas.map((coord) => [coord.latitude, coord.longitude]),
    );
    this.mapa.fitBounds(bounds, { padding: [24, 24] });
    this.mapaMensagem = '';
    this.beneficiarios.forEach((beneficiario) => {
      if (!beneficiario.coordenadas) return;
      const marcador = L.marker([
        beneficiario.coordenadas.latitude,
        beneficiario.coordenadas.longitude,
      ])
        .bindPopup(
          `<strong>${beneficiario.nome}</strong><br />${beneficiario.endereco}`,
        )
        .addTo(this.mapa as LeafletMapa);
      this.marcadores.push(marcador);
    });
    this.ajustarTamanhoMapa();
  }

  private atualizarBeneficiarioNaLista(
    beneficiarioAtualizado: BeneficiarioMapa,
    atualizarMapa = true,
  ): void {
    const index = this.beneficiarios.findIndex(
      (beneficiario) => beneficiario.id === beneficiarioAtualizado.id,
    );
    if (index >= 0) {
      this.beneficiarios[index] = beneficiarioAtualizado;
    }
    if (atualizarMapa) {
      this.atualizarMapaCompleto();
    }
  }

  private limparMapa(mensagem: string): void {
    this.mapaMensagem = mensagem;
  }

  private mapearBeneficiario(beneficiario: BeneficiarioApiPayload): BeneficiarioMapa {
    const nome = (beneficiario.nome_completo || beneficiario.nome_social || 'Sem nome')
      .toString()
      .trim();
    const endereco = this.montarEndereco(beneficiario);
    const coordenadas = this.extrairCoordenadas(beneficiario);
    return {
      id: beneficiario.id_beneficiario,
      nome: nome || 'Sem nome',
      endereco,
      logradouro: (beneficiario.logradouro ?? '').toString().trim(),
      numero: (beneficiario.numero ?? '').toString().trim(),
      municipio: (beneficiario.municipio ?? '').toString().trim(),
      uf: (beneficiario.uf ?? '').toString().trim(),
      coordenadas,
    };
  }

  private montarEndereco(beneficiario: BeneficiarioApiPayload): string {
    const partes = [
      beneficiario.logradouro,
      beneficiario.numero,
      beneficiario.bairro,
      beneficiario.municipio,
      beneficiario.uf,
      beneficiario.cep,
    ]
      .map((valor) => (valor ?? '').toString().trim())
      .filter((valor) => valor);
    return partes.length ? partes.join(', ') : 'Endereco nao informado';
  }

  private extrairCoordenadas(
    beneficiario: BeneficiarioApiPayload,
  ): CoordenadasMapa | undefined {
    const latitude = Number.parseFloat(
      (beneficiario.latitude ?? '').toString().replace(',', '.'),
    );
    const longitude = Number.parseFloat(
      (beneficiario.longitude ?? '').toString().replace(',', '.'),
    );
    if (!Number.isFinite(latitude) || !Number.isFinite(longitude)) {
      return undefined;
    }
    return { latitude, longitude };
  }

  private inicializarMapa(): void {
    if (typeof L === 'undefined') {
      const builder = new PopupErrorBuilder();
      builder.adicionar('Biblioteca de mapa nao carregada. Atualize a pagina.');
      this.popupErros = builder.build();
      this.limparMapa('Nao foi possivel carregar o mapa.');
      return;
    }
    if (!this.mapaContainer?.nativeElement || this.mapa) return;
    this.mapa = L.map(this.mapaContainer.nativeElement, { zoomControl: true });
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '(c) OpenStreetMap contributors',
      maxZoom: 19,
    }).addTo(this.mapa);
    this.mapa.setView([-19.9191, -43.9386], 11);
    this.ajustarTamanhoMapa();
  }

  private ajustarTamanhoMapa(): void {
    if (!this.mapa) return;
    window.requestAnimationFrame(() => {
      this.mapa?.invalidateSize();
    });
  }

  private atualizarCoordenadasPendentes(forcar = false): void {
    if (this.geocodificando) return;
    const pendentes = this.beneficiarios.filter(
      (beneficiario) =>
        beneficiario.id &&
        !beneficiario.coordenadas &&
        this.enderecoCompleto(beneficiario),
    );
    if (!pendentes.length && this.beneficiariosSemId > 0) {
      this.geocodificacaoFalhaNaSessao += this.beneficiariosSemId;
    }
    if (!pendentes.length) return;
    this.geocodificando = true;
    from(pendentes)
      .pipe(
        concatMap((beneficiario) =>
          this.beneficiarioService.geocodificarEndereco(beneficiario.id ?? '', forcar).pipe(
            catchError(() => of(null)),
          ),
        ),
        finalize(() => {
          this.geocodificando = false;
          this.atualizarMapaCompleto();
        }),
      )
      .subscribe((resposta) => {
        const beneficiario = resposta?.beneficiario;
        if (!beneficiario) {
          this.geocodificacaoFalhaNaSessao += 1;
          return;
        }
        const atualizado = this.mapearBeneficiario(beneficiario);
        if (atualizado.coordenadas) {
          this.geocodificadosNaSessao += 1;
        } else {
          this.geocodificacaoFalhaNaSessao += 1;
        }
        this.atualizarBeneficiarioNaLista(atualizado, false);
      });
  }

  private notificarEnderecoIncompleto(): void {
    if (this.totalComCoordenadas > 0) return;
    if (this.beneficiariosEnderecoIncompleto <= 0) return;
    const builder = new PopupErrorBuilder();
    builder.adicionar('Existem beneficiarios com endereco incompleto.');
    builder.adicionar('Preencha logradouro, numero, municipio e UF para geocodificar.');
    this.popupErros = builder.build();
  }

  private enderecoCompleto(beneficiario: BeneficiarioMapa): boolean {
    const logradouro = (beneficiario.logradouro ?? '').trim();
    const numero = (beneficiario.numero ?? '').trim();
    const municipio = (beneficiario.municipio ?? '').trim();
    const uf = (beneficiario.uf ?? '').trim();
    return Boolean(logradouro && numero && municipio && uf);
  }
}
