package br.com.g3.prontuario.dto;

import java.time.LocalDateTime;

public class ProntuarioAnexoResponse {
  private Long id;
  private Long registroId;
  private String nomeArquivo;
  private String tipoMime;
  private String urlArquivo;
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getRegistroId() {
    return registroId;
  }

  public void setRegistroId(Long registroId) {
    this.registroId = registroId;
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

  public String getUrlArquivo() {
    return urlArquivo;
  }

  public void setUrlArquivo(String urlArquivo) {
    this.urlArquivo = urlArquivo;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
