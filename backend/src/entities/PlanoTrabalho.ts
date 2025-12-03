import {
  Column,
  CreateDateColumn,
  Entity,
  ManyToOne,
  OneToMany,
  PrimaryGeneratedColumn,
  UpdateDateColumn
} from 'typeorm';
import { TermoFomento } from './TermoFomento';
import { PlanoMeta } from './PlanoMeta';
import { PlanoCronogramaItem } from './PlanoCronogramaItem';
import { PlanoEquipe } from './PlanoEquipe';

export type PlanoStatus =
  | 'EM_ELABORACAO'
  | 'ENVIADO_ANALISE'
  | 'APROVADO'
  | 'EM_EXECUCAO'
  | 'CONCLUIDO'
  | 'REPROVADO';

@Entity('planos_trabalho')
export class PlanoTrabalho {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column({ unique: true })
  codigoInterno!: string;

  @Column()
  titulo!: string;

  @Column({ type: 'text' })
  descricaoGeral!: string;

  @Column({ default: 'EM_ELABORACAO' })
  status!: PlanoStatus;

  @Column({ nullable: true })
  orgaoConcedente?: string;

  @Column({ nullable: true })
  orgaoOutroDescricao?: string;

  @Column({ nullable: true })
  areaPrograma?: string;

  @Column({ type: 'date', nullable: true })
  dataElaboracao?: string;

  @Column({ type: 'date', nullable: true })
  dataAprovacao?: string;

  @Column({ type: 'date', nullable: true })
  vigenciaInicio?: string;

  @Column({ type: 'date', nullable: true })
  vigenciaFim?: string;

  @Column({ nullable: true })
  numeroProcesso?: string;

  @Column({ nullable: true })
  modalidade?: string;

  @Column({ type: 'text', nullable: true })
  observacoesVinculacao?: string;

  @ManyToOne(() => TermoFomento, (termo) => termo.planos, { eager: true, nullable: false })
  termoFomento!: TermoFomento;

  @OneToMany(() => PlanoMeta, (meta) => meta.plano, { cascade: true, eager: true })
  metas!: PlanoMeta[];

  @OneToMany(() => PlanoCronogramaItem, (item) => item.plano, { cascade: true, eager: true })
  cronograma!: PlanoCronogramaItem[];

  @OneToMany(() => PlanoEquipe, (membro) => membro.plano, { cascade: true, eager: true })
  equipe!: PlanoEquipe[];

  @CreateDateColumn()
  createdAt!: Date;

  @UpdateDateColumn()
  updatedAt!: Date;
}
