import { Router } from 'express';
import { Between } from 'typeorm';
import { AppDataSource } from '../data-source';
import { Beneficiario } from '../entities/Beneficiario';
import { CursoAtendimento, EnrollmentRecord } from '../entities/CursoAtendimento';
import { IndiceVulnerabilidadeFamiliar } from '../entities/IndiceVulnerabilidadeFamiliar';

const router = Router();

function last12Months(): { start: Date; end: Date } {
  const end = new Date();
  const start = new Date();
  start.setMonth(start.getMonth() - 11, 1);
  start.setHours(0, 0, 0, 0);
  return { start, end };
}

router.get('/beneficiarios-status', async (_req, res) => {
  const repo = AppDataSource.getRepository(Beneficiario);
  const [ativos, inativos] = await Promise.all([
    repo.count({ where: { status: 'ATIVO' } }),
    repo.count({ where: { status: 'INATIVO' } })
  ]);

  res.json({ labels: ['Ativos', 'Inativos'], values: [ativos, inativos] });
});

router.get('/atendimentos-mes', async (_req, res) => {
  const repo = AppDataSource.getRepository(CursoAtendimento);
  const { start, end } = last12Months();
  const atendimentos = await repo.find({ where: { createdAt: Between(start, end) } });

  const labels: string[] = [];
  const values: number[] = [];

  for (let i = 0; i < 12; i++) {
    const date = new Date(start);
    date.setMonth(start.getMonth() + i);
    const label = `${(date.getMonth() + 1).toString().padStart(2, '0')}/${date.getFullYear()}`;
    labels.push(label);
    const count = atendimentos.filter((item) => item.createdAt.getMonth() === date.getMonth() && item.createdAt.getFullYear() === date.getFullYear()).length;
    values.push(count);
  }

  res.json({ labels, values });
});

router.get('/atendimentos-tipo', async (_req, res) => {
  const repo = AppDataSource.getRepository(CursoAtendimento);
  const items = await repo.find();
  const groups = items.reduce<Record<string, number>>((acc, item) => {
    acc[item.tipo] = (acc[item.tipo] ?? 0) + 1;
    return acc;
  }, {});

  res.json({ labels: Object.keys(groups), values: Object.values(groups) });
});

router.get('/cursos-metricas', async (_req, res) => {
  const repo = AppDataSource.getRepository(CursoAtendimento);
  const cursos = await repo.find();

  let inscritos = 0;
  let concluintes = 0;
  let cancelados = 0;

  cursos.forEach((curso) => {
    const enrollments = (curso.enrollments ?? []) as EnrollmentRecord[];
    inscritos += enrollments.length;
    concluintes += enrollments.filter((enrollment) => enrollment.status === 'Concluído').length;
    cancelados += enrollments.filter((enrollment) => enrollment.status === 'Cancelado').length;
  });

  const evasao = inscritos ? (cancelados / inscritos) * 100 : 0;

  res.json({ inscritos, concluintes, evasao, ativos: cursos.length });
});

router.get('/ivf-faixas', async (_req, res) => {
  const repo = AppDataSource.getRepository(IndiceVulnerabilidadeFamiliar);
  const registros = await repo.find();
  const grupos = registros.reduce<Record<string, number>>((acc, item) => {
    acc[item.faixaVulnerabilidade] = (acc[item.faixaVulnerabilidade] ?? 0) + 1;
    return acc;
  }, {});

  res.json({ labels: Object.keys(grupos), values: Object.values(grupos) });
});

export default router;
