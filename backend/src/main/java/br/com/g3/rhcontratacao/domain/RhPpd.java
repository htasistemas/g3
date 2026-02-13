package br.com.g3.rhcontratacao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "rh_ppd")
public class RhPpd {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "processo_id", nullable = false)
  private Long processoId;

  @Column(name = "cabecalho_json", columnDefinition = "TEXT")
  private String cabecalhoJson;

  @Column(name = "lado_a_json", columnDefinition = "TEXT")
  private String ladoAJson;

  @Column(name = "lado_b_json", columnDefinition = "TEXT")
  private String ladoBJson;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

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

  public String getCabecalhoJson() {
    return cabecalhoJson;
  }

  public void setCabecalhoJson(String cabecalhoJson) {
    this.cabecalhoJson = cabecalhoJson;
  }

  public String getLadoAJson() {
    return ladoAJson;
  }

  public void setLadoAJson(String ladoAJson) {
    this.ladoAJson = ladoAJson;
  }

  public String getLadoBJson() {
    return ladoBJson;
  }

  public void setLadoBJson(String ladoBJson) {
    this.ladoBJson = ladoBJson;
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
}
