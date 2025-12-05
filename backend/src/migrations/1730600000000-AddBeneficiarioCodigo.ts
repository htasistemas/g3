import { MigrationInterface, QueryRunner } from "typeorm";

export class AddBeneficiarioCodigo1730600000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    // NO-OP: coluna codigo já existe.
    return;
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    // NO-OP.
    return;
  }
}
