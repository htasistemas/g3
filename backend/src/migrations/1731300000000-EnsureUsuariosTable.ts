import { MigrationInterface, QueryRunner, Table } from 'typeorm';

export class EnsureUsuariosTable1731300000000 implements MigrationInterface {
  name = 'EnsureUsuariosTable1731300000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    const tableExists = await queryRunner.hasTable('usuarios');
    if (tableExists) return;

    await queryRunner.createTable(
      new Table({
        name: 'usuarios',
        columns: [
          {
            name: 'id',
            type: 'integer',
            isPrimary: true,
            isGenerated: true,
            generationStrategy: 'increment'
          },
          {
            name: 'nome_usuario',
            type: 'varchar',
            length: '150',
            isNullable: false,
            isUnique: true
          },
          {
            name: 'hash_senha',
            type: 'text',
            isNullable: false
          },
          {
            name: 'criado_em',
            type: 'timestamp without time zone',
            default: 'now()'
          },
          {
            name: 'atualizado_em',
            type: 'timestamp without time zone',
            default: 'now()'
          }
        ]
      })
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const tableExists = await queryRunner.hasTable('usuarios');
    if (!tableExists) return;

    await queryRunner.dropTable('usuarios');
  }
}
