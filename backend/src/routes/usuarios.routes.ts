import { Router } from 'express';
import bcrypt from 'bcryptjs';
import { AppDataSource } from '../data-source';
import { User } from '../entities/User';

const router = Router();

function toResponse(user: User) {
  return {
    id: user.id,
    nomeUsuario: user.nomeUsuario,
    criadoEm: user.criadoEm,
    atualizadoEm: user.atualizadoEm
  };
}

router.get('/', async (_req, res) => {
  const repo = AppDataSource.getRepository(User);
  const usuarios = await repo.find({ order: { nomeUsuario: 'ASC' } });

  res.json({ usuarios: usuarios.map(toResponse) });
});

router.post('/', async (req, res) => {
  const { nomeUsuario, senha } = req.body as { nomeUsuario?: string; senha?: string };

  if (!nomeUsuario || !senha) {
    return res.status(400).json({ message: 'Usuário e senha são obrigatórios.' });
  }

  const repo = AppDataSource.getRepository(User);
  const existing = await repo.findOne({ where: { nomeUsuario } });

  if (existing) {
    return res.status(409).json({ message: 'Já existe um usuário com este nome.' });
  }

  const hashSenha = await bcrypt.hash(senha, 10);
  const user = repo.create({ nomeUsuario, hashSenha });
  const saved = await repo.save(user);

  return res.status(201).json(toResponse(saved));
});

router.put('/:id', async (req, res) => {
  const { nomeUsuario, senha } = req.body as { nomeUsuario?: string; senha?: string };
  const id = Number(req.params.id);
  const repo = AppDataSource.getRepository(User);
  const user = await repo.findOne({ where: { id } });

  if (!user) {
    return res.status(404).json({ message: 'Usuário não encontrado.' });
  }

  if (nomeUsuario && nomeUsuario !== user.nomeUsuario) {
    const existing = await repo.findOne({ where: { nomeUsuario } });
    if (existing) {
      return res.status(409).json({ message: 'Já existe um usuário com este nome.' });
    }
    user.nomeUsuario = nomeUsuario;
  }

  if (senha) {
    user.hashSenha = await bcrypt.hash(senha, 10);
  }

  const saved = await repo.save(user);
  return res.json(toResponse(saved));
});

router.delete('/:id', async (req, res) => {
  const id = Number(req.params.id);
  const repo = AppDataSource.getRepository(User);
  const user = await repo.findOne({ where: { id } });

  if (!user) {
    return res.status(404).json({ message: 'Usuário não encontrado.' });
  }

  await repo.remove(user);
  return res.status(204).send();
});

export default router;
