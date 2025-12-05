import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
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
export class AssistanceUnitComponent implements OnInit, OnDestroy {
  readonly tabs = [
    { id: 'dados', label: 'Dados da Unidade' },
    { id: 'endereco', label: 'Endereço da Unidade' },
    { id: 'imagens', label: 'Imagens da Unidade' },
    { id: 'diretoria', label: 'Diretoria da Unidade' }
  ] as const;

  unidade: AssistanceUnitPayload | null = null;
  logoPreview: string | null = null;
  reportLogoPreview: string | null = null;
  feedback: { type: 'success' | 'error' | 'warning'; message: string } | null = null;
  deleteConfirmation = false;
  activeTab: (typeof this.tabs)[number]['id'] = 'dados';
  private feedbackTimeout: ReturnType<typeof setTimeout> | null = null;

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

  ngOnDestroy(): void {
    this.clearFeedbackTimeout();
  }

  get activeTabIndex(): number {
    return this.tabs.findIndex((tab) => tab.id === this.activeTab);
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
        this.unitService.setActiveUnit(created.nomeFantasia, created.logomarca || null);
        this.deleteConfirmation = false;
        this.setFeedback(
          {
            type: 'success',
            message: payload.id ? 'Unidade atualizada com sucesso.' : 'Unidade salva com sucesso.'
          },
          { autoDismiss: true }
        );
      },
      error: (error) => {
        console.error('Erro ao salvar unidade', error);
        this.setFeedback({
          type: 'error',
          message: 'Não foi possível salvar a unidade. Tente novamente.'
        });
      }
    });
  }

  requestDeletion(): void {
    if (!this.unidade?.id) {
      return;
    }

    this.deleteConfirmation = true;
    this.setFeedback(
      {
        type: 'warning',
        message: 'Você está excluindo a unidade. Tem certeza? Esta ação é irreversível.'
      },
      { autoDismiss: false }
    );
  }

  cancelDeletion(): void {
    this.deleteConfirmation = false;
    this.dismissFeedback();
  }

  confirmDeletion(): void {
    if (!this.unidade?.id) {
      return;
    }

    this.unitService.remove(this.unidade.id).subscribe({
      next: () => {
        this.unidade = null;
        this.form.reset();
        this.logoPreview = null;
        this.reportLogoPreview = null;
        this.deleteConfirmation = false;
        this.setFeedback({ type: 'success', message: 'Unidade excluída com sucesso.' }, { autoDismiss: true });
      },
      error: (error) => {
        console.error('Erro ao excluir unidade', error);
        this.setFeedback({
          type: 'error',
          message: 'Não foi possível excluir a unidade. Tente novamente.'
        });
      }
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
          this.unitService.setActiveUnit(unidade.nomeFantasia, unidade.logomarca || null);
        }
      },
      error: (error) => console.error('Erro ao carregar unidade', error)
    });
  }

  changeTab(tabId: (typeof this.tabs)[number]['id']): void {
    this.activeTab = tabId;
  }

  getTabLabel(tabId: (typeof this.tabs)[number]['id']): string {
    return this.tabs.find((tab) => tab.id === tabId)?.label ?? '';
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
    this.dismissFeedback();
    this.deleteConfirmation = false;
  }

  startNew(): void {
    this.unidade = null;
    this.form.reset();
    this.logoPreview = null;
    this.reportLogoPreview = null;
    this.deleteConfirmation = false;
    this.activeTab = 'dados';
    this.dismissFeedback();
  }

  closeForm(): void {
    window.history.back();
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
            @page {
              size: A4;
              margin: 12mm;
            }
            *, *::before, *::after { box-sizing: border-box; }
            body {
              font-family: 'Inter', 'Segoe UI', system-ui, -apple-system, sans-serif;
              background: #ffffff;
              color: #0f172a;
              margin: 0;
              padding: 0;
            }
            .report {
              width: 100%;
              max-width: calc(210mm - 24mm);
              min-height: calc(297mm - 24mm);
              margin: 0 auto;
              background: #ffffff;
              border-radius: 0;
              overflow: hidden;
              box-shadow: none;
              border: none;
              display: flex;
              flex-direction: column;
            }
            .hero {
              position: relative;
              padding: 24px 26px;
              background: linear-gradient(135deg, #0ea5e9, #6366f1, #8b5cf6);
              color: #ffffff;
              display: flex;
              gap: 20px;
              align-items: center;
            }
            .hero::after {
              content: '';
              position: absolute;
              inset: 0;
              background: radial-gradient(circle at 20% 20%, rgba(255,255,255,0.25), transparent 35%),
                          radial-gradient(circle at 80% 0%, rgba(255,255,255,0.18), transparent 40%);
              pointer-events: none;
            }
            .logo-badge {
              position: relative;
              z-index: 1;
              width: 96px;
              height: 96px;
              border-radius: 20px;
              overflow: hidden;
              border: 1px solid rgba(255, 255, 255, 0.35);
              background: rgba(255, 255, 255, 0.12);
              display: grid;
              place-items: center;
              backdrop-filter: blur(6px);
              box-shadow: 0 15px 40px rgba(0, 0, 0, 0.25);
            }
            .logo-badge img {
              max-width: 90%;
              max-height: 90%;
              object-fit: contain;
              filter: drop-shadow(0 8px 20px rgba(0, 0, 0, 0.25));
            }
            .logo-placeholder {
              font-size: 14px;
              font-weight: 600;
              color: #e0f2fe;
              text-align: center;
              padding: 12px;
            }
            .hero-content { position: relative; z-index: 1; }
            .eyebrow {
              text-transform: uppercase;
              letter-spacing: 0.12em;
              font-size: 11px;
              font-weight: 700;
              margin: 0 0 6px;
              color: #c7d2fe;
            }
            h1 {
              margin: 0 0 6px;
              font-size: 26px;
              font-weight: 800;
              line-height: 1.15;
            }
            .hero-subtitle {
              margin: 0;
              color: #e0f2fe;
              font-size: 14px;
              display: flex;
              gap: 10px;
              align-items: center;
              flex-wrap: wrap;
            }
            .badge {
              display: inline-flex;
              align-items: center;
              gap: 8px;
              background: rgba(255, 255, 255, 0.16);
              color: #fff;
              padding: 8px 12px;
              border-radius: 12px;
              font-weight: 600;
              font-size: 12px;
              border: 1px solid rgba(255, 255, 255, 0.18);
              backdrop-filter: blur(6px);
            }
            .body {
              padding: 18px 20px 22px;
              display: grid;
              gap: 18px;
            }
            .section-card {
              border: none;
              border-radius: 0;
              padding: 12px 0 14px;
              background: transparent;
            }
            .section-title {
              margin: 0 0 10px;
              font-size: 15px;
              letter-spacing: 0.02em;
              color: #0f172a;
            }
            .data-grid {
              display: grid;
              gap: 16px 18px;
              grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
            }
            .data-item {
              display: grid;
              gap: 4px;
              padding: 4px 0;
            }
            .label {
              display: block;
              font-size: 11px;
              letter-spacing: 0.08em;
              text-transform: uppercase;
              color: #475569;
              margin-bottom: 2px;
              font-weight: 700;
            }
            .value { font-size: 15px; font-weight: 700; color: #0f172a; margin: 0; }
            .muted { color: #64748b; font-weight: 500; font-size: 13px; margin: 0; }
            .footer-note {
              padding: 12px 0 0;
              background: transparent;
              color: #0f172a;
              border-radius: 0;
              border: none;
            }
          </style>
        </head>
        <body>
          <div class="report">
            <div class="hero">
              <div class="logo-badge">
                ${
                  printLogo
                    ? `<img src="${printLogo}" alt="Imagem institucional" />`
                    : `<div class="logo-placeholder">Logo da unidade</div>`
                }
              </div>
              <div class="hero-content">
                <p class="eyebrow">Cadastro da unidade</p>
                <h1>${unidade.nomeFantasia || 'Unidade assistencial'}</h1>
                <p class="hero-subtitle">
                  <span class="badge">${unidade.cidade || 'Cidade não informada'} · ${unidade.estado || 'UF'}</span>
                  <span class="badge">${unidade.horarioFuncionamento || 'Horário não informado'}</span>
                </p>
              </div>
            </div>

            <div class="body">
              <div class="section-card">
                <p class="section-title">Identidade e contato</p>
                <div class="data-grid">
                  <div class="data-item">
                    <span class="label">Razão social</span>
                    <p class="value">${unidade.razaoSocial || 'Não informada'}</p>
                    <p class="muted">Nome completo cadastrado.</p>
                  </div>
                  <div class="data-item">
                    <span class="label">CNPJ</span>
                    <p class="value">${unidade.cnpj || 'Não informado'}</p>
                    <p class="muted">Identificação fiscal da organização.</p>
                  </div>
                  <div class="data-item">
                    <span class="label">Telefone</span>
                    <p class="value">${unidade.telefone || 'Não informado'}</p>
                    <p class="muted">Canal preferencial para contato direto.</p>
                  </div>
                  <div class="data-item">
                    <span class="label">E-mail</span>
                    <p class="value">${unidade.email || 'Não informado'}</p>
                    <p class="muted">Endereço eletrônico para comunicação institucional.</p>
                  </div>
                  <div class="data-item">
                    <span class="label">CEP</span>
                    <p class="value">${unidade.cep || 'Não informado'}</p>
                    <p class="muted">Código de endereçamento para localização.</p>
                  </div>
                  <div class="data-item">
                    <span class="label">Horário de funcionamento</span>
                    <p class="value">${unidade.horarioFuncionamento || 'Não informado'}</p>
                    <p class="muted">Período de atendimento ao público.</p>
                  </div>
                </div>
              </div>

              <div class="section-card">
                <p class="section-title">Localização</p>
                <div class="data-grid">
                  <div class="data-item">
                    <span class="label">Endereço</span>
                    <p class="value">${unidade.endereco || 'Endereço não informado'} ${unidade.numeroEndereco || ''}</p>
                    <p class="muted">Bairro: ${unidade.bairro || 'Não informado'}</p>
                  </div>
                  <div class="data-item">
                    <span class="label">Cidade / Estado</span>
                    <p class="value">${unidade.cidade || 'Sem cidade'} / ${unidade.estado || 'UF'}</p>
                    <p class="muted">Referência geográfica da unidade.</p>
                  </div>
                </div>
              </div>

              <div class="section-card">
                <p class="section-title">Responsável institucional</p>
                <div class="data-grid">
                  <div class="data-item">
                    <span class="label">Nome</span>
                    <p class="value">${unidade.responsavelNome || 'Não informado'}</p>
                    <p class="muted">Diretor(a), coordenador(a) ou presidente.</p>
                  </div>
                  <div class="data-item">
                    <span class="label">CPF</span>
                    <p class="value">${unidade.responsavelCpf || 'Não informado'}</p>
                    <p class="muted">Documento pessoal do responsável.</p>
                  </div>
                  <div class="data-item">
                    <span class="label">Período de mandato</span>
                    <p class="value">${unidade.responsavelPeriodoMandato || 'Não informado'}</p>
                    <p class="muted">Vigência da gestão atual.</p>
                  </div>
                </div>
              </div>

              <div>
                <p class="section-title">Observações</p>
                <div class="footer-note">
                  ${unidade.observacoes || 'Nenhuma observação registrada até o momento.'}
                </div>
              </div>
            </div>
          </div>
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

  private setFeedback(
    feedback: { type: 'success' | 'error' | 'warning'; message: string },
    options: { autoDismiss?: boolean; duration?: number } = {}
  ): void {
    this.clearFeedbackTimeout();
    this.feedback = feedback;

    const shouldAutoDismiss = options.autoDismiss ?? true;

    if (shouldAutoDismiss) {
      const duration = options.duration ?? 4500;
      this.feedbackTimeout = setTimeout(() => {
        this.dismissFeedback();
      }, duration);
    }
  }

  private clearFeedbackTimeout(): void {
    if (this.feedbackTimeout) {
      clearTimeout(this.feedbackTimeout);
      this.feedbackTimeout = null;
    }
  }

  public dismissFeedback(): void {
    this.clearFeedbackTimeout();
    this.feedback = null;
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
