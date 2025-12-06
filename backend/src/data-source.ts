import 'reflect-metadata';
import dotenv from 'dotenv';
import { DataSource, DataSourceOptions } from 'typeorm';
import fs from 'fs';
import path from 'path';
import { User } from './entities/User';
import { Beneficiario } from './entities/Beneficiario';
import { AssistanceUnit } from './entities/AssistanceUnit';
import { BeneficiaryDocumentConfig } from './entities/BeneficiaryDocumentConfig';
import { Familia } from './entities/Familia';
import { FamiliaMembro } from './entities/FamiliaMembro';
import { Patrimonio } from './entities/Patrimonio';
import { PatrimonioMovimento } from './entities/PatrimonioMovimento';
import { TermoFomento } from './entities/TermoFomento';
import { PlanoTrabalho } from './entities/PlanoTrabalho';
import { PlanoMeta } from './entities/PlanoMeta';
import { PlanoAtividade } from './entities/PlanoAtividade';
import { PlanoEtapa } from './entities/PlanoEtapa';
import { PlanoCronogramaItem } from './entities/PlanoCronogramaItem';
import { PlanoEquipe } from './entities/PlanoEquipe';
import { CursoAtendimento } from './entities/CursoAtendimento';
import { Sala } from './entities/Sala';
import { RenameSchemaToPortuguese1729700000000 } from './migrations/1729700000000-RenameSchemaToPortuguese';
import { CreateBeneficiarioFamiliaSchema1729800000000 } from './migrations/1729800000000-CreateBeneficiarioFamiliaSchema';
import { UpdateAssistanceUnitSchema1730100000000 } from './migrations/1730100000000-UpdateAssistanceUnitSchema';
import { AddLogoToAssistanceUnit1730200000000 } from './migrations/1730200000000-AddLogoToAssistanceUnit';
import { AddReportLogoAndScheduleToAssistanceUnit1730300000000 } from './migrations/1730300000000-AddReportLogoAndScheduleToAssistanceUnit';
import { AddBeneficiarioDocuments1730400000000 } from './migrations/1730400000000-AddBeneficiarioDocuments';
import { AddBeneficiarioPhoto1730500000000 } from './migrations/1730500000000-AddBeneficiarioPhoto';
import { AddBeneficiarioCodigo1730600000000 } from './migrations/1730600000000-AddBeneficiarioCodigo';
import { CreateSalas1730700000000 } from './migrations/1730700000000-CreateSalas';

dotenv.config();

type SupportedDatabase = 'postgres' | 'mysql' | 'mariadb' | 'sqlite';

// Default to Postgres so multiple environments share the same persisted database.
const dbType: SupportedDatabase = (process.env.DB_TYPE as SupportedDatabase) || 'postgres';

const migrations = [
  RenameSchemaToPortuguese1729700000000,
  CreateBeneficiarioFamiliaSchema1729800000000,
  UpdateAssistanceUnitSchema1730100000000,
  AddLogoToAssistanceUnit1730200000000,
  AddReportLogoAndScheduleToAssistanceUnit1730300000000,
  AddBeneficiarioDocuments1730400000000,
  AddBeneficiarioPhoto1730500000000,
  AddBeneficiarioCodigo1730600000000,
  CreateSalas1730700000000
];

function resolveRelationalPort(): number {
  if (process.env.DB_PORT !== undefined && process.env.DB_PORT !== '') {
    return Number(process.env.DB_PORT);
  }

  return dbType === 'mysql' ? 3306 : 5434;
}

function ensureSqliteDirectory(databasePath: string): void {
  const directory = path.dirname(databasePath);
  if (directory && directory !== '.') {
    fs.mkdirSync(directory, { recursive: true });
  }
}

const baseOptions = {
  entities: [
    User,
    Beneficiario,
    AssistanceUnit,
    BeneficiaryDocumentConfig,
    Familia,
    FamiliaMembro,
    Patrimonio,
    PatrimonioMovimento,
    TermoFomento,
    PlanoTrabalho,
    PlanoMeta,
    PlanoAtividade,
    PlanoEtapa,
    PlanoCronogramaItem,
    PlanoEquipe,
    CursoAtendimento,
    Sala
  ],
  logging: (process.env.DB_LOGGING || '').toLowerCase() === 'true'
} satisfies Partial<DataSourceOptions>;

const dataSourceOptions: DataSourceOptions = (() => {
  if (dbType === 'sqlite') {
    const database = process.env.DB_NAME || './data/g3.sqlite';
    ensureSqliteDirectory(database);

    return {
      ...baseOptions,
      type: 'sqlite',
      database,
      synchronize: false,
      migrations,
      migrationsRun: true
    } satisfies DataSourceOptions;
  }

  return {
    ...baseOptions,
    type: dbType as SupportedDatabase,
    host: process.env.DB_HOST || '72.60.156.202',
    port: resolveRelationalPort(),
    username: process.env.DB_USER || 'g3',
    password: process.env.DB_PASSWORD || 'g3',
    database: process.env.DB_NAME || 'g3',
    synchronize: false,
    migrations,
    migrationsRun: true
  } satisfies DataSourceOptions;
})();

export const AppDataSource = new DataSource(dataSourceOptions);
