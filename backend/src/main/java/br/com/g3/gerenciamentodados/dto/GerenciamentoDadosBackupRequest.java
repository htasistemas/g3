package br.com.g3.gerenciamentodados.dto;

public class GerenciamentoDadosBackupRequest {
  private String rotulo;
  private String tipo;
  private String status;
  private String armazenadoEm;
  private String tamanho;
  private Boolean criptografado;
  private Integer retencaoDias;

  public String getRotulo() {
    return rotulo;
  }

  public void setRotulo(String rotulo) {
    this.rotulo = rotulo;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getArmazenadoEm() {
    return armazenadoEm;
  }

  public void setArmazenadoEm(String armazenadoEm) {
    this.armazenadoEm = armazenadoEm;
  }

  public String getTamanho() {
    return tamanho;
  }

  public void setTamanho(String tamanho) {
    this.tamanho = tamanho;
  }

  public Boolean getCriptografado() {
    return criptografado;
  }

  public void setCriptografado(Boolean criptografado) {
    this.criptografado = criptografado;
  }

  public Integer getRetencaoDias() {
    return retencaoDias;
  }

  public void setRetencaoDias(Integer retencaoDias) {
    this.retencaoDias = retencaoDias;
  }
}
