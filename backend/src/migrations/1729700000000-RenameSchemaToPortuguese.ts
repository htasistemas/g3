import { MigrationInterface, QueryRunner, TableColumn } from 'typeorm';

export class Migration1729700000000 implements MigrationInterface {
  name = 'Migration1729700000000';

  public async up(queryRunner: QueryRunner): Promise<void> {
    await this.renameBeneficiaries(queryRunner);
    await this.renameUsers(queryRunner);
    await this.renameAssistanceUnits(queryRunner);
    await this.renameBeneficiaryDocuments(queryRunner);
  }

  public async down(queryRunner: QueryRunner): Promise<void> {
    // Reversão parcial para manter idempotência em ambientes já migrados.
    await this.revertAssistanceUnits(queryRunner);
    await this.revertUsers(queryRunner);
    await this.revertBeneficiaries(queryRunner);
    await this.revertBeneficiaryDocuments(queryRunner);
  }

  private async renameBeneficiaries(queryRunner: QueryRunner) {
    const oldTable = 'beneficiaries';
    const newTable = 'beneficiarios';

    if (await queryRunner.hasTable(oldTable)) {
      if (!(await queryRunner.hasTable(newTable))) {
        await queryRunner.renameTable(oldTable, newTable);
      }
    }

    const table = await queryRunner.getTable(newTable);
    if (!table) return;

    const renames: Array<[string, string]> = [
      ['fullName', 'nome_completo'],
      ['motherName', 'nome_mae'],
      ['document', 'documentos'],
      ['birthDate', 'data_nascimento'],
      ['age', 'idade'],
      ['phone', 'telefone'],
      ['zipCode', 'cep'],
      ['address', 'endereco'],
      ['addressNumber', 'numero_endereco'],
      ['referencePoint', 'ponto_referencia'],
      ['neighborhood', 'bairro'],
      ['city', 'cidade'],
      ['state', 'estado'],
      ['notes', 'observacoes'],
      ['hasMinorChildren', 'possui_filhos_menores'],
      ['hasDriverLicense', 'possui_cnh'],
      ['minorChildrenCount', 'quantidade_filhos_menores'],
      ['educationLevel', 'escolaridade'],
      ['individualIncome', 'renda_individual'],
      ['familyIncome', 'renda_familiar'],
      ['housingInformation', 'informacoes_moradia'],
      ['sanitationConditions', 'condicoes_saneamento'],
      ['employmentStatus', 'situacao_emprego'],
      ['occupation', 'ocupacao'],
      ['documents', 'documentos_anexos'],
      ['createdAt', 'criado_em'],
    ];

    for (const [oldName, newName] of renames) {
      const currentTable = await queryRunner.getTable(newTable);
      if (!currentTable) break;

      const oldColumn = currentTable.findColumnByName(oldName);
      const newColumn = currentTable.findColumnByName(newName);
      if (oldColumn && !newColumn) {
        await queryRunner.renameColumn(newTable, oldName, newName);
      }
    }

    const finalTable = await queryRunner.getTable(newTable);
    if (!finalTable) return;

    if (!finalTable.findColumnByName('foto')) {
      await queryRunner.addColumn(
        newTable,
        new TableColumn({ name: 'foto', type: 'text', isNullable: true })
      );
    }
  }

  private async renameUsers(queryRunner: QueryRunner) {
    const oldTable = 'users';
    const newTable = 'usuarios';

    if (await queryRunner.hasTable(oldTable)) {
      if (!(await queryRunner.hasTable(newTable))) {
        await queryRunner.renameTable(oldTable, newTable);
      }
    }

    const table = await queryRunner.getTable(newTable);
    if (!table) return;

    const renames: Array<[string, string]> = [
      ['username', 'nome_usuario'],
      ['password_hash', 'hash_senha'],
      ['created_at', 'criado_em'],
      ['updated_at', 'atualizado_em'],
    ];

    for (const [oldName, newName] of renames) {
      const currentTable = await queryRunner.getTable(newTable);
      if (!currentTable) break;

      const oldColumn = currentTable.findColumnByName(oldName);
      const newColumn = currentTable.findColumnByName(newName);
      if (oldColumn && !newColumn) {
        await queryRunner.renameColumn(newTable, oldName, newName);
      }
    }
  }

