import { Request, Router } from 'express';
import { AppDataSource } from '../data-source';
import { Beneficiario, BeneficiarioStatus } from '../entities/Beneficiario';

const router = Router();

function onlyDigits(value: string): string {
  return value.replace(/\D/g, '');
}

function parseBoolean(value: unknown, fallback = false): boolean {
  if (typeof value === 'boolean') return value;
  if (typeof value === 'string') {
    const normalized = value.toLowerCase();
    if (['true', '1', 'on', 'yes', 'sim'].includes(normalized)) return true;
    if (['false', '0', 'off', 'no', 'nao', 'não'].includes(normalized)) return false;
  }
  return fallback;
}

function parseNumber(value: unknown): string | undefined {
  if (value === null || value === undefined || value === '') return undefined;
  const numeric = Number(value);
  if (Number.isNaN(numeric)) return undefined;
  return numeric.toFixed(2);
}

function sanitizeCpf(cpf?: string | null): string | undefined {
  if (!cpf) return undefined;
  const digits = onlyDigits(cpf);
  return digits.length === 11 ? digits : undefined;
}

function getMissingRequiredDocuments(body: any, documentsFromBody?: any[]): string[] {
  const documents = Array.isArray(documentsFromBody)
    ? documentsFromBody
    : Array.isArray(body?.documentosObrigatorios)
      ? body.documentosObrigatorios
      : [];

  return documents
    .filter((doc) => doc?.obrigatorio && !doc?.nomeArquivo && !doc?.conteudo)
    .map((doc) => doc?.nome)
    .filter(Boolean);
}

function hasPendingData(payload: Beneficiario, missingDocuments: string[]): boolean {
  return !payload.nomeCompleto || !payload.dataNascimento || !payload.nomeMae || missingDocuments.length > 0;
}

function isOutdated(entity?: Beneficiario | null): boolean {
  if (!entity) return false;

  const referenceDate = entity.dataAtualizacao ?? entity.dataCadastro;
  if (!referenceDate) return false;

  const lastUpdate = new Date(referenceDate);
  const oneYearMs = 1000 * 60 * 60 * 24 * 365;

  return Date.now() - lastUpdate.getTime() > oneYearMs;
}

function resolveStatus(
  payload: Beneficiario,
  missingDocuments: string[],
  options: { existing?: Beneficiario; requestedStatus?: BeneficiarioStatus }
): BeneficiarioStatus {
  const hasPending = hasPendingData(payload, missingDocuments);

  if (!options.existing) {
    return hasPending ? 'INCOMPLETO' : 'EM_ANALISE';
  }

  if (hasPending) {
    return 'INCOMPLETO';
  }

  if (isOutdated(options.existing) && options.requestedStatus !== 'BLOQUEADO') {
    return 'DESATUALIZADO';
  }

  return options.requestedStatus ?? options.existing.status ?? 'EM_ANALISE';
}

async function ensureCpfUnique(cpf: string, ignoreId?: string): Promise<boolean> {
  const repository = AppDataSource.getRepository(Beneficiario);
  const existing = await repository.findOne({ where: { cpf } });
  if (!existing) return true;
  if (ignoreId && existing.idBeneficiario === ignoreId) return true;
  return false;
}

