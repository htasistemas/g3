package br.com.g3.manualsistema.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "manual_sistema_secoes")
public class ManualSistemaSecao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "slug", nullable = false, unique = true, length = 120)
  private String slug;

  @Column(name = "titulo", nullable = false, length = 200)
  private String titulo;

  @Column(name = "conteudo", columnDefinition = "TEXT")
  private String conteudo;

  @Column(name = "ordem", nullable = false)
  private Integer ordem;

  @Column(name = "tags", length = 300)
  private String tags;

  @Column(name = "atualizado_em")
  private LocalDateTime atualizadoEm;

  @Column(name = "atualizado_por", length = 120)
  private String atualizadoPor;

  @Column(name = "versao", length = 40)
  private String versao;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getConteudo() {
    return conteudo;
  }

  public void setConteudo(String conteudo) {
    this.conteudo = conteudo;
  }

  public Integer getOrdem() {
    return ordem;
  }

  public void setOrdem(Integer ordem) {
    this.ordem = ordem;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public void setAtualizadoEm(LocalDateTime atualizadoEm) {
    this.atualizadoEm = atualizadoEm;
  }

  public String getAtualizadoPor() {
    return atualizadoPor;
  }

  public void setAtualizadoPor(String atualizadoPor) {
    this.atualizadoPor = atualizadoPor;
  }

  public String getVersao() {
    return versao;
  }

  public void setVersao(String versao) {
    this.versao = versao;
  }
}