  private async renameAssistanceUnits(queryRunner: QueryRunner) {
    const oldTable = 'assistance_units';
    const newTable = 'unidades_assistenciais';

    if (await queryRunner.hasTable(oldTable)) {
      if (!(await queryRunner.hasTable(newTable))) {
        await queryRunner.renameTable(oldTable, newTable);
      }
    }

    const table = await queryRunner.getTable(newTable);
    if (!table) return;

    if (table.findColumnByName('name') && !table.findColumnByName('nome')) {
      await queryRunner.renameColumn(newTable, 'name', 'nome');
    }

    if (table.findColumnByName('createdAt') && !table.findColumnByName('criado_em')) {
      await queryRunner.renameColumn(newTable, 'createdAt', 'criado_em');
    }

    const finalTable = await queryRunner.getTable(newTable);
    if (!finalTable) return;

    const columnsToEnsure: TableColumn[] = [
      new TableColumn({ name: 'telefone', type: 'varchar', isNullable: true }),
      new TableColumn({ name: 'email', type: 'varchar', isNullable: true }),
      new TableColumn({ name: 'cep', type: 'varchar', isNullable: true }),
      new TableColumn({ name: 'endereco', type: 'varchar', isNullable: true }),
      new TableColumn({ name: 'numero_endereco', type: 'varchar', isNullable: true }),
      new TableColumn({ name: 'bairro', type: 'varchar', isNullable: true }),
      new TableColumn({ name: 'cidade', type: 'varchar', isNullable: true }),
      new TableColumn({ name: 'estado', type: 'varchar', isNullable: true }),
      new TableColumn({ name: 'observacoes', type: 'text', isNullable: true }),
    ];

    for (const column of columnsToEnsure) {
      const hasColumn = (await queryRunner.getTable(newTable))?.findColumnByName(column.name);
      if (!hasColumn) {
        await queryRunner.addColumn(newTable, column);
      }
    }
  }

  private async renameBeneficiaryDocuments(queryRunner: QueryRunner) {
    const oldTable = 'beneficiary_document_config';
    const newTable = 'config_documentos_beneficiario';

    if (await queryRunner.hasTable(oldTable)) {
      if (!(await queryRunner.hasTable(newTable))) {
        await queryRunner.renameTable(oldTable, newTable);
      }
    }

    const table = await queryRunner.getTable(newTable);

    if (!table) return;

    const renames: Array<[string, string]> = [
      ['name', 'nome'],
      ['required', 'obrigatorio'],
    ];

    for (const [oldName, newName] of renames) {
      const currentTable = await queryRunner.getTable(newTable);
      if (!currentTable) break;
      const oldColumn = currentTable.findColumnByName(oldName);
      const newColumn = currentTable.findColumnByName(newName);
      if (oldColumn && !newColumn) {
        await queryRunner.renameColumn(newTable, oldName, newName);
      }
    }
  }

  private async revertBeneficiaries(queryRunner: QueryRunner) {
    const newTable = 'beneficiarios';
    const oldTable = 'beneficiaries';

    if (await queryRunner.hasTable(newTable)) {
      const renames: Array<[string, string]> = [
        ['nome_completo', 'fullName'],
        ['nome_mae', 'motherName'],
        ['documentos', 'document'],
        ['data_nascimento', 'birthDate'],
        ['idade', 'age'],
        ['telefone', 'phone'],
        ['cep', 'zipCode'],
        ['endereco', 'address'],
        ['numero_endereco', 'addressNumber'],
        ['ponto_referencia', 'referencePoint'],
        ['bairro', 'neighborhood'],
        ['cidade', 'city'],
        ['estado', 'state'],
        ['observacoes', 'notes'],
        ['possui_filhos_menores', 'hasMinorChildren'],
        ['possui_cnh', 'hasDriverLicense'],
        ['quantidade_filhos_menores', 'minorChildrenCount'],
        ['escolaridade', 'educationLevel'],
        ['renda_individual', 'individualIncome'],
        ['renda_familiar', 'familyIncome'],
        ['informacoes_moradia', 'housingInformation'],
        ['condicoes_saneamento', 'sanitationConditions'],
        ['situacao_emprego', 'employmentStatus'],
        ['ocupacao', 'occupation'],
        ['documentos_anexos', 'documents'],
        ['criado_em', 'createdAt'],
      ];

      for (const [oldName, newName] of renames) {
        const currentTable = await queryRunner.getTable(newTable);
        if (!currentTable) break;
        const oldColumn = currentTable.findColumnByName(oldName);
        const newColumn = currentTable.findColumnByName(newName);
        if (oldColumn && !newColumn) {
          await queryRunner.renameColumn(newTable, oldName, newName);
        }
      }

      if ((await queryRunner.getTable(newTable))?.findColumnByName('foto')) {
        await queryRunner.dropColumn(newTable, 'foto');
      }
    }

    if (await queryRunner.hasTable(newTable)) {
      if (!(await queryRunner.hasTable(oldTable))) {
        await queryRunner.renameTable(newTable, oldTable);
      }
    }
  }

