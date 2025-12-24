import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

interface DocumentoInstitucional {
  id: string;
  tipoDocumento: string;
  orgaoEmissor: string;
  numeroCodigo: string;
  descricao?: string;
  categoria: string;
  emissao: string;
  validade?: string | null;
  responsavelInterno?: string;
  modoRenovacao: 'Manual' | 'Automática';
  observacaoRenovacao?: string;
  gerarAlerta?: boolean;
  diasAntecedencia?: number[];
  formaAlerta?: string;
  emRenovacao?: boolean;
  semVencimento?: boolean;
  vencimentoIndeterminado?: boolean;
  situacao?: DocumentoSituacao;
}

interface DocumentoAnexo {
  id: string;
  documentoId: string;
  nomeArquivo: string;
  tipo: string;
  dataUpload: string;
  usuario: string;
  dataUrl?: string;
  tamanho?: string;
}

interface DocumentoHistorico {
  id: string;
  documentoId: string;
  dataHora: string;
  usuario: string;
  tipoAlteracao: string;
  observacao: string;
}

type DocumentoSituacao = 'valido' | 'vence_em_breve' | 'vencido' | 'em_renovacao' | 'sem_vencimento';

type AlertaFiltro = 'hoje' | '7' | '30' | '60' | 'vencidos';

@Component({
  selector: 'app-documentos-institucionais',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './documentos-institucionais.component.html',
  styleUrl: './documentos-institucionais.component.scss'
})
export class DocumentosInstitucionaisComponent {
  readonly tabs = [
    { id: 'lista', label: 'Lista de Documentos' },
    { id: 'cadastro', label: 'Cadastro / Edição' },
    { id: 'alertas', label: 'Alertas e Vencimentos' },
    { id: 'anexos', label: 'Anexos e Histórico' },
    { id: 'relatorios', label: 'Relatórios / Dashboard' }
  ];

  activeTab: string = 'lista';
  editingId: string | null = null;
  feedback: string | null = null;

  filterForm: FormGroup;
  documentoForm: FormGroup;
  anexoForm: FormGroup;

  selectedDocumentId: string | null = null;
  alertaFiltro: AlertaFiltro = '30';

  readonly categorias = ['Fiscal', 'Trabalhista', 'Jurídico', 'Contratos', 'Licenças', 'Outros'];
  readonly tiposPadrao = [
    'CND Federal',
    'CND Estadual',
    'CND Municipal',
    'FGTS',
    'Alvará',
    'Contrato',
    'Certificado',
    'Outro'
  ];
  readonly modosRenovacao: Array<'Manual' | 'Automática'> = ['Manual', 'Automática'];

  documentos: DocumentoInstitucional[] = [
    {
      id: '1',
      tipoDocumento: 'CND Federal',
      orgaoEmissor: 'Receita Federal',
      numeroCodigo: '2025/001',
      categoria: 'Fiscal',
      emissao: '2024-12-15',
      validade: '2025-12-15',
      responsavelInterno: 'Setor Fiscal',
      modoRenovacao: 'Manual',
      observacaoRenovacao: 'Renovar no e-CAC com certificado digital',
      gerarAlerta: true,
      diasAntecedencia: [30, 60],
      formaAlerta: 'Sistema',
      situacao: 'valido'
    },
    {
      id: '2',
      tipoDocumento: 'CND Municipal',
      orgaoEmissor: 'Prefeitura Municipal',
      numeroCodigo: '7812/2024',
      categoria: 'Fiscal',
      emissao: '2024-06-01',
      validade: this.addDaysFromToday(15),
      responsavelInterno: 'Tesouraria',
      modoRenovacao: 'Manual',
      gerarAlerta: true,
      diasAntecedencia: [15, 30],
      formaAlerta: 'Sistema',
      situacao: 'vence_em_breve'
    },
    {
      id: '3',
      tipoDocumento: 'Contrato de prestação de serviços',
      orgaoEmissor: 'Fornecedor XPTO',
      numeroCodigo: 'CTR-8891',
      categoria: 'Contratos',
      emissao: '2023-10-01',
      validade: '2024-10-01',
      responsavelInterno: 'Jurídico',
      modoRenovacao: 'Automática',
      observacaoRenovacao: 'Renovação automática anual, avaliar cláusula 7',
      gerarAlerta: true,
      diasAntecedencia: [30],
      formaAlerta: 'Sistema',
      situacao: 'vencido'
    },
    {
      id: '4',
      tipoDocumento: 'Alvará de funcionamento',
      orgaoEmissor: 'Prefeitura',
      numeroCodigo: 'ALV-9988',
      categoria: 'Licenças',
      emissao: '2025-01-02',
      validade: null,
      semVencimento: true,
      responsavelInterno: 'Administrativo',
      modoRenovacao: 'Manual',
      observacaoRenovacao: 'Sem vencimento definido, acompanhar legislações locais',
      gerarAlerta: false,
      formaAlerta: 'Sistema',
      situacao: 'sem_vencimento'
    }
  ];

