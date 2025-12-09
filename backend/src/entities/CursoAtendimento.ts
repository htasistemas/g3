import {
  Column,
  CreateDateColumn,
  Entity,
  JoinColumn,
  ManyToOne,
  PrimaryGeneratedColumn,
  UpdateDateColumn
} from 'typeorm';
import { Sala } from './Sala';

type EnrollmentStatus = 'Ativo' | 'Concluído' | 'Cancelado';

export type AtendimentoStatus = 'TRIAGEM' | 'EM_ANDAMENTO' | 'ENCAMINHADO' | 'EM_VISITA' | 'CONCLUIDO';

export interface AtendimentoStatusEntry {
  status: AtendimentoStatus;
  changedAt: string;
  justification?: string;
}

export interface EnrollmentRecord {
  id: string;
  beneficiaryName: string;
  cpf: string;
  whatsapp?: string | null;
  status: EnrollmentStatus;
  enrolledAt: string;
}

export interface WaitlistRecord {
  id: string;
  beneficiaryName: string;
  cpf: string;
  whatsapp?: string | null;
  joinedAt: string;
}

@Entity('cursos_atendimentos')
export class CursoAtendimento {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column()
  tipo!: 'Curso' | 'Atendimento' | 'Oficina';

  @Column()
  nome!: string;

  @Column({ type: 'text' })
  descricao!: string;

  @Column({ type: 'text', nullable: true })
  imagem?: string | null;

  @Column({ type: 'integer' })
  vagasTotais!: number;

  @Column({ type: 'integer' })
  vagasDisponiveis!: number;

  @Column({ type: 'integer', nullable: true })
  cargaHoraria?: number | null;

  @Column({ type: 'varchar' })
  horarioInicial!: string;

  @Column({ type: 'integer' })
  duracaoHoras!: number;

  @Column({ type: 'simple-array', nullable: true })
  diasSemana?: string[];

  @Column({ type: 'varchar' })
  profissional!: string;

  @ManyToOne(() => Sala, (sala) => sala.cursos, { eager: true, nullable: true, onDelete: 'SET NULL' })
  @JoinColumn({ name: 'salaId' })
  sala?: Sala | null;

  @Column({ type: 'uuid', nullable: true })
  salaId?: string | null;

  @Column({ type: 'simple-json', nullable: true })
  enrollments?: EnrollmentRecord[];

  @Column({ type: 'simple-json', nullable: true })
  waitlist?: WaitlistRecord[];

  @Column({ type: 'text', nullable: true, name: 'certificate_template' })
  certificateTemplate?: string | null;

  @Column({ type: 'varchar', default: 'TRIAGEM' })
  status!: AtendimentoStatus;

  @Column({ type: 'simple-json', nullable: true })
  statusHistory?: AtendimentoStatusEntry[] | null;

  @Column({ type: 'timestamp', nullable: true })
  dataTriagem?: Date | null;

  @Column({ type: 'timestamp', nullable: true })
  dataEncaminhamento?: Date | null;

  @Column({ type: 'timestamp', nullable: true })
  dataConclusao?: Date | null;

  @CreateDateColumn()
  createdAt!: Date;

  @UpdateDateColumn()
  updatedAt!: Date;
}