  private async revertUsers(queryRunner: QueryRunner) {
    const newTable = 'usuarios';
    const oldTable = 'users';

    if (await queryRunner.hasTable(newTable)) {
      const renames: Array<[string, string]> = [
        ['nome_usuario', 'username'],
        ['hash_senha', 'password_hash'],
        ['criado_em', 'created_at'],
        ['atualizado_em', 'updated_at'],
      ];

      for (const [oldName, newName] of renames) {
        const currentTable = await queryRunner.getTable(newTable);
        if (!currentTable) break;
        const oldColumn = currentTable.findColumnByName(oldName);
        const newColumn = currentTable.findColumnByName(newName);
        if (oldColumn && !newColumn) {
          await queryRunner.renameColumn(newTable, oldName, newName);
        }
      }
    }

    if (await queryRunner.hasTable(newTable)) {
      if (!(await queryRunner.hasTable(oldTable))) {
        await queryRunner.renameTable(newTable, oldTable);
      }
    }
  }

  private async revertAssistanceUnits(queryRunner: QueryRunner) {
    const newTable = 'unidades_assistenciais';
    const oldTable = 'assistance_units';

    if (await queryRunner.hasTable(newTable)) {
      const currentTable = await queryRunner.getTable(newTable);
      if (currentTable?.findColumnByName('nome') && !currentTable.findColumnByName('name')) {
        await queryRunner.renameColumn(newTable, 'nome', 'name');
      }
      if (currentTable?.findColumnByName('criado_em') && !currentTable.findColumnByName('createdAt')) {
        await queryRunner.renameColumn(newTable, 'criado_em', 'createdAt');
      }

      const optionalColumns = [
        'telefone',
        'email',
        'cep',
        'endereco',
        'numero_endereco',
        'bairro',
        'cidade',
        'estado',
        'observacoes',
      ];

      for (const columnName of optionalColumns) {
        const tableCheck = await queryRunner.getTable(newTable);
        if (tableCheck?.findColumnByName(columnName)) {
          await queryRunner.dropColumn(newTable, columnName);
        }
      }
    }

    if (await queryRunner.hasTable(newTable)) {
      if (!(await queryRunner.hasTable(oldTable))) {
        await queryRunner.renameTable(newTable, oldTable);
      }
    }
  }

  private async revertBeneficiaryDocuments(queryRunner: QueryRunner) {
    const newTable = 'config_documentos_beneficiario';
    const oldTable = 'beneficiary_document_config';
    const table = await queryRunner.getTable(newTable);

    if (!table) return;

    const renames: Array<[string, string]> = [
      ['nome', 'name'],
      ['obrigatorio', 'required'],
    ];

    for (const [oldName, newName] of renames) {
      const currentTable = await queryRunner.getTable(newTable);
      if (!currentTable) break;
      const oldColumn = currentTable.findColumnByName(oldName);
      const newColumn = currentTable.findColumnByName(newName);
      if (oldColumn && !newColumn) {
        await queryRunner.renameColumn(newTable, oldName, newName);
      }
    }

    if (await queryRunner.hasTable(newTable)) {
      if (!(await queryRunner.hasTable(oldTable))) {
        await queryRunner.renameTable(newTable, oldTable);
      }
    }
  }
}
