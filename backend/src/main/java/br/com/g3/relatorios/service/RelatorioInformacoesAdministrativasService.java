package br.com.g3.relatorios.service;

import br.com.g3.relatorios.dto.InformacaoAdministrativaRelatorioRequest;

public interface RelatorioInformacoesAdministrativasService {
  byte[] gerarPdf(InformacaoAdministrativaRelatorioRequest request);
}
