import { DataSourceOptions } from 'typeorm';

const DEFAULT_CONNECTION_TIMEOUT_MS = 5_000;
const DEFAULT_STATEMENT_TIMEOUT_MS = 10_000;
const DEFAULT_LOCAL_CONFIG = {
  host: 'localhost',
  port: 5432,
  database: 'g3',
  user: 'postgres',
  password: 'postgres',
  schema: 'public',
  ssl: false
};

const isProd = (process.env.NODE_ENV || '').toLowerCase() === 'production';

export type DatabaseSource = 'DATABASE_URL' | 'DB_VARIABLES';

export interface DatabaseConfigInfo {
  source: DatabaseSource;
  host: string;
  port: number;
  database: string;
  user: string;
  maskedPassword: string;
  ssl: boolean;
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

const resolveString = (value: string | undefined, name: string, fallback?: string) => {
  if (value && value.trim().length > 0) {
    return value.trim();
  }

  if (!isProd && fallback !== undefined) {
    return fallback;
  }

  return ensureHasValue(value, name);
};

const parsePort = (rawPort: string, sourceLabel: string) => {
  const parsed = Number(rawPort);

  if (!Number.isInteger(parsed) || parsed <= 0) {
    throw new Error(`Database configuration error: ${sourceLabel} must be a positive integer.`);
  }

  return parsed;
};

const resolvePort = (rawPort: string | undefined, sourceLabel: string, fallback: number) => {
  if (!rawPort || rawPort.trim().length === 0) {
    if (!isProd) {
      return fallback;
    }

    return parsePort(ensureHasValue(rawPort, sourceLabel), sourceLabel);
  }

  return parsePort(rawPort, sourceLabel);
};

const parseBoolean = (rawValue: string | undefined, fallback: boolean) => {
  if (!rawValue || rawValue.trim().length === 0) {
    return fallback;
  }

  const normalized = rawValue.trim().toLowerCase();
  if (['true', '1', 'yes'].includes(normalized)) {
    return true;
  }
  if (['false', '0', 'no'].includes(normalized)) {
    return false;
  }

  throw new Error(`Database configuration error: DB_SSL must be a boolean value.`);
};

export const buildDatabaseConfig = (): ResolvedDatabaseConfig => {
  const databaseUrl = process.env.DATABASE_URL?.trim();
  const sslEnabled = parseBoolean(process.env.DB_SSL, DEFAULT_LOCAL_CONFIG.ssl);

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
        ssl: sslEnabled,
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
        maskedPassword: maskPassword(password),
        ssl: sslEnabled
      }
    };
  }

  const host = resolveString(process.env.DB_HOST, 'DB_HOST', DEFAULT_LOCAL_CONFIG.host);
  const port = resolvePort(process.env.DB_PORT, 'DB_PORT', DEFAULT_LOCAL_CONFIG.port);
  const user = resolveString(
    process.env.DB_USERNAME || process.env.DB_USER,
    'DB_USER',
    DEFAULT_LOCAL_CONFIG.user
  );
  const password = resolveString(process.env.DB_PASSWORD, 'DB_PASSWORD', DEFAULT_LOCAL_CONFIG.password);
  const database = resolveString(process.env.DB_NAME, 'DB_NAME', DEFAULT_LOCAL_CONFIG.database);
  const schema = resolveString(process.env.DB_SCHEMA, 'DB_SCHEMA', DEFAULT_LOCAL_CONFIG.schema);

  return {
    options: {
      type: 'postgres',
      host,
      port,
      username: user,
      password,
      database,
      schema,
      ssl: sslEnabled,
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
      maskedPassword: maskPassword(password),
      ssl: sslEnabled
    }
  };
};
