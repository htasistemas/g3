package br.com.g3.planotrabalho.dto;

import java.util.List;

public class PlanoTrabalhoAtividadeRequest {
  private Long id;
  private String descricao;
  private String justificativa;
  private String publicoAlvo;
  private String localExecucao;
  private String produtoEsperado;
  private List<PlanoTrabalhoEtapaRequest> etapas;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getJustificativa() {
    return justificativa;
  }

  public void setJustificativa(String justificativa) {
    this.justificativa = justificativa;
  }

  public String getPublicoAlvo() {
    return publicoAlvo;
  }

  public void setPublicoAlvo(String publicoAlvo) {
    this.publicoAlvo = publicoAlvo;
  }

  public String getLocalExecucao() {
    return localExecucao;
  }

  public void setLocalExecucao(String localExecucao) {
    this.localExecucao = localExecucao;
  }

  public String getProdutoEsperado() {
    return produtoEsperado;
  }

  public void setProdutoEsperado(String produtoEsperado) {
    this.produtoEsperado = produtoEsperado;
  }

  public List<PlanoTrabalhoEtapaRequest> getEtapas() {
    return etapas;
  }

  public void setEtapas(List<PlanoTrabalhoEtapaRequest> etapas) {
    this.etapas = etapas;
  }
}
