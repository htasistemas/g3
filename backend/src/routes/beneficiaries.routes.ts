import { Request, Router } from 'express';
import multer from 'multer';
import { AppDataSource } from '../data-source';
import { Beneficiary } from '../entities/Beneficiary';
import { ensureBeneficiaryDocumentConfig } from '../utils/document-config';

const upload = multer({ storage: multer.memoryStorage() });

function normalizeBoolean(value: unknown): boolean {
  if (typeof value === 'boolean') {
    return value;
  }

  if (typeof value === 'string') {
    const normalized = value.toLowerCase();

    if (['false', '0', 'off', 'no', ''].includes(normalized)) {
      return false;
    }

    return ['true', '1', 'on', 'yes'].includes(normalized);
  }

  return Boolean(value);
}

function normalizeNumber(value: unknown): number | undefined {
  if (value === undefined || value === null || value === '') {
    return undefined;
  }

  const parsed = Number(value);
  return Number.isNaN(parsed) ? undefined : parsed;
}

function parseDocumentsAnexos(value: unknown): Array<{ nome: string; nomeArquivo?: string }> {
  if (Array.isArray(value)) {
    return value as Array<{ nome: string; nomeArquivo?: string }>;
  }

  if (typeof value === 'string' && value.trim()) {
    try {
      const parsed = JSON.parse(value);
      return Array.isArray(parsed) ? parsed : [];
    } catch (error) {
      console.error('Failed to parse documentosAnexos', error);
    }
  }

  return [];
}

function buildBeneficiaryPayload(req: Request, existing?: Beneficiary): Beneficiary {
  const body = req.body as Record<string, unknown>;
  const documentosAnexos = parseDocumentsAnexos(body.documentosAnexos);
  const fotoFromFile = req.file
    ? `data:${req.file.mimetype};base64,${req.file.buffer.toString('base64')}`
    : undefined;
  const fotoFromBody = typeof body.foto === 'string' && body.foto.trim() ? (body.foto as string) : undefined;

  const payload: Beneficiary = {
    ...existing,
    cep: String(body.cep ?? existing?.cep ?? ''),
    nomeCompleto: String(body.nomeCompleto ?? existing?.nomeCompleto ?? ''),
    nomeMae: (body.nomeMae as string) ?? existing?.nomeMae,
    documentos: String(body.documentos ?? existing?.documentos ?? ''),
    dataNascimento: String(body.dataNascimento ?? existing?.dataNascimento ?? ''),
    idade: normalizeNumber(body.idade) ?? existing?.idade,
    telefone: String(body.telefone ?? existing?.telefone ?? ''),
    email: String(body.email ?? existing?.email ?? ''),
    endereco: String(body.endereco ?? existing?.endereco ?? ''),
    numeroEndereco: (body.numeroEndereco as string) ?? existing?.numeroEndereco,
    pontoReferencia: (body.pontoReferencia as string) ?? existing?.pontoReferencia,
    bairro: (body.bairro as string) ?? existing?.bairro,
    cidade: (body.cidade as string) ?? existing?.cidade,
    estado: (body.estado as string) ?? existing?.estado,
    observacoes: (body.observacoes as string) ?? existing?.observacoes,
    status: String(body.status ?? existing?.status ?? 'Ativo'),
    motivoBloqueio: (body.motivoBloqueio as string) ?? existing?.motivoBloqueio,
    possuiFilhosMenores: normalizeBoolean(body.possuiFilhosMenores ?? existing?.possuiFilhosMenores),
    possuiCnh: normalizeBoolean(body.possuiCnh ?? existing?.possuiCnh),
    quantidadeFilhosMenores: normalizeNumber(body.quantidadeFilhosMenores) ?? existing?.quantidadeFilhosMenores,
    escolaridade: (body.escolaridade as string) ?? existing?.escolaridade,
    rendaIndividual: normalizeNumber(body.rendaIndividual) ?? existing?.rendaIndividual,
    rendaFamiliar: normalizeNumber(body.rendaFamiliar) ?? existing?.rendaFamiliar,
    informacoesMoradia: (body.informacoesMoradia as string) ?? existing?.informacoesMoradia,
    condicoesSaneamento: (body.condicoesSaneamento as string) ?? existing?.condicoesSaneamento,
    situacaoEmprego: (body.situacaoEmprego as string) ?? existing?.situacaoEmprego,
    ocupacao: (body.ocupacao as string) ?? existing?.ocupacao,
    documentosAnexos,
    foto: fotoFromFile ?? fotoFromBody ?? existing?.foto ?? null
  } as Beneficiary;

  return payload;
}

