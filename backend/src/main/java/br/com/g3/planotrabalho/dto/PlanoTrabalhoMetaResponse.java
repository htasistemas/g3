package br.com.g3.planotrabalho.dto;

import java.math.BigDecimal;
import java.util.List;

public class PlanoTrabalhoMetaResponse {
  private final Long id;
  private final String codigo;
  private final String descricao;
  private final String indicador;
  private final String unidadeMedida;
  private final BigDecimal quantidadePrevista;
  private final String resultadoEsperado;
  private final List<PlanoTrabalhoAtividadeResponse> atividades;

  public PlanoTrabalhoMetaResponse(
      Long id,
      String codigo,
      String descricao,
      String indicador,
      String unidadeMedida,
      BigDecimal quantidadePrevista,
      String resultadoEsperado,
      List<PlanoTrabalhoAtividadeResponse> atividades) {
    this.id = id;
    this.codigo = codigo;
    this.descricao = descricao;
    this.indicador = indicador;
    this.unidadeMedida = unidadeMedida;
    this.quantidadePrevista = quantidadePrevista;
    this.resultadoEsperado = resultadoEsperado;
    this.atividades = atividades;
  }

  public Long getId() {
    return id;
  }

  public String getCodigo() {
    return codigo;
  }

  public String getDescricao() {
    return descricao;
  }

  public String getIndicador() {
    return indicador;
  }

  public String getUnidadeMedida() {
    return unidadeMedida;
  }

  public BigDecimal getQuantidadePrevista() {
    return quantidadePrevista;
  }

  public String getResultadoEsperado() {
    return resultadoEsperado;
  }

  public List<PlanoTrabalhoAtividadeResponse> getAtividades() {
    return atividades;
  }
}
