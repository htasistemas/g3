package br.com.g3.planotrabalho.dto;

import java.math.BigDecimal;
import java.util.List;

public class PlanoTrabalhoMetaRequest {
  private Long id;
  private String codigo;
  private String descricao;
  private String indicador;
  private String unidadeMedida;
  private BigDecimal quantidadePrevista;
  private String resultadoEsperado;
  private List<PlanoTrabalhoAtividadeRequest> atividades;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public List<PlanoTrabalhoAtividadeRequest> getAtividades() {
    return atividades;
  }

  public void setAtividades(List<PlanoTrabalhoAtividadeRequest> atividades) {
    this.atividades = atividades;
  }
}