  anexos: DocumentoAnexo[] = [
    {
      id: 'a1',
      documentoId: '1',
      nomeArquivo: 'CND_Federal_2025.pdf',
      tipo: 'PDF',
      dataUpload: '2025-01-10',
      usuario: 'Maria Lima'
    },
    {
      id: 'a2',
      documentoId: '2',
      nomeArquivo: 'CND_Municipal_2024.pdf',
      tipo: 'PDF',
      dataUpload: '2024-06-02',
      usuario: 'Equipe Fiscal'
    },
    {
      id: 'a3',
      documentoId: '3',
      nomeArquivo: 'Contrato_XPTO.pdf',
      tipo: 'PDF',
      dataUpload: '2023-10-01',
      usuario: 'Jurídico'
    }
  ];

  historico: DocumentoHistorico[] = [
    {
      id: 'h1',
      documentoId: '1',
      dataHora: '2025-01-12 10:30',
      usuario: 'Maria Lima',
      tipoAlteracao: 'Inclusão',
      observacao: 'Documento inserido com alerta de 60 dias.'
    },
    {
      id: 'h2',
      documentoId: '2',
      dataHora: '2024-12-01 09:00',
      usuario: 'Tesouraria',
      tipoAlteracao: 'Mudança de data de vencimento',
      observacao: 'Reemissão pela prefeitura. Novo vencimento para 15 dias.'
    },
    {
      id: 'h3',
      documentoId: '3',
      dataHora: '2024-10-02 14:40',
      usuario: 'Jurídico',
      tipoAlteracao: 'Mudança de situação',
      observacao: 'Contrato vencido, aguardando negociação de renovação.'
    }
  ];

  enviosRecentes = [
    { destinatario: 'Receita Federal', documento: 'CND Federal', protocolo: 'PR-2025-0102', data: '2025-01-15' },
    { destinatario: 'Prefeitura Municipal', documento: 'CND Municipal', protocolo: 'PR-2025-0089', data: '2025-01-10' },
    { destinatario: 'Fornecedor XPTO', documento: 'Contrato de prestação de serviços', protocolo: 'PR-2024-0771', data: '2024-10-05' }
  ];

  constructor(private readonly fb: FormBuilder) {
    this.filterForm = this.fb.group({
      tipoDocumento: [''],
      orgaoEmissor: [''],
      numeroDocumento: [''],
      situacao: [''],
      periodoVencimento: [''],
      orgaoFiltro: ['']
    });

    this.documentoForm = this.fb.group({
      tipoDocumento: ['', Validators.required],
      orgaoEmissor: ['', Validators.required],
      numeroCodigo: ['', Validators.required],
      descricao: [''],
      categoria: ['Fiscal'],
      emissao: ['', Validators.required],
      validade: [''],
      semVencimento: [false],
      vencimentoIndeterminado: [false],
      responsavelInterno: [''],
      modoRenovacao: ['Manual'],
      observacaoRenovacao: [''],
      gerarAlerta: [true],
      diasAntecedencia: this.fb.control<number[]>([30]),
      formaAlerta: ['Sistema'],
      emRenovacao: [false]
    });

    this.anexoForm = this.fb.group({
      nomeArquivo: ['', Validators.required],
      tipo: ['PDF', Validators.required],
      arquivo: [null as File | null, Validators.required]
    });

    this.refreshSituacoes();
    this.selectedDocumentId = this.documentos[0]?.id ?? null;
  }

