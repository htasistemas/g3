import { Column, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity('config_documentos_beneficiario')
export class BeneficiaryDocumentConfig {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column({ unique: true })
  name!: string;

  @Column({ default: false })
  required!: boolean;
}
