package br.com.g3.transparencia.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "transparencia_destinacoes")
public class TransparenciaDestinacao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "transparencia_id", nullable = false)
  private Long transparenciaId;

  @Column(name = "titulo", length = 200, nullable = false)
  private String titulo;

  @Column(name = "descricao", columnDefinition = "TEXT")
  private String descricao;

  @Column(name = "percentual")
  private Integer percentual;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTransparenciaId() {
    return transparenciaId;
  }

  public void setTransparenciaId(Long transparenciaId) {
    this.transparenciaId = transparenciaId;
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

  public Integer getPercentual() {
    return percentual;
  }

  public void setPercentual(Integer percentual) {
    this.percentual = percentual;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
