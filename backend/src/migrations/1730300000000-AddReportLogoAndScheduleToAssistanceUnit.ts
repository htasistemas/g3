import { MigrationInterface, QueryRunner, TableColumn } from 'typeorm';

export class AddReportLogoAndScheduleToAssistanceUnit1730300000000 implements MigrationInterface {
  name = 'AddReportLogoAndScheduleToAssistanceUnit1730300000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'unidades_assistenciais';
    const table = await queryRunner.getTable(tableName);

    if (!table) return;

    if (!table.findColumnByName('logomarca_relatorio')) {
      await queryRunner.addColumn(
        tableName,
        new TableColumn({ name: 'logomarca_relatorio', type: 'text', isNullable: true })
      );
    }

    if (!table.findColumnByName('horario_funcionamento')) {
      await queryRunner.addColumn(
        tableName,
        new TableColumn({ name: 'horario_funcionamento', type: 'varchar', isNullable: true })
      );
    }
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const tableName = 'unidades_assistenciais';
    const table = await queryRunner.getTable(tableName);

    if (!table) return;

    const scheduleColumn = table.findColumnByName('horario_funcionamento');
    if (scheduleColumn) {
      await queryRunner.dropColumn(tableName, scheduleColumn);
    }

    const reportLogoColumn = table.findColumnByName('logomarca_relatorio');
    if (reportLogoColumn) {
      await queryRunner.dropColumn(tableName, reportLogoColumn);
    }
  }
}
