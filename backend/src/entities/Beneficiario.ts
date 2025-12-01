import {
  Column,
  CreateDateColumn,
  Entity,
  Index,
  OneToMany,
  PrimaryGeneratedColumn,
  UpdateDateColumn
} from 'typeorm';
import { FamiliaMembro } from './FamiliaMembro';

export type SexoBiologico = 'MASCULINO' | 'FEMININO' | 'OUTRO';
export type CorRaca = 'BRANCA' | 'PRETA' | 'PARDA' | 'AMARELA' | 'INDIGENA' | 'NAO_INFORMADO';
export type CertidaoTipo = 'NASCIMENTO' | 'CASAMENTO' | 'OUTRO';
export type Zona = 'URBANA' | 'RURAL';
export type SituacaoImovel = 'PROPRIO' | 'FINANCIADO' | 'ALUGADO' | 'CEDIDO' | 'OCUPACAO' | 'OUTRO';
export type TipoMoradia = 'CASA' | 'APARTAMENTO' | 'BARRACO' | 'OUTRO';
export type EsgotoTipo = 'REDE' | 'FOSSA' | 'CEU_ABERTO' | 'NAO_POSSUI';
export type ColetaLixo = 'REGULAR' | 'IRREGULAR' | 'NAO_POSSUI';
export type TipoDeficiencia = 'FISICA' | 'INTELECTUAL' | 'AUDITIVA' | 'VISUAL' | 'MULTIPLA' | 'TEA';
export type BeneficiarioStatus = 'ATIVO' | 'INATIVO' | 'DESATUALIZADO' | 'INCOMPLETO' | 'EM_ANALISE' | 'BLOQUEADO';

@Entity('beneficiario')
export class Beneficiario {
  @PrimaryGeneratedColumn('uuid', { name: 'id_beneficiario' })
  idBeneficiario!: string;

  // Identificação
  @Index('idx_beneficiario_nome')
  @Column({ name: 'nome_completo' })
  nomeCompleto!: string;

  @Column({ name: 'nome_social', nullable: true })
  nomeSocial?: string;

  @Column({ name: 'apelido', nullable: true })
  apelido?: string;

  @Column({ name: 'data_nascimento', type: 'date' })
  dataNascimento!: string;

  @Column({ name: 'sexo_biologico', nullable: true })
  sexoBiologico?: SexoBiologico;

  @Column({ name: 'identidade_genero', nullable: true })
  identidadeGenero?: string;

  @Column({ name: 'cor_raca', nullable: true })
  corRaca?: CorRaca;

  @Column({ name: 'status', default: 'EM_ANALISE' })
  status!: BeneficiarioStatus;

  @Column({ name: 'motivo_bloqueio', type: 'text', nullable: true })
  motivoBloqueio?: string;

  @Column({ name: 'estado_civil', nullable: true })
  estadoCivil?: string;

  @Column({ name: 'nacionalidade', nullable: true })
  nacionalidade?: string;

  @Column({ name: 'naturalidade_cidade', nullable: true })
  naturalidadeCidade?: string;

  @Column({ name: 'naturalidade_uf', length: 2, nullable: true })
  naturalidadeUf?: string;

  @Column({ name: 'nome_mae' })
  nomeMae!: string;

  @Column({ name: 'nome_pai', nullable: true })
  nomePai?: string;

  // Documentos
  @Index('idx_beneficiario_cpf', { unique: true, where: 'cpf IS NOT NULL' })
  @Column({ name: 'cpf', length: 11, nullable: true, unique: true, type: 'varchar' })
  cpf?: string;

  @Column({ name: 'rg_numero', nullable: true })
  rgNumero?: string;

  @Column({ name: 'rg_orgao_emissor', nullable: true })
  rgOrgaoEmissor?: string;

  @Column({ name: 'rg_uf', length: 2, nullable: true })
  rgUf?: string;

  @Column({ name: 'rg_data_emissao', type: 'date', nullable: true })
  rgDataEmissao?: string;

  @Index('idx_beneficiario_nis')
  @Column({ name: 'nis', nullable: true })
  nis?: string;

  @Column({ name: 'certidao_tipo', nullable: true })
  certidaoTipo?: CertidaoTipo;

  @Column({ name: 'certidao_livro', nullable: true })
  certidaoLivro?: string;

  @Column({ name: 'certidao_folha', nullable: true })
  certidaoFolha?: string;

  @Column({ name: 'certidao_termo', nullable: true })
  certidaoTermo?: string;

  @Column({ name: 'certidao_cartorio', nullable: true })
  certidaoCartorio?: string;

  @Column({ name: 'certidao_municipio', nullable: true })
  certidaoMunicipio?: string;

  @Column({ name: 'certidao_uf', length: 2, nullable: true })
  certidaoUf?: string;

  @Column({ name: 'titulo_eleitor', nullable: true })
  tituloEleitor?: string;

  @Column({ name: 'cnh', nullable: true })
  cnh?: string;

  @Column({ name: 'cartao_sus', nullable: true })
  cartaoSus?: string;

  // Contato
  @Column({ name: 'telefone_principal', nullable: true })
  telefonePrincipal?: string;

  @Column({ name: 'telefone_principal_whatsapp', default: false })
  telefonePrincipalWhatsapp!: boolean;

  @Column({ name: 'telefone_secundario', nullable: true })
  telefoneSecundario?: string;

