import 'dotenv/config';
import { AppDataSource } from '../data-source';
import { Beneficiary } from '../entities/Beneficiary';

async function run() {
  console.log('🔍 Iniciando verificação de banco de dados...');
  await AppDataSource.initialize();
  console.log('✅ DataSource inicializado.');

  await AppDataSource.query('SELECT 1');
  console.log('✅ Conexão testada com SELECT 1.');

  const beneficiaryRepo = AppDataSource.getRepository(Beneficiary);
  const totalBeneficiaries = await beneficiaryRepo.count();
  console.log(`ℹ️ Beneficiários disponíveis: ${totalBeneficiaries}`);

  await AppDataSource.manager.transaction(async (manager) => {
    const repo = manager.getRepository(Beneficiary);
    const timestamp = Date.now();
    const provisional = repo.create({
      nomeCompleto: `Teste Automatizado ${timestamp}`,
      nomeMae: 'Verificação G3',
      documentos: `DOC-${timestamp}`,
      dataNascimento: '1990-01-01',
      telefone: '0000000000',
      email: `healthcheck-${timestamp}@example.com`,
      cep: '00000000',
      endereco: 'Endereço de teste',
      status: 'Ativo',
    });

    const saved = await repo.save(provisional);
    console.log(`✅ Registro de teste inserido com id ${saved.id}.`);

    await repo.delete(saved.id);
    console.log('✅ Registro de teste removido.');
  });

  console.log('🎉 Checklist de banco concluído com sucesso.');
}

run()
  .catch((error) => {
    console.error('❌ Falha na verificação de banco de dados:', error);
    process.exitCode = 1;
  })
  .finally(async () => {
    if (AppDataSource.isInitialized) {
      await AppDataSource.destroy();
    }
  });
