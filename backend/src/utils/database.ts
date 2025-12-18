import { AppDataSource } from '../data-source';

let initializationPromise: Promise<void> | null = null;

const initialize = async (): Promise<void> => {
  if (!initializationPromise) {
    initializationPromise = (async () => {
      await AppDataSource.initialize();
      await AppDataSource.runMigrations();
    })().finally(() => {
      initializationPromise = null;
    });
  }

  return initializationPromise;
};

export const ensureDatabaseConnection = async (): Promise<void> => {
  if (AppDataSource.isInitialized) {
    try {
      await AppDataSource.query('SELECT 1');
      return;
    } catch (error) {
      console.warn('[database] existing connection is unavailable, reinitializing', error);
      try {
        await AppDataSource.destroy();
      } catch (destroyError) {
        console.warn('[database] failed to destroy stale connection', destroyError);
      }
    }
  }

  await initialize();
};
