import { MigrationInterface, QueryRunner } from 'typeorm';

export class AddBeneficiarioDocuments1730400000000 implements MigrationInterface {
  public readonly name = 'AddBeneficiarioDocuments1730400000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'beneficiario';

    if (!(await queryRunner.hasTable(tableName))) {
      return;
    }

    await queryRunner.query(
      `ALTER TABLE ${tableName} ADD COLUMN IF NOT EXISTS documentos_obrigatorios jsonb`
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'beneficiario';

    if (!(await queryRunner.hasTable(tableName))) {
      return;
    }

    await queryRunner.query(`ALTER TABLE ${tableName} DROP COLUMN IF EXISTS documentos_obrigatorios`);
  }
}
