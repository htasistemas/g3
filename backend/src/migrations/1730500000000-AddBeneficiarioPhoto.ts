import { MigrationInterface, QueryRunner, TableColumn } from 'typeorm';

export class AddBeneficiarioPhoto1730500000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    const table = await queryRunner.getTable('beneficiario');
    const hasColumn = table?.findColumnByName('foto_3x4');

    if (!table || hasColumn) return;

    await queryRunner.addColumn(table.name, new TableColumn({ name: 'foto_3x4', type: 'text', isNullable: true }));
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const table = await queryRunner.getTable('beneficiario');
    const column = table?.findColumnByName('foto_3x4');

    if (table && column) {
      await queryRunner.dropColumn(table.name, column);
    }
  }
}
