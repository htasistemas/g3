import { MigrationInterface, QueryRunner, Table } from 'typeorm';

export class CreateBenefitsModule1730960000000 implements MigrationInterface {
  name = 'CreateBenefitsModule1730960000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.createTable(
      new Table({
        name: 'benefit_types',
        columns: [
          { name: 'id', type: 'uuid', isPrimary: true, isGenerated: true, generationStrategy: 'uuid' },
          { name: 'descricao', type: 'varchar', isNullable: false },
          { name: 'ativo', type: 'boolean', isNullable: false, default: true },
          { name: 'createdAt', type: 'timestamp', default: 'CURRENT_TIMESTAMP' },
          { name: 'updatedAt', type: 'timestamp', default: 'CURRENT_TIMESTAMP' }
        ]
      })
    );

    await queryRunner.createTable(
      new Table({
        name: 'benefit_grants',
        columns: [
          { name: 'id', type: 'uuid', isPrimary: true, isGenerated: true, generationStrategy: 'uuid' },
          { name: 'beneficiarioId', type: 'uuid', isNullable: false },
          { name: 'benefitTypeId', type: 'uuid', isNullable: false },
          { name: 'termoFomentoId', type: 'uuid', isNullable: true },
          { name: 'dataConcessao', type: 'date', isNullable: false },
          { name: 'quantidade', type: 'integer', isNullable: true },
          { name: 'valorEstimado', type: 'decimal', precision: 10, scale: 2, isNullable: true },
          { name: 'observacao', type: 'text', isNullable: true },
          { name: 'createdAt', type: 'timestamp', default: 'CURRENT_TIMESTAMP' },
          { name: 'updatedAt', type: 'timestamp', default: 'CURRENT_TIMESTAMP' }
        ],
        foreignKeys: [
          {
            columnNames: ['beneficiarioId'],
            referencedTableName: 'beneficiarios',
            referencedColumnNames: ['id'],
            onDelete: 'CASCADE'
          },
          {
            columnNames: ['benefitTypeId'],
            referencedTableName: 'benefit_types',
            referencedColumnNames: ['id'],
            onDelete: 'RESTRICT'
          },
          {
            columnNames: ['termoFomentoId'],
            referencedTableName: 'termos_fomento',
            referencedColumnNames: ['id'],
            onDelete: 'SET NULL'
          }
        ]
      })
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.dropTable('benefit_grants');
    await queryRunner.dropTable('benefit_types');
  }
}
