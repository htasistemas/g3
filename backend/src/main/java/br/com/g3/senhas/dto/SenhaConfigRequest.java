package br.com.g3.senhas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class SenhaConfigRequest {
  @NotBlank(message = "Frase de fala e obrigatoria.")
  private String fraseFala;

  @NotBlank(message = "URL do RSS e obrigatoria.")
  private String rssUrl;

  @NotNull(message = "Velocidade do ticker e obrigatoria.")
  private Integer velocidadeTicker;

  private String modoNoticias;

  private String noticiasManuais;

  @NotNull(message = "Quantidade de ultimas chamadas e obrigatoria.")
  @Min(value = 1, message = "Quantidade de ultimas chamadas deve ser no minimo 1.")
  @Max(value = 10, message = "Quantidade de ultimas chamadas deve ser no maximo 10.")
  private Integer quantidadeUltimasChamadas;

  private Long unidadePainelId;

  private String tituloTela;
  private String descricaoTela;

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
