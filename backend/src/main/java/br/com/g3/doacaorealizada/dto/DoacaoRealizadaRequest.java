package br.com.g3.doacaorealizada.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DoacaoRealizadaRequest {
  private Long beneficiarioId;
  private Long vinculoFamiliarId;

  @NotBlank
  private String tipoDoacao;

  @NotBlank
  private String situacao;

  private String responsavel;
  private String observacoes;

  @NotNull
  private LocalDate dataDoacao;

  @Valid
  private List<DoacaoRealizadaItemRequest> itens = new ArrayList<>();

  public Long getBeneficiarioId() {
    return beneficiarioId;
  }

  public void setBeneficiarioId(Long beneficiarioId) {
    this.beneficiarioId = beneficiarioId;
  }

  public Long getVinculoFamiliarId() {
    return vinculoFamiliarId;
  }

  public void setVinculoFamiliarId(Long vinculoFamiliarId) {
    this.vinculoFamiliarId = vinculoFamiliarId;
  }

  public String getTipoDoacao() {
    return tipoDoacao;
  }

  public void setTipoDoacao(String tipoDoacao) {
    this.tipoDoacao = tipoDoacao;
  }

  public String getSituacao() {
    return situacao;
  }

  public void setSituacao(String situacao) {
    this.situacao = situacao;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public LocalDate getDataDoacao() {
    return dataDoacao;
  }

  public void setDataDoacao(LocalDate dataDoacao) {
    this.dataDoacao = dataDoacao;
  }

  public List<DoacaoRealizadaItemRequest> getItens() {
    return itens;
  }

  public void setItens(List<DoacaoRealizadaItemRequest> itens) {
    this.itens = itens;
  }
}
