import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { BeneficiarioApiPayload, BeneficiarioApiService } from '../../services/beneficiario-api.service';
import { ProfessionalRecord, ProfessionalService } from '../../services/professional.service';
import { DoadorResponse, RecebimentoDoacaoService } from '../../services/recebimento-doacao.service';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';

declare const L: any;

type TabId = 'beneficiarios' | 'profissionais' | 'doadores';

@Component({
  selector: 'app-georeferenciamento-localizacao',
  standalone: true,
  imports: [CommonModule, TelaPadraoComponent],
  templateUrl: './georeferenciamento-localizacao.component.html',
  styleUrl: './georeferenciamento-localizacao.component.scss'
})
export class GeoreferenciamentoLocalizacaoComponent implements OnInit, OnDestroy {
  readonly tabs: Array<{ id: TabId; label: string }> = [
    { id: 'beneficiarios', label: 'Localizacao de Beneficiarios' },
    { id: 'profissionais', label: 'Localizacao de Profissionais' },
    { id: 'doadores', label: 'Localizacao de Doadores' }
  ];
  activeTab: TabId = 'beneficiarios';

  beneficiarios: BeneficiarioApiPayload[] = [];
  profissionais: ProfessionalRecord[] = [];
  doadores: DoadorResponse[] = [];
  carregandoTabs: { beneficiarios: boolean; profissionais: boolean; doadores: boolean } = {
    beneficiarios: false,
    profissionais: false,
    doadores: false
  };
  erroTabs: { beneficiarios: string | null; profissionais: string | null; doadores: string | null } = {
    beneficiarios: null,
    profissionais: null,
    doadores: null
  };
  avisoTabs: { beneficiarios: string | null; profissionais: string | null; doadores: string | null } = {
    beneficiarios: null,
    profissionais: null,
    doadores: null
  };
  private mapInstances: Record<string, any> = {};
  private markersByTab: Record<string, any[]> = {
    beneficiarios: [],
    profissionais: [],
    doadores: []
  };
  private coordenadasProfissionais = new Map<string, { lat: number; lng: number }>();
  private coordenadasDoadores = new Map<string, { lat: number; lng: number }>();
  private coordenadasBeneficiarios = new Map<string, { lat: number; lng: number }>();
  private geocodeCache = new Map<string, { lat: number; lng: number } | null>();
  private readonly destroy$ = new Subject<void>();

  constructor(
    private readonly beneficiarioService: BeneficiarioApiService,
    private readonly professionalService: ProfessionalService,
    private readonly recebimentoDoacaoService: RecebimentoDoacaoService
  ) {}

