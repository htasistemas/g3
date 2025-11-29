import { Router } from 'express';
import { AppDataSource } from '../data-source';
import { AssistanceUnit } from '../entities/AssistanceUnit';

const router = Router();

router.get('/', async (_req, res) => {
  const repository = AppDataSource.getRepository(AssistanceUnit);
  const units = await repository.find({ order: { name: 'ASC' } });
  res.json({ units });
});

router.post('/', async (req, res) => {
  const repository = AppDataSource.getRepository(AssistanceUnit);
  const unit = repository.create(req.body as AssistanceUnit);

  try {
    const saved = await repository.save(unit);
    res.status(201).json(saved);
  } catch (error) {
    console.error('Failed to save assistance unit', error);
    res.status(500).json({ message: 'Falha ao salvar unidade assistencial' });
  }
});

export default router;
