import 'reflect-metadata';
import express from 'express';
import cors from 'cors';
import dotenv from 'dotenv';
import { AppDataSource } from './data-source';
import authRoutes from './routes/auth.routes';
import { ensureAdminUser } from './utils/bootstrap';
import beneficiariesRoutes from './routes/beneficiaries.routes';
import assistanceUnitsRoutes from './routes/assistance-units.routes';

dotenv.config();

const app = express();
const port = Number(process.env.PORT) || 3000;

app.use(cors());
app.use(express.json());

app.get('/api/health', (_req, res) => {
  res.json({ status: 'ok' });
});

app.use('/api/auth', authRoutes);
app.use('/api/beneficiaries', beneficiariesRoutes);
app.use('/api/assistance-units', assistanceUnitsRoutes);

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
