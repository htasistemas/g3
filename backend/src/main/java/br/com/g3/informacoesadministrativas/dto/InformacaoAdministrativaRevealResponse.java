package br.com.g3.informacoesadministrativas.dto;

import java.time.LocalDateTime;

public class InformacaoAdministrativaRevealResponse {
  private String segredo;
  private LocalDateTime expiraEm;

  public InformacaoAdministrativaRevealResponse(String segredo, LocalDateTime expiraEm) {
    this.segredo = segredo;
    this.expiraEm = expiraEm;
  }

  public String getSegredo() {
    return segredo;
  }

  public void setSegredo(String segredo) {
    this.segredo = segredo;
  }

  public LocalDateTime getExpiraEm() {
    return expiraEm;
  }

  public void setExpiraEm(LocalDateTime expiraEm) {
    this.expiraEm = expiraEm;
  }
}
