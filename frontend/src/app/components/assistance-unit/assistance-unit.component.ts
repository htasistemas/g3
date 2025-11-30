import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { filter } from 'rxjs';
import { AssistanceUnitPayload, AssistanceUnitService } from '../../services/assistance-unit.service';

interface ViaCepResponse {
  cep?: string;
  logradouro?: string;
  bairro?: string;
  localidade?: string;
  uf?: string;
  erro?: boolean;
}

@Component({
  selector: 'app-assistance-unit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, HttpClientModule],
  templateUrl: './assistance-unit.component.html',
  styleUrl: './assistance-unit.component.scss'
})
export class AssistanceUnitComponent implements OnInit {
  unidade: AssistanceUnitPayload | null = null;

  readonly estados = [
    'AC',
    'AL',
    'AP',
    'AM',
    'BA',
    'CE',
    'DF',
    'ES',
    'GO',
    'MA',
    'MT',
    'MS',
    'MG',
    'PA',
    'PB',
    'PR',
    'PE',
    'PI',
    'RJ',
    'RN',
    'RS',
    'RO',
    'RR',
    'SC',
    'SP',
    'SE',
    'TO'
  ];

  form!: FormGroup;

  constructor(
    private readonly fb: FormBuilder,
    private readonly unitService: AssistanceUnitService,
    private readonly http: HttpClient
  ) {
    this.form = this.fb.group({
      nomeFantasia: ['', [Validators.required, Validators.minLength(3)]],
      razaoSocial: ['', [Validators.minLength(3)]],
      cnpj: [''],
      telefone: [''],
      email: ['', Validators.email],
      cep: [''],
      endereco: [''],
      numeroEndereco: [''],
      bairro: [''],
      cidade: [''],
      estado: [''],
      observacoes: [''],
      responsavelNome: [''],
      responsavelCpf: [''],
      responsavelPeriodoMandato: ['']
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
        this.unitService.setActiveUnit(created.nomeFantasia);
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
          if (unidade.cep) {
            this.form.get('cep')?.setValue(this.formatCep(unidade.cep), { emitEvent: false });
          }
          this.unitService.setActiveUnit(unidade.nomeFantasia);
        }
      },
      error: (error) => console.error('Erro ao carregar unidade', error)
    });
  }

  onPhoneInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const formatted = this.formatPhone(input.value);
    this.form.get('telefone')?.setValue(formatted, { emitEvent: false });
  }

  onCepInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 8);
    const masked = this.formatCep(digits);

    this.form.get('cep')?.setValue(masked, { emitEvent: false });

    if (digits.length === 8) {
      this.fetchAddress(digits);
    }
  }

  private formatPhone(value: string): string {
    const digits = value.replace(/\D/g, '').slice(0, 11);

    if (digits.length <= 2) {
      return digits;
    }

    if (digits.length <= 6) {
      return `(${digits.slice(0, 2)}) ${digits.slice(2)}`;
    }

    if (digits.length <= 10) {
      return `(${digits.slice(0, 2)}) ${digits.slice(2, 6)}-${digits.slice(6, 10)}`;
    }

    return `(${digits.slice(0, 2)}) ${digits.slice(2, 7)}-${digits.slice(7, 11)}`;
  }

  private formatCep(value: string): string {
    if (value.length <= 5) return value;
    return `${value.slice(0, 5)}-${value.slice(5, 8)}`;
  }

  private fetchAddress(cep: string): void {
    this.http
      .get<ViaCepResponse>(`https://viacep.com.br/ws/${cep}/json/`)
      .pipe(filter((response) => !response.erro))
      .subscribe((response) => {
        this.form.patchValue({
          cep: this.formatCep(response.cep ?? cep),
          endereco: response.logradouro || this.form.value.endereco,
          bairro: response.bairro || this.form.value.bairro,
          cidade: response.localidade || this.form.value.cidade,
          estado: response.uf || this.form.value.estado
        });
      });
  }
}
