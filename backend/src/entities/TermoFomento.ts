import { Column, CreateDateColumn, Entity, OneToMany, PrimaryGeneratedColumn, UpdateDateColumn } from 'typeorm';
import { PlanoTrabalho } from './PlanoTrabalho';

@Entity('termos_fomento')
export class TermoFomento {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column({ unique: true })
  numero!: string;

  @Column({ type: 'text', nullable: true })
  objeto?: string;

  @Column({ nullable: true })
  orgaoConcedente?: string;

  @Column({ nullable: true })
  modalidade?: string;

  @Column({ type: 'date', nullable: true })
  vigenciaInicio?: string;

  @Column({ type: 'date', nullable: true })
  vigenciaFim?: string;

  @Column({ type: 'decimal', precision: 14, scale: 2, nullable: true })
  valorGlobal?: string;

  @Column({ nullable: true })
  status?: string;

  @OneToMany(() => PlanoTrabalho, (plano) => plano.termoFomento)
  planos?: PlanoTrabalho[];

  @CreateDateColumn()
  createdAt!: Date;

  @UpdateDateColumn()
  updatedAt!: Date;
}
