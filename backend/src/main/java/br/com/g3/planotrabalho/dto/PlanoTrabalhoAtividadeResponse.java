package br.com.g3.planotrabalho.dto;

import java.util.List;

public class PlanoTrabalhoAtividadeResponse {
  private final Long id;
  private final String descricao;
  private final String justificativa;
  private final String publicoAlvo;
  private final String localExecucao;
  private final String produtoEsperado;
  private final List<PlanoTrabalhoEtapaResponse> etapas;

  public PlanoTrabalhoAtividadeResponse(
      Long id,
      String descricao,
      String justificativa,
      String publicoAlvo,
      String localExecucao,
      String produtoEsperado,
      List<PlanoTrabalhoEtapaResponse> etapas) {
    this.id = id;
    this.descricao = descricao;
    this.justificativa = justificativa;
    this.publicoAlvo = publicoAlvo;
    this.localExecucao = localExecucao;
    this.produtoEsperado = produtoEsperado;
    this.etapas = etapas;
  }

  public Long getId() {
    return id;
  }

  public String getDescricao() {
    return descricao;
  }

  public String getJustificativa() {
    return justificativa;
  }

  public String getPublicoAlvo() {
    return publicoAlvo;
  }

  public String getLocalExecucao() {
    return localExecucao;
  }

  public String getProdutoEsperado() {
    return produtoEsperado;
  }

  public List<PlanoTrabalhoEtapaResponse> getEtapas() {
    return etapas;
  }
}
