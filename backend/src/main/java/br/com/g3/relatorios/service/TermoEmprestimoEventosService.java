package br.com.g3.relatorios.service;

import br.com.g3.relatorios.dto.EmprestimoEventoRelatorioRequest;

public interface TermoEmprestimoEventosService {
  byte[] gerarPdf(EmprestimoEventoRelatorioRequest requisicao);
}
