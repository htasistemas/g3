import { MigrationInterface, QueryRunner, TableColumn } from 'typeorm';

export class AddBeneficiarioPhoto1730500000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.addColumn(
      'beneficiario',
      new TableColumn({ name: 'foto_3x4', type: 'text', isNullable: true })
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.dropColumn('beneficiario', 'foto_3x4');
  }
}
