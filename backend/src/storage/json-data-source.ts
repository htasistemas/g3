import { promises as fs } from 'fs';
import path from 'path';
import { randomUUID } from 'crypto';
import {
  EntityTarget,
  FindManyOptions,
  FindOneOptions,
  FindOperator,
  FindOptionsWhere,
  ObjectLiteral
} from 'typeorm';
import type { DataSourceLike, EntityManagerLike, RepositoryLike } from './types';

interface JsonDatabaseFile {
  version: number;
  updatedAt: string;
  entities: Record<string, ObjectLiteral[]>;
  sequences: Record<string, number>;
}

type IdStrategy = 'uuid' | 'number';

interface EntityMeta {
  primaryKey: string;
  idStrategy: IdStrategy;
  createdAtField?: string;
  updatedAtField?: string;
}

interface RelationDefinition {
  type: 'one-to-many' | 'many-to-one';
  target: string;
  localKey: string;
  foreignKey?: string;
  targetKey?: string;
}

const DEFAULT_DB: JsonDatabaseFile = {
  version: 1,
  updatedAt: new Date().toISOString(),
  entities: {},
  sequences: {}
};

const entityMetadata: Record<string, EntityMeta> = {
  AssistanceUnit: { primaryKey: 'id', idStrategy: 'number', createdAtField: 'criadoEm' },
  Beneficiario: { primaryKey: 'idBeneficiario', idStrategy: 'uuid', createdAtField: 'dataCadastro', updatedAtField: 'dataAtualizacao' },
  BeneficiaryDocumentConfig: { primaryKey: 'id', idStrategy: 'number' },
  BenefitGrant: { primaryKey: 'id', idStrategy: 'uuid', createdAtField: 'createdAt', updatedAtField: 'updatedAt' },
  BenefitType: { primaryKey: 'id', idStrategy: 'uuid', createdAtField: 'createdAt', updatedAtField: 'updatedAt' },
  CursoAtendimento: { primaryKey: 'id', idStrategy: 'uuid', createdAtField: 'createdAt', updatedAtField: 'updatedAt' },
  Familia: { primaryKey: 'idFamilia', idStrategy: 'uuid', createdAtField: 'dataCadastro', updatedAtField: 'dataAtualizacao' },
  FamiliaMembro: { primaryKey: 'idFamiliaMembro', idStrategy: 'uuid', createdAtField: 'dataCadastro', updatedAtField: 'dataAtualizacao' },
  IndiceVulnerabilidadeFamiliar: { primaryKey: 'id', idStrategy: 'uuid', createdAtField: 'dataCalculo' },
  Patrimonio: { primaryKey: 'idPatrimonio', idStrategy: 'uuid', createdAtField: 'createdAt', updatedAtField: 'updatedAt' },
  PatrimonioMovimento: { primaryKey: 'idMovimento', idStrategy: 'uuid', createdAtField: 'dataMovimento' },
  PlanoAtividade: { primaryKey: 'id', idStrategy: 'uuid' },
  PlanoCronogramaItem: { primaryKey: 'id', idStrategy: 'uuid' },
  PlanoEquipe: { primaryKey: 'id', idStrategy: 'uuid' },
  PlanoEtapa: { primaryKey: 'id', idStrategy: 'uuid' },
  PlanoMeta: { primaryKey: 'id', idStrategy: 'uuid' },
  PlanoTrabalho: { primaryKey: 'id', idStrategy: 'uuid', createdAtField: 'createdAt', updatedAtField: 'updatedAt' },
  Prontuario: { primaryKey: 'idProntuario', idStrategy: 'uuid', createdAtField: 'createdAt', updatedAtField: 'updatedAt' },
  ProntuarioAtendimento: { primaryKey: 'idProntuarioAtendimento', idStrategy: 'uuid', createdAtField: 'createdAt', updatedAtField: 'updatedAt' },
  ProntuarioEncaminhamento: { primaryKey: 'idProntuarioEncaminhamento', idStrategy: 'uuid', createdAtField: 'createdAt', updatedAtField: 'updatedAt' },
  Sala: { primaryKey: 'id', idStrategy: 'uuid', createdAtField: 'createdAt', updatedAtField: 'updatedAt' },
  StockItem: { primaryKey: 'id', idStrategy: 'uuid', createdAtField: 'createdAt', updatedAtField: 'updatedAt' },
  StockMovement: { primaryKey: 'id', idStrategy: 'uuid', createdAtField: 'createdAt' },
  TermoFomento: { primaryKey: 'id', idStrategy: 'uuid', createdAtField: 'createdAt', updatedAtField: 'updatedAt' },
  User: { primaryKey: 'id', idStrategy: 'number', createdAtField: 'criadoEm', updatedAtField: 'atualizadoEm' }
};

