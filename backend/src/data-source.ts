import 'reflect-metadata';
import { DataSource } from 'typeorm';
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
import { IndiceVulnerabilidadeFamiliar } from './entities/IndiceVulnerabilidadeFamiliar';
import { BenefitType } from './entities/BenefitType';
import { BenefitGrant } from './entities/BenefitGrant';
import { StockItem } from './entities/StockItem';
import { StockMovement } from './entities/StockMovement';
import { Prontuario } from './entities/Prontuario';
import { ProntuarioAtendimento } from './entities/ProntuarioAtendimento';
import { ProntuarioEncaminhamento } from './entities/ProntuarioEncaminhamento';
import { RenameSchemaToPortuguese1729700000000 } from './migrations/1729700000000-RenameSchemaToPortuguese';
import { CreateBeneficiarioFamiliaSchema1729800000000 } from './migrations/1729800000000-CreateBeneficiarioFamiliaSchema';
import { UpdateAssistanceUnitSchema1730100000000 } from './migrations/1730100000000-UpdateAssistanceUnitSchema';
import { AddLogoToAssistanceUnit1730200000000 } from './migrations/1730200000000-AddLogoToAssistanceUnit';
import { AddReportLogoAndScheduleToAssistanceUnit1730300000000 } from './migrations/1730300000000-AddReportLogoAndScheduleToAssistanceUnit';
import { AddBeneficiarioDocuments1730400000000 } from './migrations/1730400000000-AddBeneficiarioDocuments';
import { AddBeneficiarioPhoto1730500000000 } from './migrations/1730500000000-AddBeneficiarioPhoto';
import { CreateSalas1730700000000 } from './migrations/1730700000000-CreateSalas';
import { CreateBeneficiarioBaseSchema1730800000000 } from './migrations/1730800000000-CreateBeneficiarioBaseSchema';
import { CreateVulnerabilityIndex1730900000000 } from './migrations/1730900000000-CreateVulnerabilityIndex';
import { AddStatusToCursosAtendimentos1730950000000 } from './migrations/1730950000000-AddStatusToCursosAtendimentos';
import { CreateTermosFomento1730959000000 } from './migrations/1730959000000-CreateTermosFomento';
import { CreateBenefitsModule1730960000000 } from './migrations/1730960000000-CreateBenefitsModule';
import { CreateStockModule1731000000000 } from './migrations/1731000000000-CreateStockModule';
import { RenameBeneficiariesToBeneficiarios1731100000000 } from './migrations/1731100000000-RenameBeneficiariesToBeneficiarios';
import { CreateProntuarioModule1731200000000 } from './migrations/1731200000000-CreateProntuarioModule';
import { EnsureUsuariosTable1731300000000 } from './migrations/1731300000000-EnsureUsuariosTable';
import { RenameUsuariosToUsuario1731400000000 } from './migrations/1731400000000-RenameUsuariosToUsuario';
import { buildDatabaseConfig } from './utils/databaseConfig';

const { options: databaseOptions, info: dbConnectionInfo } = buildDatabaseConfig();
export { dbConnectionInfo };

export const AppDataSource = new DataSource({
  ...databaseOptions,
  synchronize: false,
  logging: false,
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
    Sala,
    IndiceVulnerabilidadeFamiliar,
    BenefitType,
    BenefitGrant,
    StockItem,
    StockMovement,
    Prontuario,
    ProntuarioAtendimento,
    ProntuarioEncaminhamento
  ],
  migrations: [
    RenameSchemaToPortuguese1729700000000,
    CreateBeneficiarioFamiliaSchema1729800000000,
    UpdateAssistanceUnitSchema1730100000000,
    AddLogoToAssistanceUnit1730200000000,
    AddReportLogoAndScheduleToAssistanceUnit1730300000000,
    AddBeneficiarioDocuments1730400000000,
    AddBeneficiarioPhoto1730500000000,
    CreateSalas1730700000000,
    CreateBeneficiarioBaseSchema1730800000000,
    CreateVulnerabilityIndex1730900000000,
    AddStatusToCursosAtendimentos1730950000000,
    CreateTermosFomento1730959000000,
    CreateBenefitsModule1730960000000,
    CreateStockModule1731000000000,
    RenameBeneficiariesToBeneficiarios1731100000000,
    CreateProntuarioModule1731200000000,
    EnsureUsuariosTable1731300000000,
    RenameUsuariosToUsuario1731400000000
  ]
});
