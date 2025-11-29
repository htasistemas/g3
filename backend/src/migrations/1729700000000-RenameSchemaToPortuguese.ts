import { MigrationInterface, QueryRunner, TableColumn } from 'typeorm';

export class RenameSchemaToPortuguese1729700000000 implements MigrationInterface {
  name = 'RenameSchemaToPortuguese1729700000000';

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

    await this.renameColumnIfExists(queryRunner, newTable, ['fullName', 'fullname', 'full_name'], 'nome_completo');
    await this.renameColumnIfExists(queryRunner, newTable, ['motherName', 'mothername', 'mother_name'], 'nome_mae');
    await this.renameColumnIfExists(queryRunner, newTable, ['document', 'documents', 'documento'], 'documentos');
    await this.renameColumnIfExists(queryRunner, newTable, ['birthDate', 'birthdate', 'birth_date'], 'data_nascimento');
    await this.renameColumnIfExists(queryRunner, newTable, ['age'], 'idade');
    await this.renameColumnIfExists(queryRunner, newTable, ['phone', 'telephone'], 'telefone');
    await this.renameColumnIfExists(queryRunner, newTable, ['zipCode', 'zipcode', 'zip_code', 'cep'], 'cep');
    await this.renameColumnIfExists(queryRunner, newTable, ['address', 'endereco'], 'endereco');
    await this.renameColumnIfExists(queryRunner, newTable, ['addressNumber', 'address_number', 'numero_endereco'], 'numero_endereco');
    await this.renameColumnIfExists(queryRunner, newTable, ['referencePoint', 'reference_point', 'ponto_referencia'], 'ponto_referencia');
    await this.renameColumnIfExists(queryRunner, newTable, ['neighborhood', 'bairro'], 'bairro');
    await this.renameColumnIfExists(queryRunner, newTable, ['city', 'cidade'], 'cidade');
    await this.renameColumnIfExists(queryRunner, newTable, ['state', 'estado'], 'estado');
    await this.renameColumnIfExists(queryRunner, newTable, ['notes', 'observacoes'], 'observacoes');
    await this.renameColumnIfExists(queryRunner, newTable, ['hasMinorChildren', 'has_minor_children'], 'possui_filhos_menores');
    await this.renameColumnIfExists(queryRunner, newTable, ['hasDriverLicense', 'has_driver_license'], 'possui_cnh');
    await this.renameColumnIfExists(queryRunner, newTable, ['minorChildrenCount', 'minor_children_count'], 'quantidade_filhos_menores');
    await this.renameColumnIfExists(queryRunner, newTable, ['educationLevel', 'education_level'], 'escolaridade');
    await this.renameColumnIfExists(queryRunner, newTable, ['individualIncome', 'individual_income'], 'renda_individual');
    await this.renameColumnIfExists(queryRunner, newTable, ['familyIncome', 'family_income'], 'renda_familiar');
    await this.renameColumnIfExists(queryRunner, newTable, ['housingInformation', 'housing_information'], 'informacoes_moradia');
    await this.renameColumnIfExists(queryRunner, newTable, ['sanitationConditions', 'sanitation_conditions'], 'condicoes_saneamento');
    await this.renameColumnIfExists(queryRunner, newTable, ['employmentStatus', 'employment_status'], 'situacao_emprego');
    await this.renameColumnIfExists(queryRunner, newTable, ['occupation', 'ocupacao'], 'ocupacao');
    await this.renameColumnIfExists(queryRunner, newTable, ['documents', 'documentos_anexos'], 'documentos_anexos');
    await this.renameColumnIfExists(queryRunner, newTable, ['createdAt', 'created_at', 'criado_em'], 'criado_em');

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

