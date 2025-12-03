import { Router } from 'express';
import { AppDataSource } from '../data-source';
import { TermoFomento } from '../entities/TermoFomento';

const router = Router();

router.get('/', async (_req, res) => {
  const termos = await AppDataSource.getRepository(TermoFomento).find({ order: { createdAt: 'DESC' } });
  res.json({ termos });
});

router.post('/', async (req, res) => {
  const repository = AppDataSource.getRepository(TermoFomento);
  const payload = buildPayload(req.body);

  if (!payload.numero) {
    return res.status(400).json({ message: 'Número do termo é obrigatório' });
  }

  const termo = repository.create(payload);
  const saved = await repository.save(termo);
  res.status(201).json({ termo: saved });
});

router.put('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(TermoFomento);
  const termo = await repository.findOne({ where: { id: req.params.id } });
  if (!termo) return res.status(404).json({ message: 'Termo de fomento não encontrado' });

  const payload = buildPayload(req.body, termo);
  const saved = await repository.save(payload);
  res.json({ termo: saved });
});

router.delete('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(TermoFomento);
  const termo = await repository.findOne({ where: { id: req.params.id } });
  if (!termo) return res.status(404).json({ message: 'Termo de fomento não encontrado' });

  await repository.remove(termo);
  res.status(204).send();
});

function buildPayload(body: any, existing?: TermoFomento): TermoFomento {
  return {
    ...existing,
    numero: body.numero ?? existing?.numero,
    objeto: body.objeto ?? existing?.objeto,
    orgaoConcedente: body.orgaoConcedente ?? existing?.orgaoConcedente,
    modalidade: body.modalidade ?? existing?.modalidade,
    vigenciaInicio: body.vigenciaInicio ?? existing?.vigenciaInicio,
    vigenciaFim: body.vigenciaFim ?? existing?.vigenciaFim,
    valorGlobal: body.valorGlobal ?? existing?.valorGlobal,
    status: body.status ?? existing?.status
  } as TermoFomento;
}

export default router;
