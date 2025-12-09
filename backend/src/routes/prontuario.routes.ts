import { Router } from 'express';
import { AppDataSource } from '../data-source';
import { Beneficiario } from '../entities/Beneficiario';
import { FamiliaMembro } from '../entities/FamiliaMembro';
import { Prontuario } from '../entities/Prontuario';
import { ProntuarioAtendimento } from '../entities/ProntuarioAtendimento';
import { ProntuarioEncaminhamento } from '../entities/ProntuarioEncaminhamento';

const router = Router();

router.get('/:beneficiarioId', async (req, res) => {
  const { beneficiarioId } = req.params;

  const beneficiarioRepo = AppDataSource.getRepository(Beneficiario);
  const familiaMembroRepo = AppDataSource.getRepository(FamiliaMembro);
  const prontuarioRepo = AppDataSource.getRepository(Prontuario);

  const beneficiario = await beneficiarioRepo.findOne({ where: { idBeneficiario: beneficiarioId } });
  if (!beneficiario) {
    return res.status(404).json({ message: 'Beneficiário não encontrado' });
  }

  const composicaoFamiliar = await familiaMembroRepo.find({
    where: { beneficiarioId },
    relations: ['familia', 'beneficiario']
  });

  const prontuario = await prontuarioRepo.findOne({
    where: { beneficiarioId },
    relations: ['atendimentos', 'encaminhamentos']
  });

  res.json({
    beneficiario,
    prontuario,
    composicaoFamiliar,
    historicoAtendimentos: prontuario?.atendimentos ?? [],
    encaminhamentos: prontuario?.encaminhamentos ?? []
  });
});

router.post('/:beneficiarioId', async (req, res) => {
  const { beneficiarioId } = req.params;
  const payload = req.body ?? {};

  const beneficiarioRepo = AppDataSource.getRepository(Beneficiario);
  const prontuarioRepo = AppDataSource.getRepository(Prontuario);
  const atendimentoRepo = AppDataSource.getRepository(ProntuarioAtendimento);
  const encaminhamentoRepo = AppDataSource.getRepository(ProntuarioEncaminhamento);
  const familiaMembroRepo = AppDataSource.getRepository(FamiliaMembro);

  const beneficiario = await beneficiarioRepo.findOne({ where: { idBeneficiario: beneficiarioId } });
  if (!beneficiario) {
    return res.status(404).json({ message: 'Beneficiário não encontrado' });
  }

  let prontuario = await prontuarioRepo.findOne({
    where: { beneficiarioId },
    relations: ['atendimentos', 'encaminhamentos']
  });

  await AppDataSource.transaction(async (manager) => {
    const repo = manager.getRepository(Prontuario);

    prontuario = repo.merge(prontuario ?? repo.create({ beneficiarioId }), {
      identificacao: payload.identificacao,
      composicaoFamiliar: payload.composicaoFamiliar,
      situacoesVulnerabilidade: payload.situacoesVulnerabilidade,
      participacoesServicos: payload.participacoesServicos,
      parecerTecnico: payload.parecerTecnico
    });

    prontuario = await repo.save(prontuario);

    const atendimentosPayload: ProntuarioAtendimento[] = (payload.historicoAtendimentos ?? []).map(
      (item: any) =>
        atendimentoRepo.create({
          idProntuarioAtendimento: item.idProntuarioAtendimento,
          prontuarioId: prontuario!.idProntuario,
          dataAtendimento: item.dataAtendimento,
          tipoAtendimento: item.tipoAtendimento,
          descricao: item.descricao,
          responsavel: item.responsavel,
          resultado: item.resultado
        })
    );

    const encaminhamentosPayload: ProntuarioEncaminhamento[] = (payload.encaminhamentos ?? []).map(
      (item: any) =>
        encaminhamentoRepo.create({
          idProntuarioEncaminhamento: item.idProntuarioEncaminhamento,
          prontuarioId: prontuario!.idProntuario,
          dataEncaminhamento: item.dataEncaminhamento,
          destino: item.destino,
          motivo: item.motivo,
          responsavel: item.responsavel,
          status: item.status,
          retornoPrevisto: item.retornoPrevisto,
          observacoes: item.observacoes
        })
    );

    await manager.getRepository(ProntuarioAtendimento).delete({ prontuarioId: prontuario.idProntuario });
    await manager
      .getRepository(ProntuarioEncaminhamento)
      .delete({ prontuarioId: prontuario.idProntuario });

    if (atendimentosPayload.length) {
      await manager.getRepository(ProntuarioAtendimento).save(atendimentosPayload);
    }

    if (encaminhamentosPayload.length) {
      await manager.getRepository(ProntuarioEncaminhamento).save(encaminhamentosPayload);
    }
  });

  const reloaded = await prontuarioRepo.findOne({
    where: { beneficiarioId },
    relations: ['atendimentos', 'encaminhamentos']
  });

  const composicaoFamiliar = await familiaMembroRepo.find({
    where: { beneficiarioId },
    relations: ['familia', 'beneficiario']
  });

  res.status(201).json({
    beneficiario,
    prontuario: reloaded,
    historicoAtendimentos: reloaded?.atendimentos ?? [],
    encaminhamentos: reloaded?.encaminhamentos ?? [],
    composicaoFamiliar
  });
});

export default router;
