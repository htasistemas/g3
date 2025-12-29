import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subject, takeUntil } from 'rxjs';
import { BeneficiarioApiPayload, BeneficiarioApiService } from '../../services/beneficiario-api.service';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';

declare const L: any;

@Component({
  selector: 'app-georeferenciamento-localizacao',
  standalone: true,
  imports: [CommonModule, TelaPadraoComponent],
  templateUrl: './georeferenciamento-localizacao.component.html',
  styleUrl: './georeferenciamento-localizacao.component.scss'
})
export class GeoreferenciamentoLocalizacaoComponent implements OnInit, OnDestroy {
  readonly tabs = [{ id: 'beneficiarios', label: 'Dados dos beneficiarios' }];
  activeTab = 'beneficiarios';

  beneficiarios: BeneficiarioApiPayload[] = [];
  carregando = false;
  erro: string | null = null;
  private mapInstance: any;
  private markers: any[] = [];
  private readonly destroy$ = new Subject<void>();

  constructor(private readonly beneficiarioService: BeneficiarioApiService) {}

  ngOnInit(): void {
    this.loadLeaflet()
      .then(() => {
        this.initMap();
        this.carregarBeneficiarios();
      })
      .catch(() => {
        this.erro = 'Nao foi possivel carregar o mapa.';
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    if (this.mapInstance) {
      this.mapInstance.remove();
    }
  }

  private initMap(): void {
    if (!L) {
      return;
    }
    const map = L.map('beneficiarios-map', {
      zoomControl: true
    }).setView([-14.235, -51.9253], 4);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '&copy; OpenStreetMap contributors'
    }).addTo(map);

    this.mapInstance = map;
  }

  private carregarBeneficiarios(): void {
    this.carregando = true;
    this.erro = null;

    this.beneficiarioService
      .list()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response: { beneficiarios?: BeneficiarioApiPayload[] }) => {
          this.beneficiarios = response.beneficiarios ?? [];
          this.atualizarMarcadores();
          this.geocodificarPendentes();
        },
        error: () => {
          this.erro = 'Nao foi possivel carregar os beneficiarios.';
        },
        complete: () => {
          this.carregando = false;
        }
      });
  }

  private atualizarMarcadores(): void {
    if (!this.mapInstance || !L) {
      return;
    }

    this.markers.forEach((marker) => marker.remove());
    this.markers = [];

    const validos = this.beneficiarios
      .map((beneficiario) => ({
        ...beneficiario,
        lat: Number(beneficiario.latitude),
        lng: Number(beneficiario.longitude)
      }))
      .filter((item) => Number.isFinite(item.lat) && Number.isFinite(item.lng));

    validos.forEach((beneficiario) => {
      const marker = L.marker([beneficiario.lat, beneficiario.lng])
        .addTo(this.mapInstance)
        .bindPopup(this.buildPopup(beneficiario));
      this.markers.push(marker);
    });
  }

  private geocodificarPendentes(): void {
    const pendentes = this.beneficiarios.filter((beneficiario) => {
      const temCoordenadas = this.hasCoordinates(beneficiario);
      const temEndereco = this.hasAddress(beneficiario);
      return !temCoordenadas && temEndereco && beneficiario.id_beneficiario;
    });

    if (!pendentes.length) {
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
            this.atualizarMarcadores();
          }
          resolve();
        },
        error: () => resolve()
      });
    });
  }

  private buildPopup(beneficiario: BeneficiarioApiPayload): string {
    const nome = beneficiario.nome_completo || beneficiario.nome_social || 'Beneficiario';
    const endereco = [beneficiario.logradouro, beneficiario.numero, beneficiario.bairro, beneficiario.municipio, beneficiario.uf]
      .filter(Boolean)
      .join(', ');
    return `<strong>${nome}</strong><br/>${endereco || 'Endereco nao informado'}`;
  }

  private hasCoordinates(beneficiario: BeneficiarioApiPayload): boolean {
    return Boolean(beneficiario.latitude) && Boolean(beneficiario.longitude);
  }

  private hasAddress(beneficiario: BeneficiarioApiPayload): boolean {
    return Boolean(beneficiario.logradouro || beneficiario.bairro || beneficiario.municipio || beneficiario.uf);
  }

  private delay(ms: number): Promise<void> {
    return new Promise((resolve) => setTimeout(resolve, ms));
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
