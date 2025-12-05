import 'reflect-metadata';
import fs from 'node:fs';
import path from 'node:path';
import dotenv from 'dotenv';
import { DataSource, DataSourceOptions } from 'typeorm';
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
import { CreateBeneficiarioBaseSchema1730800000000 } from './migrations/1730800000000-CreateBeneficiarioBaseSchema';

dotenv.config();

type SupportedRelational = 'postgres' | 'mysql' | 'mariadb';

const dbTypeInput = (process.env.DB_TYPE as SupportedRelational | 'sqlite' | undefined) || 'postgres';

function isSupportedRelational(dbType: string | undefined): dbType is SupportedRelational {
  return ['postgres', 'mysql', 'mariadb'].includes(dbType as SupportedRelational);
}

const dbType: SupportedRelational = isSupportedRelational(dbTypeInput) ? dbTypeInput : 'postgres';

function resolveRelationalPort(): number {
  if (process.env.DB_PORT !== undefined && process.env.DB_PORT !== '') {
    return Number(process.env.DB_PORT);
  }

  return dbType === 'mysql' ? 3306 : 5432;
}

const migrations = [
  RenameSchemaToPortuguese1729700000000,
  CreateBeneficiarioFamiliaSchema1729800000000,
  UpdateAssistanceUnitSchema1730100000000,
  AddLogoToAssistanceUnit1730200000000,
  AddReportLogoAndScheduleToAssistanceUnit1730300000000,
  AddBeneficiarioDocuments1730400000000,
  AddBeneficiarioPhoto1730500000000,
  AddBeneficiarioCodigo1730600000000,
  CreateSalas1730700000000,
  CreateBeneficiarioBaseSchema1730800000000
];

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
  logging: (process.env.DB_LOGGING || '').toLowerCase() === 'true',
  migrations,
  migrationsRun: true,
  synchronize: false
} satisfies Partial<DataSourceOptions>;

const relationalOptions: DataSourceOptions = {
  ...baseOptions,
  type: dbType,
  host: process.env.DB_HOST || '72.60.156.202',
  port: resolveRelationalPort(),
  username: process.env.DB_USER || 'g3',
  password: process.env.DB_PASSWORD || 'admin',
  database: process.env.DB_NAME || 'g3'
};

function ensureSqliteDirectory(databasePath: string) {
  const directory = path.dirname(databasePath);

  if (!fs.existsSync(directory)) {
    fs.mkdirSync(directory, { recursive: true });
  }
}

function createDataSourceOptions(): DataSourceOptions {
  if (dbTypeInput === 'sqlite') {
    const databasePath = process.env.DB_NAME || './data/g3.sqlite';
    const resolvedDatabasePath = path.resolve(databasePath);

    ensureSqliteDirectory(resolvedDatabasePath);

    return {
      ...baseOptions,
      type: 'sqlite',
      database: resolvedDatabasePath
    } satisfies DataSourceOptions;
  }

  return relationalOptions;
}

export const AppDataSource = new DataSource(createDataSourceOptions());
