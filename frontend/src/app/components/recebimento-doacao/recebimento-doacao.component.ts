import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faClipboardList, faHandshake, faIdCard, faListCheck, faUserPlus } from '@fortawesome/free-solid-svg-icons';
import { PopupMessagesComponent } from '../compartilhado/popup-messages/popup-messages.component';
import { TelaPadraoComponent } from '../compartilhado/tela-padrao/tela-padrao.component';
import { PopupErrorBuilder } from '../../utils/popup-error.builder';
import {
  DoadorResponse,
  RecebimentoDoacaoResponse,
  RecebimentoDoacaoService
} from '../../services/recebimento-doacao.service';

interface TabItem {
  id: 'doador' | 'dados' | 'recorrencia' | 'gestao' | 'lista';
  label: string;
  icon: any;
}

@Component({
  selector: 'app-recebimento-doacao',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FontAwesomeModule, TelaPadraoComponent, PopupMessagesComponent],
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
    { id: 'dados', label: 'Dados da doacao', icon: faHandshake },
    { id: 'recorrencia', label: 'Recorrencia', icon: faListCheck },
    { id: 'gestao', label: 'Gestao de doacao', icon: faIdCard },
    { id: 'lista', label: 'Listagem', icon: faClipboardList }
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
  recebimentos: RecebimentoDoacaoResponse[] = [];
  doadorSelecionadoId: number | null = null;
  carregandoDoadores = false;
  carregandoRecebimentos = false;

  get isDoacaoDinheiro(): boolean {
    const tipo = (this.recebimentoForm.get('tipoDoacao')?.value ?? '').toString().toLowerCase();
    return tipo.includes('dinheiro');
  }

  constructor(
    private readonly fb: FormBuilder,
    private readonly service: RecebimentoDoacaoService
  ) {
    this.doadorForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      tipoPessoa: ['fisica', Validators.required],
      documento: ['', [Validators.required, this.cpfValidator]],
      email: ['', [Validators.email]],
      telefone: ['', [this.telefoneValidator]],
      observacoes: ['']
    });

    this.recebimentoForm = this.fb.group({
      doadorId: [null, Validators.required],
      tipoDoacao: ['', Validators.required],
      dataRecebimento: ['', Validators.required],
      valor: [''],
      formaRecebimento: [''],
      status: ['Aguardando', Validators.required],
      descricao: [''],
      observacoes: ['']
    });

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
    if (tab === 'doador') {
      this.doadorSelecionadoId = null;
      this.carregarDoadores();
    }
    if (tab === 'lista') {
      this.carregarRecebimentos();
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
    this.doadorForm.reset({ tipoPessoa: 'fisica' });
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
    this.service.listarDoadores().subscribe({
      next: (lista: DoadorResponse[]) => {
        this.doadores = lista;
        this.carregandoDoadores = false;
      },
      error: () => {
        this.doadores = [];
        this.carregandoDoadores = false;
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
  }

  onTipoDoacaoChange(): void {
    if (!this.isDoacaoDinheiro) {
      this.recebimentoForm.get('formaRecebimento')?.setValue('');
      this.recebimentoForm.get('valor')?.setValue('');
    }
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

  onCpfInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '').slice(0, 11);
    const parts = [digits.slice(0, 3), digits.slice(3, 6), digits.slice(6, 9), digits.slice(9, 11)].filter(Boolean);
    input.value = parts.length > 3 ? `${parts[0]}.${parts[1]}.${parts[2]}-${parts[3]}` : parts.join('.');
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

  onValorInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    const digits = input.value.replace(/\D/g, '');
    const value = Number(digits) / 100;
    const formatted = value.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
    input.value = `R$ ${formatted}`;
    this.recebimentoForm.get('valor')?.setValue(value, { emitEvent: false });
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

  private salvarDoador(): void {
    if (this.doadorForm.invalid) {
      this.doadorForm.markAllAsTouched();
      return;
    }
    this.service.criarDoador(this.doadorForm.getRawValue()).subscribe({
      next: (response: DoadorResponse) => {
        this.doadores = [response, ...this.doadores];
        this.recebimentoForm.get('doadorId')?.setValue(response.id);
        this.changeTab('dados');
      },
      error: () => {
        this.popupErros = new PopupErrorBuilder().adicionar('Nao foi possivel salvar o doador.').build();
      }
    });
  }

  private salvarRecebimento(): void {
    if (this.recebimentoForm.invalid) {
      this.recebimentoForm.markAllAsTouched();
      return;
    }

    const recorrencia = this.recorrenciaForm.getRawValue();
    const payload = {
      ...this.recebimentoForm.getRawValue(),
      recorrente: !!recorrencia.recorrente,
      periodicidade: recorrencia.periodicidade,
      proximaCobranca: recorrencia.proximaCobranca || undefined
    };

    this.service.criarRecebimento(payload).subscribe({
      next: (response: RecebimentoDoacaoResponse) => {
        this.recebimentos = [response, ...this.recebimentos];
        this.changeTab('lista');
      },
      error: () => {
        this.popupErros = new PopupErrorBuilder().adicionar('Nao foi possivel registrar o recebimento.').build();
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
