import { MigrationInterface, QueryRunner } from 'typeorm';

export class AddBeneficiarioCodigo1730600000000 implements MigrationInterface {
  public readonly name = 'AddBeneficiarioCodigo1730600000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'beneficiario';

    if (!(await queryRunner.hasTable(tableName))) {
      return;
    }

    const hasColumn = await queryRunner.hasColumn(tableName, 'codigo');

    await queryRunner.query(
      `ALTER TABLE ${tableName} ADD COLUMN IF NOT EXISTS codigo varchar(32) UNIQUE`
    );

    const beneficiarios: Array<{ id_beneficiario: string }> = await queryRunner.query(
      'SELECT id_beneficiario FROM beneficiario ORDER BY data_cadastro ASC'
    );

    if (!hasColumn) {
      let counter = 1;
      for (const beneficiario of beneficiarios) {
        const codigo = `B-${String(counter).padStart(6, '0')}`;
        await queryRunner.query('UPDATE beneficiario SET codigo = $1 WHERE id_beneficiario = $2', [
          codigo,
          beneficiario.id_beneficiario
        ]);
        counter += 1;
      }
    }
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'beneficiario';

    if (!(await queryRunner.hasTable(tableName))) {
      return;
    }

    await queryRunner.query(`ALTER TABLE ${tableName} DROP COLUMN IF EXISTS codigo`);
  }
}
