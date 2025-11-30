import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { BeneficiarioApiService, BeneficiarioApiPayload } from '../../services/beneficiario-api.service';

@Component({
  selector: 'app-beneficiario-cadastro',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './beneficiario-cadastro.component.html',
  styleUrl: './beneficiario-cadastro.component.scss'
})
export class BeneficiarioCadastroComponent implements OnInit {
  form: FormGroup;
  activeTab = 'dados';
  saving = false;
  feedback: string | null = null;
  beneficiarioId: string | null = null;

  tabs = [
    { id: 'dados', label: 'Dados Pessoais' },
    { id: 'endereco', label: 'Endereço' },
    { id: 'contato', label: 'Contato' },
    { id: 'documentos', label: 'Documentos' },
    { id: 'familiar', label: 'Situação Familiar e Social' },
    { id: 'escolaridade', label: 'Escolaridade & Trabalho' },
    { id: 'saude', label: 'Saúde' },
    { id: 'beneficios', label: 'Benefícios' },
    { id: 'observacoes', label: 'Observações & Anexos' }
  ];

  constructor(
    private readonly fb: FormBuilder,
    private readonly service: BeneficiarioApiService,
    private readonly router: Router,
    private readonly route: ActivatedRoute
  ) {
    this.form = this.fb.group({
      dadosPessoais: this.fb.group({
        nome_completo: ['', Validators.required],
        nome_social: [''],
        apelido: [''],
        data_nascimento: ['', Validators.required],
        sexo_biologico: [''],
        identidade_genero: [''],
        cor_raca: [''],
        estado_civil: [''],
        nacionalidade: [''],
        naturalidade_cidade: [''],
        naturalidade_uf: [''],
        nome_mae: ['', Validators.required],
        nome_pai: ['']
      }),
      endereco: this.fb.group({
        usa_endereco_familia: [true],
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
      contato: this.fb.group({
        telefone_principal: [''],
        telefone_principal_whatsapp: [false],
        telefone_secundario: [''],
        telefone_recado_nome: [''],
        telefone_recado_numero: [''],
        email: [''],
        permite_contato_tel: [true],
        permite_contato_whatsapp: [true],
        permite_contato_sms: [false],
        permite_contato_email: [false],
        horario_preferencial_contato: ['']
      }),
      documentos: this.fb.group({
        cpf: [''],
        rg_numero: [''],
        rg_orgao_emissor: [''],
        rg_uf: [''],
        rg_data_emissao: [''],
        nis: [''],
        certidao_tipo: [''],
        certidao_livro: [''],
        certidao_folha: [''],
        certidao_termo: [''],
        certidao_cartorio: [''],
        certidao_municipio: [''],
        certidao_uf: [''],
        titulo_eleitor: [''],
        cnh: [''],
        cartao_sus: ['']
      }),
      familiar: this.fb.group({
        mora_com_familia: [false],
        responsavel_legal: [false],
        vinculo_familiar: [''],
        situacao_vulnerabilidade: ['']
      }),
      escolaridade: this.fb.group({
        sabe_ler_escrever: [false],
        nivel_escolaridade: [''],
        estuda_atualmente: [false],
        ocupacao: [''],
        situacao_trabalho: [''],
        local_trabalho: [''],
        renda_mensal: [''],
        fonte_renda: ['']
      }),
      saude: this.fb.group({
        possui_deficiencia: [false],
        tipo_deficiencia: [''],
        cid_principal: [''],
        usa_medicacao_continua: [false],
        descricao_medicacao: [''],
        servico_saude_referencia: ['']
      }),
      beneficios: this.fb.group({
        recebe_beneficio: [false],
        beneficios_descricao: [''],
        valor_total_beneficios: ['']
      }),
      observacoes: this.fb.group({
        aceite_lgpd: [false],
        data_aceite_lgpd: [''],
        observacoes: ['']
      })
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.beneficiarioId = id;
        this.service.getById(id).subscribe(({ beneficiario }) => {
          this.form.patchValue(this.mapToForm(beneficiario));
        });
      }
    });
  }

  mapToForm(beneficiario: BeneficiarioApiPayload) {
    return {
      dadosPessoais: {
        nome_completo: beneficiario.nome_completo,
        nome_social: beneficiario.nome_social,
        apelido: beneficiario.apelido,
        data_nascimento: beneficiario.data_nascimento,
        sexo_biologico: beneficiario.sexo_biologico,
        identidade_genero: beneficiario.identidade_genero,
        cor_raca: beneficiario.cor_raca,
        estado_civil: beneficiario.estado_civil,
        nacionalidade: beneficiario.nacionalidade,
        naturalidade_cidade: beneficiario.naturalidade_cidade,
        naturalidade_uf: beneficiario.naturalidade_uf,
        nome_mae: beneficiario.nome_mae,
        nome_pai: beneficiario.nome_pai
      },
      endereco: {
        usa_endereco_familia: beneficiario.usa_endereco_familia,
        cep: beneficiario.cep,
        logradouro: beneficiario.logradouro,
        numero: beneficiario.numero,
        complemento: beneficiario.complemento,
        bairro: beneficiario.bairro,
        ponto_referencia: beneficiario.ponto_referencia,
        municipio: beneficiario.municipio,
        uf: beneficiario.uf,
        zona: beneficiario.zona,
        situacao_imovel: beneficiario.situacao_imovel,
        tipo_moradia: beneficiario.tipo_moradia,
        agua_encanada: beneficiario.agua_encanada,
        esgoto_tipo: beneficiario.esgoto_tipo,
        coleta_lixo: beneficiario.coleta_lixo,
        energia_eletrica: beneficiario.energia_eletrica,
        internet: beneficiario.internet
      },
      contato: {
        telefone_principal: beneficiario.telefone_principal,
        telefone_principal_whatsapp: beneficiario.telefone_principal_whatsapp,
        telefone_secundario: beneficiario.telefone_secundario,
        telefone_recado_nome: beneficiario.telefone_recado_nome,
        telefone_recado_numero: beneficiario.telefone_recado_numero,
        email: beneficiario.email,
        permite_contato_tel: beneficiario.permite_contato_tel,
        permite_contato_whatsapp: beneficiario.permite_contato_whatsapp,
        permite_contato_sms: beneficiario.permite_contato_sms,
        permite_contato_email: beneficiario.permite_contato_email,
        horario_preferencial_contato: beneficiario.horario_preferencial_contato
      },
      documentos: {
        cpf: beneficiario.cpf,
        rg_numero: beneficiario.rg_numero,
        rg_orgao_emissor: beneficiario.rg_orgao_emissor,
        rg_uf: beneficiario.rg_uf,
        rg_data_emissao: beneficiario.rg_data_emissao,
        nis: beneficiario.nis,
        certidao_tipo: beneficiario.certidao_tipo,
        certidao_livro: beneficiario.certidao_livro,
        certidao_folha: beneficiario.certidao_folha,
        certidao_termo: beneficiario.certidao_termo,
        certidao_cartorio: beneficiario.certidao_cartorio,
        certidao_municipio: beneficiario.certidao_municipio,
        certidao_uf: beneficiario.certidao_uf,
        titulo_eleitor: beneficiario.titulo_eleitor,
        cnh: beneficiario.cnh,
        cartao_sus: beneficiario.cartao_sus
      },
      familiar: {
        mora_com_familia: beneficiario.mora_com_familia,
        responsavel_legal: beneficiario.responsavel_legal,
        vinculo_familiar: beneficiario.vinculo_familiar,
        situacao_vulnerabilidade: beneficiario.situacao_vulnerabilidade
      },
      escolaridade: {
        sabe_ler_escrever: beneficiario.sabe_ler_escrever,
        nivel_escolaridade: beneficiario.nivel_escolaridade,
        estuda_atualmente: beneficiario.estuda_atualmente,
        ocupacao: beneficiario.ocupacao,
        situacao_trabalho: beneficiario.situacao_trabalho,
        local_trabalho: beneficiario.local_trabalho,
        renda_mensal: beneficiario.renda_mensal,
        fonte_renda: beneficiario.fonte_renda
      },
      saude: {
        possui_deficiencia: beneficiario.possui_deficiencia,
        tipo_deficiencia: beneficiario.tipo_deficiencia,
        cid_principal: beneficiario.cid_principal,
        usa_medicacao_continua: beneficiario.usa_medicacao_continua,
        descricao_medicacao: beneficiario.descricao_medicacao,
        servico_saude_referencia: beneficiario.servico_saude_referencia
      },
      beneficios: {
        recebe_beneficio: beneficiario.recebe_beneficio,
        beneficios_descricao: beneficiario.beneficios_descricao,
        valor_total_beneficios: beneficiario.valor_total_beneficios
      },
      observacoes: {
        aceite_lgpd: beneficiario.aceite_lgpd,
        data_aceite_lgpd: beneficiario.data_aceite_lgpd,
        observacoes: beneficiario.observacoes
      }
    };
  }

  changeTab(tab: string) {
    this.activeTab = tab;
  }

  submit() {
    if (this.form.invalid) {
      this.feedback = 'Preencha os campos obrigatórios.';
      return;
    }
    this.saving = true;
    const payload = this.toPayload();
    const request = this.beneficiarioId
      ? this.service.update(this.beneficiarioId, payload)
      : this.service.create(payload);

    request.subscribe({
      next: () => {
        this.feedback = 'Registro salvo com sucesso';
        this.saving = false;
        this.router.navigate(['/cadastros/beneficiarios']);
      },
      error: (error) => {
        this.feedback = error?.error?.message || 'Erro ao salvar beneficiário';
        this.saving = false;
      }
    });
  }

  private toPayload(): BeneficiarioApiPayload {
    const value = this.form.value;
    return {
      ...(value.dadosPessoais as any),
      ...(value.endereco as any),
      ...(value.contato as any),
      ...(value.documentos as any),
      ...(value.familiar as any),
      ...(value.escolaridade as any),
      ...(value.saude as any),
      ...(value.beneficios as any),
      ...(value.observacoes as any)
    };
  }
}
