package br.com.g3.relatorios.service;

import br.com.g3.relatorios.dto.AutorizacaoCompraSolicitacaoRequest;

public interface RelatorioSolicitacaoComprasService {
  byte[] gerarPdf(AutorizacaoCompraSolicitacaoRequest request);
}
