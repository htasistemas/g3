import { MigrationInterface, QueryRunner } from 'typeorm';

export class AddBeneficiarioDocuments1730400000000 implements MigrationInterface {
  public readonly name = 'AddBeneficiarioDocuments1730400000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    // NO-OP: coluna documentos_obrigatorios já existe.
    return;
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    // NO-OP: não remover coluna existente.
    return;
  }
}
