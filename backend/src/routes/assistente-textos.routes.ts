import { Router } from 'express';
import { gerarTextoSugerido } from '../services/assistente-textos.service';

const router = Router();

router.post('/gerar', (req, res) => {
  const texto = gerarTextoSugerido(req.body || {});
  res.json({ texto });
});

export default router;
