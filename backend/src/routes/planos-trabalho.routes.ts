import { Router } from 'express';
import { AppDataSource } from '../data-source';
import { PlanoTrabalho } from '../entities/PlanoTrabalho';
import { TermoFomento } from '../entities/TermoFomento';
import { PlanoMeta } from '../entities/PlanoMeta';
import { PlanoAtividade } from '../entities/PlanoAtividade';
import { PlanoEtapa } from '../entities/PlanoEtapa';
import { PlanoCronogramaItem } from '../entities/PlanoCronogramaItem';
import { PlanoEquipe } from '../entities/PlanoEquipe';

const router = Router();

router.get('/', async (_req, res) => {
  const repository = AppDataSource.getRepository(PlanoTrabalho);
  const planos = await repository.find({ order: { createdAt: 'DESC' } });
  res.json({ planos });
});

router.get('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(PlanoTrabalho);
  const plano = await repository.findOne({ where: { id: req.params.id } });
  if (!plano) return res.status(404).json({ message: 'Plano de trabalho não encontrado' });
  res.json({ plano });
});

router.get('/:id/export', async (req, res) => {
  const repository = AppDataSource.getRepository(PlanoTrabalho);
  const plano = await repository.findOne({ where: { id: req.params.id } });
  if (!plano) return res.status(404).json({ message: 'Plano de trabalho não encontrado' });

  const totalPorFonte = (plano.cronograma ?? []).reduce<Record<string, number>>((acc, item) => {
    const fonte = item.fonteRecurso || 'Não informado';
    const valor = item.valorPrevisto ? Number(item.valorPrevisto) : 0;
    acc[fonte] = (acc[fonte] || 0) + valor;
    return acc;
  }, {});

  const totalGlobal = Object.values(totalPorFonte).reduce((sum, value) => sum + value, 0);

  res.json({
    plano,
    resumoFinanceiro: {
      totalPorFonte,
      totalGlobal
    }
  });
});

router.post('/', async (req, res) => {
  try {
    const termo = await findTermoObrigatorio(req.body.termoFomentoId);
    const invalidDates = validarDatas(req.body);
    if (invalidDates) {
      return res.status(400).json({ message: invalidDates });
    }
    const payload = mapPayloadToPlano(req.body, termo);
    if (!payload.titulo || !payload.descricaoGeral) {
      return res.status(400).json({ message: 'Título e descrição são obrigatórios' });
    }

    payload.codigoInterno = payload.codigoInterno || gerarCodigoInterno();

    const repository = AppDataSource.getRepository(PlanoTrabalho);
    const saved = await repository.save(repository.create(payload));
    const plano = await repository.findOne({ where: { id: saved.id } });
    res.status(201).json({ plano });
  } catch (error) {
    if ((error as any)?.status) {
      return res.status((error as any).status).json({ message: (error as any).message });
    }
    console.error('Erro ao salvar plano de trabalho', error);
    res.status(500).json({ message: 'Não foi possível salvar o plano de trabalho' });
  }
});

router.put('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(PlanoTrabalho);
  const existing = await repository.findOne({ where: { id: req.params.id } });
  if (!existing) return res.status(404).json({ message: 'Plano de trabalho não encontrado' });

  try {
    const termo = await findTermoObrigatorio(req.body.termoFomentoId);
    const invalidDates = validarDatas(req.body);
    if (invalidDates) {
      return res.status(400).json({ message: invalidDates });
    }
    const payload = mapPayloadToPlano(req.body, termo);
    payload.codigoInterno = existing.codigoInterno;

    await AppDataSource.manager.transaction(async (manager) => {
      await manager.remove(PlanoCronogramaItem, existing.cronograma ?? []);
      await manager.remove(PlanoEquipe, existing.equipe ?? []);
      const etapas = (existing.metas || []).flatMap((meta) => (meta.atividades || []).flatMap((a) => a.etapas || []));
      const atividades = (existing.metas || []).flatMap((meta) => meta.atividades || []);
      await manager.remove(PlanoEtapa, etapas);
      await manager.remove(PlanoAtividade, atividades);
      await manager.remove(PlanoMeta, existing.metas || []);

      const novoPlano = manager.merge(PlanoTrabalho, existing, payload, {
        id: existing.id,
        codigoInterno: existing.codigoInterno,
        createdAt: existing.createdAt,
        updatedAt: existing.updatedAt
      });
      await manager.save(novoPlano);
    });

    const plano = await repository.findOne({ where: { id: existing.id } });
    res.json({ plano });
  } catch (error) {
    if ((error as any)?.status) {
      return res.status((error as any).status).json({ message: (error as any).message });
    }
    console.error('Erro ao atualizar plano de trabalho', error);
    res.status(500).json({ message: 'Não foi possível atualizar o plano de trabalho' });
  }
});

router.delete('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(PlanoTrabalho);
  const plano = await repository.findOne({ where: { id: req.params.id } });
  if (!plano) return res.status(404).json({ message: 'Plano de trabalho não encontrado' });

  await repository.remove(plano);
  res.status(204).send();
});

