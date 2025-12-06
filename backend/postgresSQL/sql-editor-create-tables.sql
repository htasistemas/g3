-- Schema bootstrap for PostgreSQL
-- Generated from the current TypeORM entities so logins and registrations land in Postgres.

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Usuários do sistema
CREATE TABLE IF NOT EXISTS usuarios (
  id SERIAL PRIMARY KEY,
  nome_usuario VARCHAR(150) NOT NULL UNIQUE,
  hash_senha TEXT NOT NULL,
  criado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  atualizado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Configuração de documentos obrigatórios
CREATE TABLE IF NOT EXISTS config_documentos_beneficiario (
  id SERIAL PRIMARY KEY,
  nome VARCHAR NOT NULL UNIQUE,
  obrigatorio BOOLEAN NOT NULL DEFAULT FALSE
);

-- Unidades assistenciais
CREATE TABLE IF NOT EXISTS unidades_assistenciais (
  id SERIAL PRIMARY KEY,
  nome_fantasia VARCHAR UNIQUE,
  razao_social VARCHAR,
  cnpj VARCHAR,
  telefone VARCHAR,
  email VARCHAR,
  cep VARCHAR,
  endereco VARCHAR,
  numero_endereco VARCHAR,
  bairro VARCHAR,
  cidade VARCHAR,
  estado VARCHAR,
  observacoes TEXT,
  logomarca TEXT,
  logomarca_relatorio TEXT,
  horario_funcionamento VARCHAR,
  responsavel_nome VARCHAR,
  responsavel_cpf VARCHAR,
  responsavel_periodo_mandato VARCHAR,
  criado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Beneficiários
CREATE TABLE IF NOT EXISTS beneficiario (
  id_beneficiario UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  codigo VARCHAR(32) UNIQUE,
  nome_completo VARCHAR NOT NULL,
  nome_social VARCHAR,
  apelido VARCHAR,
  data_nascimento DATE NOT NULL,
  sexo_biologico VARCHAR,
  identidade_genero VARCHAR,
  cor_raca VARCHAR,
  status VARCHAR NOT NULL DEFAULT 'INCOMPLETO',
  motivo_bloqueio TEXT,
  foto_3x4 TEXT,
  documentos_obrigatorios JSONB,
  estado_civil VARCHAR,
  nacionalidade VARCHAR,
  naturalidade_cidade VARCHAR,
  naturalidade_uf CHAR(2),
  nome_mae VARCHAR NOT NULL,
  nome_pai VARCHAR,
  cpf VARCHAR(11) UNIQUE,
  rg_numero VARCHAR,
  rg_orgao_emissor VARCHAR,
  rg_uf CHAR(2),
  rg_data_emissao DATE,
  nis VARCHAR,
  certidao_tipo VARCHAR,
  certidao_livro VARCHAR,
  certidao_folha VARCHAR,
  certidao_termo VARCHAR,
  certidao_cartorio VARCHAR,
  certidao_municipio VARCHAR,
  certidao_uf CHAR(2),
  titulo_eleitor VARCHAR,
  cnh VARCHAR,
  cartao_sus VARCHAR,
  telefone_principal VARCHAR,
  telefone_principal_whatsapp BOOLEAN NOT NULL DEFAULT FALSE,
  telefone_secundario VARCHAR,
  telefone_recado_nome VARCHAR,
  telefone_recado_numero VARCHAR,
  email VARCHAR,
  permite_contato_tel BOOLEAN NOT NULL DEFAULT TRUE,
  permite_contato_whatsapp BOOLEAN NOT NULL DEFAULT TRUE,
  permite_contato_sms BOOLEAN NOT NULL DEFAULT FALSE,
  permite_contato_email BOOLEAN NOT NULL DEFAULT FALSE,
  horario_preferencial_contato VARCHAR,
  usa_endereco_familia BOOLEAN NOT NULL DEFAULT TRUE,
  cep VARCHAR,
  logradouro VARCHAR,
  numero VARCHAR,
  complemento VARCHAR,
  bairro VARCHAR,
  ponto_referencia VARCHAR,
  municipio VARCHAR,
  uf CHAR(2),
  zona VARCHAR,
  situacao_imovel VARCHAR,
  tipo_moradia VARCHAR,
  agua_encanada BOOLEAN NOT NULL DEFAULT FALSE,
  esgoto_tipo VARCHAR,
  coleta_lixo VARCHAR,
  energia_eletrica BOOLEAN NOT NULL DEFAULT FALSE,
  internet BOOLEAN NOT NULL DEFAULT FALSE,
  mora_com_familia BOOLEAN NOT NULL DEFAULT FALSE,
  responsavel_legal BOOLEAN NOT NULL DEFAULT FALSE,
  vinculo_familiar VARCHAR,
  situacao_vulnerabilidade TEXT,
  sabe_ler_escrever BOOLEAN NOT NULL DEFAULT FALSE,
  nivel_escolaridade VARCHAR,
  estuda_atualmente BOOLEAN NOT NULL DEFAULT FALSE,
  ocupacao VARCHAR,
  situacao_trabalho VARCHAR,
  local_trabalho VARCHAR,
  renda_mensal NUMERIC(12,2),
  fonte_renda VARCHAR,
  possui_deficiencia BOOLEAN NOT NULL DEFAULT FALSE,
  tipo_deficiencia VARCHAR,
  cid_principal VARCHAR,
  usa_medicacao_continua BOOLEAN NOT NULL DEFAULT FALSE,
  descricao_medicacao TEXT,
  servico_saude_referencia VARCHAR,
  recebe_beneficio BOOLEAN NOT NULL DEFAULT FALSE,
  beneficios_descricao TEXT,
  valor_total_beneficios NUMERIC(12,2),
  aceite_lgpd BOOLEAN NOT NULL DEFAULT FALSE,
  data_aceite_lgpd TIMESTAMP,
  observacoes TEXT,
  data_cadastro TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_atualizacao TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_beneficiario_nome ON beneficiario (nome_completo);
CREATE INDEX IF NOT EXISTS idx_beneficiario_cpf ON beneficiario (cpf);
CREATE INDEX IF NOT EXISTS idx_beneficiario_nis ON beneficiario (nis);

-- Famílias
CREATE TABLE IF NOT EXISTS familia (
  id_familia UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  nome_familia VARCHAR NOT NULL,
  id_referencia_familiar UUID,
  cep VARCHAR,
  logradouro VARCHAR,
  numero VARCHAR,
  complemento VARCHAR,
  bairro VARCHAR,
  ponto_referencia VARCHAR,
  municipio VARCHAR,
  uf CHAR(2),
  zona VARCHAR,
  situacao_imovel VARCHAR,
  tipo_moradia VARCHAR,
  agua_encanada BOOLEAN NOT NULL DEFAULT FALSE,
  esgoto_tipo VARCHAR,
  coleta_lixo VARCHAR,
  energia_eletrica BOOLEAN NOT NULL DEFAULT FALSE,
  internet BOOLEAN NOT NULL DEFAULT FALSE,
  arranjo_familiar VARCHAR,
  qtd_membros INT NOT NULL DEFAULT 0,
  qtd_criancas INT NOT NULL DEFAULT 0,
  qtd_adolescentes INT NOT NULL DEFAULT 0,
  qtd_idosos INT NOT NULL DEFAULT 0,
  qtd_pessoas_deficiencia INT NOT NULL DEFAULT 0,
  renda_familiar_total NUMERIC(12,2) NOT NULL DEFAULT 0,
  renda_per_capita NUMERIC(12,2) NOT NULL DEFAULT 0,
  faixa_renda_per_capita VARCHAR,
  principais_fontes_renda TEXT,
  situacao_inseguranca_alimentar VARCHAR,
  possui_dividas_relevantes BOOLEAN NOT NULL DEFAULT FALSE,
  descricao_dividas TEXT,
  vulnerabilidades_familia TEXT,
  servicos_acompanhamento TEXT,
  tecnico_responsavel VARCHAR,
  periodicidade_atendimento VARCHAR,
  proxima_visita_prevista DATE,
  observacoes TEXT,
  data_cadastro TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_atualizacao TIMESTAMP
);

ALTER TABLE familia
  ADD CONSTRAINT fk_familia_referencia_familiar FOREIGN KEY (id_referencia_familiar)
    REFERENCES beneficiario(id_beneficiario) ON DELETE SET NULL;

-- Membros de família
CREATE TABLE IF NOT EXISTS familia_membro (
  id_familia_membro UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  id_familia UUID NOT NULL,
  id_beneficiario UUID NOT NULL,
  parentesco VARCHAR NOT NULL,
  responsavel_familiar BOOLEAN NOT NULL DEFAULT FALSE,
  contribui_renda BOOLEAN NOT NULL DEFAULT FALSE,
  renda_individual NUMERIC(12,2),
  participa_servicos BOOLEAN NOT NULL DEFAULT FALSE,
  observacoes TEXT,
  data_cadastro TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_atualizacao TIMESTAMP,
  CONSTRAINT uk_familia_membro_beneficiario UNIQUE (id_familia, id_beneficiario)
);

ALTER TABLE familia_membro
  ADD CONSTRAINT fk_familia_membro_familia FOREIGN KEY (id_familia)
    REFERENCES familia(id_familia) ON DELETE CASCADE;
ALTER TABLE familia_membro
  ADD CONSTRAINT fk_familia_membro_beneficiario FOREIGN KEY (id_beneficiario)
    REFERENCES beneficiario(id_beneficiario) ON DELETE CASCADE;

-- Patrimônio
CREATE TABLE IF NOT EXISTS patrimonios (
  id_patrimonio UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  numero_patrimonio VARCHAR NOT NULL UNIQUE,
  nome VARCHAR NOT NULL,
  categoria VARCHAR,
  subcategoria VARCHAR,
  conservacao VARCHAR,
  status VARCHAR,
  data_aquisicao DATE,
  valor_aquisicao DECIMAL(12,2),
  origem VARCHAR,
  responsavel VARCHAR,
  unidade VARCHAR,
  sala VARCHAR,
  taxa_depreciacao DECIMAL(5,2),
  observacoes TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS patrimonio_movimentos (
  id_movimento UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  tipo VARCHAR NOT NULL,
  destino VARCHAR,
  responsavel VARCHAR,
  observacao TEXT,
  data_movimento TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  patrimonio_id UUID NOT NULL
);

ALTER TABLE patrimonio_movimentos
  ADD CONSTRAINT fk_patrimonio_movimento_patrimonio FOREIGN KEY (patrimonio_id)
    REFERENCES patrimonios(id_patrimonio) ON DELETE CASCADE;

-- Termos de fomento
CREATE TABLE IF NOT EXISTS termos_fomento (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  numero VARCHAR NOT NULL UNIQUE,
  objeto TEXT,
  orgao_concedente VARCHAR,
  modalidade VARCHAR,
  vigencia_inicio DATE,
  vigencia_fim DATE,
  valor_global DECIMAL(14,2),
  status VARCHAR,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Plano de trabalho
CREATE TABLE IF NOT EXISTS planos_trabalho (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  codigo_interno VARCHAR NOT NULL UNIQUE,
  titulo VARCHAR NOT NULL,
  descricao_geral TEXT NOT NULL,
  status VARCHAR NOT NULL DEFAULT 'EM_ELABORACAO',
  orgao_concedente VARCHAR,
  orgao_outro_descricao VARCHAR,
  area_programa VARCHAR,
  data_elaboracao DATE,
  data_aprovacao DATE,
  vigencia_inicio DATE,
  vigencia_fim DATE,
  numero_processo VARCHAR,
  modalidade VARCHAR,
  observacoes_vinculacao TEXT,
  termo_fomento_id UUID NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE planos_trabalho
  ADD CONSTRAINT fk_plano_trabalho_termo FOREIGN KEY (termo_fomento_id)
    REFERENCES termos_fomento(id) ON DELETE CASCADE;

-- Metas do plano
CREATE TABLE IF NOT EXISTS planos_meta (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  plano_id UUID NOT NULL,
  titulo VARCHAR NOT NULL,
  descricao TEXT,
  indicador VARCHAR,
  linha_base VARCHAR,
  meta_ate_final VARCHAR,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE planos_meta
  ADD CONSTRAINT fk_plano_meta_plano FOREIGN KEY (plano_id)
    REFERENCES planos_trabalho(id) ON DELETE CASCADE;

-- Atividades do plano
CREATE TABLE IF NOT EXISTS planos_atividade (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  meta_id UUID NOT NULL,
  descricao TEXT NOT NULL,
  publico_alvo VARCHAR,
  local_execucao VARCHAR,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE planos_atividade
  ADD CONSTRAINT fk_plano_atividade_meta FOREIGN KEY (meta_id)
    REFERENCES planos_meta(id) ON DELETE CASCADE;

-- Etapas do plano
CREATE TABLE IF NOT EXISTS planos_etapa (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  atividade_id UUID NOT NULL,
  descricao TEXT NOT NULL,
  data_inicio DATE,
  data_fim DATE,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE planos_etapa
  ADD CONSTRAINT fk_plano_etapa_atividade FOREIGN KEY (atividade_id)
    REFERENCES planos_atividade(id) ON DELETE CASCADE;

-- Cronograma
CREATE TABLE IF NOT EXISTS planos_cronograma_item (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  plano_id UUID NOT NULL,
  etapa_id UUID,
  descricao TEXT NOT NULL,
  mes INT,
  valor_previsto DECIMAL(14,2),
  valor_executado DECIMAL(14,2),
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE planos_cronograma_item
  ADD CONSTRAINT fk_plano_cronograma_plano FOREIGN KEY (plano_id)
    REFERENCES planos_trabalho(id) ON DELETE CASCADE;
ALTER TABLE planos_cronograma_item
  ADD CONSTRAINT fk_plano_cronograma_etapa FOREIGN KEY (etapa_id)
    REFERENCES planos_etapa(id) ON DELETE SET NULL;

-- Equipe
CREATE TABLE IF NOT EXISTS planos_equipe (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  plano_id UUID NOT NULL,
  nome VARCHAR NOT NULL,
  papel VARCHAR,
  carga_horaria_semanal INT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE planos_equipe
  ADD CONSTRAINT fk_plano_equipe_plano FOREIGN KEY (plano_id)
    REFERENCES planos_trabalho(id) ON DELETE CASCADE;

-- Cursos e atendimentos
CREATE TABLE IF NOT EXISTS cursos_atendimento (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  nome VARCHAR NOT NULL,
  descricao TEXT,
  carga_horaria INT,
  data_inicio DATE,
  data_fim DATE,
  local VARCHAR,
  publico_alvo VARCHAR,
  certificado_modelo TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Salas
CREATE TABLE IF NOT EXISTS salas (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  nome VARCHAR NOT NULL,
  capacidade INT,
  observacoes TEXT,
  created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
