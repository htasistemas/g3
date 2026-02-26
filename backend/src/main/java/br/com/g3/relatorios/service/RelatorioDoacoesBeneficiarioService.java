package br.com.g3.relatorios.service;

import br.com.g3.relatorios.dto.DoacaoBeneficiarioRelatorioRequest;

public interface RelatorioDoacoesBeneficiarioService {
  byte[] gerarPdf(DoacaoBeneficiarioRelatorioRequest request);
}
