import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { titleCaseWords } from '../../utils/capitalization.util';
import {
  ProntuarioAnexoRequest,
  ProntuarioRegistroRequest,
  ProntuarioRegistroResponse
} from '../../services/prontuario.service';

@Component({
  selector: 'app-prontuario-form-modal',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './prontuario-form-modal.component.html',
  styleUrl: './prontuario-form-modal.component.scss'
})
export class ProntuarioFormModalComponent implements OnChanges {
  @Input() aberto = false;
  @Input() registro: ProntuarioRegistroResponse | null = null;
  @Input() tipoInicial: ProntuarioRegistroRequest['tipo'] | null = null;

  @Output() salvar = new EventEmitter<{ registro: ProntuarioRegistroRequest; anexo?: ProntuarioAnexoRequest }>();
  @Output() fechar = new EventEmitter<void>();

  formulario: FormGroup;

  constructor(private readonly fb: FormBuilder) {
    this.formulario = this.fb.group({
      tipo: ['atendimento', Validators.required],
      dataRegistro: ['', Validators.required],
      profissionalId: [''],
      unidadeId: [''],
      titulo: [''],
      descricao: ['', [Validators.required, Validators.minLength(10)]],
      status: ['aberto', Validators.required],
      demanda: [''],
      orientacoes: [''],
      desfecho: [''],
      destino: [''],
      motivo: [''],
      endereçoConfirmado: [''],
      procedimento: [''],
      anexoNome: [''],
      anexoUrl: [''],
      anexoTipo: ['']
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['registro'] || changes['tipoInicial']) {
      this.preencherFormulario();
    }
  }

  private preencherFormulario(): void {
    if (!this.registro) {
      this.formulario.reset({
        tipo: this.tipoInicial ?? 'atendimento',
        dataRegistro: '',
        profissionalId: '',
        unidadeId: '',
        titulo: '',
        descricao: '',
        status: 'aberto',
        demanda: '',
        orientacoes: '',
        desfecho: '',
        destino: '',
        motivo: '',
        endereçoConfirmado: '',
        procedimento: '',
        anexoNome: '',
        anexoUrl: '',
        anexoTipo: ''
      });
      return;
    }

    const extra = this.registro.dadosExtra || {};
    this.formulario.patchValue({
      tipo: this.registro.tipo,
      dataRegistro: this.registro.dataRegistro,
      profissionalId: this.registro.profissionalId ?? '',
      unidadeId: this.registro.unidadeId ?? '',
      titulo: this.registro.titulo ?? '',
      descricao: this.registro.descricao,
      status: this.registro.status,
      demanda: extra['demanda'] ?? '',
      orientacoes: extra['orientacoes'] ?? '',
      desfecho: extra['desfecho'] ?? '',
      destino: extra['destino'] ?? '',
      motivo: extra['motivo'] ?? '',
      endereçoConfirmado: extra['endereçoConfirmado'] ?? '',
      procedimento: extra['procedimento'] ?? '',
      anexoNome: '',
      anexoUrl: '',
      anexoTipo: ''
    });
  }

  submit(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const value = this.formulario.value;
    const dadosExtra: Record<string, unknown> = {};
    if (value.demanda) dadosExtra['demanda'] = titleCaseWords(value.demanda);
    if (value.orientacoes) dadosExtra['orientacoes'] = titleCaseWords(value.orientacoes);
    if (value.desfecho) dadosExtra['desfecho'] = titleCaseWords(value.desfecho);
    if (value.destino) dadosExtra['destino'] = titleCaseWords(value.destino);
    if (value.motivo) dadosExtra['motivo'] = titleCaseWords(value.motivo);
    if (value.endereçoConfirmado) dadosExtra['endereçoConfirmado'] = titleCaseWords(value.endereçoConfirmado);
    if (value.procedimento) dadosExtra['procedimento'] = titleCaseWords(value.procedimento);

    const registro: ProntuarioRegistroRequest = {
      tipo: value.tipo,
      dataRegistro: value.dataRegistro,
      profissionalId: value.profissionalId ? Number(value.profissionalId) : undefined,
      unidadeId: value.unidadeId ? Number(value.unidadeId) : undefined,
      titulo: value.titulo ? titleCaseWords(value.titulo) : undefined,
      descricao: value.descricao,
      status: value.status,
      dadosExtra
    };

    const anexo =
      value.anexoNome && value.anexoUrl
        ? {
            nomeArquivo: value.anexoNome,
            urlArquivo: value.anexoUrl,
            tipoMime: value.anexoTipo || undefined
          }
        : undefined;

    this.salvar.emit({ registro, anexo });
  }

  close(): void {
    this.fechar.emit();
  }
}
