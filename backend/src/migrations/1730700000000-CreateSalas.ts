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

    const salasExists = await queryRunner.hasTable('salas');
    if (!salasExists) {
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
    }

    const hasCursosAtendimentos = await queryRunner.hasTable('cursos_atendimentos');
    if (!hasCursosAtendimentos) return;

    const hasSalaColumn = await queryRunner.hasColumn('cursos_atendimentos', 'salaId');
    if (!hasSalaColumn) {
      await queryRunner.addColumn(
        'cursos_atendimentos',
        new TableColumn({ name: 'salaId', type: 'uuid', isNullable: true })
      );
    }

    const table = await queryRunner.getTable('cursos_atendimentos');
    const salaForeignKey = table?.foreignKeys.find((fk) => fk.columnNames.includes('salaId'));
    if (!salaForeignKey) {
      await queryRunner.createForeignKey(
        'cursos_atendimentos',
        new TableForeignKey({
          columnNames: ['salaId'],
          referencedTableName: 'salas',
          referencedColumnNames: ['id'],
          onDelete: 'SET NULL'
        })
      );
    }

    const hasIndex = await queryRunner.hasIndex('cursos_atendimentos', 'IDX_cursos_atendimentos_sala');
    if (!hasIndex) {
      await queryRunner.createIndex(
        'cursos_atendimentos',
        new TableIndex({ name: 'IDX_cursos_atendimentos_sala', columnNames: ['salaId'] })
      );
    }
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const hasCursosAtendimentos = await queryRunner.hasTable('cursos_atendimentos');
    if (hasCursosAtendimentos) {
      const hasIndex = await queryRunner.hasIndex('cursos_atendimentos', 'IDX_cursos_atendimentos_sala');
      if (hasIndex) {
        await queryRunner.dropIndex('cursos_atendimentos', 'IDX_cursos_atendimentos_sala');
      }

      const table = await queryRunner.getTable('cursos_atendimentos');
      const foreignKey = table?.foreignKeys.find((fk) => fk.columnNames.includes('salaId'));
      if (foreignKey) {
        await queryRunner.dropForeignKey('cursos_atendimentos', foreignKey);
      }

      const hasSalaColumn = await queryRunner.hasColumn('cursos_atendimentos', 'salaId');
      if (hasSalaColumn) {
        await queryRunner.dropColumn('cursos_atendimentos', 'salaId');
      }
    }

    const salasExists = await queryRunner.hasTable('salas');
    if (salasExists) {
      await queryRunner.dropTable('salas');
    }
  }
}
