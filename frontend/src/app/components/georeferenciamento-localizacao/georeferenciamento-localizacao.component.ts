import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { BeneficiarioApiPayload, BeneficiarioApiService } from '../../services/beneficiario-api.service';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

type TabId = 'beneficiarios';

@Component({
  selector: 'app-georeferenciamento-localizacao',
  standalone: true,
  imports: [CommonModule, FormsModule, TelaPadraoComponent],
  templateUrl: './georeferenciamento-localizacao.component.html',
  styleUrl: './georeferenciamento-localizacao.component.scss'
})
export class GeoreferenciamentoLocalizacaoComponent implements OnInit, OnDestroy {
  activeTab: TabId = 'beneficiarios';

  beneficiarios: BeneficiarioApiPayload[] = [];
  beneficiarioSelecionadoId: string | null = null;
  mapaModalOpen = false;
  mapaEnderecoUrl: SafeResourceUrl | null = null;
  mapaEnderecoLink = '';
  loteAtual = 0;
  totalLotes = 0;
  private lotesConsultas: string[][] = [];
  carregandoTabs: { beneficiarios: boolean } = {
    beneficiarios: false
  };
  erroTabs: { beneficiarios: string | null } = {
    beneficiarios: null
  };
  avisoTabs: { beneficiarios: string | null } = {
    beneficiarios: null
  };
  private readonly destroy$ = new Subject<void>();

  constructor(
    private readonly beneficiarioService: BeneficiarioApiService,
    private readonly sanitizer: DomSanitizer
  ) {}

