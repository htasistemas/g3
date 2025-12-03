import { Column, Entity, ManyToOne, OneToMany, PrimaryGeneratedColumn } from 'typeorm';
import { PlanoMeta } from './PlanoMeta';
import { PlanoEtapa } from './PlanoEtapa';

@Entity('planos_atividades')
export class PlanoAtividade {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column()
  descricao!: string;

  @Column({ type: 'text', nullable: true })
  justificativa?: string;

  @Column({ type: 'text', nullable: true })
  publicoAlvo?: string;

  @Column({ nullable: true })
  localExecucao?: string;

  @Column({ nullable: true })
  produtoEsperado?: string;

  @ManyToOne(() => PlanoMeta, (meta) => meta.atividades, { onDelete: 'CASCADE' })
  meta!: PlanoMeta;

  @OneToMany(() => PlanoEtapa, (etapa) => etapa.atividade, { cascade: true, eager: true })
  etapas!: PlanoEtapa[];
}
