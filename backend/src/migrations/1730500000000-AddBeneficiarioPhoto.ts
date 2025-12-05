import { MigrationInterface, QueryRunner } from "typeorm";

export class AddBeneficiarioPhoto1730500000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    // NO-OP: coluna foto_3x4 já existe.
    return;
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    // NO-OP.
    return;
  }
}
