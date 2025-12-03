import { Column, Entity, ManyToOne, PrimaryGeneratedColumn } from 'typeorm';
import { PlanoTrabalho } from './PlanoTrabalho';

@Entity('planos_cronograma')
export class PlanoCronogramaItem {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column({ nullable: true })
  referenciaTipo?: string;

  @Column({ nullable: true })
  referenciaId?: string;

  @Column({ nullable: true })
  referenciaDescricao?: string;

  @Column()
  competencia!: string;

  @Column({ type: 'text', nullable: true })
  descricaoResumida?: string;

  @Column({ type: 'decimal', precision: 14, scale: 2, nullable: true })
  valorPrevisto?: string;

  @Column({ nullable: true })
  fonteRecurso?: string;

  @Column({ nullable: true })
  naturezaDespesa?: string;

  @Column({ type: 'text', nullable: true })
  observacoes?: string;

  @ManyToOne(() => PlanoTrabalho, (plano) => plano.cronograma, { onDelete: 'CASCADE' })
  plano!: PlanoTrabalho;
}
