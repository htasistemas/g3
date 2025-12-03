import { Router } from 'express';
import { Between } from 'typeorm';
import { AppDataSource } from '../data-source';
import { Beneficiario } from '../entities/Beneficiario';
import { Familia } from '../entities/Familia';
import { FamiliaMembro } from '../entities/FamiliaMembro';
import { TermoFomento } from '../entities/TermoFomento';

const router = Router();

function parseDateParam(value?: string): Date | null {
  if (!value) return null;
  const parsed = new Date(value);
  return Number.isNaN(parsed.getTime()) ? null : parsed;
}

function getAge(dateOfBirth?: string | null): number | null {
  if (!dateOfBirth) return null;
  const dob = new Date(dateOfBirth);
  if (Number.isNaN(dob.getTime())) return null;
  const diff = Date.now() - dob.getTime();
  return Math.floor(diff / (1000 * 60 * 60 * 24 * 365.25));
}

function resolveAgeBand(age: number | null): string {
  if (age === null) return 'Não informado';
  if (age <= 6) return '0-6';
  if (age <= 12) return '7-12';
  if (age <= 17) return '13-17';
  if (age <= 29) return '18-29';
  if (age <= 59) return '30-59';
  return '60+';
}

function sum(values: number[]): number {
  return values.reduce((acc, value) => acc + (Number.isFinite(value) ? value : 0), 0);
}

