package br.com.g3.rhcontratacao.dto;

import java.time.LocalDateTime;

public class RhAuditoriaContratacaoResponse {
  private Long id;
  private Long processoId;
  private Long atorId;
  private String acao;
  private String detalhes;
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getProcessoId() {
    return processoId;
  }

  public void setProcessoId(Long processoId) {
    this.processoId = processoId;
  }

  public Long getAtorId() {
    return atorId;
  }

  public void setAtorId(Long atorId) {
    this.atorId = atorId;
  }

  public String getAcao() {
    return acao;
  }

  public void setAcao(String acao) {
    this.acao = acao;
  }

  public String getDetalhes() {
    return detalhes;
  }

  public void setDetalhes(String detalhes) {
    this.detalhes = detalhes;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
