package br.com.g3.oficios.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class OficioRequest {
  @NotNull
  @Valid
  private OficioIdentificacaoRequest identificacao;

  @NotNull
  @Valid
  private OficioConteudoRequest conteudo;

  @NotNull
  @Valid
  private OficioProtocoloRequest protocolo;

  @Valid
  private List<OficioTramiteRequest> tramites;

  public OficioIdentificacaoRequest getIdentificacao() {
    return identificacao;
  }

  public void setIdentificacao(OficioIdentificacaoRequest identificacao) {
    this.identificacao = identificacao;
  }

  public OficioConteudoRequest getConteudo() {
    return conteudo;
  }

  public void setConteudo(OficioConteudoRequest conteudo) {
    this.conteudo = conteudo;
  }

  public OficioProtocoloRequest getProtocolo() {
    return protocolo;
  }

  public void setProtocolo(OficioProtocoloRequest protocolo) {
    this.protocolo = protocolo;
  }

  public List<OficioTramiteRequest> getTramites() {
    return tramites;
  }

  public void setTramites(List<OficioTramiteRequest> tramites) {
    this.tramites = tramites;
  }
}
