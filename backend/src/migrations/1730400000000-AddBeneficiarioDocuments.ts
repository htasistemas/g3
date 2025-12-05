import { MigrationInterface, QueryRunner, TableColumn } from 'typeorm';

export class AddBeneficiarioDocuments1730400000000 implements MigrationInterface {
  name = 'AddBeneficiarioDocuments1730400000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    const driver = queryRunner.connection.driver.options.type;
    const isPostgres = driver === 'postgres';
    const column = new TableColumn({
      name: 'documentos_obrigatorios',
      type: isPostgres ? 'jsonb' : 'json',
      isNullable: true
    });

    await queryRunner.addColumn('beneficiario', column);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.dropColumn('beneficiario', 'documentos_obrigatorios');
  }
}