  ngOnInit(): void {
    this.loadLeaflet()
      .then(() => {
        this.initMap('beneficiarios');
        this.carregarBeneficiarios();
      })
      .catch(() => {
        this.erroTabs.beneficiarios = 'Nao foi possivel carregar o mapa.';
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    Object.values(this.mapInstances).forEach((map) => map?.remove());
  }

  changeTab(tabId: TabId): void {
    this.activeTab = tabId;
    if (tabId === 'beneficiarios') {
      this.initMap(tabId);
      this.carregarBeneficiarios();
      return;
    }
    if (tabId === 'profissionais') {
      this.initMap(tabId);
      this.carregarProfissionais();
      return;
    }
    if (tabId === 'doadores') {
      this.initMap(tabId);
      this.carregarDoadores();
    }
  }

  mapId(tabId: TabId): string {
    return `${tabId}-map`;
  }

  private initMap(tabId: TabId): void {
    if (!L) {
      return;
    }
    if (this.mapInstances[tabId]) {
      this.revalidarMapa(tabId);
      return;
    }
    const map = L.map(this.mapId(tabId), {
      zoomControl: true
    }).setView([-14.235, -51.9253], 4);

    L.tileLayer('https://{s}.tile.openstreetmap.fr/hot/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors, tiles courtesy of Humanitarian OpenStreetMap Team'
    }).addTo(map);

    this.mapInstances[tabId] = map;
    this.revalidarMapa(tabId);
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
          this.geocodificarBeneficiariosPendentes();
          this.atualizarMarcadores('beneficiarios');
        },
        error: () => {
          this.erroTabs.beneficiarios = 'Nao foi possivel carregar os beneficiarios.';
        },
        complete: () => {
          this.carregandoTabs.beneficiarios = false;
        }
      });
  }

  private carregarProfissionais(): void {
    if (this.profissionais.length) {
      this.atualizarMarcadores('profissionais');
      return;
    }
    this.carregandoTabs.profissionais = true;
    this.erroTabs.profissionais = null;
    this.avisoTabs.profissionais = null;

    this.professionalService
      .list()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (profissionais) => {
          this.profissionais = profissionais ?? [];
          this.geocodificarProfissionaisPendentes();
          this.atualizarMarcadores('profissionais');
        },
        error: () => {
          this.erroTabs.profissionais = 'Nao foi possivel carregar os profissionais.';
        },
        complete: () => {
          this.carregandoTabs.profissionais = false;
        }
      });
  }

  private carregarDoadores(): void {
    if (this.doadores.length) {
      this.atualizarMarcadores('doadores');
      return;
    }
    this.carregandoTabs.doadores = true;
    this.erroTabs.doadores = null;
    this.avisoTabs.doadores = null;

    this.recebimentoDoacaoService
      .listarDoadores()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (doadores) => {
          this.doadores = doadores ?? [];
          this.geocodificarDoadoresPendentes();
          this.atualizarMarcadores('doadores');
        },
        error: () => {
          this.erroTabs.doadores = 'Nao foi possivel carregar os doadores.';
        },
        complete: () => {
          this.carregandoTabs.doadores = false;
        }
      });
  }

  private geocodificarPendentes(): void {
    const pendentes = this.beneficiarios.filter((beneficiario) => {
      const temCoordenadas = this.hasCoordinates(beneficiario);
      const temEndereco = this.hasEnderecoMinimo(beneficiario);
      return !temCoordenadas && temEndereco && beneficiario.id_beneficiario;
    });

    if (!pendentes.length) {
      if (!this.beneficiarios.some((item) => this.hasEnderecoMinimo(item))) {
        this.avisoTabs.beneficiarios = 'Informe logradouro, municipio e UF para localizar.';
      }
      return;
    }

    let chain = Promise.resolve();
    pendentes.forEach((beneficiario) => {
      chain = chain
        .then(() => this.delay(1100))
        .then(() => this.geocodificarBeneficiario(beneficiario))
        .catch(() => undefined);
    });
  }

  private geocodificarBeneficiario(beneficiario: BeneficiarioApiPayload): Promise<void> {
    const id = beneficiario.id_beneficiario;
    if (!id) {
      return Promise.resolve();
    }

    return new Promise((resolve) => {
      this.beneficiarioService.geocodificarEndereco(String(id)).subscribe({
        next: ({ beneficiario: atualizado }: { beneficiario?: BeneficiarioApiPayload }) => {
          const index = this.beneficiarios.findIndex((item) => item.id_beneficiario === id);
          if (index >= 0) {
            this.beneficiarios[index] = { ...this.beneficiarios[index], ...atualizado };
            this.atualizarMarcadores('beneficiarios');
          }
          resolve();
        },
        error: () => {
          this.avisoTabs.beneficiarios = 'Nao foi possivel geocodificar alguns enderecos.';
          resolve();
        }
      });
    });
  }

  private buildPopup(beneficiario: BeneficiarioApiPayload): string {
    const nome = beneficiario.nome_completo || beneficiario.nome_social || 'Beneficiario';
    const endereco = this.formatarEndereco(beneficiario);
    return `<strong>${nome}</strong><br/>${endereco || 'Endereco nao informado'}`;
  }

  formatarEndereco(beneficiario: BeneficiarioApiPayload): string {
    return [beneficiario.logradouro, beneficiario.numero, beneficiario.bairro, beneficiario.municipio, beneficiario.uf]
      .filter(Boolean)
      .join(', ');
  }

  private hasCoordinates(beneficiario: BeneficiarioApiPayload): boolean {       
    return Boolean(beneficiario.latitude) && Boolean(beneficiario.longitude);
  }

  private hasAddress(beneficiario: BeneficiarioApiPayload): boolean {
    return Boolean(beneficiario.logradouro || beneficiario.bairro || beneficiario.municipio || beneficiario.uf);
  }

  private hasEnderecoMinimo(beneficiario: BeneficiarioApiPayload): boolean {
    return Boolean(beneficiario.logradouro && beneficiario.municipio && beneficiario.uf);
  }

  private delay(ms: number): Promise<void> {
    return new Promise((resolve) => setTimeout(resolve, ms));
  }

  private atualizarMarcadores(tabId: TabId): void {
    const mapInstance = this.mapInstances[tabId];
    if (!mapInstance || !L) {
      return;
    }

    this.markersByTab[tabId].forEach((marker) => marker.remove());
    this.markersByTab[tabId] = [];

    const markers = this.getMarkersForTab(tabId);
    if (!markers.length) {
      this.avisoTabs[tabId] = 'Nenhuma localizacao valida encontrada.';
    } else {
      this.avisoTabs[tabId] = null;
    }

    markers.forEach((item) => {
      const marker = L.marker([item.lat, item.lng])
        .addTo(mapInstance)
        .bindPopup(item.popup);
      this.markersByTab[tabId].push(marker);
    });

    if (markers.length) {
      const bounds = L.latLngBounds(markers.map((item) => [item.lat, item.lng]));
      mapInstance.fitBounds(bounds, { padding: [40, 40] });
    }
  }

  private getMarkersForTab(tabId: TabId): Array<{ lat: number; lng: number; popup: string }> {
    if (tabId === 'beneficiarios') {
      return this.beneficiarios
        .map((beneficiario) => {
          const lat = Number(beneficiario.latitude);
          const lng = Number(beneficiario.longitude);
          if (Number.isFinite(lat) && Number.isFinite(lng)) {
            return { lat, lng, popup: this.buildPopup(beneficiario) };
          }
          const id = String(beneficiario.id_beneficiario ?? '');
          const coords = id ? this.coordenadasBeneficiarios.get(id) : null;
          if (!coords) {
            return null;
          }
          return { lat: coords.lat, lng: coords.lng, popup: this.buildPopup(beneficiario) };
        })
        .filter((item): item is { lat: number; lng: number; popup: string } => Boolean(item));
    }
    if (tabId === 'profissionais') {
      return this.profissionais
        .map((profissional) => {
          const coords = this.coordenadasProfissionais.get(profissional.id);
          if (!coords) {
            return null;
          }
          return {
            lat: coords.lat,
            lng: coords.lng,
            popup: `<strong>${profissional.nomeCompleto}</strong><br/>${this.formatarEnderecoProfissional(profissional)}`
          };
        })
        .filter((item): item is { lat: number; lng: number; popup: string } => Boolean(item));
    }
    return this.doadores
      .map((doador) => {
        const coords = this.coordenadasDoadores.get(String(doador.id));
        if (!coords) {
          return null;
        }
        return {
          lat: coords.lat,
          lng: coords.lng,
          popup: `<strong>${doador.nome}</strong><br/>${this.formatarEnderecoDoador(doador)}`
        };
      })
      .filter((item): item is { lat: number; lng: number; popup: string } => Boolean(item));
  }

  private revalidarMapa(tabId: TabId): void {
    const mapInstance = this.mapInstances[tabId];
    if (!mapInstance) {
      return;
    }
    setTimeout(() => {
      if (!mapInstance) {
        return;
      }
      mapInstance.invalidateSize();
      this.atualizarMarcadores(tabId);
    }, 0);
  }

  private geocodificarProfissionaisPendentes(): void {
    const pendentes = this.profissionais.filter((profissional) => {
      const id = profissional.id;
      if (!id) {
        return false;
      }
      const jaTem = this.coordenadasProfissionais.has(id);
      const endereco = this.formatarEnderecoProfissional(profissional);
      return !jaTem && Boolean(endereco);
    });

    if (!pendentes.length) {
      return;
    }

    let chain = Promise.resolve();
    pendentes.forEach((profissional) => {
      chain = chain
        .then(() => this.delay(1100))
        .then(() => this.geocodificarEnderecoLivre(this.formatarEnderecoProfissional(profissional)))
        .then((coords) => {
          if (coords && profissional.id) {
            this.coordenadasProfissionais.set(profissional.id, coords);
            this.atualizarMarcadores('profissionais');
          }
        })
        .catch(() => undefined);
    });
  }

  private geocodificarBeneficiariosPendentes(): void {
    const pendentes = this.beneficiarios.filter((beneficiario) => {
      const id = beneficiario.id_beneficiario;
      if (!id) {
        return false;
      }
      const jaTemCoords = this.hasCoordinates(beneficiario) || this.coordenadasBeneficiarios.has(String(id));
      const endereco = this.formatarEndereco(beneficiario);
      return !jaTemCoords && this.hasEnderecoMinimo(beneficiario) && Boolean(endereco);
    });

    if (!pendentes.length) {
      if (!this.beneficiarios.some((item) => this.hasEnderecoMinimo(item))) {
        this.avisoTabs.beneficiarios = 'Informe logradouro, municipio e UF para localizar.';
      }
      return;
    }

    let chain = Promise.resolve();
    pendentes.forEach((beneficiario) => {
      const id = String(beneficiario.id_beneficiario ?? '');
      chain = chain
        .then(() => this.delay(1100))
        .then(() => this.geocodificarEnderecoLivre(this.formatarEndereco(beneficiario)))
        .then((coords) => {
          if (coords && id) {
            this.coordenadasBeneficiarios.set(id, coords);
            this.atualizarMarcadores('beneficiarios');
          }
        })
        .catch(() => undefined);
    });
  }

  private geocodificarDoadoresPendentes(): void {
    const pendentes = this.doadores.filter((doador) => {
      const id = String(doador.id);
      const jaTem = this.coordenadasDoadores.has(id);
      const endereco = this.formatarEnderecoDoador(doador);
      return !jaTem && Boolean(endereco);
    });

    if (!pendentes.length) {
      return;
    }

    let chain = Promise.resolve();
    pendentes.forEach((doador) => {
      chain = chain
        .then(() => this.delay(1100))
        .then(() => this.geocodificarEnderecoLivre(this.formatarEnderecoDoador(doador)))
        .then((coords) => {
          if (coords) {
            this.coordenadasDoadores.set(String(doador.id), coords);
            this.atualizarMarcadores('doadores');
          }
        })
        .catch(() => undefined);
    });
  }

  private formatarEnderecoProfissional(profissional: ProfessionalRecord): string {
    return [profissional.logradouro, profissional.numero, profissional.bairro, profissional.municipio, profissional.uf]
      .filter(Boolean)
      .join(', ');
  }

  private formatarEnderecoDoador(doador: DoadorResponse): string {
    return [doador.logradouro, doador.numero, doador.bairro, doador.cidade, doador.uf]
      .filter(Boolean)
      .join(', ');
  }

  private async geocodificarEnderecoLivre(endereco: string): Promise<{ lat: number; lng: number } | null> {
    const enderecoLimpo = this.normalizarEnderecoGeocode(endereco);
    if (!enderecoLimpo) {
      return null;
    }

    const consultas = this.construirConsultasEndereco(enderecoLimpo);
    for (const consultaBase of consultas) {
      const consulta = `${consultaBase}, Brasil`;
      if (this.geocodeCache.has(consulta)) {
        const cached = this.geocodeCache.get(consulta) ?? null;
        if (cached) {
          return cached;
        }
        continue;
      }

      try {
        const url = `https://nominatim.openstreetmap.org/search?format=json&limit=1&countrycodes=br&q=${encodeURIComponent(
          consulta
        )}`;
        const items = (await fetch(url, { headers: { 'Accept-Language': 'pt-BR' } }).then((response) =>
          response.json()
        )) as Array<{ lat: string; lon: string }>;
        if (!items.length) {
          this.geocodeCache.set(consulta, null);
          continue;
        }
        const lat = Number(items[0].lat);
        const lng = Number(items[0].lon);
        const coords = Number.isFinite(lat) && Number.isFinite(lng) ? { lat, lng } : null;
        const coordsValidas = coords && this.isCoordenadaBrasil(coords.lat, coords.lng) ? coords : null;
        this.geocodeCache.set(consulta, coords);
        if (coordsValidas) {
          return coordsValidas;
        }
      } catch {
        this.geocodeCache.set(consulta, null);
      }
    }

    return null;
  }

  private construirConsultasEndereco(endereco: string): string[] {
    const partes = endereco
      .split(',')
      .map((parte) => parte.trim())
      .filter(Boolean);

    if (partes.length < 3) {
      return [endereco];
    }

    const [logradouro, numero, bairro, municipio, uf] = partes;
    const consultas = new Set<string>();
    consultas.add(partes.join(', '));
    if (logradouro && numero && municipio && uf) {
      consultas.add([logradouro, numero, municipio, uf].filter(Boolean).join(', '));
    }
    if (logradouro && municipio && uf) {
      consultas.add([logradouro, municipio, uf].filter(Boolean).join(', '));
    }
    return Array.from(consultas);
  }

  private isCoordenadaBrasil(lat: number, lng: number): boolean {
    return lat <= 5.5 && lat >= -34.5 && lng <= -34.0 && lng >= -74.5;
  }

  private normalizarEnderecoGeocode(endereco: string): string {
    const texto = endereco?.trim();
    if (!texto) {
      return '';
    }
    return texto
      .replace(/\s+/g, ' ')
      .replace(/\s*,\s*/g, ', ')
      .replace(/\s+-\s+/g, ' - ');
  }

  private loadLeaflet(): Promise<void> {
    const scriptId = 'leaflet-script';
    const styleId = 'leaflet-style';

    if (!document.getElementById(styleId)) {
      const link = document.createElement('link');
      link.id = styleId;
      link.rel = 'stylesheet';
      link.href = 'https://unpkg.com/leaflet@1.9.4/dist/leaflet.css';
      document.head.appendChild(link);
    }

    if (document.getElementById(scriptId)) {
      return Promise.resolve();
    }

    return new Promise((resolve, reject) => {
      const script = document.createElement('script');
      script.id = scriptId;
      script.src = 'https://unpkg.com/leaflet@1.9.4/dist/leaflet.js';
      script.async = true;
      script.onload = () => resolve();
      script.onerror = () => reject();
      document.body.appendChild(script);
    });
  }
}
