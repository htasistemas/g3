import {
  Column,
  CreateDateColumn,
  Entity,
  JoinColumn,
  ManyToOne,
  PrimaryGeneratedColumn,
  UpdateDateColumn
} from 'typeorm';
import { Beneficiario } from './Beneficiario';
import { BenefitType } from './BenefitType';
import { TermoFomento } from './TermoFomento';

@Entity('benefit_grants')
export class BenefitGrant {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @ManyToOne(() => Beneficiario, { eager: true, nullable: false, onDelete: 'CASCADE' })
  @JoinColumn({ name: 'beneficiarioId' })
  beneficiario!: Beneficiario;

  @Column({ type: 'integer' })
  beneficiarioId!: number;

  @ManyToOne(() => BenefitType, { eager: true, nullable: false, onDelete: 'RESTRICT' })
  @JoinColumn({ name: 'benefitTypeId' })
  benefitType!: BenefitType;

  @Column({ type: 'uuid' })
  benefitTypeId!: string;

  @ManyToOne(() => TermoFomento, { eager: true, nullable: true, onDelete: 'SET NULL' })
  @JoinColumn({ name: 'termoFomentoId' })
  termoFomento?: TermoFomento | null;

  @Column({ type: 'uuid', nullable: true })
  termoFomentoId?: string | null;

  @Column({ type: 'date' })
  dataConcessao!: string;

  @Column({ type: 'integer', nullable: true })
  quantidade?: number | null;

  @Column({ type: 'decimal', precision: 10, scale: 2, nullable: true })
  valorEstimado?: string | null;

  @Column({ type: 'text', nullable: true })
  observacao?: string | null;

  @CreateDateColumn()
  createdAt!: Date;

  @UpdateDateColumn()
  updatedAt!: Date;
}
