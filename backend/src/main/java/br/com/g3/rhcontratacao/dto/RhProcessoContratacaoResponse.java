package br.com.g3.rhcontratacao.dto;

import java.time.LocalDateTime;

public class RhProcessoContratacaoResponse {
  private Long id;
  private Long candidatoId;
  private String status;
  private Long responsavelId;
  private Long gestorId;
  private LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;
  private LocalDateTime ultimaMovimentacaoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCandidatoId() {
    return candidatoId;
  }

  public void setCandidatoId(Long candidatoId) {
    this.candidatoId = candidatoId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Long getResponsavelId() {
    return responsavelId;
  }

  public void setResponsavelId(Long responsavelId) {
    this.responsavelId = responsavelId;
  }

  public Long getGestorId() {
    return gestorId;
  }

  public void setGestorId(Long gestorId) {
    this.gestorId = gestorId;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public void setAtualizadoEm(LocalDateTime atualizadoEm) {
    this.atualizadoEm = atualizadoEm;
  }

  public LocalDateTime getUltimaMovimentacaoEm() {
    return ultimaMovimentacaoEm;
  }

  public void setUltimaMovimentacaoEm(LocalDateTime ultimaMovimentacaoEm) {
    this.ultimaMovimentacaoEm = ultimaMovimentacaoEm;
  }
}
