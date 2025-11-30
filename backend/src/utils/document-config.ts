import { In } from 'typeorm';
import { AppDataSource } from '../data-source';
import { ConfiguracaoDocumentoBeneficiario } from '../entities/ConfiguracaoDocumentoBeneficiario';

export const DOCUMENTOS_BENEFICIARIO_PADRAO: Array<Pick<ConfiguracaoDocumentoBeneficiario, 'nome' | 'obrigatorio'>> = [
  { nome: 'RG', obrigatorio: true },
  { nome: 'CPF', obrigatorio: true },
  { nome: 'Comprovante de Endereço', obrigatorio: false },
  { nome: 'Certidão de Nascimento', obrigatorio: false },
  { nome: 'CNH', obrigatorio: false }
];

const DOCUMENTOS_REMOVIDOS = ['Foto 3x4'];

export async function obterRepositorioConfiguracaoDocumentosBeneficiario() {
  return AppDataSource.getRepository(ConfiguracaoDocumentoBeneficiario);
}

export async function garantirConfiguracaoDocumentosBeneficiario() {
  const repository = await obterRepositorioConfiguracaoDocumentosBeneficiario();
  let documents = await repository.find();

  if (documents.length) {
    const toRemove = documents.filter((doc) => DOCUMENTOS_REMOVIDOS.includes(doc.nome)).map((doc) => doc.nome);

    if (toRemove.length) {
      await repository.delete({ nome: In(toRemove) });
      documents = await repository.find();
    }
  }

  const missingDefaults = DOCUMENTOS_BENEFICIARIO_PADRAO.filter((defaultDoc) => !documents.some((doc) => doc.nome === defaultDoc.nome));

  if (!documents.length || missingDefaults.length) {
    const created = missingDefaults.map((doc) => repository.create(doc));
    documents = await repository.save(created);
  }

  return documents;
}