  get activeTabIndex(): number {
    return this.tabs.findIndex((tab) => tab.id === this.activeTab);
  }

  get hasNextTab(): boolean {
    return this.activeTabIndex < this.tabs.length - 1;
  }

  get hasPreviousTab(): boolean {
    return this.activeTabIndex > 0;
  }

  get nextTabLabel(): string {
    return this.hasNextTab ? this.tabs[this.activeTabIndex + 1].label : '';
  }

  get documentosFiltrados(): DocumentoInstitucional[] {
    const { tipoDocumento, orgaoEmissor, numeroDocumento, situacao, periodoVencimento } = this.filterForm.value;
    return this.documentos
      .filter((doc) =>
        !tipoDocumento || doc.tipoDocumento.toLowerCase().includes((tipoDocumento as string).toLowerCase())
      )
      .filter((doc) => !orgaoEmissor || doc.orgaoEmissor.toLowerCase().includes((orgaoEmissor as string).toLowerCase()))
      .filter((doc) => !numeroDocumento || doc.numeroCodigo.toLowerCase().includes((numeroDocumento as string).toLowerCase()))
      .filter((doc) => !situacao || doc.situacao === situacao)
      .filter((doc) => this.filtrarPorPeriodo(doc, periodoVencimento as string))
      .sort((a, b) => (a.validade || '').localeCompare(b.validade || ''));
  }

  get documentoSelecionado(): DocumentoInstitucional | undefined {
    return this.documentos.find((doc) => doc.id === this.selectedDocumentId);
  }

  get anexosSelecionados(): DocumentoAnexo[] {
    if (!this.selectedDocumentId) return [];
    return this.anexos.filter((anexo) => anexo.documentoId === this.selectedDocumentId);
  }

  get historicoSelecionado(): DocumentoHistorico[] {
    if (!this.selectedDocumentId) return [];
    return this.historico.filter((registro) => registro.documentoId === this.selectedDocumentId);
  }

  get contadorSituacao() {
    return {
      total: this.documentos.length,
      ativos: this.documentos.filter((doc) => doc.situacao === 'valido' || doc.situacao === 'sem_vencimento').length,
      inativos: this.documentos.filter((doc) => doc.situacao === 'em_renovacao').length,
      vencidos: this.documentos.filter((doc) => doc.situacao === 'vencido').length,
      aVencer: this.documentos.filter((doc) => doc.situacao === 'vence_em_breve').length
    };
  }

  get documentosCriticos(): DocumentoInstitucional[] {
    const ordered = [...this.documentos].sort((a, b) => (a.validade || '').localeCompare(b.validade || ''));
    const vencidos = ordered.filter((d) => d.situacao === 'vencido');
    const proximos = ordered.filter((d) => d.situacao === 'vence_em_breve');
    return [...vencidos, ...proximos].filter((doc) => this.filtrarAlertas(doc));
  }

  changeTab(tab: string): void {
    this.feedback = null;
    this.activeTab = tab;
  }

  goToNextTab(): void {
    if (this.hasNextTab) {
      this.activeTab = this.tabs[this.activeTabIndex + 1].id;
    }
  }

  goToPreviousTab(): void {
    if (this.hasPreviousTab) {
      this.activeTab = this.tabs[this.activeTabIndex - 1].id;
    }
  }

