package br.com.g3.oficios.dto;

import jakarta.validation.constraints.NotBlank;

public class OficioConteudoRequest {
  @NotBlank
  private String razaoSocial;

  private String logoUrl;
  private String titulo;
  private String saudacao;

  @NotBlank
  private String assunto;

  @NotBlank
  private String corpo;

  private String finalizacao;
  private String assinaturaNome;
  private String assinaturaCargo;
  private String rodape;

  public String getRazaoSocial() {
    return razaoSocial;
  }

  public void setRazaoSocial(String razaoSocial) {
    this.razaoSocial = razaoSocial;
  }

  public String getLogoUrl() {
    return logoUrl;
  }

  public void setLogoUrl(String logoUrl) {
    this.logoUrl = logoUrl;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getSaudacao() {
    return saudacao;
  }

  public void setSaudacao(String saudacao) {
    this.saudacao = saudacao;
  }

  public String getAssunto() {
    return assunto;
  }

  public void setAssunto(String assunto) {
    this.assunto = assunto;
  }

  public String getCorpo() {
    return corpo;
  }

  public void setCorpo(String corpo) {
    this.corpo = corpo;
  }

  public String getFinalizacao() {
    return finalizacao;
  }

  public void setFinalizacao(String finalizacao) {
    this.finalizacao = finalizacao;
  }

  public String getAssinaturaNome() {
    return assinaturaNome;
  }

  public void setAssinaturaNome(String assinaturaNome) {
    this.assinaturaNome = assinaturaNome;
  }

  public String getAssinaturaCargo() {
    return assinaturaCargo;
  }

  public void setAssinaturaCargo(String assinaturaCargo) {
    this.assinaturaCargo = assinaturaCargo;
  }

  public String getRodape() {
    return rodape;
  }

  public void setRodape(String rodape) {
    this.rodape = rodape;
  }
}
