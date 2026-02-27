package br.com.g3.relatorios.service;

import br.com.g3.relatorios.dto.RelatorioContasAPagarRequest;
import br.com.g3.relatorios.dto.RelatorioContasAReceberRequest;
import br.com.g3.relatorios.dto.RelatorioContasBancariasRequest;
import br.com.g3.relatorios.dto.RelatorioExtratoMensalRequest;

public interface RelatorioContabilidadeService {
  byte[] gerarExtratoMensal(RelatorioExtratoMensalRequest request);

  byte[] gerarContasAReceber(RelatorioContasAReceberRequest request);

  byte[] gerarContasAPagar(RelatorioContasAPagarRequest request);

  byte[] gerarContasBancarias(RelatorioContasBancariasRequest request);
}
