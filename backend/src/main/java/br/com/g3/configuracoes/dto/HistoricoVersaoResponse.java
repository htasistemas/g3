package br.com.g3.configuracoes.dto;

import java.time.LocalDateTime;

public class HistoricoVersaoResponse {
  private Long id;
  private String versao;
  private String descricao;
  private LocalDateTime criadoEm;

  public HistoricoVersaoResponse() {}

  public HistoricoVersaoResponse(Long id, String versao, String descricao, LocalDateTime criadoEm) {
    this.id = id;
    this.versao = versao;
    this.descricao = descricao;
    this.criadoEm = criadoEm;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
