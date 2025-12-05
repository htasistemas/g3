import 'reflect-metadata';
import dotenv from 'dotenv';
import { DataSource, DataSourceOptions } from 'typeorm';
import { AssistanceUnit } from './entities/AssistanceUnit';
import { Beneficiario } from './entities/Beneficiario';
import { BeneficiaryDocumentConfig } from './entities/BeneficiaryDocumentConfig';
import { CursoAtendimento } from './entities/CursoAtendimento';
import { Familia } from './entities/Familia';
import { FamiliaMembro } from './entities/FamiliaMembro';
import { PlanoAtividade } from './entities/PlanoAtividade';
import { PlanoCronogramaItem } from './entities/PlanoCronogramaItem';
import { PlanoEquipe } from './entities/PlanoEquipe';
import { PlanoEtapa } from './entities/PlanoEtapa';
import { PlanoMeta } from './entities/PlanoMeta';
import { PlanoTrabalho } from './entities/PlanoTrabalho';
import { Sala } from './entities/Sala';
import { Patrimonio } from './entities/Patrimonio';
import { PatrimonioMovimento } from './entities/PatrimonioMovimento';
import { TermoFomento } from './entities/TermoFomento';
import { User } from './entities/User';
import { AddBeneficiarioDocuments1730400000000 } from './migrations/1730400000000-AddBeneficiarioDocuments';
import { AddBeneficiarioPhoto1730500000000 } from './migrations/1730500000000-AddBeneficiarioPhoto';
import { AddBeneficiarioCodigo1730600000000 } from './migrations/1730600000000-AddBeneficiarioCodigo';
import { CreateSalas1730700000000 } from './migrations/1730700000000-CreateSalas';
import { CreateBeneficiarioBaseSchema1730800000000 } from './migrations/1730800000000-CreateBeneficiarioBaseSchema';
import { AddLogoToAssistanceUnit1730200000000 } from './migrations/1730200000000-AddLogoToAssistanceUnit';
import { AddReportLogoAndScheduleToAssistanceUnit1730300000000 } from './migrations/1730300000000-AddReportLogoAndScheduleToAssistanceUnit';
import { CreateBeneficiarioFamiliaSchema1729800000000 } from './migrations/1729800000000-CreateBeneficiarioFamiliaSchema';
import { RenameSchemaToPortuguese1729700000000 } from './migrations/1729700000000-RenameSchemaToPortuguese';
import { UpdateAssistanceUnitSchema1730100000000 } from './migrations/1730100000000-UpdateAssistanceUnitSchema';

dotenv.config();

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

const dataSourceOptions: DataSourceOptions = {
  type: 'postgres',
  host: process.env.DB_HOST || 'localhost',
  port: Number(process.env.DB_PORT || 5432),
  username: process.env.DB_USERNAME || 'g3',
  password: process.env.DB_PASSWORD || 'g3',
  database: process.env.DB_DATABASE || 'g3',
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
  migrations,
  migrationsRun: true,
  synchronize: false,
  logging: (process.env.DB_LOGGING || '').toLowerCase() === 'true'
};

const connectionOptions: DataSourceOptions = process.env.DATABASE_URL
  ? { ...dataSourceOptions, url: process.env.DATABASE_URL }
  : dataSourceOptions;

export const AppDataSource = new DataSource(connectionOptions);
