import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

interface StepTab {
  id: string;
  label: string;
}

interface VolunteerRecord {
  id: string;
  nome: string;
  cpf: string;
  email: string;
  telefone?: string;
  cidade?: string;
  estado?: string;
  areaInteresse?: string;
  habilidades?: string;
  idiomas?: string;
  disponibilidadeDias?: string[];
  disponibilidadePeriodos?: string[];
  cargaHoraria?: string;
  presencial?: boolean;
  remoto?: boolean;
  inicioPrevisto?: string;
  motivacao?: string;
  observacoes?: string;
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

  volunteers: VolunteerRecord[] = [
    {
      id: 'VOL-001',
      nome: 'Ana Clara Martins',
      cpf: '123.456.789-00',
      email: 'ana.martins@email.com',
      telefone: '(11) 98888-1122',
      cidade: 'São Paulo',
      estado: 'SP',
      areaInteresse: 'Educação',
      habilidades: 'Alfabetização, contação de histórias',
      idiomas: 'Português, Inglês',
      disponibilidadeDias: ['Terça-feira', 'Quinta-feira'],
      disponibilidadePeriodos: ['Manhã'],
      cargaHoraria: '4h semanais',
      presencial: true,
      remoto: false,
      inicioPrevisto: '2024-09-01',
      motivacao: 'Contribuir com reforço escolar',
      observacoes: 'Disponível para deslocamentos próximos ao centro.'
    },
    {
      id: 'VOL-002',
      nome: 'Carlos Nogueira',
      cpf: '987.654.321-00',
      email: 'carlos.nogueira@email.com',
      telefone: '(31) 97777-2211',
      cidade: 'Belo Horizonte',
      estado: 'MG',
      areaInteresse: 'Captação de recursos',
      habilidades: 'Planejamento de eventos, comunicação',
      idiomas: 'Português',
      disponibilidadeDias: ['Quarta-feira'],
      disponibilidadePeriodos: ['Noite'],
      cargaHoraria: '3h semanais',
      presencial: false,
      remoto: true,
      inicioPrevisto: '2024-08-20',
      motivacao: 'Ajudar com campanhas',
      observacoes: 'Preferência por atividades online.'
    }
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
            ${this.abntStyles()}
          </style>
        </head>
        <body>
          <header class="abnt-header">
            <div>
              <p class="abnt-organization">Agência Adventista de Desenvolvimento e Recursos Assistenciais</p>
              <h1>TERMO DE ADESÃO AO TRABALHO VOLUNTÁRIO</h1>
            </div>
            <p class="abnt-subtitle">Documento emitido conforme diretrizes de relatório ABNT (margens e fonte 12 pt).</p>
          </header>

          <section class="abnt-section">
            <h2>1. IDENTIFICAÇÃO DO(A) VOLUNTÁRIO(A)</h2>
            <p><strong>Nome completo:</strong> ${value.dadosPessoais?.nomeCompleto || '_____________________'} </p>
            <p><strong>CPF:</strong> ${value.dadosPessoais?.cpf || '_____________________'} | <strong>RG:</strong> ${value.dadosPessoais?.rg || '_____________________'} </p>
            <p><strong>Contato:</strong> ${value.contato?.email || '_____________________'} | ${value.contato?.telefone || ''}</p>
            <p><strong>Área de interesse:</strong> ${value.contato?.areaInteresse || '_____________________'}</p>
          </section>
          <section class="abnt-section">
            <h2>2. TERMO DE ADESÃO AO VOLUNTARIADO</h2>
            <p>Declaro que atuarei de forma espontânea, sem vínculo empregatício, podendo encerrar minha participação a qualquer momento mediante aviso prévio à instituição.</p>
            <ul>
              <li>Prestarei serviços alinhados à missão institucional, respeitando políticas internas e confidencialidade.</li>
              <li>Receberei orientações de segurança, recursos e supervisão necessários para executar minhas atividades.</li>
              <li>Autorizo o tratamento dos meus dados pessoais exclusivamente para fins de gestão de voluntariado, conforme LGPD.</li>
            </ul>
          </section>
          <section class="abnt-section">
            <h2>3. AUTORIZAÇÃO DE USO DE IMAGEM</h2>
            <p>Autorizo, de forma gratuita e por prazo indeterminado, a captação e utilização de minha imagem, voz e nome em materiais institucionais, digitais ou impressos, relacionados às ações de voluntariado.</p>
            <p>Esta autorização é revogável mediante solicitação formal, preservando registros já publicados.</p>
          </section>
          <section class="abnt-section">
            <h2>4. SAÚDE E SEGURANÇA</h2>
            <p>Comprometo-me a seguir as orientações de segurança fornecidas pela instituição e a comunicar qualquer condição que possa afetar minha participação.</p>
          </section>
          <section class="abnt-section">
            <h2>5. VIGÊNCIA E FORO</h2>
            <p>O presente termo vigora enquanto durar a participação voluntária. Para dirimir controvérsias, fica eleito o foro da comarca da organização.</p>
          </section>
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
      const record = this.buildVolunteerRecord();
      this.volunteers = [record, ...this.volunteers];
      this.feedback = 'Cadastro de voluntário salvo com sucesso. Termos registrados com formatação ABNT.';
    }, 600);
  }

  printVolunteer(volunteer: VolunteerRecord): void {
    const printWindow = window.open('', '_blank', 'width=900,height=1200');
    if (!printWindow) return;

    printWindow.document.write(`
      <html>
        <head>
          <title>Ficha do voluntário ${volunteer.nome}</title>
          <style>${this.abntStyles()}</style>
        </head>
        <body>
          <header class="abnt-header">
            <div>
              <p class="abnt-organization">Agência Adventista de Desenvolvimento e Recursos Assistenciais</p>
              <h1>FICHA DE VOLUNTÁRIO</h1>
            </div>
            <p class="abnt-subtitle">Registro completo do voluntário para controle interno.</p>
          </header>
          <section class="abnt-section">
            <h2>1. IDENTIFICAÇÃO</h2>
            <p><strong>Nome:</strong> ${volunteer.nome}</p>
            <p><strong>CPF:</strong> ${volunteer.cpf}</p>
            <p><strong>E-mail:</strong> ${volunteer.email}</p>
            <p><strong>Telefone:</strong> ${volunteer.telefone || 'Não informado'}</p>
            <p><strong>Cidade/UF:</strong> ${volunteer.cidade || '---'} / ${volunteer.estado || '--'}</p>
          </section>
          <section class="abnt-section">
            <h2>2. COMPETÊNCIAS E INTERESSES</h2>
            <p><strong>Área de interesse:</strong> ${volunteer.areaInteresse || 'Não informado'}</p>
            <p><strong>Habilidades:</strong> ${volunteer.habilidades || 'Não informado'}</p>
            <p><strong>Idiomas:</strong> ${volunteer.idiomas || 'Não informado'}</p>
            <p><strong>Motivação:</strong> ${volunteer.motivacao || 'Não informado'}</p>
          </section>
          <section class="abnt-section">
            <h2>3. DISPONIBILIDADE</h2>
            <p><strong>Dias:</strong> ${this.formatArray(volunteer.disponibilidadeDias)}</p>
            <p><strong>Períodos:</strong> ${this.formatArray(volunteer.disponibilidadePeriodos)}</p>
            <p><strong>Carga horária:</strong> ${volunteer.cargaHoraria || 'Não informado'}</p>
            <p><strong>Formato:</strong> ${(volunteer.presencial ? 'Presencial ' : '') + (volunteer.remoto ? 'Remoto' : '') || 'Não informado'}</p>
            <p><strong>Início previsto:</strong> ${volunteer.inicioPrevisto || 'Não informado'}</p>
            <p><strong>Observações:</strong> ${volunteer.observacoes || '---'}</p>
          </section>
        </body>
      </html>
    `);

    printWindow.document.close();
    printWindow.focus();
    printWindow.print();
  }

  printVolunteerList(): void {
    const printWindow = window.open('', '_blank', 'width=900,height=1200');
    if (!printWindow) return;

    const rows = this.volunteers
      .map(
        (volunteer, index) => `
          <tr>
            <td>${index + 1}</td>
            <td>${volunteer.nome}</td>
            <td>${volunteer.areaInteresse || '---'}</td>
            <td>${volunteer.disponibilidadeDias?.join(', ') || '---'}</td>
            <td>${volunteer.telefone || '---'}</td>
          </tr>
        `
      )
      .join('');

    printWindow.document.write(`
      <html>
        <head>
          <title>Relação de voluntários</title>
          <style>
            ${this.abntStyles()}
            table { width: 100%; border-collapse: collapse; margin-top: 16px; }
            th, td { border: 1px solid #cbd5e1; padding: 8px; text-align: left; }
            th { background: #f1f5f9; font-weight: 700; }
          </style>
        </head>
        <body>
          <header class="abnt-header">
            <div>
              <p class="abnt-organization">Agência Adventista de Desenvolvimento e Recursos Assistenciais</p>
              <h1>RELAÇÃO DE VOLUNTÁRIOS</h1>
            </div>
            <p class="abnt-subtitle">Lista de voluntários cadastrados para impressão.</p>
          </header>
          <table>
            <thead>
              <tr>
                <th>#</th>
                <th>Nome</th>
                <th>Área</th>
                <th>Disponibilidade</th>
                <th>Contato</th>
              </tr>
            </thead>
            <tbody>${rows}</tbody>
          </table>
        </body>
      </html>
    `);

    printWindow.document.close();
    printWindow.focus();
    printWindow.print();
  }

  private abntStyles(): string {
    return `
      @page { margin: 3cm 2cm 2cm 3cm; }
      body { font-family: 'Times New Roman', serif; font-size: 12pt; line-height: 1.5; color: #0f172a; }
      h1 { margin: 0 0 12px; font-size: 14pt; text-align: center; font-weight: 700; letter-spacing: 0.02em; }
      h2 { margin: 16px 0 8px; font-size: 12pt; text-transform: uppercase; }
      p, li { margin: 6px 0; }
      ul { margin: 6px 0 6px 18px; padding: 0; }
      .abnt-header { display: flex; align-items: flex-start; justify-content: space-between; gap: 12px; border-bottom: 1px solid #cbd5e1; padding-bottom: 8px; margin-bottom: 12px; }
      .abnt-organization { margin: 0; text-transform: uppercase; font-size: 10pt; letter-spacing: 0.04em; }
      .abnt-subtitle { margin: 0; font-size: 10pt; color: #475569; text-align: right; }
      .abnt-section { margin-top: 8px; border: 1px solid #e2e8f0; padding: 10px 12px; border-radius: 8px; }
      .signature { margin-top: 24px; padding-top: 12px; border-top: 1px solid #cbd5e1; display: grid; gap: 10px; }
    `;
  }

  private buildVolunteerRecord(): VolunteerRecord {
    const value = this.form.value;
    return {
      id: `VOL-${String(this.volunteers.length + 1).padStart(3, '0')}`,
      nome: value.dadosPessoais?.nomeCompleto || 'Voluntário sem nome',
      cpf: value.dadosPessoais?.cpf || '---',
      email: value.contato?.email || '---',
      telefone: value.contato?.telefone,
      cidade: value.contato?.cidade,
      estado: value.contato?.estado,
      areaInteresse: value.contato?.areaInteresse,
      habilidades: value.contato?.habilidades,
      idiomas: value.contato?.idiomas,
      disponibilidadeDias: value.disponibilidade?.dias,
      disponibilidadePeriodos: value.disponibilidade?.periodos,
      cargaHoraria: value.disponibilidade?.cargaHorariaSemanal,
      presencial: value.disponibilidade?.presencial,
      remoto: value.disponibilidade?.remoto,
      inicioPrevisto: value.disponibilidade?.inicioPrevisto,
      motivacao: value.dadosPessoais?.motivacao,
      observacoes: value.disponibilidade?.observacoes
    };
  }

  private formatArray(values?: string[]): string {
    return values?.length ? values.join(', ') : 'Não informado';
  }
}
