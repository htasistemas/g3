import { MigrationInterface, QueryRunner, TableColumn } from 'typeorm';

export class UpdateAssistanceUnitSchema1730100000000 implements MigrationInterface {
  name = 'UpdateAssistanceUnitSchema1730100000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'unidades_assistenciais';
    const table = await queryRunner.getTable(tableName);

    if (!table) return;

    const nomeFantasiaExists = table.findColumnByName('nome_fantasia');
    const nomeExists = table.findColumnByName('nome');

    if (!nomeFantasiaExists && nomeExists) {
      await queryRunner.renameColumn(tableName, 'nome', 'nome_fantasia');
    }

    const newColumns: TableColumn[] = [
      new TableColumn({ name: 'razao_social', type: 'varchar', isNullable: true }),
      new TableColumn({ name: 'cnpj', type: 'varchar', isNullable: true }),
      new TableColumn({ name: 'responsavel_nome', type: 'varchar', isNullable: true }),
      new TableColumn({ name: 'responsavel_cpf', type: 'varchar', isNullable: true }),
      new TableColumn({ name: 'responsavel_periodo_mandato', type: 'varchar', isNullable: true }),
    ];

    for (const column of newColumns) {
      const currentTable = await queryRunner.getTable(tableName);
      if (!currentTable?.findColumnByName(column.name)) {
        await queryRunner.addColumn(tableName, column);
      }
    }
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'unidades_assistenciais';
    const table = await queryRunner.getTable(tableName);
    if (!table) return;

    const columnsToDrop = [
      'razao_social',
      'cnpj',
      'responsavel_nome',
      'responsavel_cpf',
      'responsavel_periodo_mandato',
    ];

    for (const columnName of columnsToDrop) {
      const currentTable = await queryRunner.getTable(tableName);
      if (currentTable?.findColumnByName(columnName)) {
        await queryRunner.dropColumn(tableName, columnName);
      }
    }

    const nomeFantasiaExists = table.findColumnByName('nome_fantasia');
    const nomeExists = table.findColumnByName('nome');

    if (nomeFantasiaExists && !nomeExists) {
      await queryRunner.renameColumn(tableName, 'nome_fantasia', 'nome');
    }
  }
}
