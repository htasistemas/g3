import { MigrationInterface, QueryRunner } from "typeorm";

export class AddBeneficiarioDocuments1730400000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
<<<<<<< HEAD
    // NO-OP: coluna documentos_obrigatorios já existe.
    return;
=======
    const tableName = 'beneficiario';
    const hasTable = await queryRunner.hasTable(tableName);

    if (!hasTable) return;

    const hasDocumentosCol = await queryRunner.hasColumn(tableName, 'documentos_obrigatorios');

    if (hasDocumentosCol) return;

    await queryRunner.addColumn(
      tableName,
      new TableColumn({
        name: 'documentos_obrigatorios',
        type: 'jsonb',
        isNullable: true,
      })
    );
>>>>>>> 42461ee7e1bb3260f1a3e48902ff1fab1f023c4e
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    // NO-OP: não remover coluna existente.
    return;
  }
}
