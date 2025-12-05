import { MigrationInterface, QueryRunner, TableColumn } from 'typeorm';

export class AddBeneficiarioDocuments1730400000000 implements MigrationInterface {
  public readonly name = 'AddBeneficiarioDocuments1730400000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'beneficiario';

    if (!(await queryRunner.hasTable(tableName))) {
      return;
    }

    const hasDocumentColumn = await queryRunner.hasColumn(tableName, 'documentos_obrigatorios');
    if (hasDocumentColumn) {
      return;
    }

    await queryRunner.addColumn(
      tableName,
      new TableColumn({
        name: 'documentos_obrigatorios',
        type: 'jsonb',
        isNullable: true
      })
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'beneficiario';

    if (!(await queryRunner.hasTable(tableName))) {
      return;
    }

    const hasDocumentColumn = await queryRunner.hasColumn(tableName, 'documentos_obrigatorios');
    if (!hasDocumentColumn) {
      return;
    }

    await queryRunner.dropColumn(tableName, 'documentos_obrigatorios');
  }
}
