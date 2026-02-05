import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faClipboardList, faHandshake, faIdCard, faListCheck, faUserPlus } from '@fortawesome/free-solid-svg-icons';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { PopupErrorBuilder } from '../../utils/popup-error.builder';
import { AutocompleteComponent, AutocompleteOpcao } from '../compartilhado/autocomplete/autocomplete.component';
import { DialogComponent } from '../compartilhado/dialog/dialog.component';
import {
  DoadorResponse,
  RecebimentoDoacaoResponse,
  RecebimentoDoacaoService
} from '../../services/recebimento-doacao.service';

interface TabItem {
  id: 'doador' | 'doadores' | 'dados' | 'recorrencia' | 'gestao' | 'lista';
  label: string;
  icon: any;
}

@Component({
  selector: 'app-recebimento-doacao',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FontAwesomeModule,
    TelaPadraoComponent,
    PopupMessagesComponent,
    AutocompleteComponent,
    DialogComponent
  ],
  templateUrl: './recebimento-doacao.component.html',
  styleUrl: './recebimento-doacao.component.scss'
})
export class RecebimentoDoacaoComponent implements OnInit {
  readonly faClipboardList = faClipboardList;
  readonly faUserPlus = faUserPlus;
  readonly faHandshake = faHandshake;
  readonly faIdCard = faIdCard;
  readonly faListCheck = faListCheck;

  tabs: TabItem[] = [
    { id: 'doador', label: 'Cadastro do doador', icon: faUserPlus },
    { id: 'doadores', label: 'Listagem de doadores', icon: faClipboardList },
    { id: 'dados', label: 'Dados da doacao', icon: faHandshake },
    { id: 'recorrencia', label: 'Recorrencia', icon: faListCheck },
    { id: 'gestao', label: 'Gestao de doacao', icon: faIdCard },
    { id: 'lista', label: 'Listagem de doacoes', icon: faClipboardList }
  ];

  activeTab: TabItem['id'] = 'doador';

  acoesToolbar = {
    salvar: true,
    excluir: true,
    novo: true,
    cancelar: true,
    imprimir: true,
    buscar: true
  };

  acoesDesabilitadas = {
    salvar: false,
    excluir: true,
    novo: false,
    cancelar: false,
    imprimir: false,
    buscar: false
  };

  popupErros: string[] = [];

  doadorForm: FormGroup;
  recebimentoForm: FormGroup;
  recorrenciaForm: FormGroup;
  gestaoForm: FormGroup;

  doadores: DoadorResponse[] = [];
  doadoresOpcoes: AutocompleteOpcao[] = [];
  doadoresCarregando = false;
  doadoresErro: string | null = null;
  termoBuscaDoador = '';
  recebimentos: RecebimentoDoacaoResponse[] = [];
  doadorSelecionadoId: number | null = null;
  carregandoDoadores = false;
  carregandoRecebimentos = false;
  recebimentoEditandoId: number | null = null;
  dialogExcluirDoadorAberto = false;
  doadorParaExcluir: DoadorResponse | null = null;

  get isDoacaoDinheiro(): boolean {
    const tipo = (this.recebimentoForm.get('tipoDoacao')?.value ?? '').toString().toLowerCase();
    return tipo.includes('dinheiro');
  }

  get isDoacaoItens(): boolean {
    return !this.isDoacaoDinheiro;
  }

