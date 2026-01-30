package br.com.g3.senhas.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "senha_config")
public class SenhaConfig {
  @Id
  private Long id;

  @Column(name = "frase_fala", length = 300)
  private String fraseFala;

  @Column(name = "rss_url", length = 500)
  private String rssUrl;

  @Column(name = "velocidade_ticker")
  private Integer velocidadeTicker;

  @Column(name = "modo_noticias", length = 20)
  private String modoNoticias;

  @Column(name = "noticias_manuais")
  private String noticiasManuais;

  @Column(name = "quantidade_ultimas_chamadas")
  private Integer quantidadeUltimasChamadas;

  @Column(name = "unidade_painel_id")
  private Long unidadePainelId;

  @Column(name = "titulo_tela", length = 120)
  private String tituloTela;

  @Column(name = "descricao_tela", length = 200)
  private String descricaoTela;

  @Column(name = "atualizado_em")
  private LocalDateTime atualizadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public void setAtualizadoEm(LocalDateTime atualizadoEm) {
    this.atualizadoEm = atualizadoEm;
  }
}
