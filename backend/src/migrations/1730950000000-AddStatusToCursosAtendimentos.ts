import { MigrationInterface, QueryRunner, TableColumn } from 'typeorm';

export class AddStatusToCursosAtendimentos1730950000000 implements MigrationInterface {
  name = 'AddStatusToCursosAtendimentos1730950000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.addColumns('cursos_atendimentos', [
      new TableColumn({
        name: 'status',
        type: 'varchar',
        isNullable: false,
        default: `'TRIAGEM'`
      }),
      new TableColumn({
        name: 'statusHistory',
        type: 'text',
        isNullable: true
      }),
      new TableColumn({
        name: 'dataTriagem',
        type: 'timestamp',
        isNullable: true
      }),
      new TableColumn({
        name: 'dataEncaminhamento',
        type: 'timestamp',
        isNullable: true
      }),
      new TableColumn({
        name: 'dataConclusao',
        type: 'timestamp',
        isNullable: true
      })
    ]);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    await queryRunner.dropColumn('cursos_atendimentos', 'dataConclusao');
    await queryRunner.dropColumn('cursos_atendimentos', 'dataEncaminhamento');
    await queryRunner.dropColumn('cursos_atendimentos', 'dataTriagem');
    await queryRunner.dropColumn('cursos_atendimentos', 'statusHistory');
    await queryRunner.dropColumn('cursos_atendimentos', 'status');
  }
}
