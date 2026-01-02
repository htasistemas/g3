package br.com.g3.fotoseventos.dto;

import java.util.List;

public class FotoEventoDetalheResponse {
  private final FotoEventoResponse evento;
  private final List<FotoEventoFotoResponse> fotos;

  public FotoEventoDetalheResponse(FotoEventoResponse evento, List<FotoEventoFotoResponse> fotos) {
    this.evento = evento;
    this.fotos = fotos;
  }

  public FotoEventoResponse getEvento() {
    return evento;
  }

  public List<FotoEventoFotoResponse> getFotos() {
    return fotos;
  }
}
