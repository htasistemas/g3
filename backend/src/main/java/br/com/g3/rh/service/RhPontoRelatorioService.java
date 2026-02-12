package br.com.g3.rh.service;

public interface RhPontoRelatorioService {
  byte[] gerarRelatorioEspelhoMensal(Integer mes, Integer ano, Long funcionarioId, Long usuarioId);

  byte[] gerarRelacaoColaboradores(Long usuarioId);

  byte[] gerarRelatorioConfiguracao(Long usuarioId);
}
