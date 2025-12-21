import { Router } from 'express';
import { AppDataSource } from '../data-source';
import { StockItem, StockItemStatus } from '../entities/StockItem';
import { StockMovement, StockMovementType } from '../entities/StockMovement';
import type { RepositoryLike } from '../storage/types';

const router = Router();

function normalizeInteger(value: unknown, fallback = 0): number {
  const parsed = Number(value);
  if (Number.isNaN(parsed)) return fallback;
  return Math.max(0, Math.round(parsed));
}

function normalizeDecimal(value: unknown, fallback = 0): number {
  const parsed = Number(value);
  if (Number.isNaN(parsed)) return fallback;
  return Number(parsed.toFixed(2));
}

function buildItemPayload(body: any, existing?: StockItem): StockItem {
  return {
    ...existing,
    code: body.code ?? existing?.code ?? '',
    description: body.description ?? existing?.description ?? '',
    category: body.category ?? existing?.category,
    unit: body.unit ?? existing?.unit,
    location: body.location ?? existing?.location,
    locationDetail: body.locationDetail ?? existing?.locationDetail,
    currentStock: normalizeInteger(body.currentStock ?? body.current_stock, existing?.currentStock ?? 0),
    minStock: normalizeInteger(body.minStock ?? body.min_stock, existing?.minStock ?? 0),
    unitValue: normalizeDecimal(body.unitValue ?? body.unit_value, existing?.unitValue ?? 0),
    status: (body.status ?? existing?.status ?? 'Ativo') as StockItemStatus,
    notes: body.notes ?? existing?.notes
  } as StockItem;
}

function extractNumericCode(code?: string | null): number {
  if (!code) return 0;
  const match = code.match(/\d+/g);
  if (!match) return 0;
  return match.reduce((max, part) => Math.max(max, Number(part) || 0), 0);
}

async function generateNextItemCode(repository: RepositoryLike<StockItem>): Promise<string> {
  const items = await repository.find({ select: ['code'] });
  const currentMax = items.reduce((max, item) => Math.max(max, extractNumericCode(item.code)), 0);
  const nextValue = currentMax + 1;
  return String(nextValue).padStart(5, '0');
}

router.get('/items', async (_req, res) => {
  const repository = AppDataSource.getRepository(StockItem);
  const items = await repository.find({ order: { updatedAt: 'DESC' } });
  res.json({ items });
});

router.get('/items/next-code', async (_req, res) => {
  const repository = AppDataSource.getRepository(StockItem);
  const code = await generateNextItemCode(repository);
  res.json({ code });
});

router.get('/movements', async (_req, res) => {
  const repository = AppDataSource.getRepository(StockMovement);
  const movements = await repository.find({ order: { date: 'DESC', createdAt: 'DESC' } });
  res.json({ movements });
});

router.post('/items', async (req, res) => {
  const repository = AppDataSource.getRepository(StockItem);
  const payload = buildItemPayload(req.body);
  payload.code = await generateNextItemCode(repository);

  if (!payload.code || !payload.description || !payload.category || !payload.unit || payload.minStock === undefined) {
    return res.status(400).json({ message: 'Campos obrigatórios não preenchidos.' });
  }

  try {
    const entity = repository.create(payload);
    const saved = await repository.save(entity);
    res.status(201).json({ item: saved });
  } catch (error) {
    console.error('Erro ao salvar item de estoque', error);
    res.status(500).json({ message: 'Não foi possível salvar o item de estoque.' });
  }
});

router.put('/items/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(StockItem);
  const existing = await repository.findOne({ where: { id: req.params.id } });

  if (!existing) {
    return res.status(404).json({ message: 'Item não encontrado.' });
  }

  const payload = buildItemPayload(req.body, existing);

  if (!payload.code || !payload.description || !payload.category || !payload.unit || payload.minStock === undefined) {
    return res.status(400).json({ message: 'Campos obrigatórios não preenchidos.' });
  }

  try {
    const existingCode = await repository.findOne({ where: { code: payload.code } });
    if (existingCode && existingCode.id !== existing.id) {
      return res.status(400).json({ message: 'Já existe um item cadastrado com este código.' });
    }

    const saved = await repository.save(payload);
    res.json({ item: saved });
  } catch (error) {
    console.error('Erro ao atualizar item de estoque', error);
    res.status(500).json({ message: 'Não foi possível atualizar o item de estoque.' });
  }
});

router.post('/movements', async (req, res) => {
  const { date, type, itemCode, quantity, reference, responsible, notes, adjustmentDirection } = req.body;

  const normalizedType = (type ?? '') as StockMovementType;
  const normalizedQuantity = Number(quantity);

  if (!['Entrada', 'Saída', 'Ajuste'].includes(normalizedType)) {
    return res.status(400).json({ message: 'Tipo de movimentação inválido.' });
  }

  if (!itemCode || Number.isNaN(normalizedQuantity) || normalizedQuantity <= 0) {
    return res.status(400).json({ message: 'Informe o item e uma quantidade válida para movimentação.' });
  }

  const itemRepository = AppDataSource.getRepository(StockItem);
  const movementRepository = AppDataSource.getRepository(StockMovement);

  const item = await itemRepository.findOne({ where: { code: itemCode } });

  if (!item) {
    return res.status(404).json({ message: 'Item não encontrado para movimentação.' });
  }

  let quantityChange = normalizedQuantity;

  if (normalizedType === 'Saída') {
    if (normalizedQuantity > item.currentStock) {
      return res.status(400).json({ message: 'Não é possível registrar saída maior que o estoque atual.' });
    }
    quantityChange = -normalizedQuantity;
  }

  if (normalizedType === 'Ajuste') {
    const direction = adjustmentDirection === 'decrease' ? -1 : 1;
    quantityChange = normalizedQuantity * direction;
    if (item.currentStock + quantityChange < 0) {
      return res.status(400).json({ message: 'Ajuste resultaria em estoque negativo.' });
    }
  }

  const newBalance = item.currentStock + quantityChange;
  item.currentStock = newBalance;

  const movement = movementRepository.create({
    date: date || new Date().toISOString().substring(0, 10),
    type: normalizedType,
    quantity: normalizedQuantity,
    balanceAfter: newBalance,
    reference,
    responsible,
    notes,
    itemCode: item.code,
    itemDescription: item.description,
    item
  });

  try {
    const savedMovement = await movementRepository.save(movement);
    const savedItem = await itemRepository.save(item);
    res.status(201).json({ movement: savedMovement, item: savedItem });
  } catch (error) {
    console.error('Erro ao registrar movimentação de estoque', error);
    res.status(500).json({ message: 'Não foi possível registrar a movimentação.' });
  }
});

export default router;
