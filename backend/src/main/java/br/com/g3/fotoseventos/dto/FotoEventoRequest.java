package br.com.g3.fotoseventos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class FotoEventoRequest {
  @NotBlank
  private String titulo;

  private String descricao;

  @NotNull
  private LocalDate dataEvento;

  private String local;

  private String status;

  private List<String> tags;

  private Long unidadeId;

  @Valid
  private FotoEventoUploadRequest fotoPrincipalUpload;

  private Long fotoPrincipalId;

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public LocalDate getDataEvento() {
    return dataEvento;
  }

  public void setDataEvento(LocalDate dataEvento) {
    this.dataEvento = dataEvento;
  }

  public String getLocal() {
    return local;
  }

  public void setLocal(String local) {
    this.local = local;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public void setUnidadeId(Long unidadeId) {
    this.unidadeId = unidadeId;
  }

  public FotoEventoUploadRequest getFotoPrincipalUpload() {
    return fotoPrincipalUpload;
  }

  public void setFotoPrincipalUpload(FotoEventoUploadRequest fotoPrincipalUpload) {
    this.fotoPrincipalUpload = fotoPrincipalUpload;
  }

  public Long getFotoPrincipalId() {
    return fotoPrincipalId;
  }

  public void setFotoPrincipalId(Long fotoPrincipalId) {
    this.fotoPrincipalId = fotoPrincipalId;
  }
}
