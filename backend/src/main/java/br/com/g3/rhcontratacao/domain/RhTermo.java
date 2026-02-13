package br.com.g3.rhcontratacao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rh_termo")
public class RhTermo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "processo_id", nullable = false)
  private Long processoId;

  @Column(name = "tipo", nullable = false, length = 40)
  private String tipo;

  @Column(name = "dados_json", columnDefinition = "TEXT")
  private String dadosJson;

  @Column(name = "status_assinatura", length = 30)
  private String statusAssinatura;

  @Column(name = "data_assinatura")
  private LocalDate dataAssinatura;

  @Column(name = "responsavel", length = 120)
  private String responsavel;

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

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getDadosJson() {
    return dadosJson;
  }

  public void setDadosJson(String dadosJson) {
    this.dadosJson = dadosJson;
  }

  public String getStatusAssinatura() {
    return statusAssinatura;
  }

  public void setStatusAssinatura(String statusAssinatura) {
    this.statusAssinatura = statusAssinatura;
  }

  public LocalDate getDataAssinatura() {
    return dataAssinatura;
  }

  public void setDataAssinatura(LocalDate dataAssinatura) {
    this.dataAssinatura = dataAssinatura;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
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
