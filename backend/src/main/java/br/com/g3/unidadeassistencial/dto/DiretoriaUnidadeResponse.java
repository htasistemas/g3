package br.com.g3.unidadeassistencial.dto;

public class DiretoriaUnidadeResponse {
  private final Long id;
  private final String nomeCompleto;
  private final String documento;
  private final String funcao;
  private final String mandatoInicio;
  private final String mandatoFim;

  public DiretoriaUnidadeResponse(
      Long id, String nomeCompleto, String documento, String funcao, String mandatoInicio, String mandatoFim) {
    this.id = id;
    this.nomeCompleto = nomeCompleto;
    this.documento = documento;
    this.funcao = funcao;
    this.mandatoInicio = mandatoInicio;
    this.mandatoFim = mandatoFim;
  }

  public Long getId() {
    return id;
  }

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public String getDocumento() {
    return documento;
  }

  public String getFuncao() {
    return funcao;
  }

  public String getMandatoInicio() {
    return mandatoInicio;
  }

  public String getMandatoFim() {
    return mandatoFim;
  }
}
