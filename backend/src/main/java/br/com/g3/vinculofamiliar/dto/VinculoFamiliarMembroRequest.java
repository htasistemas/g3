package br.com.g3.vinculofamiliar.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class VinculoFamiliarMembroRequest {
  @JsonProperty("id_familia_membro")
  private Long idFamiliaMembro;

  @NotNull
  @JsonProperty("id_beneficiario")
  private Long idBeneficiario;

  @NotBlank
  @Size(max = 120)
  @JsonProperty("parentesco")
  private String parentesco;

  @JsonProperty("responsavel_familiar")
  private Boolean responsavelFamiliar;

  @JsonProperty("contribui_renda")
  private Boolean contribuiRenda;

  @JsonProperty("renda_individual")
  private String rendaIndividual;

  @JsonProperty("participa_servicos")
  private Boolean participaServicos;

  @JsonProperty("observacoes")
  private String observacoes;

  @JsonProperty("usa_endereco_familia")
  private Boolean usaEnderecoFamilia;

  public Long getIdFamiliaMembro() {
    return idFamiliaMembro;
  }

  public void setIdFamiliaMembro(Long idFamiliaMembro) {
    this.idFamiliaMembro = idFamiliaMembro;
  }

  public Long getIdBeneficiario() {
    return idBeneficiario;
  }

  public void setIdBeneficiario(Long idBeneficiario) {
    this.idBeneficiario = idBeneficiario;
  }

  public String getParentesco() {
    return parentesco;
  }

  public void setParentesco(String parentesco) {
    this.parentesco = parentesco;
  }

  public Boolean getResponsavelFamiliar() {
    return responsavelFamiliar;
  }

  public void setResponsavelFamiliar(Boolean responsavelFamiliar) {
    this.responsavelFamiliar = responsavelFamiliar;
  }

  public Boolean getContribuiRenda() {
    return contribuiRenda;
  }

  public void setContribuiRenda(Boolean contribuiRenda) {
    this.contribuiRenda = contribuiRenda;
  }

  public String getRendaIndividual() {
    return rendaIndividual;
  }

  public void setRendaIndividual(String rendaIndividual) {
    this.rendaIndividual = rendaIndividual;
  }

  public Boolean getParticipaServicos() {
    return participaServicos;
  }

  public void setParticipaServicos(Boolean participaServicos) {
    this.participaServicos = participaServicos;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public Boolean getUsaEnderecoFamilia() {
    return usaEnderecoFamilia;
  }

  public void setUsaEnderecoFamilia(Boolean usaEnderecoFamilia) {
    this.usaEnderecoFamilia = usaEnderecoFamilia;
  }
}
