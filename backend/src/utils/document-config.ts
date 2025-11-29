import { In } from 'typeorm';
import { AppDataSource } from '../data-source';
import { BeneficiaryDocumentConfig } from '../entities/BeneficiaryDocumentConfig';

export const DEFAULT_BENEFICIARY_DOCUMENTS: Array<Pick<BeneficiaryDocumentConfig, 'nome' | 'obrigatorio'>> = [
  { nome: 'RG', obrigatorio: true },
  { nome: 'CPF', obrigatorio: true },
  { nome: 'Comprovante de Endereço', obrigatorio: false },
  { nome: 'Certidão de Nascimento', obrigatorio: false },
  { nome: 'CNH', obrigatorio: false }
];

const REMOVED_DOCUMENTS = ['Foto 3x4'];

export async function getBeneficiaryDocumentRepository() {
  return AppDataSource.getRepository(BeneficiaryDocumentConfig);
}

export async function ensureBeneficiaryDocumentConfig() {
  const repository = await getBeneficiaryDocumentRepository();
  let documents = await repository.find();

  if (documents.length) {
    const toRemove = documents.filter((doc) => REMOVED_DOCUMENTS.includes(doc.nome)).map((doc) => doc.nome);

    if (toRemove.length) {
      await repository.delete({ nome: In(toRemove) });
      documents = await repository.find();
    }
  }

  const missingDefaults = DEFAULT_BENEFICIARY_DOCUMENTS.filter(
    (defaultDoc) => !documents.some((doc) => doc.nome === defaultDoc.nome)
  );

  if (!documents.length || missingDefaults.length) {
    const created = missingDefaults.map((doc) => repository.create(doc));
    documents = await repository.save(created);
  }

  return documents;
}