  ngOnInit(): void {
    this.carregarBeneficiarios();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  abrirMapaBeneficiario(): void {
    if (!this.beneficiarioSelecionadoId) {
      this.avisoTabs.beneficiarios = 'Selecione um beneficiario para visualizar no Google Maps.';
      return;
    }
    if (this.beneficiarioSelecionadoId === 'TODOS') {
      this.abrirMapaTodosBeneficiarios();
      return;
    }
    const beneficiario = this.beneficiarios.find(
      (item) => String(item.id_beneficiario) === this.beneficiarioSelecionadoId
    );
    if (!beneficiario) {
      this.avisoTabs.beneficiarios = 'Beneficiario nao encontrado.';
      return;
    }
    const coordenadas = this.obterCoordenadasBeneficiario(beneficiario);
    if (coordenadas) {
      this.definirMapaEndereco(coordenadas);
      return;
    }
    const consulta = this.montarConsultaEnderecoBeneficiario(beneficiario);
    if (!consulta) {
      this.avisoTabs.beneficiarios = 'Endereco incompleto para consultar no Google Maps.';
      return;
    }
    this.definirMapaEndereco(consulta);
  }

  onBeneficiarioSelecionado(): void {
    if (!this.beneficiarioSelecionadoId) {
      return;
    }
    if (this.beneficiarioSelecionadoId === 'TODOS') {
      this.abrirMapaTodosBeneficiarios();
      return;
    }
    this.abrirMapaBeneficiario();
  }

  abrirLoteAnterior(): void {
    if (this.loteAtual <= 0) {
      return;
    }
    this.loteAtual -= 1;
    this.abrirMapaLoteAtual();
  }

  abrirProximoLote(): void {
    if (this.loteAtual >= this.totalLotes - 1) {
      return;
    }
    this.loteAtual += 1;
    this.abrirMapaLoteAtual();
  }

  fecharMapaEndereco(): void {
    this.mapaModalOpen = false;
    this.mapaEnderecoUrl = null;
    this.mapaEnderecoLink = '';
    this.loteAtual = 0;
    this.totalLotes = 0;
    this.lotesConsultas = [];
  }

  private carregarBeneficiarios(): void {
    this.carregandoTabs.beneficiarios = true;
    this.erroTabs.beneficiarios = null;
    this.avisoTabs.beneficiarios = null;

    this.beneficiarioService
      .list()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: { beneficiarios?: BeneficiarioApiPayload[] }) => {
          this.beneficiarios = response.beneficiarios ?? [];
          if (!this.beneficiarioSelecionadoId && this.beneficiarios.length) {
            this.beneficiarioSelecionadoId = String(this.beneficiarios[0].id_beneficiario ?? '');
            this.abrirMapaBeneficiario();
          }
        },
        error: () => {
          this.erroTabs.beneficiarios = 'Nao foi possivel carregar os beneficiarios.';
        },
        complete: () => {
          this.carregandoTabs.beneficiarios = false;
        }
      });
  }

  formatarEndereco(beneficiario: BeneficiarioApiPayload): string {
    return [beneficiario.logradouro, beneficiario.numero, beneficiario.bairro, beneficiario.municipio, beneficiario.uf]
      .filter(Boolean)
      .join(', ');
  }

  private obterCoordenadasBeneficiario(beneficiario: BeneficiarioApiPayload): string | null {
    const latitude = (beneficiario.latitude ?? '').toString().trim();
    const longitude = (beneficiario.longitude ?? '').toString().trim();
    if (!latitude || !longitude) {
      return null;
    }
    return `${latitude},${longitude}`;
  }

  private montarConsultaEnderecoBeneficiario(beneficiario: BeneficiarioApiPayload): string | null {
    const partes = [
      beneficiario.logradouro,
      beneficiario.numero,
      beneficiario.bairro,
      beneficiario.municipio,
      beneficiario.uf,
      (beneficiario as { cep?: string | null })?.cep,
    ]
      .map((valor) => (valor ?? '').toString().trim())
      .filter((valor) => valor);
    return partes.length ? partes.join(', ') : null;
  }

  private abrirMapaTodosBeneficiarios(): void {
    const consultas = this.beneficiarios
      .map((beneficiario) => this.formatarConsultaBeneficiario(beneficiario))
      .filter((consulta): consulta is string => Boolean(consulta));

    if (!consultas.length) {
      this.avisoTabs.beneficiarios = 'Nenhum endereco valido para exibir no Google Maps.';
      return;
    }

    if (consultas.length === 1) {
      this.definirMapaEndereco(consultas[0]);
      return;
    }

    const limiteWaypoints = 23;
    const tamanhoLote = limiteWaypoints + 2;
    const lotes: string[][] = [];
    for (let i = 0; i < consultas.length; i += tamanhoLote) {
      lotes.push(consultas.slice(i, i + tamanhoLote));
    }
    this.lotesConsultas = lotes;
    this.totalLotes = lotes.length;
    this.loteAtual = 0;
    if (this.totalLotes > 1) {
      this.avisoTabs.beneficiarios =
        'O Google Maps tem limite de pontos por rota. Use os botoes para alternar entre as partes.';
    }
    this.abrirMapaLoteAtual();
  }

  private abrirMapaLoteAtual(): void {
    if (!this.lotesConsultas.length) {
      return;
    }
    const consultasUsadas = this.lotesConsultas[this.loteAtual] ?? [];
    if (consultasUsadas.length === 1) {
      this.definirMapaEndereco(consultasUsadas[0]);
      return;
    }
    const [origem, ...resto] = consultasUsadas;
    const destino = resto.pop() ?? origem;
    const waypoints = resto;
    const link = this.montarLinkRotas(origem, destino, waypoints);
    this.mapaEnderecoLink = link;
    this.mapaEnderecoUrl = this.sanitizer.bypassSecurityTrustResourceUrl(`${link}&output=embed`);
    this.mapaModalOpen = true;
  }

  private formatarConsultaBeneficiario(beneficiario: BeneficiarioApiPayload): string | null {
    const coordenadas = this.obterCoordenadasBeneficiario(beneficiario);
    if (coordenadas) {
      return coordenadas;
    }
    return this.montarConsultaEnderecoBeneficiario(beneficiario);
  }

  private montarLinkRotas(origem: string, destino: string, waypoints: string[]): string {
    const origemEncoded = encodeURIComponent(origem);
    const destinoEncoded = encodeURIComponent(destino);
    const base = `https://www.google.com/maps/dir/?api=1&origin=${origemEncoded}&destination=${destinoEncoded}`;
    if (!waypoints.length) {
      return base;
    }
    const waypointsEncoded = waypoints.map((ponto) => encodeURIComponent(ponto)).join('|');
    return `${base}&waypoints=${waypointsEncoded}`;
  }

  private definirMapaEndereco(consulta: string): void {
    const query = encodeURIComponent(consulta);
    const link = `https://www.google.com/maps?q=${query}`;
    this.mapaEnderecoLink = link;
    this.mapaEnderecoUrl = this.sanitizer.bypassSecurityTrustResourceUrl(`${link}&output=embed`);
    this.mapaModalOpen = true;
  }

}
