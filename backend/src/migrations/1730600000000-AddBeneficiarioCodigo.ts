import { MigrationInterface, QueryRunner, TableColumn } from "typeorm";

export class AddBeneficiarioCodigo1730600000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    const tableName = "beneficiario";


    const hasCodigoColumn = await queryRunner.hasColumn(tableName, "codigo");

    if (hasCodigoColumn) {

      return;
    }

    await queryRunner.addColumn(
      tableName,
      new TableColumn({
        name: "codigo",
        type: "varchar",
        length: "32",
        isNullable: true,
      })
    );


    const hasCodigoUnique = (await queryRunner.getTable(tableName))
      ?.uniques.some((uq) => uq.columnNames.includes("codigo"));


    if (!hasCodigoUnique) {
      await queryRunner.query(
        `CREATE UNIQUE INDEX IF NOT EXISTS "UQ_beneficiario_codigo" ON "${tableName}" ("codigo")`
      );
    }
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const tableName = "beneficiario";


    const hasCodigoColumn = await queryRunner.hasColumn(tableName, "codigo");

    if (hasCodigoColumn) {
      await queryRunner.dropColumn(tableName, "codigo");

    }

    try {
      await queryRunner.query(
        `DROP INDEX IF EXISTS "UQ_beneficiario_codigo"`
      );
    } catch (e) {
      // ignore if index does not exist
    }
  }
}