  constructor(
    private readonly fb: FormBuilder,
    private readonly service: RecebimentoDoacaoService
  ) {
    this.doadorForm = this.fb.group({
      tipoPessoa: ['FISICA', Validators.required],
      nome: ['', [Validators.required, Validators.minLength(3)]],
      documento: ['', [Validators.required, this.cpfValidator]], 
      responsavelEmpresa: [''],
      email: ['', [Validators.email]],
      telefone: ['', [this.telefoneValidator]],
      logradouro: [''],
      numero: [''],
      complemento: [''],
      bairro: [''],
      cidade: [''],
      uf: [''],
      cep: [''],
      observacoes: ['']
    });
    this.atualizarTipoPessoaValidators();

    this.recebimentoForm = this.fb.group({
      doadorId: [null, Validators.required],
      tipoDoacao: ['', Validators.required],
      dataRecebimento: ['', Validators.required],
      valor: [''],
      quantidadeItens: [''],
      valorMedio: [''],
      valorTotal: [''],
      formaRecebimento: [''],
      status: ['Aguardando', Validators.required],
      descricao: [''],
      observacoes: ['']
    });
    this.atualizarValidatorsRecebimento();
    this.onTipoDoacaoChange();

    this.recorrenciaForm = this.fb.group({
      recorrente: [false],
      periodicidade: ['Mensal'],
      proximaCobranca: ['']
    });

    this.gestaoForm = this.fb.group({
      canal: ['whatsapp'],
      mensagem: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  ngOnInit(): void {
    this.carregarDoadores();
    this.carregarRecebimentos();
  }

  changeTab(tab: TabItem['id']): void {
    this.activeTab = tab;
    if (tab === 'doador' || tab === 'doadores') {
      this.doadorSelecionadoId = null;
      this.carregarDoadores();
    }
    if (tab === 'lista') {
      this.carregarRecebimentos();
    }
    if (tab === 'dados') {
      const selectedId = this.recebimentoForm.get('doadorId')?.value;
      const selected = this.doadores.find((doador) => doador.id === Number(selectedId));
      this.termoBuscaDoador = selected?.nome || '';
    }
  }

  getTabIndex(id: TabItem['id']): number {
    return this.tabs.findIndex((tab) => tab.id === id);
  }

  onSalvar(): void {
    if (this.activeTab === 'doador') {
      this.salvarDoador();
      return;
    }
    if (this.activeTab === 'dados') {
      this.salvarRecebimento();
      return;
    }
  }

  onExcluir(): void {
    this.popupErros = new PopupErrorBuilder()
      .adicionar('Exclusao nao implementada para esta aba.')
      .build();
  }

  onNovo(): void {
    this.doadorForm.reset({ tipoPessoa: 'FISICA' });
    this.recebimentoForm.reset({ status: 'Aguardando' });
    this.recorrenciaForm.reset({ recorrente: false, periodicidade: 'Mensal' });
    this.gestaoForm.reset({ canal: 'whatsapp' });
    this.popupErros = [];
  }

  onCancelar(): void {
    this.onNovo();
    this.changeTab('doador');
  }

  onImprimir(): void {
    window.print();
  }

  onBuscar(): void {
    this.carregarRecebimentos();
  }

  fecharPopupErros(): void {
    this.popupErros = [];
  }

  carregarDoadores(): void {
    this.carregandoDoadores = true;
    this.doadoresCarregando = true;
    this.doadoresErro = null;
    this.service.listarDoadores().subscribe({
      next: (lista: DoadorResponse[]) => {
        this.doadores = lista;
        this.doadoresOpcoes = lista.map((doador) => ({
          id: doador.id,
          label: doador.nome
        }));
        this.carregandoDoadores = false;
        this.doadoresCarregando = false;
      },
      error: () => {
        this.doadores = [];
        this.carregandoDoadores = false;
        this.doadoresCarregando = false;
        this.doadoresErro = 'NÃ£o foi possÃ­vel carregar os doadores.';
      }
    });
  }

  carregarRecebimentos(): void {
    this.carregandoRecebimentos = true;
    this.service.listarRecebimentos().subscribe({
      next: (lista: RecebimentoDoacaoResponse[]) => {
        this.recebimentos = lista;
        this.carregandoRecebimentos = false;
      },
      error: () => {
        this.recebimentos = [];
        this.carregandoRecebimentos = false;
      }
    });
  }

  selecionarDoador(doador: DoadorResponse): void {
    this.recebimentoForm.get('doadorId')?.setValue(doador.id);
    this.doadorSelecionadoId = doador.id;
    this.termoBuscaDoador = doador.nome;
  }

  abrirDialogoExcluirDoador(doador: DoadorResponse): void {
    this.doadorParaExcluir = doador;
    this.dialogExcluirDoadorAberto = true;
  }

  cancelarExcluirDoador(): void {
    this.dialogExcluirDoadorAberto = false;
    this.doadorParaExcluir = null;
  }

  confirmarExcluirDoador(): void {
    if (!this.doadorParaExcluir) {
      this.cancelarExcluirDoador();
      return;
    }
    const doador = this.doadorParaExcluir;
    this.service.excluirDoador(doador.id).subscribe({
      next: () => {
        this.doadores = this.doadores.filter((item) => item.id !== doador.id);
        this.doadoresOpcoes = this.doadores.map((item) => ({
          id: item.id,
          label: item.nome
        }));
        if (this.doadorSelecionadoId === doador.id) {
          this.doadorSelecionadoId = null;
          this.recebimentoForm.get('doadorId')?.setValue(null);
          this.termoBuscaDoador = '';
        }
        this.cancelarExcluirDoador();
      },
      error: () => {
        this.popupErros = new PopupErrorBuilder().adicionar('Nao foi possivel excluir o doador.').build();
      }
    });
  }

  onDoadorTermoChange(termo: string): void {
    this.termoBuscaDoador = termo;
  }

  onDoadorSelecionado(opcao: AutocompleteOpcao): void {
    this.recebimentoForm.get('doadorId')?.setValue(opcao.id);
    this.doadorSelecionadoId = Number(opcao.id);
    this.termoBuscaDoador = opcao.label;
  }

  get doadoresFiltrados(): AutocompleteOpcao[] {
    const termo = (this.termoBuscaDoador || '').trim().toLowerCase();
    if (!termo) {
      return this.doadoresOpcoes.slice(0, 15);
    }
    return this.doadoresOpcoes
      .filter((doador) => doador.label.toLowerCase().includes(termo))
      .slice(0, 15);
  }

  onTipoDoacaoChange(): void {
    if (!this.isDoacaoDinheiro) {
      this.recebimentoForm.get('formaRecebimento')?.setValue('');
      this.recebimentoForm.get('valor')?.setValue('');
      this.recebimentoForm.get('formaRecebimento')?.disable({ emitEvent: false });
    } else {
      this.recebimentoForm.get('formaRecebimento')?.enable({ emitEvent: false });
      this.recebimentoForm.get('quantidadeItens')?.setValue('');
      this.recebimentoForm.get('valorMedio')?.setValue('');
      this.recebimentoForm.get('valorTotal')?.setValue('');
    }
    this.atualizarValidatorsRecebimento();
  }

  private atualizarValidatorsRecebimento(): void {
    const quantidade = this.recebimentoForm.get('quantidadeItens');
    const valorMedio = this.recebimentoForm.get('valorMedio');
    const valorTotal = this.recebimentoForm.get('valorTotal');
    const valor = this.recebimentoForm.get('valor');
    if (this.isDoacaoDinheiro) {
      quantidade?.clearValidators();
      valorMedio?.clearValidators();
      valorTotal?.clearValidators();
      valor?.setValidators([Validators.required]);
    } else {
      quantidade?.setValidators([Validators.required]);
      valorMedio?.setValidators([Validators.required]);
      valorTotal?.setValidators([Validators.required]);
      valor?.clearValidators();
    }
    quantidade?.updateValueAndValidity({ emitEvent: false });
    valorMedio?.updateValueAndValidity({ emitEvent: false });
    valorTotal?.updateValueAndValidity({ emitEvent: false });
    valor?.updateValueAndValidity({ emitEvent: false });
  }

  atualizarProximaCobranca(): void {
    if (!this.recorrenciaForm.get('recorrente')?.value) {
      return;
    }
    const base = new Date();
    base.setMonth(base.getMonth() + 1);
    const iso = base.toISOString().slice(0, 10);
    this.recorrenciaForm.get('proximaCobranca')?.setValue(iso);
  }

  aplicarTemplateMensagem(tipo: 'lembrete' | 'agradecimento' | 'transparencia'): void {
    const templates: Record<string, string> = {
      lembrete: 'Ola! Passando para lembrar sobre a doacao programada. Podemos ajudar em algo?',
      agradecimento: 'Obrigado pelo apoio! Sua doacao faz a diferenca no atendimento social.',
      transparencia: 'Segue um resumo da aplicacao dos recursos recebidos. Obrigado pela parceria.'
    };
    this.gestaoForm.get('mensagem')?.setValue(templates[tipo]);
  }

  enviarMensagemGestao(): void {
    if (this.gestaoForm.invalid) {
      this.gestaoForm.markAllAsTouched();
      return;
    }
    this.popupErros = new PopupErrorBuilder().adicionar('Mensagem preparada para envio.').build();
  }

  getNomeDoador(doadorId?: number | null): string {
    const found = this.doadores.find((item) => item.id === doadorId);
    return found?.nome || '---';
  }

  get isPessoaJuridica(): boolean {
    return this.doadorForm.get('tipoPessoa')?.value === 'JURIDICA';
  }

  get documentoLabel(): string {
    return this.isPessoaJuridica ? 'Documento (CNPJ)' : 'Documento (CPF)';
  }

  get documentoPlaceholder(): string {
    return this.isPessoaJuridica ? '00.000.000/0000-00' : '000.000.000-00';
  }

  onTipoPessoaChange(): void {
    this.doadorForm.get('documento')?.reset();
    this.doadorForm.get('responsavelEmpresa')?.reset();
    this.atualizarTipoPessoaValidators();
  }

  private atualizarTipoPessoaValidators(): void {
    const documentoControl = this.doadorForm.get('documento');
    const responsavelControl = this.doadorForm.get('responsavelEmpresa');
    if (this.isPessoaJuridica) {
      documentoControl?.setValidators([Validators.required, this.cnpjValidator]);
      responsavelControl?.setValidators([Validators.required, Validators.minLength(3)]);
    } else {
      documentoControl?.setValidators([Validators.required, this.cpfValidator]);
      responsavelControl?.clearValidators();
    }
    documentoControl?.updateValueAndValidity({ emitEvent: false });
    responsavelControl?.updateValueAndValidity({ emitEvent: false });
  }

  onDocumentoInput(event: Event): void {
    if (this.isPessoaJuridica) {
      this.onCnpjInput(event);
      return;
    }
    this.onCpfInput(event);
  }

  onCpfInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 11);
    const parts = [digits.slice(0, 3), digits.slice(3, 6), digits.slice(6, 9), digits.slice(9, 11)].filter(Boolean);
    input.value = parts.length > 3 ? `${parts[0]}.${parts[1]}.${parts[2]}-${parts[3]}` : parts.join('.');
    this.doadorForm.get('documento')?.setValue(input.value, { emitEvent: false });
  }

  onCnpjInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 14);
    const parts = [
      digits.slice(0, 2),
      digits.slice(2, 5),
      digits.slice(5, 8),
      digits.slice(8, 12),
      digits.slice(12, 14)
    ].filter(Boolean);
    let masked = parts[0] ?? '';
    if (parts[1]) masked += `.${parts[1]}`;
    if (parts[2]) masked += `.${parts[2]}`;
    if (parts[3]) masked += `/${parts[3]}`;
    if (parts[4]) masked += `-${parts[4]}`;
    input.value = masked;
    this.doadorForm.get('documento')?.setValue(input.value, { emitEvent: false });
  }

  onTelefoneInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 11);
    const ddd = digits.slice(0, 2);
    const parte1 = digits.slice(2, digits.length > 10 ? 7 : 6);
    const parte2 = digits.slice(digits.length > 10 ? 7 : 6, 11);
    input.value = parte2 ? `(${ddd}) ${parte1}-${parte2}` : ddd ? `(${ddd}) ${parte1}` : parte1;
    this.doadorForm.get('telefone')?.setValue(input.value, { emitEvent: false });
  }

  onCepInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 8);
    const masked = digits.replace(/(\d{5})(\d{0,3})/, (_, p1, p2) => (p2 ? `${p1}-${p2}` : p1));
    input.value = masked;
    this.doadorForm.get('cep')?.setValue(masked, { emitEvent: false });
  }

  onValorInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const value = this.formatCurrencyInput(input, true);
    this.recebimentoForm.get('valor')?.setValue(value, { emitEvent: false });
  }

  onValorMedioInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const value = this.formatCurrencyInput(input, true);
    this.recebimentoForm.get('valorMedio')?.setValue(value, { emitEvent: false });
    this.atualizarValorTotalItens();
  }

  onValorTotalInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const value = this.formatCurrencyInput(input, true);
    this.recebimentoForm.get('valorTotal')?.setValue(value, { emitEvent: false });
  }

  onQuantidadeItensChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    const value = Number(input.value || 0);
    this.recebimentoForm.get('quantidadeItens')?.setValue(value, { emitEvent: false });
    this.atualizarValorTotalItens();
  }

  private atualizarValorTotalItens(): void {
    const quantidade = Number(this.recebimentoForm.get('quantidadeItens')?.value || 0);
    const valorMedio = this.parseCurrencyValue(this.recebimentoForm.get('valorMedio')?.value);
    if (!quantidade || !valorMedio) return;
    const total = quantidade * valorMedio;
    this.recebimentoForm
      .get('valorTotal')
      ?.setValue(this.formatCurrencyValue(total, true), { emitEvent: false });
  }

  private formatCurrencyInput(input: HTMLInputElement, comPrefixo: boolean): string {
    const digits = input.value.replace(/\D/g, '');
    const value = Number(digits) / 100;
    const formatted = value.toLocaleString('pt-BR', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    });
    const formattedValue = comPrefixo ? `R$ ${formatted}` : formatted;
    input.value = formattedValue;
    return formattedValue;
  }

  private formatCurrencyValue(value: number, comPrefixo: boolean): string {
    const formatted = value.toLocaleString('pt-BR', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    });
    return comPrefixo ? `R$ ${formatted}` : formatted;
  }

  private parseCurrencyValue(value: string | number | null | undefined): number {
    if (value === null || value === undefined || value === '') {
      return 0;
    }
    if (typeof value === 'number') {
      return value;
    }
    const digits = String(value).replace(/\D/g, '');
    return digits ? Number(digits) / 100 : 0;
  }

  onSentenceCaseBlur(event: Event): void {
    const input = event.target as HTMLInputElement;
    const value = (input.value || '').trim();
    if (!value) return;
    const normalized = value
      .split(' ')
      .filter(Boolean)
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
      .join(' ');
    input.value = normalized;
    const control = this.findControlByElement(input);
    control?.setValue(normalized, { emitEvent: false });
  }

  private salvarDoador(manterNaAba = false): void {
    if (this.doadorForm.invalid) {
      this.doadorForm.markAllAsTouched();
      return;
    }
    this.service.criarDoador(this.doadorForm.getRawValue()).subscribe({
      next: (response: DoadorResponse) => {
        this.doadores = [response, ...this.doadores];
        this.recebimentoForm.get('doadorId')?.setValue(response.id);
        this.doadorSelecionadoId = response.id;
        this.termoBuscaDoador = response.nome;
        if (!manterNaAba) {
          this.changeTab('dados');
        }
      },
      error: () => {
        this.popupErros = new PopupErrorBuilder().adicionar('Nao foi possivel salvar o doador.').build();
      }
    });
  }

  salvarDoadorSemDoacao(): void {
    this.salvarDoador(true);
  }

  private salvarRecebimento(): void {
    if (this.recebimentoForm.invalid) {
      this.recebimentoForm.markAllAsTouched();
      return;
    }

    const recorrencia = this.recorrenciaForm.getRawValue();
    const raw = this.recebimentoForm.getRawValue();
    const payload = {
      ...raw,
      valor: this.parseCurrencyValue(raw.valor),
      valorMedio: this.parseCurrencyValue(raw.valorMedio),
      valorTotal: this.parseCurrencyValue(raw.valorTotal),
      recorrente: !!recorrencia.recorrente,
      periodicidade: recorrencia.periodicidade,
      proximaCobranca: recorrencia.proximaCobranca || undefined
    };

    const action$ = this.recebimentoEditandoId
      ? this.service.atualizarRecebimento(this.recebimentoEditandoId, payload)
      : this.service.criarRecebimento(payload);

    action$.subscribe({
      next: (response: RecebimentoDoacaoResponse) => {
        this.recebimentos = this.recebimentoEditandoId
          ? this.recebimentos.map((item) => (item.id === response.id ? response : item))
          : [response, ...this.recebimentos];
        this.recebimentoEditandoId = null;
        this.changeTab('lista');
      },
      error: () => {
        this.popupErros = new PopupErrorBuilder().adicionar('Nao foi possivel registrar o recebimento.').build();
      }
    });
  }

  editarRecebimento(item: RecebimentoDoacaoResponse): void {
    this.recebimentoEditandoId = item.id;
    this.recebimentoForm.patchValue({
      doadorId: item.doadorId ?? null,
      tipoDoacao: item.tipoDoacao,
      dataRecebimento: item.dataRecebimento,
      valor: item.valor !== null && item.valor !== undefined
        ? this.formatCurrencyValue(Number(item.valor), true)
        : '',
      quantidadeItens: item.quantidadeItens ?? '',
      valorMedio: item.valorMedio !== null && item.valorMedio !== undefined
        ? this.formatCurrencyValue(Number(item.valorMedio), true)
        : '',
      valorTotal: item.valorTotal !== null && item.valorTotal !== undefined
        ? this.formatCurrencyValue(Number(item.valorTotal), true)
        : '',
      formaRecebimento: item.formaRecebimento ?? '',
      status: item.status,
      descricao: item.descricao ?? '',
      observacoes: item.observacoes ?? ''
    });
    const selected = this.doadores.find((doador) => doador.id === Number(item.doadorId));
    this.termoBuscaDoador = selected?.nome || item.doadorNome || '';
    this.onTipoDoacaoChange();
    this.changeTab('dados');
  }

  excluirRecebimento(item: RecebimentoDoacaoResponse): void {
    this.service.excluirRecebimento(item.id).subscribe({
      next: () => {
        this.recebimentos = this.recebimentos.filter((registro) => registro.id !== item.id);
      },
      error: () => {
        this.popupErros = new PopupErrorBuilder().adicionar('Nao foi possivel excluir o recebimento.').build();
      }
    });
  }

  private cpfValidator(control: AbstractControl): ValidationErrors | null {
    const digits = (control.value || '').replace(/\D/g, '');
    if (!digits) return null;
    if (digits.length !== 11) return { cpf: true };
    if (/^(\d)\1+$/.test(digits)) return { cpf: true };

    const calc = (slice: number) => {
      let sum = 0;
      for (let i = 0; i < slice; i++) {
        sum += parseInt(digits.charAt(i), 10) * (slice + 1 - i);
      }
      const mod = (sum * 10) % 11;
      return mod === 10 ? 0 : mod;
    };

    const dig1 = calc(9);
    const dig2 = calc(10);
    if (dig1 !== parseInt(digits.charAt(9), 10) || dig2 !== parseInt(digits.charAt(10), 10)) {
      return { cpf: true };
    }
    return null;
  }

  private cnpjValidator(control: AbstractControl): ValidationErrors | null {
    const digits = (control.value || '').replace(/\D/g, '');
    if (!digits) return null;
    if (digits.length !== 14) return { cnpj: true };
    if (/^(\d)\1+$/.test(digits)) return { cnpj: true };

    const calc = (length: number) => {
      const weights =
        length === 12
          ? [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2]
          : [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];
      let sum = 0;
      for (let i = 0; i < weights.length; i++) {
        sum += parseInt(digits.charAt(i), 10) * weights[i];
      }
      const mod = sum % 11;
      return mod < 2 ? 0 : 11 - mod;
    };

    const dig1 = calc(12);
    const dig2 = calc(13);
    if (dig1 !== parseInt(digits.charAt(12), 10) || dig2 !== parseInt(digits.charAt(13), 10)) {
      return { cnpj: true };
    }
    return null;
  }

  private telefoneValidator(control: AbstractControl): ValidationErrors | null {
    const digits = (control.value || '').replace(/\D/g, '');
    if (!digits) return null;
    return digits.length < 10 ? { telefone: true } : null;
  }

  private findControlByElement(element: HTMLInputElement | HTMLTextAreaElement): AbstractControl | null {
    const name = element.getAttribute('formControlName');
    if (!name) return null;
    if (this.doadorForm.get(name)) return this.doadorForm.get(name);
    if (this.recebimentoForm.get(name)) return this.recebimentoForm.get(name);
    if (this.recorrenciaForm.get(name)) return this.recorrenciaForm.get(name);
    if (this.gestaoForm.get(name)) return this.gestaoForm.get(name);
    return null;
  }
}

