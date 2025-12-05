import {
  MigrationInterface,
  QueryRunner,
  Table,
  TableColumn,
  TableForeignKey,
  TableIndex
} from 'typeorm';

export class CreateSalas1730700000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    const uuidDefault = 'gen_random_uuid()';

    await queryRunner.createTable(
      new Table({
        name: 'salas',
        columns: [
          { name: 'id', type: 'uuid', isPrimary: true, default: uuidDefault },
          { name: 'nome', type: 'varchar', isUnique: true },
          { name: 'createdAt', type: 'timestamp', default: 'now()' },
          { name: 'updatedAt', type: 'timestamp', default: 'now()' }
        ]
      })
    );

    await queryRunner.addColumn(
      'cursos_atendimentos',
      new TableColumn({ name: 'salaId', type: 'uuid', isNullable: true })
    );

    await queryRunner.createForeignKey(
      'cursos_atendimentos',
      new TableForeignKey({
        columnNames: ['salaId'],
        referencedTableName: 'salas',
        referencedColumnNames: ['id'],
        onDelete: 'SET NULL'
      })
    );

    await queryRunner.createIndex(
      'cursos_atendimentos',
      new TableIndex({ name: 'IDX_cursos_atendimentos_sala', columnNames: ['salaId'] })
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.dropIndex('cursos_atendimentos', 'IDX_cursos_atendimentos_sala');

    const table = await queryRunner.getTable('cursos_atendimentos');
    const foreignKey = table?.foreignKeys.find((fk) => fk.columnNames.includes('salaId'));
    if (foreignKey) {
      await queryRunner.dropForeignKey('cursos_atendimentos', foreignKey);
    }

    await queryRunner.dropColumn('cursos_atendimentos', 'salaId');
    await queryRunner.dropTable('salas');
  }
}
