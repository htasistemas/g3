import { Router } from 'express';
import { AppDataSource } from '../data-source';
import { Beneficiario } from '../entities/Beneficiario';
import { Familia } from '../entities/Familia';
import { FamiliaMembro } from '../entities/FamiliaMembro';

const router = Router();

function parseBoolean(value: unknown, fallback = false): boolean {
  if (typeof value === 'boolean') return value;
  if (typeof value === 'string') {
    const normalized = value.toLowerCase();
    if (['true', '1', 'on', 'yes', 'sim'].includes(normalized)) return true;
    if (['false', '0', 'off', 'no', 'nao', 'não'].includes(normalized)) return false;
  }
  return fallback;
}

function parseNumeric(value: unknown): string | undefined {
  if (value === null || value === undefined || value === '') return undefined;
  const num = Number(value);
  if (Number.isNaN(num)) return undefined;
  return num.toFixed(2);
}

function calculateAge(dateString?: string | null): number | null {
  if (!dateString) return null;
  const date = new Date(dateString);
  if (Number.isNaN(date.getTime())) return null;
  const today = new Date();
  let age = today.getFullYear() - date.getFullYear();
  const m = today.getMonth() - date.getMonth();
  if (m < 0 || (m === 0 && today.getDate() < date.getDate())) {
    age--;
  }
  return age;
}

async function ensureSingleResponsible(familiaId: string, responsavelId?: string) {
  if (!responsavelId) return;
  const membroRepository = AppDataSource.getRepository(FamiliaMembro);
  await membroRepository
    .createQueryBuilder()
    .update(FamiliaMembro)
    .set({ responsavelFamiliar: false })
    .where('id_familia = :id', { id: familiaId })
    .andWhere('id_familia_membro != :membroId', { membroId: responsavelId })
    .execute();
}

async function recalculateIndicadores(familia: Familia) {
  const membroRepository = AppDataSource.getRepository(FamiliaMembro);
  const membros = await membroRepository.find({ where: { familiaId: familia.idFamilia }, relations: ['beneficiario'] });

  let qtdMembros = 0;
  let qtdCriancas = 0;
  let qtdAdolescentes = 0;
  let qtdIdosos = 0;
  let qtdPessoasDeficiencia = 0;
  let rendaTotal = 0;

  membros.forEach((membro) => {
    qtdMembros += 1;
    const idade = calculateAge(membro.beneficiario.dataNascimento);
    if (idade !== null) {
      if (idade <= 12) qtdCriancas += 1;
      else if (idade <= 17) qtdAdolescentes += 1;
      else if (idade >= 60) qtdIdosos += 1;
    }
    if (membro.beneficiario.possuiDeficiencia) {
      qtdPessoasDeficiencia += 1;
    }
    const renda = membro.rendaIndividual ? Number(membro.rendaIndividual) : Number(membro.beneficiario.rendaMensal ?? 0);
    rendaTotal += Number.isNaN(renda) ? 0 : renda;
  });

  const rendaPerCapita = qtdMembros > 0 ? rendaTotal / qtdMembros : 0;
  familia.qtdMembros = qtdMembros;
  familia.qtdCriancas = qtdCriancas;
  familia.qtdAdolescentes = qtdAdolescentes;
  familia.qtdIdosos = qtdIdosos;
  familia.qtdPessoasDeficiencia = qtdPessoasDeficiencia;
  familia.rendaFamiliarTotal = rendaTotal.toFixed(2);
  familia.rendaPerCapita = rendaPerCapita.toFixed(2);

  if (rendaPerCapita <= 0.25 * 1320) {
    familia.faixaRendaPerCapita = 'ATE_1_4_SM';
  } else if (rendaPerCapita <= 0.5 * 1320) {
    familia.faixaRendaPerCapita = 'ENTRE_1_4_E_1_2_SM';
  } else if (rendaPerCapita <= 1320) {
    familia.faixaRendaPerCapita = 'ENTRE_1_2_E_1_SM';
  } else {
    familia.faixaRendaPerCapita = 'ACIMA_1_SM';
  }

  await AppDataSource.getRepository(Familia).save(familia);
}

