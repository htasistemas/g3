package br.com.g3.oficios.dto;

import java.time.LocalDate;

public class OficioIdentificacaoResponse {
  private Long id;
  private String tipo;
  private String numero;
  private LocalDate data;
  private String setorOrigem;
  private String responsavel;
  private String destinatario;
  private String meioEnvio;
  private String prazoResposta;
  private String classificacao;

  public OficioIdentificacaoResponse(
      Long id,
      String tipo,
      String numero,
      LocalDate data,
      String setorOrigem,
      String responsavel,
      String destinatario,
      String meioEnvio,
      String prazoResposta,
      String classificacao) {
    this.id = id;
    this.tipo = tipo;
    this.numero = numero;
    this.data = data;
    this.setorOrigem = setorOrigem;
    this.responsavel = responsavel;
    this.destinatario = destinatario;
    this.meioEnvio = meioEnvio;
    this.prazoResposta = prazoResposta;
    this.classificacao = classificacao;
  }

  public Long getId() {
    return id;
  }

  public String getTipo() {
    return tipo;
  }

  public String getNumero() {
    return numero;
  }

  public LocalDate getData() {
    return data;
  }

  public String getSetorOrigem() {
    return setorOrigem;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public String getDestinatario() {
    return destinatario;
  }


  public String getMeioEnvio() {
    return meioEnvio;
  }

  public String getPrazoResposta() {
    return prazoResposta;
  }

  public String getClassificacao() {
    return classificacao;
  }
}
