package br.com.g3.contabilidade.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmendaImpositivaRequest {
  private String identificacao;
  private String referenciaLegal;
  private LocalDate dataPrevista;
  private BigDecimal valorPrevisto;
  private Integer diasAlerta;
  private String status;
  private String observacoes;

  public String getIdentificacao() {
    return identificacao;
  }

  public void setIdentificacao(String identificacao) {
    this.identificacao = identificacao;
  }

  public String getReferenciaLegal() {
    return referenciaLegal;
  }

  public void setReferenciaLegal(String referenciaLegal) {
    this.referenciaLegal = referenciaLegal;
  }

  public LocalDate getDataPrevista() {
    return dataPrevista;
  }

  public void setDataPrevista(LocalDate dataPrevista) {
    this.dataPrevista = dataPrevista;
  }

  public BigDecimal getValorPrevisto() {
    return valorPrevisto;
  }

  public void setValorPrevisto(BigDecimal valorPrevisto) {
    this.valorPrevisto = valorPrevisto;
  }

  public Integer getDiasAlerta() {
    return diasAlerta;
  }

  public void setDiasAlerta(Integer diasAlerta) {
    this.diasAlerta = diasAlerta;
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
