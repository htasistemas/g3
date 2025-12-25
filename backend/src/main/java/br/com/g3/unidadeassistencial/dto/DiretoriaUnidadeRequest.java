package br.com.g3.unidadeassistencial.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DiretoriaUnidadeRequest {
  @NotBlank
  @Size(max = 200)
  private String nomeCompleto;

  @NotBlank
  @Size(max = 60)
  private String documento;

  @NotBlank
  @Size(max = 120)
  private String funcao;

  @Size(max = 9)
  private String mandatoInicio;

  @Size(max = 9)
  private String mandatoFim;

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public void setNomeCompleto(String nomeCompleto) {
    this.nomeCompleto = nomeCompleto;
  }

  public String getDocumento() {
    return documento;
  }

  public void setDocumento(String documento) {
    this.documento = documento;
  }

  public String getFuncao() {
    return funcao;
  }

  public void setFuncao(String funcao) {
    this.funcao = funcao;
  }

  public String getMandatoInicio() {
    return mandatoInicio;
  }

  public void setMandatoInicio(String mandatoInicio) {
    this.mandatoInicio = mandatoInicio;
  }

  public String getMandatoFim() {
    return mandatoFim;
  }

  public void setMandatoFim(String mandatoFim) {
    this.mandatoFim = mandatoFim;
  }
}
