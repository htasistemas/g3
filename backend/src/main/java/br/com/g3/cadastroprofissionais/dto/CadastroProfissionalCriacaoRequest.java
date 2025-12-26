package br.com.g3.cadastroprofissionais.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public class CadastroProfissionalCriacaoRequest {
  @NotBlank
  @Size(max = 200)
  @JsonProperty("nome_completo")
  private String nomeCompleto;

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
