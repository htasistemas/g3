package br.com.g3.relatorios.dto;

public class CursoAtendimentoRelacaoRequest {
  private String nome;
  private String tipo;
  private String status;
  private String profissional;
  private Long salaId;
  private String usuarioEmissor;

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
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

  public String getProfissional() {
    return profissional;
  }

  public void setProfissional(String profissional) {
    this.profissional = profissional;
  }

  public Long getSalaId() {
    return salaId;
  }

  public void setSalaId(Long salaId) {
    this.salaId = salaId;
  }

  public String getUsuarioEmissor() {
    return usuarioEmissor;
  }

  public void setUsuarioEmissor(String usuarioEmissor) {
    this.usuarioEmissor = usuarioEmissor;
  }
}
