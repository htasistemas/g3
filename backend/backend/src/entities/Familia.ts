import {
  Column,
  CreateDateColumn,
  Entity,
  JoinColumn,
  ManyToOne,
  OneToMany,
  PrimaryGeneratedColumn,
  UpdateDateColumn
} from 'typeorm';
import { Beneficiario } from './Beneficiario';
import { FamiliaMembro } from './FamiliaMembro';

export type ArranjoFamiliar = 'NUCLEAR' | 'MONOPARENTAL' | 'EXTENSA' | 'RECONSTITUIDA' | 'OUTRO';
export type FaixaRendaPerCapita = 'ATE_1_4_SM' | 'ENTRE_1_4_E_1_2_SM' | 'ENTRE_1_2_E_1_SM' | 'ACIMA_1_SM';
export type SituacaoInsegurancaAlimentar = 'NAO_APRESENTA' | 'MODERADA' | 'GRAVE';

@Entity('familia')
export class Familia {
  @PrimaryGeneratedColumn('uuid', { name: 'id_familia' })
  idFamilia!: string;

  @Column({ name: 'nome_familia' })
  nomeFamilia!: string;

  @Column({ name: 'id_referencia_familiar', type: 'uuid', nullable: true })
  idReferenciaFamiliar?: string;

  @ManyToOne(() => Beneficiario, { nullable: true })
  @JoinColumn({ name: 'id_referencia_familiar', referencedColumnName: 'idBeneficiario' })
  referenciaFamiliar?: Beneficiario | null;

  // Endereço
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
  zona?: string;

  @Column({ name: 'situacao_imovel', nullable: true })
  situacaoImovel?: string;

  @Column({ name: 'tipo_moradia', nullable: true })
  tipoMoradia?: string;

  @Column({ name: 'agua_encanada', default: false })
  aguaEncanada!: boolean;

  @Column({ name: 'esgoto_tipo', nullable: true })
  esgotoTipo?: string;

  @Column({ name: 'coleta_lixo', nullable: true })
  coletaLixo?: string;

  @Column({ name: 'energia_eletrica', default: false })
  energiaEletrica!: boolean;

  @Column({ name: 'internet', default: false })
  internet!: boolean;

  // Indicadores
  @Column({ name: 'arranjo_familiar', nullable: true })
  arranjoFamiliar?: ArranjoFamiliar;

  @Column({ name: 'qtd_membros', type: 'int', default: 0 })
  qtdMembros!: number;

  @Column({ name: 'qtd_criancas', type: 'int', default: 0 })
  qtdCriancas!: number;

  @Column({ name: 'qtd_adolescentes', type: 'int', default: 0 })
  qtdAdolescentes!: number;

  @Column({ name: 'qtd_idosos', type: 'int', default: 0 })
  qtdIdosos!: number;

  @Column({ name: 'qtd_pessoas_deficiencia', type: 'int', default: 0 })
  qtdPessoasDeficiencia!: number;

  // Renda
  @Column({ name: 'renda_familiar_total', type: 'numeric', precision: 12, scale: 2, default: 0 })
  rendaFamiliarTotal!: string;

  @Column({ name: 'renda_per_capita', type: 'numeric', precision: 12, scale: 2, default: 0 })
  rendaPerCapita!: string;

  @Column({ name: 'faixa_renda_per_capita', nullable: true })
  faixaRendaPerCapita?: FaixaRendaPerCapita;

  @Column({ name: 'principais_fontes_renda', type: 'text', nullable: true })
  principaisFontesRenda?: string;

  @Column({ name: 'situacao_inseguranca_alimentar', nullable: true })
  situacaoInsegurancaAlimentar?: SituacaoInsegurancaAlimentar;

  @Column({ name: 'possui_dividas_relevantes', default: false })
  possuiDividasRelevantes!: boolean;

  @Column({ name: 'descricao_dividas', type: 'text', nullable: true })
  descricaoDividas?: string;

  // Vulnerabilidades e acompanhamento
  @Column({ name: 'vulnerabilidades_familia', type: 'text', nullable: true })
  vulnerabilidadesFamilia?: string;

  @Column({ name: 'servicos_acompanhamento', type: 'text', nullable: true })
  servicosAcompanhamento?: string;

  @Column({ name: 'tecnico_responsavel', nullable: true })
  tecnicoResponsavel?: string;

  @Column({ name: 'periodicidade_atendimento', nullable: true })
  periodicidadeAtendimento?: string;

  @Column({ name: 'proxima_visita_prevista', type: 'date', nullable: true })
  proximaVisitaPrevista?: string;

  @Column({ name: 'observacoes', type: 'text', nullable: true })
  observacoes?: string;

  // Auditoria
  @CreateDateColumn({ name: 'data_cadastro' })
  dataCadastro!: Date;

  @UpdateDateColumn({ name: 'data_atualizacao', nullable: true })
  dataAtualizacao?: Date;

  @OneToMany(() => FamiliaMembro, (membro) => membro.familia, { cascade: true })
  membros?: FamiliaMembro[];
}
