package br.com.g3.cadastrobeneficiario.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CadastroBeneficiarioCriacaoRequest {
  @NotBlank
  @Size(max = 200)
  @JsonProperty("nome_completo")
  private String nomeCompleto;

  @Size(max = 200)
  @JsonProperty("nome_social")
  private String nomeSocial;

  @Size(max = 120)
  @JsonProperty("apelido")
  private String apelido;

  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("data_nascimento")
  private LocalDate dataNascimento;

  @Size(max = 60)
  @JsonProperty("sexo_biologico")
  private String sexoBiologico;

  @Size(max = 120)
  @JsonProperty("identidade_genero")
  private String identidadeGenero;

  @Size(max = 60)
  @JsonProperty("cor_raca")
  private String corRaca;

  @Size(max = 60)
  @JsonProperty("estado_civil")
  private String estadoCivil;

  @Size(max = 120)
  @JsonProperty("nacionalidade")
  private String nacionalidade;

  @Size(max = 150)
  @JsonProperty("naturalidade_cidade")
  private String naturalidadeCidade;

  @Size(max = 2)
  @JsonProperty("naturalidade_uf")
  private String naturalidadeUf;

  @NotBlank
  @Size(max = 200)
  @JsonProperty("nome_mae")
  private String nomeMae;

  @Size(max = 200)
  @JsonProperty("nome_pai")
  private String nomePai;

  @Size(max = 20)
  @JsonProperty("cep")
  private String cep;

  @Size(max = 200)
  @JsonProperty("logradouro")
  private String logradouro;

  @Size(max = 20)
  @JsonProperty("numero")
  private String numero;

  @Size(max = 150)
  @JsonProperty("complemento")
  private String complemento;

  @Size(max = 150)
  @JsonProperty("bairro")
  private String bairro;

  @Size(max = 200)
  @JsonProperty("ponto_referencia")
  private String pontoReferencia;

  @Size(max = 150)
  @JsonProperty("municipio")
  private String municipio;

  @Size(max = 60)
  @JsonProperty("zona")
  private String zona;

  @Size(max = 60)
  @JsonProperty("subzona")
  private String subzona;

  @Size(max = 2)
  @JsonProperty("uf")
  private String uf;

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public void setNomeCompleto(String nomeCompleto) {
    this.nomeCompleto = nomeCompleto;
  }

  public String getNomeSocial() {
    return nomeSocial;
  }

  public void setNomeSocial(String nomeSocial) {
    this.nomeSocial = nomeSocial;
  }

  public String getApelido() {
    return apelido;
  }

  public void setApelido(String apelido) {
    this.apelido = apelido;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public String getSexoBiologico() {
    return sexoBiologico;
  }

  public void setSexoBiologico(String sexoBiologico) {
    this.sexoBiologico = sexoBiologico;
  }

  public String getIdentidadeGenero() {
    return identidadeGenero;
  }

  public void setIdentidadeGenero(String identidadeGenero) {
    this.identidadeGenero = identidadeGenero;
  }

  public String getCorRaca() {
    return corRaca;
  }

  public void setCorRaca(String corRaca) {
    this.corRaca = corRaca;
  }

  public String getEstadoCivil() {
    return estadoCivil;
  }

  public void setEstadoCivil(String estadoCivil) {
    this.estadoCivil = estadoCivil;
  }

  public String getNacionalidade() {
    return nacionalidade;
  }

  public void setNacionalidade(String nacionalidade) {
    this.nacionalidade = nacionalidade;
  }

  public String getNaturalidadeCidade() {
    return naturalidadeCidade;
  }

  public void setNaturalidadeCidade(String naturalidadeCidade) {
    this.naturalidadeCidade = naturalidadeCidade;
  }

  public String getNaturalidadeUf() {
    return naturalidadeUf;
  }

  public void setNaturalidadeUf(String naturalidadeUf) {
    this.naturalidadeUf = naturalidadeUf;
  }

  public String getNomeMae() {
    return nomeMae;
  }

  public void setNomeMae(String nomeMae) {
    this.nomeMae = nomeMae;
  }

  public String getNomePai() {
    return nomePai;
  }

  public void setNomePai(String nomePai) {
    this.nomePai = nomePai;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public String getLogradouro() {
    return logradouro;
  }

  public void setLogradouro(String logradouro) {
    this.logradouro = logradouro;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
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

  public String getMunicipio() {
    return municipio;
  }

  public void setMunicipio(String municipio) {
    this.municipio = municipio;
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

  public String getUf() {
    return uf;
  }

  public void setUf(String uf) {
    this.uf = uf;
  }
}
