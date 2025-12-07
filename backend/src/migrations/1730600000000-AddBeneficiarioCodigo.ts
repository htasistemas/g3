import { MigrationInterface, QueryRunner, TableColumn, TableUnique } from 'typeorm';

export class AddBeneficiarioCodigo1730600000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    const hasCodigoColumn = await queryRunner.hasColumn('beneficiario', 'codigo');

    if (!hasCodigoColumn) {
      await queryRunner.addColumn(
        'beneficiario',
        new TableColumn({ name: 'codigo', type: 'varchar', length: '32', isNullable: true })
      );
    }

    const beneficiarios: Array<{ id_beneficiario: string }> = await queryRunner.query(
      'SELECT id_beneficiario FROM beneficiario ORDER BY data_cadastro ASC'
    );

    let counter = 1;
    for (const beneficiario of beneficiarios) {
      const codigo = `B-${String(counter).padStart(6, '0')}`;
      await queryRunner.query('UPDATE beneficiario SET codigo = ? WHERE id_beneficiario = ?', [
        codigo,
        beneficiario.id_beneficiario
      ]);
      counter += 1;
    }

    const hasUniqueConstraint = (await queryRunner.getTable('beneficiario'))?.uniques.some(
      (unique) => unique.columnNames.includes('codigo')
    );

    if (!hasUniqueConstraint) {
      await queryRunner.createUniqueConstraint(
        'beneficiario',
        new TableUnique({ name: 'UQ_beneficiario_codigo', columnNames: ['codigo'] })
      );
    }

    await queryRunner.changeColumn(
      'beneficiario',
      'codigo',
      new TableColumn({ name: 'codigo', type: 'varchar', length: '32', isNullable: false })
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.dropUniqueConstraint('beneficiario', 'UQ_beneficiario_codigo');
    await queryRunner.dropColumn('beneficiario', 'codigo');
  }
}
