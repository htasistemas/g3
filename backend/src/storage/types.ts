import type {
  DeepPartial,
  EntityTarget,
  FindManyOptions,
  FindOneOptions,
  FindOptionsWhere,
  ObjectLiteral
} from 'typeorm';

export interface RepositoryLike<Entity extends ObjectLiteral> {
  find(options?: FindManyOptions<Entity>): Promise<Entity[]>;
  findOne(options: FindOneOptions<Entity>): Promise<Entity | null>;
  save(entity: DeepPartial<Entity> | DeepPartial<Entity>[]): Promise<Entity | Entity[]>;
  create(entity?: DeepPartial<Entity> | DeepPartial<Entity>[]): Entity | Entity[];
  merge(target: Entity, ...sources: DeepPartial<Entity>[]): Entity;
  delete(criteria: FindOptionsWhere<Entity> | FindOptionsWhere<Entity>[]): Promise<void>;
  remove(entity: Entity | Entity[]): Promise<void>;
  count(options?: FindManyOptions<Entity>): Promise<number>;
}

export interface EntityManagerLike {
  getRepository<Entity extends ObjectLiteral>(target: EntityTarget<Entity>): RepositoryLike<Entity>;
  merge<Entity extends ObjectLiteral>(
    target: EntityTarget<Entity> | Entity,
    entity: Entity,
    ...sources: DeepPartial<Entity>[]
  ): Entity;
  save<Entity extends ObjectLiteral>(entity: DeepPartial<Entity> | DeepPartial<Entity>[]): Promise<Entity | Entity[]>;
  remove<Entity extends ObjectLiteral>(
    target: EntityTarget<Entity> | Entity | Entity[],
    entities?: Entity | Entity[]
  ): Promise<void>;
  transaction<T>(runInTransaction: (manager: EntityManagerLike) => Promise<T>): Promise<T>;
}

export interface DataSourceLike {
  isInitialized: boolean;
  initialize(): Promise<void>;
  runMigrations(): Promise<void>;
  destroy(): Promise<void>;
  query(query: string): Promise<unknown>;
  getRepository<Entity extends ObjectLiteral>(target: EntityTarget<Entity>): RepositoryLike<Entity>;
  transaction<T>(runInTransaction: (manager: EntityManagerLike) => Promise<T>): Promise<T>;
  manager: EntityManagerLike;
}
