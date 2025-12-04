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

export interface EnrollmentRecord {
  id: string;
  beneficiaryName: string;
  cpf: string;
  status: EnrollmentStatus;
  enrolledAt: string;
}

export interface WaitlistRecord {
  id: string;
  beneficiaryName: string;
  cpf: string;
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

  @CreateDateColumn()
  createdAt!: Date;

  @UpdateDateColumn()
  updatedAt!: Date;
}

