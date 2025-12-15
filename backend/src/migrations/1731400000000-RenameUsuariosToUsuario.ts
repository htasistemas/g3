import { MigrationInterface, QueryRunner } from 'typeorm';

export class RenameUsuariosToUsuario1731400000000 implements MigrationInterface {
  name = 'RenameUsuariosToUsuario1731400000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    const oldTable = 'usuarios';
    const newTable = 'usuario';

    if (await queryRunner.hasTable(oldTable)) {
      if (!(await queryRunner.hasTable(newTable))) {
        await queryRunner.renameTable(oldTable, newTable);
      }
    }
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    const oldTable = 'usuarios';
    const newTable = 'usuario';

    if (await queryRunner.hasTable(newTable)) {
      if (!(await queryRunner.hasTable(oldTable))) {
        await queryRunner.renameTable(newTable, oldTable);
      }
    }
  }
}
