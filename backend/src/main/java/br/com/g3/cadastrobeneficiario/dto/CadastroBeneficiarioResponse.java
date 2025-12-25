package br.com.g3.cadastrobeneficiario.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CadastroBeneficiarioResponse {
  @JsonProperty("id_beneficiario")
  private final Long id;

  @JsonProperty("nome_completo")
  private final String nomeCompleto;

  @JsonProperty("nome_social")
  private final String nomeSocial;

  @JsonProperty("apelido")
  private final String apelido;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("data_nascimento")
  private final LocalDate dataNascimento;

  @JsonProperty("sexo_biologico")
  private final String sexoBiologico;

  @JsonProperty("identidade_genero")
  private final String identidadeGenero;

  @JsonProperty("cor_raca")
  private final String corRaca;

  @JsonProperty("estado_civil")
  private final String estadoCivil;

  @JsonProperty("nacionalidade")
  private final String nacionalidade;

  @JsonProperty("naturalidade_cidade")
  private final String naturalidadeCidade;

  @JsonProperty("naturalidade_uf")
  private final String naturalidadeUf;

  @JsonProperty("nome_mae")
  private final String nomeMae;

  @JsonProperty("nome_pai")
  private final String nomePai;

  @JsonProperty("cep")
  private final String cep;

  @JsonProperty("logradouro")
  private final String logradouro;

  @JsonProperty("numero")
  private final String numero;

  @JsonProperty("complemento")
  private final String complemento;

  @JsonProperty("bairro")
  private final String bairro;

  @JsonProperty("ponto_referencia")
  private final String pontoReferencia;

  @JsonProperty("municipio")
  private final String municipio;

  @JsonProperty("zona")
  private final String zona;

  @JsonProperty("subzona")
  private final String subzona;

  @JsonProperty("uf")
  private final String uf;

  @JsonProperty("data_cadastro")
  private final LocalDateTime dataCadastro;

  @JsonProperty("data_atualizacao")
  private final LocalDateTime dataAtualizacao;

  public CadastroBeneficiarioResponse(
      Long id,
      String nomeCompleto,
      String nomeSocial,
      String apelido,
      LocalDate dataNascimento,
      String sexoBiologico,
      String identidadeGenero,
      String corRaca,
      String estadoCivil,
      String nacionalidade,
      String naturalidadeCidade,
      String naturalidadeUf,
      String nomeMae,
      String nomePai,
      String cep,
      String logradouro,
      String numero,
      String complemento,
      String bairro,
      String pontoReferencia,
      String municipio,
      String zona,
      String subzona,
      String uf,
      LocalDateTime dataCadastro,
      LocalDateTime dataAtualizacao) {
    this.id = id;
    this.nomeCompleto = nomeCompleto;
    this.nomeSocial = nomeSocial;
    this.apelido = apelido;
    this.dataNascimento = dataNascimento;
    this.sexoBiologico = sexoBiologico;
    this.identidadeGenero = identidadeGenero;
    this.corRaca = corRaca;
    this.estadoCivil = estadoCivil;
    this.nacionalidade = nacionalidade;
    this.naturalidadeCidade = naturalidadeCidade;
    this.naturalidadeUf = naturalidadeUf;
    this.nomeMae = nomeMae;
    this.nomePai = nomePai;
    this.cep = cep;
    this.logradouro = logradouro;
    this.numero = numero;
    this.complemento = complemento;
    this.bairro = bairro;
    this.pontoReferencia = pontoReferencia;
    this.municipio = municipio;
    this.zona = zona;
    this.subzona = subzona;
    this.uf = uf;
    this.dataCadastro = dataCadastro;
    this.dataAtualizacao = dataAtualizacao;
  }

  public Long getId() {
    return id;
  }

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public String getNomeSocial() {
    return nomeSocial;
  }

  public String getApelido() {
    return apelido;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public String getSexoBiologico() {
    return sexoBiologico;
  }

  public String getIdentidadeGenero() {
    return identidadeGenero;
  }

  public String getCorRaca() {
    return corRaca;
  }

  public String getEstadoCivil() {
    return estadoCivil;
  }

  public String getNacionalidade() {
    return nacionalidade;
  }

  public String getNaturalidadeCidade() {
    return naturalidadeCidade;
  }

  public String getNaturalidadeUf() {
    return naturalidadeUf;
  }

  public String getNomeMae() {
    return nomeMae;
  }

  public String getNomePai() {
    return nomePai;
  }

  public String getCep() {
    return cep;
  }

  public String getLogradouro() {
    return logradouro;
  }

  public String getNumero() {
    return numero;
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

  public String getMunicipio() {
    return municipio;
  }

  public String getZona() {
    return zona;
  }

  public String getSubzona() {
    return subzona;
  }

  public String getUf() {
    return uf;
  }

  public LocalDateTime getDataCadastro() {
    return dataCadastro;
  }

  public LocalDateTime getDataAtualizacao() {
    return dataAtualizacao;
  }
}
