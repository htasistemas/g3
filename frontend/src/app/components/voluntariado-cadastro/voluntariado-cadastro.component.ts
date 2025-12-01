import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

interface StepTab {
  id: string;
  label: string;
}

@Component({
  selector: 'app-voluntariado-cadastro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './voluntariado-cadastro.component.html',
  styleUrl: './voluntariado-cadastro.component.scss'
})
export class VoluntariadoCadastroComponent {
  form: FormGroup;
  activeTab = 'dados';
  saving = false;
  feedback: string | null = null;

  tabs: StepTab[] = [
    { id: 'dados', label: 'Dados Pessoais' },
    { id: 'contato', label: 'Contato & Competências' },
    { id: 'disponibilidade', label: 'Disponibilidade' },
    { id: 'termos', label: 'Termos e Documentos' }
  ];

  readonly diasSemana = ['Segunda-feira', 'Terça-feira', 'Quarta-feira', 'Quinta-feira', 'Sexta-feira', 'Sábado'];
  readonly periodos = ['Manhã', 'Tarde', 'Noite'];
  readonly habilidadesSugestao = ['Atendimento direto', 'Captação de recursos', 'Comunicação', 'Administrativo', 'Tecnologia', 'Eventos'];

  constructor(private readonly fb: FormBuilder) {
    this.form = this.fb.group({
      dadosPessoais: this.fb.group({
        nomeCompleto: ['', Validators.required],
        cpf: ['', Validators.required],
        rg: [''],
        dataNascimento: [''],
        genero: [''],
        profissao: [''],
        motivacao: ['']
      }),
      contato: this.fb.group({
        telefone: [''],
        email: ['', [Validators.required, Validators.email]],
        cidade: [''],
        estado: [''],
        areaInteresse: [''],
        habilidades: [''],
        idiomas: [''],
        linkedin: ['']
      }),
      disponibilidade: this.fb.group({
        dias: this.fb.control<string[]>([]),
        periodos: this.fb.control<string[]>([]),
        cargaHorariaSemanal: [''],
        presencial: [true],
        remoto: [false],
        inicioPrevisto: [''],
        observacoes: ['']
      }),
      termos: this.fb.group({
        documentoIdentificacao: [''],
        comprovanteEndereco: [''],
        aceiteVoluntariado: [false, Validators.requiredTrue],
        aceiteImagem: [false, Validators.requiredTrue],
        assinaturaDigital: ['']
      })
    });
  }

  get activeTabIndex(): number {
    return this.tabs.findIndex((tab) => tab.id === this.activeTab);
  }

  get hasPreviousTab(): boolean {
    return this.activeTabIndex > 0;
  }

  get hasNextTab(): boolean {
    return this.activeTabIndex < this.tabs.length - 1;
  }

  get nextTabLabel(): string {
    return this.hasNextTab ? this.tabs[this.activeTabIndex + 1].label : '';
  }

  changeTab(tab: string): void {
    this.activeTab = tab;
  }

  goToNextTab(): void {
    if (this.hasNextTab) {
      this.changeTab(this.tabs[this.activeTabIndex + 1].id);
    }
  }

  goToPreviousTab(): void {
    if (this.hasPreviousTab) {
      this.changeTab(this.tabs[this.activeTabIndex - 1].id);
    }
  }

  toggleSelection(path: (string | number)[], option: string): void {
    const control = this.form.get(path);
    const current = new Set(control?.value ?? []);

    if (current.has(option)) {
      current.delete(option);
    } else {
      current.add(option);
    }

    control?.setValue(Array.from(current));
  }

  selectionChecked(path: (string | number)[], option: string): boolean {
    const control = this.form.get(path);
    return (control?.value as string[] | undefined)?.includes(option) ?? false;
  }

  openTermo(): void {
    const value = this.form.value;
    const termoWindow = window.open('', '_blank', 'width=900,height=1200');
    if (!termoWindow) return;

    termoWindow.document.write(`
      <html>
        <head>
          <title>Termo de Voluntariado</title>
          <style>
            body { font-family: 'Inter', Arial, sans-serif; line-height: 1.6; padding: 28px; color: #0f172a; }
            h1 { color: #0f7a43; margin-bottom: 4px; }
            h2 { color: #0f172a; margin-top: 20px; }
            h3 { color: #0ea5e9; margin-top: 16px; }
            p { margin: 10px 0; }
            ul { margin: 8px 0 16px 20px; }
            .section { padding: 12px 14px; border: 1px solid #e2e8f0; border-radius: 12px; background: #f8fafc; margin-top: 12px; }
            .signature { margin-top: 32px; padding-top: 16px; border-top: 1px solid #e2e8f0; display: grid; gap: 12px; }
            .muted { color: #475569; }
          </style>
        </head>
        <body>
          <h1>Termo de Voluntariado</h1>
          <p class="muted">Este documento apresenta as responsabilidades, garantias e autorizações relacionadas à atividade voluntária.</p>
          <div class="section">
            <h2>1. Identificação do voluntário</h2>
            <p><strong>Nome:</strong> ${value.dadosPessoais?.nomeCompleto || '_____________________'} </p>
            <p><strong>CPF:</strong> ${value.dadosPessoais?.cpf || '_____________________'} | <strong>RG:</strong> ${value.dadosPessoais?.rg || '_____________________'}</p>
            <p><strong>Contato:</strong> ${value.contato?.email || '_____________________'} | ${value.contato?.telefone || ''}</p>
          </div>
          <div class="section">
            <h2>2. Termo de adesão ao voluntariado</h2>
            <p>Declaro que atuarei de forma espontânea, sem vínculo empregatício, podendo encerrar minha participação a qualquer momento com aviso prévio.</p>
            <ul>
              <li>Prestarei serviços alinhados à missão institucional, respeitando políticas internas e confidencialidade.</li>
              <li>Receberei orientações de segurança, recursos e supervisão necessários para executar minhas atividades.</li>
              <li>Autorizo o tratamento dos meus dados pessoais exclusivamente para fins de gestão de voluntariado, conforme LGPD.</li>
            </ul>
          </div>
          <div class="section">
            <h2>3. Termo de autorização de uso de imagem</h2>
            <p>Autorizo, de forma gratuita e por prazo indeterminado, a captação e utilização de minha imagem, voz e nome em materiais institucionais, digitais ou impressos, relacionados às ações de voluntariado.</p>
            <p>Esta autorização é revogável mediante solicitação formal, preservando registros já publicados.</p>
          </div>
          <div class="section">
            <h2>4. Saúde e segurança</h2>
            <p>Comprometo-me a seguir as orientações de segurança fornecidas pela instituição e a comunicar qualquer condição que possa afetar minha participação.</p>
          </div>
          <div class="section">
            <h2>5. Vigência e foro</h2>
            <p>O presente termo vigora enquanto durar a participação voluntária. Para dirimir controvérsias, fica eleito o foro da comarca da organização.</p>
          </div>
          <div class="signature">
            <p><strong>Local e data:</strong> ________________________________</p>
            <p><strong>Assinatura do(a) voluntário(a):</strong> _____________________________________________</p>
            <p><strong>Assinatura do(a) representante da instituição:</strong> _______________________________</p>
          </div>
        </body>
      </html>
    `);

    termoWindow.document.close();
    termoWindow.focus();
    termoWindow.print();
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      this.feedback = 'Revise os campos obrigatórios para concluir o cadastro.';
      return;
    }

    this.feedback = null;
    this.saving = true;

    setTimeout(() => {
      this.saving = false;
      this.feedback = 'Cadastro de voluntário salvo com sucesso. Termos registrados com aceite digital.';
    }, 600);
  }
}
