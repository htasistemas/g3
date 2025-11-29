import { Router } from 'express';
import { AppDataSource } from '../data-source';
import { Beneficiary } from '../entities/Beneficiary';

const router = Router();

const REQUIRED_DOCUMENTS = ['RG', 'CPF', 'Comprovante de residência', 'Foto 3x4'];

router.get('/documents', (_req, res) => {
  res.json({ documents: REQUIRED_DOCUMENTS });
});

router.get('/', async (_req, res) => {
  const repository = AppDataSource.getRepository(Beneficiary);
  const beneficiaries = await repository.find({ order: { createdAt: 'DESC' } });
  res.json({ beneficiaries });
});

router.post('/', async (req, res) => {
  const repository = AppDataSource.getRepository(Beneficiary);
  const beneficiary = repository.create(req.body as Beneficiary);

  try {
    const saved = await repository.save(beneficiary);
    res.status(201).json(saved);
  } catch (error) {
    console.error('Failed to save beneficiary', error);
    res.status(500).json({ message: 'Falha ao salvar beneficiário' });
  }
});

router.put('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(Beneficiary);
  const id = Number(req.params.id);
  const existing = await repository.findOne({ where: { id } });

  if (!existing) {
    return res.status(404).json({ message: 'Beneficiário não encontrado' });
  }

  repository.merge(existing, req.body as Beneficiary);

  try {
    const saved = await repository.save(existing);
    res.json(saved);
  } catch (error) {
    console.error('Failed to update beneficiary', error);
    res.status(500).json({ message: 'Falha ao atualizar beneficiário' });
  }
});

export default router;
