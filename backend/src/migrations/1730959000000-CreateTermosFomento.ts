import { MigrationInterface, QueryRunner, Table } from 'typeorm';

export class CreateTermosFomento1730959000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    const hasTable = await queryRunner.hasTable('termos_fomento');
    if (hasTable) return;

    await queryRunner.createTable(
      new Table({
        name: 'termos_fomento',
        columns: [
          {
            name: 'id',
            type: 'uuid',
            isPrimary: true,
            generationStrategy: 'uuid',
            default: 'gen_random_uuid()'
          },
          {
            name: 'numero',
            type: 'varchar',
            isNullable: false,
            isUnique: true
          },
          {
            name: 'objeto',
            type: 'text',
            isNullable: true
          },
          {
            name: 'orgaoConcedente',
            type: 'varchar',
            isNullable: true
          },
          {
            name: 'modalidade',
            type: 'varchar',
            isNullable: true
          },
          {
            name: 'vigenciaInicio',
            type: 'date',
            isNullable: true
          },
          {
            name: 'vigenciaFim',
            type: 'date',
            isNullable: true
          },
          {
            name: 'valorGlobal',
            type: 'numeric',
            precision: 14,
            scale: 2,
            isNullable: true
          },
          {
            name: 'status',
            type: 'varchar',
            isNullable: true
          },
          {
            name: 'createdAt',
            type: 'timestamp',
            default: 'CURRENT_TIMESTAMP'
          },
          {
            name: 'updatedAt',
            type: 'timestamp',
            default: 'CURRENT_TIMESTAMP'
          }
        ]
      })
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.dropTable('termos_fomento');
  }
}
