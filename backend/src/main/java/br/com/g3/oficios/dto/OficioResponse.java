package br.com.g3.oficios.dto;

import java.util.List;

public class OficioResponse {
  private Long id;
  private OficioIdentificacaoResponse identificacao;
  private OficioConteudoResponse conteudo;
  private OficioProtocoloResponse protocolo;
  private List<OficioTramiteResponse> tramites;
  private String pdfAssinadoNome;
  private String pdfAssinadoUrl;

  public OficioResponse(
      Long id,
      OficioIdentificacaoResponse identificacao,
      OficioConteudoResponse conteudo,
      OficioProtocoloResponse protocolo,
      List<OficioTramiteResponse> tramites,
      String pdfAssinadoNome,
      String pdfAssinadoUrl) {
    this.id = id;
    this.identificacao = identificacao;
    this.conteudo = conteudo;
    this.protocolo = protocolo;
    this.tramites = tramites;
    this.pdfAssinadoNome = pdfAssinadoNome;
    this.pdfAssinadoUrl = pdfAssinadoUrl;
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

  public String getPdfAssinadoNome() {
    return pdfAssinadoNome;
  }

  public String getPdfAssinadoUrl() {
    return pdfAssinadoUrl;
  }
}
