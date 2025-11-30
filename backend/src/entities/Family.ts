import { Column, CreateDateColumn, Entity, OneToMany, PrimaryGeneratedColumn } from 'typeorm';
import { FamilyMember } from './FamilyMember';

@Entity('familias')
export class Family {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column({ name: 'responsavel_familiar_id', type: 'int', nullable: true })
  responsavelFamiliarId?: number | null;

  @Column()
  logradouro!: string;

  @Column()
  numero!: string;

  @Column()
  bairro!: string;

  @Column()
  cidade!: string;

  @Column({ name: 'uf', length: 2 })
  uf!: string;

  @Column()
  cep!: string;

  @Column({ nullable: true })
  complemento?: string;

  @CreateDateColumn({ name: 'criado_em' })
  criadoEm!: Date;

  @OneToMany(() => FamilyMember, (member) => member.familia, { cascade: true })
  membros?: FamilyMember[];
}
