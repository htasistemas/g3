package br.com.g3.configuracoes.dto;

import java.time.LocalDateTime;

public class VersaoSistemaResponse {
  private String versao;
  private String descricao;
  private LocalDateTime atualizadoEm;

  public VersaoSistemaResponse() {}

  public VersaoSistemaResponse(String versao, String descricao, LocalDateTime atualizadoEm) {
    this.versao = versao;
    this.descricao = descricao;
    this.atualizadoEm = atualizadoEm;
  }

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

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public void setAtualizadoEm(LocalDateTime atualizadoEm) {
    this.atualizadoEm = atualizadoEm;
  }
}