function buildFamiliaPayload(body: any, existing?: Familia): Familia {
  return {
    ...existing,
    nomeFamilia: body.nome_familia ?? body.nomeFamilia ?? existing?.nomeFamilia ?? '',
    idReferenciaFamiliar: body.id_referencia_familiar ?? body.idReferenciaFamiliar ?? existing?.idReferenciaFamiliar,
    cep: body.cep ?? existing?.cep,
    logradouro: body.logradouro ?? existing?.logradouro,
    numero: body.numero ?? existing?.numero,
    complemento: body.complemento ?? existing?.complemento,
    bairro: body.bairro ?? existing?.bairro,
    pontoReferencia: body.ponto_referencia ?? existing?.pontoReferencia,
    municipio: body.municipio ?? existing?.municipio,
    uf: body.uf ?? existing?.uf,
    zona: body.zona ?? existing?.zona,
    situacaoImovel: body.situacao_imovel ?? existing?.situacaoImovel,
    tipoMoradia: body.tipo_moradia ?? existing?.tipoMoradia,
    aguaEncanada: parseBoolean(body.agua_encanada, existing?.aguaEncanada ?? false),
    esgotoTipo: body.esgoto_tipo ?? existing?.esgotoTipo,
    coletaLixo: body.coleta_lixo ?? existing?.coletaLixo,
    energiaEletrica: parseBoolean(body.energia_eletrica, existing?.energiaEletrica ?? false),
    internet: parseBoolean(body.internet, existing?.internet ?? false),
    arranjoFamiliar: body.arranjo_familiar ?? existing?.arranjoFamiliar,
    principaisFontesRenda: body.principais_fontes_renda ?? existing?.principaisFontesRenda,
    situacaoInsegurancaAlimentar:
      body.situacao_inseguranca_alimentar ?? existing?.situacaoInsegurancaAlimentar,
    possuiDividasRelevantes: parseBoolean(
      body.possui_dividas_relevantes,
      existing?.possuiDividasRelevantes ?? false
    ),
    descricaoDividas: body.descricao_dividas ?? existing?.descricaoDividas,
    vulnerabilidadesFamilia: body.vulnerabilidades_familia ?? existing?.vulnerabilidadesFamilia,
    servicosAcompanhamento: body.servicos_acompanhamento ?? existing?.servicosAcompanhamento,
    tecnicoResponsavel: body.tecnico_responsavel ?? existing?.tecnicoResponsavel,
    periodicidadeAtendimento: body.periodicidade_atendimento ?? existing?.periodicidadeAtendimento,
    proximaVisitaPrevista: body.proxima_visita_prevista ?? existing?.proximaVisitaPrevista,
    observacoes: body.observacoes ?? existing?.observacoes
  } as Familia;
}

async function persistMembros(familia: Familia, membros: any[] | undefined) {
  if (!membros || !membros.length) return;
  const membroRepository = AppDataSource.getRepository(FamiliaMembro);

  for (const membro of membros) {
    const entity = membroRepository.create({
      familiaId: familia.idFamilia,
      beneficiarioId: membro.id_beneficiario ?? membro.beneficiarioId,
      parentesco: membro.parentesco,
      responsavelFamiliar: parseBoolean(membro.responsavel_familiar, membro.responsavelFamiliar),
      contribuiRenda: parseBoolean(membro.contribui_renda, membro.contribuiRenda),
      rendaIndividual: parseNumeric(membro.renda_individual) ?? membro.rendaIndividual,
      participaServicos: parseBoolean(membro.participa_servicos, membro.participaServicos),
      observacoes: membro.observacoes
    });
    await membroRepository.save(entity);
    if (entity.responsavelFamiliar) {
      await ensureSingleResponsible(familia.idFamilia, entity.idFamiliaMembro);
    }
  }
}

router.get('/', async (req, res) => {
  const repository = AppDataSource.getRepository(Familia);
  const { nome_familia, municipio, referencia } = req.query as Record<string, string>;
  const qb = repository.createQueryBuilder('familia');

  if (nome_familia) {
    qb.andWhere('LOWER(familia.nome_familia) LIKE LOWER(:nome)', { nome: `%${nome_familia}%` });
  }
  if (municipio) {
    qb.andWhere('LOWER(familia.municipio) LIKE LOWER(:municipio)', { municipio: `%${municipio}%` });
  }
  if (referencia) {
    qb.leftJoin(Beneficiario, 'ref', 'ref.id_beneficiario = familia.id_referencia_familiar');
    qb.andWhere('LOWER(ref.nome_completo) LIKE LOWER(:ref) OR ref.cpf = :ref', {
      ref: `%${referencia}%`
    });
  }

  const familias = await qb.orderBy('familia.nome_familia', 'ASC').getMany();
  res.json({ familias });
});

router.get('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(Familia);
  const familia = await repository.findOne({
    where: { idFamilia: req.params.id },
    relations: ['membros', 'membros.beneficiario', 'referenciaFamiliar']
  });

  if (!familia) {
    return res.status(404).json({ message: 'Família não encontrada' });
  }

  res.json({ familia });
});

