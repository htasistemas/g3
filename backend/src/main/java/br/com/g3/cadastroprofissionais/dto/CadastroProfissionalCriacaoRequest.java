package br.com.g3.cadastroprofissionais.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public class CadastroProfissionalCriacaoRequest {
  @NotBlank
  @Size(max = 200)
  @JsonProperty("nome_completo")
  private String nomeCompleto;

  @Size(max = 20)
  @JsonProperty("cpf")
  private String cpf;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("data_nascimento")
  private LocalDate dataNascimento;

  @JsonProperty("foto_3x4")
  private String foto3x4;

  @Size(max = 60)
  @JsonProperty("sexo_biologico")
  private String sexoBiologico;

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

  @Size(max = 200)
  @JsonProperty("nome_mae")
  private String nomeMae;

  @Size(max = 40)
  @JsonProperty("vinculo")
  private String vinculo;

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

  @NotBlank
  @Size(max = 120)
  @JsonProperty("categoria")
  private String categoria;

  @Size(max = 120)
  @JsonProperty("registro_conselho")
  private String registroConselho;

  @Size(max = 120)
  @JsonProperty("especialidade")
  private String especialidade;

  @Size(max = 150)
  @JsonProperty("email")
  private String email;

  @Size(max = 30)
  @JsonProperty("telefone")
  private String telefone;

  @Size(max = 200)
  @JsonProperty("unidade")
  private String unidade;

  @JsonProperty("sala_atendimento")
  private String salaAtendimento;

  @JsonProperty("carga_horaria")
  private Integer cargaHoraria;

  @JsonProperty("disponibilidade")
  private List<String> disponibilidade;

  @JsonProperty("canais_atendimento")
  private List<String> canaisAtendimento;

  @Size(max = 60)
  @JsonProperty("status")
  private String status;

  @JsonProperty("tags")
  private List<String> tags;

  @JsonProperty("resumo")
  private String resumo;

  @JsonProperty("observacoes")
  private String observacoes;

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

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public void setDataNascimento(LocalDate dataNascimento) {
    this.dataNascimento = dataNascimento;
  }

  public String getFoto3x4() {
    return foto3x4;
  }

  public void setFoto3x4(String foto3x4) {
    this.foto3x4 = foto3x4;
  }

  public String getSexoBiologico() {
    return sexoBiologico;
  }

  public void setSexoBiologico(String sexoBiologico) {
    this.sexoBiologico = sexoBiologico;
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

  public String getVinculo() {
    return vinculo;
  }

  public void setVinculo(String vinculo) {
    this.vinculo = vinculo;
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

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public String getRegistroConselho() {
    return registroConselho;
  }

  public void setRegistroConselho(String registroConselho) {
    this.registroConselho = registroConselho;
  }

  public String getEspecialidade() {
    return especialidade;
  }

  public void setEspecialidade(String especialidade) {
    this.especialidade = especialidade;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getUnidade() {
    return unidade;
  }

  public void setUnidade(String unidade) {
    this.unidade = unidade;
  }

  public String getSalaAtendimento() {
    return salaAtendimento;
  }

  public void setSalaAtendimento(String salaAtendimento) {
    this.salaAtendimento = salaAtendimento;
  }

  public Integer getCargaHoraria() {
    return cargaHoraria;
  }

  public void setCargaHoraria(Integer cargaHoraria) {
    this.cargaHoraria = cargaHoraria;
  }

  public List<String> getDisponibilidade() {
    return disponibilidade;
  }

  public void setDisponibilidade(List<String> disponibilidade) {
    this.disponibilidade = disponibilidade;
  }

  public List<String> getCanaisAtendimento() {
    return canaisAtendimento;
  }

  public void setCanaisAtendimento(List<String> canaisAtendimento) {
    this.canaisAtendimento = canaisAtendimento;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public String getResumo() {
    return resumo;
  }

  public void setResumo(String resumo) {
    this.resumo = resumo;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }
}
