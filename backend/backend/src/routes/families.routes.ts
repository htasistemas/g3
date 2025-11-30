import { Router } from 'express';
import { AppDataSource } from '../data-source';
import { Family } from '../entities/Family';
import { FamilyMember } from '../entities/FamilyMember';

const router = Router();

function validateMembers(members: FamilyMember[]): string | null {
  if (!members || !members.length) {
    return 'Informe ao menos um membro na composição familiar.';
  }

  for (const member of members) {
    if (!member.beneficiarioId) {
      return 'Todos os membros precisam de um beneficiário vinculado.';
    }
    if (!member.parentesco || String(member.parentesco).trim().length === 0) {
      return 'Todos os membros precisam ter o parentesco preenchido.';
    }
  }

  return null;
}

router.get('/:id', async (req, res) => {
  const id = Number(req.params.id);
  const repository = AppDataSource.getRepository(Family);
  const family = await repository.findOne({ where: { id }, relations: ['membros', 'membros.beneficiario'] });

  if (!family) {
    return res.status(404).json({ message: 'Família não encontrada' });
  }

  res.json({ familia: family });
});

router.post('/', async (req, res) => {
  const familyRepository = AppDataSource.getRepository(Family);
  const membersRepository = AppDataSource.getRepository(FamilyMember);
  const queryRunner = AppDataSource.createQueryRunner();

  const { membros = [], ...payload } = req.body as Family & { membros: FamilyMember[] };
  const validationMessage = validateMembers(membros);

  if (validationMessage) {
    return res.status(400).json({ message: validationMessage });
  }

  await queryRunner.connect();
  await queryRunner.startTransaction();

  try {
    const family = queryRunner.manager.create(Family, payload);
    const savedFamily = await queryRunner.manager.save(family);

    for (const member of membros) {
      const memberEntity = membersRepository.create({
        ...member,
        familiaId: savedFamily.id
      });
      await queryRunner.manager.save(memberEntity);
    }

    await queryRunner.commitTransaction();
    const created = await familyRepository.findOne({
      where: { id: savedFamily.id },
      relations: ['membros', 'membros.beneficiario']
    });
    res.status(201).json(created);
  } catch (error) {
    console.error('Failed to save family', error);
    await queryRunner.rollbackTransaction();
    res.status(500).json({ message: 'Falha ao salvar família' });
  } finally {
    await queryRunner.release();
  }
});

router.put('/:id', async (req, res) => {
  const familyRepository = AppDataSource.getRepository(Family);
  const membersRepository = AppDataSource.getRepository(FamilyMember);
  const queryRunner = AppDataSource.createQueryRunner();
  const id = Number(req.params.id);

  const { membros = [], ...payload } = req.body as Family & { membros: FamilyMember[] };
  const validationMessage = validateMembers(membros);

  if (validationMessage) {
    return res.status(400).json({ message: validationMessage });
  }

  await queryRunner.connect();
  await queryRunner.startTransaction();

  try {
    const existing = await familyRepository.findOne({ where: { id } });

    if (!existing) {
      return res.status(404).json({ message: 'Família não encontrada' });
    }

    familyRepository.merge(existing, payload);
    await queryRunner.manager.save(existing);

    const existingMembers = await membersRepository.find({ where: { familiaId: id } });
    const incomingIds = membros.filter((member) => member.id).map((member) => member.id) as number[];
    const toRemove = existingMembers.filter((member) => !incomingIds.includes(member.id));

    if (toRemove.length) {
      await queryRunner.manager.remove(toRemove);
    }

    for (const member of membros) {
      const memberPayload = membersRepository.create({ ...member, familiaId: id });
      await queryRunner.manager.save(memberPayload);
    }

    await queryRunner.commitTransaction();
    const updated = await familyRepository.findOne({ where: { id }, relations: ['membros', 'membros.beneficiario'] });
    res.json(updated);
  } catch (error) {
    console.error('Failed to update family', error);
    await queryRunner.rollbackTransaction();
    res.status(500).json({ message: 'Falha ao atualizar família' });
  } finally {
    await queryRunner.release();
  }
});

export default router;
