import { Request, Response, Router } from 'express';
import { ensureBeneficiaryDocumentConfig, getBeneficiaryDocumentRepository } from '../utils/document-config';

const router = Router();

router.get('/beneficiary-documents', async (_req, res) => {
  const documents = await ensureBeneficiaryDocumentConfig();
  res.json({ documents });
});

const saveDocuments = async (req: Request, res: Response) => {
  const repository = await getBeneficiaryDocumentRepository();
  const payload = Array.isArray(req.body?.documents) ? req.body.documents : [];

  if (!payload.length) {
    return res.status(400).json({ message: 'Nenhum documento enviado para atualização' });
  }

  try {
    const existing = await ensureBeneficiaryDocumentConfig();
    const updates = payload.map((doc: any) => ({
      nome: String(doc.nome ?? doc.name),
      obrigatorio: Boolean(doc.obrigatorio ?? doc.required)
    }));

    for (const doc of updates) {
      const current = existing.find((item) => item.nome === doc.nome);

      if (current) {
        await repository.save({ ...current, obrigatorio: doc.obrigatorio });
      } else {
        await repository.save(repository.create(doc));
      }
    }

    const updatedDocuments = await repository.find();
    res.json({ documents: updatedDocuments });
  } catch (error) {
    console.error('Failed to update beneficiary documents config', error);
    res.status(500).json({ message: 'Falha ao salvar configurações' });
  }
};

router.put('/beneficiary-documents', saveDocuments);
router.post('/beneficiary-documents', saveDocuments);

export default router;
