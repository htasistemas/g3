package br.com.g3.rhcontratacao.dto;

public class RhCandidatoResumoResponse {
  private Long candidatoId;
  private Long processoId;
  private String nomeCompleto;
  private String cpf;
  private String vagaPretendida;
  private String status;
  private Boolean ativo;

  public Long getCandidatoId() {
    return candidatoId;
  }

  public void setCandidatoId(Long candidatoId) {
    this.candidatoId = candidatoId;
  }

  public Long getProcessoId() {
    return processoId;
  }

  public void setProcessoId(Long processoId) {
    this.processoId = processoId;
  }

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public void setNomeCompleto(String nomeCompleto) {
    this.nomeCompleto = nomeCompleto;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getVagaPretendida() {
    return vagaPretendida;
  }

  public void setVagaPretendida(String vagaPretendida) {
    this.vagaPretendida = vagaPretendida;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean ativo) {
    this.ativo = ativo;
  }
}
