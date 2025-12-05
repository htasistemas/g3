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
    const qb = repository
      .createQueryBuilder('beneficiario')
      .select([
        'beneficiario.idBeneficiario',
        'beneficiario.codigo',
        'beneficiario.nomeCompleto',
        'beneficiario.nomeSocial',
        'beneficiario.cpf',
        'beneficiario.nis',
        'beneficiario.dataNascimento',
        'beneficiario.bairro',
        'beneficiario.municipio',
        'beneficiario.uf',
        'beneficiario.status',
        'beneficiario.dataCadastro'
      ]);

    if (filters.nome) {
      qb.andWhere(
        '(LOWER(beneficiario.nome_completo) LIKE LOWER(:nome) OR LOWER(beneficiario.nome_social) LIKE LOWER(:nome))',
        { nome: `%${filters.nome}%` }
      );
    }

    if (filters.codigo) {
      qb.andWhere('LOWER(beneficiario.codigo) LIKE LOWER(:codigo)', { codigo: `%${filters.codigo}%` });
    }

    if (filters.cpf) {
      qb.andWhere('beneficiario.cpf = :cpf', { cpf: filters.cpf.replace(/\D/g, '') });
    }

    if (filters.status) {
      qb.andWhere('beneficiario.status = :status', { status: filters.status });
    }

    if (filters.dataNascimento) {
      qb.andWhere('beneficiario.data_nascimento = :dataNascimento', { dataNascimento: filters.dataNascimento });
    }

    qb.orderBy('beneficiario.nome_completo', 'ASC');

    const beneficiarios = await qb.getMany();

    const payload: BeneficiaryListPayload = {
      issuedAt: new Date().toISOString(),
      filters,
      beneficiarios
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