const relationMap: Record<string, Record<string, RelationDefinition>> = {
  CursoAtendimento: {
    sala: { type: 'many-to-one', target: 'Sala', localKey: 'salaId', targetKey: 'id' }
  },
  Familia: {
    membros: { type: 'one-to-many', target: 'FamiliaMembro', localKey: 'idFamilia', foreignKey: 'familiaId' },
    referenciaFamiliar: {
      type: 'many-to-one',
      target: 'Beneficiario',
      localKey: 'idReferenciaFamiliar',
      targetKey: 'idBeneficiario'
    }
  },
  FamiliaMembro: {
    beneficiario: {
      type: 'many-to-one',
      target: 'Beneficiario',
      localKey: 'beneficiarioId',
      targetKey: 'idBeneficiario'
    },
    familia: { type: 'many-to-one', target: 'Familia', localKey: 'familiaId', targetKey: 'idFamilia' }
  },
  Patrimonio: {
    movimentos: {
      type: 'one-to-many',
      target: 'PatrimonioMovimento',
      localKey: 'idPatrimonio',
      foreignKey: 'patrimonioId'
    }
  },
  Prontuario: {
    atendimentos: {
      type: 'one-to-many',
      target: 'ProntuarioAtendimento',
      localKey: 'idProntuario',
      foreignKey: 'prontuarioId'
    },
    encaminhamentos: {
      type: 'one-to-many',
      target: 'ProntuarioEncaminhamento',
      localKey: 'idProntuario',
      foreignKey: 'prontuarioId'
    }
  }
};

const ENTITY_KEY = Symbol('jsonEntityKey');

const clone = <T>(value: T): T => structuredClone(value);

const attachEntityKey = <T extends ObjectLiteral>(value: T, key: string): T => {
  Object.defineProperty(value, ENTITY_KEY, {
    value: key,
    enumerable: false,
    writable: false
  });
  return value;
};

const getEntityKey = (value: unknown): string | undefined =>
  typeof value === 'object' && value !== null ? ((value as Record<symbol, string>)[ENTITY_KEY] as string) : undefined;

const sanitizeEntity = <T extends ObjectLiteral>(value: T): T => {
  const cleaned = clone(value);
  if (ENTITY_KEY in cleaned) {
    delete (cleaned as Record<symbol, unknown>)[ENTITY_KEY];
  }
  return cleaned;
};

const isFindOperator = (value: unknown): value is FindOperator<any> =>
  typeof value === 'object' && value !== null && 'type' in value && 'value' in value;

const toSearchRegex = (pattern: string, caseInsensitive: boolean) => {
  const escaped = pattern.replace(/[.*+?^${}()|[\]\\]/g, '\\$&').replace(/%/g, '.*');
  return new RegExp(`^${escaped}$`, caseInsensitive ? 'i' : undefined);
};

const matchFindOperator = (fieldValue: unknown, operator: FindOperator<any>) => {
  const type = operator.type;
  if (type === 'in') {
    const values = Array.isArray(operator.value) ? operator.value : [];
    return values.includes(fieldValue);
  }
  if (type === 'between') {
    const [start, end] = Array.isArray(operator.value) ? operator.value : [undefined, undefined];
    if (start === undefined || end === undefined) return false;
    const normalize = (value: unknown) =>
      value instanceof Date ? value.toISOString() : value;
    const current = normalize(fieldValue);
    return current >= normalize(start) && current <= normalize(end);
  }
  if (type === 'ilike' || type === 'like') {
    const pattern = String(operator.value ?? '');
    const regex = toSearchRegex(pattern, type === 'ilike');
    return regex.test(String(fieldValue ?? ''));
  }
  return fieldValue === operator.value;
};

const matchWhere = (item: ObjectLiteral, where: FindOptionsWhere<any> | FindOptionsWhere<any>[]): boolean => {
  if (Array.isArray(where)) {
    return where.some((entry) => matchWhere(item, entry));
  }

  return Object.entries(where).every(([key, value]) => {
    const fieldValue = (item as Record<string, unknown>)[key];
    if (isFindOperator(value)) {
      return matchFindOperator(fieldValue, value);
    }
    if (value && typeof value === 'object' && !Array.isArray(value)) {
      if (fieldValue && typeof fieldValue === 'object') {
        return matchWhere(fieldValue as ObjectLiteral, value as FindOptionsWhere<any>);
      }
      return false;
    }
    return fieldValue === value;
  });
};

