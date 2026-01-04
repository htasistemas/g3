package br.com.g3.fotoseventos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fotos_eventos")
public class FotoEvento {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "unidade_id")
  private Long unidadeId;

  @Column(name = "titulo", length = 200, nullable = false)
  private String titulo;

  @Column(name = "descricao", columnDefinition = "TEXT")
  private String descricao;

  @Column(name = "data_evento", nullable = false)
  private LocalDate dataEvento;

  @Column(name = "local", length = 200)
  private String local;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", length = 20, nullable = false)
  private StatusFotoEvento status;

  @Column(name = "tags", columnDefinition = "TEXT")
  private String tags;

  @Column(name = "foto_principal_id")
  private Long fotoPrincipalId;

  @Column(name = "criado_por")
  private Long criadoPor;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public void setUnidadeId(Long unidadeId) {
    this.unidadeId = unidadeId;
  }

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

  public StatusFotoEvento getStatus() {
    return status;
  }

  public void setStatus(StatusFotoEvento status) {
    this.status = status;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public Long getFotoPrincipalId() {
    return fotoPrincipalId;
  }

  public void setFotoPrincipalId(Long fotoPrincipalId) {
    this.fotoPrincipalId = fotoPrincipalId;
  }

  public Long getCriadoPor() {
    return criadoPor;
  }

  public void setCriadoPor(Long criadoPor) {
    this.criadoPor = criadoPor;
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
