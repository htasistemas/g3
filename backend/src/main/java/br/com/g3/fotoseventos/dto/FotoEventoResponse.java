package br.com.g3.fotoseventos.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FotoEventoResponse {
  private final Long id;
  private final Long unidadeId;
  private final String titulo;
  private final String descricao;
  private final LocalDate dataEvento;
  private final String local;
  private final String tags;
  private final String fotoPrincipal;
  private final String fotoPrincipalUrl;
  private final LocalDateTime criadoEm;
  private final LocalDateTime atualizadoEm;

  public FotoEventoResponse(
      Long id,
      Long unidadeId,
      String titulo,
      String descricao,
      LocalDate dataEvento,
      String local,
      String tags,
      String fotoPrincipal,
      String fotoPrincipalUrl,
      LocalDateTime criadoEm,
      LocalDateTime atualizadoEm) {
    this.id = id;
    this.unidadeId = unidadeId;
    this.titulo = titulo;
    this.descricao = descricao;
    this.dataEvento = dataEvento;
    this.local = local;
    this.tags = tags;
    this.fotoPrincipal = fotoPrincipal;
    this.fotoPrincipalUrl = fotoPrincipalUrl;
    this.criadoEm = criadoEm;
    this.atualizadoEm = atualizadoEm;
  }

  public Long getId() {
    return id;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescricao() {
    return descricao;
  }

  public LocalDate getDataEvento() {
    return dataEvento;
  }

  public String getLocal() {
    return local;
  }

  public String getTags() {
    return tags;
  }

  public String getFotoPrincipal() {
    return fotoPrincipal;
  }

  public String getFotoPrincipalUrl() {
    return fotoPrincipalUrl;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }
}