const applyOrder = <T extends ObjectLiteral>(items: T[], order: FindManyOptions<T>['order']): T[] => {
  if (!order) return items;
  const entries = Object.entries(order);
  if (!entries.length) return items;

  return [...items].sort((a, b) => {
    for (const [field, direction] of entries) {
      const normalizedDirection =
        typeof direction === 'string' ? direction.toUpperCase() : direction === 1 ? 'ASC' : 'DESC';
      const valueA = (a as Record<string, unknown>)[field];
      const valueB = (b as Record<string, unknown>)[field];
      if (valueA === valueB) continue;
      if (valueA === undefined || valueA === null) return normalizedDirection === 'ASC' ? 1 : -1;
      if (valueB === undefined || valueB === null) return normalizedDirection === 'ASC' ? -1 : 1;
      if (valueA > valueB) return normalizedDirection === 'ASC' ? 1 : -1;
      if (valueA < valueB) return normalizedDirection === 'ASC' ? -1 : 1;
    }
    return 0;
  });
};

const buildRelationTree = (relations: string[]) => {
  const root: Record<string, Record<string, any>> = {};
  for (const relation of relations) {
    let node = root;
    for (const part of relation.split('.')) {
      node[part] ??= {};
      node = node[part];
    }
  }
  return root;
};

export class JsonDataSource implements DataSourceLike {
  private readonly storagePath: string;
  private db: JsonDatabaseFile | null = null;
  isInitialized = false;

  manager: EntityManagerLike;

  constructor(storagePath: string) {
    this.storagePath = storagePath;
    this.manager = new JsonEntityManager(this);
  }

  async initialize(): Promise<void> {
    await this.loadDatabase();
    this.isInitialized = true;
  }

  async runMigrations(): Promise<void> {
    return;
  }

  async destroy(): Promise<void> {
    this.isInitialized = false;
  }

  async query(_query: string): Promise<unknown> {
    return { ok: true };
  }

  async transaction<T>(runInTransaction: (manager: EntityManagerLike) => Promise<T>): Promise<T> {
    return runInTransaction(this.manager);
  }

  getRepository<Entity extends ObjectLiteral>(target: EntityTarget<Entity>): RepositoryLike<Entity> {
    const key = typeof target === 'function' ? target.name : String(target);
    return new JsonRepository<Entity>(this, key);
  }

  async getDatabase(): Promise<JsonDatabaseFile> {
    if (!this.db) {
      await this.loadDatabase();
    }
    return this.db ?? clone(DEFAULT_DB);
  }

  async persistDatabase(db: JsonDatabaseFile): Promise<void> {
    db.updatedAt = new Date().toISOString();
    this.db = db;
    await fs.writeFile(this.storagePath, JSON.stringify(db, null, 2), 'utf8');
  }

  private async loadDatabase(): Promise<void> {
    if (this.db) return;
    await fs.mkdir(path.dirname(this.storagePath), { recursive: true });
    try {
      const raw = await fs.readFile(this.storagePath, 'utf8');
      this.db = JSON.parse(raw) as JsonDatabaseFile;
    } catch (error: any) {
      if (error?.code !== 'ENOENT') {
        throw error;
      }
      this.db = clone(DEFAULT_DB);
      await this.persistDatabase(this.db);
    }
  }
}

class JsonRepository<Entity extends ObjectLiteral> implements RepositoryLike<Entity> {
  private readonly entityKey: string;
  private readonly dataSource: JsonDataSource;

  constructor(dataSource: JsonDataSource, entityKey: string) {
    this.dataSource = dataSource;
    this.entityKey = entityKey;
  }

