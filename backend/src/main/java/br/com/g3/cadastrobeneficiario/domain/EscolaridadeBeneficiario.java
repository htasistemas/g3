package br.com.g3.cadastrobeneficiario.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "escolaridade_beneficiario")
public class EscolaridadeBeneficiario {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "beneficiario_id", nullable = false)
  private CadastroBeneficiario beneficiario;

  @Column(name = "sabe_ler_escrever")
  private Boolean sabeLerEscrever;

  @Column(name = "nivel_escolaridade", length = 150)
  private String nivelEscolaridade;

  @Column(name = "estuda_atualmente")
  private Boolean estudaAtualmente;

  @Column(length = 150)
  private String ocupacao;

  @Column(name = "situacao_trabalho", length = 150)
  private String situacaoTrabalho;

  @Column(name = "local_trabalho", length = 150)
  private String localTrabalho;

  @Column(name = "renda_mensal", length = 60)
  private String rendaMensal;

  @Column(name = "fonte_renda", length = 150)
  private String fonteRenda;

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

  public CadastroBeneficiario getBeneficiario() {
    return beneficiario;
  }

  public void setBeneficiario(CadastroBeneficiario beneficiario) {
    this.beneficiario = beneficiario;
  }

  public Boolean getSabeLerEscrever() {
    return sabeLerEscrever;
  }

  public void setSabeLerEscrever(Boolean sabeLerEscrever) {
    this.sabeLerEscrever = sabeLerEscrever;
  }

  public String getNivelEscolaridade() {
    return nivelEscolaridade;
  }

  public void setNivelEscolaridade(String nivelEscolaridade) {
    this.nivelEscolaridade = nivelEscolaridade;
  }

  public Boolean getEstudaAtualmente() {
    return estudaAtualmente;
  }

  public void setEstudaAtualmente(Boolean estudaAtualmente) {
    this.estudaAtualmente = estudaAtualmente;
  }

  public String getOcupacao() {
    return ocupacao;
  }

  public void setOcupacao(String ocupacao) {
    this.ocupacao = ocupacao;
  }

  public String getSituacaoTrabalho() {
    return situacaoTrabalho;
  }

  public void setSituacaoTrabalho(String situacaoTrabalho) {
    this.situacaoTrabalho = situacaoTrabalho;
  }

  public String getLocalTrabalho() {
    return localTrabalho;
  }

  public void setLocalTrabalho(String localTrabalho) {
    this.localTrabalho = localTrabalho;
  }

  public String getRendaMensal() {
    return rendaMensal;
  }

  public void setRendaMensal(String rendaMensal) {
    this.rendaMensal = rendaMensal;
  }

  public String getFonteRenda() {
    return fonteRenda;
  }

  public void setFonteRenda(String fonteRenda) {
    this.fonteRenda = fonteRenda;
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
