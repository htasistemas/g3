package br.com.g3.oficios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class OficioIdentificacaoRequest {
  @NotBlank
  private String tipo;

  @NotBlank
  private String numero;

  @NotNull
  private LocalDate data;

  @NotBlank
  private String setorOrigem;

  @NotBlank
  private String responsavel;

  @NotBlank
  private String destinatario;

  @NotBlank
  private String meioEnvio;

  private String prazoResposta;
  private String classificacao;

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public LocalDate getData() {
    return data;
  }

  public void setData(LocalDate data) {
    this.data = data;
  }

  public String getSetorOrigem() {
    return setorOrigem;
  }

  public void setSetorOrigem(String setorOrigem) {
    this.setorOrigem = setorOrigem;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public String getDestinatario() {
    return destinatario;
  }

  public void setDestinatario(String destinatario) {
    this.destinatario = destinatario;
  }

  public String getMeioEnvio() {
    return meioEnvio;
  }

  public void setMeioEnvio(String meioEnvio) {
    this.meioEnvio = meioEnvio;
  }

  public String getPrazoResposta() {
    return prazoResposta;
  }

  public void setPrazoResposta(String prazoResposta) {
    this.prazoResposta = prazoResposta;
  }

  public String getClassificacao() {
    return classificacao;
  }

  public void setClassificacao(String classificacao) {
    this.classificacao = classificacao;
  }
}
