import 'reflect-metadata';
import express from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import { AppDataSource } from './data-source';
import authRoutes from './routes/auth.routes';
import { ensureAdminUser } from './utils/bootstrap';
import beneficiariosRoutes from './routes/beneficiarios.routes';
import assistanceUnitsRoutes from './routes/assistance-units.routes';
import configRoutes from './routes/config.routes';
import usuariosRoutes from './routes/usuarios.routes';
import familiasRoutes from './routes/familias.routes';
import patrimoniosRoutes from './routes/patrimonios.routes';
import planosTrabalhoRoutes from './routes/planos-trabalho.routes';
import termosFomentoRoutes from './routes/termos-fomento.routes';
import dashboardAssistenciaRoutes from './routes/dashboard-assistencia.routes';
import reportsRoutes from './routes/reports.routes';
import cursosAtendimentosRoutes from './routes/cursos-atendimentos.routes';
import salasRoutes from './routes/salas.routes';
import vulnerabilityIndexRoutes from './routes/vulnerability-index.routes';
import dashboardBiRoutes from './routes/dashboard-bi.routes';
import benefitTypesRoutes from './routes/benefit-types.routes';
import benefitGrantsRoutes from './routes/benefit-grants.routes';
import assistenteTextosRoutes from './routes/assistente-textos.routes';
import prontuarioRoutes from './routes/prontuario.routes';
import almoxarifadoRoutes from './routes/almoxarifado.routes';

dotenv.config();

const app = express();
const port = Number(process.env.PORT) || 3000;

app.use(cors());
app.use(
  express.json({
    limit: '50mb'
  })
);

app.get('/api/health', (_req, res) => {
  res.json({ status: 'ok' });
});

app.get('/api/health/db', async (_req, res) => {
  try {
    if (!AppDataSource.isInitialized) {
      await AppDataSource.initialize();
    }

    await AppDataSource.query('SELECT 1');
    res.json({ status: 'ok' });
  } catch (error) {
    console.error('Database health check failed', error);
    res.status(500).json({ status: 'error', message: 'Database unavailable' });
  }
});

app.use('/api/auth', authRoutes);
app.use('/api/beneficiarios', beneficiariosRoutes);
app.use('/api/beneficiaries', beneficiariosRoutes);
app.use('/api/assistance-units', assistanceUnitsRoutes);
app.use('/api/config', configRoutes);
app.use('/api/usuarios', usuariosRoutes);
app.use('/api/familias', familiasRoutes);
app.use('/api/patrimonios', patrimoniosRoutes);
app.use('/api/planos-trabalho', planosTrabalhoRoutes);
app.use('/api/termos-fomento', termosFomentoRoutes);
app.use('/api/dashboard', dashboardAssistenciaRoutes);
app.use('/api/reports', reportsRoutes);
app.use('/api/cursos-atendimentos', cursosAtendimentosRoutes);
app.use('/api/salas', salasRoutes);
app.use('/api/ivf', vulnerabilityIndexRoutes);
app.use('/api/dashboard/bi', dashboardBiRoutes);
app.use('/api/benefits/types', benefitTypesRoutes);
app.use('/api/benefits/grants', benefitGrantsRoutes);
app.use('/api/assistente-textos', assistenteTextosRoutes);
app.use('/api/prontuario', prontuarioRoutes);
app.use('/api/almoxarifado', almoxarifadoRoutes);

async function start() {
  try {
    await AppDataSource.initialize();
    await AppDataSource.runMigrations();
    await ensureAdminUser();
    app.listen(port, () => {
      console.log(`API server running on port ${port}`);
    });
  } catch (error) {
    console.error('Failed to start server', error);
    process.exit(1);
  }
}

start();
