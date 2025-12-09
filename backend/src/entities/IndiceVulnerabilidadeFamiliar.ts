import {
  Column,
  CreateDateColumn,
  Entity,
  JoinColumn,
  ManyToOne,
  PrimaryGeneratedColumn
} from 'typeorm';
import { Beneficiario } from './Beneficiario';

export type FaixaVulnerabilidade = 'Baixa' | 'Média' | 'Alta' | 'Crítica';

@Entity('indice_vulnerabilidade_familiar')
export class IndiceVulnerabilidadeFamiliar {
  @PrimaryGeneratedColumn('uuid', { name: 'id' })
  id!: string;

  @Column({ name: 'id_beneficiario', type: 'uuid' })
  idBeneficiario!: string;

  @ManyToOne(() => Beneficiario)
  @JoinColumn({ name: 'id_beneficiario', referencedColumnName: 'idBeneficiario' })
  beneficiario?: Beneficiario;

  @Column({ name: 'pontuacao_total', type: 'int' })
  pontuacaoTotal!: number;

  @Column({ name: 'faixa_vulnerabilidade' })
  faixaVulnerabilidade!: FaixaVulnerabilidade;

  @CreateDateColumn({ name: 'data_calculo' })
  dataCalculo!: Date;
}
