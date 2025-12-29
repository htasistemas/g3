package br.com.g3.vinculofamiliar.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class BeneficiarioResumoResponse {
  @JsonProperty("id_beneficiario")
  private final Long id;

  @JsonProperty("codigo")
  private final String codigo;

  @JsonProperty("nome_completo")
  private final String nomeCompleto;

  @JsonProperty("nome_social")
  private final String nomeSocial;

  @JsonProperty("cpf")
  private final String cpf;

  @JsonProperty("telefone")
  private final String telefone;

  @JsonProperty("bairro")
  private final String bairro;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("data_nascimento")
  private final LocalDate dataNascimento;

  public BeneficiarioResumoResponse(
      Long id,
      String codigo,
      String nomeCompleto,
      String nomeSocial,
      String cpf,
      String telefone,
      String bairro,
      LocalDate dataNascimento) {
    this.id = id;
    this.codigo = codigo;
    this.nomeCompleto = nomeCompleto;
    this.nomeSocial = nomeSocial;
    this.cpf = cpf;
    this.telefone = telefone;
    this.bairro = bairro;
    this.dataNascimento = dataNascimento;
  }

  public Long getId() {
    return id;
  }

  public String getCodigo() {
    return codigo;
  }

  public String getNomeCompleto() {
    return nomeCompleto;
  }

  public String getNomeSocial() {
    return nomeSocial;
  }

  public String getCpf() {
    return cpf;
  }

  public String getTelefone() {
    return telefone;
  }

  public String getBairro() {
    return bairro;
  }

  public LocalDate getDataNascimento() {
    return dataNascimento;
  }
}
