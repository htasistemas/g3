package br.com.g3.vinculofamiliar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VinculoFamiliarMembroResponse {
  @JsonProperty("id_familia_membro")
  private final Long idFamiliaMembro;

  @JsonProperty("id_beneficiario")
  private final Long idBeneficiario;

  @JsonProperty("parentesco")
  private final String parentesco;

  @JsonProperty("responsavel_familiar")
  private final Boolean responsavelFamiliar;

  @JsonProperty("contribui_renda")
  private final Boolean contribuiRenda;

  @JsonProperty("renda_individual")
  private final String rendaIndividual;

  @JsonProperty("participa_servicos")
  private final Boolean participaServicos;

  @JsonProperty("observacoes")
  private final String observacoes;

  @JsonProperty("usa_endereco_familia")
  private final Boolean usaEnderecoFamilia;

  @JsonProperty("beneficiario")
  private final BeneficiarioResumoResponse beneficiario;

  public VinculoFamiliarMembroResponse(
      Long idFamiliaMembro,
      Long idBeneficiario,
      String parentesco,
      Boolean responsavelFamiliar,
      Boolean contribuiRenda,
      String rendaIndividual,
      Boolean participaServicos,
      String observacoes,
      Boolean usaEnderecoFamilia,
      BeneficiarioResumoResponse beneficiario) {
    this.idFamiliaMembro = idFamiliaMembro;
    this.idBeneficiario = idBeneficiario;
    this.parentesco = parentesco;
    this.responsavelFamiliar = responsavelFamiliar;
    this.contribuiRenda = contribuiRenda;
    this.rendaIndividual = rendaIndividual;
    this.participaServicos = participaServicos;
    this.observacoes = observacoes;
    this.usaEnderecoFamilia = usaEnderecoFamilia;
    this.beneficiario = beneficiario;
  }

  public Long getIdFamiliaMembro() {
    return idFamiliaMembro;
  }

  public Long getIdBeneficiario() {
    return idBeneficiario;
  }

  public String getParentesco() {
    return parentesco;
  }

  public Boolean getResponsavelFamiliar() {
    return responsavelFamiliar;
  }

  public Boolean getContribuiRenda() {
    return contribuiRenda;
  }

  public String getRendaIndividual() {
    return rendaIndividual;
  }

  public Boolean getParticipaServicos() {
    return participaServicos;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public Boolean getUsaEnderecoFamilia() {
    return usaEnderecoFamilia;
  }

  public BeneficiarioResumoResponse getBeneficiario() {
    return beneficiario;
  }
}
