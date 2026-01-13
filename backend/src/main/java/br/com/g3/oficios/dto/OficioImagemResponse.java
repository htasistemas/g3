package br.com.g3.oficios.dto;

import java.time.LocalDateTime;

public class OficioImagemResponse {
  private Long id;
  private Long oficioId;
  private String nomeArquivo;
  private String tipoMime;
  private String conteudoBase64;
  private Integer ordem;
  private LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getOficioId() {
    return oficioId;
  }

  public void setOficioId(Long oficioId) {
    this.oficioId = oficioId;
  }

  public String getNomeArquivo() {
    return nomeArquivo;
  }

  public void setNomeArquivo(String nomeArquivo) {
    this.nomeArquivo = nomeArquivo;
  }

  public String getTipoMime() {
    return tipoMime;
  }

  public void setTipoMime(String tipoMime) {
    this.tipoMime = tipoMime;
  }

  public String getConteudoBase64() {
    return conteudoBase64;
  }

  public void setConteudoBase64(String conteudoBase64) {
    this.conteudoBase64 = conteudoBase64;
  }

  public Integer getOrdem() {
    return ordem;
  }

  public void setOrdem(Integer ordem) {
    this.ordem = ordem;
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