function buildBeneficiarioPayload(req: Request, existing?: Beneficiario): Beneficiario {
  const body = req.body as Record<string, any>;
  const cpf = sanitizeCpf(body.cpf ?? existing?.cpf ?? undefined);
  const documentosObrigatorios = Array.isArray(body?.documentosObrigatorios)
    ? body.documentosObrigatorios
    : existing?.documentosObrigatorios
      ? [...existing.documentosObrigatorios]
      : [];
  const missingDocuments = getMissingRequiredDocuments(body, documentosObrigatorios);

  const payload: Beneficiario = {
    ...existing,
    nomeCompleto: body.nome_completo ?? body.nomeCompleto ?? existing?.nomeCompleto ?? '',
    nomeSocial: body.nome_social ?? body.nomeSocial ?? existing?.nomeSocial,
    apelido: body.apelido ?? existing?.apelido,
    dataNascimento: body.data_nascimento ?? body.dataNascimento ?? existing?.dataNascimento ?? '',
    sexoBiologico: body.sexo_biologico ?? existing?.sexoBiologico,
    identidadeGenero: body.identidade_genero ?? existing?.identidadeGenero,
    corRaca: body.cor_raca ?? existing?.corRaca,
    estadoCivil: body.estado_civil ?? existing?.estadoCivil,
    nacionalidade: body.nacionalidade ?? existing?.nacionalidade,
    naturalidadeCidade: body.naturalidade_cidade ?? existing?.naturalidadeCidade,
    naturalidadeUf: body.naturalidade_uf ?? existing?.naturalidadeUf,
    nomeMae: body.nome_mae ?? body.nomeMae ?? existing?.nomeMae ?? '',
    nomePai: body.nome_pai ?? existing?.nomePai,
    cpf: cpf ?? null,
    rgNumero: body.rg_numero ?? existing?.rgNumero,
    rgOrgaoEmissor: body.rg_orgao_emissor ?? existing?.rgOrgaoEmissor,
    rgUf: body.rg_uf ?? existing?.rgUf,
    rgDataEmissao: body.rg_data_emissao ?? existing?.rgDataEmissao,
    nis: body.nis ?? existing?.nis,
    certidaoTipo: body.certidao_tipo ?? existing?.certidaoTipo,
    certidaoLivro: body.certidao_livro ?? existing?.certidaoLivro,
    certidaoFolha: body.certidao_folha ?? existing?.certidaoFolha,
    certidaoTermo: body.certidao_termo ?? existing?.certidaoTermo,
    certidaoCartorio: body.certidao_cartorio ?? existing?.certidaoCartorio,
    certidaoMunicipio: body.certidao_municipio ?? existing?.certidaoMunicipio,
    certidaoUf: body.certidao_uf ?? existing?.certidaoUf,
    tituloEleitor: body.titulo_eleitor ?? existing?.tituloEleitor,
    cnh: body.cnh ?? existing?.cnh,
    cartaoSus: body.cartao_sus ?? existing?.cartaoSus,
    telefonePrincipal: body.telefone_principal ?? existing?.telefonePrincipal,
    telefonePrincipalWhatsapp: parseBoolean(body.telefone_principal_whatsapp, existing?.telefonePrincipalWhatsapp ?? false),
    telefoneSecundario: body.telefone_secundario ?? existing?.telefoneSecundario,
    telefoneRecadoNome: body.telefone_recado_nome ?? existing?.telefoneRecadoNome,
    telefoneRecadoNumero: body.telefone_recado_numero ?? existing?.telefoneRecadoNumero,
    email: body.email ?? existing?.email,
    permiteContatoTel: parseBoolean(body.permite_contato_tel, existing?.permiteContatoTel ?? true),
    permiteContatoWhatsapp: parseBoolean(
      body.permite_contato_whatsapp,
      existing?.permiteContatoWhatsapp ?? true
    ),
    permiteContatoSms: parseBoolean(body.permite_contato_sms, existing?.permiteContatoSms ?? false),
    permiteContatoEmail: parseBoolean(body.permite_contato_email, existing?.permiteContatoEmail ?? false),
    horarioPreferencialContato: body.horario_preferencial_contato ?? existing?.horarioPreferencialContato,
    usaEnderecoFamilia: parseBoolean(body.usa_endereco_familia, existing?.usaEnderecoFamilia ?? true),
    cep: body.cep ?? existing?.cep,
    logradouro: body.logradouro ?? existing?.logradouro,
    numero: body.numero ?? existing?.numero,
    complemento: body.complemento ?? existing?.complemento,
    bairro: body.bairro ?? existing?.bairro,
    pontoReferencia: body.ponto_referencia ?? existing?.pontoReferencia,
    municipio: body.municipio ?? existing?.municipio,
    uf: body.uf ?? existing?.uf,
    zona: body.zona ?? existing?.zona,
    situacaoImovel: body.situacao_imovel ?? existing?.situacaoImovel,
    tipoMoradia: body.tipo_moradia ?? existing?.tipoMoradia,
    aguaEncanada: parseBoolean(body.agua_encanada, existing?.aguaEncanada ?? false),
    esgotoTipo: body.esgoto_tipo ?? existing?.esgotoTipo,
    coletaLixo: body.coleta_lixo ?? existing?.coletaLixo,
    energiaEletrica: parseBoolean(body.energia_eletrica, existing?.energiaEletrica ?? false),
    internet: parseBoolean(body.internet, existing?.internet ?? false),
    moraComFamilia: parseBoolean(body.mora_com_familia, existing?.moraComFamilia ?? false),
    responsavelLegal: parseBoolean(body.responsavel_legal, existing?.responsavelLegal ?? false),
    vinculoFamiliar: body.vinculo_familiar ?? existing?.vinculoFamiliar,
    situacaoVulnerabilidade: body.situacao_vulnerabilidade ?? existing?.situacaoVulnerabilidade,
    sabeLerEscrever: parseBoolean(body.sabe_ler_escrever, existing?.sabeLerEscrever ?? false),
    nivelEscolaridade: body.nivel_escolaridade ?? existing?.nivelEscolaridade,
    estudaAtualmente: parseBoolean(body.estuda_atualmente, existing?.estudaAtualmente ?? false),
    ocupacao: body.ocupacao ?? existing?.ocupacao,
    situacaoTrabalho: body.situacao_trabalho ?? existing?.situacaoTrabalho,
    localTrabalho: body.local_trabalho ?? existing?.localTrabalho,
    rendaMensal: parseNumber(body.renda_mensal) ?? existing?.rendaMensal,
    fonteRenda: body.fonte_renda ?? existing?.fonteRenda,
    possuiDeficiencia: parseBoolean(body.possui_deficiencia, existing?.possuiDeficiencia ?? false),
    tipoDeficiencia: body.tipo_deficiencia ?? existing?.tipoDeficiencia,
    cidPrincipal: body.cid_principal ?? existing?.cidPrincipal,
    usaMedicacaoContinua: parseBoolean(
      body.usa_medicacao_continua,
      existing?.usaMedicacaoContinua ?? false
    ),
    descricaoMedicacao: body.descricao_medicacao ?? existing?.descricaoMedicacao,
    servicoSaudeReferencia: body.servico_saude_referencia ?? existing?.servicoSaudeReferencia,
    recebeBeneficio: parseBoolean(body.recebe_beneficio, existing?.recebeBeneficio ?? false),
    beneficiosDescricao: body.beneficios_descricao ?? existing?.beneficiosDescricao,
    valorTotalBeneficios: parseNumber(body.valor_total_beneficios) ?? existing?.valorTotalBeneficios,
    aceiteLgpd: parseBoolean(body.aceite_lgpd, existing?.aceiteLgpd ?? false),
    dataAceiteLgpd: body.data_aceite_lgpd ?? existing?.dataAceiteLgpd,
    observacoes: body.observacoes ?? existing?.observacoes,
    motivoBloqueio: body.motivo_bloqueio ?? existing?.motivoBloqueio,
    status: existing?.status ?? 'EM_ANALISE',
    documentosObrigatorios
  } as Beneficiario;

  payload.status = resolveStatus(payload, missingDocuments, {
    existing,
    requestedStatus: body.status ?? existing?.status
  });

  return payload;
}

