package br.com.g3.planotrabalho.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "plano_trabalho_metas")
public class PlanoTrabalhoMeta {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "plano_trabalho_id", nullable = false)
  private Long planoTrabalhoId;

  @Column(name = "codigo", length = 60)
  private String codigo;

  @Column(name = "descricao", columnDefinition = "TEXT", nullable = false)
  private String descricao;

  @Column(name = "indicador", columnDefinition = "TEXT")
  private String indicador;

  @Column(name = "unidade_medida", length = 60)
  private String unidadeMedida;

  @Column(name = "quantidade_prevista")
  private BigDecimal quantidadePrevista;

  @Column(name = "resultado_esperado", columnDefinition = "TEXT")
  private String resultadoEsperado;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPlanoTrabalhoId() {
    return planoTrabalhoId;
  }

  public void setPlanoTrabalhoId(Long planoTrabalhoId) {
    this.planoTrabalhoId = planoTrabalhoId;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getIndicador() {
    return indicador;
  }

  public void setIndicador(String indicador) {
    this.indicador = indicador;
  }

  public String getUnidadeMedida() {
    return unidadeMedida;
  }

  public void setUnidadeMedida(String unidadeMedida) {
    this.unidadeMedida = unidadeMedida;
  }

  public BigDecimal getQuantidadePrevista() {
    return quantidadePrevista;
  }

  public void setQuantidadePrevista(BigDecimal quantidadePrevista) {
    this.quantidadePrevista = quantidadePrevista;
  }

  public String getResultadoEsperado() {
    return resultadoEsperado;
  }

  public void setResultadoEsperado(String resultadoEsperado) {
    this.resultadoEsperado = resultadoEsperado;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
