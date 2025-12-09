import { MigrationInterface, QueryRunner, Table, TableForeignKey } from 'typeorm';

export class CreateProntuarioModule1731200000000 implements MigrationInterface {
  name = 'CreateProntuarioModule1731200000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.createTable(
      new Table({
        name: 'prontuarios',
        columns: [
          {
            name: 'id_prontuario',
            type: 'uuid',
            isPrimary: true,
            isGenerated: true,
            generationStrategy: 'uuid'
          },
          { name: 'id_beneficiario', type: 'uuid', isNullable: false },
          { name: 'identificacao', type: 'jsonb', isNullable: true },
          { name: 'composicao_familiar', type: 'jsonb', isNullable: true },
          { name: 'situacoes_vulnerabilidade', type: 'jsonb', isNullable: true },
          { name: 'participacoes_servicos', type: 'jsonb', isNullable: true },
          { name: 'parecer_tecnico', type: 'text', isNullable: true },
          { name: 'created_at', type: 'timestamp', default: 'CURRENT_TIMESTAMP' },
          { name: 'updated_at', type: 'timestamp', default: 'CURRENT_TIMESTAMP' }
        ]
      })
    );

    await queryRunner.createForeignKey(
      'prontuarios',
      new TableForeignKey({
        columnNames: ['id_beneficiario'],
        referencedTableName: 'beneficiarios',
        referencedColumnNames: ['id'],
        onDelete: 'CASCADE'
      })
    );

    await queryRunner.createTable(
      new Table({
        name: 'prontuario_atendimentos',
        columns: [
          {
            name: 'id_prontuario_atendimento',
            type: 'uuid',
            isPrimary: true,
            isGenerated: true,
            generationStrategy: 'uuid'
          },
          { name: 'id_prontuario', type: 'uuid', isNullable: false },
          { name: 'data_atendimento', type: 'date', isNullable: true },
          { name: 'tipo_atendimento', type: 'varchar', isNullable: true },
          { name: 'descricao', type: 'text', isNullable: true },
          { name: 'responsavel', type: 'varchar', isNullable: true },
          { name: 'resultado', type: 'text', isNullable: true },
          { name: 'created_at', type: 'timestamp', default: 'CURRENT_TIMESTAMP' },
          { name: 'updated_at', type: 'timestamp', default: 'CURRENT_TIMESTAMP' }
        ]
      })
    );

    await queryRunner.createForeignKey(
      'prontuario_atendimentos',
      new TableForeignKey({
        columnNames: ['id_prontuario'],
        referencedTableName: 'prontuarios',
        referencedColumnNames: ['id_prontuario'],
        onDelete: 'CASCADE'
      })
    );

    await queryRunner.createTable(
      new Table({
        name: 'prontuario_encaminhamentos',
        columns: [
          {
            name: 'id_prontuario_encaminhamento',
            type: 'uuid',
            isPrimary: true,
            isGenerated: true,
            generationStrategy: 'uuid'
          },
          { name: 'id_prontuario', type: 'uuid', isNullable: false },
          { name: 'data_encaminhamento', type: 'date', isNullable: true },
          { name: 'destino', type: 'varchar', isNullable: true },
          { name: 'motivo', type: 'text', isNullable: true },
          { name: 'responsavel', type: 'varchar', isNullable: true },
          { name: 'status', type: 'varchar', isNullable: true },
          { name: 'retorno_previsto', type: 'date', isNullable: true },
          { name: 'observacoes', type: 'text', isNullable: true },
          { name: 'created_at', type: 'timestamp', default: 'CURRENT_TIMESTAMP' },
          { name: 'updated_at', type: 'timestamp', default: 'CURRENT_TIMESTAMP' }
        ]
      })
    );

    await queryRunner.createForeignKey(
      'prontuario_encaminhamentos',
      new TableForeignKey({
        columnNames: ['id_prontuario'],
        referencedTableName: 'prontuarios',
        referencedColumnNames: ['id_prontuario'],
        onDelete: 'CASCADE'
      })
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.dropTable('prontuario_encaminhamentos');
    await queryRunner.dropTable('prontuario_atendimentos');
    await queryRunner.dropTable('prontuarios');
  }
}
