import { MigrationInterface, QueryRunner, TableColumn } from 'typeorm';

export class AddBeneficiarioCodigo1730600000000 implements MigrationInterface {
  name = 'AddBeneficiarioCodigo1730600000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    const table = await queryRunner.getTable('beneficiario');
    const hasCodigo = table?.columns.find((column) => column.name === 'codigo');

    if (!hasCodigo) {
      await queryRunner.addColumn(
        'beneficiario',
        new TableColumn({
          name: 'codigo',
          type: 'varchar',
          length: '32',
          isNullable: true,
          isUnique: true,
        })
      );
    }
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const table = await queryRunner.getTable('beneficiario');
    const hasCodigo = table?.columns.find((column) => column.name === 'codigo');

    if (hasCodigo) {
      await queryRunner.dropColumn('beneficiario', 'codigo');
    }
  }
}
