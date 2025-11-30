import { MigrationInterface, QueryRunner, Table, TableForeignKey } from 'typeorm';

export class CreateFamiliesAndMembers1729750000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.createTable(
      new Table({
        name: 'familias',
        columns: [
          { name: 'id', type: 'int', isPrimary: true, isGenerated: true, generationStrategy: 'increment' },
          { name: 'responsavel_familiar_id', type: 'int', isNullable: true },
          { name: 'logradouro', type: 'varchar' },
          { name: 'numero', type: 'varchar' },
          { name: 'bairro', type: 'varchar' },
          { name: 'cidade', type: 'varchar' },
          { name: 'uf', type: 'varchar', length: '2' },
          { name: 'cep', type: 'varchar' },
          { name: 'complemento', type: 'varchar', isNullable: true },
          { name: 'criado_em', type: 'timestamp', default: 'CURRENT_TIMESTAMP' }
        ]
      })
    );

    await queryRunner.createTable(
      new Table({
        name: 'familia_membros',
        columns: [
          { name: 'id', type: 'int', isPrimary: true, isGenerated: true, generationStrategy: 'increment' },
          { name: 'familia_id', type: 'int' },
          { name: 'beneficiario_id', type: 'int' },
          { name: 'parentesco', type: 'varchar' },
          { name: 'eh_responsavel_familiar', type: 'boolean', default: false },
          { name: 'data_entrada_familia', type: 'date', isNullable: true },
          { name: 'observacoes', type: 'text', isNullable: true }
        ]
      })
    );

    await queryRunner.createForeignKey(
      'familias',
      new TableForeignKey({
        columnNames: ['responsavel_familiar_id'],
        referencedTableName: 'beneficiarios',
        referencedColumnNames: ['id'],
        onDelete: 'SET NULL'
      })
    );

    await queryRunner.createForeignKey(
      'familia_membros',
      new TableForeignKey({
        columnNames: ['familia_id'],
        referencedTableName: 'familias',
        referencedColumnNames: ['id'],
        onDelete: 'CASCADE'
      })
    );

    await queryRunner.createForeignKey(
      'familia_membros',
      new TableForeignKey({
        columnNames: ['beneficiario_id'],
        referencedTableName: 'beneficiarios',
        referencedColumnNames: ['id'],
        onDelete: 'CASCADE'
      })
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.dropTable('familia_membros');
    await queryRunner.dropTable('familias');
  }
}
