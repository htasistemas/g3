package br.com.g3.relatorios.service;

import br.com.g3.relatorios.dto.CursoAtendimentoFichaRequest;

public interface CursoAtendimentoFichaService {
  byte[] gerarPdf(CursoAtendimentoFichaRequest request);
}
