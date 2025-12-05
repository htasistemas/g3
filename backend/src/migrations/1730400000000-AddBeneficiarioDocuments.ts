import { MigrationInterface, QueryRunner, TableColumn } from 'typeorm';

export class AddBeneficiarioDocuments1730400000000 implements MigrationInterface {
  name = 'AddBeneficiarioDocuments1730400000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'beneficiario';
    const hasTable = await queryRunner.hasTable(tableName);

    if (!hasTable) return;

    const hasDocumentosCol = await queryRunner.hasColumn(tableName, 'documentos_obrigatorios');

    if (hasDocumentosCol) return;

    await queryRunner.addColumn(
      tableName,
      new TableColumn({
        name: 'documentos_obrigatorios',
        type: 'simple-json',
        isNullable: true,
      })
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'beneficiario';
    const hasTable = await queryRunner.hasTable(tableName);

    if (!hasTable) return;

    const hasDocumentosCol = await queryRunner.hasColumn(tableName, 'documentos_obrigatorios');

    if (hasDocumentosCol) {
      await queryRunner.dropColumn(tableName, 'documentos_obrigatorios');
    }
  }
}
