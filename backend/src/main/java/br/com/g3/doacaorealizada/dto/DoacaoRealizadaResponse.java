package br.com.g3.doacaorealizada.dto;

import java.time.LocalDate;
import java.util.List;

public class DoacaoRealizadaResponse {
  private final Long id;
  private final Long beneficiarioId;
  private final Long vinculoFamiliarId;
  private final String beneficiarioNome;
  private final String familiaNome;
  private final String tipoDoacao;
  private final String situacao;
  private final String responsavel;
  private final String observacoes;
  private final LocalDate dataDoacao;
  private final List<DoacaoRealizadaItemResponse> itens;

  public DoacaoRealizadaResponse(
      Long id,
      Long beneficiarioId,
      Long vinculoFamiliarId,
      String beneficiarioNome,
      String familiaNome,
      String tipoDoacao,
      String situacao,
      String responsavel,
      String observacoes,
      LocalDate dataDoacao,
      List<DoacaoRealizadaItemResponse> itens) {
    this.id = id;
    this.beneficiarioId = beneficiarioId;
    this.vinculoFamiliarId = vinculoFamiliarId;
    this.beneficiarioNome = beneficiarioNome;
    this.familiaNome = familiaNome;
    this.tipoDoacao = tipoDoacao;
    this.situacao = situacao;
    this.responsavel = responsavel;
    this.observacoes = observacoes;
    this.dataDoacao = dataDoacao;
    this.itens = itens;
  }

  public Long getId() {
    return id;
  }

  public Long getBeneficiarioId() {
    return beneficiarioId;
  }

  public Long getVinculoFamiliarId() {
    return vinculoFamiliarId;
  }

  public String getBeneficiarioNome() {
    return beneficiarioNome;
  }

  public String getFamiliaNome() {
    return familiaNome;
  }

  public String getTipoDoacao() {
    return tipoDoacao;
  }

  public String getSituacao() {
    return situacao;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public LocalDate getDataDoacao() {
    return dataDoacao;
  }

  public List<DoacaoRealizadaItemResponse> getItens() {
    return itens;
  }
}
