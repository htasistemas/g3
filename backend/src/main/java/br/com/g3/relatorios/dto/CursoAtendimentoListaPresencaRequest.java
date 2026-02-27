package br.com.g3.relatorios.dto;

import java.time.LocalDate;

public class CursoAtendimentoListaPresencaRequest {
  private Long cursoId;
  private LocalDate dataAula;
  private String usuarioEmissor;
  private Boolean exibirCpf;

  public Long getCursoId() {
    return cursoId;
  }

  public void setCursoId(Long cursoId) {
    this.cursoId = cursoId;
  }

  public LocalDate getDataAula() {
    return dataAula;
  }

  public void setDataAula(LocalDate dataAula) {
    this.dataAula = dataAula;
  }

  public String getUsuarioEmissor() {
    return usuarioEmissor;
  }

  public void setUsuarioEmissor(String usuarioEmissor) {
    this.usuarioEmissor = usuarioEmissor;
  }

  public Boolean getExibirCpf() {
    return exibirCpf;
  }

  public void setExibirCpf(Boolean exibirCpf) {
    this.exibirCpf = exibirCpf;
  }
}
