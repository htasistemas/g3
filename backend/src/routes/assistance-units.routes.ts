import { Router } from 'express';
import { AppDataSource } from '../data-source';
import { AssistanceUnit } from '../entities/AssistanceUnit';

const router = Router();

router.get('/', async (_req, res) => {
  const repository = AppDataSource.getRepository(AssistanceUnit);
  const unidade = await repository.findOne({ where: {}, order: { criadoEm: 'ASC' } });
  res.json({ unidade });
});

router.post('/', async (req, res) => {
  const repository = AppDataSource.getRepository(AssistanceUnit);
  const existing = await repository.count();

  if (existing > 0) {
    return res.status(400).json({ message: 'Já existe uma unidade cadastrada.' });
  }

  const unit = repository.create(req.body as AssistanceUnit);

  try {
    const saved = await repository.save(unit);
    res.status(201).json(saved);
  } catch (error) {
    console.error('Failed to save assistance unit', error);
    res.status(500).json({ message: 'Falha ao salvar unidade assistencial' });
  }
});

router.put('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(AssistanceUnit);
  const id = Number(req.params.id);
  const unidade = await repository.findOne({ where: { id } });

  if (!unidade) {
    return res.status(404).json({ message: 'Unidade assistencial não encontrada.' });
  }

  repository.merge(unidade, req.body as AssistanceUnit);

  try {
    const saved = await repository.save(unidade);
    res.json(saved);
  } catch (error) {
    console.error('Failed to update assistance unit', error);
    res.status(500).json({ message: 'Falha ao atualizar unidade assistencial' });
  }
});

router.delete('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(AssistanceUnit);
  const id = Number(req.params.id);
  const unidade = await repository.findOne({ where: { id } });

  if (!unidade) {
    return res.status(404).json({ message: 'Unidade assistencial não encontrada.' });
  }

  await repository.remove(unidade);
  res.status(204).send();
});

export default router;
