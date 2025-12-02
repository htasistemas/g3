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
import usersRoutes from './routes/users.routes';
import familiasRoutes from './routes/familias.routes';
import patrimoniosRoutes from './routes/patrimonios.routes';

dotenv.config();

const app = express();
const port = Number(process.env.PORT) || 3000;

app.use(cors());
app.use(
  express.json({
    limit: '10mb'
  })
);

app.get('/api/health', (_req, res) => {
  res.json({ status: 'ok' });
});

app.use('/api/auth', authRoutes);
app.use('/api/beneficiarios', beneficiariosRoutes);
app.use('/api/beneficiaries', beneficiariosRoutes);
app.use('/api/assistance-units', assistanceUnitsRoutes);
app.use('/api/config', configRoutes);
app.use('/api/users', usersRoutes);
app.use('/api/familias', familiasRoutes);
app.use('/api/patrimonios', patrimoniosRoutes);

async function start() {
  try {
    await AppDataSource.initialize();
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