function mapPayloadToPlano(body: any, termo: TermoFomento): PlanoTrabalho {
  const metas = Array.isArray(body.metas)
    ? body.metas.map((meta: any) => {
        const atividades = Array.isArray(meta.atividades)
          ? meta.atividades.map((atividade: any) => {
              const etapas = Array.isArray(atividade.etapas)
                ? atividade.etapas.map((etapa: any) => {
                    const etapaEntity = new PlanoEtapa();
                    etapaEntity.id = etapa.id;
                    etapaEntity.descricao = etapa.descricao ?? '';
                    etapaEntity.status = (etapa.status || 'NAO_INICIADA').toUpperCase();
                    etapaEntity.dataInicioPrevista = etapa.dataInicioPrevista;
                    etapaEntity.dataFimPrevista = etapa.dataFimPrevista;
                    etapaEntity.dataConclusao = etapa.dataConclusao;
                    etapaEntity.responsavel = etapa.responsavel;
                    return etapaEntity;
                  })
                : [];

              const atividadeEntity = new PlanoAtividade();
              atividadeEntity.id = atividade.id;
              atividadeEntity.descricao = atividade.descricao ?? '';
              atividadeEntity.justificativa = atividade.justificativa;
              atividadeEntity.publicoAlvo = atividade.publicoAlvo;
              atividadeEntity.localExecucao = atividade.localExecucao;
              atividadeEntity.produtoEsperado = atividade.produtoEsperado;
              atividadeEntity.etapas = etapas;
              return atividadeEntity;
            })
          : [];

        const metaEntity = new PlanoMeta();
        metaEntity.id = meta.id;
        metaEntity.codigo = meta.codigo;
        metaEntity.descricao = meta.descricao ?? '';
        metaEntity.indicador = meta.indicador;
        metaEntity.unidadeMedida = meta.unidadeMedida;
        metaEntity.quantidadePrevista = meta.quantidadePrevista ? Number(meta.quantidadePrevista) : undefined;
        metaEntity.resultadoEsperado = meta.resultadoEsperado;
        metaEntity.atividades = atividades;
        return metaEntity;
      })
    : [];

  const cronograma = Array.isArray(body.cronograma)
    ? body.cronograma.map((item: any) => {
        const cronogramaItem = new PlanoCronogramaItem();
        cronogramaItem.id = item.id;
        cronogramaItem.referenciaTipo = item.referenciaTipo;
        cronogramaItem.referenciaId = item.referenciaId;
        cronogramaItem.referenciaDescricao = item.referenciaDescricao;
        cronogramaItem.competencia = item.competencia ?? '';
        cronogramaItem.descricaoResumida = item.descricaoResumida;
        cronogramaItem.valorPrevisto = item.valorPrevisto !== undefined && item.valorPrevisto !== null ? String(item.valorPrevisto) : undefined;
        cronogramaItem.fonteRecurso = item.fonteRecurso;
        cronogramaItem.naturezaDespesa = item.naturezaDespesa;
        cronogramaItem.observacoes = item.observacoes;
        return cronogramaItem;
      })
    : [];

  const equipe = Array.isArray(body.equipe)
    ? body.equipe.map((membro: any) => {
        const equipeEntity = new PlanoEquipe();
        equipeEntity.id = membro.id;
        equipeEntity.nome = membro.nome ?? '';
        equipeEntity.funcao = membro.funcao;
        equipeEntity.cpf = membro.cpf;
        equipeEntity.cargaHoraria = membro.cargaHoraria;
        equipeEntity.tipoVinculo = membro.tipoVinculo;
        equipeEntity.contato = membro.contato;
        return equipeEntity;
      })
    : [];

  return {
    codigoInterno: body.codigoInterno,
    titulo: body.titulo ?? '',
    descricaoGeral: body.descricaoGeral ?? body.descricao ?? '',
    status: (body.status || 'EM_ELABORACAO').toUpperCase(),
    orgaoConcedente: body.orgaoConcedente,
    orgaoOutroDescricao: body.orgaoOutroDescricao,
    areaPrograma: body.areaPrograma,
    dataElaboracao: body.dataElaboracao,
    dataAprovacao: body.dataAprovacao,
    vigenciaInicio: body.vigenciaInicio,
    vigenciaFim: body.vigenciaFim,
    numeroProcesso: body.numeroProcesso,
    modalidade: body.modalidade,
    observacoesVinculacao: body.observacoesVinculacao,
    termoFomento: termo,
    metas,
    cronograma,
    equipe
  } as PlanoTrabalho;
}

function gerarCodigoInterno(): string {
  const now = new Date();
  return `PT-${now.getFullYear()}${String(now.getMonth() + 1).padStart(2, '0')}${String(now.getDate()).padStart(2, '0')}-${
    now.getHours()
  }${now.getMinutes()}${now.getSeconds()}`;
}

async function findTermoObrigatorio(termoId?: string): Promise<TermoFomento> {
  if (!termoId) {
    const error = new Error('O plano precisa estar vinculado a um Termo de Fomento');
    (error as any).status = 400;
    throw error;
  }

  const termo = await AppDataSource.getRepository(TermoFomento).findOne({ where: { id: termoId } });
  if (!termo) {
    const error = new Error('Termo de fomento informado não existe');
    (error as any).status = 404;
    throw error;
  }
  return termo;
}

function validarDatas(body: any): string | null {
  if (body.vigenciaInicio && body.vigenciaFim && new Date(body.vigenciaInicio) > new Date(body.vigenciaFim)) {
    return 'A data de início da vigência não pode ser posterior à data de término';
  }

  for (const meta of body.metas ?? []) {
    for (const atividade of meta.atividades ?? []) {
      for (const etapa of atividade.etapas ?? []) {
        if (etapa.dataInicioPrevista && etapa.dataFimPrevista && new Date(etapa.dataInicioPrevista) > new Date(etapa.dataFimPrevista)) {
          return 'Datas de início e fim das etapas precisam ser coerentes';
        }
      }
    }
  }

  return null;
}

export default router;
