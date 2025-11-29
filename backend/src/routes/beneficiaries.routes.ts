import { Router } from 'express';
import { AppDataSource } from '../data-source';
import { Beneficiary } from '../entities/Beneficiary';
import { ensureBeneficiaryDocumentConfig } from '../utils/document-config';

const router = Router();

router.get('/documents', async (_req, res) => {
  const documents = await ensureBeneficiaryDocumentConfig();
  res.json({ documents });
});

router.get('/', async (_req, res) => {
  const repository = AppDataSource.getRepository(Beneficiary);
  const beneficiaries = await repository.find({ order: { createdAt: 'DESC' } });
  res.json({ beneficiaries });
});

router.post('/', async (req, res) => {
  const repository = AppDataSource.getRepository(Beneficiary);
  const beneficiary = repository.create(req.body as Beneficiary);
  const configuredDocs = await ensureBeneficiaryDocumentConfig();
  const requiredDocs = configuredDocs.filter((doc) => doc.required).map((doc) => doc.name);
  const submittedDocs = (req.body.documents || []) as Array<{ name: string; fileName?: string }>;
  const hasMinorChildren = Boolean(req.body.hasMinorChildren);
  const hasDriverLicense = Boolean(req.body.hasDriverLicense);

  if (hasMinorChildren && !requiredDocs.includes('Certidão de Nascimento')) {
    requiredDocs.push('Certidão de Nascimento');
  }
  if (hasDriverLicense && !requiredDocs.includes('CNH')) {
    requiredDocs.push('CNH');
  }
  const missingRequired = requiredDocs.filter(
    (docName) => !submittedDocs.some((doc) => doc.name === docName && doc.fileName)
  );

  if (missingRequired.length) {
    return res.status(400).json({
      message: 'Documentos obrigatórios ausentes',
      missingDocuments: missingRequired
    });
  }

  try {
    beneficiary.documents = submittedDocs;
    const saved = await repository.save(beneficiary);
    res.status(201).json(saved);
  } catch (error) {
    console.error('Failed to save beneficiary', error);
    res.status(500).json({ message: 'Falha ao salvar beneficiário' });
  }
});

router.put('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(Beneficiary);
  const id = Number(req.params.id);
  const existing = await repository.findOne({ where: { id } });
  const configuredDocs = await ensureBeneficiaryDocumentConfig();
  const requiredDocs = configuredDocs.filter((doc) => doc.required).map((doc) => doc.name);
  const submittedDocs = (req.body.documents || []) as Array<{ name: string; fileName?: string }>;
  const hasMinorChildren = Boolean(req.body.hasMinorChildren);
  const hasDriverLicense = Boolean(req.body.hasDriverLicense);

  if (hasMinorChildren && !requiredDocs.includes('Certidão de Nascimento')) {
    requiredDocs.push('Certidão de Nascimento');
  }
  if (hasDriverLicense && !requiredDocs.includes('CNH')) {
    requiredDocs.push('CNH');
  }
  const missingRequired = requiredDocs.filter(
    (docName) => !submittedDocs.some((doc) => doc.name === docName && doc.fileName)
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

  repository.merge(existing, req.body as Beneficiary);
  existing.documents = submittedDocs;

  try {
    const saved = await repository.save(existing);
    res.json(saved);
  } catch (error) {
    console.error('Failed to update beneficiary', error);
    res.status(500).json({ message: 'Falha ao atualizar beneficiário' });
  }
});

export default router;
