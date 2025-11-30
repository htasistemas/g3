import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FamilyService, FamiliaMembroPayload, FamiliaPayload } from '../../services/family.service';
import { BeneficiarioApiService, BeneficiarioApiPayload } from '../../services/beneficiario-api.service';

@Component({
  selector: 'app-familia-cadastro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './familia-cadastro.component.html',
  styleUrl: './familia-cadastro.component.scss'
})
export class FamiliaCadastroComponent implements OnInit {
  form: FormGroup;
  activeTab = 'familia';
  familiaId: string | null = null;
  feedback: string | null = null;
  saving = false;
  membrosFiltrados: BeneficiarioApiPayload[] = [];

  tabs = [
    { id: 'familia', label: 'Dados da Família' },
    { id: 'endereco', label: 'Endereço' },
    { id: 'membros', label: 'Composição Familiar' },
    { id: 'renda', label: 'Renda & Condições' },
    { id: 'vulnerabilidades', label: 'Vulnerabilidades / Acompanhamento' }
  ];

  constructor(
    private readonly fb: FormBuilder,
    private readonly service: FamilyService,
    private readonly beneficiarioService: BeneficiarioApiService,
    private readonly route: ActivatedRoute,
    private readonly router: Router
  ) {
    this.form = this.fb.group({
      familia: this.fb.group({
        nome_familia: ['', Validators.required],
        id_referencia_familiar: [''],
        arranjo_familiar: ['']
      }),
      endereco: this.fb.group({
        cep: [''],
        logradouro: [''],
        numero: [''],
        complemento: [''],
        bairro: [''],
        ponto_referencia: [''],
        municipio: [''],
        uf: [''],
        zona: [''],
        situacao_imovel: [''],
        tipo_moradia: [''],
        agua_encanada: [false],
        esgoto_tipo: [''],
        coleta_lixo: [''],
        energia_eletrica: [false],
        internet: [false]
      }),
      membros: this.fb.array([]),
      renda: this.fb.group({
        renda_familiar_total: [{ value: '', disabled: true }],
        renda_per_capita: [{ value: '', disabled: true }],
        situacao_inseguranca_alimentar: [''],
        possui_dividas_relevantes: [false],
        descricao_dividas: [''],
        principais_fontes_renda: ['']
      }),
      vulnerabilidades: this.fb.group({
        vulnerabilidades_familia: [''],
        servicos_acompanhamento: [''],
        tecnico_responsavel: [''],
        periodicidade_atendimento: [''],
        proxima_visita_prevista: [''],
        observacoes: ['']
      })
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.familiaId = id;
        this.service.getById(id).subscribe(({ familia }) => {
          this.populateForm(familia);
        });
      }
    });
  }

  get membros(): FormArray {
    return this.form.get('membros') as FormArray;
  }

  addMembro(membro?: FamiliaMembroPayload) {
    const group = this.fb.group({
      id_familia_membro: [membro?.id_familia_membro],
      id_beneficiario: [membro?.id_beneficiario || '', Validators.required],
      parentesco: [membro?.parentesco || '', Validators.required],
      responsavel_familiar: [membro?.responsavel_familiar || false],
      contribui_renda: [membro?.contribui_renda || false],
      renda_individual: [membro?.renda_individual || ''],
      participa_servicos: [membro?.participa_servicos || false],
      observacoes: [membro?.observacoes || ''],
      beneficiario: [membro?.beneficiario]
    });
    this.membros.push(group);
  }

  removeMembro(index: number) {
    this.membros.removeAt(index);
  }

  changeTab(tab: string) {
    this.activeTab = tab;
  }

  populateForm(familia: FamiliaPayload) {
    this.form.patchValue({
      familia: {
        nome_familia: familia.nome_familia,
        id_referencia_familiar: familia.id_referencia_familiar,
        arranjo_familiar: familia.arranjo_familiar
      },
      endereco: {
        cep: familia.cep,
        logradouro: familia.logradouro,
        numero: familia.numero,
        complemento: familia.complemento,
        bairro: familia.bairro,
        ponto_referencia: familia.ponto_referencia,
        municipio: familia.municipio,
        uf: familia.uf,
        zona: familia.zona,
        situacao_imovel: familia.situacao_imovel,
        tipo_moradia: familia.tipo_moradia,
        agua_encanada: familia.agua_encanada,
        esgoto_tipo: familia.esgoto_tipo,
        coleta_lixo: familia.coleta_lixo,
        energia_eletrica: familia.energia_eletrica,
        internet: familia.internet
      },
      renda: {
        renda_familiar_total: familia.renda_familiar_total,
        renda_per_capita: familia.renda_per_capita,
        situacao_inseguranca_alimentar: familia.situacao_inseguranca_alimentar,
        possui_dividas_relevantes: familia.possui_dividas_relevantes,
        descricao_dividas: familia.descricao_dividas,
        principais_fontes_renda: familia.principais_fontes_renda
      },
      vulnerabilidades: {
        vulnerabilidades_familia: familia.vulnerabilidades_familia,
        servicos_acompanhamento: familia.servicos_acompanhamento,
        tecnico_responsavel: familia.tecnico_responsavel,
        periodicidade_atendimento: familia.periodicidade_atendimento,
        proxima_visita_prevista: familia.proxima_visita_prevista,
        observacoes: familia.observacoes
      }
    });

    this.membros.clear();
    (familia.membros || []).forEach((m) => this.addMembro(m));
  }

  buscarBeneficiarios(term: string) {
    this.beneficiarioService.list({ nome: term }).subscribe(({ beneficiarios }) => {
      this.membrosFiltrados = beneficiarios;
    });
  }

  submit() {
    if (this.form.invalid) {
      this.feedback = 'Preencha os campos obrigatórios';
      return;
    }
    this.saving = true;
    const payload = this.toPayload();
    const request = this.familiaId
      ? this.service.update(this.familiaId, payload)
      : this.service.create(payload);

    request.subscribe({
      next: () => {
        this.feedback = 'Família salva com sucesso';
        this.saving = false;
        this.router.navigate(['/cadastros/familias']);
      },
      error: (error) => {
        this.feedback = error?.error?.message || 'Erro ao salvar família';
        this.saving = false;
      }
    });
  }

  private toPayload(): FamiliaPayload {
    const value = this.form.value;
    return {
      ...(value.familia as any),
      ...(value.endereco as any),
      ...(value.renda as any),
      ...(value.vulnerabilidades as any),
      membros: (value.membros as any[]).map((m) => ({
        id_familia_membro: m.id_familia_membro,
        id_beneficiario: m.id_beneficiario,
        parentesco: m.parentesco,
        responsavel_familiar: m.responsavel_familiar,
        contribui_renda: m.contribui_renda,
        renda_individual: m.renda_individual,
        participa_servicos: m.participa_servicos,
        observacoes: m.observacoes
      }))
    } as FamiliaPayload;
  }
}
