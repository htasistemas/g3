package br.com.g3.relatorios.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TermoAutorizacaoRequest {
  @JsonProperty("beneficiarioNome")
  private String beneficiarioNome;

  @JsonProperty("rg")
  private String rg;

  @JsonProperty("cpf")
  private String cpf;

  @JsonProperty("enderecoCompleto")
  private String enderecoCompleto;

  @JsonProperty("cidade")
  private String cidade;

  @JsonProperty("uf")
  private String uf;

  @JsonProperty("finalidadeDados")
  private String finalidadeDados;

  @JsonProperty("finalidadeImagem")
  private String finalidadeImagem;

  @JsonProperty("vigencia")
  private String vigencia;

  @JsonProperty("localAssinatura")
  private String localAssinatura;

  @JsonProperty("dataAssinatura")
  private String dataAssinatura;

  @JsonProperty("responsavelNome")
  private String responsavelNome;

  @JsonProperty("responsavelCpf")
  private String responsavelCpf;

  @JsonProperty("responsavelRelacao")
  private String responsavelRelacao;

  @JsonProperty("representanteNome")
  private String representanteNome;

  @JsonProperty("representanteCargo")
  private String representanteCargo;

  @JsonProperty("issuedBy")
  private String issuedBy;

  public String getBeneficiarioNome() {
    return beneficiarioNome;
  }

  public void setBeneficiarioNome(String beneficiarioNome) {
    this.beneficiarioNome = beneficiarioNome;
  }

  public String getRg() {
    return rg;
  }

  public void setRg(String rg) {
    this.rg = rg;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getEnderecoCompleto() {
    return enderecoCompleto;
  }

  public void setEnderecoCompleto(String enderecoCompleto) {
    this.enderecoCompleto = enderecoCompleto;
  }

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public String getUf() {
    return uf;
  }

  public void setUf(String uf) {
    this.uf = uf;
  }

  public String getFinalidadeDados() {
    return finalidadeDados;
  }

  public void setFinalidadeDados(String finalidadeDados) {
    this.finalidadeDados = finalidadeDados;
  }

  public String getFinalidadeImagem() {
    return finalidadeImagem;
  }

  public void setFinalidadeImagem(String finalidadeImagem) {
    this.finalidadeImagem = finalidadeImagem;
  }

  public String getVigencia() {
    return vigencia;
  }

  public void setVigencia(String vigencia) {
    this.vigencia = vigencia;
  }

  public String getLocalAssinatura() {
    return localAssinatura;
  }

  public void setLocalAssinatura(String localAssinatura) {
    this.localAssinatura = localAssinatura;
  }

  public String getDataAssinatura() {
    return dataAssinatura;
  }

  public void setDataAssinatura(String dataAssinatura) {
    this.dataAssinatura = dataAssinatura;
  }

  public String getResponsavelNome() {
    return responsavelNome;
  }

  public void setResponsavelNome(String responsavelNome) {
    this.responsavelNome = responsavelNome;
  }

  public String getResponsavelCpf() {
    return responsavelCpf;
  }

  public void setResponsavelCpf(String responsavelCpf) {
    this.responsavelCpf = responsavelCpf;
  }

  public String getResponsavelRelacao() {
    return responsavelRelacao;
  }

  public void setResponsavelRelacao(String responsavelRelacao) {
    this.responsavelRelacao = responsavelRelacao;
  }

  public String getRepresentanteNome() {
    return representanteNome;
  }

  public void setRepresentanteNome(String representanteNome) {
    this.representanteNome = representanteNome;
  }

  public String getRepresentanteCargo() {
    return representanteCargo;
  }

  public void setRepresentanteCargo(String representanteCargo) {
    this.representanteCargo = representanteCargo;
  }

  public String getIssuedBy() {
    return issuedBy;
  }

  public void setIssuedBy(String issuedBy) {
    this.issuedBy = issuedBy;
  }
}
