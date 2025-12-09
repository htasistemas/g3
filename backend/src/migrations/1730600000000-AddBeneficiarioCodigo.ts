import { MigrationInterface, QueryRunner, TableColumn } from "typeorm";

export class AddBeneficiarioCodigo1730600000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    // Verifica se a coluna 'codigo' já existe na tabela 'beneficiario'
    const hasCodigo = await queryRunner.hasColumn("beneficiario", "codigo");

    if (hasCodigo) {
      // Já existe, não faz nada para evitar coluna duplicada
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
    const hasCodigo = await queryRunner.hasColumn("beneficiario", "codigo");

    if (!hasCodigo) {
      // Não existe a coluna, não faz nada
      return;
    }

    await queryRunner.dropColumn("beneficiario", "codigo");
  }
}
