import { Column, CreateDateColumn, Entity, PrimaryGeneratedColumn } from 'typeorm';

interface DocumentAttachment {
  name: string;
  fileName?: string;
}

@Entity('beneficiaries')
export class Beneficiary {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column()
  fullName!: string;

  @Column({ nullable: true })
  motherName?: string;

  @Column()
  document!: string;

  @Column({ type: 'date' })
  birthDate!: string;

  @Column({ type: 'int', nullable: true })
  age?: number;

  @Column()
  phone!: string;

  @Column()
  email!: string;

  @Column()
  zipCode!: string;

  @Column()
  address!: string;

  @Column({ nullable: true })
  addressNumber?: string;

  @Column({ nullable: true })
  referencePoint?: string;

  @Column({ nullable: true })
  neighborhood?: string;

  @Column({ nullable: true })
  city?: string;

  @Column({ nullable: true })
  state?: string;

  @Column({ nullable: true })
  notes?: string;

  @Column({ default: 'Ativo' })
  status!: string;

  @Column({ type: 'simple-json', nullable: true })
  documents?: DocumentAttachment[];

  @CreateDateColumn()
  createdAt!: Date;
}