router.post('/', async (req, res) => {
  const repository = AppDataSource.getRepository(Familia);
  const payload = buildFamiliaPayload(req.body);

  if (!payload.nomeFamilia) {
    return res.status(400).json({ message: 'Nome da família é obrigatório' });
  }

  try {
    const created = repository.create(payload);
    const saved = await repository.save(created);
    await persistMembros(saved, req.body.membros);
    await recalculateIndicadores(saved);
    const updated = await repository.findOne({
      where: { idFamilia: saved.idFamilia },
      relations: ['membros', 'membros.beneficiario']
    });
    res.status(201).json({ familia: updated });
  } catch (error) {
    console.error('Erro ao salvar família', error);
    res.status(500).json({ message: 'Erro ao salvar família' });
  }
});

router.put('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(Familia);
  const familia = await repository.findOne({ where: { idFamilia: req.params.id } });

  if (!familia) {
    return res.status(404).json({ message: 'Família não encontrada' });
  }

  const payload = buildFamiliaPayload(req.body, familia);

  try {
    repository.merge(familia, payload);
    await repository.save(familia);
    await recalculateIndicadores(familia);
    const updated = await repository.findOne({
      where: { idFamilia: familia.idFamilia },
      relations: ['membros', 'membros.beneficiario']
    });
    res.json({ familia: updated });
  } catch (error) {
    console.error('Erro ao atualizar família', error);
    res.status(500).json({ message: 'Erro ao atualizar família' });
  }
});

router.post('/:id/membros', async (req, res) => {
  const familiaRepository = AppDataSource.getRepository(Familia);
  const membroRepository = AppDataSource.getRepository(FamiliaMembro);
  const familia = await familiaRepository.findOne({ where: { idFamilia: req.params.id } });

  if (!familia) {
    return res.status(404).json({ message: 'Família não encontrada' });
  }

  const payload = membroRepository.create({
    familiaId: familia.idFamilia,
    beneficiarioId: req.body.id_beneficiario ?? req.body.beneficiarioId,
    parentesco: req.body.parentesco,
    responsavelFamiliar: parseBoolean(req.body.responsavel_familiar, req.body.responsavelFamiliar),
    contribuiRenda: parseBoolean(req.body.contribui_renda, req.body.contribuiRenda),
    rendaIndividual: parseNumeric(req.body.renda_individual) ?? req.body.rendaIndividual,
    participaServicos: parseBoolean(req.body.participa_servicos, req.body.participaServicos),
    observacoes: req.body.observacoes
  });

  try {
    const saved = await membroRepository.save(payload);
    if (saved.responsavelFamiliar) {
      await ensureSingleResponsible(familia.idFamilia, saved.idFamiliaMembro);
    }
    await recalculateIndicadores(familia);
    res.status(201).json({ membro: saved });
  } catch (error) {
    console.error('Erro ao adicionar membro', error);
    res.status(500).json({ message: 'Erro ao adicionar membro' });
  }
});

router.put('/:id/membros/:idMembro', async (req, res) => {
  const membroRepository = AppDataSource.getRepository(FamiliaMembro);
  const familiaRepository = AppDataSource.getRepository(Familia);
  const membro = await membroRepository.findOne({ where: { idFamiliaMembro: req.params.idMembro } });

  if (!membro) {
    return res.status(404).json({ message: 'Membro não encontrado' });
  }

  membro.parentesco = req.body.parentesco ?? membro.parentesco;
  membro.contribuiRenda = parseBoolean(req.body.contribui_renda, membro.contribuiRenda);
  membro.rendaIndividual = parseNumeric(req.body.renda_individual) ?? membro.rendaIndividual;
  membro.participaServicos = parseBoolean(req.body.participa_servicos, membro.participaServicos);
  membro.observacoes = req.body.observacoes ?? membro.observacoes;

  const markedResponsible = parseBoolean(req.body.responsavel_familiar, membro.responsavelFamiliar);
  membro.responsavelFamiliar = markedResponsible;

  try {
    await membroRepository.save(membro);
    const familia = await familiaRepository.findOne({ where: { idFamilia: membro.familiaId } });
    if (membro.responsavelFamiliar) {
      await ensureSingleResponsible(membro.familiaId, membro.idFamiliaMembro);
    }
    if (familia) {
      await recalculateIndicadores(familia);
    }
    res.json({ membro });
  } catch (error) {
    console.error('Erro ao atualizar membro', error);
    res.status(500).json({ message: 'Erro ao atualizar membro' });
  }
});

router.delete('/:id/membros/:idMembro', async (req, res) => {
  const membroRepository = AppDataSource.getRepository(FamiliaMembro);
  const familiaRepository = AppDataSource.getRepository(Familia);
  const membro = await membroRepository.findOne({ where: { idFamiliaMembro: req.params.idMembro } });

  if (!membro) {
    return res.status(404).json({ message: 'Membro não encontrado' });
  }

  await membroRepository.remove(membro);
  const familia = await familiaRepository.findOne({ where: { idFamilia: membro.familiaId } });
  if (familia) {
    await recalculateIndicadores(familia);
  }

  res.status(204).send();
});

export default router;
