package br.com.g3.chamadas.beneficiario.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "beneficiario")
public class BeneficiarioEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_beneficiario")
  private Long idBeneficiario;

  @Column(name = "nome_beneficiario", nullable = false, length = 200)
  private String nomeBeneficiario;

  @Column(name = "documento", length = 50)
  private String documento;

  @Column(name = "ativo", nullable = false)
  private Boolean ativo;

  @Column(name = "data_hora_criacao", nullable = false)
  private LocalDateTime dataHoraCriacao;

  @Column(name = "data_hora_atualizacao", nullable = false)
  private LocalDateTime dataHoraAtualizacao;

  public Long getIdBeneficiario() {
    return idBeneficiario;
  }

  public void setIdBeneficiario(Long idBeneficiario) {
    this.idBeneficiario = idBeneficiario;
  }

  public String getNomeBeneficiario() {
    return nomeBeneficiario;
  }

  public void setNomeBeneficiario(String nomeBeneficiario) {
    this.nomeBeneficiario = nomeBeneficiario;
  }

  public String getDocumento() {
    return documento;
  }

  public void setDocumento(String documento) {
    this.documento = documento;
  }

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean ativo) {
    this.ativo = ativo;
  }

  public LocalDateTime getDataHoraCriacao() {
    return dataHoraCriacao;
  }

  public void setDataHoraCriacao(LocalDateTime dataHoraCriacao) {
    this.dataHoraCriacao = dataHoraCriacao;
  }

  public LocalDateTime getDataHoraAtualizacao() {
    return dataHoraAtualizacao;
  }

  public void setDataHoraAtualizacao(LocalDateTime dataHoraAtualizacao) {
    this.dataHoraAtualizacao = dataHoraAtualizacao;
  }
}
