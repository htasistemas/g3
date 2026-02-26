package br.com.g3.relatorios.service;

import br.com.g3.relatorios.dto.DoacaoPlanejadaRelatorioRequest;

public interface RelatorioDoacoesPlanejadasService {
  byte[] gerarPdf(DoacaoPlanejadaRelatorioRequest request);
}