router.get('/', async (req, res) => {
  const repository = AppDataSource.getRepository(Beneficiario);
  const { nome, cpf, nis } = req.query as { nome?: string; cpf?: string; nis?: string };
  const qb = repository.createQueryBuilder('beneficiario');

  if (nome) {
    qb.andWhere('LOWER(beneficiario.nome_completo) LIKE LOWER(:nome)', { nome: `%${nome}%` });
  }
  if (cpf) {
    qb.andWhere('beneficiario.cpf = :cpf', { cpf: onlyDigits(String(cpf)) });
  }
  if (nis) {
    qb.andWhere('beneficiario.nis = :nis', { nis });
  }

  const beneficiarios = await qb.orderBy('beneficiario.nome_completo', 'ASC').getMany();

  const outdated = beneficiarios.filter((item) => isOutdated(item) && item.status !== 'DESATUALIZADO');
  if (outdated.length) {
    await Promise.all(
      outdated.map((record) => {
        record.status = 'DESATUALIZADO';
        return repository.save(record);
      })
    );
  }
  res.json({ beneficiarios });
});

router.get('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(Beneficiario);
  const beneficiario = await repository.findOne({ where: { idBeneficiario: req.params.id } });

  if (!beneficiario) {
    return res.status(404).json({ message: 'Beneficiário não encontrado' });
  }

  if (isOutdated(beneficiario) && beneficiario.status !== 'DESATUALIZADO') {
    beneficiario.status = 'DESATUALIZADO';
    await repository.save(beneficiario);
  }

  res.json({ beneficiario });
});

router.post('/', async (req, res) => {
  const repository = AppDataSource.getRepository(Beneficiario);
  const payload = buildBeneficiarioPayload(req);
  const missingDocuments = getMissingRequiredDocuments(req.body, payload.documentosObrigatorios);

  payload.status = resolveStatus(payload, missingDocuments, { requestedStatus: 'EM_ANALISE' });

  if (!payload.nomeCompleto || !payload.dataNascimento || !payload.nomeMae) {
    return res.status(400).json({ message: 'Campos obrigatórios não preenchidos' });
  }

  if (payload.cpf) {
    const unique = await ensureCpfUnique(payload.cpf);
    if (!unique) {
      return res.status(409).json({ message: 'CPF já cadastrado' });
    }
  }

  try {
    const created = repository.create(payload);
    const saved = await repository.save(created);
    res.status(201).json({ beneficiario: saved });
  } catch (error) {
    console.error('Erro ao salvar beneficiário', error);
    res.status(500).json({ message: 'Erro ao salvar beneficiário' });
  }
});

router.put('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(Beneficiario);
  const existing = await repository.findOne({ where: { idBeneficiario: req.params.id } });

  if (!existing) {
    return res.status(404).json({ message: 'Beneficiário não encontrado' });
  }

  const payload = buildBeneficiarioPayload(req, existing);

  if (payload.cpf) {
    const unique = await ensureCpfUnique(payload.cpf, existing.idBeneficiario);
    if (!unique) {
      return res.status(409).json({ message: 'CPF já cadastrado' });
    }
  }

  try {
    repository.merge(existing, payload);
    const saved = await repository.save(existing);
    res.json({ beneficiario: saved });
  } catch (error) {
    console.error('Erro ao atualizar beneficiário', error);
    res.status(500).json({ message: 'Erro ao atualizar beneficiário' });
  }
});

router.delete('/:id', async (req, res) => {
  const repository = AppDataSource.getRepository(Beneficiario);
  const existing = await repository.findOne({ where: { idBeneficiario: req.params.id } });

  if (!existing) {
    return res.status(404).json({ message: 'Beneficiário não encontrado' });
  }

  await repository.remove(existing);
  res.status(204).send();
});

export default router;
