import { MigrationInterface, QueryRunner, TableColumn } from 'typeorm';

export class AddStatusToBeneficiario1730000000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    const table = 'beneficiario';

    const statusColumn = new TableColumn({
      name: 'status',
      type: 'varchar',
      isNullable: false,
      default: "'EM_ANALISE'"
    });

    const motivoBloqueioColumn = new TableColumn({
      name: 'motivo_bloqueio',
      type: 'text',
      isNullable: true
    });

    const hasStatus = await queryRunner.hasColumn(table, 'status');
    if (!hasStatus) {
      await queryRunner.addColumn(table, statusColumn);
    }

    const hasMotivoBloqueio = await queryRunner.hasColumn(table, 'motivo_bloqueio');
    if (!hasMotivoBloqueio) {
      await queryRunner.addColumn(table, motivoBloqueioColumn);
    }
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const table = 'beneficiario';

    const hasMotivoBloqueio = await queryRunner.hasColumn(table, 'motivo_bloqueio');
    if (hasMotivoBloqueio) {
      await queryRunner.dropColumn(table, 'motivo_bloqueio');
    }

    const hasStatus = await queryRunner.hasColumn(table, 'status');
    if (hasStatus) {
      await queryRunner.dropColumn(table, 'status');
    }
  }
}
