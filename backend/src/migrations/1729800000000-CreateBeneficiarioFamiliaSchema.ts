import { MigrationInterface, QueryRunner, Table, TableForeignKey, TableIndex } from 'typeorm';

export class CreateBeneficiarioFamiliaSchema1729800000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    const uuidDefault = 'gen_random_uuid()';

    const hasBeneficiarioTable = await queryRunner.hasTable('beneficiario');

    if (!hasBeneficiarioTable) {
      await queryRunner.query(`
        CREATE TABLE "beneficiario" (
          "id_beneficiario" uuid PRIMARY KEY NOT NULL DEFAULT (gen_random_uuid()),
          "nome_completo" varchar NOT NULL,
          "nome_social" varchar,
          "apelido" varchar,
          "data_nascimento" date NOT NULL,
          "sexo_biologico" varchar,
          "identidade_genero" varchar,
          "cor_raca" varchar,
          "estado_civil" varchar,
          "nacionalidade" varchar,
          "naturalidade_cidade" varchar,
          "naturalidade_uf" varchar(2),
          "nome_mae" varchar NOT NULL,
          "nome_pai" varchar,
          "cpf" varchar(11),
          "rg_numero" varchar,
          "rg_orgao_emissor" varchar,
          "rg_uf" varchar(2),
          "rg_data_emissao" date,
          "nis" varchar,
          "certidao_tipo" varchar,
          "certidao_livro" varchar,
          "certidao_folha" varchar,
          "certidao_termo" varchar,
          "certidao_cartorio" varchar,
          "certidao_municipio" varchar,
          "certidao_uf" varchar(2),
          "titulo_eleitor" varchar,
          "cnh" varchar,
          "cartao_sus" varchar,
          "telefone_principal" varchar,
          "telefone_principal_whatsapp" boolean NOT NULL DEFAULT (false),
          "telefone_secundario" varchar,
          "telefone_recado_nome" varchar,
          "telefone_recado_numero" varchar,
          "email" varchar,
          "permite_contato_tel" boolean NOT NULL DEFAULT (true),
          "permite_contato_whatsapp" boolean NOT NULL DEFAULT (true),
          "permite_contato_sms" boolean NOT NULL DEFAULT (false),
          "permite_contato_email" boolean NOT NULL DEFAULT (false),
          "horario_preferencial_contato" varchar,
          "usa_endereco_familia" boolean NOT NULL DEFAULT (true),
          "cep" varchar,
          "logradouro" varchar,
          "numero" varchar,
          "complemento" varchar,
          "bairro" varchar,
          "ponto_referencia" varchar,
          "municipio" varchar,
          "uf" varchar(2),
          "zona" varchar,
          "situacao_imovel" varchar,
          "tipo_moradia" varchar,
          "agua_encanada" boolean NOT NULL DEFAULT (false),
          "esgoto_tipo" varchar,
          "coleta_lixo" varchar,
          "energia_eletrica" boolean NOT NULL DEFAULT (false),
          "internet" boolean NOT NULL DEFAULT (false),
          "mora_com_familia" boolean NOT NULL DEFAULT (false),
          "responsavel_legal" boolean NOT NULL DEFAULT (false),
          "vinculo_familiar" varchar,
          "situacao_vulnerabilidade" text,
          "sabe_ler_escrever" boolean NOT NULL DEFAULT (false),
          "nivel_escolaridade" varchar,
          "estuda_atualmente" boolean NOT NULL DEFAULT (false),
          "ocupacao" varchar,
          "situacao_trabalho" varchar,
          "local_trabalho" varchar,
          "renda_mensal" numeric(12,2),
          "fonte_renda" varchar,
          "possui_deficiencia" boolean NOT NULL DEFAULT (false),
          "tipo_deficiencia" varchar,
          "cid_principal" varchar,
          "usa_medicacao_continua" boolean NOT NULL DEFAULT (false),
          "descricao_medicacao" text,
          "servico_saude_referencia" varchar,
          "recebe_beneficio" boolean NOT NULL DEFAULT (false),
          "beneficios_descricao" text,
          "valor_total_beneficios" numeric(12,2),
          "aceite_lgpd" boolean NOT NULL DEFAULT (false),
          "data_aceite_lgpd" timestamp,
          "observacoes" text,
          "data_cadastro" timestamp NOT NULL DEFAULT (now()),
          "data_atualizacao" timestamp,
          CONSTRAINT "UQ_a608d9b31059b0e934e9b9ddd6d" UNIQUE ("cpf")
        )
      `);
    }

    const beneficiarioTable = await queryRunner.getTable('beneficiario');

    if (beneficiarioTable) {
      const indicesToEnsure = [
        { name: 'idx_beneficiario_nome', columns: ['nome_completo'] },
        { name: 'idx_beneficiario_cpf', columns: ['cpf'] },
        { name: 'idx_beneficiario_nis', columns: ['nis'] }
      ];

      const missingIndices = indicesToEnsure.filter(
        (idx) => !beneficiarioTable.indices.find((tableIdx) => tableIdx.name === idx.name)
      );

      if (missingIndices.length > 0) {
        await queryRunner.createIndices(
          'beneficiario',
          missingIndices.map((idx) => new TableIndex({ name: idx.name, columnNames: idx.columns }))
        );
      }
    }

    const hasFamiliaTable = await queryRunner.hasTable('familia');

    if (!hasFamiliaTable) {
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
        }),
        true
      );
    }

    const familiaTable = await queryRunner.getTable('familia');
    const shouldAddFamiliaFk =
      familiaTable && !familiaTable.foreignKeys.find((fk) => fk.columnNames.includes('id_referencia_familiar'));

    if (shouldAddFamiliaFk) {
      await queryRunner.createForeignKeys('familia', [
        new TableForeignKey({
          columnNames: ['id_referencia_familiar'],
          referencedTableName: 'beneficiario',
          referencedColumnNames: ['id_beneficiario'],
          onDelete: 'SET NULL'
        })
      ]);
    }

    const hasFamiliaMembroTable = await queryRunner.hasTable('familia_membro');

    if (!hasFamiliaMembroTable) {
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
        }),
        true
      );
    }

    const familiaMembroTable = await queryRunner.getTable('familia_membro');
    const hasFamiliaFk = familiaMembroTable?.foreignKeys.some(
      (fk) => fk.columnNames.length === 1 && fk.columnNames[0] === 'id_familia'
    );
    const hasBeneficiarioFk = familiaMembroTable?.foreignKeys.some(
      (fk) => fk.columnNames.length === 1 && fk.columnNames[0] === 'id_beneficiario'
    );

    const foreignKeysToCreate: TableForeignKey[] = [];

    if (!hasFamiliaFk) {
      foreignKeysToCreate.push(
        new TableForeignKey({
          columnNames: ['id_familia'],
          referencedTableName: 'familia',
          referencedColumnNames: ['id_familia'],
          onDelete: 'CASCADE'
        })
      );
    }

    if (!hasBeneficiarioFk) {
      foreignKeysToCreate.push(
        new TableForeignKey({
          columnNames: ['id_beneficiario'],
          referencedTableName: 'beneficiario',
          referencedColumnNames: ['id_beneficiario'],
          onDelete: 'CASCADE'
        })
      );
    }

    if (foreignKeysToCreate.length > 0) {
      await queryRunner.createForeignKeys('familia_membro', foreignKeysToCreate);
    }
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.query('DROP TABLE IF EXISTS "familia_membro"');
    await queryRunner.query('DROP TABLE IF EXISTS "familia"');
    await queryRunner.query('DROP TABLE IF EXISTS "beneficiario"');
  }
}
