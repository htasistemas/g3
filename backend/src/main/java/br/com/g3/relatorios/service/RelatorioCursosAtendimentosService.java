package br.com.g3.relatorios.service;

import br.com.g3.relatorios.dto.CursoAtendimentoRelacaoRequest;

public interface RelatorioCursosAtendimentosService {
  byte[] gerarPdf(CursoAtendimentoRelacaoRequest request);
}
