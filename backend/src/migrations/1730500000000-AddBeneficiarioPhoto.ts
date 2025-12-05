import { MigrationInterface, QueryRunner, TableColumn } from 'typeorm';

export class AddBeneficiarioPhoto1730500000000 implements MigrationInterface {
  public readonly name = 'AddBeneficiarioPhoto1730500000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'beneficiario';

    if (!(await queryRunner.hasTable(tableName))) {
      return;
    }

    const hasColumn = await queryRunner.hasColumn(tableName, 'foto_3x4');
    if (hasColumn) {
      return;
    }

    await queryRunner.addColumn(
      tableName,
      new TableColumn({ name: 'foto_3x4', type: 'text', isNullable: true })
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'beneficiario';

    if (!(await queryRunner.hasTable(tableName))) {
      return;
    }

    const hasColumn = await queryRunner.hasColumn(tableName, 'foto_3x4');
    if (!hasColumn) {
      return;
    }

    await queryRunner.dropColumn(tableName, 'foto_3x4');
  }
}