  limparFiltros(): void {
    this.filterForm.reset({ situacao: '', periodoVencimento: '' });
  }

  prepararNovoDocumento(): void {
    this.editingId = null;
    this.documentoForm.reset({
      tipoDocumento: '',
      orgaoEmissor: '',
      numeroCodigo: '',
      descricao: '',
      categoria: 'Fiscal',
      emissao: '',
      validade: '',
      semVencimento: false,
      vencimentoIndeterminado: false,
      responsavelInterno: '',
      modoRenovacao: 'Manual',
      observacaoRenovacao: '',
      gerarAlerta: true,
      diasAntecedencia: [30],
      formaAlerta: 'Sistema',
      emRenovacao: false
    });
    this.activeTab = 'cadastro';
  }

  editarDocumento(doc: DocumentoInstitucional): void {
    this.editingId = doc.id;
    this.documentoForm.patchValue({
      ...doc,
      validade: doc.validade || '',
      diasAntecedencia: doc.diasAntecedencia ?? [30]
    });
    this.activeTab = 'cadastro';
  }

  selecionarDocumento(documento: DocumentoInstitucional): void {
    this.selectedDocumentId = documento.id;
  }

  salvarDocumento(): void {
    this.feedback = null;
    if (this.documentoForm.invalid) {
      this.feedback = 'Preencha os campos obrigatórios do documento.';
      this.documentoForm.markAllAsTouched();
      return;
    }

    const valor = this.documentoForm.value as DocumentoInstitucional;

    if (!valor.semVencimento && valor.validade && valor.emissao && valor.validade < valor.emissao) {
      this.feedback = 'A data de vencimento não pode ser anterior à data de emissão.';
      return;
    }

    const payload: DocumentoInstitucional = {
      ...valor,
      validade: valor.semVencimento ? null : valor.validade || null,
      situacao: this.calcularSituacao(valor),
      id: this.editingId ?? crypto.randomUUID()
    };

    if (this.editingId) {
      this.documentos = this.documentos.map((doc) => (doc.id === this.editingId ? { ...doc, ...payload } : doc));
      this.feedback = 'Documento atualizado com sucesso.';
    } else {
      this.documentos = [...this.documentos, payload];
      this.feedback = 'Documento cadastrado com sucesso.';
    }

    this.selectedDocumentId = payload.id;
    this.refreshSituacoes();
    this.activeTab = 'lista';
  }

  excluirDocumento(doc: DocumentoInstitucional): void {
    this.documentos = this.documentos.filter((d) => d.id !== doc.id);
    if (this.selectedDocumentId === doc.id) {
      this.selectedDocumentId = this.documentos[0]?.id ?? null;
    }
    this.refreshSituacoes();
  }

  async adicionarAnexo(): Promise<void> {
    this.feedback = null;
    if (!this.selectedDocumentId) {
      this.feedback = 'Selecione um documento antes de anexar o arquivo digitalizado.';
      return;
    }

    if (this.anexoForm.invalid) {
      this.feedback = 'Preencha o nome e selecione o arquivo digitalizado.';
      this.anexoForm.markAllAsTouched();
      return;
    }

    const arquivo = this.anexoForm.value.arquivo as File | null;
    if (!arquivo) return;

    const dataUrl = await this.readFileAsDataUrl(arquivo).catch(() => null);

    const payload: DocumentoAnexo = {
      id: crypto.randomUUID(),
      documentoId: this.selectedDocumentId,
      nomeArquivo: this.anexoForm.value.nomeArquivo,
      tipo: this.anexoForm.value.tipo,
      dataUpload: new Date().toISOString().slice(0, 10),
      usuario: 'Usuário atual',
      dataUrl: dataUrl ?? undefined,
      tamanho: this.formatarTamanho(arquivo.size)
    };

    this.anexos = [payload, ...this.anexos];
    this.anexoForm.reset({ tipo: 'PDF', arquivo: null, nomeArquivo: '' });
  }

