import { MigrationInterface, QueryRunner, TableColumn } from "typeorm";

export class AddBeneficiarioCodigo1730600000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    const table = await queryRunner.getTable("beneficiario");

    const hasCodigo =
      table?.columns.some((column) => column.name === "codigo") ?? false;

    // Se a coluna já existe, NÃO faz nada para evitar duplicação
    if (hasCodigo) {
      return;
    }

    await queryRunner.addColumn(
      "beneficiario",
      new TableColumn({
        name: "codigo",
        type: "varchar",
        length: "32",
        isNullable: true,
        isUnique: true,
      })
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const table = await queryRunner.getTable("beneficiario");

    const hasCodigo =
      table?.columns.some((column) => column.name === "codigo") ?? false;

    if (hasCodigo) {
      await queryRunner.dropColumn("beneficiario", "codigo");
    }
  }
}