  async find(options?: FindManyOptions<Entity>): Promise<Entity[]> {
    const db = await this.dataSource.getDatabase();
    const items = (db.entities[this.entityKey] ?? []) as Entity[];
    let result = items.map((item) => attachEntityKey(clone(item), this.entityKey));

    if (options?.where) {
      result = result.filter((item) => matchWhere(item, options.where as FindOptionsWhere<Entity> | FindOptionsWhere<Entity>[]));
    }

    result = applyOrder(result, options?.order);

    if (options?.take !== undefined) {
      result = result.slice(0, options.take);
    }

    const relationList = Array.isArray(options?.relations)
      ? options?.relations
      : options?.relations
        ? Object.keys(options.relations).filter((key) => (options.relations as Record<string, boolean>)[key])
        : [];

    if (relationList.length) {
      await this.attachRelations(result, relationList);
    }

    const selectList = Array.isArray(options?.select)
      ? options?.select
      : options?.select
        ? Object.keys(options.select).filter((key) => (options.select as Record<string, boolean>)[key])
        : [];

    if (selectList.length) {
      result = result.map((item) => {
        const selected: ObjectLiteral = {};
        selectList.forEach((field) => {
          selected[field] = (item as ObjectLiteral)[field];
        });
        return selected as Entity;
      });
    }

    return result;
  }

  async findOne(options: FindOneOptions<Entity>): Promise<Entity | null> {
    const result = await this.find({ ...options, take: 1 } as FindManyOptions<Entity>);
    return result[0] ?? null;
  }

  create(entity?: Entity | Entity[]): Entity | Entity[] {
    if (!entity) {
      return attachEntityKey({} as Entity, this.entityKey);
    }
    if (Array.isArray(entity)) {
      return entity.map((item) => attachEntityKey(clone(item), this.entityKey));
    }
    return attachEntityKey(clone(entity), this.entityKey);
  }

  merge(target: Entity, ...sources: Partial<Entity>[]): Entity {
    return Object.assign(target, ...sources);
  }

  async save(entity: Entity | Entity[]): Promise<Entity | Entity[]> {
    if (Array.isArray(entity)) {
      const results = [];
      for (const entry of entity) {
        results.push(await this.save(entry));
      }
      return results as Entity[];
    }

    const db = await this.dataSource.getDatabase();
    const meta = entityMetadata[this.entityKey] ?? { primaryKey: 'id', idStrategy: 'uuid' };
    const items = (db.entities[this.entityKey] ?? []) as Entity[];
    const idValue = (entity as ObjectLiteral)[meta.primaryKey];
    const existingIndex = idValue ? items.findIndex((item) => (item as ObjectLiteral)[meta.primaryKey] === idValue) : -1;
    const now = new Date().toISOString();

    const nextEntity = sanitizeEntity(entity);

    if (existingIndex >= 0) {
      if (meta.updatedAtField) {
        (nextEntity as ObjectLiteral)[meta.updatedAtField] = now;
      }
      items[existingIndex] = { ...items[existingIndex], ...nextEntity };
    } else {
      if (!idValue) {
        (nextEntity as ObjectLiteral)[meta.primaryKey] = this.generateId(db, meta);
      }
      if (meta.createdAtField && !(nextEntity as ObjectLiteral)[meta.createdAtField]) {
        (nextEntity as ObjectLiteral)[meta.createdAtField] = now;
      }
      if (meta.updatedAtField && !(nextEntity as ObjectLiteral)[meta.updatedAtField]) {
        (nextEntity as ObjectLiteral)[meta.updatedAtField] = now;
      }
      items.push(nextEntity as Entity);
    }

    db.entities[this.entityKey] = items;
    await this.dataSource.persistDatabase(db);

    return attachEntityKey(clone(nextEntity as Entity), this.entityKey);
  }

  async delete(criteria: FindOptionsWhere<Entity> | FindOptionsWhere<Entity>[]): Promise<void> {
    const db = await this.dataSource.getDatabase();
    const items = (db.entities[this.entityKey] ?? []) as Entity[];
    db.entities[this.entityKey] = items.filter((item) => !matchWhere(item, criteria));
    await this.dataSource.persistDatabase(db);
  }

  async remove(entity: Entity | Entity[]): Promise<void> {
    const entities = Array.isArray(entity) ? entity : [entity];
    const meta = entityMetadata[this.entityKey] ?? { primaryKey: 'id', idStrategy: 'uuid' };
    const idsToRemove = new Set(
      entities.map((item) => (item as ObjectLiteral)[meta.primaryKey]).filter((value) => value !== undefined && value !== null)
    );

    if (idsToRemove.size === 0) return;

    const db = await this.dataSource.getDatabase();
    const items = (db.entities[this.entityKey] ?? []) as Entity[];
    db.entities[this.entityKey] = items.filter((item) => !idsToRemove.has((item as ObjectLiteral)[meta.primaryKey]));
    await this.dataSource.persistDatabase(db);
  }

