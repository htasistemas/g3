import bwipjs from 'bwip-js';
import { Repository } from 'typeorm';
import { Beneficiario } from '../entities/Beneficiario';

async function loadExistingNumericCodes(repository: Repository<Beneficiario>): Promise<number[]> {
  const results = await repository
    .createQueryBuilder('beneficiario')
    .select(['beneficiario.codigo'])
    .where('beneficiario.codigo IS NOT NULL')
    .getMany();

  return results
    .map((item) => item.codigo)
    .filter((codigo): codigo is string => typeof codigo === 'string')
    .filter((codigo) => /^\d{5}$/.test(codigo))
    .map((codigo) => Number.parseInt(codigo, 10))
    .filter((codigo) => Number.isFinite(codigo));
}

export async function generateSequentialCodigo(
  repository: Repository<Beneficiario>
): Promise<string> {
  const numericCodes = await loadExistingNumericCodes(repository);
  const lastCodigo = numericCodes.length ? Math.max(...numericCodes) : 0;
  const nextCodigo = Math.min(lastCodigo + 1, 99999);

  return nextCodigo.toString().padStart(5, '0');
}

export async function generateCodigoBarras(
  codigo?: string | null
): Promise<string | undefined> {
  if (!codigo) return undefined;

  const png = await bwipjs.toBuffer({
    bcid: 'code128',
    text: codigo,
    scale: 3,
    height: 10,
    includetext: true,
    textxalign: 'center'
  });

  return `data:image/png;base64,${png.toString('base64')}`;
}
