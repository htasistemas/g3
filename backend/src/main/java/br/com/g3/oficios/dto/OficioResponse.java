package br.com.g3.oficios.dto;

import java.util.List;

public class OficioResponse {
  private Long id;
  private OficioIdentificacaoResponse identificacao;
  private OficioConteudoResponse conteudo;
  private OficioProtocoloResponse protocolo;
  private List<OficioTramiteResponse> tramites;

  public OficioResponse(
      Long id,
      OficioIdentificacaoResponse identificacao,
      OficioConteudoResponse conteudo,
      OficioProtocoloResponse protocolo,
      List<OficioTramiteResponse> tramites) {
    this.id = id;
    this.identificacao = identificacao;
    this.conteudo = conteudo;
    this.protocolo = protocolo;
    this.tramites = tramites;
  }

  public Long getId() {
    return id;
  }

  public OficioIdentificacaoResponse getIdentificacao() {
    return identificacao;
  }

  public OficioConteudoResponse getConteudo() {
    return conteudo;
  }

  public OficioProtocoloResponse getProtocolo() {
    return protocolo;
  }

  public List<OficioTramiteResponse> getTramites() {
    return tramites;
  }
}
