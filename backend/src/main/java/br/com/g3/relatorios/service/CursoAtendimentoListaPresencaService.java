package br.com.g3.relatorios.service;

import br.com.g3.relatorios.dto.CursoAtendimentoListaPresencaRequest;

public interface CursoAtendimentoListaPresencaService {
  byte[] gerarPdf(CursoAtendimentoListaPresencaRequest request);
}
