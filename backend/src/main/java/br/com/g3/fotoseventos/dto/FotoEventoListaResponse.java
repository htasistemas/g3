package br.com.g3.fotoseventos.dto;

import java.util.List;

public class FotoEventoListaResponse {
  private final List<FotoEventoResponse> eventos;
  private final int pagina;
  private final int tamanho;
  private final long total;
  private final int totalPaginas;

  public FotoEventoListaResponse(
      List<FotoEventoResponse> eventos,
      int pagina,
      int tamanho,
      long total,
      int totalPaginas) {
    this.eventos = eventos;
    this.pagina = pagina;
    this.tamanho = tamanho;
    this.total = total;
    this.totalPaginas = totalPaginas;
  }

  public List<FotoEventoResponse> getEventos() {
    return eventos;
  }

  public int getPagina() {
    return pagina;
  }

  public int getTamanho() {
    return tamanho;
  }

  public long getTotal() {
    return total;
  }

  public int getTotalPaginas() {
    return totalPaginas;
  }
}
