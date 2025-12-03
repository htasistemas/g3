import { Column, Entity, ManyToOne, PrimaryGeneratedColumn } from 'typeorm';
import { PlanoTrabalho } from './PlanoTrabalho';

@Entity('planos_equipes')
export class PlanoEquipe {
  @PrimaryGeneratedColumn('uuid')
  id!: string;

  @Column()
  nome!: string;

  @Column({ nullable: true })
  funcao?: string;

  @Column({ nullable: true })
  cpf?: string;

  @Column({ nullable: true })
  cargaHoraria?: string;

  @Column({ nullable: true })
  tipoVinculo?: string;

  @Column({ nullable: true })
  contato?: string;

  @ManyToOne(() => PlanoTrabalho, (plano) => plano.equipe, { onDelete: 'CASCADE' })
  plano!: PlanoTrabalho;
}
