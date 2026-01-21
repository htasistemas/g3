CREATE INDEX IF NOT EXISTS idx_fila_status_data ON fila_atendimento (status_fila, data_hora_entrada);
CREATE INDEX IF NOT EXISTS idx_chamada_data ON chamada (data_hora_chamada DESC);
CREATE INDEX IF NOT EXISTS idx_chamada_local_data ON chamada (local_atendimento, data_hora_chamada DESC);
