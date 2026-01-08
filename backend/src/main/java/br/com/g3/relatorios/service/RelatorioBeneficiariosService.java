package br.com.g3.relatorios.service;

import br.com.g3.relatorios.dto.BeneficiarioRelacaoRequest;

public interface RelatorioBeneficiariosService {
  byte[] gerarPdf(BeneficiarioRelacaoRequest request);
}
