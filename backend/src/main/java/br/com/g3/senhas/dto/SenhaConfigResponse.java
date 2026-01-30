package br.com.g3.senhas.dto;

public class SenhaConfigResponse {
  private String fraseFala;
  private String rssUrl;
  private Integer velocidadeTicker;
  private String modoNoticias;
  private String noticiasManuais;
  private Integer quantidadeUltimasChamadas;
  private Long unidadePainelId;
  private String tituloTela;
  private String descricaoTela;

  public SenhaConfigResponse() {}

  public SenhaConfigResponse(
      String fraseFala,
      String rssUrl,
      Integer velocidadeTicker,
      String modoNoticias,
      String noticiasManuais,
      Integer quantidadeUltimasChamadas,
      Long unidadePainelId,
      String tituloTela,
      String descricaoTela) {
    this.fraseFala = fraseFala;
    this.rssUrl = rssUrl;
    this.velocidadeTicker = velocidadeTicker;
    this.modoNoticias = modoNoticias;
    this.noticiasManuais = noticiasManuais;
    this.quantidadeUltimasChamadas = quantidadeUltimasChamadas;
    this.unidadePainelId = unidadePainelId;
    this.tituloTela = tituloTela;
    this.descricaoTela = descricaoTela;
  }

  public String getFraseFala() {
    return fraseFala;
  }

  public void setFraseFala(String fraseFala) {
    this.fraseFala = fraseFala;
  }

  public String getRssUrl() {
    return rssUrl;
  }

  public void setRssUrl(String rssUrl) {
    this.rssUrl = rssUrl;
  }

  public Integer getVelocidadeTicker() {
    return velocidadeTicker;
  }

  public void setVelocidadeTicker(Integer velocidadeTicker) {
    this.velocidadeTicker = velocidadeTicker;
  }

  public String getModoNoticias() {
    return modoNoticias;
  }

  public void setModoNoticias(String modoNoticias) {
    this.modoNoticias = modoNoticias;
  }

  public String getNoticiasManuais() {
    return noticiasManuais;
  }

  public void setNoticiasManuais(String noticiasManuais) {
    this.noticiasManuais = noticiasManuais;
  }

  public Integer getQuantidadeUltimasChamadas() {
    return quantidadeUltimasChamadas;
  }

  public void setQuantidadeUltimasChamadas(Integer quantidadeUltimasChamadas) {
    this.quantidadeUltimasChamadas = quantidadeUltimasChamadas;
  }

  public Long getUnidadePainelId() {
    return unidadePainelId;
  }

  public void setUnidadePainelId(Long unidadePainelId) {
    this.unidadePainelId = unidadePainelId;
  }

  public String getTituloTela() {
    return tituloTela;
  }

  public void setTituloTela(String tituloTela) {
    this.tituloTela = tituloTela;
  }

  public String getDescricaoTela() {
    return descricaoTela;
  }

  public void setDescricaoTela(String descricaoTela) {
    this.descricaoTela = descricaoTela;
  }
}
