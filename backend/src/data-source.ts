import 'reflect-metadata';
import fs from 'fs';
import path from 'path';
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
import { RenameSchemaToPortuguese1729700000000 } from './migrations/1729700000000-RenameSchemaToPortuguese';
import { CreateBeneficiarioFamiliaSchema1729800000000 } from './migrations/1729800000000-CreateBeneficiarioFamiliaSchema';
import { UpdateAssistanceUnitSchema1730100000000 } from './migrations/1730100000000-UpdateAssistanceUnitSchema';
import { AddLogoToAssistanceUnit1730200000000 } from './migrations/1730200000000-AddLogoToAssistanceUnit';
import { AddReportLogoAndScheduleToAssistanceUnit1730300000000 } from './migrations/1730300000000-AddReportLogoAndScheduleToAssistanceUnit';
import { AddBeneficiarioDocuments1730400000000 } from './migrations/1730400000000-AddBeneficiarioDocuments';
import { AddBeneficiarioPhoto1730500000000 } from './migrations/1730500000000-AddBeneficiarioPhoto';
import { AddBeneficiarioCodigo1730600000000 } from './migrations/1730600000000-AddBeneficiarioCodigo';

dotenv.config();

type SupportedRelational = 'postgres' | 'mysql' | 'mariadb';
type SupportedDrivers = SupportedRelational | 'sqlite';

// Default to Postgres so multiple environments share the same persisted database
// unless SQLite is explicitly requested.
const dbType: SupportedDrivers = (process.env.DB_TYPE as SupportedDrivers) || 'postgres';

function resolveSqlitePath(): string {
  const rawPath = process.env.DB_NAME && process.env.DB_NAME.trim() !== '' ? process.env.DB_NAME.trim() : path.join('data', 'g3.sqlite');
  const absolutePath = path.isAbsolute(rawPath) ? rawPath : path.resolve(__dirname, '..', rawPath);
  fs.mkdirSync(path.dirname(absolutePath), { recursive: true });
  return absolutePath;
}

function resolveRelationalPort(): number {
  if (process.env.DB_PORT !== undefined && process.env.DB_PORT !== '') {
    return Number(process.env.DB_PORT);
  }

  return dbType === 'mysql' ? 3306 : 5432;
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
    CursoAtendimento
  ],
  logging: (process.env.DB_LOGGING || '').toLowerCase() === 'true'
} satisfies Partial<DataSourceOptions>;

const sqliteOptions: DataSourceOptions = {
  ...baseOptions,
  type: 'sqlite',
  database: resolveSqlitePath(),
  synchronize: true,
  migrationsRun: false
};

const relationalOptions: DataSourceOptions | null = dbType === 'sqlite'
  ? null
  : {
      ...baseOptions,
      type: dbType as SupportedRelational,
      host: process.env.DB_HOST || '72.60.156.202',
      port: resolveRelationalPort(),
      username: process.env.DB_USER || 'g3',
      password: process.env.DB_PASSWORD || 'admin',
      database: process.env.DB_NAME || 'g3',
      synchronize: false,
      migrations: [
        RenameSchemaToPortuguese1729700000000,
        CreateBeneficiarioFamiliaSchema1729800000000,
        UpdateAssistanceUnitSchema1730100000000,
        AddLogoToAssistanceUnit1730200000000,
        AddReportLogoAndScheduleToAssistanceUnit1730300000000,
        AddBeneficiarioDocuments1730400000000,
        AddBeneficiarioPhoto1730500000000,
        AddBeneficiarioCodigo1730600000000
      ],
      migrationsRun: true
    };

export const AppDataSource = new DataSource(dbType === 'sqlite' ? sqliteOptions : relationalOptions!);
