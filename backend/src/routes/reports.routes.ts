import { Router } from 'express';
import { AppDataSource } from '../data-source';
import { Beneficiario } from '../entities/Beneficiario';
import { renderPdf } from '../services/jsreport.service';
import {
  AuthorizationTermPayload,
  buildAuthorizationTermTemplate
} from '../templates/reports/beneficiarios/authorization-term';
import {
  BeneficiaryListPayload,
  BeneficiaryListRequest,
  buildBeneficiaryListTemplate
} from '../templates/reports/beneficiarios/beneficiary-list';
import {
  BeneficiaryProfilePayload,
  buildBeneficiaryProfileTemplate
} from '../templates/reports/beneficiarios/beneficiary-profile';

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

router.post('/beneficiarios/relacao', async (req, res) => {
  try {
    const filters = (req.body || {}) as BeneficiaryListRequest;
    const repository = AppDataSource.getRepository(Beneficiario);
    const beneficiarios = await repository.find({ order: { nomeCompleto: 'ASC' } });
    const nomeBusca = filters.nome?.toLowerCase();
    const codigoBusca = filters.codigo?.toLowerCase();
    const cpfBusca = filters.cpf ? filters.cpf.replace(/\D/g, '') : undefined;

    const filtered = beneficiarios.filter((beneficiario) => {
      if (nomeBusca) {
        const nomeCompleto = (beneficiario.nomeCompleto ?? '').toLowerCase();
        const nomeSocial = (beneficiario.nomeSocial ?? '').toLowerCase();
        if (!nomeCompleto.includes(nomeBusca) && !nomeSocial.includes(nomeBusca)) {
          return false;
        }
      }

      if (codigoBusca) {
        const codigo = (beneficiario.codigo ?? '').toLowerCase();
        if (!codigo.includes(codigoBusca)) {
          return false;
        }
      }

      if (cpfBusca && (beneficiario.cpf ?? '').replace(/\D/g, '') !== cpfBusca) {
        return false;
      }

      if (filters.status && beneficiario.status !== filters.status) {
        return false;
      }

      if (filters.dataNascimento && beneficiario.dataNascimento !== filters.dataNascimento) {
        return false;
      }

      return true;
    });

    const payloadBeneficiarios = filtered.map((beneficiario) => ({
      idBeneficiario: beneficiario.idBeneficiario,
      codigo: beneficiario.codigo,
      nomeCompleto: beneficiario.nomeCompleto,
      nomeSocial: beneficiario.nomeSocial,
      cpf: beneficiario.cpf,
      nis: beneficiario.nis,
      dataNascimento: beneficiario.dataNascimento,
      bairro: beneficiario.bairro,
      municipio: beneficiario.municipio,
      uf: beneficiario.uf,
      status: beneficiario.status,
      dataCadastro: beneficiario.dataCadastro
    }));

    const payload: BeneficiaryListPayload = {
      issuedAt: new Date().toISOString(),
      filters,
      beneficiarios: payloadBeneficiarios
    };

    const content = buildBeneficiaryListTemplate(payload);
    const pdf = await renderPdf({ content, data: {} });

    res.setHeader('Content-Type', 'application/pdf');
    res.setHeader('Content-Disposition', 'inline; filename="relacao-beneficiarios.pdf"');
    res.send(pdf);
  } catch (error) {
    console.error('Failed to generate beneficiary list', error);
    res.status(500).json({ message: 'Não foi possível gerar a relação de beneficiários.' });
  }
});

router.post('/beneficiarios/ficha', async (req, res) => {
  try {
    const { beneficiarioId } = req.body as { beneficiarioId?: string };

    if (!beneficiarioId) {
      return res.status(400).json({ message: 'Informe o beneficiário para gerar a ficha.' });
    }

    const repository = AppDataSource.getRepository(Beneficiario);
    const beneficiario = await repository.findOne({ where: { idBeneficiario: beneficiarioId } });

    if (!beneficiario) {
      return res.status(404).json({ message: 'Beneficiário não encontrado para geração da ficha.' });
    }

    const payload: BeneficiaryProfilePayload = {
      issuedAt: new Date().toISOString(),
      beneficiario
    };

    const content = buildBeneficiaryProfileTemplate(payload);
    const pdf = await renderPdf({ content, data: {} });

    res.setHeader('Content-Type', 'application/pdf');
    res.setHeader('Content-Disposition', 'inline; filename="ficha-individual-beneficiario.pdf"');
    res.send(pdf);
  } catch (error) {
    console.error('Failed to generate beneficiary profile', error);
    res.status(500).json({ message: 'Não foi possível gerar a ficha individual do beneficiário.' });
  }
});

export default router;
