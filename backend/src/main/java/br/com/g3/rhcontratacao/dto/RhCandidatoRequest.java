package br.com.g3.rhcontratacao.dto;

import java.time.LocalDate;

public class RhCandidatoRequest {
  private String nomeCompleto;
  private String cpf;
  private String rg;
  private String pis;
  private LocalDate dataNascimento;
  private String naturalidade;
  private String estadoCivil;
  private String nomeMae;
  private String nomeConjuge;
  private String vagaPretendida;
  private LocalDate dataPreenchimento;
  private Boolean filhosPossui;
  private String filhosJson;
  private Boolean deficienciaPossui;
  private String deficienciaTipo;
  private String deficienciaDescricao;
  private String enderecoJson;
  private String telefone;
  private String whatsapp;
  private String anexosJson;
  private Boolean ativo;

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

  public String getRg() {
    return rg;
  }

  public void setRg(String rg) {
    this.rg = rg;
  }

  public String getPis() {
    return pis;
  }

  public void setPis(String pis) {
    this.pis = pis;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public String getNaturalidade() {
    return naturalidade;
  }

  public void setNaturalidade(String naturalidade) {
    this.naturalidade = naturalidade;
  }

  public String getEstadoCivil() {
    return estadoCivil;
  }

  public void setEstadoCivil(String estadoCivil) {
    this.estadoCivil = estadoCivil;
  }

  public String getNomeMae() {
    return nomeMae;
  }

  public void setNomeMae(String nomeMae) {
    this.nomeMae = nomeMae;
  }

  public String getNomeConjuge() {
    return nomeConjuge;
  }

  public void setNomeConjuge(String nomeConjuge) {
    this.nomeConjuge = nomeConjuge;
  }

  public String getVagaPretendida() {
    return vagaPretendida;
  }

  public void setVagaPretendida(String vagaPretendida) {
    this.vagaPretendida = vagaPretendida;
  }

  public LocalDate getDataPreenchimento() {
    return dataPreenchimento;
  }

  public void setDataPreenchimento(LocalDate dataPreenchimento) {
    this.dataPreenchimento = dataPreenchimento;
  }

  public Boolean getFilhosPossui() {
    return filhosPossui;
  }

  public void setFilhosPossui(Boolean filhosPossui) {
    this.filhosPossui = filhosPossui;
  }

  public String getFilhosJson() {
    return filhosJson;
  }

  public void setFilhosJson(String filhosJson) {
    this.filhosJson = filhosJson;
  }

  public Boolean getDeficienciaPossui() {
    return deficienciaPossui;
  }

  public void setDeficienciaPossui(Boolean deficienciaPossui) {
    this.deficienciaPossui = deficienciaPossui;
  }

  public String getDeficienciaTipo() {
    return deficienciaTipo;
  }

  public void setDeficienciaTipo(String deficienciaTipo) {
    this.deficienciaTipo = deficienciaTipo;
  }

  public String getDeficienciaDescricao() {
    return deficienciaDescricao;
  }

  public void setDeficienciaDescricao(String deficienciaDescricao) {
    this.deficienciaDescricao = deficienciaDescricao;
  }

  public String getEnderecoJson() {
    return enderecoJson;
  }

  public void setEnderecoJson(String enderecoJson) {
    this.enderecoJson = enderecoJson;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getWhatsapp() {
    return whatsapp;
  }

  public void setWhatsapp(String whatsapp) {
    this.whatsapp = whatsapp;
  }

  public String getAnexosJson() {
    return anexosJson;
  }

  public void setAnexosJson(String anexosJson) {
    this.anexosJson = anexosJson;
  }

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean ativo) {
    this.ativo = ativo;
  }
}
