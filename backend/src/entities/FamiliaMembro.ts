import {
  Column,
  CreateDateColumn,
  Entity,
  JoinColumn,
  ManyToOne,
  PrimaryGeneratedColumn,
  Unique,
  UpdateDateColumn
} from 'typeorm';
import { Familia } from './Familia';
import { Beneficiario } from './Beneficiario';

@Entity('familia_membro')
@Unique('uk_familia_membro_beneficiario', ['familiaId', 'beneficiarioId'])
export class FamiliaMembro {
  @PrimaryGeneratedColumn('uuid', { name: 'id_familia_membro' })
  idFamiliaMembro!: string;

  @Column({ name: 'id_familia', type: 'uuid' })
  familiaId!: string;

  @Column({ name: 'id_beneficiario', type: 'uuid' })
  beneficiarioId!: string;

  @Column({ name: 'parentesco' })
  parentesco!: string;

  @Column({ name: 'responsavel_familiar', default: false })
  responsavelFamiliar!: boolean;

  @Column({ name: 'contribui_renda', default: false })
  contribuiRenda!: boolean;

  @Column({ name: 'renda_individual', type: 'numeric', precision: 12, scale: 2, nullable: true })
  rendaIndividual?: string;

  @Column({ name: 'participa_servicos', default: false })
  participaServicos!: boolean;

  @Column({ name: 'observacoes', type: 'text', nullable: true })
  observacoes?: string;

  @CreateDateColumn({ name: 'data_cadastro' })
  dataCadastro!: Date;

  @UpdateDateColumn({ name: 'data_atualizacao', nullable: true })
  dataAtualizacao?: Date;

  @ManyToOne(() => Familia, (familia) => familia.membros, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'id_familia', referencedColumnName: 'idFamilia' })
  familia!: Familia;

  @ManyToOne(() => Beneficiario, (beneficiario) => beneficiario.familias, { eager: true, onDelete: 'CASCADE' })
  @JoinColumn({ name: 'id_beneficiario', referencedColumnName: 'idBeneficiario' })
  beneficiario!: Beneficiario;
}
