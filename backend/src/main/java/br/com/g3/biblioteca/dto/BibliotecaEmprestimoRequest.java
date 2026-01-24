package br.com.g3.biblioteca.dto;

import java.time.LocalDate;

public class BibliotecaEmprestimoRequest {
  private Long livroId;
  private Long beneficiarioId;
  private String beneficiarioNome;
  private Long responsavelId;
  private String responsavelNome;
  private LocalDate dataEmprestimo;
  private LocalDate dataDevolucaoPrevista;
  private LocalDate dataDevolucaoReal;
  private String status;
  private String observacoes;

  public Long getLivroId() {
    return livroId;
  }

  public void setLivroId(Long livroId) {
    this.livroId = livroId;
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

  public Long getResponsavelId() {
    return responsavelId;
  }

  public void setResponsavelId(Long responsavelId) {
    this.responsavelId = responsavelId;
  }

  public String getResponsavelNome() {
    return responsavelNome;
  }

  public void setResponsavelNome(String responsavelNome) {
    this.responsavelNome = responsavelNome;
  }

  public LocalDate getDataEmprestimo() {
    return dataEmprestimo;
  }

  public void setDataEmprestimo(LocalDate dataEmprestimo) {
    this.dataEmprestimo = dataEmprestimo;
  }

  public LocalDate getDataDevolucaoPrevista() {
    return dataDevolucaoPrevista;
  }

  public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
    this.dataDevolucaoPrevista = dataDevolucaoPrevista;
  }

  public LocalDate getDataDevolucaoReal() {
    return dataDevolucaoReal;
  }

  public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
    this.dataDevolucaoReal = dataDevolucaoReal;
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
