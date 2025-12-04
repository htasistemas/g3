import { MigrationInterface, QueryRunner, TableColumn } from 'typeorm';

export class AddCertificateTemplateToCursoAtendimento1730140000000
  implements MigrationInterface
{
  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.addColumn(
      'cursos_atendimentos',
      new TableColumn({
        name: 'certificate_template',
        type: 'text',
        isNullable: true
      })
    );
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.dropColumn('cursos_atendimentos', 'certificate_template');
  }
}
