package br.com.g3.oficios.dto;

public class OficioConteudoResponse {
  private String razaoSocial;
  private String logoUrl;
  private String titulo;
  private String saudacao;
  private String para;
  private String cargoPara;
  private String assunto;
  private String corpo;
  private String finalizacao;
  private String assinaturaNome;
  private String assinaturaCargo;
  private String rodape;

  public OficioConteudoResponse(
      String razaoSocial,
      String logoUrl,
      String titulo,
      String saudacao,
      String para,
      String cargoPara,
      String assunto,
      String corpo,
      String finalizacao,
      String assinaturaNome,
      String assinaturaCargo,
      String rodape) {
    this.razaoSocial = razaoSocial;
    this.logoUrl = logoUrl;
    this.titulo = titulo;
    this.saudacao = saudacao;
    this.para = para;
    this.cargoPara = cargoPara;
    this.assunto = assunto;
    this.corpo = corpo;
    this.finalizacao = finalizacao;
    this.assinaturaNome = assinaturaNome;
    this.assinaturaCargo = assinaturaCargo;
    this.rodape = rodape;
  }

  public String getRazaoSocial() {
    return razaoSocial;
  }

  public String getLogoUrl() {
    return logoUrl;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getSaudacao() {
    return saudacao;
  }

  public String getPara() {
    return para;
  }

  public String getCargoPara() {
    return cargoPara;
  }

  public String getAssunto() {
    return assunto;
  }

  public String getCorpo() {
    return corpo;
  }

  public String getFinalizacao() {
    return finalizacao;
  }

  public String getAssinaturaNome() {
    return assinaturaNome;
  }

  public String getAssinaturaCargo() {
    return assinaturaCargo;
  }

  public String getRodape() {
    return rodape;
  }
}
