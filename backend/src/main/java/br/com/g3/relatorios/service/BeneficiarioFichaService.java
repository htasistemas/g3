package br.com.g3.relatorios.service;

import br.com.g3.relatorios.dto.BeneficiarioFichaRequest;

public interface BeneficiarioFichaService {
  byte[] gerarPdf(BeneficiarioFichaRequest request);
}
