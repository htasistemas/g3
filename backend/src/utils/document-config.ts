import { AppDataSource } from '../data-source';
import { BeneficiaryDocumentConfig } from '../entities/BeneficiaryDocumentConfig';

export const DEFAULT_BENEFICIARY_DOCUMENTS: Array<Pick<BeneficiaryDocumentConfig, 'name' | 'required'>> = [
  { name: 'RG', required: true },
  { name: 'CPF', required: true },
  { name: 'Comprovante de Endereço', required: false },
  { name: 'Certidão de Nascimento', required: false },
  { name: 'Foto 3x4', required: false }
];

export async function getBeneficiaryDocumentRepository() {
  return AppDataSource.getRepository(BeneficiaryDocumentConfig);
}

export async function ensureBeneficiaryDocumentConfig() {
  const repository = await getBeneficiaryDocumentRepository();
  let documents = await repository.find();

  if (!documents.length) {
    const created = DEFAULT_BENEFICIARY_DOCUMENTS.map((doc) => repository.create(doc));
    documents = await repository.save(created);
  }

  return documents;
}