    await this.renameColumnIfExists(queryRunner, newTable, ['name'], 'nome');
    await this.renameColumnIfExists(queryRunner, newTable, ['required'], 'obrigatorio');
  }

  private async revertBeneficiaries(queryRunner: QueryRunner) {
    const newTable = 'beneficiarios';
    const oldTable = 'beneficiaries';

    if (await queryRunner.hasTable(newTable)) {
      await this.renameColumnIfExists(queryRunner, newTable, ['nome_completo'], 'fullName');
      await this.renameColumnIfExists(queryRunner, newTable, ['nome_mae'], 'motherName');
      await this.renameColumnIfExists(queryRunner, newTable, ['documentos'], 'document');
      await this.renameColumnIfExists(queryRunner, newTable, ['data_nascimento'], 'birthDate');
      await this.renameColumnIfExists(queryRunner, newTable, ['idade'], 'age');
      await this.renameColumnIfExists(queryRunner, newTable, ['telefone'], 'phone');
      await this.renameColumnIfExists(queryRunner, newTable, ['cep'], 'zipCode');
      await this.renameColumnIfExists(queryRunner, newTable, ['endereco'], 'address');
      await this.renameColumnIfExists(queryRunner, newTable, ['numero_endereco'], 'addressNumber');
      await this.renameColumnIfExists(queryRunner, newTable, ['ponto_referencia'], 'referencePoint');
      await this.renameColumnIfExists(queryRunner, newTable, ['bairro'], 'neighborhood');
      await this.renameColumnIfExists(queryRunner, newTable, ['cidade'], 'city');
      await this.renameColumnIfExists(queryRunner, newTable, ['estado'], 'state');
      await this.renameColumnIfExists(queryRunner, newTable, ['observacoes'], 'notes');
      await this.renameColumnIfExists(queryRunner, newTable, ['possui_filhos_menores'], 'hasMinorChildren');
      await this.renameColumnIfExists(queryRunner, newTable, ['possui_cnh'], 'hasDriverLicense');
      await this.renameColumnIfExists(queryRunner, newTable, ['quantidade_filhos_menores'], 'minorChildrenCount');
      await this.renameColumnIfExists(queryRunner, newTable, ['escolaridade'], 'educationLevel');
      await this.renameColumnIfExists(queryRunner, newTable, ['renda_individual'], 'individualIncome');
      await this.renameColumnIfExists(queryRunner, newTable, ['renda_familiar'], 'familyIncome');
      await this.renameColumnIfExists(queryRunner, newTable, ['informacoes_moradia'], 'housingInformation');
      await this.renameColumnIfExists(queryRunner, newTable, ['condicoes_saneamento'], 'sanitationConditions');
      await this.renameColumnIfExists(queryRunner, newTable, ['situacao_emprego'], 'employmentStatus');
      await this.renameColumnIfExists(queryRunner, newTable, ['ocupacao'], 'occupation');
      await this.renameColumnIfExists(queryRunner, newTable, ['documentos_anexos'], 'documents');
      await this.renameColumnIfExists(queryRunner, newTable, ['criado_em'], 'createdAt');

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
      await this.renameColumnIfExists(queryRunner, newTable, ['nome_usuario'], 'username');
      await this.renameColumnIfExists(queryRunner, newTable, ['hash_senha'], 'password_hash');
      await this.renameColumnIfExists(queryRunner, newTable, ['criado_em'], 'created_at');
      await this.renameColumnIfExists(queryRunner, newTable, ['atualizado_em'], 'updated_at');
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

    await this.renameColumnIfExists(queryRunner, newTable, ['nome'], 'name');
    await this.renameColumnIfExists(queryRunner, newTable, ['obrigatorio'], 'required');

    if (await queryRunner.hasTable(newTable)) {
      if (!(await queryRunner.hasTable(oldTable))) {
        await queryRunner.renameTable(newTable, oldTable);
      }
    }
  }

  private async renameColumnIfExists(
    queryRunner: QueryRunner,
    tableName: string,
    currentNames: string[],
    targetName: string
  ) {
    const table = await queryRunner.getTable(tableName);
    if (!table) return;

    const targetExists = table.findColumnByName(targetName);
    if (targetExists) return;

    const source = currentNames.find((name) => table.findColumnByName(name));
    if (!source) return;

    await queryRunner.renameColumn(tableName, source, targetName);
  }
}
