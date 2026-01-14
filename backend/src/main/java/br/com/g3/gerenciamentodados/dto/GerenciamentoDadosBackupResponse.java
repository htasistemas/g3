package br.com.g3.gerenciamentodados.dto;

import java.time.LocalDateTime;

public class GerenciamentoDadosBackupResponse {
  private Long id;
  private String codigo;
  private String rotulo;
  private String tipo;
  private String status;
  private LocalDateTime iniciadoEm;
  private String armazenadoEm;
  private String tamanho;
  private Boolean criptografado;
  private Integer retencaoDias;

  public GerenciamentoDadosBackupResponse() {}

  public GerenciamentoDadosBackupResponse(
      Long id,
      String codigo,
      String rotulo,
      String tipo,
      String status,
      LocalDateTime iniciadoEm,
      String armazenadoEm,
      String tamanho,
      Boolean criptografado,
      Integer retencaoDias) {
    this.id = id;
    this.codigo = codigo;
    this.rotulo = rotulo;
    this.tipo = tipo;
    this.status = status;
    this.iniciadoEm = iniciadoEm;
    this.armazenadoEm = armazenadoEm;
    this.tamanho = tamanho;
    this.criptografado = criptografado;
    this.retencaoDias = retencaoDias;
  }

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

  public LocalDateTime getIniciadoEm() {
    return iniciadoEm;
  }

  public void setIniciadoEm(LocalDateTime iniciadoEm) {
    this.iniciadoEm = iniciadoEm;
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
