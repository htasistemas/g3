-- Schema bootstrap for MySQL
-- Script alinhado com as entidades TypeORM usadas nas telas do G3
-- Execute em uma base vazia ou existente para garantir as tabelas necessárias

-- Usuários do sistema
CREATE TABLE IF NOT EXISTS usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome_usuario VARCHAR(150) NOT NULL UNIQUE,
  hash_senha TEXT NOT NULL,
  criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  atualizado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Configuração de documentos obrigatórios
CREATE TABLE IF NOT EXISTS config_documentos_beneficiario (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome VARCHAR(255) NOT NULL UNIQUE,
  obrigatorio TINYINT(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Unidades assistenciais
CREATE TABLE IF NOT EXISTS unidades_assistenciais (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nome_fantasia VARCHAR(255) UNIQUE,
  razao_social VARCHAR(255),
  cnpj VARCHAR(255),
  telefone VARCHAR(255),
  email VARCHAR(255),
  cep VARCHAR(20),
  endereco VARCHAR(255),
  numero_endereco VARCHAR(50),
  bairro VARCHAR(255),
  cidade VARCHAR(255),
  estado VARCHAR(50),
  observacoes TEXT,
  logomarca LONGTEXT,
  logomarca_relatorio LONGTEXT,
  horario_funcionamento VARCHAR(255),
  responsavel_nome VARCHAR(255),
  responsavel_cpf VARCHAR(50),
  responsavel_periodo_mandato VARCHAR(255),
  criado_em DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Beneficiários
CREATE TABLE IF NOT EXISTS beneficiario (
  id_beneficiario CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  codigo VARCHAR(32) UNIQUE,
  nome_completo VARCHAR(255) NOT NULL,
  nome_social VARCHAR(255),
  apelido VARCHAR(255),
  data_nascimento DATE NOT NULL,
  sexo_biologico VARCHAR(255),
  identidade_genero VARCHAR(255),
  cor_raca VARCHAR(255),
  status VARCHAR(255) NOT NULL DEFAULT 'INCOMPLETO',
  motivo_bloqueio TEXT,
  foto_3x4 LONGTEXT,
  documentos_obrigatorios JSON,
  estado_civil VARCHAR(255),
  nacionalidade VARCHAR(255),
  naturalidade_cidade VARCHAR(255),
  naturalidade_uf CHAR(2),
  nome_mae VARCHAR(255) NOT NULL,
  nome_pai VARCHAR(255),
  cpf VARCHAR(11) UNIQUE,
  rg_numero VARCHAR(255),
  rg_orgao_emissor VARCHAR(255),
  rg_uf CHAR(2),
  rg_data_emissao DATE,
  nis VARCHAR(255),
  certidao_tipo VARCHAR(255),
  certidao_livro VARCHAR(255),
  certidao_folha VARCHAR(255),
  certidao_termo VARCHAR(255),
  certidao_cartorio VARCHAR(255),
  certidao_municipio VARCHAR(255),
  certidao_uf CHAR(2),
  titulo_eleitor VARCHAR(255),
  cnh VARCHAR(255),
  cartao_sus VARCHAR(255),
  telefone_principal VARCHAR(255),
  telefone_principal_whatsapp TINYINT(1) NOT NULL DEFAULT 0,
  telefone_secundario VARCHAR(255),
  telefone_recado_nome VARCHAR(255),
  telefone_recado_numero VARCHAR(255),
  email VARCHAR(255),
  permite_contato_tel TINYINT(1) NOT NULL DEFAULT 1,
  permite_contato_whatsapp TINYINT(1) NOT NULL DEFAULT 1,
  permite_contato_sms TINYINT(1) NOT NULL DEFAULT 0,
  permite_contato_email TINYINT(1) NOT NULL DEFAULT 0,
  horario_preferencial_contato VARCHAR(255),
  usa_endereco_familia TINYINT(1) NOT NULL DEFAULT 1,
  cep VARCHAR(20),
  logradouro VARCHAR(255),
  numero VARCHAR(50),
  complemento VARCHAR(255),
  bairro VARCHAR(255),
  ponto_referencia VARCHAR(255),
  municipio VARCHAR(255),
  uf CHAR(2),
  zona VARCHAR(255),
  situacao_imovel VARCHAR(255),
  tipo_moradia VARCHAR(255),
  agua_encanada TINYINT(1) NOT NULL DEFAULT 0,
  esgoto_tipo VARCHAR(255),
  coleta_lixo VARCHAR(255),
  energia_eletrica TINYINT(1) NOT NULL DEFAULT 0,
  internet TINYINT(1) NOT NULL DEFAULT 0,
  mora_com_familia TINYINT(1) NOT NULL DEFAULT 0,
  responsavel_legal TINYINT(1) NOT NULL DEFAULT 0,
  vinculo_familiar VARCHAR(255),
  situacao_vulnerabilidade TEXT,
  sabe_ler_escrever TINYINT(1) NOT NULL DEFAULT 0,
  nivel_escolaridade VARCHAR(255),
  estuda_atualmente TINYINT(1) NOT NULL DEFAULT 0,
  ocupacao VARCHAR(255),
  situacao_trabalho VARCHAR(255),
  local_trabalho VARCHAR(255),
  renda_mensal DECIMAL(12,2),
  fonte_renda VARCHAR(255),
  possui_deficiencia TINYINT(1) NOT NULL DEFAULT 0,
  tipo_deficiencia VARCHAR(255),
  cid_principal VARCHAR(255),
  usa_medicacao_continua TINYINT(1) NOT NULL DEFAULT 0,
  descricao_medicacao TEXT,
  servico_saude_referencia VARCHAR(255),
  recebe_beneficio TINYINT(1) NOT NULL DEFAULT 0,
  beneficios_descricao TEXT,
  valor_total_beneficios DECIMAL(12,2),
  aceite_lgpd TINYINT(1) NOT NULL DEFAULT 0,
  data_aceite_lgpd DATETIME,
  observacoes TEXT,
  data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_atualizacao DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP INDEX IF EXISTS idx_beneficiario_nome ON beneficiario;
CREATE INDEX idx_beneficiario_nome ON beneficiario (nome_completo);
DROP INDEX IF EXISTS idx_beneficiario_cpf ON beneficiario;
CREATE INDEX idx_beneficiario_cpf ON beneficiario (cpf);
DROP INDEX IF EXISTS idx_beneficiario_nis ON beneficiario;
CREATE INDEX idx_beneficiario_nis ON beneficiario (nis);

-- Famílias
CREATE TABLE IF NOT EXISTS familia (
  id_familia CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  nome_familia VARCHAR(255) NOT NULL,
  id_referencia_familiar CHAR(36),
  cep VARCHAR(20),
  logradouro VARCHAR(255),
  numero VARCHAR(50),
  complemento VARCHAR(255),
  bairro VARCHAR(255),
  ponto_referencia VARCHAR(255),
  municipio VARCHAR(255),
  uf CHAR(2),
  zona VARCHAR(255),
  situacao_imovel VARCHAR(255),
  tipo_moradia VARCHAR(255),
  agua_encanada TINYINT(1) NOT NULL DEFAULT 0,
  esgoto_tipo VARCHAR(255),
  coleta_lixo VARCHAR(255),
  energia_eletrica TINYINT(1) NOT NULL DEFAULT 0,
  internet TINYINT(1) NOT NULL DEFAULT 0,
  arranjo_familiar VARCHAR(255),
  qtd_membros INT NOT NULL DEFAULT 0,
  qtd_criancas INT NOT NULL DEFAULT 0,
  qtd_adolescentes INT NOT NULL DEFAULT 0,
  qtd_idosos INT NOT NULL DEFAULT 0,
  qtd_pessoas_deficiencia INT NOT NULL DEFAULT 0,
  renda_familiar_total DECIMAL(12,2) NOT NULL DEFAULT 0,
  renda_per_capita DECIMAL(12,2) NOT NULL DEFAULT 0,
  faixa_renda_per_capita VARCHAR(255),
  principais_fontes_renda TEXT,
  situacao_inseguranca_alimentar VARCHAR(255),
  possui_dividas_relevantes TINYINT(1) NOT NULL DEFAULT 0,
  descricao_dividas TEXT,
  vulnerabilidades_familia TEXT,
  servicos_acompanhamento TEXT,
  tecnico_responsavel VARCHAR(255),
  periodicidade_atendimento VARCHAR(255),
  proxima_visita_prevista DATE,
  observacoes TEXT,
  data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_atualizacao DATETIME,
  CONSTRAINT fk_familia_referencia_familiar FOREIGN KEY (id_referencia_familiar)
    REFERENCES beneficiario(id_beneficiario) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Membros de família
CREATE TABLE IF NOT EXISTS familia_membro (
  id_familia_membro CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  id_familia CHAR(36) NOT NULL,
  id_beneficiario CHAR(36) NOT NULL,
  parentesco VARCHAR(255) NOT NULL,
  responsavel_familiar TINYINT(1) NOT NULL DEFAULT 0,
  contribui_renda TINYINT(1) NOT NULL DEFAULT 0,
  renda_individual DECIMAL(12,2),
  participa_servicos TINYINT(1) NOT NULL DEFAULT 0,
  observacoes TEXT,
  data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  data_atualizacao DATETIME,
  CONSTRAINT uk_familia_membro_beneficiario UNIQUE (id_familia, id_beneficiario),
  CONSTRAINT fk_familia_membro_familia FOREIGN KEY (id_familia) REFERENCES familia(id_familia) ON DELETE CASCADE,
  CONSTRAINT fk_familia_membro_beneficiario FOREIGN KEY (id_beneficiario) REFERENCES beneficiario(id_beneficiario) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Patrimônio
CREATE TABLE IF NOT EXISTS patrimonios (
  id_patrimonio CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  numero_patrimonio VARCHAR(255) NOT NULL UNIQUE,
  nome VARCHAR(255) NOT NULL,
  categoria VARCHAR(255),
  subcategoria VARCHAR(255),
  conservacao VARCHAR(255),
  status VARCHAR(255),
  data_aquisicao DATE,
  valor_aquisicao DECIMAL(12,2),
  origem VARCHAR(255),
  responsavel VARCHAR(255),
  unidade VARCHAR(255),
  sala VARCHAR(255),
  taxa_depreciacao DECIMAL(5,2),
  observacoes TEXT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS patrimonio_movimentos (
  id_movimento CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  tipo VARCHAR(255) NOT NULL,
  destino VARCHAR(255),
  responsavel VARCHAR(255),
  observacao TEXT,
  data_movimento DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  patrimonio_id CHAR(36) NOT NULL,
  CONSTRAINT fk_patrimonio_movimento_patrimonio FOREIGN KEY (patrimonio_id) REFERENCES patrimonios(id_patrimonio) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Termos de fomento
CREATE TABLE IF NOT EXISTS termos_fomento (
  id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  numero VARCHAR(255) NOT NULL UNIQUE,
  objeto TEXT,
  orgao_concedente VARCHAR(255),
  modalidade VARCHAR(255),
  vigencia_inicio DATE,
  vigencia_fim DATE,
  valor_global DECIMAL(14,2),
  status VARCHAR(255),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Plano de trabalho
CREATE TABLE IF NOT EXISTS planos_trabalho (
  id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  codigo_interno VARCHAR(255) NOT NULL UNIQUE,
  titulo VARCHAR(255) NOT NULL,
  descricao_geral TEXT NOT NULL,
  status VARCHAR(255) NOT NULL DEFAULT 'EM_ELABORACAO',
  orgao_concedente VARCHAR(255),
  orgao_outro_descricao VARCHAR(255),
  area_programa VARCHAR(255),
  data_elaboracao DATE,
  data_aprovacao DATE,
  vigencia_inicio DATE,
  vigencia_fim DATE,
  numero_processo VARCHAR(255),
  modalidade VARCHAR(255),
  observacoes_vinculacao TEXT,
  termo_fomento_id CHAR(36) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_plano_trabalho_termo FOREIGN KEY (termo_fomento_id) REFERENCES termos_fomento(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Metas do plano
CREATE TABLE IF NOT EXISTS planos_meta (
  id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  plano_id CHAR(36) NOT NULL,
  titulo VARCHAR(255) NOT NULL,
  descricao TEXT,
  indicador VARCHAR(255),
  linha_base VARCHAR(255),
  meta_ate_final VARCHAR(255),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_plano_meta_plano FOREIGN KEY (plano_id) REFERENCES planos_trabalho(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Atividades do plano
CREATE TABLE IF NOT EXISTS planos_atividade (
  id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  meta_id CHAR(36) NOT NULL,
  descricao TEXT NOT NULL,
  publico_alvo VARCHAR(255),
  local_execucao VARCHAR(255),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_plano_atividade_meta FOREIGN KEY (meta_id) REFERENCES planos_meta(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Etapas do plano
CREATE TABLE IF NOT EXISTS planos_etapa (
  id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  atividade_id CHAR(36) NOT NULL,
  descricao TEXT NOT NULL,
  data_inicio DATE,
  data_fim DATE,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_plano_etapa_atividade FOREIGN KEY (atividade_id) REFERENCES planos_atividade(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Cronograma
CREATE TABLE IF NOT EXISTS planos_cronograma_item (
  id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  plano_id CHAR(36) NOT NULL,
  etapa_id CHAR(36),
  descricao TEXT NOT NULL,
  mes INT,
  valor_previsto DECIMAL(14,2),
  valor_executado DECIMAL(14,2),
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_plano_cronograma_plano FOREIGN KEY (plano_id) REFERENCES planos_trabalho(id) ON DELETE CASCADE,
  CONSTRAINT fk_plano_cronograma_etapa FOREIGN KEY (etapa_id) REFERENCES planos_etapa(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Equipe
CREATE TABLE IF NOT EXISTS planos_equipe (
  id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  plano_id CHAR(36) NOT NULL,
  nome VARCHAR(255) NOT NULL,
  papel VARCHAR(255),
  carga_horaria_semanal INT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_plano_equipe_plano FOREIGN KEY (plano_id) REFERENCES planos_trabalho(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Cursos e atendimentos
CREATE TABLE IF NOT EXISTS cursos_atendimento (
  id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  nome VARCHAR(255) NOT NULL,
  descricao TEXT,
  carga_horaria INT,
  data_inicio DATE,
  data_fim DATE,
  local VARCHAR(255),
  publico_alvo VARCHAR(255),
  certificado_modelo LONGTEXT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Salas
CREATE TABLE IF NOT EXISTS salas (
  id CHAR(36) PRIMARY KEY DEFAULT (UUID()),
  nome VARCHAR(255) NOT NULL,
  capacidade INT,
  observacoes TEXT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
