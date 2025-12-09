import { MigrationInterface, QueryRunner, TableColumn } from "typeorm";

export class AddBeneficiarioCodigo1730600000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    const tableName = "beneficiario";

    const table = await queryRunner.getTable(tableName);
    if (!table) {
      // Table not created yet (handled by later migrations)
      return;
    }

    const hasCodigoColumn = table.columns.some((column) => column.name === "codigo");
    if (hasCodigoColumn) {
      // Column already exists; nothing to do
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

    const hasCodigoUnique = table.uniques.some((unique) =>
      unique.columnNames.includes("codigo")
    );

    if (!hasCodigoUnique) {
      await queryRunner.query(
        `CREATE UNIQUE INDEX IF NOT EXISTS "UQ_beneficiario_codigo" ON "${tableName}" ("codigo")`
      );
    }
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const tableName = "beneficiario";

    const table = await queryRunner.getTable(tableName);
    if (table) {
      const hasCodigoColumn = table.columns.some((column) => column.name === "codigo");
      if (hasCodigoColumn) {
        await queryRunner.dropColumn(tableName, "codigo");
      }
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
