package br.com.g3.configuracoes.dto;

public class AtualizarVersaoRequest {
  private String versao;
  private String descricao;

  public String getVersao() {
    return versao;
  }

  public void setVersao(String versao) {
    this.versao = versao;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }
}
