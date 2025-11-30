import { Column, CreateDateColumn, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity('unidades_assistenciais')
export class AssistanceUnit {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column({ name: 'nome_fantasia', unique: true, nullable: true })
  nomeFantasia?: string;

  @Column({ name: 'razao_social', nullable: true })
  razaoSocial?: string;

  @Column({ name: 'cnpj', nullable: true })
  cnpj?: string;

  @Column({ name: 'telefone', nullable: true })
  telefone?: string;

  @Column({ name: 'email', nullable: true })
  email?: string;

  @Column({ name: 'cep', nullable: true })
  cep?: string;

  @Column({ name: 'endereco', nullable: true })
  endereco?: string;

  @Column({ name: 'numero_endereco', nullable: true })
  numeroEndereco?: string;

  @Column({ name: 'bairro', nullable: true })
  bairro?: string;

  @Column({ name: 'cidade', nullable: true })
  cidade?: string;

  @Column({ name: 'estado', nullable: true })
  estado?: string;

  @Column({ name: 'observacoes', type: 'text', nullable: true })
  observacoes?: string;

  @Column({ name: 'responsavel_nome', nullable: true })
  responsavelNome?: string;

  @Column({ name: 'responsavel_cpf', nullable: true })
  responsavelCpf?: string;

  @Column({ name: 'responsavel_periodo_mandato', nullable: true })
  responsavelPeriodoMandato?: string;

  @CreateDateColumn({ name: 'criado_em' })
  criadoEm!: Date;
}
