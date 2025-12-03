import { Column, Entity, ManyToOne, OneToMany, PrimaryGeneratedColumn } from 'typeorm';
import { PlanoTrabalho } from './PlanoTrabalho';
import { PlanoAtividade } from './PlanoAtividade';

@Entity('planos_metas')
export class PlanoMeta {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column({ nullable: true })
  codigo?: string;

  @Column()
  descricao!: string;

  @Column({ nullable: true })
  indicador?: string;

  @Column({ nullable: true })
  unidadeMedida?: string;

  @Column({ type: 'int', nullable: true })
  quantidadePrevista?: number;

  @Column({ type: 'text', nullable: true })
  resultadoEsperado?: string;

  @ManyToOne(() => PlanoTrabalho, (plano) => plano.metas, { onDelete: 'CASCADE' })
  plano!: PlanoTrabalho;

  @OneToMany(() => PlanoAtividade, (atividade) => atividade.meta, { cascade: true, eager: true })
  atividades!: PlanoAtividade[];
}
