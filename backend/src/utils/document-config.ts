import { In } from 'typeorm';
import { AppDataSource } from '../data-source';
import { BeneficiaryDocumentConfig } from '../entities/BeneficiaryDocumentConfig';

export const DEFAULT_BENEFICIARY_DOCUMENTS: Array<Pick<BeneficiaryDocumentConfig, 'name' | 'required'>> = [
  { name: 'RG', required: true },
  { name: 'CPF', required: true },
  { name: 'Comprovante de Endereço', required: false },
  { name: 'Certidão de Nascimento', required: false },
  { name: 'CNH', required: false }
];

const REMOVED_DOCUMENTS = ['Foto 3x4'];

export async function getBeneficiaryDocumentRepository() {
  return AppDataSource.getRepository(BeneficiaryDocumentConfig);
}

export async function ensureBeneficiaryDocumentConfig() {
  const repository = await getBeneficiaryDocumentRepository();
  let documents = await repository.find();

  if (documents.length) {
    const toRemove = documents.filter((doc) => REMOVED_DOCUMENTS.includes(doc.name)).map((doc) => doc.name);

    if (toRemove.length) {
      await repository.delete({ name: In(toRemove) });
      documents = await repository.find();
    }
  }

  const missingDefaults = DEFAULT_BENEFICIARY_DOCUMENTS.filter(
    (defaultDoc) => !documents.some((doc) => doc.name === defaultDoc.name)
  );

  if (!documents.length || missingDefaults.length) {
    const created = missingDefaults.map((doc) => repository.create(doc));
    documents = await repository.save(created);
  }

  return documents;
}