  registrarHistorico(mensagem: string, tipoAlteracao: string): void {
    if (!this.selectedDocumentId) return;

    const registro: DocumentoHistorico = {
      id: crypto.randomUUID(),
      documentoId: this.selectedDocumentId,
      dataHora: new Date().toISOString().replace('T', ' ').slice(0, 16),
      usuario: 'Usuário atual',
      tipoAlteracao,
      observacao: mensagem
    };

    this.historico = [registro, ...this.historico];
  }

  imprimirDocumento(doc: DocumentoInstitucional): void {
    const janela = window.open('', '_blank', 'width=900,height=1200');
    if (!janela) return;

    const situacao = this.labelSituacao(doc.situacao ?? this.calcularSituacao(doc));

    janela.document.write(`
      <html>
        <head>
          <title>Documento institucional</title>
          <style>
            body { font-family: Arial, sans-serif; padding: 24px; line-height: 1.5; }
            h1 { font-size: 20px; margin-bottom: 4px; }
            h2 { font-size: 16px; margin-top: 24px; }
            table { border-collapse: collapse; width: 100%; margin-top: 12px; }
            th, td { border: 1px solid #e5e7eb; padding: 8px; text-align: left; }
            .pill { display: inline-block; padding: 4px 10px; border-radius: 999px; background: #e0f2fe; color: #0369a1; }
          </style>
        </head>
        <body>
          <h1>Ficha do documento</h1>
          <p class="pill">${situacao}</p>
          <table>
            <tr><th>Tipo</th><td>${doc.tipoDocumento}</td></tr>
            <tr><th>Órgão emissor</th><td>${doc.orgaoEmissor}</td></tr>
            <tr><th>Número</th><td>${doc.numeroCodigo}</td></tr>
            <tr><th>Categoria</th><td>${doc.categoria}</td></tr>
            <tr><th>Emissão</th><td>${doc.emissao}</td></tr>
            <tr><th>Validade</th><td>${doc.validade || 'Sem vencimento'}</td></tr>
            <tr><th>Responsável</th><td>${doc.responsavelInterno || '-'} </td></tr>
            <tr><th>Renovação</th><td>${doc.modoRenovacao}</td></tr>
            <tr><th>Observações</th><td>${doc.descricao || doc.observacaoRenovacao || '-'}</td></tr>
          </table>
        </body>
      </html>
    `);

    janela.document.close();
    janela.focus();
    janela.print();
  }

  labelSituacao(situacao: DocumentoSituacao): string {
    const labels: Record<DocumentoSituacao, string> = {
      valido: 'Válido',
      vence_em_breve: 'Vence em breve',
      vencido: 'Vencido',
      em_renovacao: 'Em renovação',
      sem_vencimento: 'Sem vencimento'
    };
    return labels[situacao];
  }

  classeSituacao(situacao?: DocumentoSituacao): string {
    const mapa: Record<DocumentoSituacao, string> = {
      valido: 'status status--valido',
      vence_em_breve: 'status status--aviso',
      vencido: 'status status--critico',
      em_renovacao: 'status status--info',
      sem_vencimento: 'status status--neutro'
    };
    return situacao ? mapa[situacao] : 'status';
  }

  alternarDiasAntecedencia(dia: number): void {
    const control = this.documentoForm.get('diasAntecedencia');
    const atual = new Set<number>(control?.value ?? []);
    if (atual.has(dia)) {
      atual.delete(dia);
    } else {
      atual.add(dia);
    }
    control?.setValue(Array.from(atual));
  }

  contarVencendo(maxDias: number, minDias = 0): number {
    return this.documentos.filter((d) => {
      const diff = this.diferencaDias(d.validade);
      return diff <= maxDias && diff >= minDias;
    }).length;
  }

  contarPorCategoria(categoria: string): number {
    return this.documentos.filter((d) => d.categoria === categoria).length;
  }

