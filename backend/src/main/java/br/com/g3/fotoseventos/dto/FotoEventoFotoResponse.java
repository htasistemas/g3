package br.com.g3.fotoseventos.dto;

import java.time.LocalDateTime;

public class FotoEventoFotoResponse {
  private final Long id;
  private final Long eventoId;
  private final String arquivo;
  private final String arquivoUrl;
  private final String legenda;
  private final Integer ordem;
  private final LocalDateTime criadoEm;

  public FotoEventoFotoResponse(
      Long id,
      Long eventoId,
      String arquivo,
      String arquivoUrl,
      String legenda,
      Integer ordem,
      LocalDateTime criadoEm) {
    this.id = id;
    this.eventoId = eventoId;
    this.arquivo = arquivo;
    this.arquivoUrl = arquivoUrl;
    this.legenda = legenda;
    this.ordem = ordem;
    this.criadoEm = criadoEm;
  }

  public Long getId() {
    return id;
  }

  public Long getEventoId() {
    return eventoId;
  }

  public String getArquivo() {
    return arquivo;
  }

  public String getArquivoUrl() {
    return arquivoUrl;
  }

  public String getLegenda() {
    return legenda;
  }

  public Integer getOrdem() {
    return ordem;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }
}
