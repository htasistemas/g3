import { Router } from 'express';
import { Repository } from 'typeorm';
import { AppDataSource } from '../data-source';
import { CursoAtendimento } from '../entities/CursoAtendimento';

const router = Router();

type PartialCourse = Partial<CursoAtendimento> & {
  enrollments?: CursoAtendimento['enrollments'];
  waitlist?: CursoAtendimento['waitlist'];
};

const normalizeCpf = (cpf: string): string => cpf.replace(/\D/g, '');

const hasDuplicateBeneficiaries = (payload: PartialCourse) => {
  const keys = new Set<string>();

  const toKey = (item: { cpf?: string | null; beneficiaryName?: string }) => {
    if (item.cpf && item.cpf.trim() !== '') return `cpf:${normalizeCpf(item.cpf)}`;
    return `nome:${(item.beneficiaryName || '').trim().toLowerCase()}`;
  };

  const enrollments = payload.enrollments ?? [];
  const waitlist = payload.waitlist ?? [];

  for (const enrollment of [...enrollments, ...waitlist]) {
    const key = toKey(enrollment as any);
    if (!key || key === 'nome:') continue;
    if (keys.has(key)) return true;
    keys.add(key);
  }

  return false;
};

const hasRoomConflict = async (candidate: PartialCourse, ignoreId?: string): Promise<boolean> => {
  if (!candidate.salaId) return false;

  const repository = AppDataSource.getRepository(CursoAtendimento);
  const sameRoom = await repository.find({ where: { sala: { id: candidate.salaId } }, relations: ['sala'] });

  const candidateDays = new Set(candidate.diasSemana ?? []);
  const [candidateHour, candidateMinute] = (candidate.horarioInicial ?? '00:00').split(':').map(Number);
  const candidateStart = candidateHour * 60 + (candidateMinute || 0);
  const candidateEnd = candidateStart + Number(candidate.duracaoHoras ?? 0) * 60;

  return sameRoom.some((record) => {
    if (ignoreId && record.id === ignoreId) return false;
    const recordDays = new Set(record.diasSemana ?? []);
    const sharesDay = Array.from(candidateDays).some((day) => recordDays.has(day));
    if (!sharesDay) return false;

    const [hour, minute] = record.horarioInicial.split(':').map(Number);
    const start = hour * 60 + (minute || 0);
    const end = start + Number(record.duracaoHoras ?? 0) * 60;

    return candidateStart < end && start < candidateEnd;
  });
};

router.get('/', async (_req, res) => {
  const repository = AppDataSource.getRepository(CursoAtendimento);
  const records = await repository.find({ order: { createdAt: 'DESC' }, relations: ['sala'] });
  res.json({ records });
});

router.get('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(CursoAtendimento);
  const record = await repository.findOne({ where: { id: req.params.id }, relations: ['sala'] });
  if (!record) return res.status(404).json({ message: 'Registro não encontrado' });
  res.json({ record });
});

const preparePayload = async (
  repository: Repository<CursoAtendimento>,
  payload: PartialCourse
): Promise<CursoAtendimento> => {
  const course = repository.create({
    ...payload,
    sala: payload.salaId ? { id: payload.salaId } : null,
    salaId: payload.salaId ?? null,
    enrollments: payload.enrollments ?? [],
    waitlist: payload.waitlist ?? []
  });

  course.vagasDisponiveis = Math.max(
    Number(course.vagasTotais ?? 0) - (course.enrollments || []).filter((e) => e.status === 'Ativo').length,
    0
  );

  return course;
};

router.post('/', async (req, res) => {
  const repository = AppDataSource.getRepository(CursoAtendimento);
  const payload: PartialCourse = req.body;

  if (hasDuplicateBeneficiaries(payload)) {
    return res.status(400).json({ message: 'Beneficiário já inscrito neste curso/atendimento.' });
  }

  if (await hasRoomConflict(payload)) {
    return res
      .status(409)
      .json({ message: 'Já existe um curso/atendimento/oficina na mesma sala e horário selecionados.' });
  }

  const course = await preparePayload(repository, payload);
  const saved = await repository.save(course);
  const record = await repository.findOne({ where: { id: saved.id }, relations: ['sala'] });
  res.status(201).json({ record });
});

router.put('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(CursoAtendimento);
  const existing = await repository.findOne({ where: { id: req.params.id } });
  if (!existing) return res.status(404).json({ message: 'Registro não encontrado' });

  const payload: PartialCourse = { ...existing, ...req.body };

  if (hasDuplicateBeneficiaries(payload)) {
    return res.status(400).json({ message: 'Beneficiário já inscrito neste curso/atendimento.' });
  }

  if (await hasRoomConflict(payload, existing.id)) {
    return res
      .status(409)
      .json({ message: 'Já existe um curso/atendimento/oficina na mesma sala e horário selecionados.' });
  }

  const course = await preparePayload(repository, payload);

  const saved = await repository.save({ ...course, id: existing.id });
  const record = await repository.findOne({ where: { id: saved.id }, relations: ['sala'] });
  res.json({ record });
});

router.delete('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(CursoAtendimento);
  const existing = await repository.findOne({ where: { id: req.params.id } });
  if (!existing) return res.status(404).json({ message: 'Registro não encontrado' });
  await repository.remove(existing);
  res.status(204).send();
});

export default router;
