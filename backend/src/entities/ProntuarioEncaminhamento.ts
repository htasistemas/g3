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

@Entity('prontuario_encaminhamentos')
export class ProntuarioEncaminhamento {
  @PrimaryGeneratedColumn('uuid', { name: 'id_prontuario_encaminhamento' })
  idProntuarioEncaminhamento!: string;

  @Column({ name: 'id_prontuario', type: 'uuid' })
  prontuarioId!: string;

  @ManyToOne(() => Prontuario, (prontuario) => prontuario.encaminhamentos, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'id_prontuario', referencedColumnName: 'idProntuario' })
  prontuario!: Prontuario;

  @Column({ name: 'data_encaminhamento', type: 'date', nullable: true })
  dataEncaminhamento?: string;

  @Column({ name: 'destino', nullable: true })
  destino?: string;

  @Column({ name: 'motivo', type: 'text', nullable: true })
  motivo?: string;

  @Column({ name: 'responsavel', nullable: true })
  responsavel?: string;

  @Column({ name: 'status', nullable: true })
  status?: string;

  @Column({ name: 'retorno_previsto', type: 'date', nullable: true })
  retornoPrevisto?: string;

  @Column({ name: 'observacoes', type: 'text', nullable: true })
  observacoes?: string;

  @CreateDateColumn({ name: 'created_at' })
  createdAt!: Date;

  @UpdateDateColumn({ name: 'updated_at' })
  updatedAt!: Date;
}
