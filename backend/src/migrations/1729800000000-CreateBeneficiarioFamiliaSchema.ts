import { MigrationInterface, QueryRunner, Table, TableForeignKey, TableIndex } from 'typeorm';

export class CreateBeneficiarioFamiliaSchema1729800000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    const uuidDefault = 'gen_random_uuid()';

    await queryRunner.createTable(
      new Table({
        name: 'beneficiario',
        columns: [
          { name: 'id_beneficiario', type: 'uuid', isPrimary: true, default: uuidDefault },
          { name: 'nome_completo', type: 'varchar' },
          { name: 'nome_social', type: 'varchar', isNullable: true },
          { name: 'apelido', type: 'varchar', isNullable: true },
          { name: 'data_nascimento', type: 'date' },
          { name: 'sexo_biologico', type: 'varchar', isNullable: true },
          { name: 'identidade_genero', type: 'varchar', isNullable: true },
          { name: 'cor_raca', type: 'varchar', isNullable: true },
          { name: 'estado_civil', type: 'varchar', isNullable: true },
          { name: 'nacionalidade', type: 'varchar', isNullable: true },
          { name: 'naturalidade_cidade', type: 'varchar', isNullable: true },
          { name: 'naturalidade_uf', type: 'varchar', length: '2', isNullable: true },
          { name: 'nome_mae', type: 'varchar' },
          { name: 'nome_pai', type: 'varchar', isNullable: true },
          { name: 'cpf', type: 'varchar', length: '11', isNullable: true, isUnique: true },
          { name: 'rg_numero', type: 'varchar', isNullable: true },
          { name: 'rg_orgao_emissor', type: 'varchar', isNullable: true },
          { name: 'rg_uf', type: 'varchar', length: '2', isNullable: true },
          { name: 'rg_data_emissao', type: 'date', isNullable: true },
          { name: 'nis', type: 'varchar', isNullable: true },
          { name: 'certidao_tipo', type: 'varchar', isNullable: true },
          { name: 'certidao_livro', type: 'varchar', isNullable: true },
          { name: 'certidao_folha', type: 'varchar', isNullable: true },
          { name: 'certidao_termo', type: 'varchar', isNullable: true },
          { name: 'certidao_cartorio', type: 'varchar', isNullable: true },
          { name: 'certidao_municipio', type: 'varchar', isNullable: true },
          { name: 'certidao_uf', type: 'varchar', length: '2', isNullable: true },
          { name: 'titulo_eleitor', type: 'varchar', isNullable: true },
          { name: 'cnh', type: 'varchar', isNullable: true },
          { name: 'cartao_sus', type: 'varchar', isNullable: true },
          { name: 'telefone_principal', type: 'varchar', isNullable: true },
          { name: 'telefone_principal_whatsapp', type: 'boolean', default: false },
          { name: 'telefone_secundario', type: 'varchar', isNullable: true },
          { name: 'telefone_recado_nome', type: 'varchar', isNullable: true },
          { name: 'telefone_recado_numero', type: 'varchar', isNullable: true },
          { name: 'email', type: 'varchar', isNullable: true },
          { name: 'permite_contato_tel', type: 'boolean', default: true },
          { name: 'permite_contato_whatsapp', type: 'boolean', default: true },
          { name: 'permite_contato_sms', type: 'boolean', default: false },
          { name: 'permite_contato_email', type: 'boolean', default: false },
          { name: 'horario_preferencial_contato', type: 'varchar', isNullable: true },
          { name: 'usa_endereco_familia', type: 'boolean', default: true },
          { name: 'cep', type: 'varchar', isNullable: true },
          { name: 'logradouro', type: 'varchar', isNullable: true },
          { name: 'numero', type: 'varchar', isNullable: true },
          { name: 'complemento', type: 'varchar', isNullable: true },
          { name: 'bairro', type: 'varchar', isNullable: true },
          { name: 'ponto_referencia', type: 'varchar', isNullable: true },
          { name: 'municipio', type: 'varchar', isNullable: true },
          { name: 'uf', type: 'varchar', length: '2', isNullable: true },
          { name: 'zona', type: 'varchar', isNullable: true },
          { name: 'situacao_imovel', type: 'varchar', isNullable: true },
          { name: 'tipo_moradia', type: 'varchar', isNullable: true },
          { name: 'agua_encanada', type: 'boolean', default: false },
          { name: 'esgoto_tipo', type: 'varchar', isNullable: true },
          { name: 'coleta_lixo', type: 'varchar', isNullable: true },
          { name: 'energia_eletrica', type: 'boolean', default: false },
          { name: 'internet', type: 'boolean', default: false },
          { name: 'mora_com_familia', type: 'boolean', default: false },
          { name: 'responsavel_legal', type: 'boolean', default: false },
          { name: 'vinculo_familiar', type: 'varchar', isNullable: true },
          { name: 'situacao_vulnerabilidade', type: 'text', isNullable: true },
          { name: 'sabe_ler_escrever', type: 'boolean', default: false },
          { name: 'nivel_escolaridade', type: 'varchar', isNullable: true },
          { name: 'estuda_atualmente', type: 'boolean', default: false },
          { name: 'ocupacao', type: 'varchar', isNullable: true },
          { name: 'situacao_trabalho', type: 'varchar', isNullable: true },
          { name: 'local_trabalho', type: 'varchar', isNullable: true },
          { name: 'renda_mensal', type: 'numeric', precision: 12, scale: 2, isNullable: true },
          { name: 'fonte_renda', type: 'varchar', isNullable: true },
          { name: 'possui_deficiencia', type: 'boolean', default: false },
          { name: 'tipo_deficiencia', type: 'varchar', isNullable: true },
          { name: 'cid_principal', type: 'varchar', isNullable: true },
          { name: 'usa_medicacao_continua', type: 'boolean', default: false },
          { name: 'descricao_medicacao', type: 'text', isNullable: true },
          { name: 'servico_saude_referencia', type: 'varchar', isNullable: true },
          { name: 'recebe_beneficio', type: 'boolean', default: false },
          { name: 'beneficios_descricao', type: 'text', isNullable: true },
          { name: 'valor_total_beneficios', type: 'numeric', precision: 12, scale: 2, isNullable: true },
          { name: 'aceite_lgpd', type: 'boolean', default: false },
          { name: 'data_aceite_lgpd', type: 'timestamp', isNullable: true },
          { name: 'observacoes', type: 'text', isNullable: true },
          { name: 'data_cadastro', type: 'timestamp', default: 'now()' },
          { name: 'data_atualizacao', type: 'timestamp', isNullable: true }
        ]
      })
    );

    await queryRunner.createIndices('beneficiario', [
      new TableIndex({ name: 'idx_beneficiario_nome', columnNames: ['nome_completo'] }),
      new TableIndex({ name: 'idx_beneficiario_cpf', columnNames: ['cpf'] }),
      new TableIndex({ name: 'idx_beneficiario_nis', columnNames: ['nis'] })
    ]);

    await queryRunner.createTable(
      new Table({
        name: 'familia',
        columns: [
          { name: 'id_familia', type: 'uuid', isPrimary: true, default: uuidDefault },
          { name: 'nome_familia', type: 'varchar' },
          { name: 'id_referencia_familiar', type: 'uuid', isNullable: true },
          { name: 'cep', type: 'varchar', isNullable: true },
          { name: 'logradouro', type: 'varchar', isNullable: true },
          { name: 'numero', type: 'varchar', isNullable: true },
          { name: 'complemento', type: 'varchar', isNullable: true },
          { name: 'bairro', type: 'varchar', isNullable: true },
          { name: 'ponto_referencia', type: 'varchar', isNullable: true },
          { name: 'municipio', type: 'varchar', isNullable: true },
          { name: 'uf', type: 'varchar', length: '2', isNullable: true },
          { name: 'zona', type: 'varchar', isNullable: true },
          { name: 'situacao_imovel', type: 'varchar', isNullable: true },
          { name: 'tipo_moradia', type: 'varchar', isNullable: true },
          { name: 'agua_encanada', type: 'boolean', default: false },
          { name: 'esgoto_tipo', type: 'varchar', isNullable: true },
          { name: 'coleta_lixo', type: 'varchar', isNullable: true },
          { name: 'energia_eletrica', type: 'boolean', default: false },
          { name: 'internet', type: 'boolean', default: false },
          { name: 'arranjo_familiar', type: 'varchar', isNullable: true },
          { name: 'qtd_membros', type: 'int', default: 0 },
          { name: 'qtd_criancas', type: 'int', default: 0 },
          { name: 'qtd_adolescentes', type: 'int', default: 0 },
          { name: 'qtd_idosos', type: 'int', default: 0 },
          { name: 'qtd_pessoas_deficiencia', type: 'int', default: 0 },
          { name: 'renda_familiar_total', type: 'numeric', precision: 12, scale: 2, default: 0 },
          { name: 'renda_per_capita', type: 'numeric', precision: 12, scale: 2, default: 0 },
          { name: 'faixa_renda_per_capita', type: 'varchar', isNullable: true },
          { name: 'principais_fontes_renda', type: 'text', isNullable: true },
          { name: 'situacao_inseguranca_alimentar', type: 'varchar', isNullable: true },
          { name: 'possui_dividas_relevantes', type: 'boolean', default: false },
          { name: 'descricao_dividas', type: 'text', isNullable: true },
          { name: 'vulnerabilidades_familia', type: 'text', isNullable: true },
          { name: 'servicos_acompanhamento', type: 'text', isNullable: true },
          { name: 'tecnico_responsavel', type: 'varchar', isNullable: true },
          { name: 'periodicidade_atendimento', type: 'varchar', isNullable: true },
          { name: 'proxima_visita_prevista', type: 'date', isNullable: true },
          { name: 'observacoes', type: 'text', isNullable: true },
          { name: 'data_cadastro', type: 'timestamp', default: 'now()' },
          { name: 'data_atualizacao', type: 'timestamp', isNullable: true }
        ]
      })
    );

    await queryRunner.createTable(
      new Table({
        name: 'familia_membro',
        columns: [
          { name: 'id_familia_membro', type: 'uuid', isPrimary: true, default: uuidDefault },
          { name: 'id_familia', type: 'uuid' },
          { name: 'id_beneficiario', type: 'uuid' },
          { name: 'parentesco', type: 'varchar' },
          { name: 'responsavel_familiar', type: 'boolean', default: false },
          { name: 'contribui_renda', type: 'boolean', default: false },
          { name: 'renda_individual', type: 'numeric', precision: 12, scale: 2, isNullable: true },
          { name: 'participa_servicos', type: 'boolean', default: false },
          { name: 'observacoes', type: 'text', isNullable: true },
          { name: 'data_cadastro', type: 'timestamp', default: 'now()' },
          { name: 'data_atualizacao', type: 'timestamp', isNullable: true }
        ],
        uniques: [
          { name: 'uk_familia_membro_beneficiario', columnNames: ['id_familia', 'id_beneficiario'] }
        ]
      })
    );

    await queryRunner.createForeignKeys('familia', [
      new TableForeignKey({
        columnNames: ['id_referencia_familiar'],
        referencedTableName: 'beneficiario',
        referencedColumnNames: ['id_beneficiario'],
        onDelete: 'SET NULL'
      })
    ]);

    await queryRunner.createForeignKeys('familia_membro', [
      new TableForeignKey({
        columnNames: ['id_familia'],
        referencedTableName: 'familia',
        referencedColumnNames: ['id_familia'],
        onDelete: 'CASCADE'
      }),
      new TableForeignKey({
        columnNames: ['id_beneficiario'],
        referencedTableName: 'beneficiario',
        referencedColumnNames: ['id_beneficiario'],
        onDelete: 'CASCADE'
      })
    ]);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.dropTable('familia_membro');
    await queryRunner.dropTable('familia');
    await queryRunner.dropTable('beneficiario');
  }
}
