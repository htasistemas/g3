import { Column, CreateDateColumn, Entity, ManyToOne, PrimaryGeneratedColumn } from 'typeorm';
import { Patrimonio } from './Patrimonio';

export type MovimentoTipo = 'MOVIMENTACAO' | 'MANUTENCAO' | 'BAIXA';

@Entity('patrimonio_movimentos')
export class PatrimonioMovimento {
  @PrimaryGeneratedColumn('uuid')
  idMovimento!: string;

  @Column()
  tipo!: MovimentoTipo;

  @Column({ nullable: true })
  destino?: string;

  @Column({ nullable: true })
  responsavel?: string;

  @Column({ type: 'text', nullable: true })
  observacao?: string;

  @CreateDateColumn({ type: 'datetime' })
  dataMovimento!: Date;

  @ManyToOne(() => Patrimonio, (patrimonio) => patrimonio.movimentos, { onDelete: 'CASCADE' })
  patrimonio!: Patrimonio;

  @Column()
  patrimonioId!: string;
}
