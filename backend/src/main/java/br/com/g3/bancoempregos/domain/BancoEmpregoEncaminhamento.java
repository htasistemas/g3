package br.com.g3.bancoempregos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "banco_empregos_encaminhamentos")
public class BancoEmpregoEncaminhamento {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "emprego_id", nullable = false)
  private Long empregoId;

  @Column(name = "beneficiario_id")
  private Long beneficiarioId;

  @Column(name = "beneficiario_nome", length = 200)
  private String beneficiarioNome;

  @Column(name = "data_encaminhamento", nullable = false)
  private LocalDate dataEncaminhamento;

  @Column(name = "status", length = 40, nullable = false)
  private String status;

  @Column(name = "observacoes", columnDefinition = "TEXT")
  private String observacoes;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getEmpregoId() {
    return empregoId;
  }

  public void setEmpregoId(Long empregoId) {
    this.empregoId = empregoId;
  }

  public Long getBeneficiarioId() {
    return beneficiarioId;
  }

  public void setBeneficiarioId(Long beneficiarioId) {
    this.beneficiarioId = beneficiarioId;
  }

  public String getBeneficiarioNome() {
    return beneficiarioNome;
  }

  public void setBeneficiarioNome(String beneficiarioNome) {
    this.beneficiarioNome = beneficiarioNome;
  }

  public LocalDate getDataEncaminhamento() {
    return dataEncaminhamento;
  }

  public void setDataEncaminhamento(LocalDate dataEncaminhamento) {
    this.dataEncaminhamento = dataEncaminhamento;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }
}
