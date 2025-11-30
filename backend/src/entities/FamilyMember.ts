import { Column, Entity, JoinColumn, ManyToOne, PrimaryGeneratedColumn } from 'typeorm';
import { Family } from './Family';
import { Beneficiary } from './Beneficiary';

@Entity('familia_membros')
export class FamilyMember {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column({ name: 'familia_id' })
  familiaId!: number;

  @Column({ name: 'beneficiario_id' })
  beneficiarioId!: number;

  @Column({ name: 'parentesco' })
  parentesco!: string;

  @Column({ name: 'eh_responsavel_familiar', default: false })
  ehResponsavelFamiliar!: boolean;

  @Column({ name: 'data_entrada_familia', type: 'date', nullable: true })
  dataEntradaFamilia?: string;

  @Column({ name: 'observacoes', type: 'text', nullable: true })
  observacoes?: string;

  @ManyToOne(() => Family, (family) => family.membros, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'familia_id' })
  familia!: Family;

  @ManyToOne(() => Beneficiary, { eager: true, onDelete: 'CASCADE' })
  @JoinColumn({ name: 'beneficiario_id' })
  beneficiario!: Beneficiary;
}
