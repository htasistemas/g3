package br.com.g3.relatorios.dto;

public class EmprestimoEventoRelatorioRequest {
  private Long emprestimoId;
  private String usuarioEmissor;

  public Long getEmprestimoId() {
    return emprestimoId;
  }

  public void setEmprestimoId(Long emprestimoId) {
    this.emprestimoId = emprestimoId;
  }

  public String getUsuarioEmissor() {
    return usuarioEmissor;
  }

  public void setUsuarioEmissor(String usuarioEmissor) {
    this.usuarioEmissor = usuarioEmissor;
  }
}
