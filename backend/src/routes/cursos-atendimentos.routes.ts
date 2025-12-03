import { Router } from 'express';
import { AppDataSource } from '../data-source';
import { CursoAtendimento } from '../entities/CursoAtendimento';

const router = Router();

router.get('/', async (_req, res) => {
  const repository = AppDataSource.getRepository(CursoAtendimento);
  const records = await repository.find({ order: { createdAt: 'DESC' } });
  res.json({ records });
});

router.get('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(CursoAtendimento);
  const record = await repository.findOne({ where: { id: req.params.id } });
  if (!record) return res.status(404).json({ message: 'Registro não encontrado' });
  res.json({ record });
});

router.post('/', async (req, res) => {
  const repository = AppDataSource.getRepository(CursoAtendimento);
  const payload = repository.create(req.body);
  payload.vagasDisponiveis = Math.max(
    Number(payload.vagasTotais ?? 0) - (payload.enrollments || []).filter((e) => e.status === 'Ativo').length,
    0
  );
  const saved = await repository.save(payload);
  res.status(201).json({ record: saved });
});

router.put('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(CursoAtendimento);
  const existing = await repository.findOne({ where: { id: req.params.id } });
  if (!existing) return res.status(404).json({ message: 'Registro não encontrado' });

  const payload = repository.merge(existing, req.body);
  payload.vagasDisponiveis = Math.max(
    Number(payload.vagasTotais ?? 0) - (payload.enrollments || []).filter((e) => e.status === 'Ativo').length,
    0
  );

  const saved = await repository.save(payload);
  res.json({ record: saved });
});

router.delete('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(CursoAtendimento);
  const existing = await repository.findOne({ where: { id: req.params.id } });
  if (!existing) return res.status(404).json({ message: 'Registro não encontrado' });
  await repository.remove(existing);
  res.status(204).send();
});

export default router;
