import { MigrationInterface, QueryRunner, TableColumn } from 'typeorm';

export class AddBeneficiarioCodigo1730600000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'beneficiario';

    const hasTable = await queryRunner.hasTable(tableName);
    if (!hasTable) return;

    const hasColumn = await queryRunner.hasColumn(tableName, 'codigo');

    if (!hasColumn) {
      await queryRunner.addColumn(
        tableName,
        new TableColumn({
          name: 'codigo',
          type: 'varchar',
          length: '32',
          isNullable: true,
          isUnique: true,
        })
      );

      const beneficiarios: Array<{ id_beneficiario: string }> = await queryRunner.query(
        'SELECT id_beneficiario FROM beneficiario ORDER BY data_cadastro ASC'
      );

      let counter = 1;
      for (const beneficiario of beneficiarios) {
        const codigo = `B-${String(counter).padStart(6, '0')}`;
        await queryRunner.query('UPDATE beneficiario SET codigo = $1 WHERE id_beneficiario = $2', [
          codigo,
          beneficiario.id_beneficiario,
        ]);
        counter += 1;
      }
    }
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'beneficiario';
    const hasTable = await queryRunner.hasTable(tableName);

    if (!hasTable) return;

    const hasColumn = await queryRunner.hasColumn(tableName, 'codigo');

    if (hasColumn) {
      await queryRunner.dropColumn(tableName, 'codigo');
    }
  }
}
