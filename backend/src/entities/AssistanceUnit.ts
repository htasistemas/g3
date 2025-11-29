import { Column, CreateDateColumn, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity('unidades_assistenciais')
export class AssistanceUnit {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column({ name: 'nome', unique: true })
  nome!: string;

  @Column({ name: 'telefone', nullable: true })
  telefone?: string;

  @Column({ name: 'email', nullable: true })
  email?: string;

  @Column({ name: 'cep', nullable: true })
  cep?: string;

  @Column({ name: 'endereco', nullable: true })
  endereco?: string;

  @Column({ name: 'numero_endereco', nullable: true })
  numeroEndereco?: string;

  @Column({ name: 'bairro', nullable: true })
  bairro?: string;

  @Column({ name: 'cidade', nullable: true })
  cidade?: string;

  @Column({ name: 'estado', nullable: true })
  estado?: string;

  @Column({ name: 'observacoes', type: 'text', nullable: true })
  observacoes?: string;

  @CreateDateColumn({ name: 'criado_em' })
  criadoEm!: Date;
}
