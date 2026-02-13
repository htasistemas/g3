package br.com.g3.rhcontratacao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "rh_ficha_admissao")
public class RhFichaAdmissao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "processo_id", nullable = false)
  private Long processoId;

  @Column(name = "dados_pessoais_json", columnDefinition = "TEXT")
  private String dadosPessoaisJson;

  @Column(name = "dependentes_json", columnDefinition = "TEXT")
  private String dependentesJson;

  @Column(name = "dados_internos_json", columnDefinition = "TEXT")
  private String dadosInternosJson;

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

  public Long getProcessoId() {
    return processoId;
  }

  public void setProcessoId(Long processoId) {
    this.processoId = processoId;
  }

  public String getDadosPessoaisJson() {
    return dadosPessoaisJson;
  }

  public void setDadosPessoaisJson(String dadosPessoaisJson) {
    this.dadosPessoaisJson = dadosPessoaisJson;
  }

  public String getDependentesJson() {
    return dependentesJson;
  }

  public void setDependentesJson(String dependentesJson) {
    this.dependentesJson = dependentesJson;
  }

  public String getDadosInternosJson() {
    return dadosInternosJson;
  }

  public void setDadosInternosJson(String dadosInternosJson) {
    this.dadosInternosJson = dadosInternosJson;
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
