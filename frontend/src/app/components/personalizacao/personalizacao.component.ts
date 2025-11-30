import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faCircleHalfStroke, faPalette, faRotateLeft } from '@fortawesome/free-solid-svg-icons';
import { debounceTime, Subject, takeUntil } from 'rxjs';
import { ThemeService } from '../../services/theme.service';
import { PaletteSettings, PersonalizationService } from '../../services/personalization.service';

@Component({
  selector: 'app-personalizacao',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FontAwesomeModule],
  templateUrl: './personalizacao.component.html',
  styleUrl: './personalizacao.component.scss'
})
export class PersonalizacaoComponent implements OnInit, OnDestroy {
  readonly faPalette = faPalette;
  readonly faCircleHalfStroke = faCircleHalfStroke;
  readonly faRotateLeft = faRotateLeft;

  form = this.fb.group({
    primaryStrong: [''],
    primaryGradient: [''],
    background: [''],
    surface: [''],
    border: [''],
    cardRadius: [''],
    controlRadius: ['']
  });

  previewTheme: 'light' | 'dark';
  private destroy$ = new Subject<void>();

  constructor(
    private readonly fb: FormBuilder,
    private readonly personalization: PersonalizationService,
    private readonly theme: ThemeService
  ) {
    this.previewTheme = this.theme.currentTheme();
  }

  ngOnInit(): void {
    this.form.patchValue(this.personalization.palette(), { emitEvent: false });
    this.form.valueChanges.pipe(debounceTime(150), takeUntil(this.destroy$)).subscribe((value) => {
      this.personalization.updatePalette(value as Partial<PaletteSettings>);
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  setTheme(mode: 'light' | 'dark'): void {
    this.theme.setTheme(mode);
    this.previewTheme = mode;
  }

  reset(): void {
    this.personalization.reset();
    this.form.patchValue(this.personalization.palette(), { emitEvent: false });
  }
}
