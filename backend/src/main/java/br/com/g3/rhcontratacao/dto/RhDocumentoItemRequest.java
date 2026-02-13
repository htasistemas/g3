package br.com.g3.rhcontratacao.dto;

public class RhDocumentoItemRequest {
  private String tipoDocumento;
  private Boolean obrigatorio;
  private String status;
  private String observacao;

  public String getTipoDocumento() {
    return tipoDocumento;
  }

  public void setTipoDocumento(String tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public Boolean getObrigatorio() {
    return obrigatorio;
  }

  public void setObrigatorio(Boolean obrigatorio) {
    this.obrigatorio = obrigatorio;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getObservacao() {
    return observacao;
  }

  public void setObservacao(String observacao) {
    this.observacao = observacao;
  }
}
