import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { Subject, takeUntil } from 'rxjs';
import {
  ProntuarioAtendimento,
  ProntuarioEncaminhamento,
  ProntuarioParticipacao,
  ProntuarioPayload,
  ProntuarioService
} from '../../services/prontuario.service';

@Component({
  selector: 'app-prontuario',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './prontuario.component.html',
  styleUrl: './prontuario.component.scss'
})
export class ProntuarioComponent implements OnInit, OnDestroy {
  prontuarioForm: FormGroup;
  beneficiarioId: string | null = null;
  carregando = false;
  salvando = false;
  feedback: { tipo: 'sucesso' | 'erro'; mensagem: string } | null = null;
  composicaoFamiliar: any[] = [];

  private destroy$ = new Subject<void>();

  get vulnerabilidadesControl() {
    return this.prontuarioForm.get('situacoesVulnerabilidade');
  }

  onVulnerabilidadesInput(event: Event): void {
    const target = event.target as HTMLTextAreaElement | null;
    const value = target?.value ?? '';

    const linhas = value
      .split(/\r?\n+/)
      .map((l) => l.trim())
      .filter((l) => l.length > 0);

    this.vulnerabilidadesControl?.setValue(linhas);
  }

  get participacoes(): FormArray {
    return this.prontuarioForm.get('participacoesServicos') as FormArray;
  }

  get historicoAtendimentos(): FormArray {
    return this.prontuarioForm.get('historicoAtendimentos') as FormArray;
  }

  get encaminhamentos(): FormArray {
    return this.prontuarioForm.get('encaminhamentos') as FormArray;
  }

  constructor(
    private readonly fb: FormBuilder,
    private readonly route: ActivatedRoute,
    private readonly prontuarioService: ProntuarioService
  ) {
    this.prontuarioForm = this.fb.group({
      identificacao: this.fb.group({
        unidadeReferencia: [''],
        responsavelTecnico: [''],
        dataAbertura: [''],
        observacoes: ['']
      }),
      composicaoFamiliar: this.fb.group({
        resumo: ['']
      }),
      situacoesVulnerabilidade: this.fb.control<string[]>([]),
      participacoesServicos: this.fb.array([]),
      historicoAtendimentos: this.fb.array([]),
      encaminhamentos: this.fb.array([]),
      parecerTecnico: ['']
    });
  }

  ngOnInit(): void {
    this.route.paramMap.pipe(takeUntil(this.destroy$)).subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.beneficiarioId = id;
        this.carregarProntuario(id);
      }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  adicionarParticipacao(participacao?: ProntuarioParticipacao) {
    this.participacoes.push(
      this.fb.group({
        servicoOuProjeto: [participacao?.servicoOuProjeto ?? ''],
        papel: [participacao?.papel ?? ''],
        situacao: [participacao?.situacao ?? ''],
        dataInicio: [participacao?.dataInicio ?? ''],
        dataFim: [participacao?.dataFim ?? ''],
        observacoes: [participacao?.observacoes ?? '']
      })
    );
  }

  removerParticipacao(index: number) {
    this.participacoes.removeAt(index);
  }

  adicionarAtendimento(atendimento?: ProntuarioAtendimento) {
    this.historicoAtendimentos.push(
      this.fb.group({
        idProntuarioAtendimento: [atendimento?.idProntuarioAtendimento],
        dataAtendimento: [atendimento?.dataAtendimento ?? ''],
        tipoAtendimento: [atendimento?.tipoAtendimento ?? ''],
        descricao: [atendimento?.descricao ?? ''],
        responsavel: [atendimento?.responsavel ?? ''],
        resultado: [atendimento?.resultado ?? '']
      })
    );
  }

  removerAtendimento(index: number) {
    this.historicoAtendimentos.removeAt(index);
  }

  adicionarEncaminhamento(encaminhamento?: ProntuarioEncaminhamento) {
    this.encaminhamentos.push(
      this.fb.group({
        idProntuarioEncaminhamento: [encaminhamento?.idProntuarioEncaminhamento],
        dataEncaminhamento: [encaminhamento?.dataEncaminhamento ?? ''],
        destino: [encaminhamento?.destino ?? ''],
        motivo: [encaminhamento?.motivo ?? ''],
        responsavel: [encaminhamento?.responsavel ?? ''],
        status: [encaminhamento?.status ?? ''],
        retornoPrevisto: [encaminhamento?.retornoPrevisto ?? ''],
        observacoes: [encaminhamento?.observacoes ?? '']
      })
    );
  }

  removerEncaminhamento(index: number) {
    this.encaminhamentos.removeAt(index);
  }

  carregarProntuario(beneficiarioId: string) {
    this.carregando = true;
    this.feedback = null;
    this.prontuarioService
      .carregar(beneficiarioId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data) => {
          this.carregando = false;
          this.composicaoFamiliar = data.composicaoFamiliar;
          this.prontuarioForm.patchValue({
            identificacao: data.prontuario?.identificacao ?? {},
            composicaoFamiliar: data.prontuario?.composicaoFamiliar ?? {},
            situacoesVulnerabilidade: data.prontuario?.situacoesVulnerabilidade ?? [],
            parecerTecnico: data.prontuario?.parecerTecnico ?? ''
          });

          this.participacoes.clear();
          (data.prontuario?.participacoesServicos ?? []).forEach((p) => this.adicionarParticipacao(p));

          this.historicoAtendimentos.clear();
          (data.historicoAtendimentos ?? []).forEach((h) => this.adicionarAtendimento(h));

          this.encaminhamentos.clear();
          (data.encaminhamentos ?? []).forEach((e) => this.adicionarEncaminhamento(e));

          if (!this.participacoes.length) this.adicionarParticipacao();
          if (!this.historicoAtendimentos.length) this.adicionarAtendimento();
          if (!this.encaminhamentos.length) this.adicionarEncaminhamento();
        },
        error: (error) => {
          this.carregando = false;
          this.feedback = { tipo: 'erro', mensagem: error?.error?.message ?? 'Erro ao carregar prontuário' };
        }
      });
  }

  salvar() {
    if (!this.beneficiarioId) return;
    this.salvando = true;
    this.feedback = null;

    const payload: ProntuarioPayload = {
      ...this.prontuarioForm.value,
      historicoAtendimentos: this.historicoAtendimentos.value,
      encaminhamentos: this.encaminhamentos.value,
      participacoesServicos: this.participacoes.value,
      composicaoFamiliar: {
        ...this.prontuarioForm.value.composicaoFamiliar,
        membros: this.composicaoFamiliar.map((m) => ({
          id: m.idFamiliaMembro ?? m.id,
          nome: m.beneficiario?.nomeCompleto ?? m.nome,
          parentesco: m.parentesco,
          renda: m.rendaIndividual ?? m.beneficiario?.rendaMensal,
          responsavelFamiliar: m.responsavelFamiliar
        }))
      }
    } as ProntuarioPayload;

    this.prontuarioService
      .salvar(this.beneficiarioId, payload)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          this.salvando = false;
          this.feedback = { tipo: 'sucesso', mensagem: 'Prontuário salvo com sucesso.' };
        },
        error: (error) => {
          this.salvando = false;
          this.feedback = { tipo: 'erro', mensagem: error?.error?.message ?? 'Erro ao salvar prontuário' };
        }
      });
  }
}