router.get('/assistencia', async (req, res) => {
  const startDate = parseDateParam(req.query.startDate as string);
  const endDate = parseDateParam(req.query.endDate as string);

  const beneficiariesRepo = AppDataSource.getRepository(Beneficiario);
  const familiesRepo = AppDataSource.getRepository(Familia);
  const familyMembersRepo = AppDataSource.getRepository(FamiliaMembro);
  const termosRepo = AppDataSource.getRepository(TermoFomento);

  const dateFilter = startDate && endDate ? Between(startDate, endDate) : undefined;

  const beneficiaries = await beneficiariesRepo.find({
    select: ['idBeneficiario', 'status', 'dataCadastro', 'dataAtualizacao', 'dataNascimento', 'recebeBeneficio', 'situacaoVulnerabilidade']
  });

  const families = await familiesRepo.find({ select: ['idFamilia', 'rendaFamiliarTotal', 'rendaPerCapita', 'faixaRendaPerCapita', 'situacaoInsegurancaAlimentar', 'qtdMembros', 'qtdCriancas'] });

  const familyMembers = await familyMembersRepo.find({ select: ['idFamiliaMembro', 'familiaId', 'parentesco', 'responsavelFamiliar'] });

  const terms = await termosRepo.find({ select: ['id', 'numero', 'valorGlobal', 'vigenciaInicio', 'vigenciaFim', 'status'] });

  const beneficiariesInPeriod = dateFilter
    ? beneficiaries.filter((item) => {
        const createdAt = item.dataCadastro ? new Date(item.dataCadastro) : null;
        if (!createdAt) return false;
        return createdAt >= startDate! && createdAt <= endDate!;
      })
    : beneficiaries;

  const activeBeneficiaries = beneficiaries.filter((item) => item.status === 'ATIVO');
  const blockedBeneficiaries = beneficiaries.filter((item) => item.status === 'BLOQUEADO');
  const pendingBeneficiaries = beneficiaries.filter((item) => item.status?.includes('PEND') || item.status === 'EM_ANALISE');
  const outdatedBeneficiaries = beneficiaries.filter((item) => item.status === 'DESATUALIZADO' || item.status === 'INCOMPLETO');

  const ageBands = beneficiaries.reduce<Record<string, number>>((acc, item) => {
    const band = resolveAgeBand(getAge(item.dataNascimento));
    acc[band] = (acc[band] ?? 0) + 1;
    return acc;
  }, {});

  const vulnerabilities = beneficiaries.reduce<Record<string, number>>((acc, item) => {
    const value = item.situacaoVulnerabilidade?.trim();
    if (!value) return acc;
    const types = value.split(',').map((entry) => entry.trim()).filter(Boolean);
    types.forEach((type) => {
      const key = type || 'Não informado';
      acc[key] = (acc[key] ?? 0) + 1;
    });
    return acc;
  }, {});

  const familyCount = families.length;
  const totalPeople = sum(
    families.map((family) => {
      if (family.qtdMembros && family.qtdMembros > 0) return family.qtdMembros;
      const members = familyMembers.filter((member) => member.familiaId === family.idFamilia);
      return members.length;
    })
  );

  const rendaMedia = families.length > 0 ? sum(families.map((f) => Number(f.rendaFamiliarTotal ?? 0))) / families.length : 0;
  const rendaPerCapitaMedia = families.length > 0 ? sum(families.map((f) => Number(f.rendaPerCapita ?? 0))) / families.length : 0;

  const insecurityDistribution = families.reduce<Record<string, number>>((acc, family) => {
    const key = family.situacaoInsegurancaAlimentar ?? 'NAO_INFORMADO';
    acc[key] = (acc[key] ?? 0) + 1;
    return acc;
  }, {});

  const faixaRendaDistribution = families.reduce<Record<string, number>>((acc, family) => {
    const key = family.faixaRendaPerCapita ?? 'NAO_INFORMADO';
    acc[key] = (acc[key] ?? 0) + 1;
    return acc;
  }, {});

  const today = new Date();
  const activeTerms = terms.filter((term) => {
    const start = term.vigenciaInicio ? new Date(term.vigenciaInicio) : null;
    const end = term.vigenciaFim ? new Date(term.vigenciaFim) : null;
    if (start && start > today) return false;
    if (end && end < today) return false;
    return true;
  });

  const totalValorGlobal = sum(terms.map((term) => Number(term.valorGlobal ?? 0)));

  const alertTerms = terms
    .filter((term) => {
      if (!term.vigenciaFim) return false;
      const end = new Date(term.vigenciaFim);
      const diffDays = (end.getTime() - today.getTime()) / (1000 * 60 * 60 * 24);
      return diffDays >= 0 && diffDays <= 60;
    })
    .map((term) => ({ numero: term.numero, vigenciaFim: term.vigenciaFim, status: term.status }));

  const response = {
    filters: {
      startDate: startDate?.toISOString() ?? null,
      endDate: endDate?.toISOString() ?? null
    },
    top12: {
      beneficiariosAtendidosPeriodo: beneficiariesInPeriod.length,
      familiasExtremaPobreza: faixaRendaDistribution['ATE_1_4_SM'] ?? 0,
      rendaMediaFamiliar: rendaMedia,
      cursosAtivos: 0,
      taxaMediaOcupacaoCursos: 0,
      certificadosEmitidos: 0,
      doacoesPeriodo: 0,
      itensDoadoResumo: {},
      visitasDomiciliares: 0,
      termosVencendo: alertTerms.length,
      execucaoFinanceira: 0,
      absenteismo: 0
    },
    atendimento: {
      totalBeneficiarios: beneficiaries.length,
      ativos: activeBeneficiaries.length,
      pendentes: pendingBeneficiaries.length,
      bloqueados: blockedBeneficiaries.length,
      emAnalise: pendingBeneficiaries.length,
      desatualizados: outdatedBeneficiaries.length,
      cadastroCompletoPercentual: beneficiaries.length
        ? ((beneficiaries.length - outdatedBeneficiaries.length) / beneficiaries.length) * 100
        : 0,
      beneficiariosPeriodo: beneficiariesInPeriod.length,
      novosBeneficiarios: beneficiariesInPeriod.length,
      reincidentes: 0,
      faixaEtaria: ageBands,
      vulnerabilidades: vulnerabilities
    },
    familias: {
      total: familyCount,
      mediaPessoas: familyCount ? totalPeople / familyCount : 0,
      rendaMediaFamiliar: rendaMedia,
      rendaPerCapitaMedia: rendaPerCapitaMedia,
      insegurancaAlimentar: insecurityDistribution,
      faixaRenda: faixaRendaDistribution
    },
    termos: {
      ativos: activeTerms.length,
      valorTotal: totalValorGlobal,
      alertas: alertTerms
    }
  };

  res.json(response);
});

export default router;
