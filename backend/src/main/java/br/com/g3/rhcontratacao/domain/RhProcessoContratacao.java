package br.com.g3.rhcontratacao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "rh_processo_contratacao")
public class RhProcessoContratacao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "candidato_id", nullable = false)
  private Long candidatoId;

  @Column(name = "status", nullable = false, length = 40)
  private String status;

  @Column(name = "responsavel_id")
  private Long responsavelId;

  @Column(name = "gestor_id")
  private Long gestorId;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

  @Column(name = "ultima_movimentacao_em")
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