  @Column({ name: 'telefone_recado_nome', nullable: true })
  telefoneRecadoNome?: string;

  @Column({ name: 'telefone_recado_numero', nullable: true })
  telefoneRecadoNumero?: string;

  @Column({ name: 'email', nullable: true })
  email?: string;

  @Column({ name: 'permite_contato_tel', default: true })
  permiteContatoTel!: boolean;

  @Column({ name: 'permite_contato_whatsapp', default: true })
  permiteContatoWhatsapp!: boolean;

  @Column({ name: 'permite_contato_sms', default: false })
  permiteContatoSms!: boolean;

  @Column({ name: 'permite_contato_email', default: false })
  permiteContatoEmail!: boolean;

  @Column({ name: 'horario_preferencial_contato', nullable: true })
  horarioPreferencialContato?: string;

  // Endereço individual
  @Column({ name: 'usa_endereco_familia', default: true })
  usaEnderecoFamilia!: boolean;

  @Column({ name: 'cep', nullable: true })
  cep?: string;

  @Column({ name: 'logradouro', nullable: true })
  logradouro?: string;

  @Column({ name: 'numero', nullable: true })
  numero?: string;

  @Column({ name: 'complemento', nullable: true })
  complemento?: string;

  @Column({ name: 'bairro', nullable: true })
  bairro?: string;

  @Column({ name: 'ponto_referencia', nullable: true })
  pontoReferencia?: string;

  @Column({ name: 'municipio', nullable: true })
  municipio?: string;

  @Column({ name: 'uf', length: 2, nullable: true })
  uf?: string;

  @Column({ name: 'zona', nullable: true })
  zona?: Zona;

  @Column({ name: 'situacao_imovel', nullable: true })
  situacaoImovel?: SituacaoImovel;

  @Column({ name: 'tipo_moradia', nullable: true })
  tipoMoradia?: TipoMoradia;

  @Column({ name: 'agua_encanada', default: false })
  aguaEncanada!: boolean;

  @Column({ name: 'esgoto_tipo', nullable: true })
  esgotoTipo?: EsgotoTipo;

  @Column({ name: 'coleta_lixo', nullable: true })
  coletaLixo?: ColetaLixo;

  @Column({ name: 'energia_eletrica', default: false })
  energiaEletrica!: boolean;

  @Column({ name: 'internet', default: false })
  internet!: boolean;

  // Situação familiar/social
  @Column({ name: 'mora_com_familia', default: false })
  moraComFamilia!: boolean;

  @Column({ name: 'responsavel_legal', default: false })
  responsavelLegal!: boolean;

  @Column({ name: 'vinculo_familiar', nullable: true })
  vinculoFamiliar?: string;

  @Column({ name: 'situacao_vulnerabilidade', type: 'text', nullable: true })
  situacaoVulnerabilidade?: string;

  // Escolaridade & Trabalho
  @Column({ name: 'sabe_ler_escrever', default: false })
  sabeLerEscrever!: boolean;

  @Column({ name: 'nivel_escolaridade', nullable: true })
  nivelEscolaridade?: string;

  @Column({ name: 'estuda_atualmente', default: false })
  estudaAtualmente!: boolean;

  @Column({ name: 'ocupacao', nullable: true })
  ocupacao?: string;

  @Column({ name: 'situacao_trabalho', nullable: true })
  situacaoTrabalho?: string;

  @Column({ name: 'local_trabalho', nullable: true })
  localTrabalho?: string;

  @Column({ name: 'renda_mensal', type: 'numeric', precision: 12, scale: 2, nullable: true })
  rendaMensal?: string;

  @Column({ name: 'fonte_renda', nullable: true })
  fonteRenda?: string;

  // Saúde
  @Column({ name: 'possui_deficiencia', default: false })
  possuiDeficiencia!: boolean;

  @Column({ name: 'tipo_deficiencia', nullable: true })
  tipoDeficiencia?: TipoDeficiencia;

  @Column({ name: 'cid_principal', nullable: true })
  cidPrincipal?: string;

  @Column({ name: 'usa_medicacao_continua', default: false })
  usaMedicacaoContinua!: boolean;

  @Column({ name: 'descricao_medicacao', type: 'text', nullable: true })
  descricaoMedicacao?: string;

  @Column({ name: 'servico_saude_referencia', nullable: true })
  servicoSaudeReferencia?: string;

  // Programas/Benefícios
  @Column({ name: 'recebe_beneficio', default: false })
  recebeBeneficio!: boolean;

  @Column({ name: 'beneficios_descricao', type: 'text', nullable: true })
  beneficiosDescricao?: string;

  @Column({ name: 'valor_total_beneficios', type: 'numeric', precision: 12, scale: 2, nullable: true })
  valorTotalBeneficios?: string;

  // LGPD e observações
  @Column({ name: 'aceite_lgpd', default: false })
  aceiteLgpd!: boolean;

  @Column({ name: 'data_aceite_lgpd', type: 'datetime', nullable: true })
  dataAceiteLgpd?: Date;

  @Column({ name: 'observacoes', type: 'text', nullable: true })
  observacoes?: string;

  // Auditoria
  @CreateDateColumn({ name: 'data_cadastro' })
  dataCadastro!: Date;

  @UpdateDateColumn({ name: 'data_atualizacao', nullable: true })
  dataAtualizacao?: Date;

  @OneToMany(() => FamiliaMembro, (membro) => membro.beneficiario)
  familias?: FamiliaMembro[];
}
