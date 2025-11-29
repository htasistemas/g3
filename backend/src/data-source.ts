import 'reflect-metadata';
import { DataSource } from 'typeorm';
import dotenv from 'dotenv';
import { User } from './entities/User';
import { Beneficiary } from './entities/Beneficiary';
import { AssistanceUnit } from './entities/AssistanceUnit';
import { BeneficiaryDocumentConfig } from './entities/BeneficiaryDocumentConfig';

dotenv.config();

const dbType = (process.env.DB_TYPE as any) || 'postgres';
const dbPort = Number(process.env.DB_PORT) || (dbType === 'mysql' ? 3306 : 5432);

export const AppDataSource = new DataSource({
  type: dbType,
  host: process.env.DB_HOST || '72.60.156.202',
  port: dbPort,
  username: process.env.DB_USER || 'g3',
  password: process.env.DB_PASSWORD || 'admin',
  database: process.env.DB_NAME || 'g3',
  synchronize: true,
  logging: false,
  entities: [User, Beneficiary, AssistanceUnit, BeneficiaryDocumentConfig],
});
