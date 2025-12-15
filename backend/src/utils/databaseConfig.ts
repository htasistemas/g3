import dotenv from 'dotenv';
import { DataSourceOptions } from 'typeorm';

dotenv.config();

const DEFAULT_CONNECTION_TIMEOUT_MS = 5_000;
const DEFAULT_STATEMENT_TIMEOUT_MS = 10_000;

export type DatabaseSource = 'DATABASE_URL' | 'DB_VARIABLES';

export interface DatabaseConfigInfo {
  source: DatabaseSource;
  host: string;
  port: number;
  database: string;
  user: string;
  maskedPassword: string;
}

interface ResolvedDatabaseConfig {
  options: DataSourceOptions;
  info: DatabaseConfigInfo;
}

const maskPassword = (password: string) => {
  if (!password) {
    return '***';
  }

  const visibleLength = Math.max(password.length, 3);
  return '*'.repeat(visibleLength);
};

const ensureHasValue = (value: string | undefined, name: string) => {
  if (!value || value.trim().length === 0) {
    throw new Error(`Database configuration error: ${name} is missing or empty.`);
  }

  return value.trim();
};

const parsePort = (rawPort: string, sourceLabel: string) => {
  const parsed = Number(rawPort);

  if (!Number.isInteger(parsed) || parsed <= 0) {
    throw new Error(`Database configuration error: ${sourceLabel} must be a positive integer.`);
  }

  return parsed;
};

export const buildDatabaseConfig = (): ResolvedDatabaseConfig => {
  const databaseUrl = process.env.DATABASE_URL?.trim();

  if (databaseUrl) {
    let parsedUrl: URL;

    try {
      parsedUrl = new URL(databaseUrl);
    } catch (error) {
      throw new Error(`Database configuration error: DATABASE_URL is invalid (${error}).`);
    }

    const host = ensureHasValue(parsedUrl.hostname, 'DATABASE_URL host');
    const database = ensureHasValue(parsedUrl.pathname.replace(/^\//, ''), 'DATABASE_URL database name');
    const user = ensureHasValue(parsedUrl.username, 'DATABASE_URL username');
    const password = ensureHasValue(parsedUrl.password, 'DATABASE_URL password');
    const port = parsedUrl.port ? parsePort(parsedUrl.port, 'DATABASE_URL port') : 5432;

    return {
      options: {
        type: 'postgres',
        url: databaseUrl,
        extra: {
          connectionTimeoutMillis: DEFAULT_CONNECTION_TIMEOUT_MS,
          statement_timeout: DEFAULT_STATEMENT_TIMEOUT_MS
        }
      },
      info: {
        source: 'DATABASE_URL',
        host,
        port,
        database,
        user,
        maskedPassword: maskPassword(password)
      }
    };
  }

  const host = ensureHasValue(process.env.DB_HOST, 'DB_HOST');
  const port = parsePort(ensureHasValue(process.env.DB_PORT, 'DB_PORT'), 'DB_PORT');
  const user = ensureHasValue(process.env.DB_USERNAME || process.env.DB_USER, 'DB_USERNAME');
  const password = ensureHasValue(process.env.DB_PASSWORD, 'DB_PASSWORD');
  const database = ensureHasValue(process.env.DB_NAME, 'DB_NAME');

  return {
    options: {
      type: 'postgres',
      host,
      port,
      username: user,
      password,
      database,
      extra: {
        connectionTimeoutMillis: DEFAULT_CONNECTION_TIMEOUT_MS,
        statement_timeout: DEFAULT_STATEMENT_TIMEOUT_MS
      }
    },
    info: {
      source: 'DB_VARIABLES',
      host,
      port,
      database,
      user,
      maskedPassword: maskPassword(password)
    }
  };
};
