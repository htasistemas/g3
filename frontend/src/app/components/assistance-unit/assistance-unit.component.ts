import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AssistanceUnitPayload, AssistanceUnitService } from '../../services/assistance-unit.service';

@Component({
  selector: 'app-assistance-unit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './assistance-unit.component.html',
  styleUrl: './assistance-unit.component.scss'
})
export class AssistanceUnitComponent implements OnInit {
  unidade: AssistanceUnitPayload | null = null;

  form!: FormGroup;

  constructor(
    private readonly fb: FormBuilder,
    private readonly unitService: AssistanceUnitService
  ) {
    this.form = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      telefone: [''],
      email: ['', Validators.email],
      cep: [''],
      endereco: [''],
      numeroEndereco: [''],
      bairro: [''],
      cidade: [''],
      estado: [''],
      observacoes: ['']
    });
  }

  ngOnInit(): void {
    this.loadUnit();
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: AssistanceUnitPayload = {
      ...this.form.value,
      id: this.unidade?.id,
    };

    this.unitService.save(payload).subscribe({
      next: (created) => {
        this.unidade = created;
        this.form.patchValue(created);
        this.unitService.setActiveUnit(created.nome);
      },
      error: (error) => {
        console.error('Erro ao salvar unidade', error);
      }
    });
  }

  delete(): void {
    if (!this.unidade?.id) {
      return;
    }

    this.unitService.remove(this.unidade.id).subscribe({
      next: () => {
        this.unidade = null;
        this.form.reset();
      },
      error: (error) => console.error('Erro ao excluir unidade', error)
    });
  }

  private loadUnit(): void {
    this.unitService.get().subscribe({
      next: ({ unidade }) => {
        this.unidade = unidade;
        if (unidade) {
          this.form.patchValue(unidade);
          this.unitService.setActiveUnit(unidade.nome);
        }
      },
      error: (error) => console.error('Erro ao carregar unidade', error)
    });
  }
}
