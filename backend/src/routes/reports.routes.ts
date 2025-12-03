import { Router } from 'express';
import { renderPdf } from '../services/jsreport.service';
import { AuthorizationTermPayload, buildAuthorizationTermTemplate } from '../templates/authorization-term';

const router = Router();

router.post('/authorization-term', async (req, res) => {
  try {
    const payload = req.body as AuthorizationTermPayload;
    const content = buildAuthorizationTermTemplate(payload);
    const pdf = await renderPdf({ content, data: {} });

    res.setHeader('Content-Type', 'application/pdf');
    res.setHeader('Content-Disposition', 'inline; filename="termo-autorizacao.pdf"');
    res.send(pdf);
  } catch (error) {
    console.error('Failed to generate authorization term', error);
    res.status(500).json({ message: 'Não foi possível gerar o termo de autorização via jsreport.' });
  }
});

export default router;
