import 'reflect-metadata';
import { AppDataSource } from '../data-source';
import { Beneficiario } from '../entities/Beneficiario';

async function main() {
  try {
    await AppDataSource.initialize();
    const beneficiaryRepo = AppDataSource.getRepository(Beneficiario);
    const total = await beneficiaryRepo.count();
    console.log(`Database connection ok. Beneficiários cadastrados: ${total}`);
  } catch (error) {
    console.error('Database connection failed', error);
    process.exit(1);
  } finally {
    await AppDataSource.destroy();
  }
}

main();
