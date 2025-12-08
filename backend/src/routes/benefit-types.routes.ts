import { Router } from 'express';
import { AppDataSource } from '../data-source';
import { BenefitType } from '../entities/BenefitType';

const router = Router();

router.get('/', async (_req, res) => {
  const repo = AppDataSource.getRepository(BenefitType);
  const types = await repo.find({ order: { descricao: 'ASC' } });
  res.json({ records: types });
});

router.post('/', async (req, res) => {
  const repo = AppDataSource.getRepository(BenefitType);
  const type = repo.create({ descricao: req.body.descricao, ativo: req.body.ativo ?? true });
  const record = await repo.save(type);
  res.status(201).json({ record });
});

router.put('/:id', async (req, res) => {
  const repo = AppDataSource.getRepository(BenefitType);
  const existing = await repo.findOne({ where: { id: req.params.id } });
  if (!existing) return res.status(404).json({ message: 'Tipo não encontrado' });

  const record = await repo.save({ ...existing, descricao: req.body.descricao ?? existing.descricao, ativo: req.body.ativo ?? existing.ativo });
  res.json({ record });
});

router.delete('/:id', async (req, res) => {
  const repo = AppDataSource.getRepository(BenefitType);
  const existing = await repo.findOne({ where: { id: req.params.id } });
  if (!existing) return res.status(404).json({ message: 'Tipo não encontrado' });
  await repo.remove(existing);
  res.status(204).send();
});

export default router;