const router = Router();

router.get('/documents', async (_req, res) => {
  const documents = await ensureBeneficiaryDocumentConfig();
  res.json({ documents });
});

router.get('/', async (_req, res) => {
  const repository = AppDataSource.getRepository(Beneficiary);
  const beneficiaries = await repository.find({ order: { criadoEm: 'DESC' } });
  res.json({ beneficiarios: beneficiaries });
});

router.get('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(Beneficiary);
  const id = Number(req.params.id);
  const beneficiary = await repository.findOne({ where: { id } });

  if (!beneficiary) {
    return res.status(404).json({ message: 'Beneficiário não encontrado' });
  }

  res.json({ beneficiario: beneficiary });
});

router.post('/', upload.single('fotoArquivo'), async (req, res) => {
  const repository = AppDataSource.getRepository(Beneficiary);
  const beneficiary = repository.create(buildBeneficiaryPayload(req));
  const configuredDocs = await ensureBeneficiaryDocumentConfig();
  const requiredDocs = configuredDocs.filter((doc) => doc.obrigatorio).map((doc) => doc.nome);
  const submittedDocs = parseDocumentsAnexos(req.body.documentosAnexos);
  const hasMinorChildren = normalizeBoolean(req.body.possuiFilhosMenores);
  const hasDriverLicense = normalizeBoolean(req.body.possuiCnh);

  if (hasMinorChildren && !requiredDocs.includes('Certidão de Nascimento')) {
    requiredDocs.push('Certidão de Nascimento');
  }
  if (hasDriverLicense && !requiredDocs.includes('CNH')) {
    requiredDocs.push('CNH');
  }
  const missingRequired = requiredDocs.filter(
    (docName) => !submittedDocs.some((doc) => doc.nome === docName && doc.nomeArquivo)
  );

  if (missingRequired.length) {
    return res.status(400).json({
      message: 'Documentos obrigatórios ausentes',
      missingDocuments: missingRequired
    });
  }

  try {
    beneficiary.documentosAnexos = submittedDocs;
    beneficiary.possuiFilhosMenores = hasMinorChildren;
    beneficiary.possuiCnh = hasDriverLicense;
    const saved = await repository.save(beneficiary);
    res.status(201).json(saved);
  } catch (error) {
    console.error('Failed to save beneficiary', error);
    res.status(500).json({ message: 'Falha ao salvar beneficiário' });
  }
});

router.put('/:id', upload.single('fotoArquivo'), async (req, res) => {
  const repository = AppDataSource.getRepository(Beneficiary);
  const id = Number(req.params.id);
  const existing = await repository.findOne({ where: { id } });
  const configuredDocs = await ensureBeneficiaryDocumentConfig();
  const requiredDocs = configuredDocs.filter((doc) => doc.obrigatorio).map((doc) => doc.nome);
  const submittedDocs = parseDocumentsAnexos(req.body.documentosAnexos);
  const hasMinorChildren = normalizeBoolean(req.body.possuiFilhosMenores);
  const hasDriverLicense = normalizeBoolean(req.body.possuiCnh);

  if (hasMinorChildren && !requiredDocs.includes('Certidão de Nascimento')) {
    requiredDocs.push('Certidão de Nascimento');
  }
  if (hasDriverLicense && !requiredDocs.includes('CNH')) {
    requiredDocs.push('CNH');
  }
  const missingRequired = requiredDocs.filter(
    (docName) => !submittedDocs.some((doc) => doc.nome === docName && doc.nomeArquivo)
  );

  if (!existing) {
    return res.status(404).json({ message: 'Beneficiário não encontrado' });
  }

  if (missingRequired.length) {
    return res.status(400).json({
      message: 'Documentos obrigatórios ausentes',
      missingDocuments: missingRequired
    });
  }

  const payload = buildBeneficiaryPayload(req, existing);
  repository.merge(existing, payload);
  existing.documentosAnexos = submittedDocs;
  existing.possuiFilhosMenores = hasMinorChildren;
  existing.possuiCnh = hasDriverLicense;

  try {
    const saved = await repository.save(existing);
    res.json(saved);
  } catch (error) {
    console.error('Failed to update beneficiary', error);
    res.status(500).json({ message: 'Falha ao atualizar beneficiário' });
  }
});

export default router;
