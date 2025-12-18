import bcrypt from 'bcryptjs';
import { AppDataSource } from '../data-source';
import { User } from '../entities/User';
import { ensureDatabaseConnection } from './database';

export async function ensureAdminUser(): Promise<void> {
  await ensureDatabaseConnection();

  const repo = AppDataSource.getRepository(User);
  const existing = await repo.findOne({ where: { nomeUsuario: 'admin' } });

  if (!existing) {
    const hashSenha = await bcrypt.hash('123', 10);
    const user = repo.create({ nomeUsuario: 'admin', hashSenha });
    await repo.save(user);
    console.info('Default admin user created.');
  }
}
