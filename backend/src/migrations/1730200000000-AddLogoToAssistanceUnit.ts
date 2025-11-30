import { MigrationInterface, QueryRunner, TableColumn } from 'typeorm';

export class AddLogoToAssistanceUnit1730200000000 implements MigrationInterface {
  name = 'AddLogoToAssistanceUnit1730200000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'unidades_assistenciais';
    const table = await queryRunner.getTable(tableName);

    if (!table) return;

    const existing = table.findColumnByName('logomarca');

    if (!existing) {
      await queryRunner.addColumn(
        tableName,
        new TableColumn({ name: 'logomarca', type: 'text', isNullable: true })
      );
    }
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'unidades_assistenciais';
    const table = await queryRunner.getTable(tableName);

    if (!table) return;

    const existing = table.findColumnByName('logomarca');

    if (existing) {
      await queryRunner.dropColumn(tableName, existing);
    }
  }
}
