import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
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
  logoPreview: string | null = null;
  reportLogoPreview: string | null = null;

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
      razaoSocial: ['', [this.optionalMinLength(3)]],
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
      logomarca: [''],
      logomarcaRelatorio: [''],
      horarioFuncionamento: [''],
      responsavelNome: [''],
      responsavelCpf: ['', [this.optionalCpfValidator()]],
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
        this.logoPreview = created.logomarca || null;
        this.reportLogoPreview = created.logomarcaRelatorio || null;
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
          this.logoPreview = unidade.logomarca || null;
          this.reportLogoPreview = unidade.logomarcaRelatorio || null;
          if (unidade.cep) {
            this.form.get('cep')?.setValue(this.formatCep(unidade.cep), { emitEvent: false });
          }
          if (unidade.responsavelCpf) {
            this.form
              .get('responsavelCpf')
              ?.setValue(this.formatCpf(unidade.responsavelCpf), { emitEvent: false });
          }
          this.unitService.setActiveUnit(unidade.nomeFantasia);
        }
      },
      error: (error) => console.error('Erro ao carregar unidade', error)
    });
  }

  onLogoSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    if (!file) {
      return;
    }

    const reader = new FileReader();
    reader.onload = () => {
      const base64 = reader.result as string;
      this.form.get('logomarca')?.setValue(base64);
      this.logoPreview = base64;
    };

    reader.readAsDataURL(file);
  }

  clearLogo(): void {
    this.form.get('logomarca')?.setValue('');
    this.logoPreview = null;
  }

  onReportLogoSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];

    if (!file) {
      return;
    }

    const reader = new FileReader();
    reader.onload = () => {
      const base64 = reader.result as string;
      this.form.get('logomarcaRelatorio')?.setValue(base64);
      this.reportLogoPreview = base64;
    };

    reader.readAsDataURL(file);
  }

  clearReportLogo(): void {
    this.form.get('logomarcaRelatorio')?.setValue('');
    this.reportLogoPreview = null;
  }

  resetForm(): void {
    this.form.reset(this.unidade || {});
    this.logoPreview = this.unidade?.logomarca || null;
    this.reportLogoPreview = this.unidade?.logomarcaRelatorio || null;
  }

  printUnit(): void {
    if (!this.unidade) {
      return;
    }

    const unidade = this.unidade;
    const printLogo = unidade.logomarcaRelatorio || unidade.logomarca;
    const content = `
      <html>
        <head>
          <title>Dados da unidade</title>
          <style>
            body { font-family: Arial, sans-serif; padding: 24px; color: #0f172a; }
            h1 { font-size: 20px; margin: 0 0 12px; }
            .item { margin-bottom: 8px; line-height: 1.45; }
            .label { font-weight: 600; }
            .logo { margin: 12px 0 16px; max-height: 80px; }
          </style>
        </head>
        <body>
          <h1>${unidade.nomeFantasia || 'Unidade assistencial'}</h1>
          ${printLogo ? `<img class="logo" src="${printLogo}" alt="Imagem institucional" />` : ''}
          <div class="item"><span class="label">Razão social:</span> ${unidade.razaoSocial || 'Não informada'}</div>
          <div class="item"><span class="label">CNPJ:</span> ${unidade.cnpj || 'Não informado'}</div>
          <div class="item"><span class="label">Telefone:</span> ${unidade.telefone || 'Não informado'}</div>
          <div class="item"><span class="label">E-mail:</span> ${unidade.email || 'Não informado'}</div>
          <div class="item"><span class="label">Horário de funcionamento:</span> ${unidade.horarioFuncionamento || 'Não informado'}</div>
          <div class="item"><span class="label">CEP:</span> ${unidade.cep || 'Não informado'}</div>
          <div class="item"><span class="label">Endereço:</span> ${unidade.endereco || ''} ${unidade.numeroEndereco || ''}</div>
          <div class="item"><span class="label">Bairro:</span> ${unidade.bairro || 'Não informado'}</div>
          <div class="item"><span class="label">Cidade/Estado:</span> ${unidade.cidade || 'Sem cidade'} / ${unidade.estado || 'UF'}</div>
          <div class="item"><span class="label">Diretor(a)/Coordenador(a)/Presidente:</span> ${unidade.responsavelNome || 'Não informado'}</div>
          <div class="item"><span class="label">CPF do responsável:</span> ${unidade.responsavelCpf || 'Não informado'}</div>
          <div class="item"><span class="label">Período de mandato:</span> ${unidade.responsavelPeriodoMandato || 'Não informado'}</div>
          <div class="item"><span class="label">Observações:</span> ${unidade.observacoes || 'Nenhuma'}</div>
        </body>
      </html>
    `;

    const printWindow = window.open('', '_blank', 'width=900,height=1200');

    if (!printWindow) {
      return;
    }

    printWindow.document.write(content);
    printWindow.document.close();
    printWindow.focus();
    setTimeout(() => {
      printWindow.print();
      printWindow.close();
    }, 250);
  }

  onPhoneInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const formatted = this.formatPhone(input.value);
    this.form.get('telefone')?.setValue(formatted, { emitEvent: false });
  }

  onCpfInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const formatted = this.formatCpf(input.value);
    this.form.get('responsavelCpf')?.setValue(formatted, { emitEvent: false });
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
    const digits = value.replace(/\D/g, '').slice(0, 8);

    if (digits.length <= 5) {
      return digits;
    }

    return `${digits.slice(0, 5)}-${digits.slice(5, 8)}`;
  }

  private formatCpf(value: string): string {
    const digits = value.replace(/\D/g, '').slice(0, 11);

    if (digits.length <= 3) {
      return digits;
    }

    if (digits.length <= 6) {
      return `${digits.slice(0, 3)}.${digits.slice(3)}`;
    }

    if (digits.length <= 9) {
      return `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6)}`;
    }

    return `${digits.slice(0, 3)}.${digits.slice(3, 6)}.${digits.slice(6, 9)}-${digits.slice(9, 11)}`;
  }

  private optionalMinLength(length: number) {
    return (control: AbstractControl) => {
      const value = (control.value || '').trim();

      if (!value) {
        return null;
      }

      return value.length >= length
        ? null
        : { minlength: { requiredLength: length, actualLength: value.length } };
    };
  }

  private optionalCpfValidator() {
    return (control: AbstractControl) => {
      const digits = (control.value || '').replace(/\D/g, '');

      if (!digits) {
        return null;
      }

      if (digits.length !== 11 || /^([0-9])\1{10}$/.test(digits)) {
        return { cpfInvalid: true };
      }

      const calculateVerifier = (base: string, factor: number) => {
        let total = 0;
        for (let i = 0; i < base.length; i += 1) {
          total += parseInt(base.charAt(i), 10) * (factor - i);
        }

        const remainder = (total * 10) % 11;
        return remainder === 10 ? 0 : remainder;
      };

      const firstVerifier = calculateVerifier(digits.slice(0, 9), 10);
      const secondVerifier = calculateVerifier(digits.slice(0, 10), 11);

      const isValid = firstVerifier === Number(digits.charAt(9)) && secondVerifier === Number(digits.charAt(10));

      return isValid ? null : { cpfInvalid: true };
    };
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
