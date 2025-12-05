import { MigrationInterface, QueryRunner } from "typeorm";

export class AddBeneficiarioDocuments1730400000000 implements MigrationInterface {
<<<<<<< HEAD
=======
  public readonly name = 'AddBeneficiarioDocuments1730400000000';

>>>>>>> 76e332a53542609843bdd6590dff35e4a398d17a
  public async up(queryRunner: QueryRunner): Promise<void> {
<<<<<<< HEAD
    // NO-OP: coluna documentos_obrigatorios já existe.
    return;
=======
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
>>>>>>> 42461ee7e1bb3260f1a3e48902ff1fab1f023c4e
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
<<<<<<< HEAD
    // NO-OP: não remover coluna existente.
    return;
=======
    const tableName = 'beneficiario';

    if (!(await queryRunner.hasTable(tableName))) {
      return;
    }

    const hasDocumentColumn = await queryRunner.hasColumn(tableName, 'documentos_obrigatorios');
    if (!hasDocumentColumn) {
      return;
    }

    await queryRunner.dropColumn(tableName, 'documentos_obrigatorios');
>>>>>>> 76e332a53542609843bdd6590dff35e4a398d17a
  }
}
