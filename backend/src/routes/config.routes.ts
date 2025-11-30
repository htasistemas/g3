import { Request, Response, Router } from 'express';
import {
  garantirConfiguracaoDocumentosBeneficiario,
  obterRepositorioConfiguracaoDocumentosBeneficiario
} from '../utils/document-config';

const router = Router();

router.get('/documentos-beneficiario', async (_req, res) => {
  const documents = await garantirConfiguracaoDocumentosBeneficiario();
  res.json({ documentos: documents });
});

const saveDocuments = async (req: Request, res: Response) => {
  const repository = await obterRepositorioConfiguracaoDocumentosBeneficiario();
  const payload = Array.isArray(req.body?.documentos) ? req.body.documentos : [];

  if (!payload.length) {
    return res.status(400).json({ message: 'Nenhum documento enviado para atualização' });
  }

  try {
    const existing = await garantirConfiguracaoDocumentosBeneficiario();
    const updates = payload.map((doc: any) => ({ nome: String(doc.nome), obrigatorio: Boolean(doc.obrigatorio) }));

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

router.put('/documentos-beneficiario', saveDocuments);
router.post('/documentos-beneficiario', saveDocuments);

export default router;
