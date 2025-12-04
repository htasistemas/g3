import { Router } from 'express';
import { AppDataSource } from '../data-source';
import { Sala } from '../entities/Sala';

const router = Router();

router.get('/', async (_req, res) => {
  const repository = AppDataSource.getRepository(Sala);
  const rooms = await repository.find({ order: { nome: 'ASC' } });
  res.json({ rooms });
});

router.post('/', async (req, res) => {
  const repository = AppDataSource.getRepository(Sala);
  const nome = String(req.body?.nome ?? '').trim();

  if (!nome) {
    return res.status(400).json({ message: 'Informe o nome da sala.' });
  }

  const existing = await repository.findOne({ where: { nome } });
  if (existing) {
    return res.status(409).json({ message: 'Já existe uma sala cadastrada com este nome.' });
  }

  const room = repository.create({ nome });
  const saved = await repository.save(room);
  res.status(201).json({ room: saved });
});

router.put('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(Sala);
  const room = await repository.findOne({ where: { id: req.params.id } });
  if (!room) return res.status(404).json({ message: 'Sala não encontrada.' });

  const nome = String(req.body?.nome ?? '').trim();
  if (!nome) return res.status(400).json({ message: 'Informe o nome da sala.' });

  const duplicate = await repository.findOne({ where: { nome } });
  if (duplicate && duplicate.id !== room.id) {
    return res.status(409).json({ message: 'Já existe uma sala cadastrada com este nome.' });
  }

  const updated = await repository.save({ ...room, nome });
  res.json({ room: updated });
});

router.delete('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(Sala);
  const room = await repository.findOne({ where: { id: req.params.id } });
  if (!room) return res.status(404).json({ message: 'Sala não encontrada.' });

  await repository.remove(room);
  res.status(204).send();
});

export default router;
