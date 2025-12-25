package br.com.g3.unidadeassistencial.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "imagens_unidade")
public class ImagemUnidade {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "unidade_id", nullable = false, unique = true)
  private UnidadeAssistencial unidadeAssistencial;

  @Column(name = "logomarca")
  private String logomarca;

  @Column(name = "logomarca_relatorio")
  private String logomarcaRelatorio;

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

  public UnidadeAssistencial getUnidadeAssistencial() {
    return unidadeAssistencial;
  }

  public void setUnidadeAssistencial(UnidadeAssistencial unidadeAssistencial) {
    this.unidadeAssistencial = unidadeAssistencial;
  }

  public String getLogomarca() {
    return logomarca;
  }

  public void setLogomarca(String logomarca) {
    this.logomarca = logomarca;
  }

  public String getLogomarcaRelatorio() {
    return logomarcaRelatorio;
  }

  public void setLogomarcaRelatorio(String logomarcaRelatorio) {
    this.logomarcaRelatorio = logomarcaRelatorio;
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
