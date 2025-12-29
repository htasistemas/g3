package br.com.g3.cadastroprofissionais.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CadastroProfissionalResponse {
  @JsonProperty("id_profissional")
  private final Long id;

  @JsonProperty("nome_completo")
  private final String nomeCompleto;

  @JsonProperty("cpf")
  private final String cpf;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("data_nascimento")
  private final LocalDate dataNascimento;

  @JsonProperty("foto_3x4")
  private final String foto3x4;

  @JsonProperty("sexo_biologico")
  private final String sexoBiologico;

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

  @JsonProperty("vinculo")
  private final String vinculo;

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

  @JsonProperty("categoria")
  private final String categoria;

  @JsonProperty("registro_conselho")
  private final String registroConselho;

  @JsonProperty("especialidade")
  private final String especialidade;

  @JsonProperty("email")
  private final String email;

  @JsonProperty("telefone")
  private final String telefone;

  @JsonProperty("unidade")
  private final String unidade;

  @JsonProperty("sala_atendimento")
  private final String salaAtendimento;

  @JsonProperty("carga_horaria")
  private final Integer cargaHoraria;

  @JsonProperty("disponibilidade")
  private final List<String> disponibilidade;

  @JsonProperty("canais_atendimento")
  private final List<String> canaisAtendimento;

  @JsonProperty("status")
  private final String status;

  @JsonProperty("tags")
  private final List<String> tags;

  @JsonProperty("resumo")
  private final String resumo;

  @JsonProperty("observacoes")
  private final String observacoes;

  @JsonProperty("data_cadastro")
  private final LocalDateTime dataCadastro;

  @JsonProperty("data_atualizacao")
  private final LocalDateTime dataAtualizacao;

  public CadastroProfissionalResponse(
      Long id,
      String nomeCompleto,
      String cpf,
      LocalDate dataNascimento,
      String foto3x4,
      String sexoBiologico,
      String estadoCivil,
      String nacionalidade,
      String naturalidadeCidade,
      String naturalidadeUf,
      String nomeMae,
      String vinculo,
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
      String categoria,
      String registroConselho,
      String especialidade,
      String email,
      String telefone,
      String unidade,
      String salaAtendimento,
      Integer cargaHoraria,
      List<String> disponibilidade,
      List<String> canaisAtendimento,
      String status,
      List<String> tags,
      String resumo,
      String observacoes,
      LocalDateTime dataCadastro,
      LocalDateTime dataAtualizacao) {
    this.id = id;
    this.nomeCompleto = nomeCompleto;
    this.cpf = cpf;
    this.dataNascimento = dataNascimento;
    this.foto3x4 = foto3x4;
    this.sexoBiologico = sexoBiologico;
    this.estadoCivil = estadoCivil;
    this.nacionalidade = nacionalidade;
    this.naturalidadeCidade = naturalidadeCidade;
    this.naturalidadeUf = naturalidadeUf;
    this.nomeMae = nomeMae;
    this.vinculo = vinculo;
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
    this.categoria = categoria;
    this.registroConselho = registroConselho;
    this.especialidade = especialidade;
    this.email = email;
    this.telefone = telefone;
    this.unidade = unidade;
    this.salaAtendimento = salaAtendimento;
    this.cargaHoraria = cargaHoraria;
    this.disponibilidade = disponibilidade;
    this.canaisAtendimento = canaisAtendimento;
    this.status = status;
    this.tags = tags;
    this.resumo = resumo;
    this.observacoes = observacoes;
    this.dataCadastro = dataCadastro;
    this.dataAtualizacao = dataAtualizacao;
  }

  public Long getId() {
    return id;
  }

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public String getCpf() {
    return cpf;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }

  public String getFoto3x4() {
    return foto3x4;
  }

  public String getSexoBiologico() {
    return sexoBiologico;
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

  public String getVinculo() {
    return vinculo;
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

  public String getCategoria() {
    return categoria;
  }

  public String getRegistroConselho() {
    return registroConselho;
  }

  public String getEspecialidade() {
    return especialidade;
  }

  public String getEmail() {
    return email;
  }

  public String getTelefone() {
    return telefone;
  }

  public String getUnidade() {
    return unidade;
  }

  public String getSalaAtendimento() {
    return salaAtendimento;
  }

  public Integer getCargaHoraria() {
    return cargaHoraria;
  }

  public List<String> getDisponibilidade() {
    return disponibilidade;
  }

  public List<String> getCanaisAtendimento() {
    return canaisAtendimento;
  }

  public String getStatus() {
    return status;
  }

  public List<String> getTags() {
    return tags;
  }

  public String getResumo() {
    return resumo;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public LocalDateTime getDataCadastro() {
    return dataCadastro;
  }

  public LocalDateTime getDataAtualizacao() {
    return dataAtualizacao;
  }
}
