package br.com.g3.prontuario.dto;

import java.util.List;

public class ProntuarioRegistroListaResponse {
  private List<ProntuarioRegistroResponse> registros;
  private long total;
  private int pagina;
  private int tamanhoPagina;

  public ProntuarioRegistroListaResponse(List<ProntuarioRegistroResponse> registros, long total, int pagina, int tamanhoPagina) {
    this.registros = registros;
    this.total = total;
    this.pagina = pagina;
    this.tamanhoPagina = tamanhoPagina;
  }

  public List<ProntuarioRegistroResponse> getRegistros() {
    return registros;
  }

  public void setRegistros(List<ProntuarioRegistroResponse> registros) {
    this.registros = registros;
  }

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  public int getPagina() {
    return pagina;
  }

  public void setPagina(int pagina) {
    this.pagina = pagina;
  }

  public int getTamanhoPagina() {
    return tamanhoPagina;
  }

  public void setTamanhoPagina(int tamanhoPagina) {
    this.tamanhoPagina = tamanhoPagina;
  }
}
