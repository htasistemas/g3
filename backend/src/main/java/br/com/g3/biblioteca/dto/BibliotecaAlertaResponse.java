package br.com.g3.biblioteca.dto;

import java.time.LocalDate;

public class BibliotecaAlertaResponse {
  private Long emprestimoId;
  private String livroTitulo;
  private String beneficiarioNome;
  private LocalDate dataDevolucaoPrevista;
  private long diasParaVencimento;
  private String status;

  public Long getEmprestimoId() {
    return emprestimoId;
  }

  public void setEmprestimoId(Long emprestimoId) {
    this.emprestimoId = emprestimoId;
  }

  public String getLivroTitulo() {
    return livroTitulo;
  }

  public void setLivroTitulo(String livroTitulo) {
    this.livroTitulo = livroTitulo;
  }

  public String getBeneficiarioNome() {
    return beneficiarioNome;
  }

  public void setBeneficiarioNome(String beneficiarioNome) {
    this.beneficiarioNome = beneficiarioNome;
  }

  public LocalDate getDataDevolucaoPrevista() {
    return dataDevolucaoPrevista;
  }

  public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
    this.dataDevolucaoPrevista = dataDevolucaoPrevista;
  }

  public long getDiasParaVencimento() {
    return diasParaVencimento;
  }

  public void setDiasParaVencimento(long diasParaVencimento) {
    this.diasParaVencimento = diasParaVencimento;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
