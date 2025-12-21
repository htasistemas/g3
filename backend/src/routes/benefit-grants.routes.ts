import { Router } from 'express';
import { Between, FindOptionsWhere } from 'typeorm';
import { AppDataSource } from '../data-source';
import { BenefitGrant } from '../entities/BenefitGrant';
import type { RepositoryLike } from '../storage/types';

const router = Router();

const buildFilters = (query: any): FindOptionsWhere<BenefitGrant> => {
  const where: FindOptionsWhere<BenefitGrant> = {};

  if (query.beneficiarioId) where.beneficiarioId = query.beneficiarioId;
  if (query.benefitTypeId) where.benefitTypeId = query.benefitTypeId;
  if (query.termoFomentoId) where.termoFomentoId = query.termoFomentoId;

  if (query.start || query.end) {
    const start = query.start ? new Date(query.start).toISOString().slice(0, 10) : undefined;
    const end = query.end ? new Date(query.end).toISOString().slice(0, 10) : undefined;
    if (start && end) where.dataConcessao = Between(start, end);
    else if (start) where.dataConcessao = Between(start, start);
    else if (end) where.dataConcessao = Between(end, end);
  }

  return where;
};

const saveGrant = async (
  repo: RepositoryLike<BenefitGrant>,
  payload: Partial<BenefitGrant>
): Promise<BenefitGrant> => {
  const grant = repo.create({
    ...payload,
    beneficiario: payload.beneficiarioId ? { id: payload.beneficiarioId } as any : undefined,
    benefitType: payload.benefitTypeId ? { id: payload.benefitTypeId } as any : undefined,
    termoFomento: payload.termoFomentoId ? { id: payload.termoFomentoId } as any : null
  });

  return repo.save(grant);
};

router.get('/', async (req, res) => {
  const repo = AppDataSource.getRepository(BenefitGrant);
  const where = buildFilters(req.query);
  const records = await repo.find({ where, order: { dataConcessao: 'DESC', createdAt: 'DESC' } });
  res.json({ records });
});

router.post('/', async (req, res) => {
  const repo = AppDataSource.getRepository(BenefitGrant);
  const record = await saveGrant(repo, req.body);
  res.status(201).json({ record });
});

router.put('/:id', async (req, res) => {
  const repo = AppDataSource.getRepository(BenefitGrant);
  const existing = await repo.findOne({ where: { id: req.params.id } });
  if (!existing) return res.status(404).json({ message: 'Concessão não encontrada' });

  const record = await saveGrant(repo, { ...existing, ...req.body });
  res.json({ record });
});

router.delete('/:id', async (req, res) => {
  const repo = AppDataSource.getRepository(BenefitGrant);
  const existing = await repo.findOne({ where: { id: req.params.id } });
  if (!existing) return res.status(404).json({ message: 'Concessão não encontrada' });
  await repo.remove(existing);
  res.status(204).send();
});

router.get('/relatorios/resumo', async (req, res) => {
  const repo = AppDataSource.getRepository(BenefitGrant);
  const where = buildFilters(req.query);
  const records = await repo.find({ where });

  const totalHoras = records.length; // placeholder for compatibility
  res.json({ total: records.length, totalHoras });
});

export default router;
