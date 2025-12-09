import {
  Column,
  CreateDateColumn,
  Entity,
  JoinColumn,
  ManyToOne,
  OneToMany,
  PrimaryGeneratedColumn,
  UpdateDateColumn
} from 'typeorm';
import { Beneficiario } from './Beneficiario';
import { ProntuarioAtendimento } from './ProntuarioAtendimento';
import { ProntuarioEncaminhamento } from './ProntuarioEncaminhamento';

export interface ProntuarioIdentificacao {
  unidadeReferencia?: string;
  responsavelTecnico?: string;
  dataAbertura?: string;
  observacoes?: string;
}

export interface ProntuarioComposicaoFamiliar {
  familiaId?: string;
  resumo?: string;
  membros?: Array<{
    id?: string;
    nome?: string;
    parentesco?: string;
    renda?: string | null;
    responsavelFamiliar?: boolean;
  }>;
}

export interface ProntuarioParticipacao {
  servicoOuProjeto?: string;
  papel?: string;
  situacao?: string;
  dataInicio?: string;
  dataFim?: string;
  observacoes?: string;
}

@Entity('prontuarios')
export class Prontuario {
  @PrimaryGeneratedColumn('uuid', { name: 'id_prontuario' })
  idProntuario!: string;

  @Column({ name: 'id_beneficiario', type: 'uuid' })
  beneficiarioId!: string;

  @ManyToOne(() => Beneficiario, { onDelete: 'CASCADE' })
  @JoinColumn({ name: 'id_beneficiario', referencedColumnName: 'idBeneficiario' })
  beneficiario!: Beneficiario;

  @Column({ name: 'identificacao', type: 'jsonb', nullable: true })
  identificacao?: ProntuarioIdentificacao;

  @Column({ name: 'composicao_familiar', type: 'jsonb', nullable: true })
  composicaoFamiliar?: ProntuarioComposicaoFamiliar;

  @Column({ name: 'situacoes_vulnerabilidade', type: 'jsonb', nullable: true })
  situacoesVulnerabilidade?: string[];

  @Column({ name: 'participacoes_servicos', type: 'jsonb', nullable: true })
  participacoesServicos?: ProntuarioParticipacao[];

  @Column({ name: 'parecer_tecnico', type: 'text', nullable: true })
  parecerTecnico?: string;

  @OneToMany(() => ProntuarioAtendimento, (atendimento) => atendimento.prontuario, { cascade: true })
  atendimentos?: ProntuarioAtendimento[];

  @OneToMany(() => ProntuarioEncaminhamento, (encaminhamento) => encaminhamento.prontuario, {
    cascade: true
  })
  encaminhamentos?: ProntuarioEncaminhamento[];

  @CreateDateColumn({ name: 'created_at' })
  createdAt!: Date;

  @UpdateDateColumn({ name: 'updated_at' })
  updatedAt!: Date;
}
