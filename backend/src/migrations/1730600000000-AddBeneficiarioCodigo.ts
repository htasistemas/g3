import { MigrationInterface, QueryRunner } from "typeorm";

export class AddBeneficiarioCodigo1730600000000 implements MigrationInterface {
  public async up(queryRunner: QueryRunner): Promise<void> {
    // Migration neutralizada.
    // Motivo: a coluna "codigo" já existe na tabela "beneficiario" no SQLite
    // e essa migration estava tentando recriar a tabela com 2 colunas "codigo".
    return;
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    // Também não remover nada aqui para não quebrar bancos já existentes.
    return;
  }
}
