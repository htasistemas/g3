package br.com.g3.chamadas.fila.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class FilaAtualizacaoDto {
  @NotNull(message = "Beneficiario obrigatorio.")
  private Long idBeneficiario;

  @Min(value = 0, message = "Prioridade invalida.")
  private Integer prioridade;

  public Long getIdBeneficiario() {
    return idBeneficiario;
  }

  public void setIdBeneficiario(Long idBeneficiario) {
    this.idBeneficiario = idBeneficiario;
  }

  public Integer getPrioridade() {
    return prioridade;
  }

  public void setPrioridade(Integer prioridade) {
    this.prioridade = prioridade;
  }
}
