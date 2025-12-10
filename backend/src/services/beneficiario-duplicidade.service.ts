import { ILike } from 'typeorm';
import { AppDataSource } from '../data-source';
import { Beneficiario } from '../entities/Beneficiario';

export interface DuplicidadePayload {
  nomeCompleto?: string;
  nomeMae?: string;
  dataNascimento?: string;
  cpf?: string;
}

export class BeneficiarioDuplicidadeService {
  private readonly repository = AppDataSource.getRepository(Beneficiario);

  async verificar(
    payload: DuplicidadePayload,
    options: { ignoreIdBeneficiario?: string | null } = {}
  ): Promise<Beneficiario[]> {
    const where = [] as any[];
    const cpf = payload.cpf?.replace(/\D/g, '');

    if (cpf) {
      where.push({ cpf });
    }

    if (payload.nomeCompleto && payload.dataNascimento) {
      where.push({ nomeCompleto: ILike(`%${payload.nomeCompleto}%`), dataNascimento: payload.dataNascimento });
    }

    if (payload.nomeMae && payload.dataNascimento) {
      where.push({ nomeMae: ILike(`%${payload.nomeMae}%`), dataNascimento: payload.dataNascimento });
    }

    if (!where.length) {
      return [];
    }

    const candidates = await this.repository.find({ where, take: 10, order: { nomeCompleto: 'ASC' } });

    return options.ignoreIdBeneficiario
      ? candidates.filter((item) => item.idBeneficiario !== options.ignoreIdBeneficiario)
      : candidates;
  }
}
