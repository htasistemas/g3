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
    const tableExists = await queryRunner.hasTable('salas');
    if (tableExists) {
      // Tabela já existe, não tentar criar novamente para evitar erro SQLITE_ERROR
      return;
    }

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

    const hasCursoTable = await queryRunner.hasTable('cursos_atendimentos');

    if (!hasCursoTable) {
      await queryRunner.createTable(
        new Table({
          name: 'cursos_atendimentos',
          columns: [
            { name: 'id', type: 'uuid', isPrimary: true, default: uuidDefault },
            { name: 'tipo', type: 'varchar' },
            { name: 'nome', type: 'varchar' },
            { name: 'descricao', type: 'text' },
            { name: 'imagem', type: 'text', isNullable: true },
            { name: 'vagasTotais', type: 'integer' },
            { name: 'vagasDisponiveis', type: 'integer' },
            { name: 'cargaHoraria', type: 'integer', isNullable: true },
            { name: 'horarioInicial', type: 'varchar' },
            { name: 'duracaoHoras', type: 'integer' },
            { name: 'diasSemana', type: 'text', isNullable: true },
            { name: 'profissional', type: 'varchar' },
            { name: 'salaId', type: 'uuid', isNullable: true },
            { name: 'enrollments', type: 'jsonb', isNullable: true },
            { name: 'waitlist', type: 'jsonb', isNullable: true },
            { name: 'certificate_template', type: 'text', isNullable: true },
            { name: 'createdAt', type: 'timestamp', default: 'now()' },
            { name: 'updatedAt', type: 'timestamp', default: 'now()' }
          ]
        })
      );
    }

    const cursoTable = await queryRunner.getTable('cursos_atendimentos');
    const hasSalaColumn = cursoTable?.findColumnByName('salaId');

    if (!hasSalaColumn) {
      await queryRunner.addColumn(
        'cursos_atendimentos',
        new TableColumn({ name: 'salaId', type: 'uuid', isNullable: true })
      );
    }

    const hasForeignKey = cursoTable?.foreignKeys.some((fk) => fk.columnNames.includes('salaId'));
    if (!hasForeignKey) {
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

    const hasIndex = cursoTable?.indices.some((index) => index.columnNames.includes('salaId'));
    if (!hasIndex) {
      await queryRunner.createIndex(
        'cursos_atendimentos',
        new TableIndex({ name: 'IDX_cursos_atendimentos_sala', columnNames: ['salaId'] })
      );
    }
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const tableExists = await queryRunner.hasTable('salas');
    if (!tableExists) {
      // Se a tabela não existir, não tenta dropar
      return;
    }

    await queryRunner.dropTable('salas');
  }
}
