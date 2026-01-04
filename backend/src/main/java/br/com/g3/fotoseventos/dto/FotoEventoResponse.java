package br.com.g3.fotoseventos.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class FotoEventoResponse {
  private final Long id;
  private final Long unidadeId;
  private final String titulo;
  private final String descricao;
  private final LocalDate dataEvento;
  private final String local;
  private final String status;
  private final List<String> tags;
  private final Long fotoPrincipalId;
  private final String fotoPrincipalUrl;
  private final Long totalFotos;
  private final LocalDateTime criadoEm;
  private final LocalDateTime atualizadoEm;

  public FotoEventoResponse(
      Long id,
      Long unidadeId,
      String titulo,
      String descricao,
      LocalDate dataEvento,
      String local,
      String status,
      List<String> tags,
      Long fotoPrincipalId,
      String fotoPrincipalUrl,
      Long totalFotos,
      LocalDateTime criadoEm,
      LocalDateTime atualizadoEm) {
    this.id = id;
    this.unidadeId = unidadeId;
    this.titulo = titulo;
    this.descricao = descricao;
    this.dataEvento = dataEvento;
    this.local = local;
    this.status = status;
    this.tags = tags;
    this.fotoPrincipalId = fotoPrincipalId;
    this.fotoPrincipalUrl = fotoPrincipalUrl;
    this.totalFotos = totalFotos;
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

  public String getStatus() {
    return status;
  }

  public List<String> getTags() {
    return tags;
  }

  public Long getFotoPrincipalId() {
    return fotoPrincipalId;
  }

  public String getFotoPrincipalUrl() {
    return fotoPrincipalUrl;
  }

  public Long getTotalFotos() {
    return totalFotos;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }
}
