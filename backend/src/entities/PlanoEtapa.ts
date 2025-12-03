import { Column, Entity, ManyToOne, PrimaryGeneratedColumn } from 'typeorm';
import { PlanoAtividade } from './PlanoAtividade';

@Entity('planos_etapas')
export class PlanoEtapa {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column()
  descricao!: string;

  @Column({ default: 'NAO_INICIADA' })
  status!: string;

  @Column({ type: 'date', nullable: true })
  dataInicioPrevista?: string;

  @Column({ type: 'date', nullable: true })
  dataFimPrevista?: string;

  @Column({ type: 'date', nullable: true })
  dataConclusao?: string;

  @Column({ nullable: true })
  responsavel?: string;

  @ManyToOne(() => PlanoAtividade, (atividade) => atividade.etapas, { onDelete: 'CASCADE' })
  atividade!: PlanoAtividade;
}
