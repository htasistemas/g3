import { Router } from 'express';
import { AppDataSource } from '../data-source';
import { Patrimonio } from '../entities/Patrimonio';
import { MovimentoTipo, PatrimonioMovimento } from '../entities/PatrimonioMovimento';

const router = Router();

function buildPayload(body: any, existing?: Patrimonio): Patrimonio {
  return {
    ...existing,
    numeroPatrimonio: body.numeroPatrimonio ?? body.numero_patrimonio ?? existing?.numeroPatrimonio,
    nome: body.nome ?? existing?.nome ?? '',
    categoria: body.categoria ?? existing?.categoria,
    subcategoria: body.subcategoria ?? existing?.subcategoria,
    conservacao: body.conservacao ?? existing?.conservacao,
    status: body.status ?? existing?.status,
    dataAquisicao: body.dataAquisicao ?? body.data_aquisicao ?? existing?.dataAquisicao,
    valorAquisicao: normalizeNumber(body.valorAquisicao ?? body.valor_aquisicao, existing?.valorAquisicao),
    origem: body.origem ?? existing?.origem,
    responsavel: body.responsavel ?? existing?.responsavel,
    unidade: body.unidade ?? existing?.unidade,
    taxaDepreciacao: normalizeNumber(body.taxaDepreciacao ?? body.taxa_depreciacao, existing?.taxaDepreciacao),
    observacoes: body.observacoes ?? existing?.observacoes
  } as Patrimonio;
}

function normalizeNumber(value: unknown, fallback?: string): string | undefined {
  if (value === undefined || value === null || value === '') return fallback;
  const num = Number(value);
  if (Number.isNaN(num)) return fallback;
  return num.toFixed(2);
}

router.get('/', async (_req, res) => {
  const repository = AppDataSource.getRepository(Patrimonio);
  const patrimonios = await repository.find({
    relations: ['movimentos'],
    order: { createdAt: 'DESC' }
  });

  res.json({ patrimonios });
});

router.post('/', async (req, res) => {
  const repository = AppDataSource.getRepository(Patrimonio);
  const payload = buildPayload(req.body);

  if (!payload.nome || !payload.numeroPatrimonio) {
    return res.status(400).json({ message: 'Número do patrimônio e nome são obrigatórios' });
  }

  try {
    const entity = repository.create(payload);
    const saved = await repository.save(entity);
    res.status(201).json({ patrimonio: saved });
  } catch (error) {
    console.error('Erro ao salvar patrimônio', error);
    res.status(500).json({ message: 'Não foi possível salvar o patrimônio' });
  }
});

router.post('/:id/movimentos', async (req, res) => {
  const patrimonioRepository = AppDataSource.getRepository(Patrimonio);
  const movimentoRepository = AppDataSource.getRepository(PatrimonioMovimento);

  const patrimonio = await patrimonioRepository.findOne({
    where: { idPatrimonio: req.params.id },
    relations: ['movimentos']
  });

  if (!patrimonio) {
    return res.status(404).json({ message: 'Patrimônio não encontrado' });
  }

  const tipo = (req.body.tipo ?? '').toUpperCase() as MovimentoTipo;
  if (!['MOVIMENTACAO', 'MANUTENCAO', 'BAIXA'].includes(tipo)) {
    return res.status(400).json({ message: 'Tipo de movimentação inválido' });
  }

  const movimento = movimentoRepository.create({
    tipo,
    destino: req.body.destino,
    responsavel: req.body.responsavel,
    observacao: req.body.observacao,
    patrimonio
  });

  if (tipo === 'MANUTENCAO') {
    patrimonio.status = 'Em manutenção';
  }
  if (tipo === 'BAIXA') {
    patrimonio.status = 'Baixado / Inativo';
  }
  if (req.body.responsavel) {
    patrimonio.responsavel = req.body.responsavel;
  }
  if (req.body.destino) {
    patrimonio.unidade = req.body.destino;
  }

  try {
    await movimentoRepository.save(movimento);
    await patrimonioRepository.save(patrimonio);
    const refreshed = await patrimonioRepository.findOne({
      where: { idPatrimonio: patrimonio.idPatrimonio },
      relations: ['movimentos'],
      order: { movimentos: { dataMovimento: 'DESC' } }
    });
    res.status(201).json({ patrimonio: refreshed });
  } catch (error) {
    console.error('Erro ao registrar movimentação', error);
    res.status(500).json({ message: 'Não foi possível registrar a movimentação' });
  }
});

export default router;
