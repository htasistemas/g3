import {
  Column,
  CreateDateColumn,
  Entity,
  OneToMany,
  PrimaryGeneratedColumn,
  UpdateDateColumn
} from 'typeorm';
import { PatrimonioMovimento } from './PatrimonioMovimento';

@Entity('patrimonios')
export class Patrimonio {
  @PrimaryGeneratedColumn('uuid')
  idPatrimonio!: string;

  @Column({ unique: true })
  numeroPatrimonio!: string;

  @Column()
  nome!: string;

  @Column({ nullable: true })
  categoria?: string;

  @Column({ nullable: true })
  subcategoria?: string;

  @Column({ nullable: true })
  conservacao?: string;

  @Column({ nullable: true })
  status?: string;

  @Column({ type: 'date', nullable: true })
  dataAquisicao?: string;

  @Column({ type: 'decimal', precision: 12, scale: 2, nullable: true })
  valorAquisicao?: string;

  @Column({ nullable: true })
  origem?: string;

  @Column({ nullable: true })
  responsavel?: string;

  @Column({ nullable: true })
  unidade?: string;

  @Column({ nullable: true })
  sala?: string;

  @Column({ type: 'decimal', precision: 5, scale: 2, nullable: true })
  taxaDepreciacao?: string;

  @Column({ type: 'text', nullable: true })
  observacoes?: string;

  @OneToMany(() => PatrimonioMovimento, (movimento) => movimento.patrimonio, { cascade: true })
  movimentos?: PatrimonioMovimento[];

  @CreateDateColumn({ type: 'datetime' })
  createdAt!: Date;

  @UpdateDateColumn({ type: 'datetime' })
  updatedAt!: Date;
}
