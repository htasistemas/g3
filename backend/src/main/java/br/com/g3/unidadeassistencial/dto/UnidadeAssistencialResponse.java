package br.com.g3.unidadeassistencial.dto;

import java.util.List;

public class UnidadeAssistencialResponse {
  private final Long id;
  private final String nomeFantasia;
  private final String razaoSocial;
  private final String cnpj;
  private final String email;
  private final String telefone;
  private final String horarioFuncionamento;
  private final String observacoes;
  private final String logomarca;
  private final String logomarcaRelatorio;
  private final List<DiretoriaUnidadeResponse> diretoria;
  private final boolean unidadePrincipal;
  private final Long enderecoId;
  private final String cep;
  private final String endereco;
  private final String numeroEndereco;
  private final String complemento;
  private final String bairro;
  private final String pontoReferencia;
  private final String cidade;
  private final String zona;
  private final String subzona;
  private final String estado;
  private final String latitude;
  private final String longitude;

  public UnidadeAssistencialResponse(
      Long id,
      String nomeFantasia,
      String razaoSocial,
      String cnpj,
      String email,
      String telefone,
      String horarioFuncionamento,
      String observacoes,
      String logomarca,
      String logomarcaRelatorio,
      List<DiretoriaUnidadeResponse> diretoria,
      boolean unidadePrincipal,
      Long enderecoId,
      String cep,
      String endereco,
      String numeroEndereco,
      String complemento,
      String bairro,
      String pontoReferencia,
      String cidade,
      String zona,
      String subzona,
      String estado,
      String latitude,
      String longitude) {
    this.id = id;
    this.nomeFantasia = nomeFantasia;
    this.razaoSocial = razaoSocial;
    this.cnpj = cnpj;
    this.email = email;
    this.telefone = telefone;
    this.horarioFuncionamento = horarioFuncionamento;
    this.observacoes = observacoes;
    this.logomarca = logomarca;
    this.logomarcaRelatorio = logomarcaRelatorio;
    this.diretoria = diretoria;
    this.unidadePrincipal = unidadePrincipal;
    this.enderecoId = enderecoId;
    this.cep = cep;
    this.endereco = endereco;
    this.numeroEndereco = numeroEndereco;
    this.complemento = complemento;
    this.bairro = bairro;
    this.pontoReferencia = pontoReferencia;
    this.cidade = cidade;
    this.zona = zona;
    this.subzona = subzona;
    this.estado = estado;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public Long getId() {
    return id;
  }

  public String getNomeFantasia() {
    return nomeFantasia;
  }

  public String getRazaoSocial() {
    return razaoSocial;
  }

  public String getCnpj() {
    return cnpj;
  }

  public String getEmail() {
    return email;
  }

  public String getTelefone() {
    return telefone;
  }

  public String getHorarioFuncionamento() {
    return horarioFuncionamento;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public String getLogomarca() {
    return logomarca;
  }

  public String getLogomarcaRelatorio() {
    return logomarcaRelatorio;
  }

  public List<DiretoriaUnidadeResponse> getDiretoria() {
    return diretoria;
  }

  public boolean isUnidadePrincipal() {
    return unidadePrincipal;
  }

  public Long getEnderecoId() {
    return enderecoId;
  }

  public String getCep() {
    return cep;
  }

  public String getEndereco() {
    return endereco;
  }

  public String getNumeroEndereco() {
    return numeroEndereco;
  }

  public String getComplemento() {
    return complemento;
  }

  public String getBairro() {
    return bairro;
  }

  public String getPontoReferencia() {
    return pontoReferencia;
  }

  public String getCidade() {
    return cidade;
  }

  public String getZona() {
    return zona;
  }

  public String getSubzona() {
    return subzona;
  }

  public String getEstado() {
    return estado;
  }

  public String getLatitude() {
    return latitude;
  }

  public String getLongitude() {
    return longitude;
  }
}
