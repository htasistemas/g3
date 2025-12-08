import { Router } from 'express';
import { AppDataSource } from '../data-source';
import { Beneficiario } from '../entities/Beneficiario';
import { FamiliaMembro } from '../entities/FamiliaMembro';
import { BenefitGrant } from '../entities/BenefitGrant';

const router = Router();

router.get('/:beneficiarioId', async (req, res) => {
  const { beneficiarioId } = req.params;

  const beneficiarioRepo = AppDataSource.getRepository(Beneficiario);
  const familiaMembroRepo = AppDataSource.getRepository(FamiliaMembro);
  const benefitGrantRepo = AppDataSource.getRepository(BenefitGrant);

  const beneficiario = await beneficiarioRepo.findOne({ where: { idBeneficiario: beneficiarioId } });
  if (!beneficiario) {
    return res.status(404).json({ message: 'Beneficiário não encontrado' });
  }

  const composicaoFamiliar = await familiaMembroRepo.find({ where: { beneficiarioId }, relations: ['familia'] });
  const beneficios = await benefitGrantRepo.find({ where: { beneficiarioId } });

  res.json({
    identificacao: beneficiario,
    composicaoFamiliar,
    renda: {
      rendaFamiliar: composicaoFamiliar
        .map((m) => Number(m.rendaIndividual || 0))
        .filter((v) => !Number.isNaN(v))
        .reduce((acc, curr) => acc + curr, 0)
    },
    atendimentos: [],
    encaminhamentos: [],
    beneficiosEventuais: beneficios
  });
});

export default router;