  imprimirListaFiltrada(): void {
    this.documentosFiltrados.forEach((doc) => this.imprimirDocumento(doc));
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    this.anexoForm.patchValue({
      nomeArquivo: file.name,
      tipo: this.detectarTipoArquivo(file),
      arquivo: file
    });
  }

  visualizarAnexo(anexo: DocumentoAnexo): void {
    this.feedback = null;
    if (!anexo.dataUrl) {
      this.feedback = 'Este registro não possui arquivo digitalizado salvo.';
      return;
    }

    const win = window.open('', '_blank', 'width=900,height=1100');
    if (!win) return;

    const isPdf = anexo.tipo.toLowerCase() === 'pdf' || anexo.nomeArquivo.toLowerCase().endsWith('.pdf');
    if (isPdf) {
      win.document.write(`<iframe src="${anexo.dataUrl}" style="width:100%;height:100%;" frameborder="0"></iframe>`);
    } else {
      win.document.write(
        `<img src="${anexo.dataUrl}" alt="${anexo.nomeArquivo}" style="max-width:100%;height:auto;display:block;margin:12px auto;" />`
      );
    }
  }

  private detectarTipoArquivo(file: File): string {
    if (file.type.includes('pdf') || file.name.toLowerCase().endsWith('.pdf')) return 'PDF';
    if (file.type.includes('png') || file.name.toLowerCase().endsWith('.png')) return 'PNG';
    if (file.type.includes('jpg') || file.type.includes('jpeg') || file.name.toLowerCase().match(/\.jpe?g$/)) return 'JPG';
    return 'PDF';
  }

  private formatarTamanho(bytes: number): string {
    if (!bytes) return '—';
    const mb = bytes / (1024 * 1024);
    return mb >= 1 ? `${mb.toFixed(2)} MB` : `${(bytes / 1024).toFixed(0)} KB`;
  }

  private readFileAsDataUrl(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.onload = () => resolve(reader.result as string);
      reader.onerror = reject;
      reader.readAsDataURL(file);
    });
  }

  private filtrarPorPeriodo(doc: DocumentoInstitucional, periodo?: string): boolean {
    if (!periodo) return true;
    if (!doc.validade) return periodo === 'sem_vencimento';

    const dias = Number(periodo);
    const diff = this.diferencaDias(doc.validade);
    return diff <= dias && diff >= 0;
  }

  private filtrarAlertas(doc: DocumentoInstitucional): boolean {
    if (this.alertaFiltro === 'vencidos') return doc.situacao === 'vencido';
    if (this.alertaFiltro === 'hoje') return this.diferencaDias(doc.validade) === 0;

    const dias = Number(this.alertaFiltro);
    if (isNaN(dias)) return true;
    const diff = this.diferencaDias(doc.validade);
    return diff >= 0 && diff <= dias;
  }

  private refreshSituacoes(): void {
    this.documentos = this.documentos.map((doc) => ({
      ...doc,
      situacao: this.calcularSituacao(doc)
    }));
  }

  private calcularSituacao(doc: DocumentoInstitucional): DocumentoSituacao {
    if (doc.emRenovacao) return 'em_renovacao';
    if (doc.semVencimento) return 'sem_vencimento';
    if (!doc.validade) return 'valido';

    const diff = this.diferencaDias(doc.validade);
    if (diff < 0) return 'vencido';
    if (diff <= 30) return 'vence_em_breve';
    return 'valido';
  }

  diferencaDias(data?: string | null): number {
    if (!data) return Number.MAX_SAFE_INTEGER;
    const hoje = new Date();
    const vencimento = new Date(data);
    const diffTime = vencimento.getTime() - hoje.getTime();
    return Math.floor(diffTime / (1000 * 60 * 60 * 24));
  }

  private addDaysFromToday(days: number): string {
    const date = new Date();
    date.setDate(date.getDate() + days);
    return date.toISOString().slice(0, 10);
  }
}
