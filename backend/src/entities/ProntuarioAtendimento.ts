import {
  Column,
  CreateDateColumn,
  Entity,
  JoinColumn,
  ManyToOne,
  PrimaryGeneratedColumn,
  UpdateDateColumn
} from 'typeorm';
import { Prontuario } from './Prontuario';

@Entity('prontuario_atendimentos')
export class ProntuarioAtendimento {
  @PrimaryGeneratedColumn('uuid', { name: 'id_prontuario_atendimento' })
  idProntuarioAtendimento!: string;

  @Column({ name: 'id_prontuario', type: 'uuid' })
  prontuarioId!: string;

  @ManyToOne(() => Prontuario, (prontuario) => prontuario.atendimentos, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'id_prontuario', referencedColumnName: 'idProntuario' })
  prontuario!: Prontuario;

  @Column({ name: 'data_atendimento', type: 'date', nullable: true })
  dataAtendimento?: string;

  @Column({ name: 'tipo_atendimento', nullable: true })
  tipoAtendimento?: string;

  @Column({ name: 'descricao', type: 'text', nullable: true })
  descricao?: string;

  @Column({ name: 'responsavel', nullable: true })
  responsavel?: string;

  @Column({ name: 'resultado', type: 'text', nullable: true })
  resultado?: string;

  @CreateDateColumn({ name: 'created_at' })
  createdAt!: Date;

  @UpdateDateColumn({ name: 'updated_at' })
  updatedAt!: Date;
}
