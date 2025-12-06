import { MigrationInterface, QueryRunner, Table, TableColumn } from 'typeorm';

export class AddBeneficiarioDocuments1730400000000 implements MigrationInterface {
  name = 'AddBeneficiarioDocuments1730400000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    const driver = queryRunner.connection.driver.options.type;
    const isPostgres = driver === 'postgres';
    const table = await queryRunner.getTable('beneficiario');

    if (!table) return;

    const desiredColumn = new TableColumn({
      name: 'documentos_obrigatorios',
      type: isPostgres ? 'jsonb' : 'simple-json',
      isNullable: true
    });
    const desiredType = isPostgres ? 'jsonb' : 'text';

    const existingColumn = table.findColumnByName('documentos_obrigatorios');

    if (!existingColumn) {
      await queryRunner.addColumn(table.name, desiredColumn);
      return;
    }

    const existingType = (existingColumn.type as string).toLowerCase();
    if (existingType === desiredType || existingType === 'jsonb' || existingType === 'simple-json') return;

    if (isPostgres) {
      await queryRunner.query(
        'ALTER TABLE "beneficiario" ALTER COLUMN "documentos_obrigatorios" TYPE jsonb USING documentos_obrigatorios::jsonb'
      );
      return;
    }

    const normalizedColumns = table.columns.map((column) => {
      if (column.name !== 'documentos_obrigatorios') return column;

      return new TableColumn({
        ...column,
        type: desiredColumn.type,
        isNullable: true,
        default: column.default
      });
    });

    const temporaryTableName = 'temporary_beneficiario_documents_fix';

    await queryRunner.createTable(
      new Table({
        name: temporaryTableName,
        columns: normalizedColumns,
        indices: table.indices,
        uniques: table.uniques,
        foreignKeys: table.foreignKeys
      })
    );

    const columnNames = table.columns.map((column) => `"${column.name}"`).join(', ');
    await queryRunner.query(
      `INSERT INTO "${temporaryTableName}" (${columnNames}) SELECT ${columnNames} FROM "${table.name}"`
    );

    await queryRunner.dropTable(table.name);
    await queryRunner.renameTable(temporaryTableName, table.name);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const table = await queryRunner.getTable('beneficiario');
    const column = table?.findColumnByName('documentos_obrigatorios');

    if (table && column) {
      await queryRunner.dropColumn(table.name, column);
    }
  }
}
