import { Router } from 'express';
import bcrypt from 'bcryptjs';
import { AppDataSource } from '../data-source';
import { User } from '../entities/User';

const router = Router();

router.post('/login', async (req, res) => {
  const { username, password } = req.body as { username?: string; password?: string };

  if (!username || !password) {
    return res.status(400).json({ message: 'Usuário e senha são obrigatórios.' });
  }

  const repo = AppDataSource.getRepository(User);
  const user = await repo.findOne({ where: { username } });

  if (!user) {
    return res.status(401).json({ message: 'Credenciais inválidas.' });
  }

  const valid = await bcrypt.compare(password, user.passwordHash);
  if (!valid) {
    return res.status(401).json({ message: 'Credenciais inválidas.' });
  }

  return res.json({
    token: 'session-token',
    user: { id: user.id, username: user.username },
  });
});

export default router;
