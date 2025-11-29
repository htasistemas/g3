import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AssistanceUnitPayload, AssistanceUnitService } from '../../services/assistance-unit.service';

@Component({
  selector: 'app-assistance-unit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './assistance-unit.component.html',
  styleUrl: './assistance-unit.component.scss'
})
export class AssistanceUnitComponent implements OnInit {
  units: AssistanceUnitPayload[] = [];

  readonly form = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(3)]]
  });

  constructor(
    private readonly fb: FormBuilder,
    private readonly unitService: AssistanceUnitService
  ) {}

  ngOnInit(): void {
    this.loadUnits();
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.unitService.save(this.form.value as AssistanceUnitPayload).subscribe({
      next: (created) => {
        this.form.reset();
        this.loadUnits();
        this.unitService.setActiveUnit(created.name);
      },
      error: (error) => {
        console.error('Erro ao salvar unidade', error);
      }
    });
  }

  selectUnit(unit: AssistanceUnitPayload): void {
    this.unitService.setActiveUnit(unit.name);
  }

  private loadUnits(): void {
    this.unitService.list().subscribe({
      next: (response) => {
        this.units = response.units;
      },
      error: (error) => console.error('Erro ao carregar unidades', error)
    });
  }
}
