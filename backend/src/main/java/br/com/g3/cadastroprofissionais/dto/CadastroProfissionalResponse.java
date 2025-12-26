package br.com.g3.cadastroprofissionais.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;

public class CadastroProfissionalResponse {
  @JsonProperty("id_profissional")
  private final Long id;

  @JsonProperty("nome_completo")
  private final String nomeCompleto;

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
      String categoria,
      String registroConselho,
      String especialidade,
      String email,
      String telefone,
      String unidade,
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
    this.categoria = categoria;
    this.registroConselho = registroConselho;
    this.especialidade = especialidade;
    this.email = email;
    this.telefone = telefone;
    this.unidade = unidade;
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
