package br.com.g3.cursosatendimentos.dto;

public class CursoAtendimentoPresencaItemResponse {
  private String matriculaId;
  private String status;

  public String getMatriculaId() {
    return matriculaId;
  }

  public void setMatriculaId(String matriculaId) {
    this.matriculaId = matriculaId;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
