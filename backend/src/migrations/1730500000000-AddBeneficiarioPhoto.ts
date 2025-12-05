import { MigrationInterface, QueryRunner } from 'typeorm';

export class AddBeneficiarioPhoto1730500000000 implements MigrationInterface {
  public readonly name = 'AddBeneficiarioPhoto1730500000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'beneficiario';

    if (!(await queryRunner.hasTable(tableName))) {
      return;
    }

    await queryRunner.query(`ALTER TABLE ${tableName} ADD COLUMN IF NOT EXISTS foto_3x4 text`);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'beneficiario';

    if (!(await queryRunner.hasTable(tableName))) {
      return;
    }

    await queryRunner.query(`ALTER TABLE ${tableName} DROP COLUMN IF EXISTS foto_3x4`);
  }
}
