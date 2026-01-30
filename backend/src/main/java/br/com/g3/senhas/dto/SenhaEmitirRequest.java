package br.com.g3.senhas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public class SenhaEmitirRequest {
  @NotNull(message = "Beneficiario e obrigatorio.")
  private Long beneficiarioId;

  private Integer prioridade;
  private Long unidadeId;
  private Long usuarioId;
  @NotBlank(message = "Sala de atendimento e obrigatoria.")
  private String salaAtendimento;

  public Long getBeneficiarioId() {
    return beneficiarioId;
  }

  public void setBeneficiarioId(Long beneficiarioId) {
    this.beneficiarioId = beneficiarioId;
  }

  public Integer getPrioridade() {
    return prioridade;
  }

  public void setPrioridade(Integer prioridade) {
    this.prioridade = prioridade;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public void setUnidadeId(Long unidadeId) {
    this.unidadeId = unidadeId;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }

  public String getSalaAtendimento() {
    return salaAtendimento;
  }

  public void setSalaAtendimento(String salaAtendimento) {
    this.salaAtendimento = salaAtendimento;
  }
}
