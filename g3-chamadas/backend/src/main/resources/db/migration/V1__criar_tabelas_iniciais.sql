CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS beneficiario (
  id_beneficiario BIGSERIAL PRIMARY KEY,
  nome_beneficiario VARCHAR(200) NOT NULL,
  documento VARCHAR(50),
  ativo BOOLEAN NOT NULL DEFAULT TRUE,
  data_hora_criacao TIMESTAMP NOT NULL DEFAULT NOW(),
  data_hora_atualizacao TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS fila_atendimento (
  id_fila_atendimento BIGSERIAL PRIMARY KEY,
  id_beneficiario BIGINT NOT NULL REFERENCES beneficiario(id_beneficiario),
  status_fila VARCHAR(20) NOT NULL,
  prioridade INTEGER NOT NULL DEFAULT 0,
  data_hora_entrada TIMESTAMP NOT NULL DEFAULT NOW(),
  data_hora_atualizacao TIMESTAMP NOT NULL DEFAULT NOW(),
  CONSTRAINT chk_status_fila CHECK (status_fila IN ('AGUARDANDO', 'CHAMADO', 'FINALIZADO'))
);

CREATE TABLE IF NOT EXISTS chamada (
  id_chamada UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  id_fila_atendimento BIGINT NOT NULL REFERENCES fila_atendimento(id_fila_atendimento),
  nome_beneficiario VARCHAR(200) NOT NULL,
  local_atendimento VARCHAR(80) NOT NULL,
  status_chamada VARCHAR(20) NOT NULL,
  data_hora_chamada TIMESTAMP NOT NULL DEFAULT NOW(),
  chamado_por VARCHAR(120),
  data_hora_criacao TIMESTAMP NOT NULL DEFAULT NOW(),
  CONSTRAINT chk_status_chamada CHECK (status_chamada IN ('CHAMADO', 'CANCELADO', 'FINALIZADO'))
);