  async count(options?: FindManyOptions<Entity>): Promise<number> {
    const result = await this.find(options);
    return result.length;
  }

  private generateId(db: JsonDatabaseFile, meta: EntityMeta): string | number {
    if (meta.idStrategy === 'number') {
      const next = (db.sequences[this.entityKey] ?? 0) + 1;
      db.sequences[this.entityKey] = next;
      return next;
    }
    return randomUUID();
  }

  private async attachRelations(items: Entity[], relations: string[]): Promise<void> {
    const tree = buildRelationTree(relations);
    await this.populateRelationTree(items, tree, this.entityKey);
  }

  private async populateRelationTree(
    items: Entity[],
    tree: Record<string, Record<string, any>>,
    currentEntityKey: string
  ): Promise<void> {
    const definitions = relationMap[currentEntityKey];
    if (!definitions) return;

    const entries = Object.entries(tree);
    for (const [relationName, nestedTree] of entries) {
      const definition = definitions[relationName];
      if (!definition) continue;

      if (definition.type === 'one-to-many') {
        const repo = this.dataSource.getRepository<ObjectLiteral>(definition.target);
        await Promise.all(
          items.map(async (item) => {
            const localValue = (item as ObjectLiteral)[definition.localKey];
            const related = localValue
              ? await repo.find({ where: { [definition.foreignKey!]: localValue } })
              : [];
            if (Object.keys(nestedTree).length && related.length) {
              await this.populateRelationTree(related as Entity[], nestedTree, definition.target);
            }
            (item as ObjectLiteral)[relationName] = related;
          })
        );
      } else {
        const repo = this.dataSource.getRepository<ObjectLiteral>(definition.target);
        await Promise.all(
          items.map(async (item) => {
            const localValue = (item as ObjectLiteral)[definition.localKey];
            if (!localValue) {
              (item as ObjectLiteral)[relationName] = null;
              return;
            }
            const targetKey = definition.targetKey ?? entityMetadata[definition.target]?.primaryKey ?? 'id';
            const related = await repo.findOne({ where: { [targetKey]: localValue } });
            if (related && Object.keys(nestedTree).length) {
              await this.populateRelationTree([related as Entity], nestedTree, definition.target);
            }
            (item as ObjectLiteral)[relationName] = related ?? null;
          })
        );
      }
    }
  }
}

class JsonEntityManager implements EntityManagerLike {
  private readonly dataSource: JsonDataSource;

  constructor(dataSource: JsonDataSource) {
    this.dataSource = dataSource;
  }

  getRepository<Entity extends ObjectLiteral>(target: EntityTarget<Entity>): RepositoryLike<Entity> {
    return this.dataSource.getRepository(target);
  }

  merge<Entity extends ObjectLiteral>(
    _target: EntityTarget<Entity> | Entity,
    entity: Entity,
    ...sources: Partial<Entity>[]
  ): Entity {
    const merged = Object.assign(entity, ...sources);
    const key = typeof _target === 'function' ? _target.name : getEntityKey(_target);
    if (key) {
      attachEntityKey(merged, key);
    }
    return merged;
  }

  async save<Entity extends ObjectLiteral>(entity: Entity | Entity[]): Promise<Entity | Entity[]> {
    if (Array.isArray(entity)) {
      return Promise.all(entity.map((entry) => this.save(entry))) as Promise<Entity[]>;
    }
    const key = getEntityKey(entity);
    const target = key ? key : (entity.constructor as EntityTarget<Entity>);
    const repo = this.getRepository(target as EntityTarget<Entity>);
    return repo.save(entity);
  }

  async remove<Entity extends ObjectLiteral>(
    target: EntityTarget<Entity> | Entity | Entity[],
    entities?: Entity | Entity[]
  ): Promise<void> {
    if (entities) {
      const repo = this.getRepository(target as EntityTarget<Entity>);
      await repo.remove(entities);
      return;
    }
    const entityValue = Array.isArray(target) ? target[0] : target;
    const key = getEntityKey(entityValue);
    const repoTarget = key ? key : (entityValue.constructor as EntityTarget<Entity>);
    const repo = this.getRepository(repoTarget as EntityTarget<Entity>);
    await repo.remove(target as Entity | Entity[]);
  }

  async transaction<T>(runInTransaction: (manager: EntityManagerLike) => Promise<T>): Promise<T> {
    return runInTransaction(this);
  }
}
