import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { Subject, takeUntil } from 'rxjs';
import { AssistanceUnitPayload, AssistanceUnitService } from '../../services/assistance-unit.service';

@Component({
  selector: 'app-georeferenciamento-localizacao',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './georeferenciamento-localizacao.component.html',
  styleUrl: './georeferenciamento-localizacao.component.scss'
})
export class GeoreferenciamentoLocalizacaoComponent implements OnInit, OnDestroy {
  readonly providers = [
    { id: 'openstreetmap', label: 'OpenStreetMap' },
    { id: 'google', label: 'Google Maps (embed)' },
    { id: 'bing', label: 'Bing Maps (iframe)' }
  ];

  readonly layers = [
    { id: 'endereco', label: 'Endereço validado', status: 'Pronto' },
    { id: 'perimetro', label: 'Perímetro de cobertura', status: '2km' },
    { id: 'rota', label: 'Rotas de acesso', status: 'Sugestões salvas' }
  ];

  readonly qualityChecklist = [
    'CEP confirmado',
    'Latitude e longitude em WGS84',
    'Revisão humana realizada',
    'Mapa sincronizado na última hora'
  ];

  unit: AssistanceUnitPayload | null = null;
  geoForm: FormGroup;
  mapPreviewUrl: SafeResourceUrl | null = null;
  addressSummary = 'Sem endereço informado.';

  private readonly destroy$ = new Subject<void>();

  constructor(
    private readonly fb: FormBuilder,
    private readonly unitService: AssistanceUnitService,
    private readonly sanitizer: DomSanitizer
  ) {
    this.geoForm = this.fb.group({
      latitude: [-23.55052, [Validators.required, Validators.min(-90), Validators.max(90)]],
      longitude: [-46.633308, [Validators.required, Validators.min(-180), Validators.max(180)]],
      precision: [5, [Validators.required, Validators.min(1), Validators.max(10)]],
      provider: ['openstreetmap', Validators.required],
      addressReference: ['', [Validators.maxLength(200)]],
      coverageRadius: [2, [Validators.required, Validators.min(0.1)]],
      accessRoutes: ['', [Validators.maxLength(200)]],
      geoStatus: ['geocoded'],
      geocoder: ['OpenStreetMap Nominatim'],
      lastSync: ['Há poucos minutos'],
      metadataNotes: ['', [Validators.maxLength(300)]]
    });
  }

  ngOnInit(): void {
    this.unitService
      .get()
      .pipe(takeUntil(this.destroy$))
      .subscribe(({ unidade }) => {
        this.unit = unidade;
        this.addressSummary = this.buildAddressSummary(unidade);
        this.prefillFromUnit(unidade);
        this.refreshMap();
      });

    this.geoForm.valueChanges.pipe(takeUntil(this.destroy$)).subscribe(() => this.refreshMap());
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  useUnitAddress(): void {
    if (!this.unit) {
      return;
    }

    this.geoForm.patchValue({
      addressReference: this.buildAddressSummary(this.unit),
      accessRoutes: 'Acesso principal conforme endereço oficial da unidade.'
    });
  }

  updateToManual(lat: number, lng: number): void {
    this.geoForm.patchValue({ latitude: lat, longitude: lng });
  }

  get latControl() {
    return this.geoForm.get('latitude');
  }

  get lngControl() {
    return this.geoForm.get('longitude');
  }

  private prefillFromUnit(unidade: AssistanceUnitPayload | null | undefined): void {
    if (!unidade) {
      return;
    }

    const reference = this.buildAddressSummary(unidade);

    this.geoForm.patchValue({
      addressReference: reference,
      accessRoutes: 'Rotas de chegada via vias principais do bairro.',
      metadataNotes: 'Coordenadas em WGS84. Ajuste fino conforme visita técnica.'
    });
  }

  private refreshMap(): void {
    const lat = Number(this.geoForm.get('latitude')?.value);
    const lng = Number(this.geoForm.get('longitude')?.value);

    if (!isFinite(lat) || !isFinite(lng)) {
      this.mapPreviewUrl = null;
      return;
    }

    const bbox = `${lng - 0.01}%2C${lat - 0.01}%2C${lng + 0.01}%2C${lat + 0.01}`;
    const marker = `${lat}%2C${lng}`;
    const baseUrl = `https://www.openstreetmap.org/export/embed.html?bbox=${bbox}&layer=mapnik&marker=${marker}`;

    this.mapPreviewUrl = this.sanitizer.bypassSecurityTrustResourceUrl(baseUrl);
  }

  private buildAddressSummary(unidade: AssistanceUnitPayload | null | undefined): string {
    if (!unidade) {
      return 'Nenhuma unidade assistencial cadastrada.';
    }

    const parts = [unidade.endereco, unidade.numeroEndereco, unidade.bairro, unidade.cidade, unidade.estado]
      .filter(Boolean)
      .join(', ');

    return parts || 'Endereço não informado';
  }
}
