package br.com.g3.fotoseventos.dto;

import java.time.LocalDateTime;
import java.util.List;

public class FotoEventoFotoResponse {
  private final Long id;
  private final Long eventoId;
  private final String arquivo;
  private final String arquivoUrl;
  private final String nomeArquivo;
  private final String mimeType;
  private final Long tamanhoBytes;
  private final Integer largura;
  private final Integer altura;
  private final String legenda;
  private final String creditos;
  private final List<String> tags;
  private final Integer ordem;
  private final LocalDateTime criadoEm;
  private final LocalDateTime atualizadoEm;

  public FotoEventoFotoResponse(
      Long id,
      Long eventoId,
      String arquivo,
      String arquivoUrl,
      String nomeArquivo,
      String mimeType,
      Long tamanhoBytes,
      Integer largura,
      Integer altura,
      String legenda,
      String creditos,
      List<String> tags,
      Integer ordem,
      LocalDateTime criadoEm,
      LocalDateTime atualizadoEm) {
    this.id = id;
    this.eventoId = eventoId;
    this.arquivo = arquivo;
    this.arquivoUrl = arquivoUrl;
    this.nomeArquivo = nomeArquivo;
    this.mimeType = mimeType;
    this.tamanhoBytes = tamanhoBytes;
    this.largura = largura;
    this.altura = altura;
    this.legenda = legenda;
    this.creditos = creditos;
    this.tags = tags;
    this.ordem = ordem;
    this.criadoEm = criadoEm;
    this.atualizadoEm = atualizadoEm;
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

  public String getNomeArquivo() {
    return nomeArquivo;
  }

  public String getMimeType() {
    return mimeType;
  }

  public Long getTamanhoBytes() {
    return tamanhoBytes;
  }

  public Integer getLargura() {
    return largura;
  }

  public Integer getAltura() {
    return altura;
  }

  public String getLegenda() {
    return legenda;
  }

  public String getCreditos() {
    return creditos;
  }

  public List<String> getTags() {
    return tags;
  }

  public Integer getOrdem() {
    return ordem;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }
}
