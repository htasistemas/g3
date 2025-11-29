import { Column, Entity, PrimaryGeneratedColumn } from 'typeorm';

@Entity('config_documentos_beneficiario')
export class BeneficiaryDocumentConfig {
  @PrimaryGeneratedColumn()
  id!: number;

  @Column({ name: 'nome', unique: true })
  nome!: string;

  @Column({ name: 'obrigatorio', default: false })
  obrigatorio!: boolean;
}
