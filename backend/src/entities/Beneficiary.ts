import { Column, CreateDateColumn, Entity, PrimaryGeneratedColumn } from 'typeorm';

interface DocumentoAnexo {
  nome: string;
  nomeArquivo?: string;
}

@Entity('beneficiarios')
export class Beneficiary {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column({ name: 'nome_completo' })
  nomeCompleto!: string;

  @Column({ name: 'nome_mae', nullable: true })
  nomeMae?: string;

  @Column({ name: 'documentos' })
  documentos!: string;

  @Column({ name: 'data_nascimento', type: 'date' })
  dataNascimento!: string;

  @Column({ name: 'idade', type: 'int', nullable: true })
  idade?: number;

  @Column({ name: 'telefone' })
  telefone!: string;

  @Column({ name: 'email' })
  email!: string;

  @Column({ name: 'cep' })
  cep!: string;

  @Column({ name: 'endereco' })
  endereco!: string;

  @Column({ name: 'numero_endereco', nullable: true })
  numeroEndereco?: string;

  @Column({ name: 'ponto_referencia', nullable: true })
  pontoReferencia?: string;

  @Column({ name: 'bairro', nullable: true })
  bairro?: string;

  @Column({ name: 'cidade', nullable: true })
  cidade?: string;

  @Column({ name: 'estado', nullable: true })
  estado?: string;

  @Column({ name: 'observacoes', nullable: true })
  observacoes?: string;

  @Column({ name: 'status', default: 'Ativo' })
  status!: string;

  @Column({ name: 'motivo_bloqueio', type: 'text', nullable: true })
  motivoBloqueio?: string;

  @Column({ name: 'possui_filhos_menores', default: false })
  possuiFilhosMenores!: boolean;

  @Column({ name: 'possui_cnh', default: false })
  possuiCnh!: boolean;

  @Column({ name: 'quantidade_filhos_menores', type: 'int', nullable: true })
  quantidadeFilhosMenores?: number;

  @Column({ name: 'escolaridade', nullable: true })
  escolaridade?: string;

  @Column({ name: 'renda_individual', type: 'decimal', precision: 12, scale: 2, nullable: true })
  rendaIndividual?: number;

  @Column({ name: 'renda_familiar', type: 'decimal', precision: 12, scale: 2, nullable: true })
  rendaFamiliar?: number;

  @Column({ name: 'informacoes_moradia', type: 'text', nullable: true })
  informacoesMoradia?: string;

  @Column({ name: 'condicoes_saneamento', type: 'text', nullable: true })
  condicoesSaneamento?: string;

  @Column({ name: 'situacao_emprego', nullable: true })
  situacaoEmprego?: string;

  @Column({ name: 'ocupacao', nullable: true })
  ocupacao?: string;

  @Column({ name: 'documentos_anexos', type: 'simple-json', nullable: true })
  documentosAnexos?: DocumentoAnexo[];

  @Column({ name: 'foto', type: 'text', nullable: true })
  foto?: string | null;

  @CreateDateColumn({ name: 'criado_em' })
  criadoEm!: Date;
}
