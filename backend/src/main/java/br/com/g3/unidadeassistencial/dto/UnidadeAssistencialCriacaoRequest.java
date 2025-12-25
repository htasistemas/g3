package br.com.g3.unidadeassistencial.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public class UnidadeAssistencialCriacaoRequest {
  @NotBlank
  @Size(max = 200)
  private String nomeFantasia;

  @Size(max = 200)
  private String razaoSocial;

  @Size(max = 20)
  private String cnpj;

  @Size(max = 150)
  private String email;

  @Size(max = 20)
  private String cep;

  @Size(max = 200)
  private String endereco;

  @Size(max = 20)
  private String numeroEndereco;

  @Size(max = 150)
  private String complemento;

  @Size(max = 150)
  private String bairro;

  @Size(max = 200)
  private String pontoReferencia;

  @Size(max = 150)
  private String cidade;

  @Size(max = 60)
  private String zona;

  @Size(max = 60)
  private String subzona;

  @Size(max = 2)
  private String estado;

  @Size(max = 30)
  private String telefone;

  @Size(max = 120)
  private String horarioFuncionamento;

  private String observacoes;

  private String logomarca;

  private String logomarcaRelatorio;

  @Valid
  private List<DiretoriaUnidadeRequest> diretoria;

  private boolean unidadePrincipal;

  public String getNomeFantasia() {
    return nomeFantasia;
  }

  public void setNomeFantasia(String nomeFantasia) {
    this.nomeFantasia = nomeFantasia;
  }

  public String getRazaoSocial() {
    return razaoSocial;
  }

  public void setRazaoSocial(String razaoSocial) {
    this.razaoSocial = razaoSocial;
  }

  public String getCnpj() {
    return cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public String getEndereco() {
    return endereco;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public String getNumeroEndereco() {
    return numeroEndereco;
  }

  public void setNumeroEndereco(String numeroEndereco) {
    this.numeroEndereco = numeroEndereco;
  }

  public String getComplemento() {
    return complemento;
  }

  public void setComplemento(String complemento) {
    this.complemento = complemento;
  }

  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getPontoReferencia() {
    return pontoReferencia;
  }

  public void setPontoReferencia(String pontoReferencia) {
    this.pontoReferencia = pontoReferencia;
  }

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public String getZona() {
    return zona;
  }

  public void setZona(String zona) {
    this.zona = zona;
  }

  public String getSubzona() {
    return subzona;
  }

  public void setSubzona(String subzona) {
    this.subzona = subzona;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getHorarioFuncionamento() {
    return horarioFuncionamento;
  }

  public void setHorarioFuncionamento(String horarioFuncionamento) {
    this.horarioFuncionamento = horarioFuncionamento;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public String getLogomarca() {
    return logomarca;
  }

  public void setLogomarca(String logomarca) {
    this.logomarca = logomarca;
  }

  public String getLogomarcaRelatorio() {
    return logomarcaRelatorio;
  }

  public void setLogomarcaRelatorio(String logomarcaRelatorio) {
    this.logomarcaRelatorio = logomarcaRelatorio;
  }

  public List<DiretoriaUnidadeRequest> getDiretoria() {
    return diretoria;
  }

  public void setDiretoria(List<DiretoriaUnidadeRequest> diretoria) {
    this.diretoria = diretoria;
  }

  public boolean isUnidadePrincipal() {
    return unidadePrincipal;
  }

  public void setUnidadePrincipal(boolean unidadePrincipal) {
    this.unidadePrincipal = unidadePrincipal;
  }
}
