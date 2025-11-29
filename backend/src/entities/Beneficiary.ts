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

  @Column({ default: false })
  hasMinorChildren!: boolean;

  @Column({ default: false })
  hasDriverLicense!: boolean;

  @Column({ type: 'int', nullable: true })
  minorChildrenCount?: number;

  @Column({ nullable: true })
  educationLevel?: string;

  @Column({ type: 'decimal', precision: 12, scale: 2, nullable: true })
  individualIncome?: number;

  @Column({ type: 'decimal', precision: 12, scale: 2, nullable: true })
  familyIncome?: number;

  @Column({ type: 'text', nullable: true })
  housingInformation?: string;

  @Column({ type: 'text', nullable: true })
  sanitationConditions?: string;

  @Column({ nullable: true })
  employmentStatus?: string;

  @Column({ nullable: true })
  occupation?: string;

  @Column({ type: 'simple-json', nullable: true })
  documents?: DocumentAttachment[];

  @CreateDateColumn()
  createdAt!: Date;
}
