import { Column, CreateDateColumn, Entity, OneToMany, PrimaryGeneratedColumn, UpdateDateColumn } from 'typeorm';
import { CursoAtendimento } from './CursoAtendimento';

@Entity('salas')
export class Sala {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column({ unique: true })
  nome!: string;

  @OneToMany(() => CursoAtendimento, (curso) => curso.sala)
  cursos?: CursoAtendimento[];

  @CreateDateColumn()
  createdAt!: Date;

  @UpdateDateColumn()
  updatedAt!: Date;
}
