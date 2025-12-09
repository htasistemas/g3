import { MigrationInterface, QueryRunner } from 'typeorm';

export class RenameBeneficiariesToBeneficiarios1731100000000 implements MigrationInterface {
  name = 'RenameBeneficiariesToBeneficiarios1731100000000';

  private readonly targetTable = 'beneficiarios';
  private readonly legacyTables = ['beneficiaries', 'beneficiario'];

  public async up(queryRunner: QueryRunner): Promise<void> {
    const existingTable = await this.findExistingTable(queryRunner);
    if (!existingTable) return;

    if (existingTable !== this.targetTable) {
      await queryRunner.renameTable(existingTable, this.targetTable);
    }
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    // Prefer reverting to the most recent legacy name if it exists.
    const rollbackTarget = (await queryRunner.hasTable('beneficiario')) ? 'beneficiario' : 'beneficiaries';

    if (await queryRunner.hasTable(this.targetTable)) {
      await queryRunner.renameTable(this.targetTable, rollbackTarget);
    }
  }

  private async findExistingTable(queryRunner: QueryRunner): Promise<string | null> {
    for (const tableName of [this.targetTable, ...this.legacyTables]) {
      if (await queryRunner.hasTable(tableName)) return tableName;
    }

    return null;
  }
}
